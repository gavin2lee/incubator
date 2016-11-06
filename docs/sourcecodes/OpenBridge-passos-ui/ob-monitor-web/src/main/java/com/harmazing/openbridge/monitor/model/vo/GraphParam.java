package com.harmazing.openbridge.monitor.model.vo;


import java.util.HashMap;
import java.util.Map;

public class GraphParam {
	
	private String protocol="tcp";
	
	private String url;
	
	private String userId;
	
	private Map<String,Object> param = new HashMap<String,Object>();

	public String getProtocol() {
		return protocol;
	}

	public String getUrl() {
		return url;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	

}
