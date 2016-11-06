package com.harmazing.openbridge.paasos.project.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.model.PaasProjectBuild;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;

import base.AppUnitTestBase;

public class PaasProjectDeployServiceImplTest extends AppUnitTestBase{
	
	@Autowired
	private PaasProjectDeployServiceImpl iPaasProjectDeployService;
	
	
	@Test
	public void testSave() {
		
		PaasProjectDeploy deploy = new PaasProjectDeploy();
//		deploy.setDeployId() ;
//		deploy.setProjectId(String projectId) ;
//		deploy.setDeployName(String deployName) ;
//		deploy.setDescription(String description) ;
//		deploy.setTenantId(String tenantId) ;
//		deploy.setEnvType(String envType) ;
//		deploy.setCreateUser(String createUser) ;
//		deploy.setCreateTime(Date createTime) ;
//		deploy.setServiceIp(String serviceIp) ;
//		deploy.setPublicIp(String publicIp) ;
//		deploy.setReplicas(Integer replicas) ;
//		deploy.setRestartPolicy(String restartPolicy) ;
//		deploy.setModifyUser(String modifyUser) ;
//		deploy.setModifyTime(Date modifyTime) ;
//		deploy.setExtBusinessEnv(String extBusinessEnv) ;
//		deploy.setExtBusinessId(String extBusinessId) ;
//		deploy.setEnvVariable(List<PaasProjectDeployEnv> envVariable) ;
//		deploy.setComputeConfig(String computeConfig) ;
//		deploy.setBuildId(String buildId) ;
//		deploy.setStatus(Integer status) ;
//		deploy.setDeleteStatus(Integer deleteStatus) ;
//		deploy.setOwnerCluster(String ownerCluster) ;
//		deploy.setLabels(String labels) ;
//		deploy.setDeployCode(String deployCode) ;
//		deploy.setPorts(List<PaasProjectDeployPort> ports) ;
//		deploy.setPublicPort(String publicPort) ;
//		deploy.setVolumn(List<PaasProjectDeployVolumn> volumn) ;
//		deploy.setImageId(String imageId) ;
//		deploy.setResourceConfig(String resourceConfig) ;
//		deploy.setBusinessId(String businessId) ;
//		deploy.setProjectType(String projectType) ;
//		deploy.setEnvId(String envId) ;
//		deploy.setExtraKey(String extraKey) ;
//		deploy.setExtraData(String extraData) ;
		
		iPaasProjectDeployService.save(iUser, deploy);
	}
	
	

}
