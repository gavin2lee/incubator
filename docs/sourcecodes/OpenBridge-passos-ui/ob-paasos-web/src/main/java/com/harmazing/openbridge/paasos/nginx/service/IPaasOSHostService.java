package com.harmazing.openbridge.paasos.nginx.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;

public interface IPaasOSHostService extends IBaseService {

	List<PaasHost> getHostByType(String type, String platform);

	PaasHost getHostById(String hostId);

	Page<PaasHost> queryHostPage(Map<String, Object> params);

	void addHost(PaasHost host);

	void updateHost(PaasHost host);
	
	int getcountHostByIp(PaasHost host);
	
	int getcountHostByName(PaasHost host);
	
	void deleteHostById(String hostId);
}
