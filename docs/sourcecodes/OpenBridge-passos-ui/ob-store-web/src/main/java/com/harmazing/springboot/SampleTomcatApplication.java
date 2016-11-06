package com.harmazing.springboot;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.harmazing.framework.util.WebUtil;
import com.harmazing.framework.web.SystemEvent;

@Configuration
@SpringBootApplication
@Import({ SpringMyBatisConfig.class, SpringWebMvcConfig.class })
@ComponentScan(value = { "com.harmazing" })
public class SampleTomcatApplication implements ServletContextInitializer {

	public static ConfigurableApplicationContext applicationContext = null;

	public static void main(String[] args) {
		applicationContext = SpringApplication.run(
				SampleTomcatApplication.class, args);

		System.out.println("系统启动成功");
	}

	public void onStartup(ServletContext servletContext)
			throws ServletException {
		// 将servletContext 保存起来方便获取，这个步骤不能少。
		WebUtil.setServletContext(servletContext);
		WebApplicationContext context = WebApplicationContextUtils
				.getWebApplicationContext(servletContext);
		// 处理临时目录
		String folder = System.getProperty("java.io.tmpdir");
		System.setProperty("tmp.dir", folder);

		// 系统key
		servletContext.setInitParameter("system.key", "store");

		// 发布系统启动事件
		SystemEvent event = new SystemEvent(context, SystemEvent.STARTUP,
				servletContext);
		context.publishEvent(event);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {

		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();

		TomcatContextCustomizer contextCustomizer = new TomcatContextCustomizer() {
			@Override
			public void customize(Context context) {
				context.addWelcomeFile("/index.html");
			}

		};
		factory.addContextCustomizers(contextCustomizer);
		TomcatConnectorCustomizer connectorCustomizer = new TomcatConnectorCustomizer() {

			@Override
			public void customize(Connector connector) {
				connector.setURIEncoding("UTF-8");
			}

		};
		factory.addConnectorCustomizers(connectorCustomizer);
		ErrorPage[] errorPages = new ErrorPage[] {
				new ErrorPage(HttpStatus.NOT_FOUND, "/common/jsp/404.jsp"),
				new ErrorPage(HttpStatus.FORBIDDEN, "/common/jsp/403.jsp"),
				new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
						"/common/jsp/500.jsp") };
		factory.addErrorPages(errorPages);
		return factory;
	}
}
