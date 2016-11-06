package com.harmazing.openbridge.paas.framework.model;

import com.harmazing.framework.common.model.IBaseModel;

public class PaasFramework implements IBaseModel {
	private String fwId;
	private String fwLang;
	private String fwKey;
	private String fwName;
	private String imageId;
	
	private String imagePort;

	private String sysType;
	
	private String dockerFile;

	public String getSysType() {
		return sysType;
	}

	public void setSysType(String sysType) {
		this.sysType = sysType;
	}

	public String getFwId() {
		return fwId;
	}

	public void setFwId(String fwId) {
		this.fwId = fwId;
	}

	public String getFwLang() {
		return fwLang;
	}

	public void setFwLang(String fwLang) {
		this.fwLang = fwLang;
	}

	public String getFwKey() {
		return fwKey;
	}

	public void setFwKey(String fwKey) {
		this.fwKey = fwKey;
	}

	public String getFwName() {
		return fwName;
	}

	public void setFwName(String fwName) {
		this.fwName = fwName;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImagePort() {
		return imagePort;
	}

	public void setImagePort(String imagePort) {
		this.imagePort = imagePort;
	}

	public String getDockerFile() {
		return dockerFile;
	}

	public void setDockerFile(String dockerFile) {
		this.dockerFile = dockerFile;
	}

	
	
	
}
