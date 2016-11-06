package com.harmazing.openbridge.monitor.model;

import com.harmazing.framework.common.model.BaseModel;



public class PaasHost extends BaseModel {
	protected String hostId;
	protected String hostIp;
	protected String hostUser;
	protected String envType;
	protected String hostType;
	protected Integer hostPort;

	protected String hostKeyPrv;
	protected String hostKeyPub;
	protected String hostPlatform;
	protected String backupHost;//备用服务器ip，实现高可用
	protected String virtualHost;//虚拟ip，dns解析，F5或keepalived使用
	protected String directoryName;//存储配置文件的目录名

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String getVirtualHost() {
		return virtualHost;
	}

	public void setVirtualHost(String virtualHost) {
		this.virtualHost = virtualHost;
	}

	public String getBackupHost() {
		return backupHost;
	}

	public void setBackupHost(String backupHost) {
		this.backupHost = backupHost;
	}

	public String getHostPlatform() {
		return hostPlatform;
	}

	public void setHostPlatform(String hostPlatform) {
		this.hostPlatform = hostPlatform;
	}

	public String getHostKeyPrv() {
		return hostKeyPrv;
	}

	public void setHostKeyPrv(String hostKeyPrv) {
		this.hostKeyPrv = hostKeyPrv;
	}

	public String getHostKeyPub() {
		return hostKeyPub;
	}

	public void setHostKeyPub(String hostKeyPub) {
		this.hostKeyPub = hostKeyPub;
	}

	public Integer getHostPort() {
		return hostPort;
	}

	public void setHostPort(Integer hostPort) {
		this.hostPort = hostPort;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getHostUser() {
		return hostUser;
	}

	public void setHostUser(String hostUser) {
		this.hostUser = hostUser;
	}

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

	public String getHostType() {
		return hostType;
	}

	public void setHostType(String hostType) {
		this.hostType = hostType;
	}

}
