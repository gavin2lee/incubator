package com.harmazing.openbridge.paasos.manager.service;

import java.util.List;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paasos.manager.model.PaaSNode;

public interface IPaaSNodeService extends IBaseService{

	List<PaaSNode> list();
	
	Page<PaaSNode> getPage(int pageNo, int pageSize);
	
	PaaSNode getById(String id);
	
	PaaSNode getByName(String name);
	
	void delete(String id);
	
	void saveOrUpdate(PaaSNode node);
	
	List<PaaSNode> getTenantNodes(String tenantId);

}
