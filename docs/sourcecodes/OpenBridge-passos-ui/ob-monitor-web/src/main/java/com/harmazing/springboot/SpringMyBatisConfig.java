package com.harmazing.springboot;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.framework.common.model.IBaseModel;

@EnableTransactionManagement
@Configuration
public class SpringMyBatisConfig {
	@Bean
	public DataSource dataSource(@Value("${jdbc.driver}") String jdbcDriver,
			@Value("${jdbc.url}") String jdbcUrl,
			@Value("${jdbc.username}") String jdbcUsername,
			@Value("${jdbc.password}") String jdbcPassword) {
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(jdbcDriver);
		source.setUrl(jdbcUrl);
		source.setUsername(jdbcUsername);
		source.setPassword(jdbcPassword);
		source.setTimeBetweenEvictionRunsMillis(1800000);
		source.setValidationQuery("select now()");
		source.setTestWhileIdle(true);
		return source;
	}

	@Bean
	public DataSourceTransactionManager transactionManager(DataSource dataSource) {
		DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
		transactionManager.setDataSource(dataSource);
		return transactionManager;
	}

	@Bean
	public FactoryBean<SqlSessionFactory> sqlSessionFactory(DataSource source) {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(source);
		sqlSessionFactory.setTypeAliasesPackage("com.harmazing");
		sqlSessionFactory.setConfigLocation(new ClassPathResource(
				"config/mybatis.xml"));
		sqlSessionFactory.setTypeAliasesSuperType(IBaseModel.class);
		return sqlSessionFactory;
	}

	@Bean
	public MapperScannerConfigurer mapperScannerConfigurer() {
		MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
		mapperScannerConfigurer.setBasePackage("com.harmazing");
		mapperScannerConfigurer.setMarkerInterface(IBaseMapper.class);
		return mapperScannerConfigurer;
	}

}
