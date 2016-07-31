package com.gl.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gl.exchange.model.CashAccount;

public interface CashAccountRepositoryJpa extends JpaRepository<CashAccount, Integer> {
	CashAccount findByHolder(String holder);
}
