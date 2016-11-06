package com.harmazing.openbridge.paasos.store.service;

import java.io.IOException;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;
import com.harmazing.openbridge.paasos.store.service.impl.PaasStoreAppServiceImpl.PaasStoreOptException;

public interface IPaasStoreAppService extends IBaseService {

	Page<PaasStoreApp> getPage(int pageNo, int pageSize, String keyword, IUser user);
	
	PaasStoreApp getById(String id);
	
	void delete(String id) throws PaasStoreOptException;
	
	void saveOrUpdate(PaasStoreApp build, IUser user);
	
	void updateLogoAndDesc(PaasStoreApp build) throws IOException;
	
	void build(String id, IUser user);
	
	String getBuildLog(String id);
	
	int existNameAndVersion(String name , String version, String id);

}
