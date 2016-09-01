package com.lachesis.mnisqm.spring;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
@Configuration
@Profile("production")
@MapperScan("com.lachesis.mnisqm.**.dao")
@EnableTransactionManagement
public class DataSourceConfiguration {
	
    @Bean(name = "dataSource")
    public DataSource dataSource() {
        JndiDataSourceLookup ds = new JndiDataSourceLookup();
        ds.setResourceRef(true);
        return ds.getDataSource("java:comp/env/jdbc/mycollabdatasource");
    }

    @Bean
    public DataSourceTransactionManager txManager() {
        DataSourceTransactionManager bean = new DataSourceTransactionManager(dataSource());
        return bean;
    }
}
