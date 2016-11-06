package com.harmazing.openbridge.sys.user.model;

@SuppressWarnings("serial")
public class SysUserDepartment extends SysIdentity{
	private String relationId;
	private String userId;
	private String depId;
	
	public String getDepId() {
		return depId;
	}
	public void setDepId(String depId) {
		this.depId = depId;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
