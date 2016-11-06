/**
 * Project Name:ob-paasos-web
 * File Name:DeleteDeployCommand.java
 * Package Name:com.harmazing.openbridge.paasos.project.deploy
 * Date:2016年4月26日下午6:04:46
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.deploy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.openbridge.paas.async.AsyncCommand;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.kubectl.AbstractK8Command;
import com.harmazing.openbridge.paasos.oslog.OSLogFactory;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;

/**
 * ClassName:DeleteDeployCommand <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月26日 下午6:04:46 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class DeleteDeployCommand extends AbstractK8Command implements AsyncCommand{

	private static Log logger = LogFactory.getLog(DeleteDeployCommand.class);
	
	private boolean isStop = false;
	
	public DeleteDeployCommand(){
		this.isStop = false;
	}
	public DeleteDeployCommand(boolean isStop){
		this.isStop = isStop;
	}
	
	@Override
	public void execute(AsyncTask t) {
		BuildTask buildTask =(BuildTask)t;
		
		if(StringUtils.hasText(buildTask.getSessionId())){
			OSLogFactory.bindSessionId(buildTask.getSessionId());
		}
		
		String deployId = buildTask.getTaskId();
//		IPaasProjectBuildService iPaasProjectBuildService =SpringUtil.getBean(IPaasProjectBuildService.class);
		IPaasProjectDeployService iPaasProjectDeployService =SpringUtil.getBean(IPaasProjectDeployService.class);
		
		PaasProjectDeploy deploy = iPaasProjectDeployService.findById(deployId);
		if(isStop){
			iPaasProjectDeployService.endDeploy(deploy.getDeployId(),7);
		}
		else{
			iPaasProjectDeployService.endDeploy(deploy.getDeployId(),6);
		}
		
		try{
			OSLogFactory.add(deployId, (isStop?OSLogFactory.STOP_OPERATOR : OSLogFactory.DELETE_OPERATOR), (isStop?"停止":"删除")+"部署:进行中[删除ReplicationController开始]");
			deleteReplicationController(deploy.getDeployCode() , deploy.getTenantId());
			OSLogFactory.add(deployId, (isStop?OSLogFactory.STOP_OPERATOR : OSLogFactory.DELETE_OPERATOR), (isStop?"停止":"删除")+"部署:进行中[删除ReplicationController成功]");
			
			OSLogFactory.add(deployId, (isStop?OSLogFactory.STOP_OPERATOR : OSLogFactory.DELETE_OPERATOR), (isStop?"停止":"删除")+"部署:进行中[删除Service开始]");
			deleteService(deploy.getDeployCode() , deploy.getTenantId());
			OSLogFactory.add(deployId, (isStop?OSLogFactory.STOP_OPERATOR : OSLogFactory.DELETE_OPERATOR), (isStop?"停止":"删除")+"部署:进行中[删除Service成功]");
			
			buildTask.setBuildStatus(0);
		}
		catch(Exception e){
			OSLogFactory.add(deployId, (isStop?OSLogFactory.STOP_OPERATOR : OSLogFactory.DELETE_OPERATOR), (isStop?"停止":"删除")+"部署:进行中[删除RC,SERVICE成功:"+e.getMessage()+"]");
			logger.error(buildTask.getTaskId()+"操作部署失败",e);
			buildTask.setBuildStatus(-1);
			buildTask.setBuildLogs(e.getMessage());
		}
	}

}

