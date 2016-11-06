package com.harmazing.framework.web.tablibs.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

@SuppressWarnings("serial")
public abstract class AbstractAuthTag extends BodyTagSupport {
	protected String value;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int doStartTag() {
		if (isValidation())
			return EVAL_BODY_INCLUDE;
		else
			return SKIP_BODY;
	}

	public int doEndTag() throws JspException {
		int tag = super.doEndTag();
		release();
		return tag;
	}

	@Override
	public void release() {
		this.value = null;
		super.release();
	}

	public String getContentPath() {
		return getRequest().getContextPath();
	}

	public HttpServletRequest getRequest() {
		return (HttpServletRequest) this.pageContext.getRequest();
	}

	protected abstract boolean isValidation();
}
