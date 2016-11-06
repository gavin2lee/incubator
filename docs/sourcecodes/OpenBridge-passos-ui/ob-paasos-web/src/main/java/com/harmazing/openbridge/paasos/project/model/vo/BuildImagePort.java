/**
 * Project Name:ob-paasos-web
 * File Name:BuildImagePort.java
 * Package Name:com.harmazing.openbridge.paasos.project.model.vo
 * Date:2016年4月28日上午10:41:24
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.model.vo;

import java.io.Serializable;
import java.util.*;

/**
 * ClassName:BuildImagePort <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月28日 上午10:41:24 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class BuildImagePort implements Serializable{
	
	private String protocol;
	
	private String port;
	
	private String key;
	
	private String remark;

	

	

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}

