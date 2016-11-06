package com.harmazing.openbridge.paasos.project.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaasProjectBuild implements Serializable{
	
	/**
	 * 构建ID paasos 会自动生成
	 */
	private String buildId;
	
	/**
	 * 镜像ID
	 */
	private String imageId;
	
	/**
	 * 开始构建时间
	 */
	private Date buildTime;
	
	/**
	 * 构建日志
	 */
	private String buildLogs;
	
	/**
	 * api app 版本id
	 */
	private String versionId;//api app 版本id
	
	/**
	 * api app 版本code
	 */
	private String versionCode;//api app 版本code
	
	/**
	 * 编译后文件下载路径
	 */
	private String filePath;//
	
	/**
	 * 文件名
	 */
	private String fileName;//
	
	/**
	 * 构建名称
	 */
	private String buildName;//
	
	/**
	 * 构建版本
	 */
	private String buildTag;//
	
	// 
	/**
	 * 端口
	 * {'port':8080,'key':'http','protocol':'tcp','remark':'asdf'}
	 */
	private String port; //端口
	
	private String createUser;
	
	private Date createDate;
	
	private Date buildSuccess;
	
	private String projectId;
	
	//镜像创建状态 1已保存  5创建中 10创建成功 0创建失败   
	private Integer status;
	
	//镜像删除状态 1删除中  2 删除失败 3删除成功
	private Integer deleteStatus;
	
	//使用镜像来源
	private String imageName;
	
	private String businessType;
	private String businessId; // 业务ID
	
	private String buildNo;// 只是api app jenkis调用的时候 构建次数
	
	private String dockerFile;
	
	private Map<String,Object> buildParam = new HashMap<String,Object>(); 
	
	public String getBuildId() {
		return buildId;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public Date getBuildTime() {
		return buildTime;
	}

	public void setBuildTime(Date buildTime) {
		this.buildTime = buildTime;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBuildName() {
		return buildName;
	}

	public void setBuildName(String buildName) {
		this.buildName = buildName;
	}

	public String getBuildTag() {
		return buildTag;
	}

	public void setBuildTag(String buildTag) {
		this.buildTag = buildTag;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getBuildLogs() {
		return buildLogs;
	}

	public void setBuildLogs(String buildLogs) {
		this.buildLogs = buildLogs;
	}

	public Date getBuildSuccess() {
		return buildSuccess;
	}

	public void setBuildSuccess(Date buildSuccess) {
		this.buildSuccess = buildSuccess;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Integer getDeleteStatus() {
		return deleteStatus;
	}

	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBuildNo() {
		return buildNo;
	}

	public void setBuildNo(String buildNo) {
		this.buildNo = buildNo;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getDockerFile() {
		return dockerFile;
	}

	public void setDockerFile(String dockerFile) {
		this.dockerFile = dockerFile;
	}

	public Map<String, Object> getBuildParam() {
		return buildParam;
	}

	public void setBuildParam(Map<String, Object> buildParam) {
		this.buildParam = buildParam;
	}

	
	
	
	
}
