package com.gl.exchange.service;

import com.gl.exchange.model.CashAccount;
import com.gl.exchange.model.SecurityAccount;

public interface ExchangeService {
	void transferCash(String fromHolder, String toHolder, double amount);
	void transferSecurity(String fromHolder,String toHolder, int shares);
	void trade(String fromHolder, String toHolder, int shares, double price);
	
	void addCashAccount(CashAccount ca);
	void addSecurityAccount(SecurityAccount sa);
	CashAccount findCashAccountByHolder(String holder);
	SecurityAccount findSecurityAccountByHolder(String holder);
}
