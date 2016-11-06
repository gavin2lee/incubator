package com.harmazing.openbridge.mod.operations.elasticsearch.bean;

import java.util.HashMap;
import java.util.Map;

public class AppMonitorForm {
	
	private String appId;
	
	private String containId;
	
	private String envId;
	
	private String beginDate;
	
	private String endDate;
	
	//d h m
	private String xtype;
	
	private String strfilter;
	
	/**
	 * 0是 1否
	 */
	private String showStaticSource;
	
	private int top;
	
	private Map<String,Object> filter = new HashMap<String,Object>();

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getContainId() {
		return containId;
	}

	public void setContainId(String containId) {
		this.containId = containId;
	}

	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public int getTop() {
		return top;
	}

	public void setTop(int top) {
		this.top = top;
	}

	public Map<String, Object> getFilter() {
		return filter;
	}

	public void setFilter(Map<String, Object> filter) {
		this.filter = filter;
	}

	public String getXtype() {
		return xtype;
	}

	public void setXtype(String xtype) {
		this.xtype = xtype;
	}

	public String getEnvId() {
		return envId;
	}

	public void setEnvId(String envId) {
		this.envId = envId;
	}

	public String getStrfilter() {
		return strfilter;
	}

	public void setStrfilter(String strfilter) {
		this.strfilter = strfilter;
	}

	public String getShowStaticSource() {
		return showStaticSource;
	}

	public void setShowStaticSource(String showStaticSource) {
		this.showStaticSource = showStaticSource;
	}

	

	
	
	

}
