/**
 * Project Name:ob-paas
 * File Name:DeployResource.java
 * Package Name:com.harmazing.openbridge.paas.deploy.model.vo
 * Date:2016年5月20日下午4:16:09
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paas.deploy.model.vo;

import java.util.*;

import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;

/**
 * ClassName:DeployResource <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月20日 下午4:16:09 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class DeployResource {
	
	private List<PaasProjectDeployEnv> envs = new ArrayList<PaasProjectDeployEnv>();

	private List<PaasProjectDeployVolumn> volumns = new ArrayList<PaasProjectDeployVolumn>();
	
	private List<PaasProjectDeployPort> ports = new ArrayList<PaasProjectDeployPort>();

	public List<PaasProjectDeployEnv> getEnvs() {
		return envs;
	}

	public void setEnvs(List<PaasProjectDeployEnv> envs) {
		this.envs = envs;
	}

	public List<PaasProjectDeployVolumn> getVolumns() {
		return volumns;
	}

	public void setVolumns(List<PaasProjectDeployVolumn> volumns) {
		this.volumns = volumns;
	}

	public List<PaasProjectDeployPort> getPorts() {
		return ports;
	}

	public void setPorts(List<PaasProjectDeployPort> ports) {
		this.ports = ports;
	}
	
	
	

}

