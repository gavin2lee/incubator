package com.harmazing.openbridge.paasos.project.service;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;

public interface IPaasProjectDeployService {

	String save(IUser user, PaasProjectDeploy deploy);

	Page<Map<String, Object>> page(Map<String, Object> params);
	
	PaasProjectDeploy findById(String deployId);

	void beginDeploy(String deployId,String deployTime); 
	
	public void endDeploy(String deployId,Integer status);
	
	public void endDeploy(String deployId, Integer status,String result);
	
	public void endDeploy(String deployId, Integer status,List<PaasProjectDeployVolumn> volumn,String result);

	void deleteDeploy(String deployId);
	
	public List<PaasProjectDeployPort> findDeployPortByDeployId(String deployId);

	void updateReplicas(PaasProjectDeploy pd);

	List<PaasProjectDeployVolumn> findDeployVolumnByDeployId(String deployId);

	void endDeploy(String deployId, Integer status, List<PaasProjectDeployVolumn> volumn);

	/**
	 * 根据实体 是查找部署  
	 * @param param
	 * @return
	 */
	List<PaasProjectDeploy> findDeployByEntity(PaasProjectDeploy param);

	List<PaasProjectDeployEnv> findDeployEnvByDeployId(String deployId);
	
	/**
	 * 查看部署的环境变量是否更新
	 * 
	 * true 有更新   false 没有
	 * @param deployId
	 * @return
	 */
	boolean hasDifference(String deployId,IUser user);

	/**
	 * 更新部署最新环境变量
	 * @param deployId
	 * @param user
	 */
	void changeLatestEnvVariables(String deployId, IUser user);
	
	Map<String, Object> getTopInstance(Map<String, Object> params);
	
	/**
	 * 保存的时候 deployId为空 ,后面有值
	 * 当部署的时候 deployId有只 其他没值
	 * @param deployId
	 * @param replicas
	 * @param cpu
	 * @param memory
	 * @param tentId
	 * @return
	 */
	public boolean hasMoreResource(String envType,String tenantId,boolean isError,String deployId);
	
	public ResourceQuota getAlreadyUsed(Map<String, Object> params);
	
	Page<Map<String, Object>> getAllDeploy(Map<String, Object> params);

	List<PaasProjectDeployPort> findDeployPortByEnvIdForNginx(String envId);

}
