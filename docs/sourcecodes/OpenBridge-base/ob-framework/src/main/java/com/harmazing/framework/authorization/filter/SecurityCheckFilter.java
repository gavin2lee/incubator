package com.harmazing.framework.authorization.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.authorization.ValidatorUtil;
import com.harmazing.framework.authorization.exceptions.AuthorizationException;
import com.harmazing.framework.authorization.exceptions.PermissionException;
import com.harmazing.framework.util.ResourceUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.UrlUtils;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.framework.util.XmlUtil;

public class SecurityCheckFilter implements Filter {

	private static final Log logger = LogFactory
			.getLog(SecurityCheckFilter.class);
	private String accessDenied = "/common/jsp/403.jsp";
	private String loginPage = "/login.jsp";

	public void setAnonymousPaths(String anonymousPaths) {
		ValidatorUtil.setAnonymousPaths(anonymousPaths);
	}

	public String getLoginPage() {
		if (loginPage.indexOf("?") > 0)
			return loginPage + "&";
		else
			return loginPage + "?";
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public void setAccessDenied(String path) {
		this.accessDenied = path;
	}

	@Override
	public void destroy() {
		ValidatorUtil.setSecurityConfigVO(null);
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponse response = (HttpServletResponse) arg1;
		boolean isChecked = false;
		String msg = "";
		try {
			isChecked = ValidatorUtil.checkRequest(request);
		} catch (PermissionException e) {
			msg = e.getMessage();
			isChecked = false;
		} catch (AuthorizationException e) {
			msg = e.getMessage();
			isChecked = false;
		} catch (Exception e) {
			msg = e.getMessage();
			logger.error("权限验证时出错误", e);
			isChecked = false;
		}
		if (isChecked) {
			chain.doFilter(arg0, arg1);
		} else {
			String requestURL = ValidatorUtil.getRequestUrl(request, true);
			IUser user = WebUtil.getUserByRequest(request);
			if (user.isAnonymous()) {
				// 已经登陆的用户，返回到登陆页面或权限拒绝的JSON
				response.setHeader("login", "true");
				if (requestURL == null || requestURL.lastIndexOf("null") > -1)
					requestURL = "/";
				if (isAjaxRequest(request)) {
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write(accessDeniedJSON("权限拒绝:" + msg));
				} else {
					response.sendRedirect(UrlUtils.getBasePath(request)
							+ getLoginPage()
							+ "targetUrl="
							+ StringUtil.urlEncode(request.getContextPath()
									+ requestURL));
				}
			} else {
				// 未登陆的用户重定向到403页面或权限拒绝的JSON
				if (isAjaxRequest(request)) {
					response.setContentType("application/json;charset=UTF-8");
					response.getWriter().write(accessDeniedJSON("权限拒绝:" + msg));
				} else {
					request.getRequestDispatcher(accessDenied).forward(request,
							response);
				}
			}
		}
	}

	private String accessDeniedJSON(String msg) {
		JSONObject json = new JSONObject();
		json.put("code", 403);
		json.put("msg", msg);
		return json.toJSONString();
	}

	private boolean isAjaxRequest(HttpServletRequest request) {
		if (request.getHeader("X-Requested-With") != null
				&& "XMLHttpRequest".equals(request
						.getHeader("X-Requested-With"))) {
			return true;
		}
		if (request.getHeader("User-Agent") != null
				&& request.getHeader("User-Agent").indexOf("PaasAPIUtil") >= 0) {
			return true;
		}
		return false;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		String configLocation = arg0.getServletContext().getInitParameter(
				"securityConfigLocation");
		try {
			InputStream input = null;
			if (StringUtil.isNull(configLocation)) {
				configLocation = "classpath:config/security.xml";
			}
			Resource[] r = ResourceUtil.getResources(configLocation);
			input = r[0].getInputStream();
			if (input != null) {
				try {
					Document doc = XmlUtil.buildDocument(input);
					ValidatorUtil
							.setSecurityConfigVO(new SecurityConfigVO(doc));
				} catch (Exception e) {
					throw new ServletException("configLocation解析出错", e);
				} finally {
					try {
						if (input != null)
							input.close();
					} catch (Exception e2) {

					}
				}
			}
		} catch (MalformedURLException e) {
			throw new ServletException("securityConfigLocation 参数不正确", e);
		} catch (IOException e) {
			throw new ServletException("securityConfigLocation 参数不正确", e);
		}
	}

}
