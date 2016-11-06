package com.harmazing.framework.authorization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IHttpUserManager {
	public IUser getUserByRequest(HttpServletRequest request,
			HttpServletResponse response);

	public IUser getAnonymousUser(HttpServletRequest request,
			HttpServletResponse response);

	public void userLogin(HttpServletRequest request,
			HttpServletResponse response, IUser user, LoginLog log);

	public void userLogout(HttpServletRequest request,
			HttpServletResponse response);
}
