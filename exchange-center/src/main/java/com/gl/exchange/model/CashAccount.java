package com.gl.exchange.model;

import java.io.Serializable;

public class CashAccount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2295702340668676896L;
	private Integer id;
	private String holder;
	private Double balance;
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
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	
}
