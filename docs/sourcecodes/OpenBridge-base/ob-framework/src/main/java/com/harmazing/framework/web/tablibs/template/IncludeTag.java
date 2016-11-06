package com.harmazing.framework.web.tablibs.template;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.DynamicAttributes;

import org.apache.taglibs.standard.tag.common.core.ParamSupport.ParamManager;

public class IncludeTag extends BaseTag implements DynamicAttributes {

	public void setDynamicAttribute(String uri, String key, Object value)
			throws JspException {
		if (this.params == null) {
			this.params = new ParamManager();
		}
		if (value != null) {
			value = value.toString();
		}
		this.params.addParameter(key, (String) value);
	}

	protected ParamManager params = null;
	public static final String DEFAULT_ENCODING = "UTF-8";
	protected String file;

	public void setFile(String file) {
		this.file = file;
	}

	protected String charEncoding;

	public void setCharEncoding(String charEncoding) {
		this.charEncoding = charEncoding;
	}

	@Override
	protected void clearResource() {
		this.file = null;
		this.charEncoding = null;
		this.params = null;
		super.clearResource();
	}

	protected String getFile() {
		if (this.params != null) {
			file = params.aggregateParams(file);
		}
		return file;
	}

	public String coverUrl(String targetUrl) {
		if (!targetUrl.startsWith("/")) {
			String sp = ((HttpServletRequest) pageContext.getRequest())
					.getServletPath();
			targetUrl = sp.substring(0, sp.lastIndexOf('/')) + '/' + targetUrl;
		}
		return targetUrl;
	}

	public int doStartTag() throws JspException {
		ResponseWriter writer = null;
		if (!TemplateUtil.hasContext(pageContext)) {
			writer = new ResponseWriter(pageContext.getOut());
		} else {
			writer = new ResponseWriter(TemplateUtil.get(pageContext),
					pageContext.getOut());
		}
		TemplateUtil.safePut(pageContext, writer);
		try {
			String targetUrl = coverUrl(getFile());
			RequestDispatcher rd = pageContext.getServletContext()
					.getRequestDispatcher(targetUrl);
			// 获取模板内容
			rd.include(pageContext.getRequest(), new ResponseWrapper(
					(HttpServletResponse) pageContext.getResponse(), writer));
			writer.start();
		} catch (Exception e) {
			throw new JspException(e);
		}
		return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspException {
		ResponseWriter writer = TemplateUtil.get(pageContext);
		try {
			writer.end();
		} catch (IOException e) {
			throw new JspException(e);
		} finally {
			writer.release(pageContext);
		}
		return super.doEndTag();
	}

}
