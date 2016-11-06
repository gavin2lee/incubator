/**
 * Project Name:ob-paasos-web
 * File Name:HighChartVo.java
 * Package Name:com.harmazing.openbridge.paasos.project.model.vo
 * Date:2016年5月12日下午2:12:01
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.monitor.model.vo;

import java.util.*;

/**
 * ClassName:HighChartVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月12日 下午2:12:01 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class HighChartVo {
	
	private String name;
	
	private String endpoint;
	
	private String counter;
	
	private List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Map<String, Object>> getData() {
		return data;
	}

	public void setData(List<Map<String, Object>> data) {
		this.data = data;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getCounter() {
		return counter;
	}

	public void setCounter(String counter) {
		this.counter = counter;
	}
	
	
	

}

