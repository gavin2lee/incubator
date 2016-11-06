package com.harmazing.openbridge.project.service;

import java.util.List;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.project.vo.SvnAuthModel;
import com.harmazing.openbridge.project.vo.SvnInfo;
import com.harmazing.openbridge.project.vo.SvnServerInfo;

public interface ISubversionService extends IBaseService {
	public String buildCheckoutCMD(String localFolder, String url,
			SvnInfo svnInfo);

	public String buildUpgradeCMD(SvnInfo svnInfo);

	/**
	 * 判断项目是否存在
	 * 
	 * @param projectName
	 *            项目名称
	 * @return
	 * @throws Exception
	 */
	public boolean projectExists(String projectName, SvnInfo svnInfo)
			throws Exception;

	/**
	 * 创建项目
	 * 
	 * @param projectName
	 *            项目名称
	 * @throws Exception
	 */
	public void createProject(String projectName, SvnInfo svnInfo)
			throws Exception;

	/**
	 * 创建项目
	 * 
	 * @param projectName
	 *            项目名称
	 * @throws Exception
	 */
	public void createProjectFolder(String projectName, String folder,
			SvnInfo svnInfo) throws Exception;

	/**
	 * 删除项目
	 * 
	 * @param projectName
	 *            项目名称
	 * @throws Exception
	 */
	public void deleteProject(String projectName, SvnInfo svnInfo)
			throws Exception;

	/**
	 * 删除项目里面的某个目录
	 * 
	 * @param projectName
	 *            项目名称
	 * @param folder
	 *            目录名称
	 * @throws Exception
	 */
	public void deleteProjectFolder(String projectName, String folder,
			SvnInfo svnInfo) throws Exception;

	/**
	 * 将本地目录导入到源码管理器中
	 * 
	 * @param project
	 *            项目名称
	 * @param folder
	 *            SVN中的目录
	 * @param localFolder
	 *            本地内容目录
	 * @return
	 * @throws Exception
	 */
	public String importProjectFolder(String project, String folder,
			String localFolder, SvnInfo svnInfo) throws Exception;

	public void syncSVNAuthsFile(String basePath, String project,
			List<String> readWrite, List<String> readOnly,
			SvnServerInfo svnServerInfo) throws Exception;

	public void initSVNAuthsFile(String basePath,
			List<SvnAuthModel> authModels, SvnServerInfo svnServerInfo)
			throws Exception;
}
