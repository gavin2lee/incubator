/**
 * Project Name:ob-paasos-web
 * File Name:CreateDeployCommand.java
 * Package Name:com.harmazing.openbridge.paasos.project.deploy
 * Date:2016年4月25日上午9:44:49
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
 * ClassName:CreateDeployCommand <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月25日 上午9:44:49 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class ReplicasDeployCommand extends AbstractK8Command implements AsyncCommand{

	private static Log logger =LogFactory.getLog(ReplicasDeployCommand.class);
	
	@Override
	public void execute(AsyncTask t) {
		BuildTask buildTask =(BuildTask)t;
		
		if(StringUtils.hasText(buildTask.getSessionId())){
			OSLogFactory.bindSessionId(buildTask.getSessionId());
		}
		
		String deployId = buildTask.getTaskId();
		IPaasProjectDeployService iPaasProjectDeployService =SpringUtil.getBean(IPaasProjectDeployService.class);
		PaasProjectDeploy deploy = iPaasProjectDeployService.findById(deployId);
		try{
			iPaasProjectDeployService.endDeploy(deploy.getDeployId(), 6);
			OSLogFactory.add(deployId, OSLogFactory.KUORONG_OPERATOR, "扩容部署:进行中[调用k8s]");
			saveReplicas(deploy.getDeployCode(), deploy.getTenantId(), buildTask.getScale());
			OSLogFactory.add(deployId, OSLogFactory.KUORONG_OPERATOR, "扩容部署:进行中[扩容成功]");
			buildTask.setBuildStatus(0);
		}
		catch(Exception e){
			OSLogFactory.add(deployId, OSLogFactory.KUORONG_OPERATOR, "扩容部署:进行中[失败  "+e.getMessage()+"]");
			buildTask.setBuildStatus(-1);
			buildTask.setBuildLogs(e.getMessage());
			e.printStackTrace();
		}
		
	}

}

