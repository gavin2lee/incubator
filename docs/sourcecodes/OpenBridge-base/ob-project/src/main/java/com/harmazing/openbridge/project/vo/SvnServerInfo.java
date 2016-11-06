package com.harmazing.openbridge.project.vo;

public class SvnServerInfo {
	private String svnServerIp;
	private String svnServerPort;
	private String svnAuthzPath;
	private String svnServerUser;
	private String svnServerPassword;

	private String svnAdminUsername;
	private String svnAdminPassword;

	public String getSvnServerIp() {
		return svnServerIp;
	}

	public void setSvnServerIp(String svnServerIp) {
		this.svnServerIp = svnServerIp;
	}

	public String getSvnServerPort() {
		return svnServerPort;
	}

	public void setSvnServerPort(String svnServerPort) {
		this.svnServerPort = svnServerPort;
	}

	public String getSvnAuthzPath() {
		return svnAuthzPath;
	}

	public void setSvnAuthzPath(String svnAuthzPath) {
		this.svnAuthzPath = svnAuthzPath;
	}

	public String getSvnServerUser() {
		return svnServerUser;
	}

	public void setSvnServerUser(String svnServerUser) {
		this.svnServerUser = svnServerUser;
	}

	public String getSvnServerPassword() {
		return svnServerPassword;
	}

	public void setSvnServerPassword(String svnServerPassword) {
		this.svnServerPassword = svnServerPassword;
	}

	public String getSvnAdminUsername() {
		return svnAdminUsername;
	}

	public void setSvnAdminUsername(String svnAdminUsername) {
		this.svnAdminUsername = svnAdminUsername;
	}

	public String getSvnAdminPassword() {
		return svnAdminPassword;
	}

	public void setSvnAdminPassword(String svnAdminPassword) {
		this.svnAdminPassword = svnAdminPassword;
	}

}
