package com.harmazing.openbridge.paas.nginx.vo;

import java.util.ArrayList;
import java.util.List;

public class NginxConfVo {

	private String confId;

	private String serviceId;

	private String versionId;

	private String versionCode;

	private String url;

	private String nginxIp;
	
	private String projectCode;
	
	private String synDeployIP;

	private List<BalanceConfVo> confList = new ArrayList<BalanceConfVo>();
	
	private List<String> slaveDomain = new ArrayList<String>();

	private String nginxConfigName;

	private Boolean isSupportSSL;
	private String sslCrtId;
	private String sslKeyId;
	
	private Boolean domainCross;//只否允许跨域ajax请求
	private Boolean skipAuth;//是否需要认证
	private String envType;

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

	public String getConfId() {
		return confId;
	}

	public void setConfId(String confId) {
		this.confId = confId;
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

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public List<BalanceConfVo> getConfList() {
		return confList;
	}

	public void setConfList(List<BalanceConfVo> confList) {
		this.confList = confList;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNginxIp() {
		return nginxIp;
	}

	public void setNginxIp(String nginxIp) {
		this.nginxIp = nginxIp;
	}

	public String getNginxConfigName() {
		return nginxConfigName;
	}

	public void setNginxConfigName(String nginxConfigName) {
		this.nginxConfigName = nginxConfigName;
	}

	public Boolean getIsSupportSSL() {
		return isSupportSSL;
	}

	public void setIsSupportSSL(Boolean isSupportSSL) {
		this.isSupportSSL = isSupportSSL;
	}

	public Boolean getDomainCross() {
		return domainCross;
	}

	public void setDomainCross(Boolean domainCross) {
		this.domainCross = domainCross;
	}

	public Boolean getSkipAuth() {
		return skipAuth;
	}

	public void setSkipAuth(Boolean skipAuth) {
		this.skipAuth = skipAuth;
	}

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getSynDeployIP() {
		return synDeployIP;
	}

	public void setSynDeployIP(String synDeployIP) {
		this.synDeployIP = synDeployIP;
	}

	public List<String> getSlaveDomain() {
		return slaveDomain;
	}

	public void setSlaveDomain(List<String> slaveDomain) {
		this.slaveDomain = slaveDomain;
	}
	
	
	
	
}
