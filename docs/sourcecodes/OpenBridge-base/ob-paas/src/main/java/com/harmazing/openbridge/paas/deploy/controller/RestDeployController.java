package com.harmazing.openbridge.paas.deploy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.extension.Plugin;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.deploy.model.vo.MonitorForm;
import com.harmazing.openbridge.paas.env.controller.AbstractEnvController;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paas.plugin.xml.ResourceNode;

public class RestDeployController extends AbstractEnvController {
		
	private List<PaasProjectDeployEnv> getDeployResource(HttpServletRequest request,String envId,String envType){
		List<PaasProjectDeployEnv> dr = new ArrayList<PaasProjectDeployEnv>();
		//String logstashHost = ConfigUtil.getConfigString("logstash.host");
		//String logstashPort = ConfigUtil.getConfigString("logstash.port");
		String logstashHost = ConfigUtil.getConfigString("logstash.url");
		PaasProjectDeployEnv e7 = new PaasProjectDeployEnv();
		e7.setKey("LOGSTASH_IP");
		//e7.setValue("'" + logstashHost + ":" + logstashPort + "'");
		String logstash="";
		String[] sourceStrArray = logstashHost.split(";");
		for (int i = 0; i < sourceStrArray.length; i++) {
			logstash+="'"+sourceStrArray[i]+"'"+",";
		}
		e7.setValue( logstash.substring(0,logstash.length()-1));
		dr.add(e7);

		PaasProjectDeployEnv deployEnv = new PaasProjectDeployEnv();
		deployEnv.setKey("ENV_ID");
		deployEnv.setValue(envId);
		dr.add(deployEnv);

		PaasProjectDeployEnv envEnv = new PaasProjectDeployEnv();
		envEnv.setKey("ENV_TYPE");
		envEnv.setValue(envType);
		dr.add(envEnv);
		
		try{
			List<PaasProjectDeployEnv> r = getExtraEnvs(request,envId,envType);
			if (r != null && r.size() != 0) {
				dr.addAll(r);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		return dr;
	}

	
	/**
	 * envId,envType,serviceId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/envResource")
	@ResponseBody
	public JsonResponse envResource(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			
			String envId = request.getParameter("envId");
			String envType = request.getParameter("envType");
			DeployResource dr = new DeployResource();
			// logstash收集要用到的环境变量
			List<PaasProjectDeployEnv> r1 = getDeployResource(request,envId,envType);
			if(r1!=null && r1.size()!=0){
				dr.getEnvs().addAll(r1);
			}
			Map<String,Object> result = new HashMap<String,Object>();
			result.put("data", dr);
			return JsonResponse.success(result);
		} catch (Exception e) {
			logger.error("获取信息失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	/**
	 * 
	 * containerAdd:(跳到部署新增页面). <br/>
	 *
	 * @author dengxq
	 * @param request
	 * @param response
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping("/add")
	public String add(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			PaasEnv env = getEnvById(request);
//			DeployResource dr = getDeployResouceByEnvId(request);

			// logstash收集要用到的环境变量
//			List<PaasProjectDeployEnv> r1 = getDeployResource(request,env.getEnvId(),env.getEnvType());
//			if(r1!=null && r1.size()!=0){
//				dr.getEnvs().addAll(r1);
//			}
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			String businessId = getBusinessId(request);
			String businessType = getBusinessType();
			osPath = osPath
					+ "project/deploy/getDeployResouceAllByEnvId.do?businessId="
					+ businessId+"&businessType="+businessType+"&envId="+envId;
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			String drs = PaasAPIUtil.get(user.getUserId(), osPath);
			if (logger.isDebugEnabled()) {
				logger.debug(drs);
			}
			JSONObject jo = JSONObject.parseObject(drs);
			int code = jo.getIntValue("code");
			if (code != 0) {
				throw new RuntimeException("从os中返回的数据为" + code);
			}
			JSONObject d = jo.getJSONObject("data");
			DeployResource dr = JSONObject.toJavaObject(d, DeployResource.class);
			request.setAttribute("dr", dr);
			
			setParam2Request(request);
			return getUrlPrefix() + "/add";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}

	}

	private void setParam2Request(HttpServletRequest request) throws Exception {
		Map<String, String> envTypes = new HashMap<String, String>();
		String paasEnvType = ConfigUtil.getConfigString("paasos.evntype");
		if (StringUtil.isNotNull(paasEnvType)) {
			String[] envTypeArray = StringUtil.split(paasEnvType);
			for (String envType : envTypeArray) {
				String[] pairs = StringUtil.split(envType, "|");
				if (pairs != null && pairs.length == 2) {
					envTypes.put(pairs[0], pairs[1]);
				}
			}
		}
		request.setAttribute("envTypes", envTypes);
		request.setAttribute("envType", "test");

		String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if (!osPath.endsWith("/")) {
			osPath = osPath + "/";
		}
		IUser user = WebUtil.getUserByRequest(request);

		String businessId = request.getParameter("serviceId");
		String businessType = request.getParameter("businessType");
		if(!StringUtils.hasText(businessType)){
			businessType = getBusinessType();
		}
		// 镜像获取
		osPath = osPath
				+ "project/build/getBuildImageByBusinessId.do?businessId="
				+ businessId+"&businessType="+businessType;
		if (logger.isDebugEnabled()) {
			logger.debug(osPath);
		}
		String imageVersion = PaasAPIUtil.get(user.getUserId(), osPath);
		if (logger.isDebugEnabled()) {
			logger.debug(imageVersion);
		}
		JSONObject jo = JSONObject.parseObject(imageVersion);
		int code = jo.getIntValue("code");
		if (code != 0) {
			throw new RuntimeException("从os中返回的数据为" + code);
		}
		TreeMap<String, List<Map<String, String>>> images = jo.parseObject(
				jo.getString("data"), TreeMap.class);
		request.setAttribute("images", images);

		setExtraRequestParamter(request);
		// 资源

	}

	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse save(HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody PaasProjectDeploy deploy, @RequestParam String envId) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			// boolean isUpdate = true;
			// if(StringUtils.isEmpty(deploy.getDeployId())){
			// isUpdate = false;
			// }
			// PaasProject paasProject =
			// iPaasProjectService.getPaasProjectInfoById(deploy.getProjectId());
			if (StringUtils.isEmpty(deploy.getProjectType())) {
				// 默认给api如果没有设置的花
				deploy.setProjectType("api");
			}

			deploy.setEnvId(envId);
			JSONObject entity = (JSONObject) JSONObject.toJSON(deploy);

			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}

			// 镜像获取
			osPath = osPath + "project/deploy/save.do?businessId="+deploy.getBusinessId()
					+ "&businessType="+getBusinessType();

			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.JSON, entity.toJSONString());
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			// if(code == 501){
			// String error = jo.getString("msg");
			// throw new RuntimeException(msg);
			// }
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success();
		} catch (DuplicateKeyException e1) {
			return JsonResponse.failure(500, e1.getCause().getMessage());
		} catch (Exception e) {
			logger.error("保存发布失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/data")
	@ResponseBody
	public JsonResponse data(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			String extraKey = request.getParameter("extraKey");
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/data.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("pageNo", 1);
			param.put("pageSize", 99999);
			param.put("envId", envId);
			param.put("extraKey", extraKey);
			param.put("businessId", getBusinessId(request));
			param.put("businessType", getBusinessType());
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success(jo.getString("data"));
		} catch (DuplicateKeyException e1) {
			return JsonResponse.failure(500, e1.getCause().getMessage());
		} catch (Exception e) {
			logger.error("保存发布失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/deploy")
	@ResponseBody
	public JsonResponse deploy(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			String variableChangeTip = request.getParameter("variableChangeTip");
			String changeVariable = request.getParameter("changeVariable");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/deploy.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			if(StringUtils.hasText(variableChangeTip)){
				param.put("variableChangeTip", variableChangeTip);
			}
			if(StringUtils.hasText(changeVariable)){
				param.put("changeVariable", changeVariable);
			}
			
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if(code==-2){
				throw new RuntimeException("-2");
			}
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("发布失败", e);
			request.setAttribute("exception", e);
			if(StringUtils.hasText(e.getMessage()) && "-2".equals(e.getMessage())){
				return JsonResponse.failure(-2, e.getMessage());
			}
			else{
				return JsonResponse.failure(500, e.getMessage());
			}
		}
	}
	
	@RequestMapping("/redeploy")
	@ResponseBody
	public JsonResponse redeploy(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			String variableChangeTip = request.getParameter("variableChangeTip");
			String changeVariable = request.getParameter("changeVariable");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/redeploy.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			if(StringUtils.hasText(variableChangeTip)){
				param.put("variableChangeTip", variableChangeTip);
			}
			if(StringUtils.hasText(changeVariable)){
				param.put("changeVariable", changeVariable);
			}
			
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if(code==-2){
				throw new RuntimeException("-2");
			}
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("发布失败", e);
			request.setAttribute("exception", e);
			if(StringUtils.hasText(e.getMessage()) && "-2".equals(e.getMessage())){
				return JsonResponse.failure(-2, e.getMessage());
			}
			else{
				return JsonResponse.failure(500, e.getMessage());
			}
		}
	}

	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse delete(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/delete.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/stop")
	@ResponseBody
	public JsonResponse stop(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/stop.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/getPods")
	@ResponseBody
	public JsonResponse getPods(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/getPods.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success(jo.getString("data"));
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/deployInfo")
	@ResponseBody
	public JsonResponse deployInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");

			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/deployInfo.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success(jo.getString("data"));
		} catch (Exception e) {
			logger.error("获取deploy信息失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/saveReplicas")
	@ResponseBody
	public JsonResponse saveReplicas(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			String replicas = request.getParameter("replicas");

			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/saveReplicas.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			param.put("replicas", replicas);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success(jo.getString("data"));
		} catch (Exception e) {
			logger.error("部署扩容失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) {
		try {

			String deployId = request.getParameter("deployId");
			IUser user = WebUtil.getUserByRequest(request);

			String envId = request.getParameter("envId");
			PaasEnv env = getEnvById(request);
			request.setAttribute("env", env);
			setParam2Request(request);

			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			osPath = osPath + "project/deploy/deployInfo.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException("获取部署信息报错[" + msg + "]");
			}
			JSONObject data = jo.getJSONObject("data");
			PaasProjectDeploy pb = data.toJavaObject(data,
					PaasProjectDeploy.class);
			request.setAttribute("paasProjectDeploy", pb);
			return getUrlPrefix() + "/edit";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/view")
	public String view(HttpServletRequest request,
			@RequestParam String serviceId, @RequestParam String deployId) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			osPath = osPath + "project/deploy/dataInfo.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException("获取部署信息报错[" + msg + "]");
			}
			JSONObject data = jo.getJSONObject("data");
			PaasProjectDeploy pb = data.toJavaObject(
					data.getJSONObject("deploy"), PaasProjectDeploy.class);
			request.setAttribute("paasProjectDeploy", pb);

			Map<String, String> envTypes = new HashMap<String, String>();
			String paasEnvType = ConfigUtil.getConfigString("paasos.evntype");
			if (StringUtil.isNotNull(paasEnvType)) {
				String[] envTypeArray = StringUtil.split(paasEnvType);
				for (String envType : envTypeArray) {
					String[] pairs = StringUtil.split(envType, "|");
					if (pairs != null && pairs.length == 2) {
						envTypes.put(pairs[0], pairs[1]);
					}
				}
			}
			request.setAttribute("envTypes", envTypes);
			setParam2Request(request);
			
			String tab = request.getParameter("tab");
			request.setAttribute("tab", tab);
			
			return getUrlPrefix() + "/view";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/getPodLog")
	@ResponseBody
	public JsonResponse getPodLog(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String podName = request.getParameter("podName");
			String deployId = request.getParameter("deployId");
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/getPodLog.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("podName", podName);
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success(jo.getString("data"));
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/getPodEvent")
	@ResponseBody
	public JsonResponse getPodEvent(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String podName = request.getParameter("podName");
			String deployId = request.getParameter("deployId");

			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/getPodEvent.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("podName", podName);
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success(jo.getString("data"));
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	/**
	 * API APP PAAOS 都在用
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/version")
	@ResponseBody
	public JsonResponse version(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> r = new HashMap<String, Object>();
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String versionId = request.getParameter("versionId");

			List<PaasProjectDeployEnv> des = new ArrayList<PaasProjectDeployEnv>();
			r.put("env", des);

			PaasProjectDeployEnv v4 = new PaasProjectDeployEnv();
			v4.setKey("VERSION_ID");
			v4.setValue(versionId);
			v4.setResourceId("version_"+versionId);
			des.add(v4);
			//
			List<PaasProjectDeployEnv> extrEnvs = getExtraVersionEnvs(request);
			if (extrEnvs != null && extrEnvs.size() > 0) {
				des.addAll(extrEnvs);
			}

			return JsonResponse.success(r);
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	public String getFrameFromRequest(HttpServletRequest request)
			throws Exception {
		throw new RuntimeException("这个方法需要继承");
	}

	public List<PaasProjectDeployEnv> getExtraEnvs(HttpServletRequest request,String envId,String envType) throws Exception {
		return null;
	}

	public List<PaasProjectDeployEnv> getExtraVersionEnvs(
			HttpServletRequest request) throws Exception {
		return null;
	}

	public void setExtraRequestParamter(HttpServletRequest request)
			throws Exception {

	}
	
	/**
	 * 判断是否需要更新环境变量
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/changeEnvs")
	@ResponseBody
	public JsonResponse changeEnvs(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> r = new HashMap<String, Object>();
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String projectId = request.getParameter("projectId");
			String deployId = request.getParameter("deployId");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/changeEnvs.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("projectId", projectId);
			param.put("deployId", deployId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			return JsonResponse.success(jo.getString("data"));
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/reloadEnv")
	@ResponseBody
	public JsonResponse reloadEnv(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> r = new HashMap<String, Object>();
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String deployId = request.getParameter("deployId");
			String versionId = request.getParameter("versionId");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/reloadEnv.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployId", deployId);
			param.put("versionId", versionId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			DeployResource dr = JSONObject.parseObject(jo.getString("data"), DeployResource.class);
			return JsonResponse.success(dr);
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	public String getBusinessType(){
		return null;
	}
	
	public String getBusinessId(HttpServletRequest request){
		return null;
	}
	
	@RequestMapping("/history")
	@ResponseBody
	public JsonResponse history(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> r = new HashMap<String, Object>();
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String key = request.getParameter("key");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/log/history.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("key", key);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
//			DeployResource dr = JSONObject.parseObject(jo.getString("data"), PaasProjectLog.class);
			return JsonResponse.success(jo.get("data"));
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/info")
	@ResponseBody
	public JsonResponse info(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> r = new HashMap<String, Object>();
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String logId = request.getParameter("logId");
			String first = request.getParameter("first");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/log/info.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("logId", logId);
			param.put("first", first);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
//			DeployResource dr = JSONObject.parseObject(jo.getString("data"), DeployResource.class);
			return JsonResponse.success(jo.get("data"));
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	
	@RequestMapping("/monitor/monitorPreData")
	@ResponseBody
	public JsonResponse monitorPreData(HttpServletRequest request,
			HttpServletResponse response) {
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String key = request.getParameter("key");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/monitor/monitorPreData.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("key", key);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			return JsonResponse.success(jo.get("data"));
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/monitor/data")
	@ResponseBody
	public Map<String,Object> data(HttpServletRequest request,
			HttpServletResponse response,MonitorForm form) {
		IUser user = WebUtil.getUserByRequest(request);
		try {
//			String key = request.getParameter("key");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/monitor/dataForApiApp.do?businessType="+getBusinessType()+"&businessId="+getBusinessId(request);
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			
			JSONObject entity = (JSONObject) JSONObject.toJSON(form);
			
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,DataType.JSON, entity.toJSONString());
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			Map<String,Object> m = JSONObject.parseObject(msg, Map.class);
//			JSONObject jo = JSONObject.parseObject(msg);
			if ((!m.containsKey("code")) ||  !"0".equals(m.get("code")+"")) {
				String error = m.get("msg")+"";
				throw new RuntimeException(error);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			return m;
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
//			request.setAttribute("exception", e);
//			return JsonResponse.failure(500, e.getMessage());
			Map<String,Object> r = new HashMap<String,Object>();
			r.put("code", "-1");
			r.put("msg", "获取数据失败");
			return r;
		}
	}
	
	@RequestMapping("/status")
	@ResponseBody
	public JsonResponse status(HttpServletRequest request,
			HttpServletResponse response) {
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String deployIds = request.getParameter("deployIds");
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/status.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("deployIds", deployIds);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,DataType.FORM, param);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			return JsonResponse.success(jo.get("data"));
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/{envId}/deploy/ports")
	@ResponseBody
	public JsonResponse deployPorts(@PathVariable String envId,HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			
			String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			// 镜像获取
			osPath = osPath + "project/deploy/"+envId+"/deploy/ports.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("envId", envId);
			param.put("businessType", getBusinessType());
			param.put("businessId", getBusinessId(request));
			logger.debug(osPath);
			String msg = PaasAPIUtil.post(user.getUserId(), osPath,
					DataType.FORM, param);
			logger.debug(msg);
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				String error = jo.getString("msg");
				throw new RuntimeException(error);
			}
			Map<String,Object> r = new HashMap<String,Object>();
			JSONObject data = jo.getJSONObject("data");
			JSONArray array = data.getJSONArray("ports");
			if(array!=null && array.size()!=0){
				List<PaasProjectDeployPort> ps = JSONArray.parseArray(array.toJSONString(), PaasProjectDeployPort.class);
				r.put("ports", ps);
			}
			return JsonResponse.success(r);
		} catch (Exception e) {
			logger.error("获取端口失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
