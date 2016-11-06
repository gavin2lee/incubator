package com.harmazing.framework.quartz;

public interface IJob {
	public void runJob(IJobContext context) throws Exception;
}
