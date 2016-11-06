package com.harmazing.framework.quartz.demo;

import org.springframework.stereotype.Service;

import com.harmazing.framework.quartz.IJob;
import com.harmazing.framework.quartz.IJobContext;

@Service
public class TestJob implements IJob {

	@Override
	public void runJob(IJobContext context) throws Exception {
		System.out.println("test");
	}

}
