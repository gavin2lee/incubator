package com.harmazing.openbridge.paas.nginx.service;

import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;

public interface IPaasHostService extends IBaseService {

	PaasHost getHostById(String hostId, String userId);

	void addHost(PaasHost host, String userId);

	void updateHost(PaasHost host, String userId);
	
	Page<PaasHost> queryHostPage(Map<String, Object> params, String userId);
	
}
