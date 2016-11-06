package com.harmazing.openbridge.paas.nginx.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.nginx.model.PaasHost;
import com.harmazing.openbridge.paas.nginx.service.IPaasHostService;
import com.harmazing.openbridge.paas.nginx.service.IPaasNginxService;

public class PaasNginxController extends AbstractController {
	private static final Log logger = LogFactory
			.getLog(PaasNginxController.class);

	@Autowired
	private IPaasNginxService nginxService;

	@Autowired
	private IPaasHostService hostService;
	
	public String view(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String hostId = request.getParameter("hostId");
			request.setAttribute("action", "update");
			PaasHost host = hostService.getHostById(hostId,user.getUserId());
			request.setAttribute("sysHost", host);
			return getUrlPrefix() + "/edit";
		} catch (Exception e) {
			logger.error("HOST 编辑页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	public String add(HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setAttribute("action", "save"); 
			PaasHost host = new PaasHost();
			host.setHostType(request.getParameter("hostType"));
			host.setHostId(StringUtil.getUUID());
			request.setAttribute("sysHost", host);
			return getUrlPrefix() + "/edit";
		} catch (Exception e) {
			logger.error("HOST 新增出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	
	public Map<String,Object> save(HttpServletRequest request,
			PaasHost host, String platform) {
		//用于把host中参数空格去除,有业务需要的才去掉
		String backupHost = host.getBackupHost();
		if(StringUtil.isNotNull(backupHost)){
			backupHost = backupHost.trim();
			host.setBackupHost(backupHost);
		}
		String directoryName = host.getDirectoryName();
		if(StringUtil.isNotNull(directoryName)){
			directoryName = directoryName.trim();
			host.setDirectoryName(directoryName);
		}
		String hostIp = host.getHostIp();
		if(StringUtil.isNotNull(hostIp)){
			hostIp = hostIp.trim();
			host.setHostIp(hostIp);
		}
		String hostUser = host.getHostUser();
		if(StringUtil.isNotNull(hostUser)){
			hostUser = hostUser.trim();
			host.setHostUser(hostUser);
		}
		String virtualHost = host.getVirtualHost();
		if(StringUtil.isNotNull(virtualHost)){
			virtualHost = virtualHost.trim();
			host.setVirtualHost(virtualHost);
		}
		

		
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			IUser user = WebUtil.getUserByRequest(request);
			if (StringUtil.isNull(host.getHostId())) {
				host.setHostId(StringUtil.getUUID());
			}
			host.setHostPlatform(platform);
			hostService.addHost(host, user.getUserId());
			result.put("code", 0);
		}
		catch (Exception e) {
			logger.error("HOST 新增 出错", e);
			result.put("code", -1);
			result.put("info", e.getMessage());
		}
		return result;
	}

	public Map<String,Object> update(HttpServletRequest request,
			PaasHost host, String platform) {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			IUser user = WebUtil.getUserByRequest(request);
			if (StringUtil.isNull(host.getHostId())) {
				throw new Exception("参数错误");
			}
			host.setHostPlatform(platform);
			hostService.updateHost(host, user.getUserId());
			result.put("code", 0);
		} 
		catch (Exception e) {
			logger.error("HOST 更新 出错", e);
			request.setAttribute("exception", e);
			result.put("code", -1);
			result.put("info",  e.getMessage());
		}
		return result;
	}

	
	public String host(HttpServletRequest request, String platform) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			String keyword = request.getParameter("keyword");
			if (keyword != null && !keyword.trim().equals("")) {
				params.put("keyword", keyword);
				request.setAttribute("keyword", keyword);
			}
			params.put("hosttype", "nginx");
			params.put("platform", platform);
			params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
			params.put("pageSize",
					StringUtil.getIntParam(request, "pageSize", 10));

			request.setAttribute("pageData", hostService.queryHostPage(params,user.getUserId()));
			return getUrlPrefix() + "/host";
		} catch (Exception e) {
			logger.error("HOST列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	public String page(HttpServletRequest request, String platform) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			String keyword = request.getParameter("keyword");
			if (keyword != null && !keyword.trim().equals("")) {
				params.put("keyword", keyword);
				request.setAttribute("keyword", keyword);
			} 
			params.put("platform", platform);
			params.put("pageNo", StringUtil.getIntParam(request, "pageNo", 1));
			params.put("pageSize",
					StringUtil.getIntParam(request, "pageSize", 10));

			request.setAttribute("pageData", nginxService.queryNginxPage(params, user.getUserId()));
			return getUrlPrefix() + "/page";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Nginx配置列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
}
