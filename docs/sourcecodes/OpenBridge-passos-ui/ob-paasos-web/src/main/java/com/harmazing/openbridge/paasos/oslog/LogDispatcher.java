package com.harmazing.openbridge.paasos.oslog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.openbridge.paas.async.AsyncThreadFactory;
import com.harmazing.openbridge.paasos.oslog.model.PaasProjectLog;
import com.harmazing.openbridge.paasos.oslog.service.IPaasProjectLogService;

public class LogDispatcher {
	
	private static LinkedBlockingQueue<PaasProjectLog> logs = new LinkedBlockingQueue<PaasProjectLog>();
	
	private static final long LOG_OUTPUT_INTERVAL = 5000;

	private static Log logger = org.apache.commons.logging.LogFactory.getLog(LogDispatcher.class);
	
	private final ScheduledExecutorService logScheduled = Executors.newScheduledThreadPool(2, 
			new AsyncThreadFactory("AsyncLogThreadFactory", true));

	private volatile ScheduledFuture<?> logFuture = null;
	
	private OSLogFactory factory;
	
	public LogDispatcher(OSLogFactory factory){
		this.factory = factory;
	}

	public void log(PaasProjectLog log) {
		init();
		logs.add(log);
    }

	private void init() {
		if (logFuture == null) {
			synchronized (logScheduled) {
				if (logFuture == null) {
					logFuture = logScheduled.scheduleWithFixedDelay(new DispatcherTask(), LOG_OUTPUT_INTERVAL, LOG_OUTPUT_INTERVAL, TimeUnit.MILLISECONDS);
				}
			}
		}
	}
	
	private class DispatcherTask implements Runnable{

		@Override
		public void run() {
			try{
				IPaasProjectLogService iPaasProjectLogService = SpringUtil.getBean(IPaasProjectLogService.class);
				while(true){
					List<PaasProjectLog> r = new ArrayList<PaasProjectLog>();
					for(int i=0;i<2000;i++){
						PaasProjectLog l = logs.poll();
						if(l==null){
							break;
						}
						r.add(l);
					}
					if(r==null || r.size()==0){
						break ;
					}
					iPaasProjectLogService.batchSave(r);
				}
			}
			catch(Exception e){
				logger.error(e.getMessage(), e);
			}
		}
		
	}
	
	
}
