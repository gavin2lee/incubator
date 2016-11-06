package com.harmazing.framework.web.tablibs.template;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TryCatchFinally;
 
public class BlockTag extends NamedTag implements TryCatchFinally {

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			ResponseWriter writer = TemplateUtil.getTop(pageContext);
			pageContext.getOut().append(writer.getBlockFlag(getName()));
			writer.addBlockContent(getName(), getBodyContent() == null ? ""
					: getBodyContent().getString());
		} catch (IOException e) {
			throw new JspException(e);
		}
		return super.doEndTag();
	}
}
