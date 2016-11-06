package com.harmazing.openbridge.portal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paasos.kubectl.K8RestApiUtil;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectDeployMapper;
import com.harmazing.openbridge.paasos.project.dao.PaasProjectMapper;
@Controller()
@RequestMapping("/portal")
public class PortalController extends AbstractController {

	@Autowired
	private PaasProjectMapper paasProjectMapper;
	@Autowired
	private PaasProjectDeployMapper PaasProjectDeployMapper;
	@RequestMapping("/*")
	public String welcome(HttpServletRequest request,
			HttpServletResponse response) {
		String path = request.getServletPath();
		String prefix = getUrlPrefix();
		if (path.startsWith(prefix)) {
			path = path.substring(prefix.length());
		}
		int end = path.indexOf(".do");
		path = path.substring(0, end);
		return prefix + path;
	}
	
	@RequestMapping("/overview")
	public String overview(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			if(user.isAnonymous()){
				return getUrlPrefix() + "/overview";
			}
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("userId", user.getUserId());
			params.put("tenantId", user.getTenantId());
		
			int countstore= paasProjectMapper.getCountstore(params);
			int count=paasProjectMapper.getCount(params);
			int openbridge=count-countstore;
			int countrun= PaasProjectDeployMapper.getCountrun(params);
			int countstop=PaasProjectDeployMapper.getCountstop(params);
			Map<String,Object> host=new HashMap<String, Object>();
			Map<String,Object> Image=new HashMap<String, Object>();
			if(StringUtil.isNotNull(user.getTenantId())){
				try{
//					int m = 1/0;
					host=K8RestApiUtil.getTenantNodeStatistics(user.getTenantId());
					Image=K8RestApiUtil.getTenantImageStatistics(user.getTenantId());
					
					int hosttotal=  (int) host.get("total");
					int hostonline=  (int) host.get("online");
					int hostoutline=hosttotal-hostonline;
					request.setAttribute("hostoutline", hostoutline);
				}
				catch(Exception e){
					logger.error("k8s失败",e);
					request.setAttribute("hostoutline", "-");
				}
			}else{
				host.put("total", 0);
				host.put("online", 0);
				host.put("offline", 0);
				host.put("unknown", 0);	
			}
			
			
			request.setAttribute("host", host);
			request.setAttribute("Image", Image);
			
			request.setAttribute("openbridge", openbridge);
			request.setAttribute("store", countstore);
			request.setAttribute("Tenantcount", count);
			request.setAttribute("countrun", countrun);
			request.setAttribute("countstop", countstop);
			request.setAttribute("TenantName",user.getTenantName());
			return getUrlPrefix() + "/overview";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
}
