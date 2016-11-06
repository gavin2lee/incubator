package com.harmazing.framework.authorization.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.util.RedisUtil;
import com.harmazing.framework.util.StringUtil;

public class RedisUserSessionManager extends AbstractHttpUserManager {

	// PC端Token信息在NoSql中的有效期
	private static final int pcTokenExpirySeconds = 12 * 60 * 60;

	@Override
	public IUser getHttpUserByRequest(HttpServletRequest request, String token) {
		if (StringUtil.isNotNull(token)) {
			IUser user = (IUser) RedisUtil.getObject(token);
			if (user != null) {
				return user;
			}
		}
		return null;
	}

	@Override
	public void loginUserInit(HttpServletRequest request,
			HttpServletResponse response, IUser user, String token) {
		if (StringUtil.isNotNull(token)) {
			RedisUtil.setObject(token, user, pcTokenExpirySeconds);
		}
	}

	@Override
	public void logoutUserDestroy(HttpServletRequest request,
			HttpServletResponse response, String token) {
		if (StringUtil.isNotNull(token)) {
			RedisUtil.delete(token);
		}
	}

}
