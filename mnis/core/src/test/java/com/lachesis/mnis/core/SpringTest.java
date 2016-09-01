package com.lachesis.mnis.core;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpringTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Test
	public void testApp() {
		Assert.assertNotNull(applicationContext);
		String[] names = applicationContext.getBeanDefinitionNames();
		System.out.println("=================================");
		for (String name : names) {
			System.out.println(name);
		}
		System.out.println("=================================");
	}
	
	@Test
	public void testDataSource() {
		Assert.assertNotNull(applicationContext);
		BasicDataSource dataSource = (BasicDataSource) applicationContext.getBean("dataSource");
		Assert.assertNotNull(dataSource);
		System.out.println("================ BasicDataSource Start =================");
		System.out.println(dataSource.getDriverClassName());
		System.out.println(dataSource.getUrl());
		System.out.println(dataSource.getUsername());
		System.out.println("================ BasicDataSource End =================");
	}
	
    public static Date getCurDateWithMaxTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
    
    public static Date getCurDateWithMinTime() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
}
