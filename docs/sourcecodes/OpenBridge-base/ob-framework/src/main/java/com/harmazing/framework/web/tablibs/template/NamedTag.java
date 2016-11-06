package com.harmazing.framework.web.tablibs.template;

public abstract class NamedTag extends BaseTag {

	protected String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	protected void clearResource() {
		this.name = null;
		super.clearResource();
	}
}
