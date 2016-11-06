package com.harmazing.openbridge.paasos.resource.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.model.BaseModel;
import com.harmazing.framework.util.StringUtil;

/**
 * 消息队列
 * @author taoshuangxi
 *
 */
@SuppressWarnings("serial")
public class PaasRabbitMQ extends BaseModel{
	private String mqId; //消息队列id
	private String mqName; //消息队列名称
	private String applyContent; //申请资源参数
	private String creater; //申请人
	private Date createDate; //申请时间
	private String allocateContent; //分配资源参数
	private Date updateDate; //修改时间
	private String tenantId;//租户Id
	private String projectId;//项目Id
	private String envId;//环境Id
	private String envType;//环境类型，测试和生产
	private Boolean onReady;//是否创建完毕
	private String resDesc;//描述信息
	private String applyType;//api,app or others
	
	private Double memory;
	private Double cpu;
	private Double storage;
	
	public String getMqId() {
		return mqId;
	}
	public void setMqId(String mqId) {
		this.mqId = mqId;
	}
	public String getMqName() {
		return mqName;
	}
	public void setMqName(String mqName) {
		this.mqName = mqName;
	}
	public String getApplyContent() {
		return applyContent;
	}
	public void setApplyContent(String applyContent) {
		this.applyContent = applyContent;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAllocateContent() {
		return allocateContent;
	}
	public void setAllocateContent(String allocateContent) {
		this.allocateContent = allocateContent;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getEnvId() {
		return envId;
	}
	public void setEnvId(String envId) {
		this.envId = envId;
	}
	public String getEnvType() {
		return envType;
	}
	public void setEnvType(String envType) {
		this.envType = envType;
	}
	public Boolean getOnReady() {
		return onReady;
	}
	public void setOnReady(Boolean onReady) {
		this.onReady = onReady;
	}
	public String getResDesc() {
		return resDesc;
	}
	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}
	
	//获取paasOS的资源id
	public String getResId(){
		String allocation = this.getAllocateContent();
		if(!StringUtil.isNull(allocation)){
			try{
				JSONObject obj = JSONObject.parseObject(allocation);
				return obj.getString("id");
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
	
	//获取版本信息
	public String getVersion() {
		String allocation = this.getAllocateContent();
		if(!StringUtil.isNull(allocation)){
			try{
				JSONObject obj = JSONObject.parseObject(allocation);
				return obj.getString("version");
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
	
	//获取申请资源参数
	public String getApplyEnvInfo(){
		String apply = this.getApplyContent();
		if(!StringUtil.isNull(apply)){
			try{
				JSONObject obj = JSONObject.parseObject(apply);
				return obj.getString("memory");
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
	
	//获取分配资源参数
	public String getAllocateEnvInfo(){
		String allocation = this.getAllocateContent();
		if(!StringUtil.isNull(allocation)){
			try{
				JSONObject obj = JSONObject.parseObject(allocation);
				return obj.getString("memory");
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
	
	//获取mq的访问连接参数
	public String getConnnectionInfo(){
		String allocation = this.getAllocateContent();
		if(!StringUtil.isNull(allocation)){
			try{
				JSONObject obj = JSONObject.parseObject(allocation);
				JSONObject connection = obj.getJSONObject("connection");
				JSONObject server = connection.getJSONObject("server");
				StringBuilder sb = new StringBuilder("帐号:");
				sb.append(connection.getString("account"));
				sb.append("\r\n密码:");
				sb.append(connection.getString("password"));
				sb.append("\r\n公网:");
				sb.append(server.getString("externalIP"));
				sb.append(":");
				sb.append(server.getString("externalPort"));
				sb.append("\r\n内网(集群):");
				sb.append(server.getString("clusterIP"));
				sb.append(":");
				sb.append(server.getString("port"));
				return sb.toString();
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
	
	//获取mq的管理连接参数
	public String getManagementInfo(){
		String allocation = this.getAllocateContent();
		if(!StringUtil.isNull(allocation)){
			try{
				JSONObject obj = JSONObject.parseObject(allocation);
				JSONObject connection = obj.getJSONObject("connection");
				JSONObject management = connection.getJSONObject("management");
				StringBuilder sb = new StringBuilder("帐号:");
				sb.append(connection.getString("account"));
				sb.append("\r\n密码:");
				sb.append(connection.getString("password"));
				sb.append("\r\n公网:");
				sb.append(management.getString("externalIP"));
				sb.append(":");
				sb.append(management.getString("externalPort"));
				sb.append("\r\n内网(集群):");
				sb.append(management.getString("clusterIP"));
				sb.append(":");
				sb.append(management.getString("port"));
				return sb.toString();
			}catch(Exception e){
				return null;
			}
		}
		return null;
	}
	public String getApplyType() {
		return applyType;
	}
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	public Double getMemory() {
		return memory;
	}
	public void setMemory(Double memory) {
		this.memory = memory;
	}
	public Double getCpu() {
		return cpu;
	}
	public void setCpu(Double cpu) {
		this.cpu = cpu;
	}
	public Double getStorage() {
		return storage;
	}
	public void setStorage(Double storage) {
		this.storage = storage;
	}
	
	
	
}
