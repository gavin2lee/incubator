package com.harmazing.openbridge.paasos.manager.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.imgbuild.BuildStatus;
import com.harmazing.openbridge.paasos.manager.model.PaaSBaseBuild;
import com.harmazing.openbridge.paasos.manager.service.IPaaSBaseBuildService;
import com.harmazing.openbridge.paasos.project.build.ProjectBuilder;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;
import com.harmazing.openbridge.web.fileupload.FileUploadHandler;

@Controller
@RequestMapping("/manager/baseimage")
public class PaasBaseImageController extends AbstractController {
	@Resource
	private IPaaSBaseBuildService paasBaseBuildService;
	@Resource
	private ProjectBuilder projectBuilder;
	
	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		
		return getUrlPrefix() + "/list";
	}
	@RequestMapping("/add_image")
	public String addImage(HttpServletRequest request, HttpServletResponse response,String id) {
		PaaSBaseBuild build = null;
		if(StringUtil.isNotNull(id)){
			build = paasBaseBuildService.getById(id);
			build.setFileData(JavaScriptUtils.javaScriptEscape(build.getFileData()));
			build.setCommand(HtmlUtils.htmlEscape(build.getCommand()));
		}
		request.setAttribute("build", build);
		if(build !=null && (build.getStatus().equals(BuildStatus.SAVED) || build.getStatus().equals(BuildStatus.BUILD_FAIL))){
			return getUrlPrefix() + "/add_image";
		}else{
			if(StringUtil.isNotNull(id)){
				return getUrlPrefix() + "/update_image_base_info";
			}else{
				return getUrlPrefix() + "/add_image";
			}
		}
		
	}
	@RequestMapping("/imageTable")
	public String imageTable(HttpServletRequest request, HttpServletResponse response,
			Integer pageNo,Integer pageSize, String viewType) {
		if(pageNo==null)	pageNo = 1;
		if(pageSize==null)	pageSize = 10;
		if(StringUtil.isNull(viewType))	viewType = "table";
		Page<PaaSBaseBuild> page = paasBaseBuildService.getPage(pageNo, pageSize);
		request.setAttribute("page", page);
		if(viewType.equals("table")){
			return getUrlPrefix() + "/imageTable";
		}else{
			return getUrlPrefix() + "/imagePreview";
		}
	}
	
	@RequestMapping(value="saveOrUpdate",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse saveOrUpdate(PaaSBaseBuild build,HttpServletRequest request){
		
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			if(build!=null){
				if(StringUtil.isNull(build.getId())){
					build.setId(StringUtil.getUUID());
				}
				if(StringUtil.isNull(build.getId())){
					build.setId(StringUtil.getUUID());
				}
				MultipartFile buildFile = null;
				try{
					MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)request;
					buildFile = multipartRequest.getFile("buildFile");
				}catch(Exception e){
					
				}
				if(buildFile!=null && !buildFile.isEmpty()){
					boolean check = true;
					String fileName = buildFile.getOriginalFilename();
					int lastIndexOf = fileName.lastIndexOf(".");
					if(lastIndexOf==-1){
						check = false;
					}
					String suffix = fileName.substring(lastIndexOf+1);
					if(!suffix.equalsIgnoreCase(build.getFileType())){
						check = false;
					}
					if(!check){
						jsonResponse = JsonResponse.failure(1,"请上传 “"+build.getFileType()+"” 类型文件！");
						return jsonResponse;
					}
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String filePath = ConfigUtil.getConfigString("file.storage")+File.separator+
							df.format(new Date())+File.separator+build.getId();
					File file = new File(filePath);
					if(!file.exists()){
						file.mkdirs();
					}
					file = new File(filePath+File.separator+fileName);
					file.createNewFile();
					buildFile.transferTo(file);
					build.setFilePath(file.getAbsolutePath());
				}
				paasBaseBuildService.saveOrUpdate(build);
			}
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
		
	}
	

	@RequestMapping(value="updateBaseInfo",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse updateBaseInfo(PaasStoreApp build,HttpServletRequest request){
		JsonResponse jsonResponse = JsonResponse.success();
		try {
			paasBaseBuildService.updateLogoAndDesc(build.getId(), build.getIconPath(), build.getDescription(),build.getTenantIds(),build.getDockerfile());
		} catch (Exception e) {
			jsonResponse = JsonResponse.failure(500, e.getMessage());
		}
		return jsonResponse;
	}
	
	@RequestMapping(value="delete",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse delete(String id){
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			if(StringUtil.isNotNull(id)){
				paasBaseBuildService.delete(id);
			}
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
	@RequestMapping(value="build",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse build(String id,HttpServletRequest request){
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			if(StringUtil.isNotNull(id)){
				paasBaseBuildService.build(id, getCurrentUser(request));
			}
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
	
	@RequestMapping("download")
	public void download(String id,HttpServletResponse response){
		if(StringUtil.isNotNull(id)){
			PaaSBaseBuild paasBaseBuild = paasBaseBuildService.getById(id);
			if(paasBaseBuild!=null && StringUtil.isNotNull(paasBaseBuild.getFilePath())){
				String filePath = paasBaseBuild.getFilePath();
				FileUploadHandler.download(response, paasBaseBuild.getFileName(), filePath);
			}
		}
	}
	
	@RequestMapping("log")
	@ResponseBody
	public Map<String, String> log(String id){
		String log = paasBaseBuildService.getBuildLog(id);
		Map<String, String> map  = new HashMap<String, String>();
		map.put("log", log);
		return map;
	}
	
	@RequestMapping("view")
	public String view(String id,HttpServletRequest request){
		request.setAttribute("build", paasBaseBuildService.getById(id));
		return getUrlPrefix() + "/view";
	}
	
	@RequestMapping("getByName")
	@ResponseBody
	public List<PaaSBaseBuild> getByName(String name){
		
		return paasBaseBuildService.getByName(name);
	}
	
	@RequestMapping("checkNameAndVersionExist")
	public void checkNameAndVersionExist(String name,String version,String id,HttpServletResponse response){
		int count  = paasBaseBuildService.existNameAndVersion(name, version, id);
		try {
			response.getOutputStream().print(count==0);
		} catch (IOException e) {
			logger.debug(e);
		}
		
	}
}
