package com.harmazing.openbridge.sys.role.model;

import java.util.List;
import java.util.Map;

public class SysRole {

	private String roleId;

	private String roleName;

	private String roleDesc;

	private String roleSystem;

	private String userIds;

	private String funcIds;

	private List<Map<String, Object>> funs;

	private List<Map<String, Object>> users;

	public String getRoleSystem() {
		return roleSystem;
	}

	public void setRoleSystem(String roleSystem) {
		this.roleSystem = roleSystem;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getFuncIds() {
		return funcIds;
	}

	public void setFuncIds(String funcIds) {
		this.funcIds = funcIds;
	}

	public List<Map<String, Object>> getFuns() {
		return funs;
	}

	public void setFuns(List<Map<String, Object>> funs) {
		this.funs = funs;
	}

	public List<Map<String, Object>> getUsers() {
		return users;
	}

	public void setUsers(List<Map<String, Object>> users) {
		this.users = users;
	}

}
