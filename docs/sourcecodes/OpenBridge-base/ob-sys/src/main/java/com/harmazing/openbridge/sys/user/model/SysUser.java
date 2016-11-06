package com.harmazing.openbridge.sys.user.model;

import java.util.Date;

import com.harmazing.framework.authorization.IUser;

@SuppressWarnings("serial")
public class SysUser extends SysIdentity implements IUser {

	private String loginName;
	private String loginPassword;
	private String email;
	private String roles;
	private Boolean activate;
	private Date createTime;
	private String apiAuthKey;
	private String token;
	private String[] groups;
	private boolean sysUser;
	private String tenantId;
	private String tenantName;
	private String mobile;
	private String[] userFunc;

	public SysUser() {

	}

	public String[] getUserFunc() {
		return userFunc;
	}

	public void setUserFunc(String[] userFunc) {
		this.userFunc = userFunc;
	}
	public String getApiAuthKey() {
		return apiAuthKey;
	}

	public void setApiAuthKey(String apiAuthKey) {
		this.apiAuthKey = apiAuthKey;
	}
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public static SysUser create(String userId) {
		SysUser user = new SysUser();
		user.setUserId(userId);
		return user;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getActivate() {
		return activate;
	}

	public void setActivate(Boolean activate) {
		this.activate = activate;
	}

	public String getUserId() {
		return this.getId();
	}

	public void setUserId(String userId) {
		this.setId(userId);
	}

	public String getUserName() {
		return this.getName();
	}

	public void setUserName(String userName) {
		this.setName(userName);
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Override
	public String[] getUserRole() {
		if (roles == null) {
			return null;
		} else {
			return roles.trim().split("\\s*[,;]\\s*");
		}
	}

	@Override
	public boolean isAnonymous() {
		return false;
	}

	@Override
	public boolean isAdministrator() {
		// 判断是不是初始化的admin用户
		if (this.getLoginName().equals("admin"))
			return true;
		// 判断用户角色是否包含administrator
		String[] roles = this.getUserRole();
		if (roles != null) {
			for (String role : roles) {
				if (role.equals("administrator")) {
					return true;
				}
			}
		}
		return false;
	}

	public void setUserGroup(String[] groups) {
		this.groups = groups;
	}

	public boolean isSysUser() {
		return sysUser;
	}

	public void setSysUser(boolean sysUser) {
		this.sysUser = sysUser;
	}

	@Override
	public String getTenantId() {
		return this.tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String getTenantName() {
		return this.tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
}
