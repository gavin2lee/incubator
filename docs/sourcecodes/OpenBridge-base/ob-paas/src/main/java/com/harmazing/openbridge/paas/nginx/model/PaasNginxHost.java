package com.harmazing.openbridge.paas.nginx.model;



@SuppressWarnings("serial")
public class PaasNginxHost extends PaasHost {
	public PaasNginxHost() {

	}

	public PaasNginxHost(PaasHost host) {
		this.setEnvType(host.getEnvType());
		this.setHostId(host.getHostId());
		this.setHostIp(host.getHostIp());
		this.setHostType(host.getHostType());
		this.setHostUser(host.getHostUser());
		this.setHostPort(host.getHostPort());
		this.setHostKeyPrv(host.getHostKeyPrv());
		this.setHostKeyPub(host.getHostKeyPub());
		this.setHostPlatform(host.getHostPlatform());
		this.setBackupHost(host.getBackupHost());
		this.setVirtualHost(host.getVirtualHost());
		this.setDirectoryName(host.getDirectoryName());
	}
}
