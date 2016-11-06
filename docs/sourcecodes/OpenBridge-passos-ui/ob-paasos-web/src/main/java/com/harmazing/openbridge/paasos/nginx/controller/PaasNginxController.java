package com.harmazing.openbridge.paasos.nginx.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;

@Controller
@RequestMapping("/paas/nginx")
public class PaasNginxController extends PaasNginxBaseController{
	
	@RequestMapping("/add")
	public String add(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setAttribute("action", "save"); 
			PaasHost host = new PaasHost();
			host.setHostType("nginx");
			host.setHostId(StringUtil.getUUID());
			request.setAttribute("sysHost", host);
			return getUrlPrefix() + "/edit";
		} catch (Exception e) {
			super.logger.error("HOST 新增出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse addHost(HttpServletRequest request, PaasHost host){
		host.setHostType("nginx");
		return super.addHost(host);
	}
	
	@RequestMapping("/edit")
	public String getHost(HttpServletRequest request, HttpServletResponse response){
		try{
			request.setAttribute("action", "update");
			PaasHost  host = super.getHostById(request);
			request.setAttribute("sysHost", host);
			return getUrlPrefix() + "/edit";
		}catch(Exception e){
			logger.error("HOST 编辑页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/update")
	@ResponseBody
	public JsonResponse updateHost(HttpServletRequest request, PaasHost host){
		return super.updateHost(host);
	}
	
	@RequestMapping("/host")
	public String listHost(HttpServletRequest request, HttpServletResponse response){
		try{
			Page<PaasHost>  hosts = super.listHost(request);
			request.setAttribute("pageData", hosts);
			return getUrlPrefix() + "/host";
		}catch(Exception e){
			logger.error("HOST列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse deleteHost(HttpServletRequest request){
		try{
			String hostId = request.getParameter("hostId");
			paasNginxService.deleteNginxHostById(hostId);
			return JsonResponse.success();
		}catch(Exception e){
			logger.error("删除Host出错",e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/page")
	public String listHostInstance(HttpServletRequest request, HttpServletResponse response){
		try{
			request.setAttribute("pageData", super.listHostInstance(request));
			return getUrlPrefix() + "/page";
		}catch(Exception e){
			logger.error("Nginx配置列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
}
