/**
 * Project Name:ob-paasos-web
 * File Name:VolumnChangeDeployCallBack.java
 * Package Name:com.harmazing.openbridge.paasos.project.deploy
 * Date:2016年5月9日上午10:33:32
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.deploy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.async.AsyncCallback;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.kubectl.AbstractK8Command;
import com.harmazing.openbridge.paasos.oslog.OSLogFactory;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;

/**
 * ClassName:VolumnChangeDeployCallBack <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月9日 上午10:33:32 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class VolumnChangeDeployCallBack extends AbstractK8Command implements AsyncCallback {

	private static Log logger =LogFactory.getLog(VolumnChangeDeployCallBack.class);
	
	private PaasProjectDeploy pb;
	
	public VolumnChangeDeployCallBack(PaasProjectDeploy pb) {
		this.pb = pb;
	}

	@Override
	public void execute(AsyncTask t) {
		try{
			BuildTask task =(BuildTask)t;
			if(pb.getVolumn()!=null && pb.getVolumn().size()>0){
				for(PaasProjectDeployVolumn v : pb.getVolumn()){
					if(!StringUtils.hasText(v.getId())){
						v.setId(StringUtil.getUUID());
					}
				}
			}
			IPaasProjectDeployService iPaasProjectDeployService = SpringUtil.getBean(IPaasProjectDeployService.class);
			if(task==null || task.getBuildStatus()==null || task.getBuildStatus().intValue()!=0){
				//失败把状态还原到10  
				//虽然新增卷失败 但是它不会影响部署的 状态 正常还是正常 
				iPaasProjectDeployService.endDeploy(pb.getDeployId(), 10);
			}
			else{
				//把状态还原到10
				iPaasProjectDeployService.endDeploy(pb.getDeployId(), 10 ,pb.getVolumn());
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

