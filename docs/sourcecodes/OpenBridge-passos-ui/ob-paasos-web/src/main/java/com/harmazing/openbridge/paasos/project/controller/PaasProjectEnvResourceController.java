package com.harmazing.openbridge.paasos.project.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.extension.Plugin;
import com.harmazing.framework.extension.interfaces.IConfiguration;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paas.plugin.xml.ResourceNode;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
import com.harmazing.openbridge.paasos.resource.model.PaasMysql;
import com.harmazing.openbridge.paasos.resource.model.PaasNAS;
import com.harmazing.openbridge.paasos.resource.model.PaasRabbitMQ;
import com.harmazing.openbridge.paasos.resource.model.PaasRedis;
import com.harmazing.openbridge.paasos.resource.service.IPaasMQService;
import com.harmazing.openbridge.paasos.resource.service.IPaasMysqlService;
import com.harmazing.openbridge.paasos.resource.service.IPaasNASService;
import com.harmazing.openbridge.paasos.resource.service.IPaasRedisService;

@Controller
@RequestMapping("/project/env/resource")
public class PaasProjectEnvResourceController extends ProjectBaseController {
	@Autowired
	private IPaasProjectEnvService envService;
	@Autowired
	private IPaasMysqlService mysqlService;
	@Autowired
	private IPaasNASService nasService;
	@Autowired
	private IPaasRedisService redisService;
	@Autowired
	private IPaasMQService mqService;

	@RequestMapping("/environment")
	public String resourceEnvironment(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			request.setAttribute("envId", envId);
			Map<String,String> envVariables = envService.getEnvironmentByEnvId(user.getUserId(),
					user.getTenantId(), envId);
			Map<String,String> sysEnv = new HashMap<String,String>();
			Map<String,String> defineEnv = new HashMap<String,String>();
			String[] sysEnvArray = new String[]{"PAASOS_NFS_PATH","PAASOS_MYSQL_USERNAME","PAASOS_MYSQL_PASSWORD",
					"PAASOS_MYSQL_DATABASE","PAASOS_MYSQL_HOST_IP", "PAASOS_MYSQL_HOST_PORT","PAASOS_REDIS_HOST_IP",
					"PAASOS_REDIS_HOST_PORT","PAASOS_REDIS_USERNAME","PAASOS_REDIS_PASSWORD","PAASOS_RABBIT_HOST_IP","PAASOS_RABBIT_HOST_PORT"};
			Iterator<String>  keyIterator = envVariables.keySet().iterator();
			while(keyIterator.hasNext()){
				String key =keyIterator.next();
				int j=0;
				for(;j<sysEnvArray.length;j++){
					if(key.equals(sysEnvArray[j])){
						sysEnv.put(key, envVariables.get(key));
						break;
					}
				}
				if(j==sysEnvArray.length){
					defineEnv.put(key, envVariables.get(key));
				}
			}
			request.setAttribute("environment",sysEnv);
			request.setAttribute("defineEnv",defineEnv);

			return getUrlPrefix() + "/environment";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/add")
	public String resourceAdd(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			PaasEnv env = envService.getEnvById(envId);
			request.setAttribute("env", env);

			Map<String, IConfiguration> configs = Plugin.getExtensionPoint(
					"com.harmazing.paas.resource").getConfigNodes("resource");

			Map<String, List<ResourceNode>> data = new LinkedHashMap<String, List<ResourceNode>>();
			Iterator<Map.Entry<String, IConfiguration>> it = configs.entrySet()
					.iterator();
			data.put("database", new ArrayList<ResourceNode>());
			data.put("cache", new ArrayList<ResourceNode>());
			data.put("storage", new ArrayList<ResourceNode>());
			data.put("mq", new ArrayList<ResourceNode>());
			//data.put("custom", new ArrayList<ResourceNode>());
			while (it.hasNext()) {
				Map.Entry<String, IConfiguration> config = it.next();
				ResourceNode bridge = (ResourceNode) config.getValue();
				if (bridge != null && data.get(bridge.getCategory()) != null)
					data.get(bridge.getCategory()).add(bridge);
			}
			request.setAttribute("resources", data);

			return getUrlPrefix() + "/add";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/apply")
	public String resourceApply(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			request.setAttribute("project", project);
			String envId = request.getParameter("envId");
			String resId = request.getParameter("resId");
			PaasEnv env = envService.getEnvById(envId);
			ResourceNode resource = (ResourceNode) Plugin.getExtensionPoint(
					"com.harmazing.paas.resource").getConfigNode("resource",
					resId);
			request.setAttribute("resource", resource);
			JSONObject options =null;
			if("mysql51".equals(resId)){
				options = mysqlService.getPaasMysqlOptions(user.getUserId(), user.getTenantId());
			}else if("redis".equals(resId)){
				options = redisService.getPaasRedisOptions(user.getUserId(), user.getTenantId());
			}else if("nas".equals(resId)){
				options = nasService.getPaasNASOptions(user.getUserId(), user.getTenantId(),"NFS");
			}else if("rocket".equals(resId)){
				options = mqService.getPaasRabbitMQOptions(user.getUserId(), user.getTenantId());
			}
			if("nas".equals(resId)){
				JSONArray limits = options.getJSONArray("storage");
				for(int index=0;index< limits.size();index++){
					JSONObject envLimit = limits.getJSONObject(index);
					if(envLimit.getString("envType").equals(env.getEnvType())){
						options.remove("storage");
						options.put("storage", envLimit.getJSONArray("free"));
					}
				}
			}
			if(options.size()>0){
				request.setAttribute("options", options);
			}
			request.setAttribute("env", env);
			return getUrlPrefix() + "/apply";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse resourceSave(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			String resId = request.getParameter("resId");
			PaasEnv env = envService.getEnvById(envId);
			
			String applyType = request.getParameter("applyType");// re-use old
																	// resource
			String oldResourceId = request.getParameter("oldResource");
			String newResourceAddition = request
					.getParameter("reourceNewAddition");
			String oldResourceAddition = request
					.getParameter("reourceOldAddition");
			String applySource = request.getParameter("source");// api or app
			if(StringUtil.isNull(applySource) || !"api".equals(applySource)&&!"app".equals(applySource)){
				applySource="paasOS";
			}
			String version = request.getParameter("version");
			String memory = request.getParameter("memory");
			String storage = request.getParameter("storage");
			String instanceName = env.getEnvName() + "_" + resId;
			String projectId = env.getProjectId();
			String envType = env.getEnvType();

			List<PaasEnvResource> appliedResources = (List<PaasEnvResource>) env
					.getResources();
			if (appliedResources != null && appliedResources.size() > 0) {
				for (PaasEnvResource tempRes : appliedResources) {
					if (tempRes.getResourceId().equals(resId)) {
						throw new RuntimeException("一个环境不能重复申请同一资源");
					}
				}
			}

			String resAddition = StringUtil.trimWhitespace(newResourceAddition);
			if ("old".equals(applyType)) {
				resAddition = StringUtil.trimWhitespace(oldResourceAddition);
				envService.addEnvResourceByReuse(envId, oldResourceId, resAddition, resId);
			} else {
				if ("mysql51".equals(resId)) {
					JSONObject MysqlConfig = new JSONObject();
					MysqlConfig.put("name", instanceName);
					MysqlConfig.put("memory", memory);
					MysqlConfig.put("storage", storage);
					MysqlConfig.put("version", version);
					MysqlConfig.put("type", "mysql");
					JSONObject attributes = new JSONObject();
					attributes.put("envId", envId);
					attributes.put("projectId", projectId);
					attributes.put("envType", envType);

					MysqlConfig.put("attributes", attributes);
					PaasMysql mysql = new PaasMysql();
					mysql.setMysqlId(StringUtil.getUUID());
					mysql.setInstanceName(instanceName);
					mysql.setMysqlType("mysql");
					mysql.setApplyContent(MysqlConfig.toJSONString());
					mysql.setCreater(user.getUserId());
					Date now = new Date();
					mysql.setCreateDate(now);
					mysql.setUpdateDate(now);
					mysql.setAllocateContent("");
					mysql.setTenantId(user.getTenantId());
					mysql.setProjectId(projectId);
					mysql.setEnvId(envId);
					mysql.setEnvType(envType);
					mysql.setOnReady(false);
					mysql.setResDesc("");
					mysql.setApplyType(applySource);
					envService.applyMysql(envId, "mysql51", resAddition, mysql);
				} else if ("nas".equals(resId)) {
					JSONObject Config = new JSONObject();
					Config.put("name", instanceName);
					Config.put("size", storage);
					Config.put("version", version);
					JSONObject attributes = new JSONObject();
					attributes.put("envId", envId);
					attributes.put("projectId", projectId);
					attributes.put("envType", envType);
					Config.put("attributes", attributes);
					PaasNAS nfs = new PaasNAS();
					nfs.setNasId(StringUtil.getUUID());
					nfs.setInstanceName(instanceName);
					nfs.setNasSource("PaasOS-UI");
					nfs.setApplyContent(Config.toJSONString());
					nfs.setCreater(user.getUserId());
					Date now = new Date();
					nfs.setCreateDate(now);
					nfs.setUpdateDate(now);
					nfs.setAllocateContent("");
					nfs.setTenantId(user.getTenantId());
					nfs.setProjectId(projectId);
					nfs.setEnvId(envId);
					nfs.setEnvType(envType);
					nfs.setOnReady(false);
					nfs.setOnStatus(0);// 初始状态
					nfs.setNasType("NFS");
					nfs.setResDesc("");
					nfs.setApplyType(applySource);
					envService.applyNas(envId, "nas", resAddition, nfs);
				} else if ("rocket".equals(resId)) {
					JSONObject RabbitMQConfig = new JSONObject();
					RabbitMQConfig.put("name", instanceName);
					RabbitMQConfig.put("memory", memory);
					RabbitMQConfig.put("version", version);
					JSONObject attributes = new JSONObject();
					attributes.put("envId", envId);
					attributes.put("projectId", projectId);
					attributes.put("envType", envType);
					RabbitMQConfig.put("attributes", attributes);
					PaasRabbitMQ mq = new PaasRabbitMQ();
					mq.setMqId(StringUtil.getUUID());
					mq.setMqName(instanceName);
					mq.setApplyContent(RabbitMQConfig.toJSONString());
					mq.setCreater(user.getUserId());
					Date now = new Date();
					mq.setCreateDate(now);
					mq.setUpdateDate(now);
					mq.setAllocateContent("");
					mq.setTenantId(user.getTenantId());
					mq.setProjectId(projectId);
					mq.setEnvId(envId);
					mq.setEnvType(envType);
					mq.setOnReady(false);
					mq.setApplyType(applySource);
					envService.applyRabbitMQ(envId, "rocket", mq);
				} else if ("redis".equals(resId)) {
					JSONObject RedisConfig = new JSONObject();
					RedisConfig.put("name", instanceName);
					RedisConfig.put("memory", memory);
					RedisConfig.put("version", version);
					JSONObject attributes = new JSONObject();
					attributes.put("envId", envId);
					attributes.put("projectId", projectId);
					attributes.put("envType", envType);
					RedisConfig.put("attributes", attributes);
					PaasRedis redis = new PaasRedis();
					redis.setRedisId(StringUtil.getUUID());
					redis.setRedisName(instanceName);
					redis.setApplyContent(RedisConfig.toJSONString());
					redis.setCreater(user.getUserId());
					Date now = new Date();
					redis.setCreateDate(now);
					redis.setUpdateDate(now);
					redis.setAllocateContent("");
					redis.setTenantId(user.getTenantId());
					redis.setProjectId(projectId);
					redis.setEnvId(envId);
					redis.setEnvType(envType);
					redis.setOnReady(false);
					redis.setApplyType(applySource);
					envService.applyRedis(envId, "redis", redis);
				}
			}
			request.setAttribute("env", env);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("申请资源出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/del")
	@ResponseBody
	public JsonResponse resourceDel(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			String recordId = request.getParameter("recordId");
			envService.deleteEnvResource(user.getUserId(), user.getTenantId(), recordId);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除资源出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/view")
	public String resourceView(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String envId = request.getParameter("envId");
			String recordId = request.getParameter("recordId");
			PaasEnvResource paasEnvResource = envService
					.getEnvResourceInfoById(recordId);

			request.setAttribute("resource", paasEnvResource);

			return getUrlPrefix() + "/view";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/queryResourceInfo")
	@ResponseBody
	public JsonResponse resourceQueryInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String recordId = request.getParameter("recordId");
			JSONObject returnJson = new JSONObject();
			PaasEnvResource paasEnvResource = envService
					.getEnvResourceInfoById(recordId);
			String resourceId = paasEnvResource.getResourceId();
			if("nas".equals(resourceId)){
				returnJson.put("PAASOS_NFS_PATH", paasEnvResource.getResAddition());
			}
			String resultConfig = paasEnvResource.getResultConfig();
			JSONObject jsonObj = JSONObject.parseObject(resultConfig);
			
			if("mysql51".equals(resourceId)){
				returnJson.put("PAASOS_MYSQL_USERNAME", jsonObj.getString("username"));
				returnJson.put("PAASOS_MYSQL_PASSWORD", jsonObj.getString("password"));
				returnJson.put("PAASOS_MYSQL_DATABASE",paasEnvResource.getResAddition());
				returnJson.put("PAASOS_MYSQL_HOST_IP", jsonObj.getString("clusterIP"));
				returnJson.put("PAASOS_MYSQL_HOST_PORT", jsonObj.getString("clusterPort"));
			}else if("redis".equals(resourceId)){
				returnJson.put("PAASOS_REDIS_USERNAME", jsonObj.getString("username"));
				returnJson.put("PAASOS_REDIS_PASSWORD", jsonObj.getString("password"));
				returnJson.put("PAASOS_REDIS_HOST_IP", jsonObj.getString("clusterIP"));
				returnJson.put("PAASOS_REDIS_HOST_PORT", jsonObj.getString("clusterPort"));
			}else if("rocket".equals(resourceId)){
				returnJson.put("PAASOS_RABBIT_HOST_IP", jsonObj.getString("clusterIP"));
				returnJson.put("PAASOS_RABBIT_HOST_PORT", jsonObj.getString("clusterPort"));
			}
			return JsonResponse.success(returnJson);
		} catch (Exception e) {
			logger.error("获取环境变量信息出错"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/help")
	public String resourceHelpInfo(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String recordId = request.getParameter("recordId");
			PaasEnvResource paasEnvResource = envService.getEnvResourceById(recordId);
			String resourceId = paasEnvResource.getResourceId();
			ResourceNode resourceNode = (ResourceNode) Plugin
					.getExtensionPoint("com.harmazing.paas.resource")
					.getConfigNode("resource", resourceId);
			String helpPage = resourceNode.getHelp();
			if("nas".equals(resourceId)){
				request.setAttribute("PAASOS_NFS_PATH", paasEnvResource.getResAddition());
			}
			String paasosResId = paasEnvResource.getPaasosResId();
			if("mysql51".equals(resourceId)){
				JSONObject jsonObj = mysqlService.getPaasMysqlConfig(paasosResId);
				request.setAttribute("PAASOS_MYSQL_USERNAME", jsonObj.getString("username"));
				request.setAttribute("PAASOS_MYSQL_PASSWORD", jsonObj.getString("password"));
				request.setAttribute("PAASOS_MYSQL_DATABASE",paasEnvResource.getResAddition());
				request.setAttribute("PAASOS_MYSQL_HOST_IP", jsonObj.getString("clusterIP"));
				request.setAttribute("PAASOS_MYSQL_HOST_PORT", jsonObj.getString("clusterPort"));
				request.setAttribute("externalIP", jsonObj.getString("externalIP"));
				request.setAttribute("externalPort", jsonObj.getString("externalPort"));
			}else if("redis".equals(resourceId)){
				JSONObject jsonObj = redisService.getPaasRedisConfig(paasosResId);
				request.setAttribute("PAASOS_REDIS_HOST_IP", jsonObj.getString("clusterIP"));
				request.setAttribute("PAASOS_REDIS_HOST_PORT", jsonObj.getString("clusterPort"));
				request.setAttribute("PAASOS_REDIS_USERNAME", jsonObj.getString("username"));
				request.setAttribute("PAASOS_REDIS_PASSWORD", jsonObj.getString("password"));
				request.setAttribute("externalIP", jsonObj.getString("externalIP"));
				request.setAttribute("externalPort", jsonObj.getString("externalPort"));
				request.setAttribute("manageIP", jsonObj.getString("manage.externalIP"));
				request.setAttribute("managePort", jsonObj.getString("manage.externalPort"));
			}else if("rocket".equals(resourceId)){
				JSONObject jsonObj = mqService.getPaasMQConfig(paasosResId);
				request.setAttribute("PAASOS_RABBIT_HOST_IP", jsonObj.getString("clusterIP"));
				request.setAttribute("PAASOS_RABBIT_HOST_PORT", jsonObj.getString("clusterPort"));
				
				request.setAttribute("externalIP", jsonObj.getString("externalIP"));
				request.setAttribute("externalPort", jsonObj.getString("externalPort"));
				
				request.setAttribute("manageIP", jsonObj.getString("manage.externalIP"));
				request.setAttribute("managePort", jsonObj.getString("manage.externalPort"));
			}
			return this.forward(helpPage);
		}catch(Exception e){
			logger.error("查看环境资源帮助信息出错"+e.getMessage());
			return this.forward(ERROR);
		}
	}
	
	@RequestMapping("/status")
	@ResponseBody
	public JsonResponse resourceStatus(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			String recordId = request.getParameter("recordId");
			PaasEnvResource paasEnvResource = envService.getEnvResourceById(recordId);
			String resourceType = paasEnvResource.getResourceId();
			String paasosResId = paasEnvResource.getPaasosResId();
			JSONObject obj =null;
			String msg="error";
			if("mysql51".equals(resourceType)){
				obj = mysqlService.queryMysqlInfo(paasosResId,user.getUserId(),user.getTenantId());
			}else if ("redis".equals(resourceType)){
				obj = redisService.queryRedisInfo(paasosResId, user.getUserId(),user.getTenantId());
			}else if ("rocket".equals(resourceType)){
				obj = mqService.queryMQInfo(paasosResId, user.getUserId(),user.getTenantId());
			}else if ("nas".equals(resourceType)){
				obj = nasService.queryNASInfo(paasosResId,user.getUserId(),user.getTenantId());
				if(obj.containsKey("id")){
					JSONObject returnObj = new JSONObject();
					returnObj.put("status", "运行中");
					return JsonResponse.success(returnObj);
				}
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
			return JsonResponse.success(returnObj);
		} catch (Exception e) {
			logger.error("获取环境资源状态出错"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	
	@RequestMapping("/saveOrUpdateDefines")
	@ResponseBody
	public JsonResponse saveOrUpdateDefines(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			loadProjectModel(request);
			String data = request.getParameter("data");
			String envId = request.getParameter("envId");
			if(StringUtil.isNull(envId)){
				throw new Exception("缺少envId参数");
			}
			envService.saveOrUpdateEnvResourceDefine(envId, data);
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("保存自定义环境变量出错"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
