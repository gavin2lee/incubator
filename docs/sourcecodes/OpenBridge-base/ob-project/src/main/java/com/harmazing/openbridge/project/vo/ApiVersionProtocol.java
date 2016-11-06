package com.harmazing.openbridge.project.vo;

import com.harmazing.framework.common.model.BaseModel;

/**
* @ClassName: ApiVersionProtocol 
* @Description: API服务接口VO
* @author weiyujia
* @date 2016年7月19日 上午11:22:32 
*
 */
public class ApiVersionProtocol extends BaseModel {

	private static final long serialVersionUID = 1L;
	
	private String protocolType;
	
	private String protocolApi;
	
	private String protocolName;
	
	private String projectName;
	
	private String projectPackage;
	
	private String sprotocol;
	
	private String servicePublic;
	
	public String getServicePublic() {
		return servicePublic;
	}

	public void setServicePublic(String servicePublic) {
		this.servicePublic = servicePublic;
	}

	public String getSprotocol() {
		return sprotocol;
	}

	public void setSprotocol(String sprotocol) {
		this.sprotocol = sprotocol;
	}

	public String getProjectPackage() {
		return projectPackage;
	}

	public void setProjectPackage(String projectPackage) {
		this.projectPackage = projectPackage;
	}

	public String getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(String protocolType) {
		this.protocolType = protocolType;
	}

	public String getProtocolApi() {
		return protocolApi;
	}

	public void setProtocolApi(String protocolApi) {
		this.protocolApi = protocolApi;
	}

	public String getProtocolName() {
		return protocolName;
	}

	public void setProtocolName(String protocolName) {
		this.protocolName = protocolName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	
}
