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

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.framework.web.SystemEvent;
import com.harmazing.openbridge.paas.async.AsyncCallback;
import com.harmazing.openbridge.paas.async.AsyncCommand;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.kubectl.AbstractK8Command;
import com.harmazing.openbridge.paasos.nginx.event.DeployChangeListenerHandler;
import com.harmazing.openbridge.paasos.oslog.OSLogFactory;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.mysql.jdbc.TimeUtil;

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
public class DeleteDeployCallBack extends AbstractK8Command implements AsyncCallback{
	
	private static Log logger =LogFactory.getLog(DeleteDeployCallBack.class);
	
	private boolean isStop = false;
	
	private AsyncCommand command = null;
	
	public DeleteDeployCallBack(){
		this.isStop = false;
	}
	public DeleteDeployCallBack(boolean isStop ,AsyncCommand command){
		this.isStop = isStop;
		this.command = command;
	}
	public DeleteDeployCallBack(boolean isStop){
		this(isStop,null);
	}

	@Override
	public void execute(AsyncTask t) {
		BuildTask task =(BuildTask)t;
		logger.debug(task.getTaskId()+"  "+ task.getBuildStatus());
		
		String envId = null;
		try{
			
			String deployId = task.getTaskId();
			IPaasProjectDeployService iPaasProjectDeployService = SpringUtil.getBean(IPaasProjectDeployService.class);
			PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
			envId = pd.getEnvId();
			if(task==null || task.getBuildStatus()==null || task.getBuildStatus().intValue()!=0){
				iPaasProjectDeployService.endDeploy(deployId, 11);
				OSLogFactory.add(deployId, (isStop?OSLogFactory.STOP_OPERATOR : OSLogFactory.DELETE_OPERATOR), (isStop?"停止":"删除")+"部署:结束[失败]",true);
			}
			else{
				if(isStop){
					iPaasProjectDeployService.endDeploy(deployId, 1);
				}
				else{
					iPaasProjectDeployService.deleteDeploy(deployId);
				}
				OSLogFactory.add(deployId, (isStop?OSLogFactory.STOP_OPERATOR : OSLogFactory.DELETE_OPERATOR), (isStop?"停止":"删除")+"部署:结束[成功]",true);
			}
		}
		catch(Exception e){
			logger.error(e);
		}
		finally{
			
			if(!isStop){
				logger.debug(envId+"   同步nginx");
				//删除才去同步nginx
				Map<String,Object> type = new HashMap<String,Object>();
				type.put("envId", envId);
				type.put("sessionId", OSLogFactory.getSessionValue());
				type.put("type", "delete");
				WebUtil.getApplicationContext().publishEvent(new SystemEvent(
						type , SystemEvent.BEAN_HANDLER,DeployChangeListenerHandler.class));
			}
			
			
			OSLogFactory.unBindSessionId();
		}
		if(command!=null){
			//默认睡2秒 在操作
			try {
				TimeUnit.SECONDS.sleep(1);
				command.execute(null);
			} catch (InterruptedException e) {
				logger.error(e.getMessage(),e);
			}
			
		}
	}

}

