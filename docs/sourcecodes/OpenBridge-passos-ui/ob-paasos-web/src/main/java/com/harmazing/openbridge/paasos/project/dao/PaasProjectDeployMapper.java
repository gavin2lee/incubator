/**
 * Project Name:ob-paasos-web
 * File Name:PaasProjectDeployMapper.java
 * Package Name:com.harmazing.openbridge.paasos.project.dao
 * Date:2016年4月22日下午9:46:52
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;

/**
 * ClassName:PaasProjectDeployMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月22日 下午9:46:52 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public interface PaasProjectDeployMapper extends IBaseMapper {

	PaasProjectDeploy findById(String deployId);

	void create(PaasProjectDeploy deploy);

	void update(PaasProjectDeploy deploy);

	int pageCount(Map<String, Object> params);
	
	int getAllDeployCount(Map<String, Object> params);
	
	Collection<? extends Map<String, Object>> page(Map<String, Object> params);
	
	Collection<? extends Map<String, Object>> getAllDeploy(Map<String, Object> params);
	String getMaxServiceIp();

	String getMaxPublicIp(String ipPrefix);

	void updateStatus(Map<String, Object> param);

	@Delete(" delete from os_project_deploy where deploy_id=#{param}" )
	void delete(String param);

	@Update(" update os_project_deploy set  replicas=#{replicas},compute_config=#{computeConfig},cpu_=#{cpu},memory_=#{memory} where deploy_id=#{deployId}")
	void updateReplicas(PaasProjectDeploy pd);

	List<PaasProjectDeploy> findDeployByEntity(PaasProjectDeploy param);
	
    List<Map<String, Object>> getTopInstance(Map<String, Object> params);
	
	public int getCountrun(Map<String, Object> params);
	public int getCountstop(Map<String, Object> params);
	
	public ResourceQuota getAlreadyUsed(Map<String, Object> params);
}

