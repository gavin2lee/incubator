package com.harmazing.openbridge.project.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.harmazing.framework.util.CMDUtil;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.FileUtil;
import com.harmazing.framework.util.LogUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.ssh.ShellClient;
import com.harmazing.openbridge.project.service.ISubversionService;
import com.harmazing.openbridge.project.vo.SvnAuthModel;
import com.harmazing.openbridge.project.vo.SvnInfo;
import com.harmazing.openbridge.project.vo.SvnServerInfo;
import com.jcraft.jsch.JSchException;

@Service
public class SubversionServiceImpl implements ISubversionService {
	private String getSvnParams() {
		return " --non-interactive --trust-server-cert-failures=unknown-ca,cn-mismatch,expired,not-yet-valid,other ";
	}

	public boolean projectExists(String projectName, SvnInfo svnInfo)
			throws Exception {
		if (folderExists("/", projectName, svnInfo)) {
			return true;
		}
		return false;
	}

	public String buildCheckoutCMD(String localFolder, String url,
			SvnInfo svnInfo) {
		String username = svnInfo.getUsername();
		String password = svnInfo.getPassword();
		String cmd = "svn checkout " + getSvnParams() + " --username "
				+ username + " --password " + password + " " + url + " "
				+ localFolder;
		return cmd;
	}

	public String buildUpgradeCMD(SvnInfo svnInfo) {
		String username = svnInfo.getUsername();
		String password = svnInfo.getPassword();
		String cmd = "svn update " + getSvnParams() + " --username " + username
				+ " --password " + password + "";
		return cmd;
	}

	public void createProject(String projectName, SvnInfo svnInfo)
			throws Exception {
		String username = svnInfo.getUsername();
		String password = svnInfo.getPassword();
		String baseUrl = svnInfo.getBaseUrl();
		makeDir(baseUrl.trim() + "/" + projectName, username, password,
				"create project");
	}

	public String importProjectFolder(String project, String folder,
			String localFolder, SvnInfo svnInfo) throws Exception {
		String username = svnInfo.getUsername();
		String password = svnInfo.getPassword();
		String baseUrl = svnInfo.getBaseUrl();
		String scmUrl = baseUrl.trim() + "/" + project.trim() + folder.trim();
		importDir(scmUrl, localFolder.trim(), username, password, "init");
		return scmUrl;
	}

	private void importDir(String svnUrl, String localDir, String username,
			String password, String comment) throws Exception {
		try {
			String cmdPath = "svn";
			String command = cmdPath + " import " + getSvnParams()
					+ " --username " + username + " --password " + password
					+ " -m \"" + comment + "\" ";
			command += localDir + " ";
			command += svnUrl;
			StringBuilder s = new StringBuilder();
			String err = CMDUtil.process(command, s);
			if (StringUtil.isNotNull(err)) {
				LogUtil.error("SVN Error:" + err);
				throw new Exception(err);
			}
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	private void makeDir(String svnUrl, String username, String password,
			String comment) throws Exception {
		try {
			String cmdPath = "svn";
			String command = cmdPath + " mkdir " + getSvnParams()
					+ " --username " + username + " --password " + password
					+ " -m \"" + comment + "\" ";
			command += svnUrl;
			StringBuilder s = new StringBuilder();
			String err = CMDUtil.process(command, s);
			if (StringUtil.isNotNull(err)) {
				LogUtil.error("SVN Error:" + err);
				throw new Exception(err);
			}
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}

	/**
	 * 判断svn服务器上指定目录下的指定文件/文件夹是否存在
	 * 
	 * @param folder
	 * @param file
	 * @return
	 * @throws Exception
	 */
	private boolean folderExists(String folder, String file, SvnInfo svnInfo)
			throws Exception {
		String baseUrl = svnInfo.getBaseUrl();
		if (baseUrl.endsWith("/")) {
			baseUrl += folder;
		} else {
			baseUrl = baseUrl + "/";
			if (!folder.equals("/")) {
				baseUrl = baseUrl + folder;
			}
		}
		String command = "svn ls " + getSvnParams() + " --username "
				+ svnInfo.getUsername() + " --password "
				+ svnInfo.getPassword() + " " + baseUrl;

		StringBuilder result = new StringBuilder();
		String err = CMDUtil.process(command, result);
		if (StringUtil.isNotNull(err)) {
			LogUtil.error("SVN Error:" + err);
			throw new Exception(err);
		}
		// 注意下面的最大匹配
		String[] pros = result.toString().split("\\s*[/\\n]\\s*"); // 在换行前后包含若干空白符，也可不包含的分离
		for (int i = 0; i < pros.length; i++) {
			if (pros[i].trim().equals(file.trim())) {
				return true;
			}
		}
		return false;
	}

	public void deleteProject(String projectName, SvnInfo svnInfo)
			throws Exception {
		if (this.projectExists(projectName, svnInfo)) {
			String cmdPath = "svn"; // 可扩展的源代码管理命令
			String username = svnInfo.getUsername();
			String password = svnInfo.getPassword();
			String baseUrl = svnInfo.getBaseUrl();
			String scmUrl = baseUrl.trim() + "/" + projectName.trim();
			// 1、删除服务代码
			String command = cmdPath + " delete " + getSvnParams()
					+ " --username " + username + " --password " + password
					+ " " + scmUrl + " -m \"delete project\"";
			StringBuilder result = new StringBuilder();
			String err = CMDUtil.process(command, result);
			if (StringUtil.isNotNull(err)) {
				LogUtil.error("SVN Error:" + err);
				throw new RuntimeException("删除SVN源代码出错" + err);
			}
		}
	}

	public void deleteProjectFolder(String projectName, String folder,
			SvnInfo svnInfo) throws Exception {
		if (this.projectExists(projectName, svnInfo)) {
			String cmdPath = "svn"; // 可扩展的源代码管理命令
			String username = svnInfo.getUsername();
			String password = svnInfo.getPassword();
			String baseUrl = svnInfo.getBaseUrl();
			String scmUrl = baseUrl.trim() + "/" + projectName.trim() + folder;
			// 1、删除服务代码
			String command = cmdPath + " delete " + getSvnParams()
					+ " --username " + username + " --password " + password
					+ " " + scmUrl + " -m \"delete folder\"";
			StringBuilder result = new StringBuilder();
			String err = CMDUtil.process(command, result);
			if (StringUtil.isNotNull(err)) {
				LogUtil.error("SVN Error:" + err);
				throw new RuntimeException("删除SVN源代码出错" + err);
			}
		}
	}

	public void createProjectFolder(String projectName, String folder,
			SvnInfo svnInfo) throws Exception {
		if (this.projectExists(projectName, svnInfo)) {
			String username = svnInfo.getUsername();
			String password = svnInfo.getPassword();
			String baseUrl = svnInfo.getBaseUrl();
			String scmUrl = baseUrl.trim() + "/" + projectName.trim() + folder;
			makeDir(scmUrl, username, password, "create folder");
		}
	}

	@Override
	public void syncSVNAuthsFile(String basePath, String project,
			List<String> readWrite, List<String> readOnly,
			SvnServerInfo svnServerInfo) throws Exception {
		svnGenAuthFile(basePath, project, readWrite, readOnly, svnServerInfo);
		svnScpAuthFileToSvnService(basePath, svnServerInfo);
	}

	private ShellClient getSVNShellClient(SvnServerInfo info)
			throws JSchException {
		String ip = info.getSvnServerIp();
		Integer port = 22;
		if (StringUtil.isNotNull(info.getSvnServerPort())) {
			port = Integer.valueOf(info.getSvnServerPort());
		}
		String user = info.getSvnServerUser();
		String privateKey = ConfigUtil.getConfigString("ssh.prv");
		String pubKey = ConfigUtil.getConfigString("ssh.pub");
		return new ShellClient(ip, port, user, privateKey.getBytes(),
				pubKey.getBytes());
	}

	/**
	 * 生成单个项目的SVN授权信息
	 * 
	 * @param basePath
	 * @param project
	 * @param readWrite
	 * @param readOnly
	 * @param svnServerInfo
	 * @throws Exception
	 */
	private void svnGenAuthFile(String basePath, String project,
			List<String> readWrite, List<String> readOnly,
			SvnServerInfo svnServerInfo) throws Exception {
		File folder = new File(basePath + "/project");
		if (!folder.exists()) {
			folder.mkdirs();
		}
		StringBuilder serviceAuthz = new StringBuilder();
		serviceAuthz.append("[/" + project + "]\n");

		if (readOnly != null) {
			List<String> temp = new ArrayList<String>();
			for (int j = 0; j < readOnly.size(); j++) {
				String ln = readOnly.get(j);
				if (!temp.contains(ln)) {
					serviceAuthz.append(readOnly.get(j) + "=r\r\n");
					temp.add(ln);
				}
			}
		}
		if (readWrite != null) {
			List<String> temp = new ArrayList<String>();
			for (int j = 0; j < readWrite.size(); j++) {
				String ln = readWrite.get(j);
				if (!temp.contains(ln)) {
					serviceAuthz.append(readWrite.get(j) + "=rw\r\n");
					temp.add(ln);
				}
			}
		}

		FileUtil.write(new File(basePath + "/project/" + project + ".authz"),
				serviceAuthz.toString(), "UTF-8");
	}

	/**
	 * 合并授权文件内容上传到SVN服务器
	 * 
	 * @param basePath
	 * @param svnServerInfo
	 * @throws Exception
	 */
	private void svnScpAuthFileToSvnService(String basePath,
			SvnServerInfo svnServerInfo) throws Exception {
		String authzFileName = basePath + "/authz";
		StringBuilder finalAuthz = new StringBuilder();
		finalAuthz.append("[/]\r\n");
		finalAuthz.append(svnServerInfo.getSvnAdminUsername() + "=rw\r\n");

		File[] files = new File(basePath + "/project").listFiles();

		for (File f : files) {
			if (f.isFile()) {
				String fileContent = FileUtil.readFileToString(f, "UTF-8");
				finalAuthz.append(fileContent + "\r\n");
			}
		}
		FileUtil.write(new File(authzFileName), finalAuthz.toString(), "UTF-8");

		ShellClient ssh = getSVNShellClient(svnServerInfo);
		String remoteFile = svnServerInfo.getSvnAuthzPath();
		String ret = ssh.scpTo(authzFileName, remoteFile);
		if (ret.indexOf("error") > -1) {
			throw new RuntimeException(ret);
		}
	}

	@Override
	public void initSVNAuthsFile(String basePath,
			List<SvnAuthModel> authModels, SvnServerInfo svnServerInfo)
			throws Exception {
		try {

			for (int i = 0; i < authModels.size(); i++) {
				SvnAuthModel model = authModels.get(i);
				if (model.getProject() != null
						&& model.getProject().length() > 0) {
					String project = model.getProject();
					svnGenAuthFile(basePath, project, model.getReadWrite(),
							model.getReadOnly(), svnServerInfo);
				}
			}

			svnScpAuthFileToSvnService(basePath, svnServerInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
