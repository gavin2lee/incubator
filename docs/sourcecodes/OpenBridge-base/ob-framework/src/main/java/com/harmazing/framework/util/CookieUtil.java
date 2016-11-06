package com.harmazing.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {
	public static Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(cookieName)) {
					return cookies[i];
				}
			}
		}
		return null;
	}

	public static String getTokenDomain(HttpServletRequest request) {
		String serverName = request.getServerName();
		String[] xx = serverName.split("\\.");
		if (xx.length == 4 && CookieUtil.isIPAddress(request.getServerName())) {
			return null;
		} else {
			return serverName;
		}
	}

	public static boolean isIPAddress(String addr) {
		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
			return false;
		}
		/**
		 * 判断IP格式和范围
		 */
		String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
		Pattern pat = Pattern.compile(rexp);
		Matcher mat = pat.matcher(addr);
		return mat.find();
	}

	public static boolean isWeiXiBrowser(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		if (userAgent != null) {
			if (userAgent.indexOf("MicroMessenger") >= 0) {
				return true;
			}
		}
		return false;
	}
}
