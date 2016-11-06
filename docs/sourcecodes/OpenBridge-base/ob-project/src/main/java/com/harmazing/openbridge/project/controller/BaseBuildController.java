package com.harmazing.openbridge.project.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.harmazing.framework.common.JsonResponse;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.DateUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.http.client.HttpException;
import com.harmazing.openbridge.project.service.IBuildService;
import com.harmazing.openbridge.project.util.CommonUtil;
import com.harmazing.openbridge.project.util.JenkinsRestUtil;
import com.harmazing.openbridge.project.util.ParseXmlUtil;
import com.harmazing.openbridge.project.vo.JenkinsConfigVO;

/**
* @ClassName: BaseBuildController 
* @Description: Jenkins代码构建基类
* @author weiyujia
* @date 2016年5月3日 下午4:16:46 
*   s
 */
public abstract class BaseBuildController {
	@Autowired
	public IBuildService buildService;
	
	
	protected  final Logger logger = Logger.getLogger(this.getClass());

	@RequestMapping("index")
	public String versionBuild(HttpServletRequest request, HttpServletResponse response,String versionId) {
		loadAppModel(request);
		boolean isExist = false;
		try {
			String jobName = request.getParameter("jobName");
			String jobName1 = getJobName(request, versionId);
			if(StringUtil.isNull(jobName)){
				jobName = jobName1;
			}
			request.setAttribute("jobDisplayName", getJobDisplayName(request,versionId));
			request.setAttribute("jobName", jobName);
			request.setAttribute("versionId", versionId);
			isExist = JenkinsRestUtil.isJobExists(jobName);
		} catch (Exception e) {
			logger.error("check job name failed.", e);
			request.setAttribute("exception", e);
		}
		request.setAttribute("isExist", isExist);
		return getUrlPrefix()+"/build";
	}

	public String getJobName(HttpServletRequest request, String versionId){
		return versionId;
//		return "ob-base";
	}
	
	public abstract String getJobDisplayName(HttpServletRequest request, String versionId);
	
	/**
	 * @author chenjinfan
	 * @Description 立即构建
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("execute")
	@ResponseBody
	public JsonResponse execute(HttpServletRequest request, HttpServletResponse response){
		JsonResponse jsonRes = JsonResponse.success();
		String jobName = request.getParameter("jobName");
		boolean isExist;
		try {
			if(StringUtil.isNull(jobName)){
				String versionId = request.getParameter("versionId");
				jobName = getJobName(request, versionId);
			}
			if(StringUtil.isNull(jobName)){
				return JsonResponse.failure(400, "Job name is not specified.");
			}
			isExist = JenkinsRestUtil.isJobExists(jobName);
			if(isExist){
				JenkinsRestUtil.updateCustomizedElement(request);
				String queueUrl = JenkinsRestUtil.build(jobName);
				String status = JenkinsRestUtil.getQueueStatus(queueUrl);
				jsonRes.addData("queueUrl", queueUrl);
				jsonRes.addData("status", status);
			}else{
				return JsonResponse.failure(404, "Job does not exist.");
			}
		} catch (Exception e) {
			logger.error("Invalid jobName:"+jobName, e);
			return JsonResponse.failure(500, e.getMessage());
		}
		return jsonRes;
	}

	@RequestMapping("home")
	public String home(HttpServletRequest request, HttpServletResponse response, String jobName){
		loadAppModel(request);
		Map<String, Object> data = JenkinsRestUtil.getJobData(jobName);
		request.setAttribute("modules", data.get("modules"));
		request.setAttribute("metaData", data);
		return getUrlPrefix() + "/home";
	}
	
	/**
	* @Title: loadAppModel 
	* @author weiyujia
	* @Description: 获取项目基础信息 
	* @param request  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年5月3日 下午4:18:26
	 */
	public abstract Object loadAppModel(HttpServletRequest request);
	
	public abstract String buildHistory(HttpServletRequest request, HttpServletResponse response
			,String versionId,String jobName) throws UnsupportedEncodingException;
	
	/*@RequestMapping("buildHistory")
	public String buildHistory(HttpServletRequest request, HttpServletResponse response
			,String versionId,String jobName) throws UnsupportedEncodingException{
		loadAppModel(request);
		String number = request.getParameter("number");
		if(StringUtil.isNotNull(number)){
			request.setAttribute("number", number);
			request.setAttribute("jobName", jobName);
			String testReport = request.getParameter("testReport");

			if(testReport!=null && "true".equals(testReport)){
				String resultJson = JenkinsRestUtil.getTestReportData(jobName,number);
				request.setAttribute("resultJson", CommonUtil.htmlEncode(resultJson));
				return getUrlPrefix() + "/testReport";
			}
			return getUrlPrefix() + "/buildConsole";
		}
		try {
			int pageNo = StringUtil.getIntParam(request, "pageNo", 1);
			int pageSize = StringUtil.getIntParam(request, "pageSize", 20);
			Page<Map<String, Object>> page = JenkinsRestUtil.getBuildHistory(jobName, pageNo, pageSize);
			request.setAttribute("page", page);
		} catch (UnsupportedEncodingException e) {
		}
		return getUrlPrefix() + "/buildHistory";
	}*/

	
	public abstract Page<Map<String, Object>> getBuildHistory(HttpServletRequest request,String jobName);
	
	/*@RequestMapping("getBuildHistory")
	@ResponseBody
	public Page<Map<String, Object>> getBuildHistory(HttpServletRequest request,String jobName){
		int offset = StringUtil.getIntParam(request, "offset", 0);
		int pageSize = StringUtil.getIntParam(request, "pageSize", 20);
		Page<Map<String, Object>> page = null;
		try {
			page = JenkinsRestUtil.getBuildHistory(jobName, offset+1, pageSize);
		} catch (HttpException | UnsupportedEncodingException e) {
		}
		return page;
	}*/
	
	public abstract String toBuildConfigEdit(HttpServletRequest request, HttpServletResponse response ,String jobName, boolean isExist) throws Exception;
	
	/**
	* @Title: scmChanges 
	* @author weiyujia
	* @Description: 构造Jenkins 变更记录界面数据 
	* @param request
	* @param response
	* @param jobName	Job名称
	* @return String    返回类型 
	* @throws 
	* @date 2016年4月13日 下午6:15:38
	 */
	@RequestMapping("scmChanges")
	public String scmChanges(HttpServletRequest request, HttpServletResponse response,String jobName){
		loadAppModel(request);
		try {
			String content = JenkinsRestUtil.getChangeHistory(jobName);
			Map<String,Object> resultMap = new HashMap<String,Object>();
			resultMap = ParseXmlUtil.fromJsonToObject(content);
			request.setAttribute("result", CommonUtil.htmlEncode(resultMap.get("builds").toString()));
		} catch (UnsupportedEncodingException e) {
			if(logger.isDebugEnabled()){
				logger.debug("",e);
			}
		}	
		return getUrlPrefix() + "/scmChanges";
	}
	
	@RequestMapping("workspace")
	public String workspace(HttpServletRequest request, HttpServletResponse response){
		loadAppModel(request);
		return getUrlPrefix() + "/workspace";
	}
	
	@RequestMapping("console")
	public String console(HttpServletRequest request, HttpServletResponse response, 
			String jobName,int number){
		loadAppModel(request);
		request.setAttribute("jobName", jobName);
		request.setAttribute("number", number);
		return getUrlPrefix() + "/buildConsole";
	}

	@RequestMapping("queryQueueAndBuildings")
	@ResponseBody
	public JsonResponse queryQueueAndBuildings(String jobName){
		JsonResponse jsonResponse = JsonResponse.success();
		List<Map<String, Object>> queueTasks = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> buildingRecores = new ArrayList<Map<String, Object>>();
		try{
			queueTasks = JenkinsRestUtil.getQueueTasks(jobName);
			buildingRecores = JenkinsRestUtil.getBuildingRecores(jobName);
		}catch(HttpException e){
			
		}
		jsonResponse.addData("queueTasks", queueTasks);
		jsonResponse.addData("buildingRecords", buildingRecores);
		return jsonResponse;
	}
	
	@RequestMapping("getArtifact")
	@ResponseBody
	public List<Map<String, Object>> getArtifact(String jobName, Integer number){
		/*List<String> pathList = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			pathList = JenkinsRestUtil.getArtifact(jobName,number.toString());
		} catch (HttpException e) {
			
		}
		for(String path : pathList){
			if(path!=null){
				String[] bar = path.split("_-_");
				if(bar.length==2){
					path = bar[0];
					jobName = bar[1];
					Map<String, Object> artifact = new HashMap<String, Object>();
					artifact.put("relativePath", path);
					artifact.put("jobName", jobName);
					String[] aStrings = path.split("/");
					artifact.put("displayName", aStrings[aStrings.length-1]);
					result.add(artifact);
				}
			}
		}
		return result;*/
		return JenkinsRestUtil.getArtifacts(jobName,number.toString());
	}
	
	@RequestMapping("getConsoleHtml")
	@ResponseBody
	public JsonResponse getConsoleHtml(String jobName, Integer number, Integer start){
		JsonResponse jsonResponse = JsonResponse.success();
		try {
			jsonResponse.addData("data", JenkinsRestUtil.getLogStream(jobName, number,start));
//			jsonResponse.addData("isBuilding", JenkinsRestUtil.isBuilding(jobName, number));
		} catch (HttpException | UnsupportedEncodingException e) {
			
		}
		return jsonResponse;
	}
	
	@RequestMapping("submitDescription")
	@ResponseBody
	public JsonResponse submitDescription(String jobName, String description){
		JsonResponse jsonResponse = JsonResponse.success();
		try {
			JenkinsRestUtil.updateDscription(jobName, description);
		} catch (HttpException e) {
			logger.error("Update jenkins job description failed.", e);
			jsonResponse = JsonResponse.failure(e.code(), e.getMessage());
		}
		return jsonResponse;
	}
	
	@RequestMapping("switchJob")
	@ResponseBody
	public JsonResponse switchJob(String jobName, boolean enable){
		JsonResponse jsonResponse = JsonResponse.success();
		try {
			JenkinsRestUtil.switchJob(jobName, enable);
		} catch (HttpException e) {
			logger.error("Swtich job failed,enable:"+enable+".", e);
			jsonResponse = JsonResponse.failure(e.code(), e.getMessage());
		}
		return jsonResponse;
	}
	
	@RequestMapping("submitConfig")
	@ResponseBody
	public JsonResponse submitConfig(HttpServletRequest request, HttpServletResponse response, JenkinsConfigVO configVO) throws TransformerException{
		JsonResponse jsonResponse = JsonResponse.success();
		boolean result = false;
		try {
			String jobConfig = JenkinsRestUtil.getConfig(configVO.getAppName());
			jobConfig = JenkinsRestUtil.updateJenkinsConfigByVO(configVO.getAppName(),configVO);
			result = JenkinsRestUtil.updateConfig(configVO.getAppName(),jobConfig);
			if(result){
				jsonResponse.addData("result", "success");
			}else{
				jsonResponse.addData("result", "fail");
			}
			return jsonResponse;
		} catch (SAXException|IOException|HttpException |TransformerFactoryConfigurationError|ParserConfigurationException e) {
			jsonResponse.addData("result", "fail");
			if(logger.isDebugEnabled()){
				logger.debug("",e);
			}
		}
		return jsonResponse;
	}
	@RequestMapping("viewFile")
	public String viewFile(HttpServletRequest request,HttpServletResponse response, String jobName, Integer number, String path, Boolean download) throws IOException{
		response.setCharacterEncoding("UTF-8");
		try{
			if(download==null)
				download = false;
			String[] coms = path.split("/");
			String fileName = coms[coms.length-1];
			response.reset();
			String [] allowTypes = new String[]{"pom","xml","java","properties","txt","sql","html"
					,"js","css","jsp","htm","json"};
			if(!download){
				boolean allow = false;
				for(String l : allowTypes){
					if(fileName.endsWith("."+l)){
						allow = true;
						break;
					}
				}
				if(!allow){
					response.getWriter().print("只能查看以下类型文件：\n"+StringUtils.join(allowTypes, ","));
					return null;
				}
			}
			if(download){
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "inline; filename=\""+fileName+"\"");
			}else{
				response.setContentType("text/plain;charset=utf8");
			}
			JenkinsRestUtil.viewFile(jobName, number.toString(), path, response.getOutputStream());
			return null;
		}catch(HttpException e){
			logger.warn(e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 测试报告趋势图 
	 */
	@RequestMapping("testTrend")
	public void testTrend(HttpServletRequest request, HttpServletResponse response, String jobName){
		String queryString = request.getQueryString();
		try {
			JenkinsRestUtil.testTrend(jobName, queryString, response.getOutputStream());
		} catch (IOException e) {
			if(logger.isDebugEnabled()){
				logger.debug("",e);
			}
		}
	}
	@RequestMapping("testTrendMap")
	public void testTrendMap(HttpServletRequest request, HttpServletResponse response, String jobName){
		String queryString = request.getQueryString();
		try {
			response.getOutputStream().print(JenkinsRestUtil.testTrendMap(jobName, queryString));
		} catch (IOException e) {
			if(logger.isDebugEnabled()){
				logger.debug("",e);
			}
		}
	}
	
	public String getAssemblyFolder(String projectId, String versionId) {
		projectId = projectId.replaceAll("[\\/:*?\"<>|]", " ");
		return "/projects/" + projectId + "/assembly/" + versionId;
	}
	
	/**
	* @Title: checkRemote 
	* @author weiyujia
	* @Description: 校验URL是否通过代码 pull验证
	* @param request
	* @param response
	* @param configVO
	* @return
	* @throws TransformerException  设定文件 
	* @return JsonResponse    返回类型 
	* @throws 
	* @date 2016年4月15日 下午6:26:32
	 */
	@RequestMapping("checkRemote")
	@ResponseBody
	public JsonResponse checkRemote(HttpServletRequest request, HttpServletResponse response, JenkinsConfigVO configVO) throws TransformerException{
		JsonResponse jsonResponse = JsonResponse.success();
		String result = JenkinsRestUtil.checkRemote(configVO.getAppName(),configVO.getRemote(),configVO.getType());
		jsonResponse.addData("result", result);
		return jsonResponse;
	}

	/**
	* @Title: testHistoryTrend 
	* @author weiyujia
	* @Description: 测试历史记录趁势图 
	* @param request
	* @param response
	* @param jobName  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年4月25日 下午6:24:18
	 */
	@RequestMapping("testHistoryTrendPng")
	public void testHistoryTrend(HttpServletRequest request, HttpServletResponse response, String jobName,String number,String type){
		try {
			JenkinsRestUtil.testHistoryTrendPng(jobName,number, type, response.getOutputStream());
		} catch (IOException e) {
			if(logger.isDebugEnabled()){
				logger.debug("获取测试结果历史趁势图异常",e);
			}
		}
	}
	
	@RequestMapping("testHistoryTrendMap")
	public void testHistoryTrendMap(HttpServletRequest request, HttpServletResponse response, String jobName,String number){
		try {
			response.getOutputStream().print(JenkinsRestUtil.testHistoryTrendMap(jobName,number));
		} catch (IOException e) {
			if(logger.isDebugEnabled()){
				logger.debug("",e);
			}
		}
	}
	
	protected String getUrlPrefix() {
		String temp = "";
		RequestMapping map = this.getClass().getAnnotation(RequestMapping.class);
		if (map != null && map.value() != null && map.value().length > 0) {
			temp = map.value()[0];
		}
		return temp;
	}
	
	
}
