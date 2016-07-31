package com.gl.exchange.model;

import java.io.Serializable;

public class SecurityAccount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7090615066327880483L;
	private Integer id;
	private String holder;
	private Integer shares;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getHolder() {
		return holder;
	}
	public void setHolder(String holder) {
		this.holder = holder;
	}
	public Integer getShares() {
		return shares;
	}
	public void setShares(Integer shares) {
		this.shares = shares;
	}
	@Override
	public String toString() {
		return "SecurityAccount [id=" + id + ", holder=" + holder + ", shares=" + shares + "]";
	}
	
	
}
