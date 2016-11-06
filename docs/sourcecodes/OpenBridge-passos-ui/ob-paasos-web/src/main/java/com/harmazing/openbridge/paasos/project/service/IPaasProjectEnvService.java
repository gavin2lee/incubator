package com.harmazing.openbridge.paasos.project.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.service.IBaseService;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paasos.resource.model.PaasMysql;
import com.harmazing.openbridge.paasos.resource.model.PaasNAS;
import com.harmazing.openbridge.paasos.resource.model.PaasRabbitMQ;
import com.harmazing.openbridge.paasos.resource.model.PaasRedis;

public interface IPaasProjectEnvService extends IBaseService {

	void createEnv(PaasEnv environment);

	PaasEnv getEnvById(String envId);

	List<PaasEnv> getEnvListByBusinessId(String businessId);

	void updateEnv(PaasEnv environment);

	void deleteEnv(String envId, String userId);

	PaasEnvResource getEnvResourceById(String recordId);

	void addEnvResourceByReuse(String envId, String oldEnvResourceId, String newAddition, String resId);

	void deleteEnvResource(String userId, String tenantId, String recordId);

	void applyMysql(String envId, String resType, String resAddition,
			PaasMysql mysql);

	void applyNas(String envId, String resType, String resAddition, PaasNAS nas);

	void applyRabbitMQ(String envId, String resType, PaasRabbitMQ mq);

	void applyRedis(String envId, String resType, PaasRedis redis);

	Map<String, String> getEnvironmentByEnvId(String userId, String tenantId,
			String envId);

	JSONObject getPaasResourceStatus(String userId, String tenantId,
			String recordId);

	PaasEnvResource getEnvResourceInfoById(String recordId);
	
	void saveOrUpdateEnvResourceDefine(String envId, String data);
	
	/**
	 * 创建部署的时候 调用api app接口  获取所有需要的环境变量
	 * 好奇怪  paaos 调用 api 获取部分  然后api在调用paasos 部分
	 * @param businessId
	 * @param envId
	 * @return
	 */
	DeployResource findDeployResourceFromAppApi(String envId,String userId);
	
	/**
	 * 获取所有的环境资源  包括创建的 包括默认的
	 * @param envId
	 * @param userId
	 * @return
	 */
	DeployResource getDeployResouceAllByEnvId(String envId,String userId,String deployId);
	
	/**
	 * 获取版本所需要的环境变量
	 * @param envId
	 * @param userId
	 * @return
	 */
	DeployResource getDeployResouceAllByVersionId(String projectId,String versionId,String userId,PaasProjectDeploy deploy);
	
	/**
	 * 环境对应创建的环境变量
	 * @param envId
	 * @return
	 */
	DeployResource getDeployResouceByEnvId(String envId);

	Map<String, Object> getPaasOsInfo(String userId);
}
