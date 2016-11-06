package com.harmazing.openbridge.paasos.container.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.bootstrap.HttpServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;

@Controller
@RequestMapping("/container")
public class PaasContainerController {
	
	
	private Log logger = LogFactory.getLog(PaasContainerController.class);
	
	@RequestMapping("/create")
	@ResponseBody
	public JsonResponse create(HttpServletRequest request){
		try{
			System.out.println(request);
//			System.out.pritnln(request.getHeaderNames();
			String podName = request.getParameter("podName");
			logger.debug("------------------>"+podName);
			System.out.println("------------------>"+podName);
			return JsonResponse.success();
		}
		catch(Exception e){
			logger.error("",e);
			return JsonResponse.failure(500, "");
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse delete(HttpServletRequest request){
		try{
			System.out.println(request);
			
			return JsonResponse.success();
		}
		catch(Exception e){
			logger.error("",e);
			return JsonResponse.failure(500, "");
		}
	}

}
