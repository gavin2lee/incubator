package com.harmazing.openbridge.web.tablibs.form;

import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.web.tablibs.ComponentTag;
 
public abstract class AbstractFormTag extends ComponentTag {
	protected String value;
	protected Boolean readonly;

	public Boolean getReadonly() {
		return readonly == null ? false : readonly;
	}

	public void setReadonly(Boolean readonly) {
		this.readonly = readonly;
	}

	public String getValue() {
		return StringUtil.isNull(value) ? "" : value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void release() {
		this.value = null;
		this.readonly = null;
		super.release();
	}

	protected String getName() {
		String temp = "";
		if (StringUtil.isNotNull(this.getId())) {
			temp += " id=\"" + this.getId() + "\"";
			temp += " name=\"" + this.getId() + "\"";
		}
		return temp;
	}

	protected String buildAttribute() {
		String temp = "";
		if (StringUtil.isNotNull(this.getStyle())) {
			temp += " style=\"" + this.getStyle() + "\"";
		}
		if (StringUtil.isNotNull(this.getCss())) {
			temp += " class=\"" + this.getCss() + "\"";
		}
		return temp;
	}
}
