package com.harmazing.openbridge.paasos.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectBuildService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;
import com.harmazing.openbridge.sys.user.service.ISysUserService;

@Controller
@RequestMapping("/project")
public class PaasProjectController extends ProjectBaseController {

	@Autowired
	private ISysUserService sysUserService;
	
	@Autowired
	private IPaasProjectService iPaasProjectService;
	
	@Autowired
	private IPaasProjectBuildService iPaasProjectBuildService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String viewType = request.getParameter("viewType");
			String listType = request.getParameter("listType");
			String keyword = request.getParameter("keyword");
			String projectType = request.getParameter("projectType");
//			String projectName = request.getParameter("projectName");
//			String projectCode = request.getParameter("projectCode");
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			if(listType != null && listType.equals("tenant")){
				params.put("tenantId", user.getTenantId());
			}else{ 
				params.put("userId", user.getUserId());
				params.put("tenantId", user.getTenantId());
			}
			params.put("keyword", keyword);
			params.put("listType", listType);
			params.put("projectType", projectType);
//			params.put("projectName", projectName);
//			params.put("projectCode", projectCode);
			Page<Map<String, Object>> pageDate = iPaasProjectService.page(params);
			request.setAttribute("pageData", pageDate);
			return getUrlPrefix() + "/list/" + viewType;
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	
	/**
	 * 
	 * list:(关联的应用/服务). <br/>
	 *
	 * @author dengxq
	 * @param request
	 * @param response
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping("/listRelation")
	public String listRelation(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String type = request.getParameter("type");
			String viewType = request.getParameter("viewType");
//			String listType = request.getParameter("listType");
			String keyword = request.getParameter("keyword");
			String status = request.getParameter("status");
			if (StringUtil.isNull(viewType)) {
				viewType = "table";
			}
			if (!viewType.equals("table") && !viewType.equals("preview")) {
				viewType = "table";
			}
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			params.put("userId", user.getUserId());
			params.put("status", status);
			params.put("tenantId", user.getTenantId());
			if(StringUtils.hasText(keyword)){
				params.put("keyword", "%"+keyword+"%");
			}
			
			Page<Map<String, Object>> pageDate = iPaasProjectService.listRelation(params,type);
		
			request.setAttribute("keyword", keyword);
			request.setAttribute("pageData", pageDate);
			return getUrlPrefix() + "/list/listRelation";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		try {
			return getUrlPrefix() + "/index";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/create")
	public String create(HttpServletRequest request,
			HttpServletResponse response) {
		try {
//			List<PaasProject> category = null;// appCategoryService.findTree();
//			request.setAttribute("category", category);
			return getUrlPrefix() + "/create";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse save(HttpServletRequest request,
			HttpServletResponse response, PaasProject project) {
		try {
			IUser user = null;
			if(project.getCreateUser()==null){
				user = WebUtil.getUserByRequest(request);
			}
			if(user.getTenantId()==null){
            	return JsonResponse.failure(2,"此用户没有租户不能创建项目");
            }
			project.setTenantId(user.getTenantId());
			String projectId = iPaasProjectService.save(user==null?project.getCreateUser():user.getUserId(),project);
			return JsonResponse.success(projectId);
		}catch(DuplicateKeyException e1){ 
			return JsonResponse.failure(501, "项目编码重复，请重新输入");
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("getProjectByBusId")
	@ResponseBody
	public List<PaasProject> getProjectByBusId(String businessId,HttpServletResponse response){
		return iPaasProjectService.getProjectByBusId(businessId);
	}

	

	@RequestMapping("/updateForAppApi")
	@ResponseBody
	public JsonResponse updateForAppApi(HttpServletRequest request,
			HttpServletResponse response, PaasProject project) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			iPaasProjectService.updatePaasProjectByBusinessIdAndType(project,user.getUserId());
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

	@RequestMapping("/deleteForAppApi")
	@ResponseBody
	public JsonResponse deleteForAppApi(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			iPaasProjectService.deleteForAppApi(request.getParameter("businessId"),request.getParameter("businessType"));
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse delete(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PaasProject app = loadProjectModel(request);
			if(app.getProjectType().equals("app")||app.getProjectType().equals("api")){
				return JsonResponse.failure(500,"请前往"+app.getProjectType()+"进行删除");
			}
			IUser user = WebUtil.getUserByRequest(request);
			String password = request.getParameter("password");
			IUser xxx = sysUserService.login(user.getLoginName(), password);
			if (xxx != null && xxx.getUserId().equals(user.getUserId())) {
				iPaasProjectService.delete(app.getProjectId());
				return JsonResponse.success();
			} else {
				return JsonResponse.success("密码错误");
			}
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/overview/index")
	public String info(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			PaasProject app = loadProjectModel(request);			

			return getUrlPrefix() + "/overview/index" ;
		} catch (Exception e) {
			logger.error("项目概述页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) {
		try {
			PaasProject app = loadProjectModel(request);			

			return getUrlPrefix() + "/edit" ;
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
}
