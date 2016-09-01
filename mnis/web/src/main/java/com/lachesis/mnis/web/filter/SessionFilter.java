/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.lachesis.mnis.core.identity.entity.SysUser;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;

/**
 * The class SessionFilter.
 * 
 * 所有的url请求都需要过滤，如果没有登录，则需要登录
 * 
 * @author: yanhui.wang
 * @since: 2014-6-12
 * @version: $Revision$ $Date$ $LastChangedBy$
 * 
 */
public class SessionFilter extends HandlerInterceptorAdapter {
	static final Logger LOGGER = LoggerFactory.getLogger(SessionFilter.class);
	
	private static final String LOGIN_URL 	= "/index/login";
	
	private static final String[] NO_FILTER_URL = { "/resources/",
			LOGIN_URL, "/index/doLogin",
			"/index/ndaDoLogin",
			"/index/loginWithFP","/index/doNdaLoginCard",
			"/nur/system/getMiddlewareVersion", "/init",
			"/nur/nurseWhiteBoardTV","/nur/patientGlance/getPateintBedList",
			"/nur/nurseWhiteBoard/getNwbRecordDtos",
			"/nur/nurseWhiteBoard/queryTempIdByDeptCode"};
	//"/nur/nurseWhiteBoardTV" 小白板
	//"/nur/patientGlance/getPateintBedList" 闭环输液调用患者信息
	
	private static final String[] NO_LOGIN_URL = {"/mnis/index/nurseLogin.do"};
	
	//private static final String NURSER_URL = "/mnis/index/patientMain2.do";

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		
		// exclude
		String url = request.getRequestURL().toString();
		
		for (String s : NO_LOGIN_URL) {
			if (url.contains(s)) {
				if(isNoLogin(request)){
					//response.sendRedirect(request.getContextPath().concat(NURSER_URL));
					return true;
				}
			}
		}
		
		
		// hasLogined
		UserInformation userInfo = WebContextUtils.getSessionUserInfo();
		if (userInfo != null) {
			// set current user info to thread
			WebContextUtils.setLocalUserInfo(userInfo);
			return true;
		}
		
		for (String s : NO_FILTER_URL) {
			if (url.contains(s)) {
				return true;
			}
		}
		
		// NDA response
		if (isFromNDA(request)) {
			response.setHeader("sessionstatus", "timeout");
			response.getOutputStream().print("{\"rslt\":"+ResultCst.LOGIN_TIMEOUT+"} ");					
			return false;
		}
		
		// XmlHttpRequest response
		if (isXHR(request)) {
			response.setHeader("sessionstatus", "timeout");
			response.getOutputStream().print("{\"rslt\":"+ResultCst.LOGIN_TIMEOUT+"} ");
			return false;
		}
		
		// normal, redirect to login
		response.sendRedirect(request.getContextPath().concat(LOGIN_URL));
		return true;
	}

	// is XmlHttpRequest?
	private boolean isXHR(HttpServletRequest request) {
		return request.getHeader("x-requested-with") != null
				&& request.getHeader("x-requested-with").equalsIgnoreCase(
						"XMLHttpRequest");
	}
	
	// is NOT LOG?
	private boolean isNoLogin(HttpServletRequest request) {
		Cookie[]  cookie = request.getCookies();
		if(cookie != null){
			String val = cookie[0].getValue();
		}
		UserInformation sessionUserInfo = new UserInformation();
		SysUser user = new SysUser();
		user.setCode("admin");
		user.setName("admin");
		sessionUserInfo.setDeptCode("5042");
		sessionUserInfo.setUser(user);
		setUserInformationToServer(request, sessionUserInfo);
		return true;
	}
	
	private void setUserInformationToServer(HttpServletRequest request,
			UserInformation userInformation) {
		HttpSession session = request.getSession(true);
		session.setAttribute(WebContextUtils.SESSION_USER_INFO, userInformation);
		WebContextUtils.setLocalUserInfo(userInformation);
	}

	private boolean isFromNDA(HttpServletRequest request) {
		String from = request.getParameter("loginType");
		if(org.apache.commons.lang.StringUtils.isNotBlank(from) &&  "nda".equals(from)){
			return true;
		}
		return false;
	}
}
