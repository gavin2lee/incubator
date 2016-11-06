package com.harmazing.openbridge.sys.config.controller;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.openbridge.sys.config.service.ISysConfigService;
import com.harmazing.openbridge.sys.config.vo.SysConfigVO;

@Controller
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController {
	@Autowired
	private ISysConfigService configService;

	@RequestMapping("/view")
	public String view(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("config", configService.getSysConfig());
		return getUrlPrefix() + "/view";
	}

	@RequestMapping("/integrate")
	public String integrate(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("config", configService.getSysConfig());
		return getUrlPrefix() + "/integrate";
	}

	@RequestMapping("/codemanage")
	public String codemanage(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("config", configService.getSysConfig());
		return getUrlPrefix() + "/codemanage";
	}

	@RequestMapping("/monitor")
	public String monitor(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("config", configService.getSysConfig());
		return getUrlPrefix() + "/monitor";
	}

	@RequestMapping("/save")
	public String save(HttpServletRequest request,
			HttpServletResponse response, SysConfigVO vo) {
		configService.saveSysConfig(vo.getConfig());
		return redirect(request, getUrlPrefix() + "/view.do");
	}

	@RequestMapping("/integratesave")
	public String integratesave(HttpServletRequest request,
			HttpServletResponse response, SysConfigVO vo) {
		configService.saveSysConfig(vo.getConfig());
		return redirect(request, getUrlPrefix() + "/integrate.do");
	}

	@RequestMapping("/codemanagesave")
	public String codemanagesave(HttpServletRequest request,
			HttpServletResponse response, SysConfigVO vo) {
		configService.saveSysConfig(vo.getConfig());
		return redirect(request, getUrlPrefix() + "/codemanage.do");
	}

	@RequestMapping("/monitorsave")
	public String monitorsave(HttpServletRequest request,
			HttpServletResponse response, SysConfigVO vo) {
		configService.saveSysConfig(vo.getConfig());
		return redirect(request, getUrlPrefix() + "/monitor.do");
	}

	@RequestMapping("/setSystemClipboard")
	@ResponseBody
	public void setSystemClipboard(HttpServletRequest request) {
		String content = request.getParameter("content");
		StringSelection stsel = new StringSelection(content);
		Toolkit.getDefaultToolkit().getSystemClipboard()
				.setContents(stsel, stsel);
	}
	
	@RequestMapping("/find")
	@ResponseBody
	public JsonResponse find(){
		try{
			Map<String, String> sysConfig = configService.getSysConfig();
			return JsonResponse.success(sysConfig);
		}
		catch(Exception e){
			logger.debug(e);
			return JsonResponse.failure(500, "获取配置信息失败");
		}
	}

}
