package com.gl.avs.model;

public class UserGroup extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5168271830272455864L;
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
