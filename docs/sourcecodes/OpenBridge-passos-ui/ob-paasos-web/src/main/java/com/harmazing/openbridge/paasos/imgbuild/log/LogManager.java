package com.harmazing.openbridge.paasos.imgbuild.log;

import com.harmazing.framework.common.cache.ICache;
import com.harmazing.framework.common.cache.MemoryCache;
import com.harmazing.framework.util.SpringUtil;

public class LogManager {

	private static ICache cache = new MemoryCache();
	
	public static String getLog(String key){
		return ((LogProvider)cache.get(key)).getResult();
	}
	
	public static void add(String key, LogProvider provider){
		cache.add(key, provider);
	}
	
	public static void delete(String key){
		cache.delete(key);
	}
	
	public static void end(String key){
		getBuildLogService().saveLog(key, getLog(key));
		delete(key);
	}
	
	public static LogProvider getCommand(String id){
		return (LogProvider)cache.get(id);
	}
	public static String getCommandLog(String id){
		IncrementLog commandLog = getCommandLog(id,0);
		if(commandLog!=null){
			return commandLog.getLog();
		}
		return "";
	}
	public static IncrementLog getCommandLog(String id,int startLine){
		LogProvider command  = getCommand(id);
		String log = "";
		boolean end = false;
		if(command != null){
			log = command.getResult();
		}else{
			end = true;
			IPaaSBuildLogService paaSBuildLogService = getBuildLogService();
			log = paaSBuildLogService.getLog(id);
		}
		IncrementLog incrementLog = IncrementLog.getLines(log, startLine);
		incrementLog.setEnd(end);
		return incrementLog;
	}
	protected static IPaaSBuildLogService getBuildLogService() {
		IPaaSBuildLogService paaSBuildLogService = SpringUtil.getBean(IPaaSBuildLogService.class);
		return paaSBuildLogService;
	}
}
