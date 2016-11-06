package com.harmazing.openbridge.project.vo;

public class SvnInfo extends ScmInfo {
	private String baseUrl;
	private String username;
	private String password;

	public SvnInfo() {
		super("svn");
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
