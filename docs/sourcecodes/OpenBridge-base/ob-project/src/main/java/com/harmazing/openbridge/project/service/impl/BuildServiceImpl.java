package com.harmazing.openbridge.project.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.DateUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.ssh.ShellClient;
import com.harmazing.openbridge.project.service.IBuildService;
import com.harmazing.openbridge.project.service.ISubversionService;
import com.harmazing.openbridge.project.util.SonarRestUtil;
import com.harmazing.openbridge.project.vo.ScmInfo;
import com.harmazing.openbridge.project.vo.SvnInfo;
import com.jcraft.jsch.JSchException;

@Service
public class BuildServiceImpl implements IBuildService {
	private static final Log logger = LogFactory.getLog(BuildServiceImpl.class);
	@Autowired
	private ISubversionService subversionService;

	private String getBuildBasePath() {
		return "~/projects";
	}

	public List<String> getProjectVersionFile(String projectId, String versionId) {
		String folderPath = getAssemblyFolder(projectId, versionId);
		File folder = new File(ConfigUtil.getConfigString("file.storage")
				+ folderPath);
		List<String> list = new ArrayList<String>();
		if (folder.exists()) {
			File[] files = folder.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					continue;
				}
				if (files[i].getName().startsWith(".")) {
					continue;
				}
				list.add(folderPath + "/" + files[i].getName());
			}
			if (list.size() > 0) {
				Collections.sort(list, new Comparator<String>() {
					@Override
					public int compare(String o1, String o2) {
						return o1.compareTo(o2) == -1 ? 0 : -1;
					}
				});
			}
		}
		return list;
	}

	private ShellClient getBuildServerSSHClient() throws JSchException {

		String host = ConfigUtil.getConfigString("build.hostip");
		Integer port = ConfigUtil.getConfigInt("build.hostport", 22);
		String user = ConfigUtil.getConfigString("build.hostuser");
		String privateKey = ConfigUtil.getConfigString("ssh.prv");
		String pubKey = ConfigUtil.getConfigString("ssh.pub");

		return new ShellClient(host, port, user, privateKey.getBytes(),
				pubKey.getBytes());
	}

	// 级联创建指定服务器目录，若已经存在则不做任何操作
	private void folderCreate(ShellClient ssh, String path) throws Exception {
		StringBuilder result = new StringBuilder();
		String err = ssh.exec("mkdir -p " + getBuildBasePath() + path, result);
		if (StringUtil.isNotNull(err)) {
			throw new Exception("\r\nerror:" + err);
		}
	}

	private boolean folderExist(ShellClient ssh, String path, String folderName) {
		StringBuilder sb = new StringBuilder();
		String err = ssh.exec("ls -a " + path, sb);
		String result = sb.toString();
		if (StringUtil.isNull(err) && StringUtil.isNotNull(result)) {
			String[] floder = result.split("\\n");
			for (int i = 0; i < floder.length; i++) {
				if (floder[i].equals(".") || floder[i].equals("..")) {
					continue;
				}
				if (floder[i].equals(folderName)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean sourceInitialized(ShellClient ssh, String scmUrl,
			ScmInfo scmInfo, String codeFolder, StringBuilder result) {
		if (scmInfo.getScmType().equals("svn")) {
			if (folderExist(ssh, codeFolder, ".svn")) {
				return true;
			}
		}
		if (scmInfo.getScmType().equals("git")) {
			if (folderExist(ssh, codeFolder, ".git")) {
				return true;
			}
		}
		return false;
	}

	private void checkoutSource(ShellClient ssh, String scmUrl,
			ScmInfo scmInfo, String sourceFolder, StringBuilder result)
			throws Exception {
		String command = "";
		String err = "";
		if (scmInfo.getScmType().equals("svn")) {
			command = subversionService.buildCheckoutCMD(sourceFolder, scmUrl,
					(SvnInfo) scmInfo);
		} else if (scmInfo.getScmType().equals("git")) {
			command = "git clone " + scmUrl + " " + sourceFolder;
		} else {
			throw new Exception("源码类型不支持");
		}
		result.append("command:svn checkout git clone");
		err = ssh.exec(command, result);
		if (StringUtil.isNotNull(err)) {
			throw new Exception("\r\nerror:" + err);
		}
	}

	private void upgradeSource(ShellClient ssh, String scmUrl, ScmInfo scmInfo,
			String sourceFolder, StringBuilder result) throws Exception {
		String command = "";
		String err = "";
		if (scmInfo.getScmType().equals("svn")) {
			command = ("cd " + sourceFolder + "\n" + subversionService
					.buildUpgradeCMD((SvnInfo) scmInfo));
		} else if (scmInfo.getScmType().equals("git")) {
			command = ("cd " + sourceFolder + "\ngit pull");
		} else {
			throw new Exception("源码类型不支持");
		}
		result.append("command:svn update git pull");
		err = ssh.exec(command, result);
		if (StringUtil.isNotNull(err)) {
			throw new Exception("\r\nerror:" + err);
		}
	}

	private void updateSource(ShellClient ssh, String scmUrl, ScmInfo scmInfo,
			String codeFolder, StringBuilder result) throws Exception {
		if (!sourceInitialized(ssh, scmUrl, scmInfo, codeFolder, result)) {
			checkoutSource(ssh, scmUrl, scmInfo, codeFolder, result);
		}
		upgradeSource(ssh, scmUrl, scmInfo, codeFolder, result);
	}

	private void exportTemplateFolder(ShellClient ssh, String scmType,
			String sourceFolder, String folderName, StringBuilder result)
			throws Exception {
		String command = "";
		String err = "";
		if (scmType.equals("svn")) {
			command = ("cd " + sourceFolder + "\nsvn export . " + folderName);

		} else if (scmType.equals("git")) {
			command = ("cp -r " + sourceFolder + " " + folderName
					+ "\n rm -rf " + folderName + "/.git");
		} else {
			throw new Exception("源码类型不支持");
		}
		appendCmd2Out(result, command);
		err = ssh.exec(command, result);
		if (StringUtil.isNotNull(err)) {
			throw new Exception("\r\nerror:" + err);
		}
	}

	public String getAssemblyFolder(String projectId, String versionId) {
		return "/projects/" + projectId + "/assembly/" + versionId;
	}

	private String getAssemblyFile(String projectId, String versionId) {
		return getAssemblyFolder(projectId, versionId) + "/"
				+ DateUtil.format(new Date(), DateUtil.YYYYMMDDHHMMSS) + ".zip";
	}

	public String buildServiceSource(String projectId, String versionId,
			String scmUrl, ScmInfo scmInfo, StringBuilder result,
			String outputFile) {
		return buildServiceSource(projectId, versionId, scmUrl, scmInfo, result, outputFile, null);
	}
		public String buildServiceSource(String projectId, String versionId,
				String scmUrl, ScmInfo scmInfo, StringBuilder result,
				String outputFile,Map<String, String> param) {
		String build = "";
		ShellClient ssh = null;
		try {
			ssh = getBuildServerSSHClient();
			String command = "";
			String err = "";

			String path1 = "/" + projectId + "/" + versionId + "/source";
			String path2 = "/" + projectId + "/" + versionId + "/temp";
			err = ssh.exec("rm -rf " + getBuildBasePath() + path1, result);
			if (StringUtil.isNotNull(err)) {
				throw new Exception("\r\nerror:" + err);
			}
			folderCreate(ssh, path1);
			folderCreate(ssh, path2);
			String codeFolder = getBuildBasePath() + path1;
			// 获取源码
			updateSource(ssh, scmUrl, scmInfo, codeFolder, result);

			String projectFolder = getBuildBasePath() + path2;
			String tempFolderName = projectFolder + "/"
					+ DateUtil.format(new Date(), DateUtil.YYYYMMDDHHMMSS);
			err = ssh.exec("rm -rf " + projectFolder + "/*", result);
			if (StringUtil.isNotNull(err)) {
				throw new Exception("\r\nerror:" + err);
			}
			// 将源码导出到临时目录
			exportTemplateFolder(ssh, scmInfo.getScmType(), codeFolder,
					tempFolderName, result);
			command = "echo $PATH";
			cmdExecAndOut(result, ssh, command);
			command = "echo $MAVEN_HOME";
			cmdExecAndOut(result, ssh, command);
			command = "echo $JAVA_HOME";
			cmdExecAndOut(result, ssh, command);
			// 编译源码
//			cmdExecAndOut(result, ssh, "cp pom.xml pom.xml.bak");//pom.xml里添加property
			String sonarExecCmd = "";
			if(ConfigUtil.getConfigInt("sonar.analysis.switch", 0)==1){
				sonarExecCmd = " sonar:sonar";
				if(param==null){
					param = new HashMap<String, String>();
				}
				param.put("sonar.host.url", ConfigUtil.getConfigString(SonarRestUtil.SONAR_SERVER_URI));
				param.put("sonar.scm.provider", scmInfo.getScmType());
//				param.put("sonar.login", ConfigUtil.getConfigString("sonar.username"));
//				param.put("sonar.password", ConfigUtil.getConfigString("sonar.password"));
//				param.put("sonar.branch", versionId);
//				param.put("sonar.projectKey", projectId);
//				param.put("sonar.svn.username", versionId);
//				param.put("sonar.svn.password.secured", versionId);
			}
			command = "cd " + tempFolderName + "\npwd\nls\nmvn clean package "+sonarExecCmd+" -Dmaven.test.skip=true";
			if(param!=null){//添加 package 参数
				Iterator<String> it = param.keySet().iterator();
				StringBuilder mavenSetting = new StringBuilder();
				while (it.hasNext()) {
					String key = it.next();
					mavenSetting.append(" -D").append(key).append("=").append(param.get(key));
				}
				command += mavenSetting.toString();
			}
			appendCmd2Out(result, command);
			StringBuilder sb = new StringBuilder();
			sb.append("开始编译:\r\n");
			err = ssh.exec(command, sb);
			String mvn = sb.toString();
			result.append(mvn);
			if (StringUtil.isNotNull(err)) {
				throw new Exception("\r\nerror:" + err);
			} else if (mvn.indexOf("BUILD SUCCESS") <= 0) {
				throw new Exception("\r\nerror:编译时出错");
			}

			String toFile = getAssemblyFile(projectId, versionId);
			String removeFile = tempFolderName + "/" + outputFile;

			// 获取编译后的打包文件
			err = ssh.scpForm(removeFile,
					ConfigUtil.getConfigString("file.storage") + toFile);
			if (StringUtil.isNotNull(err)) {
				throw new Exception("复制文件时出错" + err);
			}
			build = toFile;
			// API编译通过之后删除temp目录的编译文件，此动作的成功与否关系不大只做日志记录即可
			try {
				err = ssh.exec("rm -rf " + projectFolder + "/temp/*", result);
			} catch (Exception e) {
				logger.info("delete folder file: " + projectFolder
						+ "/temp/*  faild");
			}
		} catch (Exception e) {
			result.append(e.getMessage());
			return null;
		} finally {
			try {
				ssh.close();
			} catch (Exception e) {

			}
		}
		return build;
	}

		protected void cmdExecAndOut(StringBuilder result, ShellClient ssh,
				String command) {
			appendCmd2Out(result, command);
			ssh.exec(command, result);
		}

		protected void appendCmd2Out(StringBuilder result, String command) {
			result.append("command:\r\n" + command.replace("\\n", "\\r\\n")
					+ "\r\n");
		}
}
