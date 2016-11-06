package com.harmazing.openbridge.paasos.nginx.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;
import com.harmazing.openbridge.paas.nginx.model.PaasNginxConf;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSHostService;
import com.harmazing.openbridge.paasos.nginx.service.IPaasOSNginxService;

public class PaasNginxBaseController extends AbstractController{
	protected final Log logger = LogFactory.getLog(PaasNginxBaseController.class);
	
	@Autowired
	protected IPaasOSHostService paasHostService;
	
	@Autowired
	protected IPaasOSNginxService paasNginxService;

	public PaasHost getHostById(HttpServletRequest request){
		try{
			String hostId = request.getParameter("hostId");
			PaasHost host = paasHostService.getHostById(hostId);
			return host;
		}catch(Exception e){
			logger.error("get nginx host by id error:"+e.getMessage());
			throw e;
		}
	}
	
	public JsonResponse addHost(@RequestBody PaasHost host){
		try{
			int count=paasHostService.getcountHostByIp(host);
			if(count>0){
				throw new RuntimeException("服务器IP已存在");
			}
			int count1=paasHostService.getcountHostByName(host);
			if(count1>0){
				throw new RuntimeException("配置文件目录重复");
			}
			
			paasHostService.addHost(host);
			return JsonResponse.success(host);
		}catch(Exception e){
			logger.error("add nginx host error:"+e.getMessage());
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	public JsonResponse updateHost(PaasHost host){
		try{
			PaasHost hostold= paasHostService.getHostById(host.getHostId());
			if(!host.getHostIp().equals(hostold.getHostIp())){
			     int count=paasHostService.getcountHostByIp(host);
			         if(count>0){
				         logger.error("update nginx host error:服务器IP已存在");
						return JsonResponse.failure(500, "服务器IP已存在");
			         }
			}
			if(!host.getDirectoryName().equals(hostold.getDirectoryName())){
			      int count1=paasHostService.getcountHostByName(host);
			          if(count1>0){
				         logger.error("update nginx host error:配置文件目录重复");
						return JsonResponse.failure(500, "配置文件目录重复");
			          }
			 }
			paasHostService.updateHost(host);
			return JsonResponse.success(host);
		}catch(Exception e){
			logger.error("update nginx host error:"+e.getMessage());
			return JsonResponse.failure(500, "修改nginx代理主机信息失败");
		}
	}
	
	public Page<PaasHost> listHost(HttpServletRequest request){
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			String keyword = request.getParameter("keyword");
			if (StringUtil.isNotNull(keyword)) {
				params.put("keyword", keyword);
				request.setAttribute("keyword", keyword);
			}
			params.put("hosttype", "nginx");
			String platform= request.getParameter("platform");
			if(StringUtil.isNotNull(platform)){
				params.put("hostPlatform", platform);
			}
			params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
			params.put("pageSize", StringUtil.getIntParam(request, "pageSize", 10));

			return paasHostService.queryHostPage(params);
		}catch(Exception e){
			logger.error("list nginx host error:"+e.getMessage());
			throw e;
		}
	}
	
	
	/////////////////belows are nginx config information////////////////////////
	public Page<PaasNginxConf> listHostInstance(HttpServletRequest request){
		try{
			Map<String, Object> params = new HashMap<String, Object>();
			String keyword = request.getParameter("keyword");
			if (StringUtil.isNotNull(keyword)) {
				params.put("keyword", keyword);
			}
			params.put("hosttype", "nginx");
			String platform= request.getParameter("platform");
			if(StringUtil.isNotNull(platform)){
				params.put("hostPlatform", platform);
			}
			params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
			params.put("pageSize", StringUtil.getIntParam(request, "pageSize", 10));

			return paasNginxService.queryNginxPage(params);
		}catch(Exception e){
			logger.error("list nginx host error:"+e.getMessage());
			throw e;
		}
	}
	
}
