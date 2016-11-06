package com.harmazing.framework.web.tablibs.template;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

public class SuperTag extends BaseTag {

	public int doStartTag() throws JspException {
		Tag tag = getParent();
		if (tag == null || !(tag instanceof ReplaceTag)) {
			throw new JspTagException("super标签没在replace标签内");
		}
		ReplaceTag replace = (ReplaceTag) tag;
		try {
			ResponseWriter writer = TemplateUtil.getTop(pageContext);
			writer.superTag(replace.getName(), replace.getBodyContent());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}
}
