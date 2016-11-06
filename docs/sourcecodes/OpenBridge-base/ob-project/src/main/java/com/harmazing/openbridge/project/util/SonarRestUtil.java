package com.harmazing.openbridge.project.util;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.util.Iterator;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.http.client.GetRequest;
import com.harmazing.framework.util.http.client.HttpConnector;
import com.harmazing.framework.util.http.client.HttpConnector.Builder;
import com.harmazing.framework.util.http.client.WsConnector;
import com.harmazing.framework.util.http.client.WsResponse;
import com.harmazing.openbridge.project.vo.SonarIssue;
public class SonarRestUtil {
	public static final String SONAR_SERVER_URI = "sonar.serverUri";
	private static final String API_PROJECTS = "api/projects";
	private static final String API_RESOURCE = "api/resources/index";
	private static final String API_QUALITYGATE = "api/qualitygates";
	private static final String API_QUALITYGATE_SEARCH = "api/qualitygates/search";
	private static final String API_QUALITYGATE_LIST = "api/qualitygates/list";
	private static final String API_QUALITYGATE_SHOW = "api/qualitygates/show";
	private static final String API_QUALITYPROFILE = "api/qualityprofiles";
	private static final String API_QUALITYEVNET = "api/events";
	private static final String API_HISTORY = "api/timemachine";
	private static final String API_ISSUES_SEARCH = "api/issues/search";
	private static final String API_RULES_SEARCH = "api/rules/search";
	private static final String API_SOURCES_LINES = "api/sources/lines";
	private static final String API_COMPONENTS_APP = "api/components/app";
	public enum Qualifier{//view,sub-vew,project,module,unit test,directory,file,developer
		VW,SVW,TRK,BRC,UTS,DIR,FIL,DEV
	}
	protected static WsConnector getConnector() {
		String url = ConfigUtil.getConfigString(SONAR_SERVER_URI);
/*		String url = "http://192.168.31.65:9000";*/
		String login = ConfigUtil.getConfigString("sonar.username");
		String password = ConfigUtil.getConfigString("sonar.password");
		Builder builder = new HttpConnector.Builder();
		builder.url(url);
		builder.credentials(login, password);
		WsConnector wsConnector = builder.build();
		return wsConnector;
	}
	protected static String path(String action,String controller) {
	    return String.format("%s/%s", controller, action);
	}
	public static String getAllProjects(){
		GetRequest get = new GetRequest(path("index", API_PROJECTS)).setParam("format", "json");
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	/**
	 * @author chenjinfan
	 * @Description
	 * @param key
	 * @return
	 * "periods": [
      {
        "index": 1,
        "mode": "last_period",
        "date": "2000-04-27T00:45:23+0200"
      },
      {
        "index": 2,
        "mode": "last_version",
        "date": "2000-04-27T00:45:23+0200",
        "parameter": "2015-12-07"
      },
      {
        "index": 3,
        "mode": "last_analysis"
      },
      {
        "index": 5,
        "mode": "last_30_days",
        "parameter": "2015-11-07"
      }
    ]
	 */
	public static String getProQualityGateStatus(String key){
		return getResources(key,"alert_status,quality_gate_details",-1,Qualifier.TRK,false);
	}
	/**
	 * @author chenjinfan
	 * @Description 查找项目关联qualityGate 
	 * @param projectName
	 * @return
	 */
	public static String getAssociateQualityGate(String projectId,String lname){
		checkArgument(!isNullOrEmpty(projectId), "请指定项目id");
		GetRequest get = new GetRequest(path("list", API_QUALITYGATE));
//		get.setParam("query", key);
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		String listStr = wsResponse.content();
		JSONArray qualitygates = JSON.parseObject(listStr).getJSONArray("qualitygates");
		if(qualitygates.size()>20){
			return "";
		}
		Iterator<Object> it = qualitygates.iterator();
		JSONObject qualitygate = null;
		qg : while(it.hasNext()){
			qualitygate = (JSONObject) it.next();
			String id = qualitygate.getString("id");
			get = new GetRequest(path("search", API_QUALITYGATE));
			get.setParam("gateId", id).setParam("selected", "selected");
			String prjs = wsConnector.call(get).failIfNotSuccessful().content();
			Iterator<Object> it1 = JSON.parseObject(prjs).getJSONArray("results").iterator();
			while(it1.hasNext()){
				JSONObject pro = (JSONObject) it1.next();
				if(lname.equals(pro.getString("name"))){
					break qg;
				}else{
					qualitygate = null;
				}
			}
		}
		return qualitygate==null ? "" : qualitygate.toJSONString();
	}
	
	/**
	* @Title: getProQualityProfile 
	* @author weiyujia
	* @Description: 获取质量检测规则
	* @param @param 项目编码
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getProQualityProfile(String key){
		checkArgument(!isNullOrEmpty(key), "请指定项目key");
		GetRequest get = new GetRequest(path("search", API_QUALITYPROFILE));
		get.setParam("projectKey", key);
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	/**
	* @Title: getProEvents 
	* @author weiyujia
	* @Description: 获取code project  event 信息
	* @param @param key
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getProEvents(String key){
		checkArgument(!isNullOrEmpty(key), "请指定项目key");
		GetRequest get = new GetRequest(API_QUALITYEVNET);
		get.setParam("resource", key);
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	/**
	* @Title: getResources 
	* @author weiyujia
	* @Description: 获取项目资源相关信息
	* @param @param 项目key值
	* @param @param metrics
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getResources(String key,String metrics){
		return getResources(key, metrics, null,null,false);
	}
	public static String getResources(String key,String metrics,Integer depth,Qualifier qualifier,
			boolean includetrends){
		checkArgument(!isNullOrEmpty(key), "请指定项目key");
		if(depth==null){
			depth = 0;
		}
		GetRequest get = new GetRequest(API_RESOURCE);
		get.setParam("resource", key);
		if(!isNullOrEmpty(metrics)){
			get.setParam("metrics", metrics);
		}
		if(qualifier!=null){
			get.setParam("qualifiers", qualifier);
		}
		if(includetrends){
			get.setParam("includetrends", includetrends);
		}
		get.setParam("depth", depth);
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	/**
	* @Title: getProjectInfo 
	* @author weiyujia
	* @Description: 获取项目app相关信息
	* @param @param key
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getProjectInfo(String key){
		checkArgument(!isNullOrEmpty(key), "请指定项目key");
		GetRequest get = new GetRequest("api/navigation/component");
		get.setParam("componentKey", key);
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	/**
	* @Title: getHistoryData 
	* @author weiyujia
	* @Description: 获取历史数据
	* @param @param key
	* @param @param metrics
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getHistoryData(String key,String metrics){
		checkArgument(!isNullOrEmpty(key), "请指定项目key");
		GetRequest get = new GetRequest(path("index",API_HISTORY));
		get.setParam("resource", key);
		if(!isNullOrEmpty(metrics)){
			get.setParam("metrics", metrics);
		}
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	
	/**
	* @Title: searchMetrics 
	* @author weiyujia
	* @Description: 查找metrics
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String searchMetrics(){
		GetRequest get = new GetRequest("api/metrics/search");
//		get.setParam("isCustom", true);
		get.setParam("p", 1);
		get.setParam("ps", 500);
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	/**
	* @author weiyujia
	* @Description: 获取问题界面相关信息
	* @param @param facets
	* @param @return
	* @date 2016年3月7日 上午11:20:29     
	* @return  
	* @throws
	 */
	public static String getIssuesInfo(SonarIssue sonarIssue){
		checkArgument(!isNullOrEmpty(sonarIssue.getProjectKey()), "请指定项目key");
		GetRequest get = new GetRequest(API_ISSUES_SEARCH);
		if (!Strings.isNullOrEmpty(sonarIssue.getProjectKey())) {
			get.setParam("projectKeys", sonarIssue.getProjectKey());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getFacets())) {
			get.setParam("facets", sonarIssue.getFacets());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getSeverities())) {
			get.setParam("severities", sonarIssue.getSeverities());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getResolutions())) {
			get.setParam("resolutions", sonarIssue.getResolutions());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getResolved())) {
			get.setParam("resolved", sonarIssue.getResolved());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getStatuses())) {
			get.setParam("statuses", sonarIssue.getStatuses());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getRules())) {
			get.setParam("rules", sonarIssue.getRules());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getPs())) {
			get.setParam("ps", sonarIssue.getPs());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getP())) {
			get.setParam("p", sonarIssue.getP());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getComponentKeys())) {
			get.setParam("componentKeys", sonarIssue.getComponentKeys());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getDirectories())) {
			get.setParam("directories", sonarIssue.getDirectories());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getF())) {
			get.setParam("f", sonarIssue.getF());
		}
		if (!Strings.isNullOrEmpty(sonarIssue.getS())) {
			get.setParam("s", sonarIssue.getS());
		}
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	
	/**
	* @Title: getRulesInfo 
	* @author weiyujia
	* @Description: 获取规则详情信息
	* @param @param sonarIssue
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getRulesInfo(SonarIssue sonarIssue) {
		checkArgument(!isNullOrEmpty(sonarIssue.getProjectKey()), "请指定项目key");
		GetRequest get = new GetRequest(API_RULES_SEARCH);
		if(!Strings.isNullOrEmpty(sonarIssue.getRules())){
			get.setParam("rule_key", sonarIssue.getRules());
		}
		if(!Strings.isNullOrEmpty(sonarIssue.getFacets())){
			get.setParam("facets", sonarIssue.getFacets());
		}
		if(!Strings.isNullOrEmpty(sonarIssue.getPs())){
			get.setParam("ps", sonarIssue.getPs());
		}
		if(!Strings.isNullOrEmpty(sonarIssue.getQprofile())){
			get.setParam("qprofile", sonarIssue.getQprofile());
		}
		if(sonarIssue.isIncludetrends()){
			get.setParam("includetrends", sonarIssue.isIncludetrends());
		}
		if(!Strings.isNullOrEmpty(sonarIssue.getActiveSeverities())){
			get.setParam("active_severities", sonarIssue.getActiveSeverities());
		}
		if(!Strings.isNullOrEmpty(sonarIssue.getF())){
			get.setParam("f", sonarIssue.getF());
		}
		if(!Strings.isNullOrEmpty(sonarIssue.getS())){
			get.setParam("s", sonarIssue.getS());
		}
		if(sonarIssue.isActivation()){
			get.setParam("activation", sonarIssue.isActivation());
		}
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	
	/**
	* @Title: getQualityGateList 
	* @author weiyujia
	* @Description: 获取质量检测规则列表
	* @param @param sonarIssue
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getQualityGateList(SonarIssue sonarIssue){
		GetRequest get = new GetRequest(API_QUALITYGATE_LIST);
		checkArgument(!isNullOrEmpty(sonarIssue.getProjectKey()), "请指定项目key");
//		get.setParam("projectKey", sonarIssue.getProjectKey());
//		if(!Strings.isNullOrEmpty(sonarIssue.getPs())){
//			get.setParam("pageSize", sonarIssue.getPs());
//		}
//		if(!Strings.isNullOrEmpty(sonarIssue.getGateId())){
//			get.setParam("gateId", sonarIssue.getS());
//		}
//		if(!Strings.isNullOrEmpty(sonarIssue.getSelected())){
//			get.setParam("selected", sonarIssue.getSelected());
//		}
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	
	/**
	* @Title: getQualityGatesInfo 
	* @author weiyujia
	* @Description: 获取文件检测详情
	* @param @param sonarIssue
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String getQualityGatesInfo(SonarIssue sonarIssue) {
		GetRequest get = new GetRequest(API_QUALITYGATE_SHOW);
		if(!Strings.isNullOrEmpty(sonarIssue.getGateId())){
			get.setParam("id", sonarIssue.getGateId());
		}
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	public static String getQualityProjectInfo(SonarIssue sonarIssue) {
		GetRequest get = new GetRequest(API_QUALITYGATE_SEARCH);
		if(!Strings.isNullOrEmpty(sonarIssue.getGateId())){
			get.setParam("gateId", sonarIssue.getGateId());
		}
		if(!Strings.isNullOrEmpty(sonarIssue.getSelected())){
			get.setParam("selected", sonarIssue.getSelected());
		}
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	
	public static String getSourcesIssuesInfo(SonarIssue sonarIssue) {
		GetRequest get = new GetRequest(API_SOURCES_LINES);
		/*if(!Strings.isNullOrEmpty(sonarIssue.getFrom())){
			get.setParam("from", sonarIssue.getFrom());
		}*/
		/*if(!Strings.isNullOrEmpty(sonarIssue.getComponentKeys())){
			get.setParam("key", sonarIssue.getComponentKeys());
		}*/
		/*if(!Strings.isNullOrEmpty(sonarIssue.getTo())){
			get.setParam("to", sonarIssue.getTo());
		}*/
		if(!Strings.isNullOrEmpty(sonarIssue.getUuid())){
			get.setParam("uuid", sonarIssue.getUuid());
		}
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
	public static String getComponentsInfo(SonarIssue sonarIssue) {
		GetRequest get = new GetRequest(API_COMPONENTS_APP);
		if(!Strings.isNullOrEmpty(sonarIssue.getUuid())){
			get.setParam("uuid", sonarIssue.getUuid());
		}
		WsConnector wsConnector = getConnector();
		WsResponse wsResponse = wsConnector.call(get).failIfNotSuccessful();
		return wsResponse.content();
	}
}
