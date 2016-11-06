package com.harmazing.openbridge.paasos.imgbuild.log;

public interface IPaaSBuildLogService {

	public void saveLog(String busId,String buildLog);
	
	public void delete(String busId);
	
	public String getLog(String busId);
}
