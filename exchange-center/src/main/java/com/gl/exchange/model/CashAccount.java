package com.gl.exchange.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CashAccount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2295702340668676896L;
	@Id
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
	@Override
	public String toString() {
		return "CashAccount [id=" + id + ", holder=" + holder + ", balance=" + balance + "]";
	}
	
	
}
