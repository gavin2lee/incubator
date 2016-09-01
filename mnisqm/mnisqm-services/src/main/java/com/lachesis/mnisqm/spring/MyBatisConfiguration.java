package com.lachesis.mnisqm.spring;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.scripting.velocity.VelocityFacade;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
@Configuration
@Profile("production")
@Import(DataSourceConfiguration.class)
public class MyBatisConfiguration {
    @Autowired
    DataSourceConfiguration dbConfig;

    VelocityFacade a;

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dbConfig.dataSource());
        sqlSessionFactory.setTypeHandlersPackage("com.lachesis.mybatis.plugin.ext");
        sqlSessionFactory.setMapperLocations(buildBatchMapperResources(
                "classpath:sqlMap/common/*Mapper.xml",
                "classpath:sqlMap/common/*MapperExt.xml",
                "classpath:sqlMap/user/*Mapper.xml",
                "classpath:sqlMap/user/*MapperExt.xml",
                "classpath:sqlMap/form/*Mapper.xml",
                "classpath:sqlMap/form/*MapperExt.xml",
                "classpath:sqlMap/crm/*Mapper.xml",
                "classpath:sqlMap/crm/*MapperExt.xml",
                "classpath:sqlMap/project/*Mapper.xml",
                "classpath:sqlMap/project/*MapperExt.xml",
                "classpath:sqlMap/tracker/*Mapper.xml",
                "classpath:sqlMap/tracker/*MapperExt.xml",
                "classpath:sqlMap/house/*MapperExt.xml",
                "classpath:sqlMap/house/*Mapper.xml",
                "classpath:sqlMap/bloodsugar/*MapperExt.xml",
                "classpath:sqlMap/bloodsugar/*Mapper.xml",
                "classpath:sqlMap/config/*MapperExt.xml",
                "classpath:sqlMap/config/*Mapper.xml",                
                "classpath:sqlMap/support/*Mapper.xml"
        		));

        return sqlSessionFactory.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlMapClient() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    private Resource[] buildMapperResources(String resourcePath) throws IOException {
        try {
            ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
            Resource[] mappingLocations = patternResolver.getResources(resourcePath);
            return mappingLocations;
        } catch (FileNotFoundException e) {
            return new Resource[0];
        }
    }

    private Resource[] buildBatchMapperResources(String... resourcesPath) throws IOException {
        ArrayList<Resource> resources = new ArrayList<>();
        for (String resourcePath : resourcesPath) {
            CollectionUtils.addAll(resources, buildMapperResources(resourcePath));
        }
        return resources.toArray(new Resource[0]);
    }
}
