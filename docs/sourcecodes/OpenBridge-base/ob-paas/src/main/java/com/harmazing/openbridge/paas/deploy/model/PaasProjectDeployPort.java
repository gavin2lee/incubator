/**
 * Project Name:ob-paasos-web
 * File Name:PaasProjectDeployPort.java
 * Package Name:com.harmazing.openbridge.paasos.project.model
 * Date:2016年4月25日下午4:32:03
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paas.deploy.model;

import java.io.Serializable;
import java.util.*;

/**
 * ClassName:PaasProjectDeployPort <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月25日 下午4:32:03 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class PaasProjectDeployPort implements Serializable{

	private String portId;
	
	private String deployId;
	
	private Integer nodePort; //端口
	
	private Integer targetPort;
	
	private String portKey;  //关键字
	
	private String portRemark;
	
	private String portProtocol; //协议
	
	private String hostIp;//不存入数据库 

	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	

	public Integer getNodePort() {
		return nodePort;
	}

	public void setNodePort(Integer nodePort) {
		this.nodePort = nodePort;
	}

	public Integer getTargetPort() {
		return targetPort;
	}

	public void setTargetPort(Integer targetPort) {
		this.targetPort = targetPort;
	}

	public String getPortKey() {
		return portKey;
	}

	public void setPortKey(String portKey) {
		this.portKey = portKey;
	}

	public String getPortRemark() {
		return portRemark;
	}

	public void setPortRemark(String portRemark) {
		this.portRemark = portRemark;
	}

	public String getPortProtocol() {
		return portProtocol;
	}

	public void setPortProtocol(String portProtocol) {
		this.portProtocol = portProtocol;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	
	
	
	
}

