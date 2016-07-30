package com.gl.exchange.service;

public interface ExchangeService {
	void transferCash(String fromHolder, String toHolder, double amount);
	void transferSecurity(String fromHolder,String toHolder, int shares);
	void trade(String fromHolder, String toHolder, int shares, double price);
}
