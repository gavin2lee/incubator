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
 * Function: 挂载点. <br/>
 * Date:     2016年4月25日 下午4:32:03 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class PaasProjectDeployVolumn implements Serializable{

	private String id;
	
	private String name;
	
	private String type; //类别 emptyDir hostPath nfs iscsi
	
	private String mount; //挂在点
	
	private String capacity; //容量
	
	private String volumnId; //PAASOS 卷id
	
	private String deployId; 
	
	private String allocateContent; //针对paasos表的allocateContent字段

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMount() {
		return mount;
	}

	public void setMount(String mount) {
		this.mount = mount;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getVolumnId() {
		return volumnId;
	}

	public void setVolumnId(String volumnId) {
		this.volumnId = volumnId;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getAllocateContent() {
		return allocateContent;
	}

	public void setAllocateContent(String allocateContent) {
		this.allocateContent = allocateContent;
	}
	
	
}

