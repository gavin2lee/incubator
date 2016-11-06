package com.harmazing.openbridge.paas.deploy.model.vo;

import java.util.ArrayList;
import java.util.List;

public class PodStatus {
	
	private String podName;
	
	private List<ContainerStatus> container = new ArrayList<ContainerStatus>();
	
	private String status;
	
	private String reason;

	public String getPodName() {
		return podName;
	}

	public void setPodName(String podName) {
		this.podName = podName;
	}

	public List<ContainerStatus> getContainer() {
		return container;
	}

	public void setContainer(List<ContainerStatus> container) {
		this.container = container;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
	

}
