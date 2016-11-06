package com.harmazing.openbridge.paasos.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.PageData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.async.AsyncFactory;
import com.harmazing.openbridge.paas.async.AsyncRunnable;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.vo.ContainerStatus;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployStatus;
import com.harmazing.openbridge.paas.deploy.model.vo.PodStatus;
import com.harmazing.openbridge.paas.util.EnvMarkUtil;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.manager.model.PaaSTenant;
import com.harmazing.openbridge.paasos.manager.service.IPaaSTenantService;
import com.harmazing.openbridge.paasos.oslog.OSLogFactory;
import com.harmazing.openbridge.paasos.project.deploy.CreateDeployCallBack;
import com.harmazing.openbridge.paasos.project.deploy.CreateDeployCommand;
import com.harmazing.openbridge.paasos.project.deploy.DeleteDeployCallBack;
import com.harmazing.openbridge.paasos.project.deploy.DeleteDeployCommand;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;
import com.harmazing.openbridge.paasos.utils.KubernetesUtil;

import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;

@Controller
@RequestMapping("/manager/deploy")
public class PaasManagerDeployController extends AbstractController {

	@Autowired
	private IPaasProjectDeployService iPaasProjectDeployService;
	@Autowired
	private IPaasProjectService iPaasProjectService;
	@Resource
	private IPaaSTenantService paaSTenantService;

	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		try {
			return getUrlPrefix() + "/index";
		} catch (Exception e) {
			logger.error("列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String projectType = request.getParameter("projectType");
			String tenantId = request.getParameter("tenantId");
			String envType = request.getParameter("envType");
			String status = request.getParameter("status");
			String keyword = request.getParameter("keyword");
			String userId= request.getParameter("userId");
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			params.put("projectType", projectType);
			params.put("tenantId", tenantId);
			params.put("envType", envType);
			params.put("status", status);
			params.put("keyword", keyword);
			params.put("userId", userId);
			Page<PaaSTenant> tenant = paaSTenantService.getPage(1, 100);
			request.setAttribute("tenant", tenant);
			Page<Map<String, Object>> pageDate = iPaasProjectDeployService.getAllDeploy(params);
			request.setAttribute("pageData", pageDate);
			
			//dxq
			String remark = ConfigUtil.getConfigString("paasos.evnmark");
			if(StringUtils.hasText(remark)){
				JSONObject jo = JSONObject.parseObject(remark);
				request.setAttribute("envMark", jo);
			}
			
			
			
			return getUrlPrefix() + "/list";
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}

	@RequestMapping("/table")
	public String Table(HttpServletRequest request, HttpServletResponse response, Integer pageNo, Integer pageSize,
			String viewType) {
		if (pageNo == null)
			pageNo = 1;
		if (pageSize == null)
			pageSize = 10;
		String projectType = request.getParameter("projectType");
		String tenantId = request.getParameter("tenantId");
		String envType = request.getParameter("envType");
		String status = request.getParameter("status");
		String keyword = request.getParameter("keyword");
		String userId= request.getParameter("userId");
		String envMark = request.getParameter("envMark");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageNo", pageNo);
		params.put("pageSize", pageSize);
		params.put("projectType", projectType);
		params.put("tenantId", tenantId);
		params.put("envType", envType);
		params.put("status", status);
		params.put("keyword", keyword);
		params.put("userId", userId);
		if(!"all".equals(envMark)){
			params.put("envMark", envMark);
		}
		
		Page<PaaSTenant> tenant = paaSTenantService.getPage(1, 100);
		request.setAttribute("tenant", tenant);
		Page<Map<String, Object>> pageDate = iPaasProjectDeployService.getAllDeploy(params);
		request.setAttribute("pageData", pageDate);
		return getUrlPrefix() + "/table";
	}

	@RequestMapping("/deploy")
	@ResponseBody
	public JsonResponse deploy(HttpServletRequest request, HttpServletResponse response) {
		IUser user = WebUtil.getUserByRequest(request);
		String deployId = request.getParameter("deployId");
		try {
			// 环境变量修改是否提示
			String variableChangeTip = request.getParameter("variableChangeTip");
			String changeVariable = request.getParameter("changeVariable");

			if (StringUtils.hasText(changeVariable) && "true".equals(changeVariable)) {
				// 是否替换新的环境变量 如果替换 后面直接启动
				iPaasProjectDeployService.changeLatestEnvVariables(deployId, user);
			} else {
				if (StringUtils.hasText(variableChangeTip) && "true".equals(variableChangeTip)) {
					// 判断环境变量是否修改过
					boolean bo = iPaasProjectDeployService.hasDifference(deployId, user);
					if (bo) {
						throw new RuntimeException("-2");
					}
				}
			}

			PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
			PaasProject pp = iPaasProjectService.getPaasProjectInfoById(pd.getProjectId());

			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(deployId);
			OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:开始[进行中]", user.getUserId(), true);

			// 看是否有空余空间去配额
			iPaasProjectDeployService.hasMoreResource(pd.getEnvType(), pp.getTenantId(), true, deployId);

			buildTask.setSessionId(OSLogFactory.getSessionValue());
			AsyncFactory.execute(new AsyncRunnable(buildTask, new CreateDeployCommand(), new CreateDeployCallBack()));
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("发布失败", e);
			request.setAttribute("exception", e);
			if (e.getMessage() != null && e.getMessage().equals("-2")) {
				return JsonResponse.failure(-2, "环境变量已经发生变化");
			} else {
				OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:结束[失败][" + e.getMessage() + "]", true);
				return JsonResponse.failure(500, e.getMessage());
			}
		}
	}

	@RequestMapping("/stop")
	@ResponseBody
	public JsonResponse stop(HttpServletRequest request, HttpServletResponse response) {
		String deployId = request.getParameter("deployId");
		try {
			IUser user = WebUtil.getUserByRequest(request);

			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			// 1停止 5启动中 6变更中 7停止中 10运行中 11删除失败 0启动失败
			if (pb.getStatus() != null && pb.getStatus().intValue() != 10 && pb.getStatus().intValue() != 11) {
				throw new RuntimeException("部署不处于运行或者删除失败状态，不能停止");
			}

			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(pb.getDeployId());
			OSLogFactory.add(deployId, OSLogFactory.STOP_OPERATOR, "停止部署:开始[进行中]", user.getUserId(), true);
			buildTask.setSessionId(OSLogFactory.getSessionValue());

			AsyncFactory.execute(
					new AsyncRunnable(buildTask, new DeleteDeployCommand(true), new DeleteDeployCallBack(true)));

			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	@RequestMapping("/status")
	@ResponseBody
	public JsonResponse status(HttpServletRequest request,
			HttpServletResponse response) {
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String deployIds = request.getParameter("deployIds");
			PodList pl = KubernetesUtil.getPods(null,"serviceId",deployIds.split(","));
			if(pl==null || pl.getItems()==null || pl.getItems().size()==0){
				throw new RuntimeException("获取pod失败");
			}
			List<DeployStatus> result = new ArrayList<DeployStatus>();
			
			Map<String,DeployStatus> ref = new HashMap<String,DeployStatus>();
			for(Pod p : pl.getItems()){
				String deployId = p.getMetadata().getLabels().get("serviceId");
				PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
				if(!ref.containsKey(deployId)){
					DeployStatus ds = new DeployStatus();
					ds.setDeployId(deployId);
					ds.setProjectId(pb.getProjectId());
					result.add(ds);
					ref.put(deployId,ds);
				}
				PodStatus ps = new PodStatus();
				ps.setPodName(p.getMetadata().getName());
				ps.setStatus(p.getStatus().getPhase().toLowerCase());
				ps.setReason(StringUtils.hasText(p.getStatus().getReason())?p.getStatus().getReason() : "暂无消息");
				ref.get(deployId).getPods().add(ps);
				
				if(CollectionUtils.isEmpty(p.getStatus().getContainerStatuses())){
					ContainerStatus cs = new ContainerStatus();
					cs.setContainerName("暂时没产生");
					cs.setReason("还没分配");
					cs.setStatus("wait");
					ps.getContainer().add(cs);
				}
				else{
					for(io.fabric8.kubernetes.api.model.ContainerStatus cs1 : p.getStatus().getContainerStatuses()){
						ContainerStatus cs2 = new ContainerStatus();
						cs2.setContainerId(cs1.getContainerID());
						cs2.setContainerName(cs1.getName());
						
						if(cs1.getState().getRunning()!=null){
							cs2.setStatus("running");
						}
						if(cs1.getState().getTerminated()!=null){
							cs2.setStatus("terminated");
							cs2.setReason(cs1.getState().getTerminated().getReason());
						}
						if(cs1.getState().getWaiting()!=null){
							cs2.setStatus("waiting");
							cs2.setReason(cs1.getState().getWaiting().getReason());
						}
						ps.getContainer().add(cs2);
					}
				}
			}
			return JsonResponse.success(result);
		}catch (Exception e) {
			logger.error("发布失败", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/env/list")
	public String envList(HttpServletRequest request, HttpServletResponse response) {
		try {
			return getUrlPrefix() + "/env/list";
		} catch (Exception e) {
			logger.error("列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/env/data")
	@ResponseBody
	public JsonResponse envData(HttpServletRequest request, HttpServletResponse response) {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		try {
			Map<String,String> mark = EnvMarkUtil.getEnvMark();
			String defaultTest = null;
			String defaultLive = null;
			
			String remark = ConfigUtil.getConfigString("paasos.evnmark");
			Map<String,String> ref = new HashMap<String,String>();
			ref.put("test", "test");
			ref.put("live", "live");
			if(StringUtils.hasText(remark)){
				JSONObject jo = JSONObject.parseObject(remark);
				for(String k : jo.keySet()){
					JSONArray data = jo.getJSONArray(k);
					List<Map> envmark = JSONObject.parseArray(data.toJSONString(), Map.class);
					for(Map m : envmark){
						String code = m.get("code")+"";
						ref.put(code, k);
					}
					if("test".equals(k)){
						defaultTest = envmark.get(0).get("code")+"";
					}
					else if("live".equals(k)){
						defaultLive = envmark.get(0).get("code")+"";
					}
				}
			}
			if(StringUtils.isEmpty(defaultLive)){
				defaultLive = "live";
				mark.put("live", "生产环境");
			}
			
			
			for(String code : mark.keySet()){
				Map<String,Object> column = new HashMap<String,Object>();
				String name = mark.get(code);
				column.put("name", name);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("pageNo", 1);
				params.put("pageSize", 999999);
				params.put("envMark", code);
				params.put("envType", ref.get(code));
				if(code.equals(defaultTest) || code.equals(defaultLive)){
					params.put("envDefault", 1);
				}
				Page<Map<String, Object>> pageData = iPaasProjectDeployService.getAllDeploy(params);
				column.put("data",pageData);
				result.add(column);
			}
			return JsonResponse.success(result);
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
