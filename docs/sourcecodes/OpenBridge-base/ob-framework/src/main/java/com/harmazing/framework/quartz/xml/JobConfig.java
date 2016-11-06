package com.harmazing.framework.quartz.xml;

import com.harmazing.framework.quartz.IJobConfig;

public class JobConfig implements IJobConfig {
	private String jobName;
	private String jobService;
	private String cronExpression;
	private String jobType;
	private String parameter;
	private String onStartupRun;

	public String getOnStartupRun() {
		return onStartupRun;
	}

	public void setOnStartupRun(String onStartupRun) {
		this.onStartupRun = onStartupRun;
	}

	public String getParameter() {
		return parameter;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobService() {
		return jobService;
	}

	public void setJobService(String jobService) {
		this.jobService = jobService;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
}
