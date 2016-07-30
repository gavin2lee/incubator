package com.gl.exchange.service;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(locations = {"classpath:mybatis/repository-mybatis.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(true)
public class ExchangeServiceTest {
	@Autowired
	ExchangeService service;

	@Test
	public void testTransferCash() {
		fail("Not yet implemented");
	}

	@Test
	public void testTransferSecurity() {
		fail("Not yet implemented");
	}

	@Test
	public void testTrade() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddCashAccount() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddSecurityAccount() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindCashAccountByHolder() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindSecurityAccountByHolder() {
		fail("Not yet implemented");
	}

}
