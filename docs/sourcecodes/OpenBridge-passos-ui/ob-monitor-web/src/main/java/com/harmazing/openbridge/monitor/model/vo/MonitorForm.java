/**
 * Project Name:ob-paasos-web
 * File Name:MonitorForm.java
 * Package Name:com.harmazing.openbridge.paasos.project.model.vo
 * Date:2016年5月12日上午10:24:41
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.monitor.model.vo;

import java.util.*;

/**
 * ClassName:MonitorForm <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月12日 上午10:24:41 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class MonitorForm {
	
	private Long start;
	
	private Long end;
	
	private String projectId;
	
	private String cf;
	
	private String businessId;
	
	private String businessType;
	
	private List<Map<String,String>> endpointCounters = new ArrayList<Map<String,String>>();

	

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	public String getCf() {
		return cf;
	}

	public void setCf(String cf) {
		this.cf = cf;
	}

	public List<Map<String, String>> getEndpointCounters() {
		return endpointCounters;
	}

	public void setEndpointCounters(List<Map<String, String>> endpointCounters) {
		this.endpointCounters = endpointCounters;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	
	

}

