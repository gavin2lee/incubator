package com.lachesis.mnisqm.spring;

import java.io.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.lachesis.mnisqm.core.utils.FileUtils;

/**
 * 
 * @author Paul Xu.
 * @since 1.0.0
 * 
 */
@Configuration
@Profile("!test")
public class AppServiceLoader {
	@Bean(name = "myCollabProperties")
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		Resource[] resources;

        File myCollabResourceFile = FileUtils.getDesireFile(System.getProperty("user.dir"),
                "resource/mnisqm.properties", "src/main/resource/mnisqm.properties");

		if (myCollabResourceFile != null) {
			resources = new Resource[] { new FileSystemResource(myCollabResourceFile) };
		} else {
			resources = new Resource[] { new ClassPathResource("mnisqm.properties", AppServiceLoader.class.getClassLoader()) };
		}

		pspc.setLocations(resources);
		pspc.setIgnoreUnresolvablePlaceholders(true);
		return pspc;
	}
}
