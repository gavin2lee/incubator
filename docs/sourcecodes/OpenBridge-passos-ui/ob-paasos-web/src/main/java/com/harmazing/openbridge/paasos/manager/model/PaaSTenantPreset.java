package com.harmazing.openbridge.paasos.manager.model;

import com.harmazing.framework.common.model.BaseModel;

/**
* @ClassName: PaaSTenantPreset 
* @Description: 组织与预置应用关系VO
* @author weiyujia
* @date 2016年7月12日 下午6:26:39 
*/
public class PaaSTenantPreset extends BaseModel {

	//
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String tenantId;
	
	private String presetId;
	
	private String appName;
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getPresetId() {
		return presetId;
	}

	public void setPresetId(String presetId) {
		this.presetId = presetId;
	}
	
	
	
}
