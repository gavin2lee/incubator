package com.harmazing.openbridge.paasos.manager.model;

import com.harmazing.openbridge.paasos.utils.KubernetesUtil;

public class PodInstances{
	private String name;
	private String deployName;
	private String deployVersion;
	private String startTime;
	private String hostIp;
	private String status;
	private String namespace;
	private String containerName;
	public PodInstances(String name,String deployName,String deployVersion,String startTime,
			String hostIp, String status, String namespace,String containerName){
		this.name = name;
		this.deployName = deployName;
		this.deployVersion = deployVersion;
		this.startTime = startTime;
		this.hostIp = hostIp;
		this.status = status;
		this.namespace = namespace;
		this.containerName = containerName;
	}
	public PodInstances(String name,String hostIp){
		this.name = name;
		this.hostIp = hostIp;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeployName() {
		return deployName;
	}
	public void setDeployName(String deployName) {
		this.deployName = deployName;
	}
	public String getDeployVersion() {
		return deployVersion;
	}
	public void setDeployVersion(String deployVersion) {
		this.deployVersion = deployVersion;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	
	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public String getWebSSHUri(){
		return KubernetesUtil.getWebSSH(namespace, name, containerName);
	}
	
}