package com.harmazing.openbridge.paasos.project.model;

import java.util.Date;

import com.harmazing.framework.common.model.BaseModel;

public class PaasProject extends BaseModel {

	private static final long serialVersionUID = 1L;

	/**
	 * paasos项目ID  在paasos中生成
	 */
	private String projectId; 

	/**
	 * 租户ID 默认在 paasos controller会生存
	 */
	private String tenantId;
	private String tenantName; 
	
	/**
	 * 项目类型 api app store
	 */
	private String projectType; 
	
	/**
	 * 关联api app store 项目的项目id
	 */
	private String businessId;
	
	/**
	 * 项目名称
	 */
	private String projectName; 
	
	/**
	 * 创建用户ID
	 */
	private String createUser; 
	
	private String createUserName;
	
	/**
	 * 创建时间
	 */
	private Date createDate;
	
	/**
	 * 描述
	 */
	private String description;
	
	/**
	 * 项目编码
	 */
	private String projectCode;
	

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
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
	/*public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}*/
	
	

}
