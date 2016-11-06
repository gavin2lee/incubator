package com.harmazing.openbridge.paas.deploy.model;

public class PaasProjectProbesHandler {
	
	private String port;
	
	private String host;
	
	private String path;
	
	private String scheme;
	
	private String type;

	public String getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public String getPath() {
		return path;
	}

	public String getScheme() {
		return scheme;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
