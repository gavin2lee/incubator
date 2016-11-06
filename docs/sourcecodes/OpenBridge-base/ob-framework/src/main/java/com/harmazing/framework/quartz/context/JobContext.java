package com.harmazing.framework.quartz.context;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.harmazing.framework.quartz.IJobConfig;
import com.harmazing.framework.quartz.IJobContext;
import com.harmazing.framework.quartz.IJobScheduler;

public class JobContext implements IJobContext {
	private final static Log DEFAULT_LOGGER = LogFactory
			.getLog(JobContext.class);

	private Log logger = DEFAULT_LOGGER;

	private IJobConfig jobConfig;
	private IJobScheduler jobScheduler;

	public JobContext(IJobConfig jobConfig, IJobScheduler jobScheduler) {
		this.jobConfig = jobConfig;
		this.jobScheduler = jobScheduler;
	}

	public IJobConfig getJobConfig() {
		return jobConfig;
	}

	public IJobScheduler getJobScheduler() {
		return jobScheduler;
	}

	public String getParameter() {
		return jobConfig.getParameter();
	}

	public void logError(String errMsg) {
		logger.error(errMsg);
	}

	public void logError(Throwable e) {
		logger.error(jobConfig.getJobName() + "发生错误", e);
	}

	public void logError(String errMsg, Throwable e) {
		logger.error(errMsg, e);
	}

	public void logMessage(String msg) {
		logger.debug(msg);
	}

	public Log getLogger() {
		return logger;
	}

	public void setLogger(Log logger) {
		this.logger = logger;
	}
}
