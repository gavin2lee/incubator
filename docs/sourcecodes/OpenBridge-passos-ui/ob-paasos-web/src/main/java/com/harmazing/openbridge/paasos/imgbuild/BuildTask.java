package com.harmazing.openbridge.paasos.imgbuild;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.harmazing.openbridge.paas.async.AsyncException;
import com.harmazing.openbridge.paas.async.AsyncTask;

public class BuildTask implements AsyncTask {

	// 项目构建： 构建ID
	private String taskId;

	// 操作用户
	private String currentUserId;

	// app api source 项目镜像构建使用
	private String projectType;
	private String imageSource;

	private BuildTaskType taskType;

	// 文件名和文件路径 ，这边会统一复制filePath的文件放到source/build/taskId/fileName下面去
	private String filePath;
	private String fileName;

	private String imageName;
	private String imageVersion;

	private String imageCommand;
	private String imageArgs;
	private String imageWorkdir;
	/**
	 * JSON [ {'port':8080,'key':'http','protocol':'tcp','remark':'asdf'} ]
	 */
	private String imagePort;

	// 0成功
	private Integer buildStatus;
	private String buildImageId;
	private String buildLogs;
	// 操作结束时间
	private Date buildDate;
	private String buildImageName;

	// 用于部署模块 复本数量修改
	private int scale;

	private String dockerfile;//dockerfile内容
	
	private String sessionId;//会话ID 在生成日志的时候用到
	
	private Map<String,Object> buildParam = new HashMap<String,Object>(); 
	
	//在构建的时候用到 dxq加
	private String versionId;
	
	private String businessId;
	
	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public Date getBuildDate() {
		return buildDate;
	}

	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}

	public Integer getBuildStatus() {
		return buildStatus;
	}

	public void setBuildStatus(Integer buildStatus) {
		this.buildStatus = buildStatus;
	}

	public String getBuildImageId() {
		return buildImageId;
	}

	public void setBuildImageId(String buildImageId) {
		this.buildImageId = buildImageId;
	}

	public String getBuildLogs() {
		return buildLogs;
	}

	public void setBuildLogs(String buildLogs) {
		this.buildLogs = buildLogs;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public BuildTaskType getTaskType() {
		return taskType;
	}

	public void setTaskType(BuildTaskType taskType) {
		this.taskType = taskType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageVersion() {
		return imageVersion;
	}

	public void setImageVersion(String imageVersion) {
		this.imageVersion = imageVersion;
	}

	public String getImageCommand() {
		return imageCommand;
	}

	public void setImageCommand(String imageCommand) {
		this.imageCommand = imageCommand;
	}

	public String getImageArgs() {
		return imageArgs;
	}

	public void setImageArgs(String imageArgs) {
		this.imageArgs = imageArgs;
	}

	public String getImageWorkdir() {
		return imageWorkdir;
	}

	public void setImageWorkdir(String imageWorkdir) {
		this.imageWorkdir = imageWorkdir;
	}

	public String getImagePort() {
		return imagePort;
	}

	public void setImagePort(String imagePort) {
		this.imagePort = imagePort;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getBuildImageName() {
		return buildImageName;
	}

	public void setBuildImageName(String buildImageName) {
		this.buildImageName = buildImageName;
	}

	public String getImageSource() {
		return imageSource;
	}

	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	private AsyncException ae;

	@Override
	public void setAsyncException(AsyncException e) {
		this.ae = e;
	}

	@Override
	public AsyncException getAsyncException() {
		return this.ae;
	}

	public String getDockerfile() {
		return dockerfile;
	}

	public void setDockerfile(String dockerfile) {
		this.dockerfile = dockerfile;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Map<String, Object> getBuildParam() {
		return buildParam;
	}

	public void setBuildParam(Map<String, Object> buildParam) {
		this.buildParam = buildParam;
	}

	public String getVersionId() {
		return versionId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	
	

}
