package com.gl.avs.model;

public class ArticleVoteType extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -488917275303795362L;
	private Byte code;
	private String name;
	public Byte getCode() {
		return code;
	}
	public void setCode(Byte code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
