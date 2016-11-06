package com.harmazing.openbridge.paas.deploy.model.vo;

import java.util.ArrayList;
import java.util.List;

public class DeployStatus {
	
	private String deployId;
	private String projectId;
	private List<PodStatus> pods = new ArrayList<PodStatus>();

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}
	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public List<PodStatus> getPods() {
		return pods;
	}

	public void setPods(List<PodStatus> pods) {
		this.pods = pods;
	}
	
	

}
