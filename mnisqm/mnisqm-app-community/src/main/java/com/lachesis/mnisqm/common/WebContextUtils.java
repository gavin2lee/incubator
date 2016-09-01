/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnisqm.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lachesis.mnisqm.module.system.domain.SysUser;



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
	private static final ThreadLocal<SysUser> LOGIN_USER_INFO = new ThreadLocal<SysUser>();
	
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
	public static SysUser getSessionUserInfo() {
		HttpSession session = getHttpSession();
		
		SysUser sessionUserInfo = null;
		if (null != session.getAttribute(SESSION_USER_INFO) && null != ((SysUser)session.getAttribute(SESSION_USER_INFO))) {
			sessionUserInfo = (SysUser)session.getAttribute(SESSION_USER_INFO);
		}
		
		return sessionUserInfo;
	}
	
	public static void setLocalUserInfo(SysUser userInfo) {
		LOGIN_USER_INFO.set(userInfo);
	}
	
	public static SysUser getLocalUserInfo() {
		return LOGIN_USER_INFO.get();
	}
	
	public static void remove() {
		LOGIN_USER_INFO.remove();
	}
}

	