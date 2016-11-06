package com.harmazing.openbridge.paasos.project.controller;

import io.fabric8.kubernetes.api.model.EventList;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.extension.Plugin;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.async.AsyncCommand;
import com.harmazing.openbridge.paas.async.AsyncException;
import com.harmazing.openbridge.paas.async.AsyncFactory;
import com.harmazing.openbridge.paas.async.AsyncRunnable;
import com.harmazing.openbridge.paas.async.AsyncTask;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployEnv;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployPort;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeployVolumn;
import com.harmazing.openbridge.paas.deploy.model.vo.ContainerStatus;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployResource;
import com.harmazing.openbridge.paas.deploy.model.vo.DeployStatus;
import com.harmazing.openbridge.paas.deploy.model.vo.PodStatus;
import com.harmazing.openbridge.paas.env.model.PaasEnv;
import com.harmazing.openbridge.paas.env.model.PaasEnvResource;
import com.harmazing.openbridge.paas.plugin.xml.ResourceNode;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;
import com.harmazing.openbridge.paasos.imgbuild.service.IPaasImageService;
import com.harmazing.openbridge.paasos.manager.model.vo.ResourceQuota;
import com.harmazing.openbridge.paasos.oslog.OSLogFactory;
import com.harmazing.openbridge.paasos.project.deploy.CreateDeployCallBack;
import com.harmazing.openbridge.paasos.project.deploy.CreateDeployCommand;
import com.harmazing.openbridge.paasos.project.deploy.DeleteDeployCallBack;
import com.harmazing.openbridge.paasos.project.deploy.DeleteDeployCommand;
import com.harmazing.openbridge.paasos.project.deploy.ReplicasDeployCallBack;
import com.harmazing.openbridge.paasos.project.deploy.ReplicasDeployCommand;
import com.harmazing.openbridge.paasos.project.deploy.VolumnChangeDeployCallBack;
import com.harmazing.openbridge.paasos.project.deploy.VolumnChangeDeployCommand;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.model.PaasProjectBuild;
import com.harmazing.openbridge.paasos.project.model.vo.TwoTuple;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectBuildService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectEnvService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;
import com.harmazing.openbridge.paasos.resource.util.ResourceQuotaUtil;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;
import com.harmazing.openbridge.paasos.store.service.IPaasStoreAppService;
import com.harmazing.openbridge.paasos.utils.ConfigUtils;
import com.harmazing.openbridge.paasos.utils.KubernetesUtil;

@Controller
@RequestMapping("/project/deploy")
public class PaasProjectDeployController extends ProjectBaseController {
	@Autowired
	private IPaasProjectService iPaasProjectService;
	
	@Autowired
	private IPaasProjectBuildService iPaasProjectBuildService;
	
	@Autowired
	private IPaasProjectDeployService iPaasProjectDeployService;
	@Autowired
	private IPaasStoreAppService paasStoreAppService;
	@Autowired
	private IPaasImageService iPaasImageService;
	@Autowired
	private IPaasProjectEnvService iPaasProjectEnvService;
	
	private static Log logger = LogFactory.getLog(PaasProjectDeployController.class);
	
	@RequestMapping("/index")
	public String index(HttpServletRequest request, HttpServletResponse response){
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject app = loadProjectModel(request);
			 
			
			return getUrlPrefix() + "/index";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/getDeployResouceAllByEnvId")
	@ResponseBody
	public JsonResponse getDeployResouceAllByEnvId(HttpServletRequest request, HttpServletResponse response){
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String envId = request.getParameter("envId");
			String deployId = request.getParameter("deployId");
			DeployResource dr = iPaasProjectEnvService.getDeployResouceAllByEnvId(envId, user.getUserId(),deployId);
			return JsonResponse.success(dr);
		} catch (Exception e) {
			logger.error(e);
			return JsonResponse.failure(500, e.getMessage());
		}
		
	}
	@RequestMapping("/create")
	public String create(HttpServletRequest request, HttpServletResponse response){
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProject app = loadProjectModel(request);
			
			List<PaasProjectBuild> paasProjectBuilds = iPaasProjectBuildService.findAvailableBuild(app.getProjectId());
			request.setAttribute("paasProjectBuilds", paasProjectBuilds);
			if("store".equals(app.getProjectType())){
				PaasStoreApp storeApp = paasStoreAppService.getById(app.getBusinessId());
				if(storeApp!=null){
					request.setAttribute("resourceConfig", storeApp.getConfig());
				}
				request.setAttribute("storeApp", storeApp);
			}
			ConfigUtils.setEnvType2Request(request);
			
			String envId = request.getParameter("envId");
			request.setAttribute("envId", envId);
			PaasEnv env = iPaasProjectEnvService.getEnvById(envId);
			request.setAttribute("env", env);
			
			DeployResource dr = iPaasProjectEnvService.getDeployResouceAllByEnvId(envId, user.getUserId(),null);
			request.setAttribute("dr", dr);
			
			
			List<PaasEnvResource> resourceList = env.getResources();
			Map<String, PaasEnvResource> resourcesMap = new HashMap<String, PaasEnvResource>();
			if (env.getResources() != null) {
				for(int i=resourceList.size()-1;i>-1;i--){
					PaasEnvResource resource = env.getResources().get(i);
					if(resource.getResourceId().equals("define")){
						resourceList.remove(i);
					}
				}
				for (int i = 0; i < resourceList.size(); i++) {
					PaasEnvResource resource = env.getResources().get(i);
					ResourceNode node = (ResourceNode) Plugin.getExtensionPoint(
							ResourceNode.pointId).getConfigNode(
							ResourceNode.nodeName,
							resource.getResourceId());
					resource.attObject("node", node);
					String key = "";
					if(node.getCategory().equals("database")){
						key = "db";
					}else if(node.getCategory().equals("mq")){
						key = "mq";
					}else if(node.getCategory().equals("cache")){
						key = "cache";
					}else if(node.getCategory().equals("storage")){
						key = "nas";
					}
					resourcesMap.put(key, resource);
				}
			}
			request.setAttribute("resourcesMap", resourcesMap);
			return getUrlPrefix() + "/create";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String viewType = request.getParameter("viewType");
//			String listType = request.getParameter("listType");
			String keyword = request.getParameter("keyword");
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			String projectId = request.getParameter("projectId");
			String extraKey = request.getParameter("extraKey");
			String envId = request.getParameter("envId");
			ConfigUtils.setEnvType2Request(request);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			params.put("userId", user.getUserId());
			params.put("keyword", keyword);
			params.put("projectId", projectId);
			params.put("extraKey", extraKey);
			params.put("envId", envId);
//			params.put("listType", listType);
			Page<Map<String, Object>> pageDate = iPaasProjectDeployService.page(params);//iPaasProjectBuildService.page(params);
			request.setAttribute("pageData", pageDate);
			if(!StringUtils.hasText(viewType)){
				viewType = "preview";
			}
			return getUrlPrefix() + "/list/" + viewType;
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	/**
	 * 
	 * data:(api app获取部署列表用). <br/>
	 *
	 * @author dengxq
	 * @param request
	 * @param response
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping("/data")
	@ResponseBody
	public JsonResponse data(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
//			String viewType = request.getParameter("viewType");
			String keyword = request.getParameter("keyword");
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			String projectId = request.getParameter("projectId");
			String envId = request.getParameter("envId");
			String extraKey = request.getParameter("extraKey");
			ConfigUtils.setEnvType2Request(request);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			params.put("userId", user.getUserId());
			params.put("keyword", keyword);
			params.put("projectId", projectId);
			params.put("envId", envId);
			params.put("extraKey", extraKey);
			Page<Map<String, Object>> pageDate = iPaasProjectDeployService.page(params);//iPaasProjectBuildService.page(params);
			return JsonResponse.success(pageDate);
		} catch (Exception e) {
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	/**
	 * 
	 * save:(这里不光paasos用  api app调用部署 也会调用这个接口). <br/>
	 *
	 * @author dengxq
	 * @param request
	 * @param response
	 * @param deploy
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse save(HttpServletRequest request,
			HttpServletResponse response, @RequestBody PaasProjectDeploy deploy) {
		IUser user = WebUtil.getUserByRequest(request);
		try {
			
//			boolean isUpdate = true;
//			if(StringUtils.isEmpty(deploy.getDeployId())){
//				isUpdate  = false;
//			}
			//PaasProject paasProject = iPaasProjectService.getPaasProjectInfoById(deploy.getProjectId());
			
			String deployId = iPaasProjectDeployService.save(user,deploy);
//			OSLogFactory.add(deployId, OSLogFactory.SAVE_OPERATOR, "保存部署:结束[成功]", user.getUserId(), true);
			return JsonResponse.success(deployId);
		}catch(DuplicateKeyException e1){ 
			return JsonResponse.failure(501, e1.getCause().getMessage());
		}catch (Exception e) {
			logger.error("保存发布失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public JsonResponse delete(HttpServletRequest request,
			HttpServletResponse response) {
		String deployId = request.getParameter("deployId");
		IUser user = WebUtil.getUserByRequest(request);
		OSLogFactory.add(deployId, OSLogFactory.DELETE_OPERATOR, "删除部署:开始[进行中]", user.getUserId(), true);
		
		try {
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			// 1已保存  5创建中 10创建成功 0创建失败   
			if(pb.getStatus()!=null && pb.getStatus().intValue()==5){
				throw new RuntimeException("部署处于创建中，不能删除");
			}
			else if(pb.getDeleteStatus()!=null && pb.getDeleteStatus().intValue()==6){
				throw new RuntimeException("部署处于变更中，不能删除");
			}
			else if(pb.getDeleteStatus()!=null && pb.getDeleteStatus().intValue()==7){
				throw new RuntimeException("部署处于停止中，不能删除");
			}
			//1已保存  5创建中 6变更中 10操作成功 0创建失败  11 删除失败  
			if(pb.getStatus()!=null && ( pb.getStatus().intValue()==1 || pb.getStatus().intValue()== 0) ){
				//处于1和0直接删除表中记录
				iPaasProjectDeployService.deleteDeploy(deployId);
			}
			else{
				BuildTask buildTask = new BuildTask();
				buildTask.setTaskId(pb.getDeployId());
				buildTask.setSessionId(OSLogFactory.getSessionValue());
				AsyncFactory.execute(new AsyncRunnable(buildTask, new DeleteDeployCommand(), new DeleteDeployCallBack()));
			}
			OSLogFactory.add(deployId, OSLogFactory.DELETE_OPERATOR, "删除部署:进行中[放在后台执行]");
			return JsonResponse.success();
		} catch (Exception e) {
			OSLogFactory.add(deployId, OSLogFactory.DELETE_OPERATOR, "删除部署:结束[失败:"+e.getMessage()+"]",true);
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response){
		try {
			
			String deployId = request.getParameter("deployId");
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			if(pb.getStatus()!=null && pb.getStatus().intValue()!=1 && pb.getStatus().intValue()!=0){
				throw new RuntimeException("部署不处于停止或启动失败状态，不能修改");
			}
			ConfigUtils.setEnvType2Request(request);
			commenEditAndView(request,response);
			
			String envId = request.getParameter("envId");
			request.setAttribute("envId", envId);
			PaasEnv env = iPaasProjectEnvService.getEnvById(envId);
			request.setAttribute("env", env);
			
			return getUrlPrefix() + "/edit";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	@RequestMapping("/view")
	public String view(HttpServletRequest request, HttpServletResponse response){
		try {
			commenEditAndView(request,response);
			ConfigUtils.setEnvType2Request(request);
			String tab = request.getParameter("tab");
			if(StringUtils.hasText(tab)){
				request.setAttribute("tab", tab);
			}
			return getUrlPrefix() + "/view";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	} 
	
	/**
	 * rest 请求 提供获取详情页面数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/dataInfo")
	@ResponseBody
	public JsonResponse dataInfo(@RequestParam String deployId) {
		try {
			
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			
			List<PaasProjectDeployPort> dps = iPaasProjectDeployService.findDeployPortByDeployId(deployId);
			pb.setPorts(dps);
			
			List<PaasProjectDeployVolumn> pdv = iPaasProjectDeployService.findDeployVolumnByDeployId(deployId);
			pb.setVolumn(pdv);
			
			List<PaasProjectDeployEnv> pde = iPaasProjectDeployService.findDeployEnvByDeployId(deployId);
			pb.setEnvVariable(pde);
			
			Map<String,Object> data = new HashMap<String,Object>();
			data.put("deploy", pb);
			
			return JsonResponse.success(data);
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	private void commenEditAndView(HttpServletRequest request, HttpServletResponse response){
		IUser user = WebUtil.getUserByRequest(request);
		PaasProject app = loadProjectModel(request);
		 
		
		List<PaasProjectBuild> paasProjectBuilds = iPaasProjectBuildService.findAvailableBuild(app.getProjectId());
		request.setAttribute("paasProjectBuilds", paasProjectBuilds);
		
		String deployId = request.getParameter("deployId");
		List<PaasProjectDeployPort> dps = iPaasProjectDeployService.findDeployPortByDeployId(deployId);
		request.setAttribute("pbs", dps);
		
		List<PaasProjectDeployVolumn> pdv = iPaasProjectDeployService.findDeployVolumnByDeployId(deployId);
		request.setAttribute("pdv", pdv);
		
		List<PaasProjectDeployEnv> pde = iPaasProjectDeployService.findDeployEnvByDeployId(deployId);
		request.setAttribute("pde", pde);
		
		PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
		request.setAttribute("paasProjectDeploy", pb);
		
		PaasOsImage poi = iPaasImageService.findById(pb.getImageId());
		request.setAttribute("poi", poi);
		
		PaasProjectBuild paasProjectBuild = iPaasProjectBuildService.findById(pb.getBuildId());
		request.setAttribute("paasProjectBuild", paasProjectBuild);
	}
	
	@RequestMapping("/deploy")
	@ResponseBody
	public JsonResponse deploy(HttpServletRequest request,
			HttpServletResponse response) {
		IUser user = WebUtil.getUserByRequest(request);
		String deployId = request.getParameter("deployId");
		try {
			//环境变量修改是否提示
			String variableChangeTip = request.getParameter("variableChangeTip");
			String changeVariable = request.getParameter("changeVariable");
			
			if(StringUtils.hasText(changeVariable) && "true".equals(changeVariable)){
				//是否替换新的环境变量  如果替换 后面直接启动
				iPaasProjectDeployService.changeLatestEnvVariables(deployId,user);
			}
			else{
				if(StringUtils.hasText(variableChangeTip) && "true".equals(variableChangeTip)){
					//判断环境变量是否修改过
					boolean bo = iPaasProjectDeployService.hasDifference(deployId, user);
					if(bo){
						throw new RuntimeException("-2");
					}
				}
			}
			
			PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
			PaasProject pp = iPaasProjectService.getPaasProjectInfoById(pd.getProjectId());
			
			
			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(deployId);
			OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:开始[进行中]", user.getUserId(), true);
			
			//看是否有空余空间去配额
			iPaasProjectDeployService.hasMoreResource(pd.getEnvType(), pp.getTenantId(), true,deployId);
			
			buildTask.setSessionId(OSLogFactory.getSessionValue());
			AsyncFactory.execute(new AsyncRunnable(buildTask, new CreateDeployCommand(), new CreateDeployCallBack()));
			return JsonResponse.success();
		}catch (Exception e) {
			logger.error("发布失败", e);
			request.setAttribute("exception", e);
			if(e.getMessage()!=null && e.getMessage().equals("-2")){
				return JsonResponse.failure(-2, "环境变量已经发生变化");
			}
			else {
				OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:结束[失败]["+e.getMessage()+"]", true);
				return JsonResponse.failure(500, e.getMessage());
			}
		}
	}
	
	@RequestMapping("/redeploy")
	@ResponseBody
	public JsonResponse redeploy(HttpServletRequest request,
			HttpServletResponse response) {
		IUser user = WebUtil.getUserByRequest(request);
		String deployId = request.getParameter("deployId");
		try {
			//环境变量修改是否提示
			String variableChangeTip = request.getParameter("variableChangeTip");
			String changeVariable = request.getParameter("changeVariable");
			
			if(StringUtils.hasText(changeVariable) && "true".equals(changeVariable)){
				//是否替换新的环境变量  如果替换 后面直接启动
				iPaasProjectDeployService.changeLatestEnvVariables(deployId,user);
			}
			else{
				if(StringUtils.hasText(variableChangeTip) && "true".equals(variableChangeTip)){
					//判断环境变量是否修改过
					boolean bo = iPaasProjectDeployService.hasDifference(deployId, user);
					if(bo){
						throw new RuntimeException("-2");
					}
				}
			}
			
			PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
			if(pd.getStatus()!=null && pd.getStatus().intValue()!=10){
				throw new RuntimeException("部署不处于运行状态，不能重启");
			}
			
			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(pd.getDeployId());
			OSLogFactory.add(deployId, OSLogFactory.STOP_OPERATOR, "停止部署:开始[进行中]", user.getUserId(), true);
			buildTask.setSessionId(OSLogFactory.getSessionValue());
			
			final String _deployId = deployId;
			final IUser _user = user;
			AsyncFactory.execute(new AsyncRunnable(buildTask, new DeleteDeployCommand(true), 
					new DeleteDeployCallBack(true,new AsyncCommand() {
						@Override
						public void execute(AsyncTask task) throws AsyncException {
							BuildTask buildTasks = new BuildTask();
							buildTasks.setTaskId(_deployId);
							OSLogFactory.add(_deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:开始[进行中]", _user.getUserId(), true);
							buildTasks.setSessionId(OSLogFactory.getSessionValue());
							AsyncFactory.execute(new AsyncRunnable(buildTasks, new CreateDeployCommand(), new CreateDeployCallBack()));
						}
			})));
			
			return JsonResponse.success();
		}catch (Exception e) {
			logger.error("发布失败", e);
			request.setAttribute("exception", e);
			if(e.getMessage()!=null && e.getMessage().equals("-2")){
				return JsonResponse.failure(-2, "环境变量已经发生变化");
			}
			else {
				OSLogFactory.add(deployId, OSLogFactory.DEPLOY_OPERATOR, "应用部署:结束[失败]["+e.getMessage()+"]", true);
				return JsonResponse.failure(500, e.getMessage());
			}
		}
	}
	
	@RequestMapping("/stop")
	@ResponseBody
	public JsonResponse stop(HttpServletRequest request,
			HttpServletResponse response) {
		String deployId = request.getParameter("deployId");
		try {
			IUser user = WebUtil.getUserByRequest(request);
			
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			//1停止  5启动中 6变更中 7停止中 10运行中 11删除失败 0启动失败
			if(pb.getStatus()!=null && pb.getStatus().intValue()!=10 && pb.getStatus().intValue()!=11){
				throw new RuntimeException("部署不处于运行或者删除失败状态，不能停止");
			}
			
			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(pb.getDeployId());
			OSLogFactory.add(deployId, OSLogFactory.STOP_OPERATOR, "停止部署:开始[进行中]", user.getUserId(), true);
			buildTask.setSessionId(OSLogFactory.getSessionValue());
			
			AsyncFactory.execute(new AsyncRunnable(buildTask, new DeleteDeployCommand(true), new DeleteDeployCallBack(true)));
			
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getPods")
	@ResponseBody
	public JsonResponse getPods(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			List<PaasProjectDeployPort> resultPorts = iPaasProjectDeployService.findDeployPortByDeployId(deployId);
			//1停止  5启动中 6变更中 7停止中 10运行中 11删除失败 0启动失败
//			if(pb.getStatus()!=null && pb.getStatus().intValue()!=10 && pb.getStatus().intValue()!=11){
//				throw new RuntimeException("部署不处于运行或者删除失败状态，不能停止");
//			}
			Map<String,String> labels = new HashMap<String, String>();
			labels.put("serviceId", pb.getDeployId());
			if(StringUtils.hasText(pb.getDeployTime())){
				labels.put("deployTime", pb.getDeployTime());
			}
			PodList pl = KubernetesUtil.getPods(pb.getTenantId(), labels);
			pl.getAdditionalProperties().put("publicIp", pb.getPublicIp());
			pl.getAdditionalProperties().put("serviceIp", pb.getServiceIp());
			pl.getAdditionalProperties().put("ports", resultPorts);
			pl.getAdditionalProperties().put("deployId", deployId);
			
			 
			String webssh =  KubernetesUtil.getWebSSH(pb.getTenantId(), "podName", "containerId");
			pl.getAdditionalProperties().put("webssh", webssh);
			return JsonResponse.success(pl,"获取pods信息成功");
		} catch (Exception e) {
			logger.error("获取pod信息失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getPodLog")
	@ResponseBody
	public JsonResponse getPodLog(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String podName = request.getParameter("podName");
			String deployId = request.getParameter("deployId");
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			Map<String,String> labels = new HashMap<String, String>();
			String msg = KubernetesUtil.getPodLogs(pb.getTenantId(), podName);
			return JsonResponse.success(msg,"获取pods信息成功");
		} catch (Exception e) {
			logger.error("获取pod日志信息失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getPodEvent")
	@ResponseBody
	public JsonResponse getPodEvent(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String podName = request.getParameter("podName");
			String deployId = request.getParameter("deployId");
			Map<String,String> labels = new HashMap<String, String>();
			String tenantId = null;
			if(StringUtil.isNotNull(deployId)){
				PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
				tenantId = pb.getTenantId();
			}else{
				String namespace = request.getParameter("namespace");
				tenantId = namespace;
			}
			EventList msg = KubernetesUtil.getPodEventLogs(tenantId, podName);
			return JsonResponse.success(msg,"获取pods事件信息成功");
		} catch (Exception e) {
			logger.error("获取pod事件日志信息失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/deployInfo")
	@ResponseBody
	public JsonResponse deployInfo(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			
			List<PaasProjectDeployPort> dps = iPaasProjectDeployService.findDeployPortByDeployId(deployId);
			pb.setPorts(dps);
			
			List<PaasProjectDeployVolumn> pdv = iPaasProjectDeployService.findDeployVolumnByDeployId(deployId);
			pb.setVolumn(pdv);
			
			List<PaasProjectDeployEnv> pde = iPaasProjectDeployService.findDeployEnvByDeployId(deployId);
			pb.setEnvVariable(pde);
			
			return JsonResponse.success(pb,"获取部署信息成功");
		} catch (Exception e) {
			logger.error("获取deploy信息失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/saveReplicas")
	@ResponseBody
	public JsonResponse saveReplicas(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String deployId = request.getParameter("deployId");
			String replicas = request.getParameter("replicas");
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deployId);
			PaasProject project = iPaasProjectService.getPaasProjectInfoById(pb.getProjectId());
			if(pb.getStatus()!=null && pb.getStatus().intValue()!=10){
				throw new RuntimeException("部署状态不为运行状态不能扩容");
			}
			Integer oldReplicas = pb.getReplicas();
			
			//计算配额
			Integer repli = Integer.parseInt(replicas);
			ResourceQuota apply = new ResourceQuota();
			apply.setCpu(pb.getCpu()*repli);
			apply.setMemory(pb.getMemory() * repli);
			apply.setStorage(0.0);
			apply.setCount(Long.valueOf(repli));
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("tenantId", project.getTenantId());
			//不算上本次的
			param.put("deployId", deployId);
			ResourceQuota used = iPaasProjectDeployService.getAlreadyUsed(param);
			TwoTuple<Boolean, String> result = ResourceQuotaUtil.isSatisfy(apply, used, null, false, 
					pb.getEnvType(), project.getTenantId(), "deploy","docker");
			if(!result.a){
				throw new RuntimeException("租户配额不足:"+result.b);
			}
			
			
			
			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(pb.getDeployId());
			buildTask.setScale(repli);
			
			OSLogFactory.add(deployId, OSLogFactory.KUORONG_OPERATOR, "扩容部署:开始[进行中  "+oldReplicas+"--->"+replicas+"]", user.getUserId(), true);
			buildTask.setSessionId(OSLogFactory.getSessionValue());
			
			AsyncFactory.execute(new AsyncRunnable(buildTask, new ReplicasDeployCommand(), new ReplicasDeployCallBack()));
			return JsonResponse.success(pb,"部署扩容成功");
		} catch (Exception e) {
			logger.error("部署扩容失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	/**
	 *  没用到
	 * @param request
	 * @param response
	 * @param deploy
	 * @return
	 */
	@RequestMapping("/addVolumn")
	@ResponseBody
	public JsonResponse addVolumn(HttpServletRequest request,
			HttpServletResponse response,@RequestBody PaasProjectDeploy deploy) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			PaasProjectDeploy pb = iPaasProjectDeployService.findById(deploy.getDeployId());
			pb.setVolumn(deploy.getVolumn());
			if(pb.getStatus()!=null && pb.getStatus().intValue()!=10){
				throw new RuntimeException("部署状态不为运行状态不能新增数据卷");
			}
			
			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(pb.getDeployId());
			AsyncFactory.execute(new AsyncRunnable(buildTask, new VolumnChangeDeployCommand(pb), new VolumnChangeDeployCallBack(pb)));
			return JsonResponse.success(pb,"部署扩容成功");
		} catch (Exception e) {
			logger.error("部署扩容失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("getDeployByProjectId")
	@ResponseBody
	public List<PaasProjectDeploy> getDeployByProjectId(String projectId){
		PaasProjectDeploy deploy = new PaasProjectDeploy();
		deploy.setProjectId(projectId);
		return iPaasProjectDeployService.findDeployByEntity(deploy);
	}
	
	
	@RequestMapping("/version")
	@ResponseBody
	public JsonResponse version(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> r = new HashMap<String, Object>();
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String projectId = request.getParameter("projectId");
			String versionId = request.getParameter("versionId");
			String deployId = request.getParameter("deployId");
			PaasProjectDeploy deploy = null;
			if(StringUtils.isEmpty(deployId)){
				deploy = new PaasProjectDeploy();
				deploy.setEnvType(request.getParameter("envType"));
				deploy.setEnvId(request.getParameter("envId"));
			}
			else{
				deploy = iPaasProjectDeployService.findById(deployId);
			}
			DeployResource dr = iPaasProjectEnvService.getDeployResouceAllByVersionId(projectId, versionId, user.getUserId(),deploy);
			return JsonResponse.success(dr);
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	/**
	 * 判断是否需要更新环境变量
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/changeEnvs")
	@ResponseBody
	public JsonResponse changeEnvs(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> r = new HashMap<String, Object>();
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String projectId = request.getParameter("projectId");
			String deployId = request.getParameter("deployId");
			boolean bo = iPaasProjectDeployService.hasDifference(deployId, user);
			return JsonResponse.success(bo);
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/reloadEnv")
	@ResponseBody
	public JsonResponse reloadEnv(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> r = new HashMap<String, Object>();
		IUser user = WebUtil.getUserByRequest(request);
		try {
			String deployId = request.getParameter("deployId");
			String versionId = request.getParameter("versionId");
			
			PaasProjectDeploy deploy = iPaasProjectDeployService.findById(deployId);
			
			DeployResource dr = iPaasProjectEnvService.getDeployResouceAllByEnvId(deploy.getEnvId(), user.getUserId(),deployId);
			DeployResource dr1 = iPaasProjectEnvService.getDeployResouceAllByVersionId(deploy.getProjectId(), versionId, user.getUserId(),deploy);
			
			DeployResource result = new DeployResource();
			if(dr!=null && !CollectionUtils.isEmpty(dr.getEnvs())){
				result.getEnvs().addAll(dr.getEnvs());
			}
			if(dr1!=null && !CollectionUtils.isEmpty(dr1.getEnvs())){
				result.getEnvs().addAll(dr1.getEnvs());
			}
			return JsonResponse.success(result);
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
				String deployTime = p.getMetadata().getLabels().get("deployTime");
				if(StringUtil.hasText(deployTime)){
					PaasProjectDeploy pd = iPaasProjectDeployService.findById(deployId);
					if(StringUtil.hasText(pd.getDeployTime())){
						if(!pd.getDeployTime().equals(deployTime)){
							continue ;
						}
					}
				}
				
				if(!ref.containsKey(deployId)){
					DeployStatus ds = new DeployStatus();
					ds.setDeployId(deployId);
					result.add(ds);
					ref.put(deployId, ds);
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
	
	@RequestMapping("/{envId}/deploy/ports")
	@ResponseBody
	public JsonResponse deployPorts(@PathVariable String envId,HttpServletRequest request,
			HttpServletResponse response){
		Map<String,Object> result = new HashMap<String, Object>();
		try{
			List<PaasProjectDeployPort> ports = iPaasProjectDeployService.findDeployPortByEnvIdForNginx(envId);
			result.put("ports", ports);
			return JsonResponse.success(result);
		}
		catch(Exception e){
			logger.error("发布失败", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
}
