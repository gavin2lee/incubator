package com.harmazing.framework.quartz;

import com.harmazing.framework.quartz.log.QuartzJobLog;
 

public interface IJobLogService {
	public void add(QuartzJobLog log) throws Exception;
}
