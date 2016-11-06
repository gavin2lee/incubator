package com.harmazing.openbridge.web.tablibs.form;



import java.util.List;

import com.harmazing.framework.util.JsonUtil;
import com.harmazing.framework.util.StringUtil;

public class TagTag extends AbstractFormTag {
	
	private String placeholder;
	
	private List<String> source;
	
	public List<String> getSource() {
		return source;
	}

	public void setSource(List<String> source) {
		this.source = source;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public void release() {
		this.placeholder = null;
		super.release();
	}

	protected String acquireString(String body) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("<input type=\"text\" name=\"" + this.getId() + "\" id=\""
				+ this.getId() + "\"");
		if (StringUtil.isNotNull(this.getPlaceholder())) {
			sb.append(" placeholder='" + this.getPlaceholder() + "'");
		}
		sb.append("/>");
		sb.append("\r\n<script>\r\n");
		sb.append("var " + this.getId() + "_tag = $('#" + this.getId()
				+ "');\r\n");
		sb.append("" + this.getId() + "_tag.tag({placeholder:" + this.getId()
				+ "_tag.attr('placeholder'),width:'80%',source:"+JsonUtil.toJsonString(source)+"});\r\n");
		if (this.getValue() != null) {
			String[] texts = StringUtil.split(this.getValue());
			if (texts != null) {
				for (int i = 0; i < texts.length; i++) {
					sb.append("" + this.getId() + "_tag.data('tag').add('"
							+ texts[i] + "');\r\n");
				}
			}
		}
		sb.append("</script>");
		return super.acquireString(sb.toString());
	}
}
