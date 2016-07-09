package com.gl.avs.model;

public class ArticleVoteType extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -488917275303795362L;
	private Integer code;
	private String name;
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
