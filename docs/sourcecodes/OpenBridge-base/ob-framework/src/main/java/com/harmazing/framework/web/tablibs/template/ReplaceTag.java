package com.harmazing.framework.web.tablibs.template;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

public class ReplaceTag extends NamedTag {

	public int doStartTag() throws JspException {
		Tag parent = getParent();
		if (parent == null || !(parent instanceof IncludeTag))
			throw new JspException("Tag Error");
		return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspException {
		try {
			TemplateUtil.getTop(pageContext).replace(getName(), getBodyContent());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return super.doEndTag();
	}

}
