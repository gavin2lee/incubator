package com.gl.exchange.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gl.exchange.model.CashAccount;

public interface CashAccountRepository extends CrudRepository<CashAccount, Integer> {
	List<CashAccount> findByHolder(String holder);
}
