/**
 * Project Name:ob-paasos-web
 * File Name:VolumnChangeDeployCommand.java
 * Package Name:com.harmazing.openbridge.paasos.project.deploy
 * Date:2016年5月9日上午10:34:05
 * Copyright (c) 2016
 *
*/

package com.harmazing.openbridge.paasos.project.deploy;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.async.AsyncCommand;
import com.harmazing.openbridge.paas.async.AsyncException;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.kubectl.AbstractK8Command;
import com.harmazing.openbridge.paasos.oslog.OSLogFactory;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;

/**
 * ClassName:VolumnChangeDeployCommand <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月9日 上午10:34:05 <br/>
 * @author   dengxq
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class VolumnChangeDeployCommand extends AbstractK8Command implements AsyncCommand {

	private static Log logger =LogFactory.getLog(VolumnChangeDeployCommand.class);
	
	private PaasProjectDeploy pb;
	
	public VolumnChangeDeployCommand(PaasProjectDeploy pb) {
		this.pb = pb;
	}

	@Override
	public void execute(AsyncTask t) throws AsyncException {
		BuildTask task =(BuildTask)t;
		
		if(StringUtils.hasText(task.getSessionId())){
			OSLogFactory.bindSessionId(task.getSessionId());
		}
		
		if(pb.getVolumn()==null){
			pb.setVolumn(new ArrayList<PaasProjectDeployVolumn>());
		}
		if(pb.getVolumn()!=null && pb.getVolumn().size()>0){
			for(PaasProjectDeployVolumn v : pb.getVolumn()){
				v.setId(StringUtil.getUUID());
				v.setDeployId(pb.getDeployId());
			}
		}
		IPaasProjectDeployService iPaasProjectDeployService = SpringUtil.getBean(IPaasProjectDeployService.class);
		iPaasProjectDeployService.endDeploy(pb.getDeployId(), 6);
		logger.debug("开始更新rc");
		updateRC(null,null, null,null, null, pb.getVolumn(),pb.getDeployCode(), null, null, null,
				null, pb.getTenantId());
		logger.debug("结束更新rc");
		task.setBuildStatus(0);
	}

}

