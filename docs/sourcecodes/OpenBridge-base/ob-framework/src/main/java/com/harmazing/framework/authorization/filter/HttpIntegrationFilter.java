package com.harmazing.framework.authorization.filter;

import java.io.IOException;

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

import com.harmazing.framework.authorization.IHttpUserManager;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.util.WebUtil;

public class HttpIntegrationFilter implements Filter {
	private final Log logger = LogFactory.getLog(this.getClass());
	private IHttpUserManager httpUserManager;

	public IHttpUserManager getHttpUserManager() {
		return httpUserManager;
	}

	public void setHttpUserManager(IHttpUserManager httpUserManager) {
		this.httpUserManager = httpUserManager;
	}

	@Override
	public void destroy() {
		logger.info("HttpIntegrationFilter destroy");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if ((request != null)
				&& (request.getAttribute("__harmazing_integration_filter__") != null)) {
			chain.doFilter(request, response);
		} else {
			if (request != null) {
				request.setAttribute("__harmazing_integration_filter__",
						Boolean.TRUE);
			}
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse res = (HttpServletResponse) response;
			IUser user = httpUserManager.getUserByRequest(req, res);
			if (user == null) {
				user = httpUserManager.getAnonymousUser(req, res);
			}
			WebUtil.setUserToRequest(req, user);
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		logger.info("HttpIntegrationFilter init");
	}

}
