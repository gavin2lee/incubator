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
import com.harmazing.openbridge.paasos.resource.model.PaasRabbitMQ;
import com.harmazing.openbridge.paasos.resource.service.IPaasMQService;
import com.harmazing.openbridge.paasos.utils.ConfigUtils;

@Controller
@RequestMapping("/resource/messagequeue")
public class PaasRabbitMQController extends PaasResourceBaseController {
	protected static final Log logger = LogFactory
			.getLog(PaasRabbitMQController.class);
	
	@Autowired
	private IPaasMQService mqService;
	
	@RequestMapping("/list")//首页的列表
	public String listRabbitMQ(HttpServletRequest request, HttpServletResponse response){
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
			List<PaasRabbitMQ> pageData = mqService.queryPaasMQsByParams(params);
			request.setAttribute("pageData", pageData);
			return getUrlPrefix()+"/list";
		}catch(Exception e){
			request.setAttribute("exception", e);
			logger.error("获取队列列表信息出错");
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/queryMQStatusInfo")//实时rest请求查询mq实例的运行状态
	@ResponseBody
	public JsonResponse queryRabbitMQInfo(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String resId = request.getParameter("resId");
			String paasOsUserId = user.getUserId();
			String paasOsTenantId = user.getTenantId();
			if(StringUtil.isNull(resId)){
				return JsonResponse.failure(403, "查询参数不正确");
			}
			JSONObject RabbitMQObj = mqService.queryMQInfo(resId, paasOsUserId, paasOsTenantId);
			String status = RabbitMQObj.getString("status");
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
			if(RabbitMQObj.containsKey("podInfo")){
				if(RabbitMQObj.getJSONObject("podInfo")!=null){
					returnObj.put("podName", RabbitMQObj.getJSONObject("podInfo").get("podName"));
					returnObj.put("namespace", RabbitMQObj.getJSONObject("podInfo").get("namespace"));
				}
			}
			return JsonResponse.success(returnObj);
		}catch(Exception e){
			logger.error("查询消息队列运行状态失败"+e.getMessage());
			return JsonResponse.failure(500, "查询消息队列运行状态失败");
		}
	}
	
	@Override
	protected void setResourceLimit2Request(HttpServletRequest request){
		String userId = (String) request.getAttribute("userId");
		String tenantId = (String)request.getAttribute("tenantId");
		JSONObject RabbitMQOption = mqService.getPaasRabbitMQOptions(userId, tenantId);
		if(RabbitMQOption!=null){
			request.setAttribute("options", RabbitMQOption);
		}
	}
	
	@RequestMapping("/options")//实时rest请求查询rabbitMQ配额信息
	@ResponseBody
	public JsonResponse queryRabbitMQOptions(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			JSONObject RabbitMQOption = mqService.getPaasRabbitMQOptions(userId, tenantId);
			return JsonResponse.success(RabbitMQOption);
		}catch(Exception e){
			logger.error("查询消息队列配额信息失败"+e.getMessage());
			return JsonResponse.failure(500, "查询消息队列配额信息失败");
		}
	}
	
	@RequestMapping("/save")//保存消息队列实例的操作
	@ResponseBody
	public JsonResponse addRabbitMQSave(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String instanceName = request.getParameter("instanceName");
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
			if(StringUtil.isNull(instanceName)|| StringUtil.isNull(tenantId)){
				return JsonResponse.failure(400, "新建消息队列资源失败,实例名和租户不能为空");
			}
			PaasRabbitMQ mq = new PaasRabbitMQ();
			
			JSONObject RabbitMQConfig = new JSONObject();
			RabbitMQConfig.put("name", instanceName);
			RabbitMQConfig.put("memory", memory);
			
			mq.setMemory(StorageUtil.getMemory(memory));
			
			RabbitMQConfig.put("version", version);
			JSONObject attributes = new JSONObject();
			attributes.put("envId", envId);
			attributes.put("projectId", projectId);
			attributes.put("envType", envType);
			RabbitMQConfig.put("attributes", attributes);
			
			mq.setMqId(StringUtil.getUUID());
			mq.setMqName(instanceName);
			mq.setApplyContent(RabbitMQConfig.toJSONString());
			mq.setCreater(userId);
			Date now = new Date();
			mq.setCreateDate(now);
			mq.setUpdateDate(now);
			mq.setAllocateContent("");
			mq.setTenantId(tenantId);
			mq.setProjectId(projectId);
			mq.setEnvId(envId);
			mq.setEnvType(envType);
			mq.setOnReady(false);
			mq.setResDesc(desc);
			mq.setApplyType(applyType);
			mqService.addPaasMQResource(mq);
			logger.debug("成功添加消息队列实例，"+instanceName);
			return JsonResponse.success(mq.getMqId());
		}catch(Exception e){
			logger.error("添加消息队列实例失败"+e.getMessage());
			return JsonResponse.failure(500, "新建消息队列资源失败"+e.getMessage());
		}
	}
	
	@RequestMapping("/delete")//删除消息队列的操作
	@ResponseBody
	public JsonResponse deleteRabbitMQ(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String mqId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(mqId)||StringUtil.isNull(tenantId)||StringUtil.isNull(userId)){
				logger.error("删除消息队列资源出错，resId参数为空");
				return JsonResponse.failure(500, "删除消息队列资源失败,resId参数为空");
			}
			JSONObject res = mqService.deletePaasMQById(mqId,userId, tenantId);
			if(res.containsKey("result")&&res.getString("result").equals("success")){
				logger.debug("删除消息队列资源"+mqId);
				return JsonResponse.success(mqId);
			}else{
				logger.error("删除消息队列资源操作失败"+mqId);
				return JsonResponse.failure(500, "删除消息队列资源失败");
			}
		}catch(Exception e){
			logger.error("删除消息队列资源操作失败"+e.getMessage());
			return JsonResponse.failure(500, "删除消息队列资源失败"+e.getMessage());
		}
	}
	
	@RequestMapping("/info")//查看消息队列的基本信息页面
	public String infoRabbitMQ(HttpServletRequest request, HttpServletResponse response){
		String mqId = request.getParameter("mqId");
		PaasRabbitMQ mqInfo = mqService.getPaasMQInfoById(mqId);
		request.setAttribute("mqInfo", mqInfo);
		ConfigUtils.setEnvType2Request(request);
		return getUrlPrefix()+"/info";
	}
		
	@RequestMapping("/startRabbitMQ")//启用消息队列
	@ResponseBody
	public JsonResponse startRabbitMQ(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String resId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(resId)||StringUtil.isNull(tenantId)||StringUtil.isNull(userId)){
				logger.error("启动消息队列出错，resId参数为空");
				return JsonResponse.failure(500, "启动消息队列失败,resId参数为空");
			}
			JSONObject res = mqService.startPaasMQ(resId,userId, tenantId);
			if(res.containsKey("result")&& res.getString("result").equals("success")){
				logger.debug("成功启动消息队列--"+resId);
				return JsonResponse.success();
			}else{
				logger.error("启动消息队列失败"+resId);
				return JsonResponse.failure(500, "启动消息队列失败");
			}
			
		}catch(Exception e){
			logger.error("启动消息队列失败"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/stopRabbitMQ")//停止消息队列
	@ResponseBody
	public JsonResponse stopRabbitMQ(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String resId = request.getParameter("resId");
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if(StringUtil.isNull(resId)||StringUtil.isNull(tenantId)||StringUtil.isNull(userId)){
				logger.error("停止消息队列出错，resId参数为空");
				return JsonResponse.failure(500, "停止消息队列失败,resId参数为空");
			}
			JSONObject res = mqService.stopPaasMQ(resId,userId, tenantId);
			if(res.containsKey("result")&& res.getString("result").equals("success")){
				logger.debug("成功停止消息队列--"+resId);
				return JsonResponse.success();
			}else{
				logger.error("停止消息队列失败"+resId);
				return JsonResponse.failure(500, "停止消息队列失败");
			}
			
		}catch(Exception e){
			logger.error("停止消息队列失败"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getConfig")
	@ResponseBody
	public JsonResponse getRabbitMQConfig(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String resId = request.getParameter("resId");
			return JsonResponse.success(mqService.getPaasMQConfig(resId));
		}catch(Exception e){
			logger.error("获取消息队列配置出错---getConfig--"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getInstances")
	@ResponseBody
	public JsonResponse getRabbitMQInstance(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("start", 0);
			params.put("size", 999999);
			//params.put("userId", user.getUserId());
			params.put("byTenantId", "true");
			params.put("tenantId", user.getTenantId());
			StringBuilder sb = new StringBuilder();
			JSONObject mys = mqService.queryPaasRabbitMQWithStatus(params,sb);
			if(logger.isDebugEnabled()){
				logger.debug("统计消息队列实例及运行状态"+sb.toString());
			}
			return JsonResponse.success(mys);
		}catch(Exception e){
			logger.error("统计消息队列实例及运行状态出错"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
}
