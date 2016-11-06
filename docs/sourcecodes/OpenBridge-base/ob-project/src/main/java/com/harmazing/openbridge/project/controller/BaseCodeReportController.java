package com.harmazing.openbridge.project.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.openbridge.project.util.CommonUtil;
import com.harmazing.openbridge.project.util.SonarRestUtil;
import com.harmazing.openbridge.project.vo.SonarIssue;
import com.mysql.jdbc.StringUtils;


/**
* @ClassName: BaseCodeReportController 
* @Description: Sonar代码报告基类
* @author weiyujia
* @date 2016年5月3日 下午4:36:44 
*
 */
public abstract class BaseCodeReportController {
	
//	@RequestMapping("/index")
	public abstract String codeReport(HttpServletRequest request, HttpServletResponse response, String versionId);/* {
		// get Project key
		AppModel app = loadAppModel(request);
		AppVersion version = appVersionService.getAppVersionById(versionId);
		String sonarKey = version.getVersionId();
		// String sonarKey = "org.sonarqube:java-simple-sq-scanner";
//		 String sonarKey = "jn.cn:DocumentManage";
		request.setAttribute("projectKey", app.getAppId());
		request.setAttribute("appId", app.getAppId());
		return getUrlPrefix() + "/codeReport";
	}
*/
	/**
	 * @author weiyujia
	 * @Title:
	 * @Description: 异步请求SONAR服务器数据
	 * @param @param request
	 * @param @param response
	 * @param @param projectKey
	 * @param @return
	 * @date 2016年3月4日 下午7:03:37
	 */
	@RequestMapping("/getMetricInfo")
	@ResponseBody
	public Map<String, String> getMetricInfo(HttpServletRequest request, HttpServletResponse response, SonarIssue sonarIssue) {
		Map<String, String> resultMap = new HashMap<String, String>();
		// String metricArr = "Technical Debt,Duplications,Structure";
		String paramArr = "";
		String resultJson = "";
		String eventsJson = "";
		//首页入口
		if ("home".equals(sonarIssue.getPath())) {
			paramArr = "sqale_index,new_sqale_debt_ratio,violations,new_violations,duplicated_blocks,duplicated_lines,duplicated_lines_density,ncloc,sqale_rating,overall_coverage,new_overall_coverage,coverage,new_coverage,it_coverage,new_it_coverage,tests,duplicated_lines_density,duplicated_blocks,ncloc,ncloc_language_distribution";
			resultJson = SonarRestUtil.getResources(sonarIssue.getProjectKey(),
					paramArr, -1, null, true);
			String projectLName = getLnameFromBaseJson(resultJson);
			eventsJson = SonarRestUtil.getProEvents(sonarIssue.getProjectKey());
			resultMap.put("eventsJson", eventsJson);
//			String projectStatusJson = SonarRestUtil.getProjectStatusJson(sonarIssue);
//			resultMap.put("projectStatusJson", projectStatusJson);
			if(!StringUtils.isNullOrEmpty(projectLName)){
				String defaultQualityInfoJson = SonarRestUtil.getAssociateQualityGate(sonarIssue.getProjectKey(),projectLName);
				resultMap.put("defaultQualityInfoJson", defaultQualityInfoJson);
			}
			String proQualityProfileJson = SonarRestUtil
					.getProQualityProfile(sonarIssue.getProjectKey());
			resultMap.put("proQualityProfileJson", proQualityProfileJson);
			String proQualityGateStatusJson = SonarRestUtil
					.getQualityGateList(sonarIssue);
			resultMap.put("proQualityGateStatusJson", proQualityGateStatusJson);
			//风险界面入口
		} else if ("issues".equals(sonarIssue.getPath())) {
			/*
			 * paramArr =
			 * "severities,resolutions,resolutions,assigned_to_me,statuses";
			 * sonarIssue.setFacets(paramArr);
			 */
			resultJson = SonarRestUtil.getIssuesInfo(sonarIssue);
		} else if ("version".equals(sonarIssue.getPath())) {
			paramArr = "Version";
			resultJson = SonarRestUtil.getResources(sonarIssue.getProjectKey(),
					paramArr);
		}
		resultMap.put("resultJson", resultJson);
		return resultMap;
	}

	private String getLnameFromBaseJson(String resultJson) {
		JSONArray jsonArray = JSONArray.parseArray(resultJson);
		if(jsonArray.isEmpty()){
			return null;
		}
		JSONObject jsonObject = jsonArray.getJSONObject(0);
		return jsonObject.getString("lname");
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
	public abstract void loadAppModel(HttpServletRequest request);
	
	/**
	 * @author weiyujia
	 * @Description: 代码检查报告 project home
	 * @param @param request
	 * @param @param response
	 * @param @param projectKey
	 * @param @return
	 * @date 2016年3月4日 下午2:12:04
	 */
	@RequestMapping("/home")
	public String codeReportHome(HttpServletRequest request,
			HttpServletResponse response) {

		// String technicalDebtJson = SonarRestUtil.getResources(projectKey,
		// "Technical Debt");
		// request.setAttribute("TechnicalDebt", technicalDebtJson);
		// String duplicationsJson = SonarRestUtil.getResources(projectKey,
		// "Duplications");
		// request.setAttribute("Duplications", duplicationsJson);
		// String structureJson = SonarRestUtil.getResources(projectKey,
		// "Structure");
		// request.setAttribute("Structure", structureJson);
		return getUrlPrefix() + "/home";
	}

	/**
	* @Title: codeReportDuplications 
	* @param @param response
	* @Description: 重复率界面入口
	* @param @param request
	* @param @param projectKey
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/duplications")
	public String codeReportDuplications(HttpServletRequest request,
			HttpServletResponse response, String projectKey) {
		String metrics = "duplicated_lines,duplicated_files,duplicated_lines_density,duplicated_blocks";
		String resJson = SonarRestUtil.getResources(projectKey, metrics, null,
				null, true);
		request.setAttribute("resJson", resJson);
		return getUrlPrefix() + "/duplications";
	}

	/**
	* @Title: codeReportStructure 
	* @Description: 代码结构界面入口
	* @param @param request
	* @param @param response
	* @param @param projectKey
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/structure")
	public String codeReportStructure(HttpServletRequest request,
			HttpServletResponse response, String projectKey) {
		String structureMetrics = "accessors,classes,class_complexity_distribution,comment_lines,commented_out_code_lines,comment_lines_density,comment_lines_data,complexity,class_complexity,file_complexity,function_complexity,complexity_in_classes,complexity_in_functions,directories,files,file_complexity_distribution,functions,function_complexity_distribution,generated_lines,generated_ncloc,lines,ncloc,ncloc_language_distribution,ncloc_data,packages,projects,public_api,public_documented_api_density,public_undocumented_api,statements";
		String structureJson = SonarRestUtil.getResources(projectKey,
				structureMetrics, null, null, true);
		request.setAttribute("structureJson", structureJson);
		String componentsMetrics = "ncloc";
		String componentsJson = SonarRestUtil.getResources(projectKey,
				componentsMetrics, 1, null, true);
		request.setAttribute("componentsJson", componentsJson);
		return getUrlPrefix() + "/structure";
	}

	/**
	* @Title: codeReportIssues 
	* @Description: 代码问题界面入口
	* @param @param request
	* @param @param response
	* @param @param projectKey
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping("/issues")
	public String codeReportIssues(HttpServletRequest request,
			HttpServletResponse response, String projectKey) {

		return getUrlPrefix() + "/issues";
	}

	/**
	* @Title: getDuplicationTreeMapData 
	* @Description: 重复率界面右下角树结构入口
	* @param @param request
	* @param @param response
	* @param @param projectKey
	* @param @param metrics
	* @param @throws IOException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("/getDuplicationTreeMapData")
	public void getDuplicationTreeMapData(HttpServletRequest request,
			HttpServletResponse response, String projectKey, String metrics)
			throws IOException {
		// String metrics = "ncloc,duplicated_lines_density";
		String resJson = SonarRestUtil.getResources(projectKey, metrics, 1,
				null, false);
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().print(resJson);
	}

	/**
	* @Title: getDuplicationFilesBubble 
	* @Description: 重复率界面泡泡图
	* @param @param request
	* @param @param response
	* @param @param projectKey
	* @param @throws IOException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("/getDuplicationFilesBubble")
	public void getDuplicationFilesBubble(HttpServletRequest request,
			HttpServletResponse response, String projectKey) throws IOException {
		String metrics = "ncloc,duplicated_lines,duplicated_blocks";
		String resJson = SonarRestUtil.getResources(projectKey, metrics, -1,
				null, false);
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().print(resJson);
	}

	/**
	* @Title: getDuplicationHistoryData 
	* @Description: 获取重复率界面对比下拉框数据
	* @param @param request
	* @param @param response
	* @param @param projectKey
	* @param @throws IOException    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	@RequestMapping("/getDuplicationHistoryData")
	public void getDuplicationHistoryData(HttpServletRequest request,
			HttpServletResponse response, String projectKey) throws IOException {
		String metrics = "duplicated_lines_density";
		if (StringUtil.isNotNull(request.getParameter("metrics"))) {
			metrics = request.getParameter("metrics");
		}
		String resJson = SonarRestUtil.getHistoryData(projectKey, metrics);
		response.setContentType("text/json;charset=utf-8");
		response.getWriter().print(resJson);
	}

	/**
	* @Title: getSonarMetrics 
	* @Description: 获取sonaar metrics api数据
	* @param @param request
	* @param @param response
	* @param @param sonarIssue
	* @param @return
	* @param @throws IOException    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws
	 */
	@RequestMapping("/getSonarMetrics")
	@ResponseBody
	public Map<String, String> getSonarMetrics(HttpServletRequest request,
			HttpServletResponse response, SonarIssue sonarIssue)
			throws IOException {
		// response.setContentType("text/json;charset=utf-8");
		// response.getWriter().print(SonarRestUtil.searchMetrics());
		Map<String, String> resultMap = new HashMap<String, String>();
		String historyDuplicationJson = SonarRestUtil.getResources(
				sonarIssue.getProjectKey(), sonarIssue.getMetrics(), null,
				null, sonarIssue.isIncludetrends());
		resultMap.put("historyDuplicationJson", historyDuplicationJson);
		String historyCompareJson = SonarRestUtil.searchMetrics();
		resultMap.put("historyCompareJson", historyCompareJson);
		return resultMap;
	}

	/**
	* @Title: getRulesInfo 
	* @Description: 获取规则列表
	* @param @param request
	* @param @param response
	* @param @param sonarIssue
	* @param @return    设定文件 
	* @return Map<String,String>    返回类型 
	* @throws
	 */
	@RequestMapping("/getRulesInfo")
	@ResponseBody
	public Map<String, String> getRulesInfo(HttpServletRequest request,
			HttpServletResponse response, SonarIssue sonarIssue) {
		Map<String, String> resultMap = new HashMap<String, String>();
		String resultJson = SonarRestUtil.getRulesInfo(sonarIssue);
		resultMap.put("resultJson", resultJson);
		return resultMap;
	}

	/**
	 * @author weiyujia
	 * @Description: 代码问题检测规则详情
	 * @param @param request
	 * @param @param response
	 * @param @param sonarIssue
	 * @param @return
	 * @date 2016年3月15日 下午4:50:34
	 */
	@RequestMapping("/showProfiles")
	public String showProfiles(HttpServletRequest request,
			HttpServletResponse response, SonarIssue sonarIssue) {
		// api/qualityprofiles/search
		// String resultJson =
		// SonarRestUtil.getProQualityProfile(sonarIssue.getProjectKey());
		String resultJson = SonarRestUtil.getRulesInfo(sonarIssue);
		JSONObject jsonObject = JSONObject.parseObject(resultJson);
		jsonObject.remove("rules");
		request.setAttribute("profilesJson", jsonObject);
		request.setAttribute("projectKey", sonarIssue.getProjectKey());
		request.setAttribute("qprofile", sonarIssue.getQprofile());
		return getUrlPrefix() + "/profiles";
	}

	/**
	 * @author weiyujia
	 * @Description: 代码是否合格标准详情
	 * @param @param request
	 * @param @param response
	 * @param @param sonarIssue
	 * @param @return
	 * @date 2016年3月15日 下午4:49:55
	 */
	@RequestMapping("/showGates")
	public String showGates(HttpServletRequest request,
			HttpServletResponse response, SonarIssue sonarIssue) {
		String conditionsJson = SonarRestUtil.getQualityGatesInfo(sonarIssue);
		String projectJson = SonarRestUtil.getQualityProjectInfo(sonarIssue);
		request.setAttribute("conditionsJson", conditionsJson);
		request.setAttribute("projectJson", projectJson);
		request.setAttribute("projectKey", sonarIssue.getProjectKey());
		return getUrlPrefix() + "/qualitygates";
	}

	/**
	 * @author weiyujia
	 * @Description: 检查规则界面数据抓取
	 * @param @param request
	 * @param @param response
	 * @param @param sonarIssue 封装的参数对象
	 * @param @return
	 * @date 2016年3月14日 上午10:18:15
	 */
	@RequestMapping("/profilesSeverity")
	@ResponseBody
	public Map<String, Object> profilesSeverity(HttpServletRequest request,
			HttpServletResponse response, SonarIssue sonarIssue) {
		String resultJson = SonarRestUtil.getRulesInfo(sonarIssue);
		// JSONObject jsonObject = JSONObject.parseObject(resultJson);
		// Map<String,Object> resultList = parseJSON2List(jsonObject);
		return CommonUtil.fromJsonToObject(resultJson);
	}

	/**
	 * @author weiyujia
	 * @Description: 展现文件资源问题详情
	 * @param @param request
	 * @param @param response
	 * @param @param sonarIssue
	 * @param @return
	 * @date 2016年3月17日 下午2:53:06
	 */
	@RequestMapping("/showSourcesInfo")
	public String showSourcesInfo(HttpServletRequest request,
			HttpServletResponse response, SonarIssue sonarIssue) {
		String resultJson = SonarRestUtil.getSourcesIssuesInfo(sonarIssue);
		String linesInfoJson = SonarRestUtil.getIssuesInfo(sonarIssue);
		String fileInfoJson = SonarRestUtil.getComponentsInfo(sonarIssue);
		request.setAttribute("resultJson", CommonUtil.htmlEncode(resultJson));
		request.setAttribute("linesInfoJson",
				CommonUtil.htmlEncode(linesInfoJson));
		request.setAttribute("fileInfoJson", fileInfoJson);
		request.setAttribute("projectKey", sonarIssue.getProjectKey());
		return getUrlPrefix() + "/sourcesShow";
	}

	/**
	 * @author weiyujia
	 * @Title:
	 * @Description: 获取projectKey
	 * @param @param request
	 * @param @param versionId
	 * @param @return
	 * @date 2016年3月14日 上午10:49:48
	 */
	public abstract String getProjectKey(HttpServletRequest request, String versionId);

	/**
	 * @author weiyujia
	 * @Title:
	 * @Description: 结构详情界面
	 * @param @param request
	 * @param @param response
	 * @param @param sonarIssue
	 * @param @return
	 * @param @throws IOException
	 * @date 2016年3月22日 下午2:55:33
	 */
	@RequestMapping("/getMeasuresStructure")
	public String getMeasuresStructure(HttpServletRequest request,
			HttpServletResponse response, SonarIssue sonarIssue)
			throws IOException {
		String resultJson = SonarRestUtil.getResources(
				sonarIssue.getProjectKey(), sonarIssue.getMetrics(), 1, null,
				false);
		request.setAttribute("resultJson", CommonUtil.htmlEncode(resultJson));
		request.setAttribute("projectKey", sonarIssue.getProjectKey());
		request.setAttribute("metrics", sonarIssue.getMetrics());
		return getUrlPrefix() + "/measures";
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
