package com.harmazing.framework.web.tablibs.template;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class BaseTag extends BodyTagSupport implements TryCatchFinally {

	protected final Log logger = LogFactory.getLog(getClass());

	public void doCatch(Throwable t) throws Throwable {
		logger.error("标签(" + getClass().getSimpleName() + ")执行出现错误：", t);
		if (!(t instanceof JspException)) {
			throw new JspException(t);
		}
		throw t;
	}

	public void doFinally() {
		try {
			release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void release() {
		super.release();
		clearResource();
	}

	protected void clearResource() {

	}

}
