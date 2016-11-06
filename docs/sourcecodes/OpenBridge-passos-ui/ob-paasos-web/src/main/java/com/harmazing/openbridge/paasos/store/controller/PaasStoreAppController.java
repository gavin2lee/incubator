package com.harmazing.openbridge.paasos.store.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.JavaScriptUtils;

import com.harmazing.framework.authorization.IUser;
import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.common.controller.AbstractController;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.ExceptionUtil;
import com.harmazing.framework.util.FileUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.paasos.imgbuild.BuildStatus;
import com.harmazing.openbridge.paasos.store.model.PaasStoreApp;
import com.harmazing.openbridge.paasos.store.service.IPaasStoreAppService;
import com.harmazing.openbridge.web.fileupload.FileUploadHandler;

@Controller
@RequestMapping("/store/app")
public class PaasStoreAppController extends AbstractController {

	@Autowired
	private IPaasStoreAppService paasStoreAppService;

	@RequestMapping("/list")
	public String list(HttpServletRequest request, HttpServletResponse response) {
		return getUrlPrefix() + "/list";
	}
	@RequestMapping("/items")
	public String items(HttpServletRequest request, HttpServletResponse response,
			Integer pageNo,Integer pageSize, String viewType,String keyword) {
		if(pageNo==null)	pageNo = 1;
		if(pageSize==null)	pageSize = 10;
		if(StringUtil.isNull(viewType))	viewType = "table";
		
		IUser user = getCurrentUser(request);
		Page<PaasStoreApp> page = paasStoreAppService.getPage(pageNo, pageSize,keyword,user);
		request.setAttribute("page", page);
		
//		paasStoreAppService.getPage(pageNo, pageSize,keyword);
		if(viewType.equals("table")){
			return getUrlPrefix() + "/table";
		}else{
			return getUrlPrefix() + "/preview";
		}
	}

	@RequestMapping("/info")
	public String info(HttpServletRequest request, HttpServletResponse response) {
		return getUrlPrefix() + "/info";
	}
	@RequestMapping(value={"/add_app"})
	public String addApp(HttpServletRequest request, HttpServletResponse response,String id) {
		PaasStoreApp build = null;
		if(StringUtil.isNotNull(id)){
			build = paasStoreAppService.getById(id);
			build.setConfig(JavaScriptUtils.javaScriptEscape(build.getConfig()));
			build.setFileData(JavaScriptUtils.javaScriptEscape(build.getFileData()));
			build.setCommand(HtmlUtils.htmlEscape(build.getCommand()));
		}
		request.setAttribute("presetApp", build);
		if(build!=null && (build.getStatus().equals(BuildStatus.SAVED) || build.getStatus().equals(BuildStatus.BUILD_FAIL))){
			return getUrlPrefix() + "/add_app";
		}else{
			if(StringUtil.isNotNull(id)){
				return getUrlPrefix() + "/update_app_base_info";
			}else{
				return getUrlPrefix() + "/add_app";
			}
		}
	}
	@RequestMapping(value="saveOrUpdate",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse saveOrUpdate(PaasStoreApp build,HttpServletRequest request){
		
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			if(build!=null){
				if(StringUtil.isNull(build.getId())){
					build.setId(StringUtil.getUUID());
				}
				IUser user = getCurrentUser(request);
				paasStoreAppService.saveOrUpdate(build,user);
			}
		}catch(Exception e){
			logger.error(e);
			jsonResponse = JsonResponse.failure(1, e.getMessage());
		}
		return jsonResponse;
		
	}
	
	
	@RequestMapping(value="updateBaseInfo",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse updateBaseInfo(PaasStoreApp build,HttpServletRequest request){
		JsonResponse jsonResponse = JsonResponse.success();
		try {
//			paasStoreAppService.updateLogoAndDesc(build.getId(), build.getIconPath(), build.getDescription(),
//					build.getAppName(),build.getDocumentation(),build.getDockerfile());
			paasStoreAppService.updateLogoAndDesc(build);
		} catch (Exception e) {
			logger.warn(e);
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
				paasStoreAppService.delete(id);
			}
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, e.getMessage());
		}
		return jsonResponse;
	}
	@RequestMapping(value="build",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse build(String id,HttpServletRequest request){
		JsonResponse jsonResponse = JsonResponse.success();
		try{
			if(StringUtil.isNotNull(id)){
				paasStoreAppService.build(id, getCurrentUser(request));
			}
		}catch(Exception e){
			jsonResponse = JsonResponse.failure(1, ExceptionUtil.getExceptionString(e));
		}
		return jsonResponse;
	}
	
	@RequestMapping("download")
	public void download(String id,HttpServletResponse response){
		if(StringUtil.isNotNull(id)){
			PaasStoreApp paasStoreApp = paasStoreAppService.getById(id);
			if(paasStoreApp!=null && StringUtil.isNotNull(paasStoreApp.getFilePath())){
				String filePath = paasStoreApp.getFilePath();
				FileUploadHandler.download(response, paasStoreApp.getFileName(), filePath);
			}
		}
	}
	@RequestMapping("viewInitFile")
	public void viewInitFile(String id,String number,HttpServletResponse response){
		if(StringUtil.isNotNull(id)){
			PaasStoreApp paasStoreApp = paasStoreAppService.getById(id);
			if(paasStoreApp!=null && StringUtil.isNotNull(paasStoreApp.getFilePath())){
				Map<String, String> map = paasStoreApp.getConfigFileByNO(number);
				if(map!=null){
					String path = map.get("filePath");
					if(!path.startsWith(FileUploadHandler.getStoreDir())){
						path = FileUploadHandler.getStoreDir()+path;
					}
					File file = new File(path);
					String message = "";
					if(file.exists()){
						try {
							response.setContentType("text/plain;charset=utf-8");
							FileUtil.copyFile(file , response.getOutputStream());
							return;
						} catch (IOException e) {
							logger.error("view init file failed.",e);
							message = "获取文件内容失败。";
						}
					}else{
						logger.error("view init file failed.file not found.id:"+id+",number:"+number+",file:"+file.getAbsolutePath());
						message = "未找到文件。";
					}
					try {
						response.getOutputStream().print(message);
					} catch (IOException e) {
						
					}
				}
			}
		}
	}
	@RequestMapping("downloadInitFile")
	public void downloadInitFile(String id,String number,HttpServletResponse response){
		if(StringUtil.isNotNull(id)){
			PaasStoreApp paasStoreApp = paasStoreAppService.getById(id);
			if(paasStoreApp!=null && StringUtil.isNotNull(paasStoreApp.getFilePath())){
				Map<String, String> map = paasStoreApp.getConfigFileByNO(number);
				if(map!=null){
					String fileName = map.get("fileName");
					if(fileName!=null){
						String filePath = map.get("filePath");
						FileUploadHandler.download(response, fileName, filePath);
						return;
					}
				}
				response.setContentType("text/html;charset=utf8");
				response.setCharacterEncoding("utf-8");
				try {
					response.getWriter().write("<script>alert('文件不存在！');history.back();</script>");
				} catch (IOException e) {
					
				}
			}
		}
	}
	
	@RequestMapping("log")
	@ResponseBody
	public Map<String, String> log(String id){
		String log = paasStoreAppService.getBuildLog(id);
		Map<String, String> map  = new HashMap<String, String>();
		map.put("log", log);
		return map;
	}
	
	@RequestMapping("detail")
	public String detail(String id,HttpServletRequest request){
		request.setAttribute("presetApp", paasStoreAppService.getById(id));
		request.setAttribute("dockerRegistry", ConfigUtil.getConfigString("paasos.docker.registry"));
		return getUrlPrefix() + "/detail";
	}
	
	@RequestMapping("checkNameAndVersionExist")
	public void checkNameAndVersionExist(String name,String version,String id,HttpServletResponse response){
		int count  = paasStoreAppService.existNameAndVersion(name, version, id);
		try {
			response.getOutputStream().print(count==0);
		} catch (IOException e) {
			logger.debug(e);
		}
		
	}
	
}
