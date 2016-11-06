package com.harmazing.openbridge.paasos.resource.model;

import java.util.Date;

import com.harmazing.framework.common.model.BaseModel;
/***
 * add on 20160613
 * @author taoshuangxi
 * Used for query resource information from view os_resource
 */
@SuppressWarnings("serial")
public class PaasResource extends BaseModel {
	private String resId;
	private String resName;
	private String applyContent;
	private String creater;
	private Date createDate;
	private Date updateDate;
	private String allocateContent;
	private String tenantId;
	private String projectId;
	private String envId;
	private String envType;
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
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
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getAllocateContent() {
		return allocateContent;
	}
	public void setAllocateContent(String allocateContent) {
		this.allocateContent = allocateContent;
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
}
