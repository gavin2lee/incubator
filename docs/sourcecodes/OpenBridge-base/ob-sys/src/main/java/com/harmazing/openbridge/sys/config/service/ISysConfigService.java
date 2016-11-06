package com.harmazing.openbridge.sys.config.service;

import java.util.Map;

import com.harmazing.framework.common.service.IBaseService;

public interface ISysConfigService extends IBaseService {
	Map<String, String> getSysConfig();

	void saveSysConfig(Map<String, String> config);
}
