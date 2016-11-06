package com.harmazing.framework.authorization.impl;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.harmazing.framework.authorization.IHttpUserManager;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.IUserService;
import com.harmazing.framework.authorization.LoginLog;
import com.harmazing.framework.util.CookieUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;

public abstract class AbstractHttpUserManager implements IHttpUserManager {

	private IUserService userService;

	private YiheTokenGenerator tokenGenerator = YiheTokenGenerator
			.getInstance();

	public IUserService getUserService() {
		return userService;
	}

	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	public abstract IUser getHttpUserByRequest(HttpServletRequest request,
			String token);

	public abstract void loginUserInit(HttpServletRequest request,
			HttpServletResponse response, IUser user, String token);

	public abstract void logoutUserDestroy(HttpServletRequest request,
			HttpServletResponse response, String token);

	@Override
	public void userLogin(HttpServletRequest request,
			HttpServletResponse response, IUser user, LoginLog log) {
		String domain = CookieUtil.getTokenDomain(request);
		String tokenValue = tokenGenerator.signature(user.getUserId());
		Cookie cookie = new Cookie(YiheTokenGenerator.getTokenCookieName(),
				tokenValue);
		if (StringUtil.isNotNull(domain)) {
			cookie.setDomain(domain);
		}
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
		WebUtil.setUserToRequest(request, user);
		loginUserInit(request, response, user, tokenValue);
		PaasAPIUtil.tokenCacheDelete(user.getUserId());
		this.userService.writeUserLoginLog(log);
	}

	@Override
	public void userLogout(HttpServletRequest request,
			HttpServletResponse response) {
		String domain = CookieUtil.getTokenDomain(request);
		Cookie[] cookies = request.getCookies();
		String tokenValue = "";
		for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookie.getName().equalsIgnoreCase(
					YiheTokenGenerator.getTokenCookieName())) {
				tokenValue = cookie.getValue();
			}
			Cookie remove1 = new Cookie(cookie.getName(), null);
			remove1.setMaxAge(0);
			if (StringUtil.isNotNull(domain)) {
				remove1.setDomain(domain);
			}
			remove1.setPath("/");
			response.addCookie(remove1);
		}
		WebUtil.setUserToRequest(request, null);
		logoutUserDestroy(request, response, tokenValue);
	}

	@Override
	public IUser getUserByRequest(HttpServletRequest request,
			HttpServletResponse response) {
		String token = tokenGenerator.getTokenByRequest(request);
		String userId = tokenGenerator.verifyToken(token);
		IUser user = null;
		if (StringUtil.isNotNull(userId)) {
			user = getHttpUserByRequest(request, token);
		}
		if (user != null) {
			return user;
		}
		// paasos 产品之间相互访问,内部产品之间相互SSO
		if (StringUtil.isNotNull(userId)) {
			user = getUserService().getUserById(userId);
			if (user != null) {
				loginUserInit(request, response, user, token);
				this.userService.writeUserLoginLog(new LoginLog(request, user,
						"token:" + token, 1));
			}
			return user;
		}
		return null;

	}

	@Override
	public IUser getAnonymousUser(HttpServletRequest request,
			HttpServletResponse response) {
		return new IUser() {
			@Override
			public String[] getUserRole() {
				return null;
			}

			public String getLoginName() {
				return null;
			}

			@Override
			public String getUserName() {
				return "匿名用户";
			}

			@Override
			public String getUserId() {
				return "anonymous";
			}

			@Override
			public boolean isAnonymous() {
				return true;
			}

			@Override
			public boolean isAdministrator() {
				return false;
			}

			@Override
			public String getTenantId() {
				return null;
			}

			@Override
			public String getTenantName() {
				return null;
			}

			@Override
			public String[] getUserFunc() {
				return null;
			}
		};
	}
}
