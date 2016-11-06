package com.harmazing.openbridge.paas.env.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;

public interface IPaasEnvService extends IBaseService {
	void createEnv(String userId, PaasEnv environment);

	PaasEnv getEnvById(String userId, String envId);

	Map<String, String> getEvnEnvironment(String userId, String evnId);

	PaasEnv getEnvByName(String businessId, String envType, String envName);

	/**
	 * 提供给API和APP使用
	 * 
	 * @param userId
	 * @param businessId
	 * @param businessType
	 * @return
	 */
	List<PaasEnv> getEnvListByBusinessId(String userId, String businessId,
			String businessType); 

	void updateEnv(String userId, String envId, String envName);
	
	public void updateEnv(String userId, String envId, String envName,String envMark);

	void deleteEnv(String envId, String userId);

	void deleteEnvResource(String userId, String recordId);

	PaasEnvResource getEnvResourceById(String userId, String recordId);

	void envApplyResource(String userId, Map<String, String> params);

	void envUpdateResource(PaasEnv env, String resId,
			Map<String, String> pageConfig);

	Map<String, List<PaasEnv>> getEnvListByBusinessId(String userId,
			List<String> businessIds, boolean hasNginxConf) throws Exception;

	PaasEnvResource getEnvResourceInfoById(String userId, String recordId);

	void updateEnvResourceAddition(PaasEnvResource resource);

	JSONObject queryEnvResourceStatusById(String userId, String recordId);

	void saveOrUpdateEnvResourceDefine(String userId, String envId, String resourceData);

	DeployResource getDeployResouceByEnvId(String userId, String envId);

	// 查询已申请资源 20160526
	JSONObject getOldResource(String userId, Map<String, Object> params);
	
	JSONObject getResourcePodEvent(String userId, String podName, String namespace);

}
