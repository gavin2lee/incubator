package com.harmazing.framework.quartz;

import java.util.List;

import com.harmazing.framework.quartz.xml.JobConfig;

public interface IJobScheduler {
	/**
	 * 删除一个定时任务
	 * 
	 * @param jobModel
	 * @throws Exception
	 */
	public abstract void removeJob(String name) throws Exception;

	/**
	 * 安排一个定时任务，若该任务已经安排，则重新安排
	 * 
	 * @param jobModel
	 * @throws Exception
	 */
	public abstract void scheduleJob(JobConfig jobConfig) throws Exception;

	/**
	 * 获取当前服务器上所有的任务列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<JobConfig> findAllScheduledJobs() throws Exception;

	public IJobLogService getJobLogService();
}
