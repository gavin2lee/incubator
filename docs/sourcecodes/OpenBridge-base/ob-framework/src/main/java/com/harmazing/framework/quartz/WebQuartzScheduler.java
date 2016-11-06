package com.harmazing.framework.quartz;

import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;

import com.harmazing.framework.quartz.context.AbstractQuartzScheduler;
import com.harmazing.framework.util.ResourceUtil;
import com.harmazing.framework.util.StringUtil;

public class WebQuartzScheduler extends AbstractQuartzScheduler {
	private final Log logger = LogFactory.getLog(this.getClass());
	private IJobLogService jobLogService;

	public IJobLogService getJobLogService() {
		return jobLogService;
	}

	public void setJobLogService(IJobLogService jobLogService) {
		this.jobLogService = jobLogService;
	}

	private static WebQuartzScheduler instance;

	private WebQuartzScheduler() {
	}

	public static WebQuartzScheduler getInstance() {
		if (instance == null) {
			instance = new WebQuartzScheduler();
		}
		return instance;
	}

	public void initialized(ServletContext context) {
		String configLocation = context.getInitParameter("jobConfigLocation");
		InputStream input = null;
		try {
			if (StringUtil.isNull(configLocation)) {
				configLocation = "classpath:config/jobs.xml";
			}
			Resource[] r = ResourceUtil.getResources(configLocation);
			input = r[0].getInputStream();
		} catch (Exception e) {
			logger.error("加载配置文件失败", e);
		}

		getInstance().loadXml(input);
		getInstance().start();
	}

	public void destroyed(ServletContext context) {
		getInstance().stop();
	}

}
