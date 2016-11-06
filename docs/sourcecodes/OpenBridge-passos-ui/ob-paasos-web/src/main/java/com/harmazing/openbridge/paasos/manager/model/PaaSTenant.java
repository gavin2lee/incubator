package com.harmazing.openbridge.paasos.manager.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.harmazing.framework.common.model.BaseModel;
import com.harmazing.openbridge.sys.user.model.SysUser;

public class PaaSTenant extends BaseModel {

	//
	private static final long serialVersionUID = 1L;

	private String tenantId;
	
	private String tenantName;
	
	private String description;
	
	private SysUser admin;
	
	private List<SysUser> member = new ArrayList<SysUser>();
	
	private Date createTime;
	
	private Integer deployCount;
	
	private List<PaaSTenantQuota> tenantQuotaList = new ArrayList<PaaSTenantQuota>();

	private List<PaaSTenantPreset> presetList = new ArrayList<PaaSTenantPreset>();
	
	public List<PaaSTenantPreset> getPresetList() {
		return presetList;
	}

	public void setPresetList(List<PaaSTenantPreset> presetList) {
		this.presetList = presetList;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SysUser getAdmin() {
		return admin;
	}

	public void setAdmin(SysUser admin) {
		this.admin = admin;
	}

	public List<SysUser> getMember() {
		return member;
	}

	public void setMember(List<SysUser> member) {
		this.member = member;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getDeployCount() {
		return deployCount;
	}

	public void setDeployCount(Integer deployCount) {
		this.deployCount = deployCount;
	}

	public List<PaaSTenantQuota> getTenantQuotaList() {
		return tenantQuotaList;
	}

	public void setTenantQuotaList(List<PaaSTenantQuota> tenantQuotaList) {
		this.tenantQuotaList = tenantQuotaList;
	}

	
	
	
}
