package com.gl.avs.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
//import org.springframework.test.context.ActiveProfiles;

@ImportResource(locations={"classpath:config/spring/repository.xml"})
@SpringBootApplication
@ComponentScan(basePackages={"com.gl.avs"})
//@ActiveProfiles({"dev", "integration"})
public class Application {
	/**

	1.ENV方式： ConfigurableEnvironment.setActiveProfiles("unittest")
2.JVM参数方式: -Dspring.profiles.active="unittest"
3.web.xml方式：
<init-param>
  <param-name>spring.profiles.active</param-name>
  <param-value>production</param-value>
</init-param>
4.标注方式（junit单元测试非常实用）：
@ActiveProfiles({"unittest","productprofile"})

*/

	public static void main(String[] args) {
		System.setProperty("debug", "");
		System.setProperty("spring.profiles.active", "dev");
		SpringApplication.run(Application.class, args);

	}

}
