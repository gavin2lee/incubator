package com.harmazing.openbridge.paasos.resource.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;

@Controller
@RequestMapping("/resource/nas/iscsi")
public class PaasNASISCSIController extends PaasNASController {
	private static final Log logger = LogFactory
			.getLog(PaasNASISCSIController.class);

	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		return list(request, response, "ISCSI");
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
	
	@RequestMapping("/instance")
	@ResponseBody
	public JsonResponse instanceOfNFS(HttpServletRequest request,
			HttpServletResponse response) {
		return super.nasInstance(request, response);
	}
}