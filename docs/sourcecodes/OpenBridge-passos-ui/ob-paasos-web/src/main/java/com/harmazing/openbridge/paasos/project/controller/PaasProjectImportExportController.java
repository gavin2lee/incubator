package com.harmazing.openbridge.paasos.project.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;

/**
 * @description the controller will handler the batch import/export api/app
 * @author taoshuangxi
 * @date 20160729
 */
@Controller
@RequestMapping("/project/rest/")
public class PaasProjectImportExportController extends ProjectBaseController {

	@Autowired
	private IPaasProjectService iPaasProjectService;
	
	@Autowired
	private IPaasProjectEnvService envService;

	@RequestMapping("/export/data")
	public JsonResponse list(HttpServletRequest request, HttpServletResponse response) {
		try {
			String businessId = request.getParameter("businessId");
			String businessType = request.getParameter("businessType");
			if(StringUtil.isNull(businessId) || StringUtil.isNull(businessType)){
				return JsonResponse.failure(404, "参数businessId/businessType 不正确");
			}
			JSONObject data = new JSONObject();
			PaasProject project = iPaasProjectService.getPaasProjectByBusinessIdAndType(businessId, businessType);
			if(project==null){
				return JsonResponse.failure(404, "找不到对应的project,参数businessId/businessType 不正确");
			}
			data.put("project", project);
			List<PaasEnv> envs = envService.getEnvListByBusinessId(project.getProjectId());
			data.put("envs", envs);
			return JsonResponse.success(data);
		} catch (Exception e) {
			logger.error("在paasos获取项目导出信息失败", e);
			return JsonResponse.failure(500, "在paasos获取项目导出信息失败"+e.getMessage());
		}
	}
	
	@RequestMapping("/import/save")
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
			logger.error("在paasos导入项目出错", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}

}
