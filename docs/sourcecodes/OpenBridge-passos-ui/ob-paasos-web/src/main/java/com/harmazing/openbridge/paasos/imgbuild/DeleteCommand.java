package com.harmazing.openbridge.paasos.imgbuild;

import io.fabric8.docker.api.model.ImageDelete;
import io.fabric8.docker.client.DockerClient;

import java.util.Date;
import java.util.List;

import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.async.AsyncException;
import com.harmazing.openbridge.paasos.imgbuild.log.LogManager;
import com.harmazing.openbridge.paasos.imgbuild.log.LogProvider;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;

public class DeleteCommand extends AbstractDockerCommand implements LogProvider{
	StringBuffer sb = new StringBuffer();

	public void execute(BuildTask buildTask) {
		String taskId = buildTask.getTaskId();
		LogManager.add(taskId, this);
		String imageId = buildTask.getBuildImageId();
		if (StringUtil.isNull(imageId)) {
			sb.append("ImageId Is Null");
			buildTask.setBuildLogs(sb.toString());
			buildTask.setBuildDate(new Date());
			buildTask.setBuildStatus(0);
			throw new AsyncException(buildTask);
		} else {
			try {
				PaasOsImage poi = geImageService().findById(imageId);
				if (poi != null) {
					try {
						// TODO: 删除仓库
					} catch (Exception e) {

					}

					try {
						// TODO: 删除构建服务器
						String imageName = poi.getImageName();
						DockerClient client = getDefaultDockerClient();
						List<ImageDelete> imageDeleteList = client.image()
								.withName(imageName).delete().force()
								.andPrune();
						for (ImageDelete imageDelete : imageDeleteList) {
							if (StringUtil.isNotNull(imageDelete.getDeleted())) {
								sb.append("Deleted:" + imageDelete.getDeleted());
							}
							if (StringUtil.isNotNull(imageDelete.getUntagged())) {
								sb.append("Untagged:"
										+ imageDelete.getUntagged());
							}
						}
						client.close();
					} catch (Exception e) {

					}
					geImageService().deleteById(imageId);
					getBuildLogService().delete(taskId);
				}
				buildTask.setBuildStatus(0);
			} catch (Exception e) {
				sb.append(e.getMessage() != null ? e.getMessage()
						: ("Deleted Fail. Image id =" + buildTask
								.getBuildImageId()));
				buildTask.setBuildStatus(BuildStatus.DELETE_FAIL);
				buildTask.setBuildLogs(taskId);
				buildTask.setBuildDate(new Date());
				LogManager.end(taskId);//日志结束
				throw new AsyncException(buildTask);
			}
			LogManager.delete(taskId);
		}
	}

	@Override
	public String getResult() {
		return sb.toString();
	}
}
