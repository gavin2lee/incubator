package com.harmazing.openbridge.paasos.oslog.model;

import java.util.Date;

public class PaasProjectLog {
	
	private String id;
	
	private String neid;
	
	private String key;
	
	private String type;
	
	private String message;
	
	private Long createDate;
	
	private String createUser;
	
	private String begin; //开始 1为开始  null为中间日志
	
	
	private Long gteCreateDate;
	
	private Long ltCreateDate;
	
	private String sessionId;//根据sessionId来判断是否属于一个会话
	
	private String sort = "desc";
	
	private Integer limit;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getBegin() {
		return begin;
	}

	public void setBegin(String begin) {
		this.begin = begin;
	}

	

	public Long getGteCreateDate() {
		return gteCreateDate;
	}

	public void setGteCreateDate(Long gteCreateDate) {
		this.gteCreateDate = gteCreateDate;
	}

	public Long getLtCreateDate() {
		return ltCreateDate;
	}

	public void setLtCreateDate(Long ltCreateDate) {
		this.ltCreateDate = ltCreateDate;
	}

	public String getNeid() {
		return neid;
	}

	public void setNeid(String neid) {
		this.neid = neid;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	

}
