package com.gl.exchange.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gl.exchange.dao.SecurityAccountDao;
import com.gl.exchange.model.CashAccount;
import com.gl.exchange.model.SecurityAccount;
import com.gl.exchange.repository.CashAccountRepository;
import com.gl.exchange.service.ExchangeService;

@Service
public class ExchangeServiceImpl implements ExchangeService {
	@Autowired
	private CashAccountRepository repo;
	
	@Autowired
	private SecurityAccountDao dao;

	public void transferCash(String fromHolder, String toHolder, double amount) {
		// TODO Auto-generated method stub

	}

	public void transferSecurity(String fromHolder, String toHolder, int shares) {
		// TODO Auto-generated method stub

	}

	public void trade(String fromHolder, String toHolder, int shares, double price) {
		// TODO Auto-generated method stub

	}

	public void addCashAccount(CashAccount ca) {
		// TODO Auto-generated method stub
		
	}

	public void addSecurityAccount(SecurityAccount sa) {
		// TODO Auto-generated method stub
		
	}

	public CashAccount findCashAccountByHolder(String holder) {
		// TODO Auto-generated method stub
		return null;
	}

	public SecurityAccount findSecurityAccountByHolder(String holder) {
		// TODO Auto-generated method stub
		return null;
	}

}
