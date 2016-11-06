package com.harmazing.openbridge.paasos.oslog.service;

import java.util.List;

import com.harmazing.openbridge.paasos.oslog.model.PaasProjectLog;

public interface IPaasProjectLogService {
	
	public int batchSave(List<PaasProjectLog> logs);
	
	
	public List<PaasProjectLog> getLogHistory(String key);


	public List<PaasProjectLog> getLogHistoryInfo(String logId,boolean isFirst);

}
