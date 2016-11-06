package com.harmazing.openbridge.project.util;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.BasicProject;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueType;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.oauth.TokenSecretVerifierHolder;
import com.harmazing.openbridge.project.util.oauth.AtlassianOAuthClient;
import com.harmazing.openbridge.project.util.oauth.OauthAuthenticationHandler;
import com.harmazing.openbridge.project.util.oauth.OauthAuthenticationHandler.NeedAuthorizeException;


/**
 * 官方文档：
 * https://docs.atlassian.com/jira-rest-java-client-api/2.0.0-m31/jira-rest-java-client-api/apidocs/
 * @author chenjinfan
 * @Description jira集成工具类
 */
public class JiraRestClientUtil {
	private static final Log log = LogFactory.getLog(JiraRestClientUtil.class);
	private static final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
	private static AtlassianOAuthClient atlassianOAuthClient;
	/**
	 * @author chenjinfan
	 * @Description 设置所使用的AtlassianOAuthClient 的类型
	 * @param aoc
	 */
	public static void setAtlassianOAuthClient(AtlassianOAuthClient aoc){
		atlassianOAuthClient = aoc; 
	}
	
	public static TokenSecretVerifierHolder getReqeuestTocken(){
		return atlassianOAuthClient.getRequestToken();
	}
	public static JiraRestClient getClient() throws URISyntaxException, NeedAuthorizeException {
		if(ConfigUtil.getConfigInt(atlassianOAuthClient.getPlatform()+"jira.useOauth", 0)==1){//是否使用oauth认证
			return getOauthClient();
		}
		return factory.createWithBasicHttpAuthentication(
					new URI(ConfigUtil.getConfigString(atlassianOAuthClient.getPlatform()+"atlassian.oauth.serverBaseUr")),
					ConfigUtil.getConfigString(atlassianOAuthClient.getPlatform()+"jira.username"),
					ConfigUtil.getConfigString(atlassianOAuthClient.getPlatform()+"jira.password"));
	}

	public static JiraRestClient getOauthClient() throws URISyntaxException, NeedAuthorizeException{
		return factory.create(new URI(ConfigUtil.getConfigString(atlassianOAuthClient.getPlatform()+"atlassian.oauth.serverBaseUr")), 
				new OauthAuthenticationHandler(atlassianOAuthClient));
	}
	public static void destory(JiraRestClient restClient) throws IOException{
		if(restClient!=null){
			try {
				restClient.close();
			} catch (IOException e) {
				log.debug("Exceptioned when close jira rest client.",e);
				throw e;
			}
		}
	}
	
	/**
	 * @author chenjinfan
	 * @Description 根据issue的ID查找issue
	 * @param key  issue的key
	 * @return
	 * @throws NeedAuthorizeException 
	 */
	public static Issue getIssue(String key) throws NeedAuthorizeException{
		JiraRestClient restClient = null;
		try {
			restClient = getClient();
			return restClient.getIssueClient().getIssue(key).claim();
		} catch(RestClientException e){
			if(e.getStatusCode().get()==404){
				log.debug("Issue("+key+") is not exist.");
			}else{
				log.error("Exceptioned when get issue("+key+").",e);
			}
			return null;
		} catch (URISyntaxException e) {
			log.error("Invalid jira server uri.",e);
			return null;
		} finally{
			try {
				destory(restClient);
			} catch (IOException e) {
				log.warn("Jira rest client closed failed.", e);
			}
		}
	}
	
	/**
	 * @author chenjinfan
	 * @Description 根据project的ID查找project
	 * @param key  项目key
	 * @return
	 * @throws NeedAuthorizeException 
	 */
	public static Project getProject(String key) throws NeedAuthorizeException{
		JiraRestClient restClient = null;
		try {
			restClient = getClient();
			return restClient.getProjectClient().getProject(key).claim();
		} catch(RestClientException e){
			if(e.getStatusCode().get()==404){
				log.debug("Project("+key+") is not exist.");
			}else{
				log.error("Exceptioned when get project("+key+").",e);
			}
			return null;
		} catch (URISyntaxException e) {
			log.error("Invalid jira server uri.",e);
			return null;
		} finally{
			try {
				destory(restClient);
			} catch (IOException e) {
				log.warn("Jira rest client closed failed.", e);
			}
		}
	}
	
	/**
	 * @author chenjinfan
	 * @Description  获取所有项目
	 * @return
	 * @throws NeedAuthorizeException 
	 */
	public static List<BasicProject> getAllProjects() throws NeedAuthorizeException{
		JiraRestClient restClient = null;
		try {
			restClient = getClient();
			List<BasicProject> list = new ArrayList<BasicProject>();
			Iterator<BasicProject> iterator = restClient.getProjectClient().getAllProjects().claim().iterator();
			while(iterator.hasNext()){
				list.add(iterator.next());
			}
			return list;
		} catch(RestClientException e){
			log.error("Exceptioned when get all projects.",e);
			return null;
		} catch (URISyntaxException e) {
			log.error("Invalid jira server uri.",e);
			return null;
		} finally{
			try {
				destory(restClient);
			} catch (IOException e) {
				log.warn("Jira rest client closed failed.", e);
			}
		}
	}
	
	/**
	 * @author chenjinfan
	 * @Description 获取项目的issue列表，可分页
	 * @param projectKey
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws NeedAuthorizeException 
	 */
	public static Page<Issue> getProjectIssuesByPage(String projectKey,int pageNo,int pageSize) throws NeedAuthorizeException{
		String jql = "project="+projectKey;
		return getPageByJql(jql,pageNo,pageSize);
	}
	/**
	 * @author chenjinfan
	 * @Description 执行jql，可分页
	 * @param jql
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws NeedAuthorizeException 
	 */
	public static Page<Issue> getPageByJql(String jql,int pageNo,int pageSize) throws NeedAuthorizeException{
		try {
			return getPageByJqlBase(jql, pageNo, pageSize);
		} catch(RestClientException e){
			log.error("Exceptioned when get issue with jql '"+jql+"'.",e);
			return null;
		} catch (URISyntaxException e) {
			log.error("Invalid jira server uri.",e);
			return null;
		}
	}
	/**
	 * 此方法不处理异常
	 * @author chenjinfan
	 * @Description
	 * @param jql
	 * @param pageNo
	 * @param pageSize
	 * @return
	 * @throws NeedAuthorizeException
	 * @throws URISyntaxException
	 */
	public static Page<Issue> getPageByJqlBase(String jql,int pageNo,int pageSize) throws NeedAuthorizeException, URISyntaxException{
		JiraRestClient restClient = null;
		Set<String> filter = new HashSet<String>();
		filter.add("*all");
		Page<Issue> page = new Page<>(pageNo, pageSize);
		int maxResults = pageSize;
		int startAt = page.getStart();
		try {
			restClient = getClient();
			SearchResult result = restClient.getSearchClient().searchJql(jql, maxResults, startAt, filter).claim();
			Iterator<Issue> iterator = result.getIssues().iterator();
			List<Issue> list = new ArrayList<Issue>();
			while(iterator.hasNext()){
				list.add(iterator.next());
			}
			page.addAll(list);
			page.setRecordCount(result.getTotal());
			return page;
		} finally{
			try {
				destory(restClient);
			} catch (IOException e) {
				log.warn("Jira rest client closed failed.", e);
			}
		}
	}
	public static List<Issue> getByJql(String jql) throws NeedAuthorizeException{
		JiraRestClient restClient = null;
		try {
			restClient = getClient();
			SearchResult result = restClient.getSearchClient().searchJql(jql).claim();
			Iterator<Issue> iterator = result.getIssues().iterator();
			List<Issue> list = new ArrayList<Issue>();
			while(iterator.hasNext()){
				list.add(iterator.next());
			}
			return list;
		} catch(RestClientException e){
			log.error("Exceptioned when get issue with jql '"+jql+"'.",e);
			return null;
		} catch (URISyntaxException e) {
			log.error("Invalid jira server uri.",e);
			return null;
		} finally{
			try {
				destory(restClient);
			} catch (IOException e) {
				log.warn("Jira rest client closed failed.", e);
			}
		}
	}
	
	/**
	 * @author chenjinfan
	 * @Description 获取项目使用的问题类型
	 * @param projectKey
	 * @return
	 * @throws NeedAuthorizeException 
	 */
	public static List<IssueType> getIssueTypes(String projectKey) throws NeedAuthorizeException{
		JiraRestClient restClient = null;
		try {
			restClient = getClient();
			Project project = restClient.getProjectClient().getProject(projectKey).claim();
			Iterator<IssueType> iterator = project.getIssueTypes().iterator();
			List<IssueType> list = new ArrayList<IssueType>();
			while(iterator.hasNext()){
				list.add(iterator.next());
			}
			return list;
		} catch(RestClientException e){
			log.error("Exceptioned when get issue types of project '"+projectKey+"'.",e);
			return null;
		} catch (URISyntaxException e) {
			log.error("Invalid jira server uri.",e);
			return null;
		} finally{
			try {
				destory(restClient);
			} catch (IOException e) {
				log.warn("Jira rest client closed failed.", e);
			}
		}
	}
	
	/**
	 * 查询某个project 某个时间段的bug数、需求数、原预估消耗时间
	 * @author chenjinfan
	 * @Description
	 * @return demandCount:故事数，bugCount:bug数,timeSpent:原预估消耗时间
	 * @throws Exception 
	 * @throws NeedAuthorizeException
	 */
	public static Map<String, Object> getStatisticsDataNeedBug(String projectKey,String startTime,String endTime) throws Exception{
		String dateFilter = statisticsAddFilter(projectKey, startTime, endTime,"created");
		Page<Issue> page;
		int totalCount=0;
		int timeSpent = 0;
		int bugCount=getStatisticsDataBug(projectKey, startTime, endTime);
		try {
			page = getPageByJqlBase(dateFilter, 1, 1);
			totalCount = page.getRecordCount();
			Iterator<Issue> it = page.iterator();
			while(it.hasNext()){
				Issue issue = it.next();
				Object timeEst = issue.getField("aggregatetimeoriginalestimate").getValue();
				if(timeEst!=null && issue.getIssueType().getName()!="subtask" 
						&& issue.getIssueType().getName()!="子任务"){
					timeSpent += (Integer)timeEst;
				}
			}
		} catch (Exception e) {
			log.info("get jira all issue count failed.",e);
			throw e;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("timeSpent", timeSpent);
		map.put("demandCount", totalCount-bugCount);
		map.put("bugCount", bugCount);
		return map;
	}
	/**
	 * 查询某个project 某个时间段的bug数
	 * @author chenjinfan
	 * @Description
	 * @return requirementCount:故事数，bugCount:bug数
	 * @throws Exception 
	 * @throws NeedAuthorizeException
	 */
	public static int getStatisticsDataBug(String projectKey,String startTime,String endTime) throws Exception{
		String dateFilter = statisticsAddFilter(projectKey, startTime, endTime,"created");
		Page<Issue> page;
		int bugCount=0;
		boolean dataFound = true;
		try {
			page = getPageByJqlBase("issuetype=缺陷 and "+dateFilter, 1, 1);
			bugCount = page.getRecordCount();
		} catch (Exception e) {
			dataFound = false;
		}
		try {
			page = getPageByJqlBase("issuetype=bug and "+dateFilter, 1, 1);
			bugCount += page.getRecordCount();
		} catch (Exception e) {
			if(!dataFound){
				throw e;
			}
		}
		return bugCount;
	}
	protected static String statisticsAddFilter(String projectKey,
			String startTime, String endTime, String propertyKey) {
		String dateFilter = "";
		try {
			if(StringUtil.isNotNull(startTime)){
				dateFilter += " "+propertyKey+">='"+startTime.substring(0,16)+"' ";
			}
			if(StringUtil.isNotNull(endTime)){
				dateFilter += " and "+propertyKey+"<'"+endTime.substring(0,16)+"' ";
			}
			if(StringUtil.isNotNull(projectKey)){
				dateFilter += " and project='"+projectKey+"' ";
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Time format is not yyyy-MM-dd HH:mm:ss.SSS.");
		}
		return dateFilter;
	}
	/**
	 * Only available if time-tracking has been enabled by your JIRA administrator.
	 * @author chenjinfan
	 * @Description
	 * @param projectKey
	 * @param startTime
	 * @param endTime
	 * @return resolvedTimeSpent：耗费时间，resolvedCount:解决问题数量
	 * @throws Exception 
	 */
	public static Map<String, Object> getResolvedIssueStatistics(String projectKey,String startTime,String endTime) throws Exception{
		String dateFilter = statisticsAddFilter(projectKey, startTime, endTime,"resolved");
		Page<Issue> page;
		Double timeSpent = 0.0;
		Integer resolvedCount=0;
		try {
			page = getPageByJqlBase(dateFilter, 1, 500);
			resolvedCount = page.getRecordCount();
			Iterator<Issue> it = page.iterator();
			while(it.hasNext()){
				Issue issue = it.next();
				Integer timeSpentMinutes = issue.getTimeTracking().getTimeSpentMinutes();
				if(timeSpentMinutes != null){
					timeSpent += timeSpentMinutes*60;
				}else{
					Object startDate = issue.getField("customfield_10010").getValue();
					if(startDate != null){
						DateTime t1 = new DateTime(startDate);
						Object s1 = issue.getField("resolutiondate").getValue();
						if(t1 != null){
							DateTime t2 = new DateTime((String)s1);
							if(t2!=null){
								timeSpent += (t2.getMillis()-t1.getMillis())/1000;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resolvedTimeSpent", timeSpent);
		map.put("resolvedCount", resolvedCount);
		return map;
	}
}