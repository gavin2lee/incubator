package com.harmazing.openbridge.paasos.oslog.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.openbridge.paasos.oslog.model.PaasProjectLog;
import com.harmazing.openbridge.paasos.oslog.service.IPaasProjectLogService;



@Controller
@RequestMapping("/project/log")
public class PaasProjectLogController {
	
	@Autowired
	private IPaasProjectLogService iPaasProjectLogService;
	
	@RequestMapping("/history")
	@ResponseBody
	public JsonResponse history(HttpServletRequest request, HttpServletResponse response) {
		try {
			String key = request.getParameter("key");
			List<PaasProjectLog> result = iPaasProjectLogService.getLogHistory(key);
			Map<String,Object> r = new HashMap<String,Object>();
			r.put("data", result);
			return JsonResponse.success(r);
		} catch (Exception e) {
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/info")
	@ResponseBody
	public JsonResponse info(HttpServletRequest request, HttpServletResponse response) {
		try {
			String logId = request.getParameter("logId");
			String first = request.getParameter("first");
			boolean b = Boolean.valueOf(first);
			List<PaasProjectLog> result = iPaasProjectLogService.getLogHistoryInfo(logId,b);
			Map<String,Object> r = new HashMap<String,Object>();
			r.put("data", result);
			if(result!=null && result.size()>0){
				PaasProjectLog l = result.get(result.size()-1);
				r.put("latest", l.getId());
				r.put("isEnd", l.getBegin()!=null && l.getBegin().equals("2") ? true : false);
			}
			return JsonResponse.success(r);
		} catch (Exception e) {
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
}
