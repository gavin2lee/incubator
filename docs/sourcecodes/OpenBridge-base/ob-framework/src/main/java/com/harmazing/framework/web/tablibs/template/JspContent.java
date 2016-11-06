package com.harmazing.framework.web.tablibs.template;

public class JspContent {
	private String name;
	private String body;
	private String status;

	public JspContent(String name, String body, String status) {
		this.name = name;
		this.body = body;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
