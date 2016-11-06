package com.harmazing.framework.authorization;

import java.io.Serializable;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;

@SuppressWarnings("serial")
public class LoginLog implements Serializable {
	private String logId;
	// 登录名
	private String loginName;
	// '登陆参数，密码或者token',
	private String loginParam;
	// 登陆时间
	private Date loginTime;
	// 登陆类型 1 为 SSO 登陆 0 为正常登录
	private Integer loginType;
	// 客户端IP
	private String clientIp;
	// 浏览器UserAgent leng=1024
	private String userAgent;
	// 登陆系统 api,app,paasos
	private String loginSys;
	// 用户ID
	private String userId;

	public LoginLog() {

	}

	/**
	 * 
	 * @param request
	 * @param user
	 * @param loginPraam
	 * @param loginType
	 *            0:密码登陆,1:Token登陆,2:用户穿越
	 * @param loginSys
	 *            app,api,os
	 */
	public LoginLog(HttpServletRequest request, IUser user, String loginPraam,
			Integer loginType) {
		this.logId = StringUtil.getUUID();
		this.loginName = user.getLoginName();
		this.userId = user.getUserId();
		this.loginParam = loginPraam;
		this.loginType = loginType;
		this.loginTime = new Date();
		if (loginSys != null && loginSys.length() > 10) {
			loginSys = loginSys.substring(0, 10);
		}

		this.setLoginSys(WebUtil.getSystemKey());
		this.setClientIp(WebUtil.getClientIp(request));
		if (loginPraam != null && loginPraam.length() > 256) {
			loginPraam = loginPraam.substring(0, 256);
		}
		this.setLoginParam(loginPraam);
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null && userAgent.length() > 1024)
			userAgent = userAgent.substring(0, 1024);
		this.setUserAgent(userAgent);
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginParam() {
		return loginParam;
	}

	public void setLoginParam(String loginParam) {
		this.loginParam = loginParam;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Integer getLoginType() {
		return loginType;
	}

	public void setLoginType(Integer loginType) {
		this.loginType = loginType;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getLoginSys() {
		return loginSys;
	}

	public void setLoginSys(String loginSys) {
		this.loginSys = loginSys;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
