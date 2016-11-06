package com.harmazing.openbridge.paasos.resource.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.WebUtil;

@Controller
@RequestMapping("/resource/nas/nfs")
public class PaasNASNFSController extends PaasNASController {
	private static final Log logger = LogFactory
			.getLog(PaasMysqlController.class);

	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		return list(request, response, "NFS");
	}

	// 实时rest请求查询NFS实例的运行状态
	@RequestMapping("/queryNasStatusInfo")
	@ResponseBody
	public JsonResponse queryNasInfo(HttpServletRequest request,
			HttpServletResponse response) {
		return super.queryNasInfo(request, response);
	}

	// 实时rest请求查询NFS实例的授权
	@RequestMapping("/queryNasAccessIps")
	@ResponseBody
	public JsonResponse queryNasAccessIPs(HttpServletRequest request,
			HttpServletResponse response) {
		return super.queryNasInfo(request, response);
	}

	// 保存网络存储实例的操作
	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse addNasSave(HttpServletRequest request,
			HttpServletResponse response) {
		return super.addNasSave(request, response);
	}

	// 删除网络存储的操作
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse deleteNas(HttpServletRequest request,
			HttpServletResponse response) {
		return super.deleteNas(request, response);
	}

	// 除网络存储的详情页面
	@RequestMapping("/info")
	public String nasInfo(HttpServletRequest request,
			HttpServletResponse response) {
		return super.nasInfo(request, response);
	}

	@RequestMapping("/GrantAccess")
	@ResponseBody
	public JsonResponse grantAccess2NFS(HttpServletRequest request,
			HttpServletResponse response) {
		return super.grantAccess2NFS(request, response);
	}

	@RequestMapping("/attach")
	@ResponseBody
	public JsonResponse attach2Volume(HttpServletRequest request,
			HttpServletResponse response) {
		return super.attach2Volume(request, response);
	}
	
	@RequestMapping("/instance")
	@ResponseBody
	public JsonResponse instanceOfNFS(HttpServletRequest request,
			HttpServletResponse response) {
		return super.nasInstance(request, response);
	}
	
	@RequestMapping("/getConfig")
	@ResponseBody
	public JsonResponse getNFSConfig(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String resId = request.getParameter("resId");
			return JsonResponse.success(nasService.getPaasNASConfig(resId));
		}catch(Exception e){
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
}
