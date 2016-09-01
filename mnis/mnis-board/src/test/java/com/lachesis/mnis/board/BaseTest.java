package com.lachesis.mnis.board;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations=
{"classpath*:applicationContext-board.xml","classpath*:applicationContext-board-data.xml"})
public class BaseTest extends AbstractTransactionalJUnit4SpringContextTests{
	

	@Override
	@Resource(name = "dataSource")
	public void setDataSource(final DataSource dataSource) {
	    this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
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
	
}
