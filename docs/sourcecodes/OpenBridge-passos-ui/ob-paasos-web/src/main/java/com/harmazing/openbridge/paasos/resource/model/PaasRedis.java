package com.harmazing.openbridge.paasos.resource.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.model.BaseModel;
import com.harmazing.framework.util.StringUtil;

@SuppressWarnings("serial")
public class PaasRedis extends BaseModel{
	private String redisId;
	private String redisName;//实例名称
	private String applyContent;//申请参数
	private String creater;//申请人
	private Date createDate;//申请时间
	private String allocateContent;//分配参数
	private Date updateDate;//修改时间
	private String tenantId;//租户Id
	private String projectId;//项目Id
	private String envId;//环境Id
	private String envType;//环境类型,测试和生产
	private Boolean onReady;//是否已经创建成功
	private String resDesc;//描述信息
	private String applyType;//api,app or others
	
	private Double memory;
	private Double cpu;
	private Double storage;
	
	public String getRedisId() {
		return redisId;
	}
	public void setRedisId(String redisId) {
		this.redisId = redisId;
	}
	public String getRedisName() {
		return redisName;
	}
	public void setRedisName(String redisName) {
		this.redisName = redisName;
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
		if(StringUtil.isNull(allocation)){
			return null;
		}
		try{
			JSONObject obj = JSONObject.parseObject(allocation);
			return obj.getString("id");
		}catch(Exception e){
			return null;
		}
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
	
	//申请资源基本参数
	public String getRequestEnvInfo(){
		String apply = this.getApplyContent();
		if(StringUtil.isNull(apply)){
			return null;
		}
		try{
			JSONObject obj = JSONObject.parseObject(apply);
			return obj.getString("memory")+"内存    " ;
		}catch(Exception e){
			return null;
		}
	}
	//分配资源基本参数
	public String getAllocateEnvInfo(){
		String allocation = this.getAllocateContent();
		if(StringUtil.isNull(allocation)){
			return null;
		}
		try{
			JSONObject obj = JSONObject.parseObject(allocation);
			return obj.getString("memory");
		}catch(Exception e){
			return null;
		}
	}
	//连接参数
	public String getConnectionInfo(){
		String allocation = this.getAllocateContent();
		if(StringUtil.isNull(allocation)){
			return null;
		}
		try{
			JSONObject obj = JSONObject.parseObject(allocation);
			JSONObject connect = obj.getJSONObject("connection");
			JSONObject server = connect.getJSONObject("server");
			StringBuilder sb = new StringBuilder("帐号:");
			sb.append(connect.getString("account"));
			sb.append("\r\n密码:");
			sb.append(connect.getString("password"));
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
