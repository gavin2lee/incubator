/**
 * Project Name:ob-paasos-web
 * File Name:CreateDeployCallBack.java
 * Package Name:com.harmazing.openbridge.paasos.project.deploy
 * Date:2016年4月26日上午9:38:37
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.deploy;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.event.SimpleApplicationEventMulticaster;

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.framework.web.SystemEvent;
import com.harmazing.openbridge.paas.async.AsyncCallback;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.kubectl.AbstractK8Command;
import com.harmazing.openbridge.paasos.nginx.event.DeployChangeListenerHandler;
import com.harmazing.openbridge.paasos.oslog.OSLogFactory;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;

/**
 * ClassName:CreateDeployCallBack <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年4月26日 上午9:38:37 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class CreateDeployCallBack extends AbstractK8Command implements AsyncCallback{
	
	private static Log logger =LogFactory.getLog(CreateDeployCallBack.class);

	@Override
	public void execute(AsyncTask t) {
		BuildTask task =(BuildTask)t;
		logger.debug(task.getTaskId()+"  "+ task.getBuildStatus());
		String deployId = task.getTaskId();
		
		try{
			IPaasProjectDeployService iPaasProjectDeployService = SpringUtil.getBean(IPaasProjectDeployService.class);
			PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
			if(task==null || task.getBuildStatus()==null || task.getBuildStatus().intValue()!=0){
				iPaasProjectDeployService.endDeploy(deployId, 0);
				try{
					//默认100耗秒之后在去删除资源
					TimeUnit.MICROSECONDS.sleep(100);
					OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:结束[部署失败：删除创建的RC和service]",true);
					deleteReplicationController(pd.getDeployCode(), pd.getTenantId());
					deleteService(pd.getDeployCode(), pd.getTenantId());
				}
				catch(Exception e){
					//删除失败不用管
					OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:结束[部署失败：删除创建的RC和service失败可以忽略"+e.getMessage()+"]",true);
				}
			}
			else{
				iPaasProjectDeployService.endDeploy(deployId, 10);
				OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:结束[部署成功]",true);
				
			}
		}
		catch(Exception e){
			logger.error(e);
		}
		finally{
			
			Map<String,Object> type = new HashMap<String,Object>();
			type.put("id", deployId);
			type.put("sessionId", OSLogFactory.getSessionValue());
			type.put("type", "create");
			WebUtil.getApplicationContext().publishEvent(new SystemEvent(
					type , SystemEvent.BEAN_HANDLER,DeployChangeListenerHandler.class));
			
			OSLogFactory.unBindSessionId();
		}
	}

}

