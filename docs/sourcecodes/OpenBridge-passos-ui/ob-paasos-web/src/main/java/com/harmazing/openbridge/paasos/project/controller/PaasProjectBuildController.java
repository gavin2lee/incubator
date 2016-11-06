package com.harmazing.openbridge.paasos.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.WebUtil;
import com.harmazing.openbridge.paas.deploy.model.PaasProjectDeploy;
import com.harmazing.openbridge.paasos.imgbuild.BuildTask;
import com.harmazing.openbridge.paasos.imgbuild.BuildTaskType;
import com.harmazing.openbridge.paasos.imgbuild.dao.PaasOsImageMapper;
import com.harmazing.openbridge.paasos.imgbuild.model.PaasOsImage;
import com.harmazing.openbridge.paasos.project.build.ProjectBuilder;
import com.harmazing.openbridge.paasos.project.model.PaasProject;
import com.harmazing.openbridge.paasos.project.model.PaasProjectBuild;
import com.harmazing.openbridge.paasos.project.model.vo.BuildImagePort;
import com.harmazing.openbridge.paasos.project.model.vo.BuildVersion;
import com.harmazing.openbridge.paasos.project.model.vo.BuildVersionFile;
import com.harmazing.openbridge.paasos.project.model.vo.TwoTuple;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectBuildService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectDeployService;
import com.harmazing.openbridge.paasos.project.service.IPaasProjectService;

@Controller
@RequestMapping("/project/build")
public class PaasProjectBuildController extends ProjectBaseController {
	
	
	
	@Autowired
	private IPaasProjectService iPaasProjectService;
	
	@Autowired
	private IPaasProjectBuildService iPaasProjectBuildService;
	
	@Autowired
	private IPaasProjectDeployService iPaasProjectDeployService;
	
	@Autowired
	private ProjectBuilder projectBuilder;
	
	@Autowired
	private PaasOsImageMapper paasOsImageMapper;
	
	
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
	
	@RequestMapping("/create")
	public String create(HttpServletRequest request, HttpServletResponse response){
		PaasProject app = loadProjectModel(request);
		try {
			IUser user = WebUtil.getUserByRequest(request);
			
			 
			
			if(app.getProjectType().equals("app") || app.getProjectType().equals("api")){
				TwoTuple<List<BuildVersion>,String> tt = iPaasProjectService.findBuildVersions(app.getBusinessId(),app.getProjectType(),user.getUserId());
				/**
				 * update by weiyujia @2016/5/31 
				 * 过滤构建版本列表下拉框，已被创建过镜像的则过滤掉
				 */
				List<Map<String,Object>> existImageList = iPaasProjectBuildService.findAlreadBuildVersionList(app.getProjectId());
				filterResult(tt, existImageList);
				request.setAttribute("buildVersion", tt.a);
				Object jo = JSONObject.toJSON(tt.a);
				if(jo!=null){
					JSONArray ja = (JSONArray)jo;
					request.setAttribute("buildVersionStr", ja.toJSONString());
				}
				
				String dockerRegistry = ConfigUtil.getConfigString("paasos.docker.registry");
				// TODO 修改基础镜像
				String imageSource = tt.b;
				if (!imageSource.startsWith("/")) {
					imageSource = "/" + imageSource;
				}
				String imageName = dockerRegistry + imageSource;
				PaasOsImage poi = paasOsImageMapper.findImageByName(imageName, imageSource.split(":")[1]);
				if(StringUtils.hasText(poi.getPorts())){
					List<BuildImagePort> bp = JSONArray.parseArray(poi.getPorts(),BuildImagePort.class);
					request.setAttribute("imagePort", bp);
				}
				
			}
			
			
			return getUrlPrefix() + "/create";
		} catch (Exception e) {
			logger.error("失败", e);
			request.getSession().setAttribute("exception", StringUtils.hasText(e.getMessage())?e.getMessage():"获取数据失败");
			//return forward(ERROR);
			return redirect(request,getUrlPrefix() + "/index.do?projectId="+app.getProjectId());
		}
	}
	
	/**
	* @Title: filterResult 
	* @author weiyujia
	* @Description: 过滤数据
	* @param tt  数据源
	* @param versionList  已存在的镜像
	* @throws 
	* @date 2016年6月1日 上午11:19:46
	 */
	private void filterResult(TwoTuple<List<BuildVersion>, String> tt, List<Map<String, Object>> existImageList) {
		if(existImageList != null && existImageList.size() > 0){
			List<BuildVersion> buildVersionsList = tt.a;
			for(BuildVersion buildVersion : buildVersionsList){
				List<BuildVersionFile> buildVersionFilesList = buildVersion.getFiles();
				removeExistVersion(buildVersionFilesList,existImageList,buildVersion.getVersionName());
			}
		}
	}

	/**
	* @Title: removeExistVersion 
	* @author weiyujia
	* @Description: 
	* @param buildVersionFilesList
	* @param existImageList  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年6月1日 上午11:43:08
	 */
	private void removeExistVersion(List<BuildVersionFile> buildVersionFilesList, List<Map<String, Object>> existImageList,String versionName) {
		List<BuildVersionFile> resultList = new ArrayList<BuildVersionFile>();
		for(BuildVersionFile buildVersionFile : buildVersionFilesList){
			String buildTag = versionName + "-" + buildVersionFile.getBuildNo();
			for(Map<String,Object> map : existImageList){
				if(buildTag.equals(map.get("buildTag"))){
					resultList.add(buildVersionFile);
					break;
				}
			}
		}
		buildVersionFilesList.removeAll(resultList);
	}

	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			
			PaasProject app = loadProjectModel(request); 
			
			String viewType = request.getParameter("viewType");
//			String listType = request.getParameter("listType");
			String keyword = request.getParameter("keyword");
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 10);
			String projectId = request.getParameter("projectId");
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("pageNo", pageNo);
			params.put("pageSize", pageSize);
			params.put("userId", user.getUserId());
			params.put("keyword", keyword);
			params.put("projectId", projectId);
//			params.put("listType", listType);
			Page<Map<String, Object>> pageDate = iPaasProjectBuildService.page(params);
			request.setAttribute("pageData", pageDate);
			return getUrlPrefix() + "/list/" + viewType;
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	/**
	 * 提供给api 和app查询 状态使用
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/data")
	@ResponseBody
	public JsonResponse data(HttpServletRequest request, HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			
			String businessId = request.getParameter("businessId");
			String versionId = request.getParameter("versionId");
			String buildNo = request.getParameter("buildNo");
			Map<String,String> param = new HashMap<String,String>();
			if(StringUtils.hasText(businessId)){
				param.put("businessId", businessId);
			}
			if(StringUtils.hasText(versionId)){
				param.put("versionId", versionId);
			}
			if(StringUtils.hasText(buildNo)){
				param.put("buildNo", buildNo);
			}
			List<Map<String,Object>> result = iPaasProjectBuildService.data(param);
			return JsonResponse.success(result);
		} catch (Exception e) {
			logger.error("获取镜像信息失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/save")
	@ResponseBody
	public JsonResponse save(HttpServletRequest request,
			HttpServletResponse response, PaasProjectBuild build) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			boolean isUpdate = true;
			if(StringUtils.isEmpty(build.getBuildId())){
				isUpdate  = false;
			}
			PaasProject paasProject = null;
			if(StringUtils.hasText(build.getBusinessId())){
				paasProject  = iPaasProjectService.getPaasProjectByBusinessIdAndType(build.getBusinessId(), build.getBusinessType());
				if(paasProject==null){
					throw new RuntimeException("该businessId只提供给app和api使用,请检查关联项目必须为1个");
				}
			}
			else{
				paasProject = iPaasProjectService.getPaasProjectInfoById(build.getProjectId());
			}
			if(StringUtils.isEmpty(build.getProjectId())){
				build.setProjectId(paasProject.getProjectId());
			}
			iPaasProjectBuildService.save(user,build);
			if(!isUpdate){
				BuildTask buildTask = new BuildTask();
				buildTask.setTaskId(build.getBuildId());
				buildTask.setCurrentUserId(user.getUserId());
				buildTask.setProjectType(paasProject.getProjectType());
				buildTask.setVersionId(build.getVersionId());
				buildTask.setBusinessId(paasProject.getBusinessId());
				
				buildTask.setTaskType(BuildTaskType.ZIP);
//				buildTask.setFilePath(build.getFilePath());
				buildTask.setFileName(build.getFileName());
				buildTask.setDockerfile(build.getDockerFile());
				buildTask.setBuildParam(build.getBuildParam());
				if(paasProject.getProjectType()!=null && (
						"api".equals(paasProject.getProjectType()) || "app".equals(paasProject.getProjectType()))){
					String url = ConfigUtil.getConfigString("paasos."+paasProject.getProjectType()+".url");
					if(url.endsWith("/")){
						url =  url.substring(0,url.length()-1);
					}
					String remotePath = build.getFilePath().startsWith("/") ? build.getFilePath():( "/"+build.getFilePath());
					remotePath = url+remotePath;
					buildTask.setFilePath(remotePath);
				}
				else{
					buildTask.setFilePath(build.getFilePath());
				}
				buildTask.setImageName(IPaasProjectBuildService.IMAGE_PREFIX+build.getBuildName());
				buildTask.setImageVersion(build.getBuildTag());
				buildTask.setImagePort(build.getPort());
				buildTask.setImageSource(build.getImageName());
//				PaasRunnable pr = new PaasRunnable(buildTask,
//						imageBuildCommand,imageBuildCallBack,true);
//				TaskFactory.execute(pr);
				projectBuilder.create(buildTask);
			}
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
			IUser user = WebUtil.getUserByRequest(request);
			String buildId = request.getParameter("buildId");
			PaasProjectBuild pb = iPaasProjectBuildService.findById(buildId);
			
			
			
			// 1已保存  5创建中 10创建成功 0创建失败   
			if(pb.getStatus()!=null && pb.getStatus().intValue()==5){
				throw new RuntimeException("镜像处于创建中，不能删除");
			}
			if(pb.getDeleteStatus()!=null && pb.getDeleteStatus().intValue()==1){
				throw new RuntimeException("镜像处于删除中，不能删除");
			}
			PaasProjectDeploy param = new PaasProjectDeploy();
			param.setBuildId(buildId);
			List<PaasProjectDeploy> pds = iPaasProjectDeployService.findDeployByEntity(param);
			if(pds!=null && pds.size()>0){
				throw new RuntimeException("该构建镜像已经有部署在使用，请先删除部署");
			}
			if(pb.getStatus()!=null && ( pb.getStatus().intValue()==1 || pb.getStatus().intValue()== 0) ){
				//处于1和0直接删除表中记录
				iPaasProjectBuildService.deleteById(buildId);
			}
			else{
				PaasProject paasProject = iPaasProjectService.getPaasProjectInfoById(pb.getProjectId());
				BuildTask buildTask = new BuildTask();
				buildTask.setTaskId(pb.getBuildId());
				buildTask.setBuildImageId(pb.getImageId());
				buildTask.setImageName(IPaasProjectBuildService.IMAGE_PREFIX+pb.getBuildName());
				buildTask.setImageVersion(pb.getBuildTag());
				buildTask.setProjectType(paasProject.getProjectType());
//				PaasRunnable pr = new PaasRunnable(buildTask,
//						imageBuildDeleteCommand,imageBuildDeleteCallBack,true);
//				TaskFactory.execute(pr);
				projectBuilder.delete(buildTask);
			}
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/edit")
	public String edit(HttpServletRequest request, HttpServletResponse response){
		try {
			commenEditAndView(request,response,true);
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
			commenEditAndView(request,response,false);
			return getUrlPrefix() + "/view";
		} catch (Exception e) {
			logger.error("失败", e);
			request.setAttribute("exception", e);
			return forward(ERROR);
		}
	}
	
	private void commenEditAndView(HttpServletRequest request, HttpServletResponse response,boolean connectApi){
		IUser user = WebUtil.getUserByRequest(request);
		PaasProject app = loadProjectModel(request);
		 
		
		if(connectApi && (app.getProjectType().equals("app") || app.getProjectType().equals("api"))){
			TwoTuple<List<BuildVersion>,String> tt = iPaasProjectService.findBuildVersions(app.getBusinessId(),app.getProjectType(),user.getUserId());
			request.setAttribute("buildVersion", tt.a);
			Object jo = JSONObject.toJSON(tt.a);
			if(jo!=null){
				JSONArray ja = (JSONArray)jo;
				request.setAttribute("buildVersionStr", ja.toJSONString());
			}
		}
		String buildId = request.getParameter("buildId");
		PaasProjectBuild pb = iPaasProjectBuildService.findById(buildId);
		request.setAttribute("paasProjectBuild", pb);
	}
	
	@RequestMapping("/getPort")
	@ResponseBody
	public String getPort(HttpServletRequest request, HttpServletResponse response){
		String buildId = request.getParameter("buildId");
		PaasProjectBuild pb = iPaasProjectBuildService.findById(buildId);
		return pb.getPort();
	}
	
	@RequestMapping("/build")
	@ResponseBody
	public JsonResponse build(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String buildId = request.getParameter("buildId");
			PaasProjectBuild build = iPaasProjectBuildService.findById(buildId);
			
			PaasProject paasProject = iPaasProjectService.getPaasProjectInfoById(build.getProjectId());
			if(build.getDeleteStatus()!=null){
				throw new RuntimeException("该构建被删除过,不能重新构建");
			}
			if(build.getStatus()==null || (build.getStatus().intValue()!=1 && build.getStatus().intValue()!=0)){
				throw new RuntimeException("该构建不处于保存和失败状态,不能重新构建");
			}
			
			BuildTask buildTask = new BuildTask();
			buildTask.setTaskId(build.getBuildId());
			buildTask.setCurrentUserId(user.getUserId());
			buildTask.setProjectType(paasProject.getProjectType());
			buildTask.setTaskType(BuildTaskType.ZIP);
			buildTask.setFileName(build.getFileName());
			buildTask.setDockerfile(build.getDockerFile());
			buildTask.setVersionId(build.getVersionId());
			buildTask.setBusinessId(paasProject.getBusinessId());
			if(paasProject.getProjectType()!=null && (
					"api".equals(paasProject.getProjectType()) || "app".equals(paasProject.getProjectType()))){
				String url = ConfigUtil.getConfigString("paasos."+paasProject.getProjectType()+".url");
				if(url.endsWith("/")){
					url =  url.substring(0,url.length()-1);
				}
				String remotePath = build.getFilePath().startsWith("/") ? build.getFilePath():( "/"+build.getFilePath());
				remotePath = url+remotePath;
				buildTask.setFilePath(remotePath);
			}
			else{
				buildTask.setFilePath(build.getFilePath());
			}
			buildTask.setImageSource(build.getImageName());
			buildTask.setImageName(IPaasProjectBuildService.IMAGE_PREFIX+build.getBuildName());
			buildTask.setImageVersion(build.getBuildTag());
			buildTask.setImagePort(build.getPort());
			projectBuilder.create(buildTask);
			
			return JsonResponse.success();
		} catch (Exception e) {
			logger.error("服务列表页面出错", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	@RequestMapping("/getLogs")
	@ResponseBody
	public JsonResponse getLogs(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			IUser user = WebUtil.getUserByRequest(request);
			String buildId = request.getParameter("buildId");
			PaasProjectBuild build = iPaasProjectBuildService.findById(buildId);
			return JsonResponse.success(build.getBuildLogs());
		} catch (Exception e) {
			logger.error("获取构建日志失败", e);
			request.setAttribute("exception", e);
			return JsonResponse.failure(500, e.getMessage());
		}
	}
	
	/**
	 * 
	 * getBuildImageByVersion:(提供给app和api平台调用). <br/>
	 *
	 * @author dengxq
	 * @param versionId
	 * @param versionCode
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping("/getBuildImageByVersion")
	@ResponseBody
	public Map<String,Object> getBuildImageByVersion(@RequestParam String versionId,
			@RequestParam String versionCode,@RequestParam String businessId,@RequestParam String businessType){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			List<Map<String,Object>> data = iPaasProjectBuildService.findByVersionId(versionId);
			result.put("data", data);
			result.put("code", 0);
			return result;
		} catch (Exception e) {
			logger.error("获取构建日志失败", e);
			result.put("code", -1);
			result.put("info", StringUtils.hasText(e.getMessage())?e.getMessage():"根据版本查找失败");
			return result;
		}
	}
	
	/**
	 * 
	 * getBuildImageByBusinessId:(提供给app和api平台调用). <br/>
	 *
	 * @author dengxq
	 * @param versionId
	 * @param versionCode
	 * @return
	 * @since JDK 1.6
	 */
	@RequestMapping("/getBuildImageByBusinessId")
	@ResponseBody
	public Map<String,Object> getBuildImageByBusinessId(@RequestParam String businessId,@RequestParam String businessType){
		Map<String,Object> result = new HashMap<String, Object>();
		try {
			Map<String,List<Map<String,String>>> data = iPaasProjectBuildService.getBuildImageByBusinessId(businessId);
			result.put("data", data);
			result.put("code", 0);
			return result;
		} catch (Exception e) {
			logger.error("获取构建日志失败", e);
			result.put("code", -1);
			result.put("info", StringUtils.hasText(e.getMessage())?e.getMessage():"根据版本查找失败");
			return result;
		}
	}
}
