package com.harmazing.framework.web.listener;

import java.util.Arrays;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.context.ContextLoader;

import com.harmazing.framework.extension.Plugin;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.ResourceUtil;
import com.harmazing.framework.util.WebUtil;

public class SafeContextLoaderListener implements ServletContextListener {
	private static final Log logger = LogFactory
			.getLog(SafeContextLoaderListener.class);

	private static final ContextLoader delegate = new ContextLoader();

	private static ServletContext servletContext;

	public static ServletContext getServletContext() {
		return servletContext;
	}

	public SafeContextLoaderListener() {
		super();
	}

	public void contextInitialized(ServletContextEvent event) {
		String folder = System.getProperty("java.io.tmpdir");
		System.setProperty("tmp.dir", folder);
		this.servletContext = event.getServletContext();
		WebUtil.setServletContext(servletContext);
		try {
			// 加载数据库的配置项目
			ConfigUtil.loadPropertiesOnStartup();

			try {
				String pattern = "classpath*:META-INF/plugin/**/*.xml";
				Resource[] resources = ResourceUtil.getResources(pattern);
				Plugin.getPluginContext().init(Arrays.asList(resources));
			} catch (Exception e) {
				logger.error("插件工厂启动失败", e);
			}
			// Spring 服务
			delegate.initWebApplicationContext(event.getServletContext());
		} catch (Exception e) {
			logger.error("系统启动失败", e);
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		if (delegate != null) {
			delegate.closeWebApplicationContext(event.getServletContext());
		}
	}
}