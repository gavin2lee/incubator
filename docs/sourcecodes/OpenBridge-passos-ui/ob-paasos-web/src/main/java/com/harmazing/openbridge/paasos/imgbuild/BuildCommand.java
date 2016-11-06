package com.harmazing.openbridge.paasos.imgbuild;

import io.fabric8.docker.client.DockerClient;
import io.fabric8.docker.dsl.EventListener;
import io.fabric8.docker.dsl.OutputHandle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.ZipUtil;
import com.harmazing.openbridge.paasos.imgbuild.log.LogManager;
import com.harmazing.openbridge.paasos.imgbuild.log.LogProvider;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;

public class BuildCommand extends AbstractDockerCommand implements LogProvider {
	private StringBuffer result = new StringBuffer();
	
	private String commandId;
	
	@Override
	public String getResult() {
		return result.toString();
	}

	
	public BuildCommand(String id){
		this.commandId = id;
		LogManager.add(id, this);
	}
	
	
	public void execute(BuildTask buildTask) {
		// 开始构建
		StringBuffer filePath = new StringBuffer();
		try {
			//不要用返回值了  在build失败的时候 没有返回build  已经创建文件了
			// docker build
			build(buildTask, result,filePath);
			// docker push
			pushImage(buildTask, result);
			// insert table
			insertImage(buildTask, result);
			buildTask.setBuildStatus(0);
		} catch (Exception e) {
			buildTask.setBuildStatus(-1);
			/*buildTask.setBuildLogs(result.toString());
			buildTask.setBuildDate(new Date());
			throw new AsyncException(buildTask, e);*/
		}
		finally{
			//删除中间产生的文件
			String p = filePath.toString();
			if(StringUtils.hasText(p)){
				File f = new File(p);
				if(f.exists()){
					try {
						logger.debug("-delete file--->"+f.getAbsolutePath());
						FileUtils.deleteDirectory(f);
					} catch (IOException e) {
						//删失败就不管了
					}
				}
			}
		}
		buildTask.setBuildLogs(commandId);
		buildTask.setBuildDate(new Date());
		LogManager.end(this.commandId);//日志结束
	}
	

	private String[] getImageName(BuildTask buildTask) {
		String imageName = null;
		String dockerRegistry = ConfigUtil
				.getConfigString("paasos.docker.registry");
		if (dockerRegistry.endsWith("/")) {
			dockerRegistry = dockerRegistry.substring(0,
					dockerRegistry.length() - 1);
		}
		imageName = buildTask.getImageName();
		if (imageName.startsWith("/")) {
			imageName = imageName.substring(1, imageName.length());
		}
		imageName = dockerRegistry + "/" + imageName;
		return new String[] { imageName, buildTask.getImageVersion() };
	}

	public void insertImage(BuildTask buildTask, StringBuffer result) {
		String[] imageName = getImageName(buildTask);
		boolean isExist = true;
		PaasOsImage images = geImageService().findImageByName(imageName[0]+":"+imageName[1],
				imageName[1]);
		if (images == null) {
			isExist = false;
			images = new PaasOsImage();
			images.setImageId(StringUtil.getUUID());
		}
		images.setArgs(buildTask.getImageArgs());
		images.setBuildStatus(0 + "");
		images.setCommand(buildTask.getImageCommand());
		images.setImageName(imageName[0] + ":" + imageName[1]);
		images.setImageVersion(buildTask.getImageVersion());
		images.setPorts(buildTask.getImagePort());
		images.setWorkdir(buildTask.getImageWorkdir());

		if (isExist)
			geImageService().update(images);
		else
			geImageService().insert(images);

		buildTask.setBuildImageId(images.getImageId());
	}

	public String build(BuildTask buildTask, StringBuffer result,StringBuffer filePath) {
		String localPath = ConfigUtil.getConfigString("file.storage");
		if (localPath.endsWith(File.separator)) {
			localPath = localPath.substring(0, localPath.length() - 1);
		}
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		localPath = localPath + File.separator + "build"+ File.separator + sdf.format(new Date()) + File.separator
				+ buildTask.getTaskId();
		File targetDir = new File(localPath);
		if (targetDir.exists()) {
			try {
				FileUtils.deleteDirectory(targetDir);
			} catch (IOException e) {
				result.append("Clean Directory Error\n");
				throw new RuntimeException();
			}
		}
		if (!targetDir.mkdirs()) {
			logger.debug("-create file--->"+targetDir.getAbsolutePath());
			result.append("Create Directory Error\n");
			throw new RuntimeException();
		}
		filePath.append(localPath);
		if (buildTask.getTaskType() == BuildTaskType.ZIP) {
			try {
				ZipUtil.unZip(buildTask.getFilePath(), localPath);
				File file = new File(localPath + "/Dockerfile");
				if(StringUtils.hasText(buildTask.getDockerfile())){
					if(!file.exists()){
						file.createNewFile();
					}
					FileUtils.write(file, buildTask.getDockerfile(), "utf-8");
				}else{
					if (!file.exists()) {
						result.append(buildTask.getFilePath()
								+ "/Dockerfile No Found\n");
						throw new RuntimeException();
					}
				}
			} catch (Exception e) {
				result.append("unzip error\n");
				throw new RuntimeException();
			}
			finally{
				if(StringUtils.hasText(buildTask.getFilePath()) && StringUtils.hasText(buildTask.getProjectType())){
					if("api".equals(buildTask.getProjectType()) || "app".equals(buildTask.getProjectType())){
						File f = new File(buildTask.getFilePath());
						if(f!=null && f.exists()){
							try {
								logger.debug("-delete file--->"+f.getAbsolutePath());
								FileUtils.forceDelete(f);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}

		} else if (buildTask.getTaskType() == BuildTaskType.TARGZ) {
			// TARGZ不需要处理
		} else {
			result.append("File Type Error \n");
			throw new RuntimeException();
		}

		if (buildTask.getTaskType() == BuildTaskType.ZIP) {
			buildImage(buildTask, localPath, result);
		} else if (buildTask.getTaskType() == BuildTaskType.TARGZ) {
			loadImage(buildTask, result);
		}
		return localPath;
	}

	public void pushImage(BuildTask buildTask, final StringBuffer result) {
		// 上传镜像
		String[] imageName = getImageName(buildTask);

		result.append("Docker Push.\n");
		final java.util.List<Boolean> isOk = new ArrayList<Boolean>();
		DockerClient client = getDefaultDockerClient();
		final CountDownLatch pushDone = new CountDownLatch(1);
		OutputHandle handle = null;
		try {
			handle = client.image().withName(imageName[0]).push()
					.usingListener(new EventListener() {
						@Override
						public void onSuccess(String message) {
							result.append("Success:" + message + "\n");
							pushDone.countDown();
						}

						@Override
						public void onError(String messsage) {
							result.append("Failure:" + messsage + "\n");
							isOk.add(false);
							pushDone.countDown();
						}

						@Override
						public void onEvent(String event) {
							result.append(event + "\n");
						}
					}).withTag(imageName[1]).toRegistry();

			pushDone.await();
			if (isOk.size() > 0 && isOk.get(0) == false) {
				result.append("Push Failure.\n");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			if(StringUtil.isNotNull(e.getMessage())){
				result.append(e.getMessage() + "\n");
			}
			throw new RuntimeException();
		} finally {
			try {
				handle.close();
			} catch (Exception e2) {
			}
			try {
				client.close();
			} catch (Exception e2) {
			}
		}

		buildTask.setBuildImageName(imageName[0] + ":" + imageName[1]);
	}

	public void buildImage(BuildTask buildTask, String targetDir,
			final StringBuffer result) {
		String imageName[] = getImageName(buildTask);
		result.append("Build Start.\n");
		File file = new File(targetDir);
		if (!file.exists()) {
			result.append("File Not Exists:" + targetDir + "");
			throw new RuntimeException();
		}
		final java.util.List<Boolean> isOk = new ArrayList<Boolean>();
		final CountDownLatch buildDone = new CountDownLatch(1);
		DockerClient client = getDefaultDockerClient();
		OutputHandle handle = null;
		try {
			result.append("Upload Dockerfile.\n");
			handle = client.image().build()
					.withRepositoryName(imageName[0] + ":" + imageName[1])
					.usingListener(new EventListener() {
						@Override
						public void onSuccess(String message) {
							result.append("Success:" + message + "\n");
							buildDone.countDown();
						}

						@Override
						public void onError(String messsage) {
							result.append("Failure:" + messsage + "\n");
							isOk.add(false);
							buildDone.countDown();
						}

						@Override
						public void onEvent(String event) {
							result.append(event);
						}
					}).fromFolder(targetDir);
			result.append("Uploading.\n");
			buildDone.await();
			if (isOk.size() > 0 && isOk.get(0) == false) {
				result.append("Build Failure.\n");
				throw new RuntimeException();
			}
		} catch (Exception e) {
			result.append(e.getMessage() != null ? e.getMessage() : "");
			throw new RuntimeException();
		} finally {
			try {
				handle.close();
			} catch (IOException e1) {
			}
			try {
				client.close();
			} catch (IOException e) {
			}
		}
	}

	public void loadImage(BuildTask buildTask, StringBuffer result) {
		String[] imageName = getImageName(buildTask);
		result.append("Start Import.\n");
		DockerClient client = getDefaultDockerClient();
		boolean isOk = false;
		try {
			isOk = client.image().load(
					new FileInputStream(buildTask.getFilePath()));
			if (isOk == false) {
				throw new RuntimeException();
			}
			result.append("Import Success.\n");
		} catch (FileNotFoundException e) {
			result.append(buildTask.getFilePath() + " Not Found.\n");
			throw new RuntimeException();
		} catch (Exception e) {
			result.append(e.getMessage() != null ? e.getMessage() : "");
			throw new RuntimeException();
		}

	}
}
