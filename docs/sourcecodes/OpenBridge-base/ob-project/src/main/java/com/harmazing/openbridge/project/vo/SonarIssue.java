package com.harmazing.openbridge.project.vo;

import com.harmazing.framework.common.model.BaseModel;

/**
* @ClassName: AppIssue 
* @Description: issue  Model
* @author weiyujia
* @date 2016年3月7日 下午5:42:58 
*
 */
public class SonarIssue extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String severities;
	
	private String resolutions;
	
	private String projectKey;
	
	private String path;
	
	private String resolved;
	
	private String facets;
	
	private String statuses;
	
	private String categories;
	
	private String rules ;
	
	private String qprofile;
	
	private String p;
	
	private String ps;
	
	private boolean includetrends ;
	
	private String activeSeverities;
	
	private boolean activation; 
	
	private String f ;
	
	private String s;
	
	private String asc;
	
	private String selected;
	
	private String gateId;
	
	private String componentKeys;
	
	private String directories;
	
	private String uuid;
	
	private String from;
	
	private String to;
	
	private String componentUuids;
	
	private String metrics;
	
	public String getMetrics() {
		return metrics;
	}
	public void setMetrics(String metrics) {
		this.metrics = metrics;
	}
	public String getComponentUuids() {
		return componentUuids;
	}
	public void setComponentUuids(String componentUuids) {
		this.componentUuids = componentUuids;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getComponentKeys() {
		return componentKeys;
	}
	public void setComponentKeys(String componentKeys) {
		this.componentKeys = componentKeys;
	}
	
	public String getDirectories() {
		return directories;
	}
	public void setDirectories(String directories) {
		this.directories = directories;
	}
	public String getGateId() {
		return gateId;
	}
	public void setGateId(String gateId) {
		this.gateId = gateId;
	}
	public String getSelected() {
		return selected;
	}
	public void setSelected(String selected) {
		this.selected = selected;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public String getAsc() {
		return asc;
	}
	public void setAsc(String asc) {
		this.asc = asc;
	}
	public boolean isActivation() {
		return activation;
	}
	public void setActivation(boolean activation) {
		this.activation = activation;
	}
	public String getActiveSeverities() {
		return activeSeverities;
	}
	public void setActiveSeverities(String activeSeverities) {
		this.activeSeverities = activeSeverities;
	}
	public String getSeverities() {
		return severities;
	}
	public void setSeverities(String severities) {
		this.severities = severities;
	}
	public String getResolutions() {
		return resolutions;
	}
	public void setResolutions(String resolutions) {
		this.resolutions = resolutions;
	}
	public String getProjectKey() {
		return projectKey;
	}
	public void setProjectKey(String projectKey) {
		this.projectKey = projectKey;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getResolved() {
		return resolved;
	}
	public void setResolved(String resolved) {
		this.resolved = resolved;
	}
	public String getFacets() {
		return facets;
	}
	public void setFacets(String facets) {
		this.facets = facets;
	}
	public String getStatuses() {
		return statuses;
	}
	public void setStatuses(String statuses) {
		this.statuses = statuses;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	public String getQprofile() {
		return qprofile;
	}
	public void setQprofile(String qprofile) {
		this.qprofile = qprofile;
	}
	public String getPs() {
		return ps;
	}
	public void setPs(String ps) {
		this.ps = ps;
	}
	public boolean isIncludetrends() {
		return includetrends;
	}
	public void setIncludetrends(boolean includetrends) {
		this.includetrends = includetrends;
	}
	public String getF() {
		return f;
	}
	public void setF(String f) {
		this.f = f;
	}
	public String getS() {
		return s;
	}
	public void setS(String s) {
		this.s = s;
	}
	
}
