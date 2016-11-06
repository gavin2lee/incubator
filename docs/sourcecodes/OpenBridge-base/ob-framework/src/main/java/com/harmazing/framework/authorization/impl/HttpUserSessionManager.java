package com.harmazing.framework.authorization.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.util.StringUtil;

public class HttpUserSessionManager extends AbstractHttpUserManager {
	// 在内存中保持20分钟
	private static final int expirySeconds = 20 * 60 * 60 * 1000;

	public static AbstractCache<String, IUser> users = new AbstractCache<String, IUser>(
			true, expirySeconds);

	@Override
	public IUser getHttpUserByRequest(HttpServletRequest request, String token) {
		// 基于session存储用户
		IUser user = users.get(token);
		if (user != null) {
			return user;
		}
		return null;
	}

	@Override
	public void loginUserInit(HttpServletRequest request,
			HttpServletResponse response, IUser user, String token) {
		if (StringUtil.isNotNull(token)) {
			users.set(token, user);
		}
	}

	@Override
	public void logoutUserDestroy(HttpServletRequest request,
			HttpServletResponse response, String token) {
		if (StringUtil.isNotNull(token)) {
			users.remove(token);
		}
	}

}
