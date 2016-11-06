package com.harmazing.framework.authorization;

public interface IUser {
	public String getUserId();

	public String getUserName();

	public String getLoginName();

	public String[] getUserRole();

	public String[] getUserFunc();

	public String getTenantId();

	public String getTenantName();

	public boolean isAnonymous();

	public boolean isAdministrator();
}
