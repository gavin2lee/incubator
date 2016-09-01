package com.lachesis.mnisqm.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * 
 * @author Paul Xu.
 * @since 1.0.0
 *
 */
@Configuration
public class ValidatorConfiguration {

	@Bean()
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
/*		bean.setMappingLocations(new ClassPathResource("validator/crm-constraints.xml"),
				new ClassPathResource("validator/user-constraints.xml"),
				new ClassPathResource("validator/project-constraints.xml"),
				new ClassPathResource("validator/tracker-constraints.xml"));*/
		return bean;
	}
}
