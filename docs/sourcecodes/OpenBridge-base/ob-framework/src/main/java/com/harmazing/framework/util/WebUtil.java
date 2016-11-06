package com.harmazing.framework.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.JspException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.util.WebUtils;

import com.harmazing.framework.authorization.IUser;

public class WebUtil extends WebUtils {
	public static final String REQUESTUSERKEY = "HMUser";
	private static ServletContext servletContext;

	public static void setServletContext(ServletContext servletContext) {
		WebUtil.servletContext = servletContext;
	}

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public static ApplicationContext getApplicationContext() {
		return WebApplicationContextUtils
				.getWebApplicationContext(getServletContext());
	}

	public static void setUserToRequest(HttpServletRequest request, IUser user) {
		request.setAttribute(REQUESTUSERKEY, user);
		if (user == null) {
			request.removeAttribute(REQUESTUSERKEY);
		}
	}

	public static IUser getUserByRequest(HttpServletRequest request) {
		IUser user = (IUser) request.getAttribute(REQUESTUSERKEY);
		if (user != null && StringUtil.isNotNull(user.getUserId())) {
			return user;
		} else {
			return null;
		}
	}

	public static String getSystemKey() {
		return WebUtil.getServletContext().getInitParameter("system.key");
	}

	public static String getClientIp(HttpServletRequest request) {
		// client Ip
		String ip = request.getHeader("X-Forwarded-For");
		if (!StringUtil.isNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (!StringUtil.isNull(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}

	public static String getUrlString(HttpServletRequest request,
			HttpServletResponse response, String url) throws Exception {
		ImportResponseWrapper warpper = new ImportResponseWrapper(response);
		RequestDispatcher rd = getServletContext().getRequestDispatcher(url);
		try {
			rd.include(request, warpper);
		} catch (IOException ex) {
			throw new JspException(ex);
		} catch (RuntimeException ex) {
			throw new JspException(ex);
		} catch (ServletException ex) {
			Throwable rc = ex.getRootCause();
			if (rc == null)
				throw new JspException(ex);
			else
				throw new JspException(rc);
		}

		return warpper.getString();
	}

	private static class ImportResponseWrapper extends
			HttpServletResponseWrapper {

		public ImportResponseWrapper(HttpServletResponse response) {
			super(response);
		}

		public static final String DEFAULT_ENCODING = "UTF-8";
		private final StringWriter sw = new StringWriter();

		private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

		private final ServletOutputStream sos = new ServletOutputStream() {

			public void write(int b) throws IOException {
				bos.write(b);
			}

			public boolean isReady() {
				return false;
			}

			public void setWriteListener(WriteListener writeListener) {

			}

		};

		private boolean isWriterUsed = false;
		private boolean isStreamUsed = false;

		@Override
		public PrintWriter getWriter() throws IOException {
			if (isStreamUsed)
				throw new IllegalStateException("已经使用Stream作为输出方式!");
			isWriterUsed = true;
			return new PrintWriter(sw);
		}

		@Override
		public ServletOutputStream getOutputStream() {
			if (isWriterUsed)
				throw new IllegalStateException("已经使用Writer作为输出方式!");
			isStreamUsed = true;
			return sos;
		}

		public String getString() throws UnsupportedEncodingException {
			if (isWriterUsed)
				return sw.toString();
			else if (isStreamUsed) {
				return bos.toString(DEFAULT_ENCODING);
			} else
				return "";
		}
	}

}