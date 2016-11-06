package com.harmazing.openbridge.paasos.project.controller;

import io.fabric8.kubernetes.api.model.EventList;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
import com.harmazing.openbridge.paasos.resource.model.PaasMysql;
import com.harmazing.openbridge.paasos.resource.model.PaasNAS;
import com.harmazing.openbridge.paasos.resource.model.PaasRabbitMQ;
import com.harmazing.openbridge.paasos.resource.model.PaasRedis;
import com.harmazing.openbridge.paasos.resource.service.IPaasMQService;
import com.harmazing.openbridge.paasos.resource.service.IPaasMysqlService;
import com.harmazing.openbridge.paasos.resource.service.IPaasNASService;
import com.harmazing.openbridge.paasos.resource.service.IPaasRedisService;
import com.harmazing.openbridge.paasos.utils.KubernetesUtil;

@Controller
@RequestMapping("/project/env/rest")
public class PaasProjectEnvRestController extends ProjectBaseController {

	@Autowired
	private IPaasProjectEnvService envService;
	@Autowired
	private IPaasMysqlService mysqlService;
	@Autowired
	private IPaasMQService mqService;
	@Autowired
	private IPaasNASService nasService;
	@Autowired
	private IPaasRedisService redisService;
	@Autowired
	private IPaasProjectDeployService paasProjectDeployService;

	// 添加资源
	@RequestMapping("/addEnv")
	@ResponseBody
	public JsonResponse addPaasEnv(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);

			String envName = request.getParameter("envName");
			String envType = request.getParameter("envType");
			String businessId = request.getParameter("businessId");
			String businessType = request.getParameter("businessType");
			String envMark = request.getParameter("envMark");
			
			PaasProject project = paasProjectService
					.getPaasProjectByBusinessIdAndType(businessId, businessType);

			String userId = user.getUserId();
			PaasEnv env = new PaasEnv();
			env.setEnvId(StringUtil.getUUID());
			env.setEnvName(envName);
			env.setEnvType(envType);
			env.setProjectId(project.getProjectId());
			env.setBusinessType(businessType);
			env.setCreator(userId);
			env.setCreationTime(new Date());
			env.setEnvMark(StringUtils.isEmpty(envMark)?null:envMark);
			envService.createEnv(env);

			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("添加环境失败" + e.getMessage());
			return JsonResponse.failure(500, "添加环境失败" + e.getMessage());
		}
	}

	// 获取环境列表信息，不包含资源
	@RequestMapping("/getEnvList")
	@ResponseBody
	public JsonResponse getPaasEnvList(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String businessId = request.getParameter("businessId");
			String businessType = request.getParameter("businessType");
			String[] ids = StringUtil.split(businessId);
			List<PaasEnv> passEnvs = new ArrayList<PaasEnv>();
			if(ids !=null && ids.length>0){
				for(int i=0; i<ids.length;i++){
					String id = ids[i];
					PaasProject project = paasProjectService
							.getPaasProjectByBusinessIdAndType(id, businessType);
					if (project == null) {
						return JsonResponse.failure(4, "该项目在PAASOS里面没有绑定:id="
								+ id + ",type=" + businessType);
					}
					List<PaasEnv> envlists = envService.getEnvListByBusinessId(project
							.getProjectId());
					passEnvs.addAll(envlists);
				}
			}
			return JsonResponse.success(passEnvs);
		} catch (Exception e) {
			logger.error("获取环境列表出错" + e.getMessage());
			return JsonResponse.failure(500, "获取环境列表出错");
		}
	}

	// 获取环境及包含的资源信息
	@RequestMapping("/getEnvInfo")
	@ResponseBody
	public JsonResponse getPaasEnvInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			PaasEnv env = envService.getEnvById(envId);
			return JsonResponse.success(env);
		} catch (Exception e) {
			logger.error("获取环境失败" + e.getMessage());
			return JsonResponse.failure(500, "获取环境失败" + e.getMessage());
		}
	}

	// 修改环境名称
	@RequestMapping("/updateEnv")
	@ResponseBody
	public JsonResponse updatePaasEnv(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			String envName = request.getParameter("envName");
			String envMark = request.getParameter("envMark");
			PaasEnv environment = envService.getEnvById(envId);
			environment.setEnvName(envName);
			environment.setEnvMark(StringUtils.isEmpty(envMark)?null:envMark);
			envService.updateEnv(environment);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("修改环境名称出错" + e.getMessage());
			return JsonResponse.failure(500, StringUtils.isEmpty(e.getMessage())?"修改环境名称出错":e.getMessage());
		}
	}

	// 删除环境
	@RequestMapping("/deleteEnv")
	@ResponseBody
	public JsonResponse deletePaasEnv(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");

			envService.deleteEnv(envId, user.getUserId());
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除环境出错" + e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	// 添加环境资源
	@RequestMapping("/addEnvResource")
	@ResponseBody
	public JsonResponse addPaasEnvResource(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			String resourceType = request.getParameter("resourceType");// mysql/redis/mq/nas
			String applyType = request.getParameter("applyType");// re-use old
																	// resource
			String type = request.getParameter("type");// 上前代表mysql的类型
			String oldResourceId = request.getParameter("oldResource");
			String newResourceAddition = request
					.getParameter("reourceNewAddition");
			String oldResourceAddition = request
					.getParameter("reourceOldAddition");
			String applySource = request.getParameter("source");// api or app
			String version = request.getParameter("version");
			String memory = request.getParameter("memory");
			String storage = request.getParameter("storage");
			PaasEnv env = envService.getEnvById(envId);
			String instanceName = env.getEnvName() + "_" + resourceType;
			String projectId = env.getProjectId();
			String envType = env.getEnvType();
			String source = request.getParameter("source");

			List<PaasEnvResource> appliedResources = (List<PaasEnvResource>) env
					.getResources();
			if (appliedResources != null && appliedResources.size() > 0) {
				for (PaasEnvResource tempRes : appliedResources) {
					if (tempRes.getResourceId().equals(resourceType)) {
//						throw new RuntimeException("一个环境不能重复申请同一资源");
						logger.error("添加环境资源出错: 一个环境不能重复申请同一资源");
						return JsonResponse.failure(500, "一个环境不能重复申请同一资源");
					}
				}
			}

			String resAddition = StringUtil.trimWhitespace(newResourceAddition);
			if ("old".equals(applyType)) {
				resAddition = StringUtil.trimWhitespace(oldResourceAddition);
				envService.addEnvResourceByReuse(envId, oldResourceId, resAddition,resourceType);
			} else {
				if ("mysql51".equals(resourceType)) {
					if(StringUtil.isNull(type)){
						type="mysql";
					}
					JSONObject MysqlConfig = new JSONObject();
					MysqlConfig.put("name", instanceName);
					MysqlConfig.put("memory", memory);
					MysqlConfig.put("storage", storage);
					MysqlConfig.put("version", version);
					MysqlConfig.put("type", type);
					JSONObject attributes = new JSONObject();
					attributes.put("envId", envId);
					attributes.put("projectId", projectId);
					attributes.put("envType", envType);

					MysqlConfig.put("attributes", attributes);
					PaasMysql mysql = new PaasMysql();
					mysql.setMysqlId(StringUtil.getUUID());
					mysql.setInstanceName(instanceName);
					mysql.setMysqlType(type);
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
					mysql.setApplyType(source);
					envService.applyMysql(envId, "mysql51", resAddition, mysql);
				} else if ("nas".equals(resourceType)) {
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
					nfs.setApplyType(source);
					envService.applyNas(envId, "nas", resAddition, nfs);
				} else if ("rocket".equals(resourceType)) {
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
					mq.setApplyType(source);
					envService.applyRabbitMQ(envId, "rocket", mq);
				} else if ("redis".equals(resourceType)) {
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
					redis.setUpdateDate(now);javascript:;
					redis.setAllocateContent("");
					redis.setTenantId(user.getTenantId());
					redis.setProjectId(projectId);
					redis.setEnvId(envId);
					redis.setEnvType(envType);
					redis.setOnReady(false);
					redis.setApplyType(source);
					envService.applyRedis(envId, "redis", redis);
				}
			}

			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("添加环境资源出错" + e.getMessage(),e);
			return JsonResponse.failure(500, StringUtils.hasText(e.getMessage())?e.getMessage() : "添加环境资源出错");
		}
	}

	// 删除环境资源
	@RequestMapping("/deleteEnvResource")
	@ResponseBody
	public JsonResponse deletePaasEnvResource(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			String envResourceId = request.getParameter("recordId");

			envService.deleteEnvResource(user.getUserId(), user.getTenantId(),
					envResourceId);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除环境资源出错" + e.getMessage());
			return JsonResponse.failure(500, "删除资源出错");
		}
	}

	//获取环境的环境变量
	@RequestMapping("/getEnvironmentByEnv")
	@ResponseBody
	public JsonResponse getEnvironmentByEnv(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			Map<String, String> params = envService.getEnvironmentByEnvId(
					user.getUserId(), user.getTenantId(), envId);
			return JsonResponse.success(params);
		} catch (Exception e) {
			logger.error("无法获取环境的资源参数");
			return JsonResponse.failure(500, "无法获取环境的资源参数");
		}
	}

	//获取资源的运行状态
	@RequestMapping("/getEnvResourceStatus")
	@ResponseBody
	public JsonResponse getEnvResourceStatus(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String recordId = request.getParameter("recordId");
			JSONObject obj = envService.getPaasResourceStatus(user.getUserId(),
					user.getTenantId(), recordId);
			return JsonResponse.success(obj);
		} catch (Exception e) {
			logger.error("无法获取环境的状态",e);
			return JsonResponse.failure(500, "无法获取环境的状态");
		}
	}
	
	//获取单个资源的所有信息
	@RequestMapping("/getEnvResource")
	@ResponseBody
	public JsonResponse resourceQueryInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String recordId = request.getParameter("recordId");
			PaasEnvResource paasEnvResource = envService
					.getEnvResourceInfoById(recordId);
			return JsonResponse.success(paasEnvResource);
		}catch(Exception e){
			logger.error("");
			return JsonResponse.failure(500, "无法获取环境的资源参数");
		}
	}
	
	//保存自定义变量
	@RequestMapping("/saveOrUpdateDefines")
	@ResponseBody
	public JsonResponse saveOrUpdateDefines(HttpServletRequest request,
			HttpServletResponse response) {
		try {
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
	
	// 获取环境列表信息，不包含资源
	@RequestMapping("/getEnvDeployInfo")
	@ResponseBody
	public JsonResponse getPaasEnvDepolyInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			DeployResource deployInfo = envService.getDeployResouceByEnvId(envId);
			return JsonResponse.success(deployInfo);
		} catch (Exception e) {
			logger.error("获取环境部署参数出错" + e.getMessage());
			return JsonResponse.failure(500, "获取环境部署参数出错");
		}
	}
	
	@RequestMapping("/page")//查询列表信息
	@ResponseBody
	public JsonResponse page(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			String envType = request.getParameter("envType");
			String resouceType = request.getParameter("resouceType");
			String applyType = request.getParameter("source");
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			params.put("envType", envType);
			params.put("userId", user.getUserId());
			params.put("tenantId", user.getTenantId());
			if("true".equals(ConfigUtil.getOrElse("ShareResourceByTenant","true"))){
				params.put("byTenantId", "true");
			}
			if(StringUtil.isNull(applyType) || !"api".equals(applyType)&&!"app".equals(applyType)){
				applyType="paasOS";
			}
			params.put("applyType", applyType);
			
			JSONObject jsonData = new JSONObject();
			jsonData.put("pageSize", pageSize);
			jsonData.put("pageNo", pageNo);
			if("mysql51".equals(resouceType)){
				List<PaasMysql> pageData = mysqlService.queryPaasMysqls(params);
				if(pageData!=null && pageData.size()>0){
					jsonData.put("pageCount", ((Page<PaasMysql>)pageData).getPageCount());
					JSONArray array = new JSONArray();
					for(PaasMysql mysql : pageData){
						JSONObject ob = new JSONObject();
						ob.put("id", mysql.getMysqlId());
						ob.put("name", mysql.getInstanceName());
						ob.put("memory", mysql.getAllocateMemInfo());
						ob.put("storage", mysql.getAllocateStorageInfo());
						ob.put("desc", mysql.getResDesc());
						array.add(ob);
					}
					jsonData.put("data", array);
				}
			}else if("redis".equals(resouceType)){
				List<PaasRedis> pageData = redisService.queryPaasRediss(params);
				if(pageData!=null && pageData.size()>0){
					jsonData.put("pageCount", ((Page<PaasRedis>)pageData).getPageCount());
					JSONArray array = new JSONArray();
					for(PaasRedis redis : pageData){
						JSONObject ob = new JSONObject();
						ob.put("id", redis.getRedisId());
						ob.put("name", redis.getRedisName());
						ob.put("memory", redis.getAllocateEnvInfo());
						ob.put("desc", redis.getResDesc());
						array.add(ob);
					}
					jsonData.put("data", array);
				}
			}else if("rocket".equals(resouceType)){
				List<PaasRabbitMQ> pageData = mqService.queryPaasMQs(params);
				if(pageData!=null && pageData.size()>0){
					jsonData.put("pageCount", ((Page<PaasRabbitMQ>)pageData).getPageCount());
					JSONArray array = new JSONArray();
					for(PaasRabbitMQ mq : pageData){
						JSONObject ob = new JSONObject();
						ob.put("id", mq.getMqId());
						ob.put("name", mq.getMqName());
						ob.put("memory", mq.getAllocateEnvInfo());
						ob.put("desc", mq.getResDesc());
						array.add(ob);
					}
					jsonData.put("data", array);
				}
			}else if("nas".equals(resouceType)){
				params.put("nasType", "NFS");
				List<PaasNAS> pageData = nasService.queryPaasNASs(params);
				if(pageData!=null && pageData.size()>0){
					jsonData.put("pageCount", ((Page<PaasNAS>)pageData).getPageCount());
					JSONArray array = new JSONArray();
					for(PaasNAS nas : pageData){
						JSONObject ob = new JSONObject();
						ob.put("id", nas.getNasId());
						ob.put("name", nas.getInstanceName());
						ob.put("storage", nas.getAllocateStorageInfo());
						ob.put("desc", nas.getResDesc());
						array.add(ob);
					}
					jsonData.put("data", array);
				}
			}
			return JsonResponse.success(jsonData);
		}catch(Exception e){
			request.setAttribute("exception", e);
			logger.error("获取共享资源列表信息出错");
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getTopInstance")
	@ResponseBody
	public JsonResponse getTopInstance(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("top", Integer.parseInt(request.getParameter("top")));
			params.put("tenantId", user.getTenantId());
			Map<String, Object> r=paasProjectDeployService.getTopInstance(params);
			return JsonResponse.success(r);
		} catch (Exception e) {
			logger.error("获取实例出错" + e.getMessage());
			return JsonResponse.failure(500, "获取实例出错");
		}
	}
	
	@RequestMapping("/getPodEvent")
	@ResponseBody
	public JsonResponse getPodEvent(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String podName = request.getParameter("podName");
			String namespace = request.getParameter("namespace");
			if(StringUtil.isNull(podName) || StringUtil.isNull(namespace)){
				return JsonResponse.failure(404, "参数错误");
			}
			EventList msg = KubernetesUtil.getPodEventLogs(namespace, podName);
			return JsonResponse.success(msg,"获取pods事件信息成功");
		} catch (Exception e) {
			logger.error("获取pod事件日志信息失败", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	/**
	* @Title: osinfo 
	* @author weiyujia
	* @Description: 获取OS相关信息，如服务总数，服务市场等
	* @param request
	* @param response
	* @date 2016年8月8日 下午6:03:03
	 */
	@RequestMapping("/osinfo")
	@ResponseBody
	public JsonResponse osinfo(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap = envService.getPaasOsInfo(user.getUserId());
			return JsonResponse.success(resultMap);
		} catch (Exception e) {
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
