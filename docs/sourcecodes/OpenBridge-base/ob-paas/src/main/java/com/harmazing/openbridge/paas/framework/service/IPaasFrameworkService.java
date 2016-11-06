package com.harmazing.openbridge.paas.framework.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paas.framework.model.PaasFramework;

public interface IPaasFrameworkService extends IBaseService {
	List<PaasFramework> getPaasFrameworkList();

	PaasFramework getPaasFrameworkById(String id);

	List<PaasFramework> getPaasFrameworkByKey(String key);
	
	String getDockerFile(PaasFramework framework,Map<String,Object> root,String userId);
}
