/**
 * Project Name:ob-paasos-web
 * File Name:MonitorVo.java
 * Package Name:com.harmazing.openbridge.paasos.project.model.vo
 * Date:2016年5月12日下午2:02:48
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.model.vo;

import java.util.*;

/**
 * ClassName:MonitorVo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月12日 下午2:02:48 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class FalconVo {
	
	private String endpoint;
	
	private String counter;
	
	private int step;
	
	private List<Map<String,Object>> Values = new ArrayList<Map<String,Object>>();
	
	private Map<String,Object> value = new HashMap<String,Object>();

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

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public List<Map<String, Object>> getValues() {
		return Values;
	}

	public void setValues(List<Map<String, Object>> values) {
		Values = values;
	}

	public Map<String, Object> getValue() {
		return value;
	}

	public void setValue(Map<String, Object> value) {
		this.value = value;
	}

	
	
	
	

}

