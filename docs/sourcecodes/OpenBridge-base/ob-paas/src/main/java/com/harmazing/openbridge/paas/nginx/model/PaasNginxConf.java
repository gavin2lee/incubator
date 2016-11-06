package com.harmazing.openbridge.paas.nginx.model;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.model.BaseModel;
import com.harmazing.framework.util.StringUtil;

@SuppressWarnings("serial")
public class PaasNginxConf extends BaseModel {
	private String confId;
	private String hostId;
	private String confContent;
	private String serviceId;
	private String versionId;
	private String envType;
	private String nginxName;
	private String envId;
	private String businessType;
	private Boolean skipAuth;// 开放性服务创建nginx配置时不生成认证信息

	private Boolean isSupportSsl;
	private String sslCrtId;
	private String sslKeyId;
	
	private String projectCode;
	
//	private String envMark;

	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getConfContent() {
		return confContent;
	}

	public void setConfContent(String confContent) {
		this.confContent = confContent;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

	public String getNginxName() {
		return nginxName;
	}

	public void setNginxName(String nginxName) {
		this.nginxName = nginxName;
	}

	public Boolean getIsSupportSsl() {
		return isSupportSsl;
	}

	public void setIsSupportSsl(Boolean isSupportSsl) {
		this.isSupportSsl = isSupportSsl;
	}

	public Boolean getSkipAuth() {
		return skipAuth == null ? false : skipAuth;
	}

	public void setSkipAuth(Boolean skipAuth) {
		this.skipAuth = skipAuth;
	}

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getSslCrtId() {
		return sslCrtId;
	}

	public void setSslCrtId(String sslCrtId) {
		this.sslCrtId = sslCrtId;
	}

	public String getSslKeyId() {
		return sslKeyId;
	}

	public void setSslKeyId(String sslKeyId) {
		this.sslKeyId = sslKeyId;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

//	public String getEnvMark() {
//		return envMark;
//	}
//
//	public void setEnvMark(String envMark) {
//		this.envMark = envMark;
//	}
	
	
	
}
