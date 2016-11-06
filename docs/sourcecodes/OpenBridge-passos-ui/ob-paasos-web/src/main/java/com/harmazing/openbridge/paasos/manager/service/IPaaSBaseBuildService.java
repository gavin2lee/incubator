package com.harmazing.openbridge.paasos.manager.service;

import java.util.List;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paasos.manager.model.PaaSBaseBuild;

public interface IPaaSBaseBuildService extends IBaseService{

	Page<PaaSBaseBuild> getPage(int pageNo, int pageSize);
	
	PaaSBaseBuild getById(String id);
	
	void delete(String id);
	
	void saveOrUpdate(PaaSBaseBuild build);
	
	void updateLogoAndDesc(String id, String iconPath, String description,String tenentIds,String dockerfile);
	
	List<PaaSBaseBuild> getTenantBuild(String tenantId);
	
	void build(String id, IUser user);
	
	String getBuildLog(String id);
	
	List<PaaSBaseBuild> getByName(String name);
	
	int getPublicImageCount();

	int existNameAndVersion(String name, String version, String id);
}
