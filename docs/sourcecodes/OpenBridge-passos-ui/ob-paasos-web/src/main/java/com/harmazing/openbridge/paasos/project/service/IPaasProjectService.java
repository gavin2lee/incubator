package com.harmazing.openbridge.paasos.project.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.model.vo.BuildVersion;
import com.harmazing.openbridge.paasos.project.model.vo.TwoTuple;

public interface IPaasProjectService extends IBaseService {

	PaasProject getPaasProjectInfoById(String projectId);

	Page<Map<String, Object>> page(Map<String, Object> params);

	String save(String userId, PaasProject project);
	
 

	Page<Map<String, Object>> listRelation(Map<String, Object> params,String type);

	/**
	 * 
	 * findBuildVersions:(获取app的所有构建版本 和 构建文件信息). <br/>
	 *
	 * @author dengxq
	 * @param businessId
	 * @param projectType
	 * @return
	 * @since JDK 1.6
	 */
	public TwoTuple<List<BuildVersion>,String> findBuildVersions(String businessId, String projectType,String userId);

	List<PaasProject> getProjectByBusId(String businessId);

	void delete(String projectId);

	void deleteForAppApi(String businessId,String type);
	
	PaasProject getPaasProjectByBusinessIdAndType(String businessId,String type);

	/**
	 * 更新项目 依赖businessId和businessType
	 * @param project
	 * @author dengxiaoqian
	 */
	void updatePaasProjectByBusinessIdAndType(PaasProject project,String userId);
}
