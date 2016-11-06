package com.harmazing.openbridge.paasos.manager.controller;

import io.fabric8.kubernetes.api.model.Namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.kubectl.K8RestApiUtil;
import com.harmazing.openbridge.paasos.manager.model.PaaSNode;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenant;
import com.harmazing.openbridge.paasos.manager.service.IPaaSBaseBuildService;
import com.harmazing.openbridge.paasos.manager.service.IPaaSNodeService;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;
import com.harmazing.openbridge.paasos.utils.ConfigUtils;

@Controller
@RequestMapping("/manager/tenant")
public class PaasTenantController extends AbstractController {
	@Resource
	private IPaaSTenantService paaSTenantService;
	@Resource
	private IPaaSNodeService paasNodeService;
	@Resource
	private IPaaSBaseBuildService paasBaseBuildService;
	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response){
		return getUrlPrefix() + "/list";
	}
	@RequestMapping("/tenantTable")
	public String list(HttpServletRequest request, HttpServletResponse response,
			Integer pageNo,Integer pageSize) {
		if(pageNo==null)	pageNo = 1;
		if(pageSize==null)	pageSize = 10;
		
		Page<PaaSTenant> page = paaSTenantService.getPage(pageNo, pageSize);
		request.setAttribute("page", page);
		return getUrlPrefix() + "/tenantTable";
	}
	
	@RequestMapping("detail")
	public String detail(HttpServletRequest request,String id){
		K8RestApiUtil query = new K8RestApiUtil(paaSTenantService);
		List<PaaSNode> tenantNodes = null;
//		tenantNodes = paasNodeService.getTenantNodes(id);
		tenantNodes = query.getTenantNodes(id);
		request.setAttribute("builds", paasBaseBuildService.getTenantBuild(id));
		request.setAttribute("nodes", tenantNodes);
		request.setAttribute("envs", ConfigUtils.getEnvTypes());
		return getUrlPrefix() + "/tenantDetail";
	}
	
	@RequestMapping("/add_tenant")
	public String addTenant(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		PaaSTenant tenant = new PaaSTenant();
		if(StringUtil.isNotNull(id)){
			tenant = paaSTenantService.get(id);
		}
		request.setAttribute("tenant", tenant);
		request.setAttribute("tenantQuotaJson", JSONArray.toJSON(tenant.getTenantQuotaList()));
		
		List<Map<String,Object>> firstTenantItemCategory = paaSTenantService.findTenantItemList(new ArrayList<Map<String,Object>>());
		List<Map<String,Object>> secondTenantItemCategory = paaSTenantService.findTenantItemList(firstTenantItemCategory);
		List<Map<String,Object>> thridTenantItemCategory = paaSTenantService.findTenantItemList(secondTenantItemCategory);
		request.setAttribute("firstTenantItemCategory", firstTenantItemCategory);
		request.setAttribute("secondTenantItemCategory", secondTenantItemCategory);
		request.setAttribute("thridTenantItemCategory", thridTenantItemCategory);
		
//		Map<String,String> paramMap = new HashMap<String,String>();
//		paramMap.put("tenantId", "70jpdni2gggryyh8wvqtykuacdczlc6");
//		paramMap.put("categoryType", "database");
//		paramMap.put("subCategoryType", "mysql");
//		paramMap.put("itemLookupType", "mysql_memory");
//		List<Map<String, String>> resultMap = paaSTenantService.getTenantQuotaInfo(paramMap);
		
		return getUrlPrefix() + "/add_tenant";
	}
	@RequestMapping(value="saveOrUpdate",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse saveOrUpdate(String json){
		PaaSTenant tenant = null;
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			if(StringUtil.isNotNull(json)){
				tenant = JSON.parseObject(json, PaaSTenant.class);
			}
			if(tenant!=null){
				paaSTenantService.saveOrUpdate(tenant);
			}
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, e.getMessage());
		}
		return jsonResponse;
	}
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse delete(String id){
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			paaSTenantService.delete(id);
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
	
	@RequestMapping("getTenants")
	@ResponseBody
	public List<PaaSTenant> getTenants(){
		return paaSTenantService.listBrief();
	}
	
	@RequestMapping("getNamespaces")
	@ResponseBody
	public List<Namespace> getNamespaces(){
		K8RestApiUtil util = new K8RestApiUtil(paaSTenantService);
		return util.getClient().namespaces().list().getItems();
	}
	
	@RequestMapping("synchroNamespace")
	@ResponseBody
	public JsonResponse synchroNamespace(String id,String name){
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			if(StringUtil.isNotNull(name)){
				K8RestApiUtil util = new K8RestApiUtil(paaSTenantService);
				util.saveNamespace(id,name);
			}
		}catch(Exception e){
			logger.debug(e);
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
	
	/**
	* @Title: getDeployResouceAllByEnvId 
	* @author weiyujia
	* @Description: 获取租户资源配额信息
	* @param request
	* @param response
	* @return  设定文件 
	* @return JsonResponse    返回类型 
	* @throws 
	* @date 2016年7月12日 下午3:09:07
	 */
	@RequestMapping("/getTenantQuotaInfo")
	@ResponseBody
	public JsonResponse getTenantQuotaInfo(HttpServletRequest request, HttpServletResponse response){
		try {
			String tenantId = request.getParameter("tenantId");
			if(StringUtil.isEmpty(tenantId)){
				return JsonResponse.failure(500, "参数异常，请提供租户ID！");
			}
			String categoryType = request.getParameter("categoryType");
			String subCategoryType = request.getParameter("subCategoryType");
			String itemLookupType = request.getParameter("itemLookupType");
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("tenantId", tenantId);
			paramMap.put("categoryType", categoryType);
			paramMap.put("subCategoryType", subCategoryType);
			paramMap.put("itemLookupType", itemLookupType);
			List<Map<String,String>> resultList = paaSTenantService.getTenantQuotaInfo(paramMap);
			return JsonResponse.success(resultList);
		} catch (Exception e) {
			return JsonResponse.failure(500, e.getMessage());
		}
		
	}
	
}
