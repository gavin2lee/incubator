/**
 * Project Name:ob-paasos-web
 * File Name:PaasProjectDeployMapper.java
 * Package Name:com.harmazing.openbridge.paasos.project.dao
 * Date:2016年4月22日下午9:46:52
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.dao;

import java.util.*;

import com.harmazing.framework.common.dao.IBaseMapper;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;

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
public interface PaasProjectDeployVolumnMapper extends IBaseMapper {

	public void deleteByDeployId(String deployId);
	
	public int batchSave(List<PaasProjectDeployVolumn> params);
	
	public List<PaasProjectDeployVolumn> findByDeployId(String deployId);
	

}

