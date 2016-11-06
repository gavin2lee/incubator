package com.harmazing.springboot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.harmazing.framework.authorization.IHttpUserManager;
import com.harmazing.framework.authorization.IUserService;
import com.harmazing.framework.authorization.filter.FilterChainProxy;
import com.harmazing.framework.authorization.filter.HttpIntegrationFilter;
import com.harmazing.framework.authorization.filter.SecurityCheckFilter;
import com.harmazing.framework.authorization.impl.HttpUserSessionManager;
import com.harmazing.framework.extension.Plugin;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.ResourceUtil;

@Configuration
public class SpringWebMvcConfig extends WebMvcConfigurerAdapter {
	private static Log logger = LogFactory.getLog(SpringWebMvcConfig.class);

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/");
		viewResolver.setSuffix(".jsp");
		viewResolver.setViewClass(JstlView.class);
		return viewResolver;
	}

	@Bean
	public HandlerExceptionResolver exceptionResolver() {
		SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
		exceptionResolver.setDefaultErrorView("/common/jsp/error");
		exceptionResolver.setOrder(0);
		return exceptionResolver;
	}

	@Bean
	public IHttpUserManager httpUserManager(IUserService userService) {
		HttpUserSessionManager userManager = new HttpUserSessionManager();
		userManager.setUserService(userService);
		return userManager;
	}

	@Bean
	public Filter securityFilterChain(IHttpUserManager userManager) {
		FilterChainProxy fileter = new FilterChainProxy();
		List<Filter> filters = new ArrayList<Filter>();
		HttpIntegrationFilter http = new HttpIntegrationFilter();
		http.setHttpUserManager(userManager);
		filters.add(http);
		SecurityCheckFilter security = new SecurityCheckFilter();
		security.setAnonymousPaths("/portal/**;/docs/**;/auth/**;/metainfo/**;/common/**;/assets/**;" +
				"/login.jsp*;/index.jsp*;/logout.jsp*;/login*;/mlogin*;/html/**;/team/users*;/api/action/**");
		filters.add(security);
		fileter.setFilters(filters);
		return fileter;
	}

	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {
			public void contextInitialized(ServletContextEvent sce) {
				try {
					ConfigurableApplicationContext context = (ConfigurableApplicationContext) WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
					// 加载数据库的配置项目
					ConfigUtil.loadPropertiesOnStartup();
					try {
						String pattern = "classpath*:META-INF/plugin/**/*.xml";
						Resource[] resources = ResourceUtil
								.getResources(pattern);
						Plugin.getPluginContext()
								.init(Arrays.asList(resources));
					} catch (Exception e) {
						logger.error("插件工厂启动失败", e);
					}

				} catch (Exception e) {
					logger.error("系统启动失败", e);
				}
			}

			public void contextDestroyed(ServletContextEvent sce) {
				logger.info("ServletContext destroyed");
			}
		};
	}
}
