package com.harmazing.openbridge.sys.config.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.openbridge.sys.config.dao.SysConfigMapper;
import com.harmazing.openbridge.sys.config.model.SysConfig;
import com.harmazing.openbridge.sys.config.service.ISysConfigService;

@Service
public class SysConfigService implements ISysConfigService {
	@Autowired
	private SysConfigMapper sysConfigMapper;

	@Override
	@Transactional(readOnly = true)
	public Map<String, String> getSysConfig() {
		List<SysConfig> conf = sysConfigMapper.getAllConfig();
		Map<String, String> config = new HashMap<String, String>();
		Iterator<SysConfig> sss = conf.iterator();
		while (sss.hasNext()) {
			SysConfig type = sss.next();
			config.put(type.getConfKey(), type.getConfValue());
		}
		List<Map<String, String>> sysCoreConfig = sysConfigMapper
				.getSysCoreConfig();
		if (sysCoreConfig != null && sysCoreConfig.size() > 0) {
			for (Map<String, String> configKeyValuePair : sysCoreConfig) {
				config.put(configKeyValuePair.get("key"),
						configKeyValuePair.get("value"));
			}
		}
		return config;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveSysConfig(Map<String, String> config) {
		Map<String, String> oldValue = getSysConfig();
		Iterator<Entry<String, String>> sss = config.entrySet().iterator();
		while (sss.hasNext()) {
			Entry<String, String> entry = sss.next();
			oldValue.put(entry.getKey(), entry.getValue());
		}

		Iterator<Entry<String, String>> val = oldValue.entrySet().iterator();
		while (val.hasNext()) {
			Entry<String, String> entry = val.next();
			if(!"sys.initCheck".equals(entry.getKey())){
				SysConfig conf = sysConfigMapper.selectConfig(entry.getKey());
				if (conf == null) {
					conf = new SysConfig();
					conf.setConfId(entry.getKey());
					conf.setConfKey(entry.getKey());
					conf.setConfValue(entry.getValue());
					sysConfigMapper.insertConfig(conf);
				} else {
					conf.setConfId(entry.getKey());
					conf.setConfKey(entry.getKey());
					conf.setConfValue(entry.getValue());
					sysConfigMapper.updateConfig(conf);
				}
			}
		}
		ConfigUtil.updateConfig(oldValue);
	}

}
