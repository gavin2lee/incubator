package com.gl.avs.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ActiveProfiles;

@ImportResource(locations={"classpath:config/spring/repository.xml"})
@SpringBootApplication
@ComponentScan(basePackages={"com.gl.avs"})
@ActiveProfiles({"dev", "integration"})
public class Application {

	public static void main(String[] args) {
		System.setProperty("debug", "");
		SpringApplication.run(Application.class, args);

	}

}
