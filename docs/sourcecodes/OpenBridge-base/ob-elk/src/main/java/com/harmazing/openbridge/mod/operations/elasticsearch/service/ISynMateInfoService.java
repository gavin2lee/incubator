package com.harmazing.openbridge.mod.operations.elasticsearch.service;

import java.util.List;
import java.util.Map;

public interface ISynMateInfoService {
	
	List<Map<String, Object>> getApiUrlMatchDict();
	
	List<Map<String, Object>> getAppUrlMatchDict();

	/**
	 * 内部dubbo日志字典
	 * 
	 * @return
	 */
	List<Map<String, Object>> getDubboUrlMatchDict();

}
