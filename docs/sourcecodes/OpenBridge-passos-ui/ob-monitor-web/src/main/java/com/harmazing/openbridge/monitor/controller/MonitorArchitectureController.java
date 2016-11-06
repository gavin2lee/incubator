package com.harmazing.openbridge.monitor.controller;


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

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.monitor.model.vo.GraphParam;
import com.harmazing.openbridge.monitor.model.vo.GraphVo;
import com.harmazing.openbridge.monitor.service.IMonitorArchitectureService;

@Controller
@RequestMapping("/monitor/architecture")
public class MonitorArchitectureController {
	
	private static Log logger = LogFactory
			.getLog(MonitorArchitectureController.class);
	
	@Autowired
	private IMonitorArchitectureService iArchitectureService;
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		return "/monitor/architecture/index";
	}
	
	
	@RequestMapping("/graph")
	@ResponseBody
	public JsonResponse graph(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String type = request.getParameter("business_type");
			List<GraphVo> graphByType = iArchitectureService.getGraphByType(type,user.getUserId());
			return JsonResponse.success(graphByType.get(0));
		}
		catch(Exception e){
			logger.error("获取图形数据报错", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/node/status")
	@ResponseBody
	public JsonResponse nodeStatus(HttpServletRequest request, HttpServletResponse response,GraphParam param){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			param.setUserId(user.getUserId());
			Map<String,Object> r = iArchitectureService.getAppAndNodeStatus(param);
			return JsonResponse.success(r);
		}
		catch(Exception e){
			logger.error("获取图形数据报错", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

}
