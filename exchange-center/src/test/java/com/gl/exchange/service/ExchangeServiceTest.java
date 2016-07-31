package com.gl.exchange.service;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gl.exchange.model.CashAccount;
import com.gl.exchange.model.SecurityAccount;

@ContextConfiguration(locations = { "classpath:application-context.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(false)
public class ExchangeServiceTest {
	@Autowired
	ExchangeService service;

	@Test
	public void testTransferCash() {
		String holder1 = "ca"+System.nanoTime();
		String holder2 = "cb"+System.nanoTime();
		
		double balance1 = 10000.0;
		double balance2 = 20000.0;
		
		CashAccount ca1 = new CashAccount();
		ca1.setHolder(holder1);
		ca1.setBalance(balance1);
		
		CashAccount ca2 = new CashAccount();
		ca2.setHolder(holder2);
		ca2.setBalance(balance2);
		
		service.addCashAccount(ca1);
		service.addCashAccount(ca2);
		
		double transferAmount = 300.00;
		service.transferCash(holder1, holder2, transferAmount);
		
		CashAccount storedCa1 = service.findCashAccountByHolder(holder1);
		CashAccount storedCa2 = service.findCashAccountByHolder(holder2);
		
		MatcherAssert.assertThat(storedCa1, CoreMatchers.notNullValue());
		MatcherAssert.assertThat(storedCa2, CoreMatchers.notNullValue());
		
		MatcherAssert.assertThat(storedCa1.getBalance(), Matchers.lessThan(ca1.getBalance()));
		MatcherAssert.assertThat(storedCa2.getBalance(), Matchers.greaterThan(ca2.getBalance()));
	}

	@Test
	public void testTransferSecurity() {
		String holder1 = "ca"+System.nanoTime();
		String holder2 = "cb"+System.nanoTime();
		
		int shares1 = 10000;
		int shares2 = 20000;
		
		SecurityAccount sa1 = new SecurityAccount();
		sa1.setHolder(holder1);
		sa1.setShares(shares1);
		
		SecurityAccount sa2 = new SecurityAccount();
		sa2.setHolder(holder2);
		sa2.setShares(shares2);
		
		service.addSecurityAccount(sa1);
		service.addSecurityAccount(sa2);
		
		int transferAmount = 300;
		service.transferSecurity(holder1, holder2, transferAmount);
		
		SecurityAccount storedSa1 = service.findSecurityAccountByHolder(holder1);
		SecurityAccount storedSa2 = service.findSecurityAccountByHolder(holder2);
		
		MatcherAssert.assertThat(storedSa1, CoreMatchers.notNullValue());
		MatcherAssert.assertThat(storedSa2, CoreMatchers.notNullValue());
		
		MatcherAssert.assertThat(storedSa1.getShares(), Matchers.lessThan(sa1.getShares()));
		MatcherAssert.assertThat(storedSa2.getShares(), Matchers.greaterThan(sa2.getShares()));
	}

	@Test
	public void testTrade() {
		String holder1 = "a-client-"+System.nanoTime();
		String holder2 = "b-client-"+System.nanoTime();
		
		int shares1 = 10000;
		int shares2 = 20000;
		
		double balance1 = 10000.0;
		double balance2 = 20000.0;
		
		SecurityAccount sa1 = new SecurityAccount();
		sa1.setHolder(holder1);
		sa1.setShares(shares1);
		
		SecurityAccount sa2 = new SecurityAccount();
		sa2.setHolder(holder2);
		sa2.setShares(shares2);
		
		service.addSecurityAccount(sa1);
		service.addSecurityAccount(sa2);
		
		CashAccount ca1 = new CashAccount();
		ca1.setHolder(holder1);
		ca1.setBalance(balance1);
		
		CashAccount ca2 = new CashAccount();
		ca2.setHolder(holder2);
		ca2.setBalance(balance2);
		
		service.addCashAccount(ca1);
		service.addCashAccount(ca2);
		
		service.trade(holder1, holder2, 100, 1.2);
		
		
		CashAccount storedCa1 = service.findCashAccountByHolder(holder1);
		CashAccount storedCa2 = service.findCashAccountByHolder(holder2);
		
		MatcherAssert.assertThat(storedCa1, CoreMatchers.notNullValue());
		MatcherAssert.assertThat(storedCa2, CoreMatchers.notNullValue());
		
		MatcherAssert.assertThat(storedCa1.getBalance(), Matchers.greaterThan(ca1.getBalance()));
		MatcherAssert.assertThat(storedCa2.getBalance(), Matchers.lessThan(ca2.getBalance()));
		
		SecurityAccount storedSa1 = service.findSecurityAccountByHolder(holder1);
		SecurityAccount storedSa2 = service.findSecurityAccountByHolder(holder2);
		
		MatcherAssert.assertThat(storedSa1, CoreMatchers.notNullValue());
		MatcherAssert.assertThat(storedSa2, CoreMatchers.notNullValue());
		
		MatcherAssert.assertThat(storedSa1.getShares(), Matchers.lessThan(sa1.getShares()));
		MatcherAssert.assertThat(storedSa2.getShares(), Matchers.greaterThan(sa2.getShares()));
	}

	@Test
	public void testAddCashAccount() {
		CashAccount ca = new CashAccount();
		ca.setBalance(1000.00);
		ca.setHolder(String.valueOf(System.nanoTime()));

		service.addCashAccount(ca);
	}

	@Test
	public void testAddSecurityAccount() {
		SecurityAccount sa = new SecurityAccount();
		sa.setHolder(String.valueOf(System.nanoTime()));
		sa.setShares(500);

		service.addSecurityAccount(sa);
	}

	@Test
	public void testFindCashAccountByHolder() {
		String holder = String.valueOf(System.nanoTime());

		CashAccount ca = new CashAccount();
		ca.setHolder(holder);
		ca.setBalance(2000.00);

		service.addCashAccount(ca);

		CashAccount storedCa = service.findCashAccountByHolder(holder);
		MatcherAssert.assertThat(storedCa, CoreMatchers.notNullValue());
		MatcherAssert.assertThat(storedCa.getHolder(), CoreMatchers.equalTo(holder));

	}

	@Test
	public void testFindSecurityAccountByHolder() {
		String holder = String.valueOf(System.nanoTime());

		SecurityAccount sa = new SecurityAccount();
		sa.setHolder(holder);
		sa.setShares(500);

		service.addSecurityAccount(sa);

		SecurityAccount storedSa = service.findSecurityAccountByHolder(holder);
		MatcherAssert.assertThat(storedSa, CoreMatchers.notNullValue());
		MatcherAssert.assertThat(storedSa.getHolder(), CoreMatchers.equalTo(holder));
	}

}
