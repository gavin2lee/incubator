package com.gl.avs.model;

public class UserGroup extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5168271830272455864L;
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
