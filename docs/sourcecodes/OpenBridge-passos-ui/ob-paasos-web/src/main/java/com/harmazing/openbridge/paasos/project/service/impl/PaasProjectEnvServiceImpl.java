package com.harmazing.openbridge.paasos.project.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paasos.nginx.dao.PaasNginxMapper;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSNginxService;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectEnvMapper;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;
import com.harmazing.openbridge.paasos.resource.model.PaasMysql;
import com.harmazing.openbridge.paasos.resource.model.PaasNAS;
import com.harmazing.openbridge.paasos.resource.model.PaasRabbitMQ;
import com.harmazing.openbridge.paasos.resource.model.PaasRedis;
import com.harmazing.openbridge.paasos.resource.service.IPaasMQService;
import com.harmazing.openbridge.paasos.resource.service.IPaasMysqlService;
import com.harmazing.openbridge.paasos.resource.service.IPaasNASService;
import com.harmazing.openbridge.paasos.resource.service.IPaasRedisService;

@Service
public class PaasProjectEnvServiceImpl implements IPaasProjectEnvService {
	@Autowired
	private PaasProjectEnvMapper envMapper;
	@Autowired
	private IPaasMysqlService mysqlService;
	@Autowired
	private IPaasRedisService redisService;
	@Autowired
	private IPaasMQService mqService;
	@Autowired
	private IPaasNASService nasService;
	@Autowired
	private IPaasProjectService iPaasProjectService;
	@Autowired
	private IPaasProjectDeployService iPaasProjectDeployService;
	@Autowired
	private PaasNginxMapper paasNginxMapper;
	@Autowired
	private IPaasOSNginxService iPaasOSNginxService;

	private static Log logger = LogFactory.getLog(PaasProjectEnvServiceImpl.class);
	
	@Override
	public void createEnv(PaasEnv environment) {
		PaasEnv oldEnv = envMapper.getEnvByName(environment.getProjectId(),
				environment.getEnvType(), environment.getEnvName());
		if (oldEnv != null) {
			throw new RuntimeException("此名称已存在");
		}
		if(StringUtils.isEmpty(environment.getEnvMark())){
			environment.setEnvMark(null);
		}
		else{
			Map<String,Object> param =new HashMap<String,Object>();
			param.put("projectId", environment.getProjectId());
			param.put("envType", environment.getEnvType());
//			param.put("businessType", environment.getBusinessType());
			param.put("envMark", environment.getEnvMark());
			List<PaasEnv> result = envMapper.getenvListByMark(param);
			if(result != null && result.size()>0){
				throw new RuntimeException("该项目已经存在该标签的环境");
			}
		}
		envMapper.createEnv(environment);
	}

	@Override
	public PaasEnv getEnvById(String envId) {
		PaasEnv env = envMapper.getEnvById(envId);
		if (env != null) {
			env.setResources(envMapper.getEnvResources(env.getEnvId()));
		}
		return env;
	}

	@Override
	public List<PaasEnv> getEnvListByBusinessId(String businessId) {
		return envMapper.getEnvListByBusinessId(businessId);
	}

	@Override
	public void updateEnv(PaasEnv environment) {
		if(StringUtils.isEmpty(environment.getEnvMark())){
			environment.setEnvMark(null);
		}
		else{
			Map<String,Object> param =new HashMap<String,Object>();
			param.put("projectId", environment.getProjectId());
			param.put("envType", environment.getEnvType());
//			param.put("businessType", environment.getBusinessType());
			param.put("envMark", environment.getEnvMark());
			param.put("not_id", environment.getEnvId());
			List<PaasEnv> result = envMapper.getenvListByMark(param);
			if(result != null && result.size()>0){
				throw new RuntimeException("该项目已经存在该标签的环境");
			}
		}
		envMapper.updateEnv(environment);
	}

	@Override
	public void deleteEnv(String envId, String userId) {
		List<PaasEnvResource> resources = envMapper.getEnvResources(envId);
		if (resources != null && resources.size() > 0) {
			throw new RuntimeException("该环境还有未释放的资源");
		}
		
		//dengxiaoqian 2015-06-24 PAASOS-286 
		PaasProjectDeploy d = new PaasProjectDeploy();
		d.setEnvId(envId);
		List<PaasProjectDeploy> pds = iPaasProjectDeployService.findDeployByEntity(d);
		if(pds !=null && pds.size()>0){
			throw new RuntimeException("该环境还有部署的应用");
		}
		// 检查部署的访问代理 20160718 taoshuangxi
		List<PaasNginxConf> nginxs =paasNginxMapper.getNginxListByEnvId(envId);
		if(nginxs!=null && nginxs.size()>0){
			throw new RuntimeException("该环境还有部署的访问代理");
		}
		envMapper.deleteEnv(envId);
	}

	@Override
	public PaasEnvResource getEnvResourceById(String recordId) {
		return envMapper.getEnvResourceById(recordId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteEnvResource(String userId, String tenantId,
			String recordId) {
		PaasEnvResource resource = envMapper.getEnvResourceById(recordId);
		if (resource != null) {
			String resourceType = resource.getResourceId();
			String paasosResId = resource.getPaasosResId();
			List<PaasEnvResource> existResources = envMapper
					.getEnvResourcesByPaasOsResId(paasosResId);

			if (existResources.size() == 1) {
				//if PAASOS_RELEASE_RESOURCE_CASCADING is Y, 
				//release the resource when delete the last resouce record in os_project_env_res 
				if(ConfigUtil.getOrElse("PAASOS_RELEASE_RESOURCE_CASCADING", "N").equals("Y")){
					if ("mysql51".equals(resourceType)) {
						mysqlService.deletePaasMysqlById(resource.getPaasosResId(),
								userId, tenantId);
					} else if ("nas".equals(resourceType)) {
						nasService.deletePaasNASById(resource.getPaasosResId(),
								userId, tenantId);
					} else if ("rocket".equals(resourceType)) {
						mqService.deletePaasMQById(resource.getPaasosResId(),
								userId, tenantId);
					} else if ("redis".equals(resourceType)) {
						redisService.deletePaasRedisById(resource.getPaasosResId(),
								userId, tenantId);
					}
				}
			}
			envMapper.delEnvResourceById(recordId);
		}
	}

	@Override
	public void addEnvResourceByReuse(String envId, String oldEnvResourceId,
			String newAddition, String resId) {
		List<PaasEnvResource> oldResources = envMapper
				.getEnvResourcesByPaasOsResId(oldEnvResourceId);
		if (oldResources == null || oldResources.size()<1) {
			JSONObject applyConfig = new JSONObject();
			if("mysql51".equals(resId)){
				PaasMysql oldResource = mysqlService.getPaasMysqlInfoById(oldEnvResourceId);
				String apply=oldResource.getApplyContent();
				if(StringUtil.isNotNull(apply)){
					JSONObject temp = JSONObject.parseObject(apply);
					applyConfig.put("memory", temp.getString("memory"));
					applyConfig.put("storage", temp.getString("storage"));
				}
			}else if("nas".equals(resId)){
				PaasNAS oldResource = nasService.getPaasNASInfoById(oldEnvResourceId);
				String apply=oldResource.getApplyContent();
				if(StringUtil.isNotNull(apply)){
					JSONObject temp = JSONObject.parseObject(apply);
					applyConfig.put("storage", temp.getString("size"));
				}
			}else if("rocket".equals(resId)){
				PaasRabbitMQ oldResource = mqService.getPaasMQInfoById(oldEnvResourceId);
				String apply=oldResource.getApplyContent();
				if(StringUtil.isNotNull(apply)){
					JSONObject temp = JSONObject.parseObject(apply);
					applyConfig.put("memory", temp.getString("memory"));
				}
			}else if("redis".equals(resId)){
				PaasRedis oldResource = redisService.getPaasRedisInfoById(oldEnvResourceId);
				String apply=oldResource.getApplyContent();
				if(StringUtil.isNotNull(apply)){
					JSONObject temp = JSONObject.parseObject(apply);
					applyConfig.put("memory", temp.getString("memory"));
				}
			}else {}
			PaasEnvResource newResource = new PaasEnvResource();
			newResource.setRecordId(StringUtil.getUUID());
			newResource.setEnvId(envId);
			newResource.setApplyConfig(applyConfig.toJSONString());
			newResource.setResourceId(resId);
			newResource.setResAddition(newAddition);
			newResource.setPaasosResId(oldEnvResourceId);
			envMapper.addEnvResource(newResource);
		}else{
			PaasEnvResource oldResource = oldResources.get(0);
			PaasEnvResource newResource = new PaasEnvResource();
			newResource.setRecordId(StringUtil.getUUID());
			newResource.setEnvId(envId);
			newResource.setApplyConfig(oldResource.getApplyConfig());
			newResource.setResourceId(oldResource.getResourceId());
			newResource.setResultConfig(oldResource.getResultConfig());
			newResource.setResAddition(newAddition);
			newResource.setPaasosResId(oldResource.getPaasosResId());
			envMapper.addEnvResource(newResource);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void applyMysql(String envId, String resType, String resAddition,
			PaasMysql mysql) {
		JSONObject Apply = JSONObject.parseObject(mysql.getApplyContent());
		JSONObject resourceApply = new JSONObject();
		resourceApply.put("memory", Apply.getString("memory"));
		resourceApply.put("storage", Apply.getString("storage"));
		PaasEnvResource newResource = new PaasEnvResource();
		newResource.setRecordId(StringUtil.getUUID());
		newResource.setEnvId(envId);
		newResource.setResourceId(resType);
		newResource.setApplyConfig(resourceApply.toJSONString());
		newResource.setResAddition(resAddition);
		envMapper.addEnvResource(newResource);

		mysqlService.addPaasMysqlResource(mysql);

		newResource.setPaasosResId(mysql.getMysqlId());
		envMapper.updateEvnResource(newResource);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void applyNas(String envId, String resType, String resAddition,
			PaasNAS nas) {
		JSONObject Apply = JSONObject.parseObject(nas.getApplyContent());
		JSONObject resourceApply = new JSONObject();
		resourceApply.put("storage", Apply.getString("size"));
		PaasEnvResource newResource = new PaasEnvResource();
		newResource.setRecordId(StringUtil.getUUID());
		newResource.setEnvId(envId);
		newResource.setResourceId(resType);
		newResource.setApplyConfig(resourceApply.toJSONString());
		newResource.setResAddition(resAddition);
		envMapper.addEnvResource(newResource);

		nasService.addPaasNASResource(nas);

		newResource.setPaasosResId(nas.getNasId());
		envMapper.updateEvnResource(newResource);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void applyRabbitMQ(String envId, String resType, PaasRabbitMQ mq) {
		JSONObject Apply = JSONObject.parseObject(mq.getApplyContent());
		JSONObject resourceApply = new JSONObject();
		resourceApply.put("memory", Apply.getString("memory"));
		PaasEnvResource newResource = new PaasEnvResource();
		newResource.setRecordId(StringUtil.getUUID());
		newResource.setEnvId(envId);
		newResource.setResourceId(resType);
		newResource.setApplyConfig(resourceApply.toJSONString());
		envMapper.addEnvResource(newResource);

		mqService.addPaasMQResource(mq);

		newResource.setPaasosResId(mq.getMqId());
		envMapper.updateEvnResource(newResource);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void applyRedis(String envId, String resType, PaasRedis redis) {
		JSONObject Apply = JSONObject.parseObject(redis.getApplyContent());
		JSONObject resourceApply = new JSONObject();
		resourceApply.put("memory", Apply.getString("memory"));
		PaasEnvResource newResource = new PaasEnvResource();
		newResource.setRecordId(StringUtil.getUUID());
		newResource.setEnvId(envId);
		newResource.setResourceId(resType);
		newResource.setApplyConfig(resourceApply.toJSONString());
		envMapper.addEnvResource(newResource);

		redisService.addPaasRedisResource(redis);

		newResource.setPaasosResId(redis.getRedisId());
		envMapper.updateEvnResource(newResource);

	}

	@Override
	public Map<String, String> getEnvironmentByEnvId(String userId,
			String tenantId, String envId) {
		Map<String, String> environment = new HashMap<String, String>();
		List<PaasEnvResource> envRes = envMapper.getEnvResources(envId);
		if (envRes != null && envRes.size() > 0) {
			for (PaasEnvResource resource : envRes) {
				if ("mysql51".equals(resource.getResourceId())) {
					JSONObject mysqlConfig = mysqlService.getPaasMysqlConfig(resource.getPaasosResId());
					environment.put("PAASOS_MYSQL_USERNAME",
							mysqlConfig.getString("username"));
					environment.put("PAASOS_MYSQL_PASSWORD",
							mysqlConfig.getString("password"));
					environment.put("PAASOS_MYSQL_DATABASE",
							resource.getResAddition());
					environment.put("PAASOS_MYSQL_HOST_IP",
							mysqlConfig.getString("clusterIP"));
					environment.put("PAASOS_MYSQL_HOST_PORT",
							mysqlConfig.getString("clusterPort"));
				} else if ("nas".equals(resource.getResourceId())) {
					JSONObject allocateJson = nasService.getPaasNASConfig(resource.getPaasosResId());
					environment.put("PAASOS_NFS_PATH",
							resource.getResAddition());
				} else if ("rocket".equals(resource.getResourceId())) {
					JSONObject allocateJson = mqService.getPaasMQConfig(resource.getPaasosResId());
					environment.put("PAASOS_RABBIT_HOST_IP",
							allocateJson.getString("clusterIP"));
					environment.put("PAASOS_RABBIT_HOST_PORT",
							allocateJson.getString("clusterPort"));
				} else if ("redis".equals(resource.getResourceId())) {
					JSONObject allocateJson = redisService.getPaasRedisConfig(resource.getPaasosResId());
					environment.put("PAASOS_REDIS_USERNAME",
							allocateJson.getString("username"));
					environment.put("PAASOS_REDIS_PASSWORD",
							allocateJson.getString("password"));
					environment.put("PAASOS_REDIS_HOST_IP",
							allocateJson.getString("clusterIP"));
					environment.put("PAASOS_REDIS_HOST_PORT",
							allocateJson.getString("clusterPort"));
				} else if ("define".equals(resource.getResourceId())) {
					String resultConfig = resource.getResultConfig();
					if (StringUtil.isNotNull(resultConfig)) {
						JSONArray array = JSONArray.parseArray(resultConfig);
						for (int j = 0; j < array.size(); j++) {
							JSONObject config = array.getJSONObject(j);
							environment.put(config.getString("key"),
									config.getString("value"));
						}
					}
				}
			}
		}
		return environment;
	}

	@Override
	public JSONObject getPaasResourceStatus(String userId, String tenantId,
			String recordId) {
		PaasEnvResource resource = envMapper.getEnvResourceById(recordId);
		String resId = resource.getPaasosResId();
		String resType = resource.getResourceId();
		JSONObject obj=null;
		JSONObject retJson = new JSONObject();
		if ("mysql51".equals(resType)) {
			obj = mysqlService.queryMysqlInfo(resId, userId,
					tenantId);
		} else if ("nas".equals(resType)) {
			obj = nasService.queryNASInfo(resId, userId, tenantId);
			retJson.put("status", "运行中");
		} else if ("rocket".equals(resType)) {
			obj = mqService.queryMQInfo(resId, userId, tenantId);
		} else if ("redis".equals(resType)) {
			obj = redisService.queryRedisInfo(resId, userId,
					tenantId);
		} else {
			throw new RuntimeException("资源类型不正确");
		}
		JSONObject returnObj = new JSONObject();
		String status = obj.getString("status");
		switch(status){
		case "creating":
			status = "创建中";
			break;
		case "starting":
			status = "启动中";
			break;
		case "running":
			status = "运行中";
			break;
		case "stopped":
			status = "已停止";
			break;
		default:
			status ="";
		}
		returnObj.put("status", status);
		if(obj.containsKey("podInfo")){
			if(obj.getJSONObject("podInfo")!=null){
				returnObj.put("podName", obj.getJSONObject("podInfo").get("podName"));
				returnObj.put("namespace", obj.getJSONObject("podInfo").get("namespace"));
			}
		}
		return returnObj;
	}

	@Override
	public PaasEnvResource getEnvResourceInfoById(String recordId) {
		PaasEnvResource resource = envMapper.getEnvResourceById(recordId);
		String resourceType = resource.getResourceId();
		String paasosResId = resource.getPaasosResId();
		if("mysql51".equals(resourceType)){
			JSONObject json = mysqlService.getPaasMysqlConfig(paasosResId);
			resource.setResultConfig(json.toJSONString());
		}else if("rocket".equals(resourceType)){
			JSONObject json = mqService.getPaasMQConfig(paasosResId);
			resource.setResultConfig(json.toJSONString());
		}else if("nas".equals(resourceType)){
			JSONObject json = nasService.getPaasNASConfig(paasosResId);
			resource.setResultConfig(json.toJSONString());
		}else if("redis".equals(resourceType)){
			JSONObject json = redisService.getPaasRedisConfig(paasosResId);
			resource.setResultConfig(json.toJSONString());
		}
		return resource;
	}

	@Override
	public void saveOrUpdateEnvResourceDefine(String envId, String data) {
		PaasEnv env = getEnvById(envId);
		boolean hasDefineEnv = false;
		List<PaasEnvResource> resourceList = env.getResources();
		for (int i = 0; i < resourceList.size(); i++) {
			PaasEnvResource resource = resourceList.get(i);
			if(resource.getResourceId().equals("define")){
				hasDefineEnv = true;
				if(StringUtil.isNotNull(data) && JSONArray.parseArray(data).size()>0){
					resource.setApplyConfig(data);
					resource.setResultConfig(data);
					envMapper.updateEvnResource(resource);
				}else{
					envMapper.delEnvResource(envId, "define");
				}
				break;
			}
		}
		if(!hasDefineEnv){
			PaasEnvResource newResource = new PaasEnvResource();
			newResource.setEnvId(envId);
			newResource.setApplyConfig(data);
			newResource.setRecordId(StringUtil.getUUID());
			newResource.setResourceId("define");
			newResource.setResultConfig(data);
			envMapper.addEnvResource(newResource);
		}
	}

	@Override
	public DeployResource getDeployResouceByEnvId(String envId) {
		DeployResource deployResource = new DeployResource();
		List<PaasProjectDeployEnv> envs = new ArrayList<PaasProjectDeployEnv>();
		List<PaasProjectDeployVolumn> volumns = new ArrayList<PaasProjectDeployVolumn>();

		List<PaasEnvResource> envRes = envMapper.getEnvResources(envId);
		if (envRes != null && envRes.size() > 0) {
			for (PaasEnvResource resource : envRes) {
				if ("mysql51".equals(resource.getResourceId())) {
					JSONObject mysqlConfig = mysqlService.getPaasMysqlConfig(resource.getPaasosResId());
					PaasProjectDeployEnv tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_MYSQL_USERNAME");
					tempEnv.setValue(mysqlConfig.getString("username"));
					envs.add(tempEnv);
					tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_MYSQL_PASSWORD");
					tempEnv.setValue(mysqlConfig.getString("password"));
					envs.add(tempEnv);
					tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_MYSQL_DATABASE");
					tempEnv.setValue(resource.getResAddition());
					envs.add(tempEnv);
					tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_MYSQL_HOST_IP");
					tempEnv.setValue(mysqlConfig.getString("clusterIP"));
					envs.add(tempEnv);
					tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_MYSQL_HOST_PORT");
					tempEnv.setValue(mysqlConfig.getString("clusterPort"));
					envs.add(tempEnv);
				} else if ("nas".equals(resource.getResourceId())) {
					JSONObject allocateJson = nasService.getPaasNASConfig(resource.getPaasosResId());
					PaasProjectDeployEnv tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_NFS_PATH");
					tempEnv.setValue(resource.getResAddition());
					envs.add(tempEnv);
					PaasProjectDeployVolumn volume = new PaasProjectDeployVolumn();
					String capacity = allocateJson.getString("volume");
					if (StringUtil.isNull(capacity)) {
						JSONObject applyConfig = JSONObject
								.parseObject(resource.getApplyConfig());
						capacity = applyConfig.getString("storage");
					}
					volume.setMount(resource.getResAddition());
					volume.setType("nfs");
					volume.setCapacity(capacity);
					volume.setVolumnId(allocateJson.getString("id"));
					volume.setAllocateContent(allocateJson.getString("nfsServer")
							+ ":" + allocateJson.getString("nfsPath"));
					volumns.add(volume);
				} else if ("rocket".equals(resource.getResourceId())) {
					JSONObject allocateJson = mqService.getPaasMQConfig(resource.getPaasosResId());
					PaasProjectDeployEnv tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_RABBIT_HOST_IP");
					tempEnv.setValue(allocateJson.getString("clusterIP"));
					envs.add(tempEnv);
					tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_RABBIT_HOST_PORT");
					tempEnv.setValue(allocateJson.getString("clusterPort"));
					envs.add(tempEnv);
				} else if ("redis".equals(resource.getResourceId())) {
					JSONObject allocateJson = redisService.getPaasRedisConfig(resource.getPaasosResId());
					PaasProjectDeployEnv tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_REDIS_HOST_IP");
					tempEnv.setValue(allocateJson.getString("clusterIP"));
					envs.add(tempEnv);
					tempEnv = new PaasProjectDeployEnv();
					tempEnv.setKey("PAASOS_REDIS_HOST_PORT");
					tempEnv.setValue(allocateJson.getString("clusterPort"));
					envs.add(tempEnv);
				} else if ("define".equals(resource.getResourceId())) {
					String resultConfig = resource.getResultConfig();
					if (StringUtil.isNotNull(resultConfig)) {
						JSONArray array = JSONArray.parseArray(resultConfig);
						for (int j = 0; j < array.size(); j++) {
							JSONObject config = array.getJSONObject(j);
							PaasProjectDeployEnv tempEnv = new PaasProjectDeployEnv();
							tempEnv.setKey(config.getString("key"));
							tempEnv.setValue(config.getString("value"));
							envs.add(tempEnv);
						}
					}
				}
			}
		}
		deployResource.setEnvs(envs);
		deployResource.setVolumns(volumns);
		return deployResource;
	}

	@Override
	public DeployResource findDeployResourceFromAppApi(String envId,String userId) {
		PaasEnv env = getEnvById(envId);
		PaasProject paasProject = iPaasProjectService.getPaasProjectInfoById(env.getProjectId());
		logger.debug(paasProject==null?"无法获取查询" : envId);
		if(paasProject==null){
			throw new RuntimeException(envId+"获取的"+env.getProjectId()+" 无法获取paasos项目");
		}
		if((!"api".equals(paasProject.getProjectType())) &&  !"app".equals(paasProject.getProjectType())){
			return null;
		}
		String url = ConfigUtil.getConfigString("paasos."+paasProject.getProjectType().toLowerCase()+".url");
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		if("app".equals(paasProject.getProjectType().toLowerCase())){
			url = url + "app/env/deploy/envResource.do";
		}
		else if("api".equals(paasProject.getProjectType().toLowerCase())){
			url = url + "mod/service/env/deploy/envResource.do" ;
		}
		String r = null;
		StringBuffer sb = new StringBuffer(url);
		sb.append("?envId=").append(envId);
		sb.append("&envType=").append(env.getEnvType());
		sb.append("&serviceId=").append(paasProject.getBusinessId());
		sb.append("&appId=").append(paasProject.getBusinessId());
		try {
			r = PaasAPIUtil.get(userId, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取接口失败");
			return null;
		}

		if (!StringUtils.hasText(r)) {
			return null;
		}
		JSONObject result = JSONObject.parseObject(r);
		int code = result.getIntValue("code");
		if (code != 0) {
			logger.info("获取连接失败" + result.getString("msg"));
			return null;
		}
		JSONObject data = result.getJSONObject("data");
		data = data.getJSONObject("data");
		DeployResource envs = JSONObject.parseObject(data.toJSONString(), DeployResource.class);
		return envs;
	}
	
	@Override
	public DeployResource getDeployResouceAllByEnvId(String envId,String userId,String deployId){
		
		
		DeployResource dr1 = findDeployResourceFromAppApi(envId,userId);
		DeployResource dr2 = getDeployResouceByEnvId(envId);
		
		DeployResource r = new DeployResource();
		
		
		
		if(StringUtils.hasText(deployId)){
			PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
			PaasProject pp = iPaasProjectService.getPaasProjectInfoById(pd.getProjectId());
			
			PaasProjectDeployEnv v1 = new PaasProjectDeployEnv();
			v1.setKey("PROJECT_CODE");
			v1.setValue(pp.getProjectCode());
			
			PaasProjectDeployEnv v2 = new PaasProjectDeployEnv();
			v2.setKey("DEPLOY_CODE");
			v2.setValue(pd.getDeployCode());
			
			r.getEnvs().add(v1);
			r.getEnvs().add(v2);
		}
		
		
		if(dr1!=null && dr1.getEnvs()!=null){
			r.getEnvs().addAll(dr1.getEnvs());
		}
		if(dr1!=null && dr1.getPorts()!=null){
			r.getPorts().addAll(dr1.getPorts());
		}
		if(dr1!=null && dr1.getVolumns()!=null){
			r.getVolumns().addAll(dr1.getVolumns());
		}
		
		if(dr2!=null && dr2.getEnvs()!=null){
			r.getEnvs().addAll(dr2.getEnvs());
		}
		if(dr2!=null && dr2.getPorts()!=null){
			r.getPorts().addAll(dr2.getPorts());
		}
		if(dr2!=null && dr2.getVolumns()!=null){
			r.getVolumns().addAll(dr2.getVolumns());
		}
		return r;
	}
	
	@Override
	public DeployResource getDeployResouceAllByVersionId(String projectId,String versionId,String userId,PaasProjectDeploy deploy){
		//deploy 不一定是从数据库查出来的 小心使用 只设置了 envId 和envType
		PaasProject paasProject = iPaasProjectService.getPaasProjectInfoById(projectId);
		if(paasProject==null){
			throw new RuntimeException(projectId+"无法获取paasos项目");
		}
		if((!"api".equals(paasProject.getProjectType())) &&  !"app".equals(paasProject.getProjectType())){
			return null;
		}
		String url = ConfigUtil.getConfigString("paasos."+paasProject.getProjectType().toLowerCase()+".url");
		if (!url.endsWith("/")) {
			url = url + "/";
		}
		if("app".equals(paasProject.getProjectType().toLowerCase())){
			url = url + "app/env/deploy/version.do";
		}
		else if("api".equals(paasProject.getProjectType().toLowerCase())){
			url = url + "mod/service/env/deploy/version.do" ;
		}
		String r = null;
		StringBuffer sb = new StringBuffer(url);
		sb.append("?versionId=").append(versionId);
		sb.append("&serviceId=").append(paasProject.getBusinessId());
		sb.append("&envId=").append(deploy.getEnvId());
		sb.append("&envType=").append(deploy.getEnvType());
		if("app".equals(paasProject.getProjectType().toLowerCase())){
			sb.append("&appId=").append(paasProject.getBusinessId());
		}
		try {
			r = PaasAPIUtil.get(userId, sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取接口失败");
			return null;
		}

		if (!StringUtils.hasText(r)) {
			return null;
		}
		JSONObject result = JSONObject.parseObject(r);
		int code = result.getIntValue("code");
		if (code != 0) {
			logger.info("获取连接失败" + result.getString("msg"));
			return null;
		}
		JSONObject data = result.getJSONObject("data");
		List<PaasProjectDeployEnv> envs = JSONObject.parseArray(data.getJSONArray("env").toJSONString(), PaasProjectDeployEnv.class);
		DeployResource dr = new DeployResource();
		dr.setEnvs(envs);
		return dr;
	}

	@Override
	public Map<String, Object> getPaasOsInfo(String userId) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> totalOs = envMapper.getPaasOsInfo(null);
		List<Map<String,Object>> currentUserOs = envMapper.getPaasOsDeployInfo(userId);
		resultMap.put("deployCount",currentUserOs);
		resultMap.put("osCount", totalOs);
		return resultMap;
	}

}
