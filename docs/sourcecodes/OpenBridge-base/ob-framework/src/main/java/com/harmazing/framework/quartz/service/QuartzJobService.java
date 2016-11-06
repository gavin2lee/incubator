package com.harmazing.framework.quartz.service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.harmazing.framework.quartz.IJob;
import com.harmazing.framework.quartz.IJobConfig;
import com.harmazing.framework.quartz.IJobLogService;
import com.harmazing.framework.quartz.IJobScheduler;
import com.harmazing.framework.quartz.IJobService;
import com.harmazing.framework.quartz.context.JobContext;
import com.harmazing.framework.quartz.log.QuartzJobLog;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.StringUtil;

class QuartzJobService implements IJobService {
	private final static Log logger = LogFactory.getLog(QuartzJobService.class);

	private IJobScheduler jobScheduler;
	private IJobLogService jobLogService;

	public QuartzJobService(IJobScheduler jobScheduler) {
		this.jobScheduler = jobScheduler;
		this.jobLogService = jobScheduler.getJobLogService();
	}

	private String[] getAllLocalHostIP() {
		List<String> res = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> netInterfaces = NetworkInterface
					.getNetworkInterfaces();
			InetAddress ip = null;
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) netInterfaces
						.nextElement();
				Enumeration<InetAddress> nii = ni.getInetAddresses();
				while (nii.hasMoreElements()) {
					ip = nii.nextElement();
					if (ip.getHostAddress().indexOf(":") == -1) {
						res.add(ip.getHostAddress());
					}
				}
			}
		} catch (SocketException e) {
			logger.error("获取计算机IP地址时出错", e);
		}
		return (String[]) res.toArray(new String[0]);
	}

	public boolean ipMatching(String config) {
		String[] ips = getAllLocalHostIP();
		if (ips != null) {
			for (int j = 0; j < ips.length; j++) {
				if (("IP:" + ips[j]).equals(config)) {
					return true;
				}
			}
		}
		return false;
	}

	public void runJob(IJobConfig config) throws Exception {
		// 判断是否需要执行
		if (isNeedRun(config)) {
			execute(config);
		}
	}

	private boolean isNeedRun(IJobConfig config) {
		if (StringUtil.isNull(config.getJobType())) {
			return true;
		} else if (config.getJobType().equals("MASTER")) {
			// 主节点判断的功能暂时没实现
			return true;
		} else if (config.getJobType().startsWith("IP:")) {
			if (ipMatching(config.getJobType())) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	public void execute(IJobConfig config) throws Exception {
		JobContext jobContext = new JobContext(config, jobScheduler);
		QuartzJobLog logModel = new QuartzJobLog();
		logModel.setLogId(StringUtil.getUUID());
		logModel.setJobStartTime(new Date());
		logModel.setJobName(config.getJobName());
		logModel.setSuccess(true);
		logModel.setJobClass(SpringUtil.getBean(config.getJobService())
				.getClass().getName());
		String[] ips = getAllLocalHostIP();
		String serverIp = "";
		for (int i = 0; i < ips.length; i++) {
			serverIp = serverIp + ";" + ips[i];
		}
		if (serverIp.length() > 0) {
			serverIp = serverIp.substring(1);
		}
		logModel.setServerIp(serverIp);
		try {
			invokeBeanMethod(jobContext, config);
		} catch (Exception e) {
			logModel.setSuccess(false);
			logModel.setErrorInfo(ExceptionUtil.getExceptionString(e));
			throw e;
		} finally {
			logModel.setJobEndTime(new Date());
			String costTime = String
					.valueOf((logModel.getJobEndTime().getTime()
							- logModel.getJobStartTime().getTime() + 0.0) / 1000);
			jobContext.logMessage("任务总历时：" + costTime + "秒");
			try {
				if (jobLogService != null)
					jobLogService.add(logModel);
			} catch (Exception e) {
				logger.error("保存日志时发生错误", e);
			}
		}
	}

	/**
	 * 查找bean的方法并执行
	 * 
	 * @param jobContext
	 * @param jobConfig
	 * @throws Exception
	 */
	private void invokeBeanMethod(JobContext jobContext, IJobConfig jobConfig)
			throws Exception {
		if (!SpringUtil.containsBean(jobConfig.getJobService())) {
			jobContext
					.logError("无法找到 Spring bean：" + jobConfig.getJobService());
		}
		Object bean = SpringUtil.getBean(jobConfig.getJobService());
		if (bean instanceof IJob) {
			IJob job = (IJob) bean;
			job.runJob(jobContext);
		} else {
			jobContext.logError("service 必须实现 IJob 接口");
		}
	}

}
