/**
 * Project Name:ob-paasos-web
 * File Name:CreateDeployCommand.java
 * Package Name:com.harmazing.openbridge.paasos.project.deploy
 * Date:2016年4月25日上午9:44:49
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.deploy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.openbridge.paas.async.AsyncCommand;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.service.IPaasEnvService;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.imgbuild.log.LogProvider;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;
import com.harmazing.openbridge.paasos.imgbuild.service.IPaasImageService;
import com.harmazing.openbridge.paasos.kubectl.AbstractK8Command;
import com.harmazing.openbridge.paasos.kubectl.K8RestApiUtil;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSNginxService;
import com.harmazing.openbridge.paasos.oslog.OSLogFactory;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectDeployEnvMapper;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectDeployPortMapper;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectDeployVolumnMapper;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;

/**
 * ClassName:CreateDeployCommand <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月25日 上午9:44:49 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class CreateDeployCommand extends AbstractK8Command implements AsyncCommand,LogProvider{
	
	

	private static Log logger =LogFactory.getLog(CreateDeployCommand.class);
	public void execute(AsyncTask t) {
		BuildTask buildTask =(BuildTask)t;
		if(StringUtils.hasText(buildTask.getSessionId())){
			OSLogFactory.bindSessionId(buildTask.getSessionId());
		}
		String deployId = buildTask.getTaskId();
//		IPaasProjectBuildService iPaasProjectBuildService =SpringUtil.getBean(IPaasProjectBuildService.class);
		IPaasProjectDeployService iPaasProjectDeployService =SpringUtil.getBean(IPaasProjectDeployService.class);
		IPaasProjectService iPaasProjectService =SpringUtil.getBean(IPaasProjectService.class);
		PaasProjectDeployPortMapper paasProjectDeployPortMapper = SpringUtil.getBean(PaasProjectDeployPortMapper.class);
		IPaasImageService iPaasImageService = SpringUtil.getBean(IPaasImageService.class);
		PaasProjectDeployVolumnMapper paasProjectDeployVolumnMapper = SpringUtil.getBean(PaasProjectDeployVolumnMapper.class);
		PaasProjectDeployEnvMapper paasProjectDeployEnvMapper=SpringUtil.getBean(PaasProjectDeployEnvMapper.class);
		IPaasProjectEnvService iPaasEnvService = SpringUtil.getBean(IPaasProjectEnvService.class);
		
		
		PaasProjectDeploy deploy = iPaasProjectDeployService.findById(deployId);
		PaasProject project = iPaasProjectService.getPaasProjectInfoById(deploy.getProjectId());
//		PaasProjectBuild build = iPaasProjectBuildService.findById(deploy.getBuildId());
		PaasOsImage poi = iPaasImageService.findById(deploy.getImageId());
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		String deployTime = sdf1.format(new Date());
		iPaasProjectDeployService.beginDeploy(deployId,deployTime);
		
		Map<String,String> rclabel = new HashMap<String, String>();
		Map<String,String> podlabel = new HashMap<String, String>();
		Map<String,String> servicelabels = new HashMap<String, String>();
		Map<String,String> selector = new HashMap<String, String>();
		Map<String,String> nodeSelector = new HashMap<String, String>();
		List<PaasProjectDeployPort> ports = paasProjectDeployPortMapper.findByDeployId(deployId);
		List<PaasProjectDeployVolumn> volumns = paasProjectDeployVolumnMapper.findByDeployId(deployId);
		List<PaasProjectDeployEnv> envs = paasProjectDeployEnvMapper.findByDeployId(deployId);
		
		rclabel.put("serviceId", deployId);
		rclabel.put("projectCode", project.getProjectCode());
		rclabel.put("deployCode", deploy.getDeployCode());
		rclabel.put("deployTime", deployTime);
		
		podlabel.put("serviceId", deployId);//monitor 有用到这个作查询
		podlabel.put("projectCode", project.getProjectCode());
		podlabel.put("deployCode", deploy.getDeployCode());
		podlabel.put("deployTime", deployTime);
		
		servicelabels.put("serviceId", deployId);
		servicelabels.put("projectCode", project.getProjectCode());
		servicelabels.put("deployCode", deploy.getDeployCode());
		servicelabels.put("deployTime", deployTime);
		selector.put("serviceId", deployId);
		
		if(StringUtils.hasText(deploy.getEnvId())){
			PaasEnv paasenv = iPaasEnvService.getEnvById(deploy.getEnvId());
			if(StringUtils.hasText(paasenv.getEnvMark())){
				rclabel.put("envMark", paasenv.getEnvMark());
				podlabel.put("envMark", paasenv.getEnvMark());
				servicelabels.put("envMark", paasenv.getEnvMark());
				
				rclabel.put("envType", paasenv.getEnvType());
				podlabel.put("envType", paasenv.getEnvType());
				servicelabels.put("envType", paasenv.getEnvType());
			}
		}
		
		nodeSelector.put(K8RestApiUtil.ENVTYPE, deploy.getEnvType());
		
		String deployType = ConfigUtil.getOrElse("deployType", "runtime");
		if(StringUtils.hasText(deployType) && !"false".equals(deployType)){
			nodeSelector.put(K8RestApiUtil.TYPE, deployType);
		}
		if(nodeSelector.keySet().size()==0){
			nodeSelector=null;
		}
		try{
			
			try{
				if(envs==null){
					envs = new ArrayList<PaasProjectDeployEnv>();
				}
				
				PaasProjectDeployEnv e = new PaasProjectDeployEnv();
				e.setKey("SPRING_PROFILES_ACTIVE");
				e.setValue("paasos_"+deploy.getEnvType());
				envs.add(e);
				
				OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:进行中[获取nginx配置开始]");
				IPaasOSNginxService iPaasOSNginxService = SpringUtil.getBean(IPaasOSNginxService.class);
				List<PaasProjectDeployEnv> nginxEnvs = iPaasOSNginxService.getNginxEnvs(deploy.getEnvType());
				OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:进行中[获取nginx配置结束]");
				if(nginxEnvs!=null && nginxEnvs.size()!=0){
					envs.addAll(nginxEnvs);
				}
			}
			catch(Exception e){
				OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:进行中[获取nginx配置失败]");
				logger.debug("获取nginx信息有误", e);
			}
			
			logger.debug("-----"+deploy.getDeployCode()+" ----创建RC");
			OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:进行中[创建ReplicationController开始]");
			createReplicationController(rclabel, podlabel, selector, ports, envs, 
					volumns, deploy.getDeployCode()+"", deploy.getReplicas(), 
					poi.getImageName(), deploy.getEnvType(), nodeSelector, deploy.getTenantId(),deploy.getComputeConfig(),poi,deploy.getHealth());
			OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:进行中[创建ReplicationController结束]");
			
			
			logger.debug("-----"+deploy.getDeployCode()+" ----创建SERVICE");
			OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:进行中[创建Service开始]");
			createService(deploy.getDeployCode()+"", deploy.getTenantId(), servicelabels, podlabel, ports, deploy.getServiceIp());
			OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:进行中[创建Service结束]");
			
			buildTask.setBuildStatus(0);
		}
		catch(Exception e){
			OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:进行中[创建ReplicationController和Service失败:"+e.getMessage()+"]");
			buildTask.setBuildStatus(-1);
			buildTask.setBuildLogs(e.getMessage());
			e.printStackTrace();
			logger.error("创建失败", e);
		}
		
	}
	@Override
	public String getResult() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	

//	public void deleteReplicationController(String rcName,String deployId){
//		PaasProjectDeployMapper paasProjectDeployMapper =SpringUtil.getBean(PaasProjectDeployMapper.class);
//		IPaasProjectBuildService iPaasProjectBuildService =SpringUtil.getBean(IPaasProjectBuildService.class);
//		
//		PaasProjectDeploy paasProjectDeploy = paasProjectDeployMapper.findById(deployId);
//		getClient().replicationControllers().inNamespace(deployId).withName(rcName).delete();
//	}
//	
//	
//	
//	public void deleteService(String deployId){
//		PaasProjectDeployMapper paasProjectDeployMapper =SpringUtil.getBean(PaasProjectDeployMapper.class);
//		IPaasProjectBuildService iPaasProjectBuildService =SpringUtil.getBean(IPaasProjectBuildService.class);
//		
//		PaasProjectDeploy paasProjectDeploy = paasProjectDeployMapper.findById(deployId);
//		getClient().replicationControllers().inNamespace("thisisatest").withField("metadata.name", "testservice").delete();
//	}
}

