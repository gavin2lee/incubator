package com.harmazing.framework.quartz.service;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.harmazing.framework.quartz.IJobService;
import com.harmazing.framework.quartz.WebQuartzScheduler;
import com.harmazing.framework.quartz.xml.JobConfig;
 
public class QuartzJobDetail extends JobDetail {
	private JobConfig jobConfig;

	public QuartzJobDetail(JobConfig jobConfig) {
		this.jobConfig = jobConfig;
		setName(jobConfig.getJobName());
		setGroup(QuartzJobDetail.class.getName());
		setJobClass(IAldJobImp.class);
	}

	public JobConfig getJobConfig() {
		return jobConfig;
	}

	public static class IAldJobImp implements Job {

		public void execute(JobExecutionContext context)
				throws JobExecutionException {
			try {
				QuartzJobDetail jobDetail = (QuartzJobDetail) context
						.getJobDetail();
				IJobService jobService = new QuartzJobService(WebQuartzScheduler.getInstance());
				jobService.runJob(jobDetail.getJobConfig());
			} catch (Exception e) {
				throw new JobExecutionException(e);
			}
		}

	}
}
