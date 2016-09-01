package com.lachesis.mnisqm.test.service;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.lachesis.mnisqm.spring.test.service.RootConfigurationTest;


@ContextConfiguration(classes = {RootConfigurationTest.class})
@ActiveProfiles(profiles = "test")
public class IntergrationServiceTest {
	/*@ClassRule
	public static final EssentialInitRule essentialRule = new EssentialInitRule();

	@Rule
	public DbUnitInitializerRule dbRule = new DbUnitInitializerRule();*/
}
