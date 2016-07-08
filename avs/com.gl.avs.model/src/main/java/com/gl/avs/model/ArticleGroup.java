package com.gl.avs.model;

public class ArticleGroup extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1222447009696075745L;
	private Byte code;
	private String name;
	private String description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
