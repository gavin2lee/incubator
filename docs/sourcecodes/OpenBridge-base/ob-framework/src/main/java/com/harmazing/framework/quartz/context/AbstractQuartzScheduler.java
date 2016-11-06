package com.harmazing.framework.quartz.context;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;

import com.harmazing.framework.quartz.IJobScheduler;
import com.harmazing.framework.quartz.service.QuartzJobDetail;
import com.harmazing.framework.quartz.xml.JobConfig;
import com.harmazing.framework.quartz.xml.XmlParser;
import com.harmazing.framework.util.StringUtil;

public abstract class AbstractQuartzScheduler implements IJobScheduler {
	private final static Log logger = LogFactory
			.getLog(AbstractQuartzScheduler.class);
	private List<JobConfig> jobs;
	private XmlParser xmlParser = new XmlParser();

	protected Scheduler scheduler;

	protected void loadXml(InputStream input) {
		jobs = xmlParser.loadXmlDefinitions(input);
	}

	public void start() {
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler();
			scheduler.start();
			startupRun();
		} catch (Exception e) {
			logger.error("安排定时任务时发生错误", e);
		}

	}

	public void startupRun() {
		for (int i = 0; i < jobs.size(); i++) {
			JobConfig config = jobs.get(i);
			try {
				if (StringUtil.isNotNull(config.getOnStartupRun())
						&& config.getOnStartupRun().equals("true")) {
					scheduler.triggerJob(config.getJobName(),
							QuartzJobDetail.class.getName());
				}
			} catch (Exception e) {
				logger.error("启动时触发任务：" + config.getJobName() + "，时发生错误", e);
			}
		}
	}

	public void stop() {
		try {
			scheduler.shutdown();
		} catch (Exception e) {
			logger.error("停止定时任务时发生错误", e);
		}

	}

	/**
	 * 删除定时任务
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void removeJob(String name) throws Exception {
		try {
			scheduler.deleteJob(name, QuartzJobDetail.class.getName());
			if (logger.isDebugEnabled())
				logger.debug("删除定时任务：" + name);
		} catch (Exception e) {
			logger.error("删除定时任务：" + name + " 时发生错误", e);
			throw e;
		}
	}

	@Override
	public List<JobConfig> findAllScheduledJobs() throws Exception {
		return jobs;
	}

	/**
	 * 安排定时任务
	 * 
	 * @param jobModel
	 * @throws Exception
	 */
	public void scheduleJob(JobConfig config) throws Exception {
		removeJob(config.getJobName());
		JobDetail jobDetail = new QuartzJobDetail(config);
		CronTrigger trigger = new CronTrigger(jobDetail.getName(),
				jobDetail.getGroup(), config.getCronExpression());
		scheduler.scheduleJob(jobDetail, trigger);
		if (logger.isDebugEnabled())
			logger.debug("安排定时任务：" + config.getJobName() + ",beanName="
					+ config.getJobService() + ",cron="
					+ config.getCronExpression());
	}

	public void scheduler() {
		for (int i = 0; i < jobs.size(); i++) {
			JobConfig config = jobs.get(i);
			try {
				scheduleJob(config);
			} catch (Exception e) {
				logger.error("安排定时任务：" + config.getJobName() + "，时发生错误", e);
			}
		}
	}
}
