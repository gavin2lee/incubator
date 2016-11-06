package com.harmazing.openbridge.sys.config.model;

import com.harmazing.framework.common.model.IBaseModel;

public class SysConfig implements IBaseModel {
	private String confId;
	private String confKey;
	private String confValue;

	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
	}

	public String getConfKey() {
		return confKey;
	}

	public void setConfKey(String confKey) {
		this.confKey = confKey;
	}

	public String getConfValue() {
		return confValue;
	}

	public void setConfValue(String confValue) {
		this.confValue = confValue;
	}
}
