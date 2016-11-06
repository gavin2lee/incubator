package com.harmazing.openbridge.web.tablibs.form;

import com.harmazing.framework.util.StringUtil;

public class UserTag extends AbstractFormTag {
	private Boolean multiple;

	public void release() {
		this.multiple = null;
		super.release();
	}

	public Boolean getMultiple() {
		return multiple == null ? false : multiple;
	}

	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

	public String getStyle() {
		if (this.getMultiple()) {
			return super.getStyle() + ";min-height:80px;";
		} else {
			return super.getStyle();
		}
	}

	protected String acquireString(String body) throws Exception {
		StringBuffer sb = new StringBuffer();

		sb.append("<input " + getName() + " type='hidden' value=\""
				+ this.getValue() + "\" />");

		sb.append("<div " + buildAttribute() + " id=\"" + this.getId()
				+ "_text\" class=\"tags\">");
		String[] ids = StringUtil.split(this.getValue());
		String[] texts = StringUtil.split(body.trim());

		if (texts != null) {
			for (int i = 0; i < texts.length; i++) {
				sb.append("<span class=\"tag\" data-uid=\"" + ids[i] + "\">"
						+ texts[i]);
				sb.append("<button type=\"button\" class=\"close\" onclick=\"common.removeUser(this,'"
						+ this.getId() + "')\">×</button>");
				sb.append("</span>");
			}
		}

		sb.append("</div>");
		sb.append("<button type=\"button\" onclick=\"common.dialogUserSelect("
				+ this.getMultiple()
				+ ",'"
				+ this.getId()
				+ "','"
				+ body.trim()
				+ "')\" style='position:absolute; bottom:0px;top:0px;' class=\"btn btn-sm btn-choose\">选择</button>");

		return sb.toString();
	}
}
