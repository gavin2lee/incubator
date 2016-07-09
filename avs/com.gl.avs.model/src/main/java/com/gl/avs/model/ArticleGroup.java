package com.gl.avs.model;

public class ArticleGroup extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1222447009696075745L;
	private Integer code;
	private String name;
	private String description;
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
