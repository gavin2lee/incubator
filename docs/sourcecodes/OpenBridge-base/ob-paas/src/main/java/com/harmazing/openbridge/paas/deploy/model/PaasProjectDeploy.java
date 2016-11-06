package com.harmazing.openbridge.paas.deploy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;

public class PaasProjectDeploy implements Serializable{
	
	private String deployId        ;//                           
	private String projectId       ;//                           
	private String deployName      ;//部署名称                   
	private String description      ;//描述                       
	private String tenantId        ;//租户ID  不要使用这个字段 后面会去掉               
	private String envType         ;//环境类型取值[test|live]
	private String createUser      ;//创建者                     
	private Date createTime      ;//创建时间                   
	private String serviceIp       ;//服务IP                     
	private String publicIp        ;//外部访问IP   
	private String deployCode;
	
	private String imageId;
	
	private String deployTime;
	
	private String healthContent;
	
	private List<PaasProjectDeployPort> ports = new ArrayList<PaasProjectDeployPort>();
	
	private List<PaasProjectDeployVolumn> volumn = new ArrayList<PaasProjectDeployVolumn>();
	
	private List<PaasProjectProbes> health = new ArrayList<PaasProjectProbes>();
	
	/**
	 * {
	 * key,
	 * value
	 * }
	 */
	private String labels;
	private String ownerCluster;//所在集群
	
	
        
	private Integer replicas         ;//副本数                     
	private String restartPolicy   ;//容器重启策略 always        
	private String modifyUser      ;//最后一次修改用户           
	private Date modifyTime      ;//最后一次修改时间         
	
	private Double cpu;// cpu 默认单位为个    
	private Double memory;// 内存 默认单位为m
	
	private String businessId;//业务ID  api app用
	private String projectType;//业务编码  api app用
	private String envId;//环境ID
	private String extraKey;//用来区分部署的  部署类别 比如应用 数据库 等  现在有 application,nginx
	private String extraData;//  nginx保存json数据 json配置信息
	
	/**
	 * {
	 * name: '名称',
	 * 	type : 类型,
	 * mount : 挂在点,
	 * capacity : 容量
	 * }
	 */
//	private String volumn           ;//数据卷                 
	
	/**
	 * {
	 * 	key: 关键字，
	 * value: 值
	 * 
	 * }
	 */
//	private String envVariable     ;//环境变量
	private List<PaasProjectDeployEnv> envVariable = new ArrayList<PaasProjectDeployEnv>();
	
	/**
	 * {
	 *   spec : ms,s,m ,
	 *   cpu : cpu个数,
	 *   memory : 内存,
	 *   replicas : 副本
	 * }
	 */
	private String computeConfig   ;//服务器配置                 
	private String buildId         ;//构建id 
	//1 已保存
	private Integer status;//1停止  5启动中 6变更中 7停止中 10运行中 11删除失败 0启动失败
	private Integer deleteStatus;//1删除中  2 删除失败 3删除成功  不用了
	
	private String resourceConfig;//依赖资源配置
	
	
	private String envName;
	
	public String getDeployId() {
		return deployId;
	}
	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getDeployName() {
		return deployName;
	}
	public void setDeployName(String deployName) {
		this.deployName = deployName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public String getEnvType() {
		return envType;
	}
	public void setEnvType(String envType) {
		this.envType = envType;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getServiceIp() {
		return serviceIp;
	}
	public void setServiceIp(String serviceIp) {
		this.serviceIp = serviceIp;
	}
	public String getPublicIp() {
		return publicIp;
	}
	public void setPublicIp(String publicIp) {
		this.publicIp = publicIp;
	}
	
	public Integer getReplicas() {
		return replicas;
	}
	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}
	public String getRestartPolicy() {
		return restartPolicy;
	}
	public void setRestartPolicy(String restartPolicy) {
		this.restartPolicy = restartPolicy;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
	
	
	
	public Double getCpu() {
		return cpu;
	}
	public void setCpu(Double cpu) {
		this.cpu = cpu;
	}
	public Double getMemory() {
		return memory;
	}
	public void setMemory(Double memory) {
		this.memory = memory;
	}
	public List<PaasProjectDeployEnv> getEnvVariable() {
		return envVariable;
	}
	public void setEnvVariable(List<PaasProjectDeployEnv> envVariable) {
		this.envVariable = envVariable;
	}
	public String getComputeConfig() {
		return computeConfig;
	}
	public void setComputeConfig(String computeConfig) {
		this.computeConfig = computeConfig;
	}
	
	public String getBuildId() {
		return buildId;
	}
	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDeleteStatus() {
		return deleteStatus;
	}
	public void setDeleteStatus(Integer deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	
	public String getOwnerCluster() {
		return ownerCluster;
	}
	public void setOwnerCluster(String ownerCluster) {
		this.ownerCluster = ownerCluster;
	}
	public String getLabels() {
		return labels;
	}
	public void setLabels(String labels) {
		this.labels = labels;
	}
	public String getDeployCode() {
		return deployCode;
	}
	public void setDeployCode(String deployCode) {
		this.deployCode = deployCode;
	}
	public List<PaasProjectDeployPort> getPorts() {
		return ports;
	}
	public void setPorts(List<PaasProjectDeployPort> ports) {
		this.ports = ports;
	}

	//1是 2否
	private String publicPort;


	public String getPublicPort() {
		return publicPort;
	}
	public void setPublicPort(String publicPort) {
		this.publicPort = publicPort;
	}
	public List<PaasProjectDeployVolumn> getVolumn() {
		return volumn;
	}
	public void setVolumn(List<PaasProjectDeployVolumn> volumn) {
		this.volumn = volumn;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getResourceConfig() {
		return resourceConfig;
	}
	public void setResourceConfig(String resourceConfig) {
		this.resourceConfig = resourceConfig;
	}
	public String getBusinessId() {
		return businessId;
	}
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getEnvId() {
		return envId;
	}
	public void setEnvId(String envId) {
		this.envId = envId;
	}
	public String getExtraKey() {
		return extraKey;
	}
	public void setExtraKey(String extraKey) {
		this.extraKey = extraKey;
	}
	public String getExtraData() {
		return extraData;
	}
	public void setExtraData(String extraData) {
		this.extraData = extraData;
	}
	public String getEnvName() {
		return envName;
	}
	public void setEnvName(String envName) {
		this.envName = envName;
	}
	public String getDeployTime() {
		return deployTime;
	}
	public void setDeployTime(String deployTime) {
		this.deployTime = deployTime;
	}
	public List<PaasProjectProbes> getHealth() {
		return health;
	}
	public void setHealth(List<PaasProjectProbes> health) {
		this.health = health;
	}
	
	public String getHealthContent() {
		if(StringUtils.hasText(healthContent)){
			return healthContent;
		}
		if(health!=null){
			return JSONArray.toJSONString(health);
		}
		return healthContent;
	}
	public void setHealthContent(String healthContent) {
		if((health==null || health.size()==0) && StringUtils.hasText(healthContent)){
			List<PaasProjectProbes> h = JSONArray.parseArray(healthContent, PaasProjectProbes.class);
			if(h!=null && h.size()>0){
				setHealth(h);
			}
		}
		this.healthContent = healthContent;
	}

	
	
	

}
