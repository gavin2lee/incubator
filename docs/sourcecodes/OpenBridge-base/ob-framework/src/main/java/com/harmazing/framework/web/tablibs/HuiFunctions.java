package com.harmazing.framework.web.tablibs;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.harmazing.framework.util.JavaScript;
import com.harmazing.framework.util.StringUtil;

public class HuiFunctions {
	
	private static Map<String,Method> reference = new HashMap<String,Method>();
	
	public static String test() {
		return "test";
	}

	public static String concat(String str1, String str2) {
		return str1 + str2;
	}

	public static String upperFirst(String str) {
		return StringUtil.upperFirst(str);
	}

	public static Boolean urlStartsWith(String key, HttpServletRequest req) {
		return getRequestPath(req).startsWith(key);
	}

	public static String urlEncode(Object obj) {
		return StringUtil.urlEncode(obj);
	}

	public static String escape(String str) {
		return JavaScript.escape(str);
	}

	public static String unescape(String str) {
		return JavaScript.unescape(str);
	}

	public static String urlPath(Object obj, HttpServletRequest request) {
		if (StringUtil.isNull((String) obj)) {
			return null;
		} else {
			String temp = obj.toString();
			if (temp.startsWith("http://") || temp.startsWith("https://")) {
				return obj.toString();
			} else if (temp.startsWith("/")) {
				return request.getContextPath() + temp;
			} else {
				return temp;
			}
		}
	}

	public static String fileName(String path) {
		if (path == null)
			return path;
		if (path.lastIndexOf("/") >= 0) {
			return path.substring(path.lastIndexOf("/") + 1);
		}
		return path;
	}

	private static String getRequestPath(HttpServletRequest request) {
		String sPath = (String) request
				.getAttribute("javax.servlet.forward.servlet_path");
		if (StringUtil.isNotNull(sPath)) {
			return sPath;
		} else {
			String pathInfo = request.getPathInfo();
			String queryString = request.getQueryString();
			String uri = request.getServletPath();
			if (uri == null) {
				uri = request.getRequestURI();
				uri = uri.substring(request.getContextPath().length());
			}
			String temp = uri + ((pathInfo == null) ? "" : pathInfo);
			temp = temp
					+ ((queryString == null) ? "" : new StringBuffer()
							.append("?").append(queryString).toString());
			return temp;
		}
	}

	public static Boolean containRole(String roles, String role) {
		if (roles != null && !roles.equals("")) {
			String[] allroles = StringUtil.split(roles);
			for (String r : allroles) {
				if (r.equals(role)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static Object staticMethod(String className,String methodName,String key){
		Method me = reference.get(className+"-"+methodName);
		try{
			Class clazz = Class.forName(className);
			Object o = clazz.newInstance();
			if(me==null){
				Method method = clazz.getMethod(methodName, String.class);
				reference.put(className+"-"+methodName,method);
			}
			me = reference.get(className+"-"+methodName);
			Object result = me.invoke(o, key);
			return result;
		}
		catch(Exception e){
			return null;
		}
		
	}
}
