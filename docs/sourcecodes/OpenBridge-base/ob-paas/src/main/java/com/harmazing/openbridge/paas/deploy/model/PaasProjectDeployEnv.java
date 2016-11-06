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
 * Function: 环境变量. <br/>
 * Date:     2016年4月25日 下午4:32:03 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class PaasProjectDeployEnv implements Serializable{

	private String id;
	
	private String deployId;
	
	private String key;  //关键字
	
	private String value;  //值
	
	private String resourceId; 
	
	//TODO 后面改
	private String envType; //环境类型  1 .用户资源环境变量 2.系统默认环境变量  3.部署关联环境变量
	
//	private String hidden; // 1和null 是隐藏  2是显示
	
//	private String extraKey;//额外的key  有version 表示这个属性是从版本带过来的

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getEnvType() {
		return envType;
	}

	public void setEnvType(String envType) {
		this.envType = envType;
	}

//	public String getExtraKey() {
//		return extraKey;
//	}

//	public void setExtraKey(String extraKey) {
//		this.extraKey = extraKey;
//	}

//	public String getHidden() {
//		return hidden;
//	}
//
//	public void setHidden(String hidden) {
//		this.hidden = hidden;
//	}
	
	
	
}

