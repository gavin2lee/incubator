package com.lachesis.mnisqm.spring.test.service;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.stereotype.Controller;

@Configuration
@EnableSpringConfigured
@EnableAspectJAutoProxy
@ComponentScan(basePackages = {"com.lachesis.mnisqm.**.service", "com.lachesis.mnisqm.**.spring",
        "com.lachesis.mnisqm.**.mybatis", "com.lachesis.mnisqm.**.aspect", "com.lachesis.mnisqm.**.esb"},
        excludeFilters = {@ComponentScan.Filter(classes = {Controller.class})})
@Profile("test")
public class RootConfigurationTest {
}
