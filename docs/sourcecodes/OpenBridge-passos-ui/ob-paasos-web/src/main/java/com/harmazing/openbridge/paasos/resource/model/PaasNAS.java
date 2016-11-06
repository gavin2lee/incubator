package com.harmazing.openbridge.paasos.resource.model;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.model.BaseModel;
import com.harmazing.framework.util.StringUtil;

@SuppressWarnings("serial")
public class PaasNAS extends BaseModel {
	private String nasId;
	private String instanceName;// 实例名称
	private String nasSource;// 申请来源：paasOS，paasOS-UI
	private String applyContent;// 申请参数
	private String creater;// 申请人
	private Date createDate;// 申请时间
	private String allocateContent;// 分配参数
	private Date updateDate;// 修改时间
	private String tenantId;// 租户Id
	private String projectId;// 项目Id
	private String envId;// 环境Id
	private String envType;// 环境类别
	private Boolean onReady;// 是否创建完毕
	private int onStatus;// 操作状态
	private String nasType;// 存储类型：共享存储和块存储
	private String resDesc;// 描述信息
	private String applyType;//api,app or others
	
	private Double memory;
	private Double cpu;
	private Double storage;

	public String getNasId() {
		return nasId;
	}

	public void setNasId(String nasId) {
		this.nasId = nasId;
	}

	public String getNasSource() {
		return nasSource;
	}

	public void setNasSource(String nasSource) {
		this.nasSource = nasSource;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
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

	public int getOnStatus() {
		return onStatus;
	}

	public void setOnStatus(int onStatus) {
		this.onStatus = onStatus;
	}

	public String getNasType() {
		return nasType;
	}

	public void setNasType(String nasType) {
		this.nasType = nasType;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	// 获取paasOS的资源id
	public String getResId() {
		String allocation = this.getAllocateContent();
		if (!StringUtil.isNull(allocation)) {
			try {
				JSONObject obj = JSONObject.parseObject(allocation);
				return obj.getString("id");
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	// 获取版本信息
	public String getVersion() {
		String allocation = this.getAllocateContent();
		if (!StringUtil.isNull(allocation)) {
			try {
				JSONObject obj = JSONObject.parseObject(allocation);
				return obj.getString("version");
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

	// 申请资源基本参数
	public String getRequestEnvInfo() {
		String apply = this.getApplyContent();
		if (StringUtil.isNull(apply)) {
			return null;
		}
		try {
			JSONObject obj = JSONObject.parseObject(apply);
			return obj.getString("size");
		} catch (Exception e) {
			return null;
		}
	}

	// 分配资源存储参数
	public String getAllocateStorageInfo() {
		String apply = this.getApplyContent();
		if (StringUtil.isNull(apply)) {
			return null;
		}
		try {
			JSONObject obj = JSONObject.parseObject(apply);
			return obj.getString("size");
		} catch (Exception e) {
			return null;
		}
	}

	public String getNfsInfo() {
		String allocation = this.getAllocateContent();
		if (StringUtil.isNull(allocation)) {
			return null;
		}
		try {
			JSONObject obj = JSONObject.parseObject(allocation);
			String temp = "";
			temp += "nfsServer:" + obj.getString("nfsServer") + "\n";
			temp += "nfsPath:" + obj.getString("nfsPath");
			return temp;
		} catch (Exception e) {
			return null;
		}
	}

	public String getIscsiInfo() {
		String allocation = this.getAllocateContent();
		if (StringUtil.isNull(allocation)) {
			return null;
		}
		try {
			JSONObject obj = JSONObject.parseObject(allocation);
			String temp = "";
			temp += "iscsiPortal:" + obj.getString("iscsiPortal") + "\n";
			temp += "iscsiIQN:" + obj.getString("iscsiIQN") + "\n";
			temp += "iscsiLUN:" + obj.getString("iscsiLUN");
			return temp;
		} catch (Exception e) {
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
