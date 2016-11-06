package com.harmazing.framework.quartz;


public interface IJobService {
	public void runJob(IJobConfig config) throws Exception;
}
 