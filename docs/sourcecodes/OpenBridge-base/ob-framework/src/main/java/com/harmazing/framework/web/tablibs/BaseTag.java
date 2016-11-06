package com.harmazing.framework.web.tablibs;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.harmazing.framework.util.StringUtil;

@SuppressWarnings("serial")
public abstract class BaseTag extends BodyTagSupport implements TryCatchFinally {

	protected static final Log logger = LogFactory.getLog(BaseTag.class);

	@Override
	public String getId() {
		return this.id;
	}

	protected void registerToParent() {
		BaseTag parent = ((BaseTag) findAncestorWithClass(this, BaseTag.class));
		if (parent != null) {
			parent.receiveSubTaglib(this);
		}
	}

	protected void receiveSubTaglib(BodyTagSupport taglib) {
		// 供子类覆盖实现
	}

	public String getContentPath() {
		return getRequest().getContextPath();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) this.pageContext.getRequest();
	}

	public String getRequestPath() {
		HttpServletRequest request = getRequest();
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

	public String contentPath(String url) {
		if (!url.startsWith("/")) {
			url = "/" + url;
		}
		return getContentPath() + url;
	}

	public void doCatch(Throwable t) throws Throwable {
		logger.error(getClass().getName() + ":error:", t);
		throw t;
	}

	public void doFinally() {
		try {
			release();
		} catch (Exception e) {
			logger.error(getClass().getName() + ":release:", e);
		}
	}

	@Override
	public void release() {
		super.release();
	}

}
