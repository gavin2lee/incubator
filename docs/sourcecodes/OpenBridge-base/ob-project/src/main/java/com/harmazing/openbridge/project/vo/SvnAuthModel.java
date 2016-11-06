package com.harmazing.openbridge.project.vo;

import java.util.List;

public class SvnAuthModel {
	private String project;
	private List<String> readOnly;
	private List<String> readWrite;

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public List<String> getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(List<String> readOnly) {
		this.readOnly = readOnly;
	}

	public List<String> getReadWrite() {
		return readWrite;
	}

	public void setReadWrite(List<String> readWrite) {
		this.readWrite = readWrite;
	}

}
