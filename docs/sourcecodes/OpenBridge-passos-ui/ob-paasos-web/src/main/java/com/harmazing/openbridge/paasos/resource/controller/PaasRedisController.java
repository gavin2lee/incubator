package com.harmazing.openbridge.paasos.resource.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.util.StorageUtil;
import com.harmazing.openbridge.paasos.resource.model.PaasRedis;
import com.harmazing.openbridge.paasos.resource.service.IPaasRedisService;
import com.harmazing.openbridge.paasos.utils.ConfigUtils;

@Controller
@RequestMapping("/resource/redis")
public class PaasRedisController extends PaasResourceBaseController {
	protected static final Log logger = LogFactory
			.getLog(PaasRedisController.class);
	
	@Autowired
	private IPaasRedisService redisService;
	
	@RequestMapping("/list")//首页的列表
	public String list(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			String listType = request.getParameter("listType");
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			/*if(!user.isAdministrator()){
				params.put("userId", user.getUserId());
			}*/
			if(listType != null && listType.equals("tenant")){
				params.put("tenantId", user.getTenantId());
			}else{ 
				params.put("userId", user.getUserId());
				params.put("tenantId", user.getTenantId());
			}
			List<PaasRedis> pageData = redisService.queryPaasRedissByParams(params);
			request.setAttribute("pageData", pageData);
			return getUrlPrefix()+"/list";
		}catch(Exception e){
			request.setAttribute("exception", e);
			logger.error("获取缓存列表信息出错");
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/queryRedisStatusInfo")//实时rest请求查询redis实例的运行状态
	@ResponseBody
	public JsonResponse queryRedisInfo(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String resId = request.getParameter("resId");
			String paasOsUserId = user.getUserId();
			String paasOsTenantId = user.getTenantId();
			if(StringUtil.isNull(resId)){
				return JsonResponse.failure(403, "查询参数不正确");
			}
			JSONObject RedisObj = redisService.queryRedisInfo(resId, paasOsUserId, paasOsTenantId);
			String status = RedisObj.getString("status");
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
			JSONObject returnObj = new JSONObject();
			returnObj.put("status", status);
			if(RedisObj.containsKey("podInfo")){
				if(RedisObj.getJSONObject("podInfo")!=null){
					returnObj.put("podName", RedisObj.getJSONObject("podInfo").get("podName"));
					returnObj.put("namespace", RedisObj.getJSONObject("podInfo").get("namespace"));
				}
			}
			return JsonResponse.success(returnObj);
		}catch(Exception e){
			logger.error("查询redis运行状态失败"+e.getMessage());
			return JsonResponse.failure(500, "查询redis运行状态失败");
		}
	}
	
	@Override
	protected void setResourceLimit2Request(HttpServletRequest request){
		String userId = (String) request.getAttribute("userId");
		String tenantId = (String)request.getAttribute("tenantId");
		JSONObject RedisOptions = redisService.getPaasRedisOptions(userId, tenantId); 
		if(RedisOptions!=null){
			request.setAttribute("options", RedisOptions);
		}
	}

	@RequestMapping("/options")//实时rest请求查询redis配额信息
	@ResponseBody
	public JsonResponse queryRedisOptions(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			JSONObject RedisOptions = redisService.getPaasRedisOptions(userId, tenantId);
			return JsonResponse.success(RedisOptions);
		}catch(Exception e){
			logger.error("查询redis配额信息失败"+e.getMessage());
			return JsonResponse.failure(500, "查询redis配额信息失败");
		}
	}
	
	@RequestMapping("/save")//保存redis实例的操作
	@ResponseBody
	public JsonResponse addRedisSave(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String redisName = request.getParameter("instanceName");
			String version = request.getParameter("version");
			String memory = request.getParameter("memory");
			String projectId = request.getParameter("projectId");
			String envId = request.getParameter("envId");
			String envType = request.getParameter("envType");
			String desc = request.getParameter("resDesc");
			String applyType = request.getParameter("applyType");
			if(StringUtil.isNull(applyType) || !"api".equals(applyType)&&!"app".equals(applyType)){
				applyType="paasOS";
			}
			if(StringUtil.isNull(redisName)|| StringUtil.isNull(tenantId)){
				return JsonResponse.failure(400, "新建reids资源失败,实例名和租户不能为空");
			}
			JSONObject RedisConfig = new JSONObject();
			PaasRedis redis = new PaasRedis();
			RedisConfig.put("name", redisName);
			RedisConfig.put("memory", memory);
			
			redis.setMemory(StorageUtil.getMemory(memory));
			
			RedisConfig.put("version", version);
			JSONObject attributes = new JSONObject();
			attributes.put("envId", envId);
			attributes.put("projectId", projectId);
			attributes.put("envType", envType);
			RedisConfig.put("attributes", attributes);
			
			redis.setRedisId(StringUtil.getUUID());
			redis.setRedisName(redisName);
			redis.setApplyContent(RedisConfig.toJSONString());
			redis.setCreater(userId);
			Date now = new Date();
			redis.setCreateDate(now);
			redis.setUpdateDate(now);
			redis.setAllocateContent("");
			redis.setTenantId(tenantId);
			redis.setProjectId(projectId);
			redis.setEnvId(envId);
			redis.setEnvType(envType);
			redis.setOnReady(false);
			redis.setResDesc(desc);
			redis.setApplyType(applyType);
			redisService.addPaasRedisResource(redis);
			logger.debug("成功添加redis实例，"+redisName);
			return JsonResponse.success(redis.getRedisId());
		}catch(Exception e){
	
			logger.error("添加redis实例失败"+e.getMessage(),e);
			return JsonResponse.failure(500, "新建redis资源失败"+e.getMessage());
		}
	}
	
	@RequestMapping("/delete")//删除redis的操作
	@ResponseBody
	public JsonResponse deleteRedis(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String redisId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(redisId)||StringUtil.isNull(tenantId)||StringUtil.isNull(userId)){
				logger.error("删除redis资源出错，resId/userId/tenantId参数为空");
				return JsonResponse.failure(500, "删除redis资源失败,resId/userId/tenantId参数为空");
			}
			JSONObject res = redisService.deletePaasRedisById(redisId,userId, tenantId);
			if(res.containsKey("result") && res.getString("result").equals("success")){
				logger.debug("删除redis资源"+redisId);
				return JsonResponse.success(redisId);
			}else{
				logger.error("删除redis资源操作失败"+redisId);
				return JsonResponse.failure(500, "删除redis资源失败");
			}
		}catch(Exception e){
			logger.error("删除redis资源操作失败"+e.getMessage());
			return JsonResponse.failure(500, "删除redis资源失败"+e.getMessage());
		}
	}
	
	@RequestMapping("/info")//查看redis详情页面
	public String RedisInfo(HttpServletRequest request, HttpServletResponse response){
		String redisId = request.getParameter("redisId");
		PaasRedis redisInfo = redisService.getPaasRedisInfoById(redisId);
		request.setAttribute("redisInfo", redisInfo);
		ConfigUtils.setEnvType2Request(request);
		return getUrlPrefix()+"/info";
	}
	
	@RequestMapping("/startRedis")
	@ResponseBody
	public JsonResponse startPassRedis(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String resId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(resId)||StringUtil.isNull(tenantId)||StringUtil.isNull(userId)){
				logger.error("启动redis出错，resId/userId/tenantId参数为空");
				return JsonResponse.failure(500, "启动redis失败,resId/userId/tenantId参数为空");
			}
			JSONObject res = redisService.startPaasRedis(resId,userId, tenantId);
			if(res.containsKey("result") && res.getString("result").equals("success")){
				logger.debug("成功启动redis--"+resId);
				return JsonResponse.success();
			}else{
				logger.error("启动redis失败"+resId);
				return JsonResponse.failure(500, "启动redis失败");
			}
		}catch(Exception e){
			logger.error("启动redis失败"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/stopRedis")
	@ResponseBody
	public JsonResponse stopPassRedis(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String resId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(resId)||StringUtil.isNull(tenantId)||StringUtil.isNull(userId)){
				logger.error("停止Redis出错，resId/userId/tenantId参数为空");
				return JsonResponse.failure(500, "停止Redis失败,resId/userId/tenantId参数为空");
			}
			JSONObject res = redisService.stopPaasRedis(resId,userId, tenantId);
			if(res.containsKey("result") && res.getString("result").equals("success")){
				logger.debug("成功停止Redis--"+resId);
				return JsonResponse.success();
			}else{
				logger.error("停止Redis失败"+resId);
				return JsonResponse.failure(500, "停止Redis失败");
			}
		}catch(Exception e){
			logger.error("停止Redis失败"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getConfig")
	@ResponseBody
	public JsonResponse getMysqlConfig(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String resId = request.getParameter("resId");
			return JsonResponse.success(redisService.getPaasRedisConfig(resId));
		}catch(Exception e){
			logger.error("获取redis配置出错---getConfig--"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getInstances")
	@ResponseBody
	public JsonResponse getRedisInstance(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("start", 0);
			params.put("size", 999999);
			//params.put("userId", user.getUserId());
			params.put("byTenantId", "true");
			params.put("tenantId", user.getTenantId());
			StringBuilder sb = new StringBuilder();
			JSONObject mys = redisService.queryPaasRedisWithStatus(params,sb);
			if(logger.isDebugEnabled()){
				logger.debug("统计redis实例及运行状态"+sb.toString());
			}
			return JsonResponse.success(mys);
		}catch(Exception e){
			logger.error("统计redis实例及运行状态出错"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
}
