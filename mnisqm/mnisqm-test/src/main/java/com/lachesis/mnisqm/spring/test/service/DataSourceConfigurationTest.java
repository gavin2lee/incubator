package com.lachesis.mnisqm.spring.test.service;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.lachesis.mnisqm.test.service.DataSourceFactoryBean;

@Configuration
@Profile("test")
@MapperScan("com.lachesis.mnisqm.**.dao")
public class DataSourceConfigurationTest {

	@Bean
	public DataSource dataSource() {
		return new DataSourceFactoryBean().getDataSource();
	}

	@Bean
	public DataSourceTransactionManager txManager() {
		DataSourceTransactionManager bean = new DataSourceTransactionManager();
		bean.setDataSource(dataSource());
		return bean;
	}
}
