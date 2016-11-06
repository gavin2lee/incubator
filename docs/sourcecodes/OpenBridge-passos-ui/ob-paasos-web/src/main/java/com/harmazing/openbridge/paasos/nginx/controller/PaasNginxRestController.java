package com.harmazing.openbridge.paasos.nginx.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paas.nginx.vo.NginxConfVo;

@Controller
@RequestMapping("/paas/nginx/rest")
public class PaasNginxRestController extends PaasNginxBaseController{
	
	@RequestMapping("/host/get")
	@ResponseBody
	public JsonResponse getHost(HttpServletRequest request, HttpServletResponse response){
		try{
			PaasHost  host = super.getHostById(request);
			return JsonResponse.success(host);
		}catch(Exception e){
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/host/add")
	@ResponseBody
	public JsonResponse addHost(HttpServletRequest request, @RequestBody PaasHost host){
		return super.addHost(host);
	}
	
	@RequestMapping("/host/update")
	@ResponseBody
	public JsonResponse updateHost(HttpServletRequest request, @RequestBody PaasHost host){
		return super.updateHost(host);
	}
	
	@RequestMapping("/host/list")
	@ResponseBody
	public JsonResponse listHost(HttpServletRequest request, HttpServletResponse response){
		try{
			Page<PaasHost>  hosts = super.listHost(request);
			return JsonResponse.success(hosts);
		}catch(Exception e){
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	
	/////////////////belows are nginx config information////////////////////////
	@RequestMapping("/instances")
	@ResponseBody
	public JsonResponse listHostInstance(HttpServletRequest request, HttpServletResponse response){
		try{
			return JsonResponse.success(super.listHostInstance(request));
		}catch(Exception e){
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public JsonResponse createNginxConf(HttpServletRequest request, @RequestBody PaasNginxConf config){
		try{
			String platform = request.getParameter("platform");
			StringBuilder sb = new StringBuilder();
			paasNginxService.createNginxConf(config, platform, sb);
			return JsonResponse.success(sb.toString());
		}catch(Exception e){
			logger.error("create nginx config error:"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse deleteNginxConf(HttpServletRequest request){
		try{
			String confId = request.getParameter("confId");
			paasNginxService.deleteNginxConf(confId);
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("delete nginx config error:"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public JsonResponse updateNginxConf(HttpServletRequest request, @RequestBody PaasNginxConf config){
		try{
			StringBuilder sb = new StringBuilder();
			paasNginxService.updateNginxConf(config, sb);
			return JsonResponse.success(sb.toString());
		}catch(Exception e){
			logger.error("update nginx config error:"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/get")
	@ResponseBody
	public JsonResponse getNginxConfById(HttpServletRequest request){
		try{
			String confId = request.getParameter("confId");
			PaasNginxConf nginxConfig  = paasNginxService.findConfByConfId(confId);
			return JsonResponse.success(nginxConfig);
		}catch(Exception e){
			logger.error("find nginx config by id error:"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getvo")
	@ResponseBody
	public JsonResponse getNginxConfVoById(HttpServletRequest request){
		try{
			String confId = request.getParameter("confId");
			NginxConfVo nginxConfig  = paasNginxService.findConfVoByConfId(confId);
			return JsonResponse.success(nginxConfig);
		}catch(Exception e){
			logger.error("find nginx configVo by id error:"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/vo/list")
	@ResponseBody
	public JsonResponse getNginxConfList(HttpServletRequest request, HttpServletResponse response){
		try{
			String serviceId = request.getParameter("serviceId");
			String envType = request.getParameter("envType");
			String envId = request.getParameter("envId");
			
			//DENGXQ APP API获取依赖的api的url环境变量
			String versionIds = request.getParameter("versionIds");
			if(StringUtil.isNotNull(versionIds)){
				String envMark = request.getParameter("envMark");
				if(StringUtils.isEmpty(envMark)){
					envMark = null;
				}
				String[] s = versionIds.split(",");
				return JsonResponse.success(paasNginxService.getNginxListByVersionIds(Arrays.asList(s),envType,envMark));
			}
			
			if(StringUtil.isNotNull(envId)){
				return JsonResponse.success(paasNginxService.getNginxListByEnvId(envId));
			}else{
				return JsonResponse.success(paasNginxService.findNginxConf(serviceId, envType));
			}
		}catch(Exception e){
			logger.error("find nginx configVo list error:"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
