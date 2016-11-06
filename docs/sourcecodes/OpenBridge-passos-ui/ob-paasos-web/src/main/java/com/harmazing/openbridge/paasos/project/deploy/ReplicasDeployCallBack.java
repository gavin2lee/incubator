/**
 * Project Name:ob-paasos-web
 * File Name:CreateDeployCallBack.java
 * Package Name:com.harmazing.openbridge.paasos.project.deploy
 * Date:2016年4月26日上午9:38:37
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.deploy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.NumberUtil;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.openbridge.paas.async.AsyncCallback;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.kubectl.AbstractK8Command;
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
public class ReplicasDeployCallBack extends AbstractK8Command implements AsyncCallback{
	
	private static Log logger =LogFactory.getLog(ReplicasDeployCallBack.class);

	@Override
	public void execute(AsyncTask t) {
		BuildTask task =(BuildTask)t;
		try{
			logger.debug(task.getTaskId()+"  "+ task.getBuildStatus());
			String deployId = task.getTaskId();
			
			IPaasProjectDeployService iPaasProjectDeployService = SpringUtil.getBean(IPaasProjectDeployService.class);
			PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
			
			iPaasProjectDeployService.endDeploy(deployId, 10);
			if(task==null || task.getBuildStatus()==null || task.getBuildStatus().intValue()!=0){
				//失败把状态还原到10
				//iPaasProjectDeployService.endDeploy(deployId, 10);
				OSLogFactory.add(deployId, OSLogFactory.KUORONG_OPERATOR, "扩容部署:结束[失败:"+task.getBuildLogs()+"]",true);
			}
			else{
				//失败把状态还原到10
				//iPaasProjectDeployService.endDeploy(deployId, 10);
				pd.setReplicas(task.getScale());
				String cf = pd.getComputeConfig();
				JSONObject jo = JSONObject.parseObject(cf);
				jo.put("replicas", ""+task.getScale());
				pd.setComputeConfig(jo.toJSONString());
				
				String cpu = jo.getString("cpu");
				pd.setCpu(Double.parseDouble(cpu));
				
				String memory = jo.getString("memory");
				memory = memory.toUpperCase();//G-->Gi M-->Mi
				Double rm = Double.parseDouble(memory.substring(0, memory.length()-1));
				if(memory.endsWith("G")){
					pd.setMemory(Double.parseDouble(NumberUtil.rounded(rm*1024,2)));
				}
				else if(memory.endsWith("M")){
					pd.setMemory(rm);
				}
				else{
					throw new RuntimeException("内存格式只支持M和G");
				}
				
				iPaasProjectDeployService.updateReplicas(pd);
				
				OSLogFactory.add(deployId, OSLogFactory.KUORONG_OPERATOR, "扩容部署:结束[成功]",true);
			}
		}
		catch(Exception e){
			logger.error(e);
		}
		finally{
			OSLogFactory.unBindSessionId();
		}
	}

}

