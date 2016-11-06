package com.harmazing.openbridge.paasos.nginx.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.quartz.ListenerHandler;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.openbridge.paas.async.AsyncThreadFactory;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paas.nginx.vo.NginxConfVo;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSNginxService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;

@Service
public class DeployChangeListenerHandler implements ListenerHandler<Void>{

	private final ScheduledExecutorService logScheduled = Executors.newScheduledThreadPool(30, new AsyncThreadFactory("DeployChangeListenerHandler", true));
	
	private Log logger = LogFactory.getLog(DeployChangeListenerHandler.class);
	
	@Override
	public Void handler(Object o) {
		Map<String,Object> param = (Map<String,Object>)o;
		String deployId = param.containsKey("id") ? (String)param.get("id") : null;
		String sessionId = (String)param.get("sessionId");
		String envId = param.containsKey("envId") ? (String)param.get("envId") : null;
		String type = (String)param.get("type");
		logger.debug(deployId+"|"+envId+"|"+type+" 同步nginx");
		if(type.equals("create")){
			synNginxAfterCreate(deployId,sessionId);
		}
		else if(type.equals("delete")){
			synNginxAfterDelete(envId,sessionId);
		}
		return null;
	}
	
	private void synNginxAfterCreate(String deployId,String sessionId){
		logScheduled.schedule(new DeployChangeTask(deployId,null,sessionId), 2000, TimeUnit.MILLISECONDS);
	}
	
	private void synNginxAfterDelete(String envId,String sessionId){
		logScheduled.schedule(new DeployChangeTask(null,envId,sessionId), 1000, TimeUnit.MILLISECONDS);
	}
	
	private class DeployChangeTask implements Runnable{
		private String deployId;
		private String envId;
		private String sessionId;
		public DeployChangeTask(String deployId,String envId,String sessionId){
			this.deployId = deployId;
			this.envId = envId;
			this.sessionId = sessionId;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			IPaasProjectDeployService iPaasProjectDeployService = SpringUtil.getBean(IPaasProjectDeployService.class);
//			IPaasProjectEnvService iPaasProjectEnvService = SpringUtil.getBean(IPaasProjectEnvService.class);
			IPaasOSNginxService iPaasOSNginxService= SpringUtil.getBean(IPaasOSNginxService.class);
			
			String nenvId = null;
			if(StringUtils.hasText(envId)){
				nenvId = envId;
			}
			else{
				PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
				if(pd==null){
					return ;
				}
				nenvId = pd.getEnvId();
			}
			try{
				List<NginxConfVo> nginxs = iPaasOSNginxService.getNginxListByEnvId(nenvId);
				if(nginxs ==null || nginxs.size()==0){
					return ;
				}
				
				//不检查了 简单处理   只要同步  在重新获取一次IP  重新设置到nginx里面去
				List<PaasProjectDeployPort> rs = iPaasProjectDeployService.findDeployPortByEnvIdForNginx(nenvId);
//				int m=1;
				JSONArray service = new JSONArray();
				if(rs!=null){
					for (PaasProjectDeployPort r : rs) {
						JSONObject ss = new JSONObject();
						ss.put("ip", r.getHostIp());
						ss.put("port", r.getNodePort());
						ss.put("weight", 1);
						service.add(ss);
					}
				}
				for(NginxConfVo nginx : nginxs){
					String synDeployIP = nginx.getSynDeployIP();
					if(StringUtils.isEmpty(synDeployIP) || "false".equals(synDeployIP)){
						continue ;
					}
					StringBuilder sb = new StringBuilder();
					PaasNginxConf f = iPaasOSNginxService.findConfByConfId(nginx.getConfId());
					JSONObject j = null;
					if(StringUtils.hasText(f.getConfContent())){
						j = JSONObject.parseObject(f.getConfContent());
					}
					else{
						j = new JSONObject();
					}
					j.put("server", service);
					f.setConfContent(j.toJSONString());
					iPaasOSNginxService.updateNginxConf(f, sb);
				}
			}
			catch(Exception e){
				logger.error(e.getMessage(),e);
			}
			
		}
		
	}
}
