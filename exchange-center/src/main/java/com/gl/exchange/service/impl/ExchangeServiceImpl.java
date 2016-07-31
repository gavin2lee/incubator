package com.gl.exchange.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gl.exchange.dao.SecurityAccountDao;
import com.gl.exchange.model.CashAccount;
import com.gl.exchange.model.SecurityAccount;
import com.gl.exchange.repository.CashAccountRepository;
import com.gl.exchange.service.ExchangeService;

@Service
public class ExchangeServiceImpl implements ExchangeService {
	private static final Logger log = LoggerFactory.getLogger(ExchangeServiceImpl.class);
	@Autowired
	private CashAccountRepository repo;
	
	@Autowired
	private SecurityAccountDao dao;

	@Transactional
	public void transferCash(String fromHolder, String toHolder, double amount) {
		// TODO Auto-generated method stub

	}

	@Transactional
	public void transferSecurity(String fromHolder, String toHolder, int shares) {
		// TODO Auto-generated method stub

	}

	@Transactional
	public void trade(String fromHolder, String toHolder, int shares, double price) {
		// TODO Auto-generated method stub

	}

	@Transactional
	public void addCashAccount(CashAccount ca) {
		log.debug(String.format("add %s", ca));
		repo.save(ca);
		
	}

	@Transactional
	public void addSecurityAccount(SecurityAccount sa) {
		log.debug(String.format("insert %s", sa));
		dao.insertOne(sa);
		
	}

	@Transactional(readOnly=true)
	public CashAccount findCashAccountByHolder(String holder) {
		log.debug(String.format("find %s by %s", CashAccount.class.getSimpleName(), holder));
		List<CashAccount> accts = repo.findByHolder(holder);
		
		if(accts != null && accts.size() > 0){
			return accts.get(0);
		}
		
		return null;
	}

	@Transactional(readOnly=true)
	public SecurityAccount findSecurityAccountByHolder(String holder) {
		log.debug(String.format("find %s by %s", SecurityAccount.class.getSimpleName(), holder));
		return dao.findByHolder(holder);
	}

}
