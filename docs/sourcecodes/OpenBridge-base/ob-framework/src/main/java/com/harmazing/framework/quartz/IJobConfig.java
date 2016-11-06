package com.harmazing.framework.quartz;

public interface IJobConfig {
	public String getOnStartupRun();

	public String getParameter();

	public String getJobName();

	public String getJobService();

	public String getCronExpression();

	public String getJobType();

}
