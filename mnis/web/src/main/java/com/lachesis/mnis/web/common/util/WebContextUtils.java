/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web.common.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lachesis.mnis.core.identity.entity.UserInformation;


/**
 * The class WebContextUtils.
 * 
 * 前端帮助类
 *
 * @author: yanhui.wang
 * @since: 2014-6-12	
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
public final class WebContextUtils {

	public static final String SESSION_USER_INFO = "sessionUserInfo";
	
	//绑定用户信息到当前线程
	private static final ThreadLocal<UserInformation> LOGIN_USER_INFO = new ThreadLocal<UserInformation>();
	
	private WebContextUtils() {}
	
	/**
	 * get session for web application
	 * @return
	 */
	public static HttpSession getHttpSession() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getSession();
	}
	
	/**
	 * get user information from session.
	 * @return
	 */
	public static UserInformation getSessionUserInfo() {
		HttpSession session = getHttpSession();
		
		UserInformation sessionUserInfo = null;
		if (null != session.getAttribute(SESSION_USER_INFO) && null != ((UserInformation)session.getAttribute(SESSION_USER_INFO)).getUser()) {
			sessionUserInfo = (UserInformation)session.getAttribute(SESSION_USER_INFO);
		}
		
		return sessionUserInfo;
	}
	
	public static void setLocalUserInfo(UserInformation userInfo) {
		LOGIN_USER_INFO.set(userInfo);
	}
	
	public static UserInformation getLocalUserInfo() {
		return LOGIN_USER_INFO.get();
	}
	
	public static void remove() {
		LOGIN_USER_INFO.remove();
	}
}

	