package com.harmazing.framework.quartz;

import org.springframework.context.ApplicationListener;

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.web.SystemEvent;

public class QuartzSpringBean implements ApplicationListener<SystemEvent> {
	private IJobLogService jobLogService;

	public IJobLogService getJobLogService() {
		return jobLogService;
	}

	public void setJobLogService(IJobLogService jobLogService) {
		this.jobLogService = jobLogService;
	}

	@Override
	public void onApplicationEvent(SystemEvent event) {
		if (event.getType().equals(SystemEvent.STARTUP)) {
			WebQuartzScheduler.getInstance().setJobLogService(
					getJobLogService());
			WebQuartzScheduler.getInstance().initialized(
					event.getServletContext());
		}
		if (event.getType().equals(SystemEvent.SHUTDOWN)) {
			WebQuartzScheduler.getInstance().destroyed(
					event.getServletContext());
		}
		if(SystemEvent.BEAN_HANDLER.equals(event.getType())){
			ListenerHandler handler =(ListenerHandler) SpringUtil.getBean(event.getClazz());
			handler.handler(event.getSource());
		}
	}
}
