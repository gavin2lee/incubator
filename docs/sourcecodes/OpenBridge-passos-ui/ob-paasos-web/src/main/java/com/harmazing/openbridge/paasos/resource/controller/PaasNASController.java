package com.harmazing.openbridge.paasos.resource.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.util.StorageUtil;
import com.harmazing.openbridge.paasos.resource.model.PaasNAS;
import com.harmazing.openbridge.paasos.resource.service.IPaasNASService;
import com.harmazing.openbridge.paasos.utils.ConfigUtils;

public abstract class PaasNASController extends PaasResourceBaseController {
	protected static final Log logger = LogFactory
			.getLog(PaasNASController.class);

	@Autowired
	protected IPaasNASService nasService;

	public String list(HttpServletRequest request,
			HttpServletResponse response, String nasType) {
		try {
			ConfigUtils.setEnvType2Request(request);
			IUser user = WebUtil.getUserByRequest(request);
			Map<String, Object> params = new HashMap<String, Object>();
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			String listType = request.getParameter("listType");
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			params.put("nasType", nasType);
			/*if (!user.isAdministrator()) {
				params.put("userId", user.getUserId());
			}*/
			if(listType != null && listType.equals("tenant")){
				params.put("tenantId", user.getTenantId());
			}else{ 
				params.put("userId", user.getUserId());
				params.put("tenantId", user.getTenantId());
			}
			List<PaasNAS> pageData = nasService.queryPaasNASByParams(params);
			request.setAttribute("pageData", pageData);
			request.setAttribute("nasType", nasType);
			return getUrlPrefix() + "/list";
		} catch (Exception e) {
			request.setAttribute("exception", e);
			logger.error("获取网络存储列表信息出错");
			return forward(ERROR);
		}
	}

	@Override
	protected void setResourceLimit2Request(HttpServletRequest request) {
		String userId = (String) request.getAttribute("userId");
		String tenantId = (String) request.getAttribute("tenantId");
		String nasType = request.getParameter("nasType");
		if (StringUtil.isNull(nasType)
				|| (!nasType.equals("NFS") && !nasType.equals("VOLUME"))) {
			nasType = "NFS";
		}
		request.setAttribute("nasType", nasType);
		JSONObject limits = nasService.getPaasNASOptions(userId, tenantId,
				nasType);
		if (limits != null) {
			request.setAttribute("options", limits);
		}
	}
	
	@RequestMapping("/options")//实时rest请求查询NAS配额信息
	@ResponseBody
	public JsonResponse queryNASOptions(HttpServletRequest request, HttpServletResponse response){
		try{
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String nasType = request.getParameter("nasType");
			if (StringUtil.isNull(nasType)
					|| (!nasType.equals("NFS") && !nasType.equals("VOLUME"))) {
				nasType = "NFS";
			}
			JSONObject nasOptions = nasService.getPaasNASOptions(userId, tenantId,
					nasType);
			return JsonResponse.success(nasOptions);
		}catch(Exception e){
			logger.error("查询NAS配额信息失败"+e.getMessage());
			return JsonResponse.failure(500, "查询NAS配额信息失败");
		}
	}

	public JsonResponse queryNasInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String paasOsUserId = user.getUserId();
			String paasOsTenantId = user.getTenantId();
			String nfsId = request.getParameter("resId");
			if (StringUtil.isNull(nfsId) || StringUtil.isNull(paasOsUserId)
					|| StringUtil.isNull(paasOsTenantId)) {
				return JsonResponse.failure(403, "查询参数不正确");
			}
			JSONObject nfsObj = nasService.queryNASInfo(nfsId, paasOsUserId,
					paasOsTenantId);
			if(nfsObj!=null){
				return JsonResponse.success("可用");
			}
			return JsonResponse.failure(500, "查询网络存储状态失败");
		} catch (Exception e) {
			logger.error("查询网络存储状态失败" + e.getMessage());
			return JsonResponse.failure(500, "查询网络存储状态失败");
		}
	}

	public JsonResponse queryNasAccessIPs(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String nfsId = request.getParameter("resId");
			String paasOsUserId = request.getParameter("paasOsUserId");
			String paasOsTenantId = request.getParameter("paasOsTenantId");
			if (StringUtil.isNull(nfsId) || StringUtil.isNull(paasOsUserId)
					|| StringUtil.isNull(paasOsTenantId)) {
				return JsonResponse.failure(403, "查询参数不正确");
			}
			PaasNAS nfsInfo = nasService.getPaasNASInfoById(nfsId);
			JSONArray nfsObj = nasService.queryNASAccessIps(nfsInfo);
			return JsonResponse.success(nfsObj);
		} catch (Exception e) {
			logger.error("查询网络存储状态失败" + e.getMessage());
			return JsonResponse.failure(500, "查询网络存储状态失败");
		}
	}

	public JsonResponse addNasSave(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			String instanceName = request.getParameter("instanceName");
			String version = request.getParameter("version");
			String source = request.getParameter("source");
			String storage = request.getParameter("storage");
			String projectId = request.getParameter("projectId");
			String envId = request.getParameter("envId");
			String envType = request.getParameter("envType");
			String nasType = request.getParameter("nasType");
			String desc = request.getParameter("resDesc");
			String applyType = request.getParameter("applyType");
			if(StringUtil.isNull(applyType) || !"api".equals(applyType)&&!"app".equals(applyType)){
				applyType="paasOS";
			}
			if (StringUtil.isNull(instanceName) || StringUtil.isNull(tenantId)) {
				return JsonResponse.failure(400, "新建网络存储资源失败,实例名和租户不能为空");
			}
			JSONObject Config = new JSONObject();
			PaasNAS nfs = new PaasNAS();
			
			nfs.setStorage(StorageUtil.getMemory(storage));
			
			Config.put("name", instanceName);
			Config.put("size", storage);
			Config.put("version", version);
			JSONObject attributes = new JSONObject();
			attributes.put("envId", envId);
			attributes.put("projectId", projectId);
			attributes.put("envType", envType);
			Config.put("attributes", attributes);
			
			nfs.setNasId(StringUtil.getUUID());
			nfs.setInstanceName(instanceName);
			nfs.setNasSource(source);
			nfs.setApplyContent(Config.toJSONString());
			nfs.setCreater(userId);
			Date now = new Date();
			nfs.setCreateDate(now);
			nfs.setUpdateDate(now);
			nfs.setAllocateContent("");
			nfs.setTenantId(tenantId);
			nfs.setProjectId(projectId);
			nfs.setEnvId(envId);
			nfs.setEnvType(envType);
			nfs.setOnReady(false);
			nfs.setOnStatus(0);// 初始状态
			nfs.setNasType(nasType);
			nfs.setResDesc(desc);
			nfs.setApplyType(applyType);
			nasService.addPaasNASResource(nfs);
			logger.debug("成功添加网络存储实例，" + instanceName);
			return JsonResponse.success(nfs.getNasId());
		} catch (Exception e) {
			logger.error("添加网络存储实例失败" + e.getMessage(),e);
			return JsonResponse.failure(500, "新建网络存储资源失败" + e.getMessage());
		}
	}

	public JsonResponse deleteNas(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String nfsId = request.getParameter("resId");
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			if (StringUtil.isNull(nfsId) || StringUtil.isNull(tenantId)
					|| StringUtil.isNull(userId)) {
				logger.error("删除网络存储资源出错，参数出错");
				return JsonResponse.failure(500, "删除网络存储资源失败,参数出错");
			}
			JSONObject res = nasService.deletePaasNASById(nfsId, userId,
					tenantId);
			if (res.containsKey("result")
					|| res.getString("result").equals("success")) {
				logger.debug("删除网络存储资源" + nfsId);
				return JsonResponse.success();
			} else {
				logger.error("删除网络存储资源操作失败" + nfsId);
				return JsonResponse.failure(500, "删除网络存储资源失败");
			}
		} catch (Exception e) {
			logger.error("删除网络存储资源操作失败" + e.getMessage());
			return JsonResponse.failure(500, "删除网络存储资源失败" + e.getMessage());
		}
	}

	public String nasInfo(HttpServletRequest request,
			HttpServletResponse response) {
		String nasId = request.getParameter("nasId");
		PaasNAS nasInfo = nasService.getPaasNASInfoById(nasId);
		request.setAttribute("nasInfo", nasInfo);
		ConfigUtils.setEnvType2Request(request);
		return getUrlPrefix() + "/info";
	}

	public JsonResponse grantAccess2NFS(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String nfsId = request.getParameter("nfsId");
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			PaasNAS nfsInfo = nasService.getPaasNASInfoById(nfsId);
			if (!nfsInfo.getNasType().equals("NFS")) {
				throw new Exception("目前只能对共享存储授权");
			}
			// if(StringUtil.isNull(nfsInfo.getExportLocation())){
			// throw new Exception("暂时无法获取Export_Location");
			// }
			nasService.grantPaasNASAccess(userId, tenantId, nfsInfo);
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("共享存储授权失败");
			return JsonResponse.failure(500, "共享存储授权失败" + e.getMessage());
		}
	}

	@RequestMapping("/attach")
	@ResponseBody
	public JsonResponse attach2Volume(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String nfsId = request.getParameter("nfsId");
			IUser user = WebUtil.getUserByRequest(request);
			String userId = user.getUserId();
			String tenantId = user.getTenantId();
			PaasNAS nfsInfo = nasService.getPaasNASInfoById(nfsId);
			if (!nfsInfo.getNasType().equals("VOLUME")) {
				throw new Exception("目前只能对共享存储授权");
			}
			return JsonResponse.success(nasService
					.queryAttachServerList(nfsInfo));
		} catch (Exception e) {
			logger.error("卷存储挂载，获取服务器信息失败");
			return JsonResponse
					.failure(500, "卷存储挂载，获取服务器信息失败" + e.getMessage());
		}
	}

	public String updateNASNameDesc(HttpServletRequest request,
			HttpServletResponse response) {
		String nfsId = request.getParameter("nfsId");
		PaasNAS nfsInfo = nasService.getPaasNASInfoById(nfsId);
		request.setAttribute("nfsInfo", nfsInfo);
		ConfigUtils.setEnvType2Request(request);
		IUser user = WebUtil.getUserByRequest(request);
		request.setAttribute("userId", user.getUserId());
		request.setAttribute("tenantId", user.getTenantId());
		request.setAttribute("tenantName", user.getTenantName());
		return getUrlPrefix() + "/update";

	}

	public JsonResponse updateNASNameDescSave(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String nfsId = request.getParameter("nfsId");
			String userId = request.getParameter("userId");
			String instanceName = request.getParameter("instanceName");
			String version = request.getParameter("version");
			String source = request.getParameter("source");
			String tenantId = request.getParameter("tenantId");
			String storage = request.getParameter("storage");
			String projectId = request.getParameter("projectId");
			String envId = request.getParameter("envId");
			String envType = request.getParameter("envType");
			if (StringUtil.isNull(instanceName) || StringUtil.isNull(tenantId)) {
				return JsonResponse.failure(400, "修改网络存储资源失败,实例名和租户不能为空");
			}
			JSONObject Config = new JSONObject();
			Config.put("name", instanceName);
			Config.put("storage", storage);
			Config.put("version", version);
			JSONObject attributes = new JSONObject();
			attributes.put("envId", envId);
			attributes.put("projectId", projectId);
			attributes.put("envType", envType);
			Config.put("attributes", attributes);
			PaasNAS nfs = new PaasNAS();
			nfs.setNasId(nfsId);
			nfs.setInstanceName(instanceName);
			nfs.setNasSource(source);
			nfs.setApplyContent(Config.toJSONString());
			Date now = new Date();
			nfs.setUpdateDate(now);
			nfs.setTenantId(tenantId);
			nfs.setEnvType(envType);
			nasService.updatePaasNASInfo(nfs);
			logger.debug("成功修改网络存储实例，" + instanceName);
			return JsonResponse.success();
		} catch (Exception e) {
			return JsonResponse.failure(500, "修改网络存储配置失败");
		}
	}
	
	public JsonResponse nasInstance(HttpServletRequest request,
			HttpServletResponse response) {
		String nasId = request.getParameter("nasId");
		PaasNAS nasInfo = nasService.getPaasNASInfoById(nasId);
		Map<String,String> data = new HashMap<String,String>();
		data.put("storage", nasInfo.getAllocateStorageInfo());
		if(nasInfo.getNasType().equals("NFS")){
			data.put("info", nasInfo.getNfsInfo());
		}else{
			data.put("info", nasInfo.getIscsiInfo());
		}
		return JsonResponse.success(data);
	}
}
