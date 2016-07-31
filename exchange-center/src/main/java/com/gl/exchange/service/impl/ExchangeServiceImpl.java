package com.gl.exchange.service.impl;

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
		log.debug(String.format("transfer cash %s from %s to %s", amount, fromHolder, toHolder));
		// List<CashAccount> fromAccts = repo.findByHolder(fromHolder);
		// if (fromAccts == null || fromAccts.size() < 1) {
		// throw new RuntimeException();
		// }
		//
		// List<CashAccount> toAccts = repo.findByHolder(toHolder);
		// if (toAccts == null || toAccts.size() < 1) {
		// throw new RuntimeException();
		// }

		CashAccount fromCa = repo.findByHolder(fromHolder);
		CashAccount toCa = repo.findByHolder(toHolder);

		fromCa.setBalance(fromCa.getBalance() - amount);
		toCa.setBalance(toCa.getBalance() + amount);

		repo.updateOne(fromCa);
		repo.updateOne(toCa);
	}

	@Transactional
	public void transferSecurity(String fromHolder, String toHolder, int shares) {
		log.debug(String.format("transfer security %s from %s to %s", shares, fromHolder, toHolder));
		SecurityAccount fromAcct = dao.findByHolder(fromHolder);
		SecurityAccount toAcct = dao.findByHolder(toHolder);

		fromAcct.setShares(fromAcct.getShares() - shares);
		toAcct.setShares(toAcct.getShares() + shares);

		dao.updateOne(fromAcct);
		dao.updateOne(toAcct);

	}

	@Transactional
	public void trade(String fromHolder, String toHolder, int shares, double price) {
		log.debug(String.format("trade security %s by price %s from %s to %s", shares, price, fromHolder, toHolder));
		SecurityAccount fromSa = dao.findByHolder(fromHolder);
		SecurityAccount toSa = dao.findByHolder(toHolder);

		toSa.setShares(toSa.getShares() + shares);
		dao.updateOne(toSa);

		if (fromSa.getShares() >= shares) {
			fromSa.setShares(fromSa.getShares() - shares);
		} else {
			throw new RuntimeException("lack of security shares");
		}

		dao.updateOne(fromSa);

		double payment = price * shares;

		CashAccount fromCa = repo.findByHolder(fromHolder);
		CashAccount toCa = repo.findByHolder(toHolder);

		fromCa.setBalance(fromCa.getBalance() + payment);

		repo.updateOne(fromCa);

		if (toCa.getBalance() >= payment) {
			toCa.setBalance(toCa.getBalance() - payment);
		} else {
			throw new RuntimeException("lack of cash");
		}

		repo.updateOne(toCa);

	}

	@Transactional
	public void addCashAccount(CashAccount ca) {
		log.debug(String.format("add %s", ca));
		repo.insertOne(ca);

	}

	@Transactional
	public void addSecurityAccount(SecurityAccount sa) {
		log.debug(String.format("insert %s", sa));
		dao.insertOne(sa);

	}

	@Transactional
	public CashAccount findCashAccountByHolder(String holder) {
		log.debug(String.format("find %s by %s", CashAccount.class.getSimpleName(), holder));
		// List<CashAccount> accts = repo.findByHolder(holder);
		//
		// if (accts != null && accts.size() > 0) {
		// return accts.get(0);
		// }

		return repo.findByHolder(holder);
	}

	@Transactional
	public SecurityAccount findSecurityAccountByHolder(String holder) {
		log.debug(String.format("find %s by %s", SecurityAccount.class.getSimpleName(), holder));
		return dao.findByHolder(holder);
	}

}
