package com.harmazing.openbridge.paasos.imgbuild.log;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;

@Controller
@RequestMapping("/log/")
public class PaaSLogController {
	@RequestMapping("build")
	@ResponseBody
	public JsonResponse build(String buildId,Integer startLine){
		if(startLine==null){
			startLine = 0;
		}
		JsonResponse jsonRes = JsonResponse.success(LogManager.getCommandLog(buildId, startLine));
		return jsonRes;
	}
}
