package com.harmazing.framework.web.tablibs;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import com.harmazing.framework.util.StringUtil;

@SuppressWarnings("serial")
public abstract class ComponentTag extends BaseTag {
	protected String style;
	protected String css;
	protected String tag;

	public String getStyle() {
		return this.style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getCss() {
		return css == null ? "" : css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String getTag() {
		return tag == null ? "div" : tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_BUFFERED;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			String body = this.bodyContent == null ? "" : this.bodyContent
					.getString();
			pageContext.getOut()
					.append(acquireString(body == null ? "" : body));
		} catch (Exception e) {
			logger.error(e);
			throw new JspTagException(e);
		}
		registerToParent();
		release();
		return EVAL_PAGE;
	}

	public void release() {
		this.css = null;
		this.style = null;
		this.tag = null;
		super.release();
	}

	protected String buildAttribute() {
		String temp = "";
		if (StringUtil.isNotNull(this.getId())) {
			temp += " id=\"" + this.getId() + "\"";
		}
		if (StringUtil.isNotNull(this.getStyle())) {
			temp += " style=\"" + this.getStyle() + "\"";
		}
		if (StringUtil.isNotNull(this.getCss())) {
			temp += " class=\"" + this.getCss() + "\"";
		}
		return temp;
	}

	protected String acquireString(String body) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("<" + this.getTag() + this.buildAttribute() + ">");
		sb.append(body);
		sb.append("</" + this.getTag() + ">");
		return sb.toString();
	}

}
