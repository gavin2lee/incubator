package com.harmazing.openbridge.paasos.project.controller;

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
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paas.nginx.vo.NginxConfVo;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSNginxService;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
@Controller
@RequestMapping("/project/env/loadbalance")
public class PaasProjectEnvLoadbalanceController extends ProjectBaseController {
	@Autowired
	private IPaasProjectEnvService envService;
	@Autowired
	private IPaasOSNginxService paasNginxService;
	
	private PaasEnv loadProjectEnv(HttpServletRequest request){
		String envId = request.getParameter("envId");
		PaasEnv env = envService.getEnvById(envId);
		request.setAttribute("env", env);
		return env;
	}

	@RequestMapping("/add")
	public String loadbalanceAdd(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PaasProject project = loadProjectModel(request);
			loadProjectEnv(request);
			return getUrlPrefix() + "/add";
		} catch (Exception e) {
			logger.error("创建负载均衡代理页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/edit")
	public String loadbalanceEdit(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			loadProjectEnv(request);
			String loadbalanceId = request.getParameter("loadbalanceId");
			NginxConfVo loadbalance = paasNginxService.findConfVoByConfId(loadbalanceId);
			request.setAttribute("loadbalance", loadbalance);
			return getUrlPrefix() + "/edit";
		} catch (Exception e) {
			logger.error("修改负载均衡代理页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/del")
	@ResponseBody
	public JsonResponse loadbalanceDel(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			loadProjectEnv(request);
			String loadbalanceId = request.getParameter("loadbalanceId");
			paasNginxService.deleteNginxConf(loadbalanceId);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("删除负载均衡代理页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse loadbalanceSave(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			PaasEnv env = loadProjectEnv(request);
			String envId = request.getParameter("envId");
			String nginxConfigName = request.getParameter("nginxConfigName");
			String domain = request.getParameter("domain");
			String synDeployIP = request.getParameter("synDeployIP");
			
			String[] slaveDomain = request.getParameterValues("slaveDomain"); 
			
			StringBuilder sb = new StringBuilder();
			Boolean isSupportSSL = false;
			try {
				isSupportSSL = Boolean.parseBoolean(request
						.getParameter("isSupportSSL"));
			} catch (Exception e) {
				isSupportSSL = false;
			}

			String sslCrtId = request.getParameter("sslCrtId");
			String sslKeyId = request.getParameter("sslKeyId");
			PaasNginxConf conf = new PaasNginxConf();
			conf.setConfId(StringUtil.getUUID());

			conf.setEnvId(env.getEnvId());
			conf.setBusinessType(project.getProjectType());
			conf.setEnvType(env.getEnvType());
			conf.setServiceId(project.getBusinessId());
			conf.setVersionId("");
			conf.setSkipAuth(true);
			conf.setIsSupportSsl(isSupportSSL);
			conf.setSslCrtId(sslCrtId);
			conf.setSslKeyId(sslKeyId);
			conf.setNginxName(nginxConfigName);

			JSONObject confContent = new JSONObject();
			confContent.put("domain", domain);
			confContent.put("synDeployIP", synDeployIP);
			
			if(slaveDomain!=null && slaveDomain.length>0){
				JSONArray ja = new JSONArray();
				for(String sd : slaveDomain){
					ja.add(sd);
				}
				confContent.put("slaveDomain", ja);
			}
			
			JSONArray service = new JSONArray();
			String[] ips = request.getParameterValues("ip");
			String[] ports = request.getParameterValues("port");
			String[] weights = request.getParameterValues("weight");

			for (int i = 0; i < ips.length; i++) {
				JSONObject ss = new JSONObject();
				ss.put("ip", ips[i]);
				ss.put("port", ports[i]);
				ss.put("weight", weights[i]);
				service.add(ss);
				if (ips[i].equals("")) {
					return JsonResponse.failure(1, "代理IP存在空值");
				}
			}
			confContent.put("server", service);
			conf.setConfContent(confContent.toJSONString());
			paasNginxService.createNginxConf(conf, project.getProjectType(), sb);
			request.setAttribute("env", env);
			return JsonResponse.success();
		} catch (NullPointerException e2) {
			logger.error("创建负载均衡代理页面出错", e2);
			request.setAttribute("exception", e2);
			return JsonResponse.failure(2, "代理记录项不能为空");
		} catch (RuntimeException e1) {
			logger.error("创建负载均衡代理页面出错", e1);
			request.setAttribute("exception", e1);
			return JsonResponse.failure(3, "sslCrtId为空");
		} catch (Exception e) {
			logger.error("创建负载均衡代理页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}

	}

	@RequestMapping("/update")
	@ResponseBody
	public JsonResponse loadbalanceUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject project = loadProjectModel(request);
			PaasEnv env = loadProjectEnv(request);
			String envId = request.getParameter("envId");
			String confId = request.getParameter("confId");
			String nginxConfigName = request.getParameter("nginxConfigName");
			String domain = request.getParameter("domain");
			String versionId = request.getParameter("versionId");
			String versionCode = request.getParameter("versionCode");
			String synDeployIP = request.getParameter("synDeployIP");
			
			String[] slaveDomain = request.getParameterValues("slaveDomain"); 
			
			StringBuilder sb = new StringBuilder();
			PaasNginxConf conf = paasNginxService.findConfByConfId(confId);
			if (conf == null) {
				JsonResponse.failure(1, "confId 错误");
			}
			/*
			 * String sslCrtId = request.getParameter("sslCrtId"); String
			 * sslKeyId = request.getParameter("sslKeyId");
			 */
			String sslCrtId = conf.getSslCrtId();
			String sslKeyId = conf.getSslKeyId();
			Boolean isSupportSSL = false;
			try {
				isSupportSSL = Boolean.parseBoolean(request
						.getParameter("isSupportSSL"));
			} catch (Exception e) {
				isSupportSSL = false;
			}
			Boolean domainCross = false;
			try {
				domainCross = Boolean.parseBoolean(request
						.getParameter("domainCross"));
			} catch (Exception e) {
				domainCross = false;
			}
			conf.setIsSupportSsl(isSupportSSL);
			conf.setSslCrtId(sslCrtId);
			conf.setSslKeyId(sslKeyId);
			conf.setNginxName(nginxConfigName);
			conf.setVersionId(versionId);
			JSONObject confContent = new JSONObject();
			confContent.put("domain", domain);
			confContent.put("synDeployIP", synDeployIP);
			
			if(slaveDomain!=null && slaveDomain.length>0){
				JSONArray ja = new JSONArray();
				for(String sd : slaveDomain){
					ja.add(sd);
				}
				confContent.put("slaveDomain", ja);
			}
			
			if("api".equals(project.getProjectType())){
				confContent.put("domainCross", domainCross);
			}
			confContent.put("versionCode", versionCode);
			JSONArray service = new JSONArray();
			String[] ips = request.getParameterValues("ip");
			String[] ports = request.getParameterValues("port");
			String[] weights = request.getParameterValues("weight");
			for (int i = 0; i < ips.length; i++) {
				JSONObject ss = new JSONObject();
				ss.put("ip", ips[i]);
				ss.put("port", ports[i]);
				ss.put("weight", weights[i]);
				service.add(ss);
			}

			confContent.put("server", service);
			conf.setConfContent(confContent.toJSONString());
			paasNginxService.updateNginxConf(conf, sb);
			request.setAttribute("env", env);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("更新负载均衡代理页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

}
