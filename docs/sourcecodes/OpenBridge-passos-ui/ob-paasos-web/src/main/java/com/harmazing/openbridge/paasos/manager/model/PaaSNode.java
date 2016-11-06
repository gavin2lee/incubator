package com.harmazing.openbridge.paasos.manager.model;

import com.harmazing.framework.common.model.BaseModel;

public class PaaSNode extends BaseModel {

	//
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String name;
	
	private String envType;//环境类型
	
	private String orgId;//所属组织ID
	
	//以下为Kubernetes中的属性
	
	private String ip;
	
	private String cpu;
	
	private String ram;
	
	private String operateSystem;
	
	private int instanceCount;
	
	private String status="不存在";
	
	private String nodeEvnType;

	private String nodeType;
	
	private PaaSTenant tenant;
	
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

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getOperateSystem() {
		return operateSystem;
	}

	public void setOperateSystem(String operateSystem) {
		this.operateSystem = operateSystem;
	}

	public int getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNodeEvnType() {
		return nodeEvnType;
	}

	public void setNodeEvnType(String nodeEvnType) {
		this.nodeEvnType = nodeEvnType;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public PaaSTenant getTenant() {
		return tenant;
	}

	public void setTenant(PaaSTenant tenant) {
		this.tenant = tenant;
	}
	
	
}
