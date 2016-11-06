package com.harmazing.openbridge.sys.user.model;

import com.harmazing.framework.common.model.IBaseModel;

@SuppressWarnings("serial")
public class SysIdentity implements IBaseModel {
	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
