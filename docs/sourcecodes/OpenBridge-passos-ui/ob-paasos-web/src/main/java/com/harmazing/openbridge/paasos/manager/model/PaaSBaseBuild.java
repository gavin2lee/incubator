package com.harmazing.openbridge.paasos.manager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.model.BaseModel;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.imgbuild.BuildStatus;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.imgbuild.BuildTaskType;
import com.harmazing.openbridge.web.fileupload.FileUploadHandler;

public class PaaSBaseBuild extends BaseModel{

	//
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private String fileType;//文件类型，tar.gz | zip
	
	private String filePath;//文件路径
	
	private String fileData;//文件的JSON格式详细描述
	
	private String version;
	
	private String command;
	
	private String workDir;
	
	private String ports;//端口
	
	private String tenantIds;//授权组织
	
	private Integer status = BuildStatus.SAVED;
	
	private String description;
	
	private Date createTime;
	
	private String buildLogs;//关联osBuildLog id
	
	private String imageId;
	
	private String iconPath;//图标
	
	private String dockerfile;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileName(){
		String fileName = null;
		if(StringUtil.isNotNull(fileData)){
			JSONObject object = JSON.parseObject(fileData);
			if(object!=null){
				fileName = object.getString("fileName");
			}
		}else if(StringUtil.isNotNull(filePath)){
			fileName = filePath;
			fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
			fileName = fileName.substring(fileName.lastIndexOf("/")+1);
		}
		return fileName;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		if(StringUtil.isNotNull(this.filePath)){//更新filePath时更新fileData
			this.fileData = fileData.replace(
					JSON.toJSONString(this.filePath),
					JSON.toJSONString(filePath));
		}
		this.filePath = filePath;
	}

	public String getFileData() {
		return fileData;
	}

	public void setFileData(String fileData) {
		this.fileData = fileData;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getWorkDir() {
		return workDir;
	}

	public void setWorkDir(String workDir) {
		this.workDir = workDir;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTenantIds() {
		return tenantIds;
	}

	public void setTenantIds(String tenantIds) {
		this.tenantIds = tenantIds;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBuildLogs() {
		return buildLogs;
	}

	public void setBuildLogs(String buildLogs) {
		this.buildLogs = buildLogs;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	public String getDockerfile() {
		return dockerfile;
	}

	public void setDockerfile(String dockerfile) {
		this.dockerfile = dockerfile;
	}

	/**
	 * @author chenjinfan
	 * @Description 构建时把端口json数据转换成需要的格式
	 * @return
	 */
	public String transPortForBuild(){
		if(StringUtil.isNotNull(ports)){
			JSONArray array = JSON.parseArray(ports);
			if(array!=null){
				JSONArray newArray = new JSONArray();
				Iterator<Object> it = array.iterator();
				while (it.hasNext()) {
					JSONObject port = (JSONObject) it.next();
					if(port !=null){
						JSONObject newPort = new JSONObject();
						newPort.put("port", port.getString("portNo"));
						newPort.put("remark", port.getString("portDesc"));
						newPort.put("protocol", port.getString("portType"));
						newPort.put("key", port.getString("portKey"));
						newArray.add(newPort);
					}
				}
				return newArray.toJSONString();
			}
		}
		return ports;
	}
	
	public boolean checkPortKeyDuplicate(){
		if(StringUtil.isNotNull(ports)){
			JSONArray array = JSON.parseArray(ports);
			if(array!=null){
				Iterator<Object> it = array.iterator();
				List<String> keysList = new ArrayList<String>();
				while (it.hasNext()) {
					JSONObject port = (JSONObject) it.next();
					if(port !=null){
						String key = port.getString("portKey");
						if(keysList.contains(key)){
							return false;
						}else{
							if(StringUtil.isNotNull(key)){
								keysList.add(key);
							}
						}
					}
				}
			}
		}
		return true;
	}
	/**
	 * @author chenjinfan
	 * @Description
	 * @param id task Id
	 * @param user 操作用户
	 * @param PREFIX_BASE 生成镜像根目录
	 * @return
	 */
	public BuildTask generateBuildTask(String id, IUser user, String PREFIX_BASE) {
		BuildTask buildTask = new BuildTask();
		buildTask.setTaskId(id);
		buildTask.setCurrentUserId(user.getUserId());
		if(this.getFileType().equals("zip")){
			buildTask.setTaskType(BuildTaskType.ZIP);
		}else{
			buildTask.setTaskType(BuildTaskType.TARGZ);
		}
		buildTask.setFilePath(FileUploadHandler.getStoreDir()+this.getFilePath());
		buildTask.setFileName(this.getFileName());
		buildTask.setImageName(PREFIX_BASE+"/"+this.getName());
		buildTask.setImageVersion(this.getVersion());
		buildTask.setImagePort(this.transPortForBuild());
		buildTask.setDockerfile(this.getDockerfile());
		return buildTask;
	}
	
	
}
