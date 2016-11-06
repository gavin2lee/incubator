package com.harmazing.openbridge.project.util;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.apache.log4j.Logger;
import org.springframework.web.util.UriUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.common.Page;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.DateUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.http.client.GetRequest;
import com.harmazing.framework.util.http.client.HttpConnector;
import com.harmazing.framework.util.http.client.HttpConnector.Builder;
import com.harmazing.framework.util.http.client.HttpException;
import com.harmazing.framework.util.http.client.PostRequest;
import com.harmazing.framework.util.http.client.WsConnector;
import com.harmazing.framework.util.http.client.WsResponse;
import com.harmazing.openbridge.project.vo.JenkinsConfigVO;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
public class JenkinsRestUtil {
	
	public final Logger logger = Logger.getLogger(this.getClass());
	
	protected static WsConnector getConnector() {
		String url = ConfigUtil.getConfigString("jenkins.serverUri");
		String login = ConfigUtil.getConfigString("jenkins.username");
		String password = ConfigUtil.getConfigString("jenkins.password");
		Builder builder = new HttpConnector.Builder();
		builder.url(url);
		builder.credentials(login, password);
		WsConnector wsConnector = builder.build();
		return wsConnector;
	}
	protected static String path(String controller,String action) {
	    return String.format("%s/%s", controller, action);
	}
	protected static String path(String... components ) {
	    return org.apache.commons.lang.StringUtils.join(components, "/");
	}
	public static String genBuildUrl(String jobName, int number) throws UnsupportedEncodingException{
		return path(ConfigUtil.getConfigString("jenkins.serverUri"),"job",encodeUrlCompnent(jobName),number+"");
	}
	/**
	 * 获取jenkins job 配置
	 * @author chenjinfan
	 * @Description
	 * @param jobName
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static String getConfig(String jobName) throws HttpException, UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		GetRequest get = new GetRequest(path("job", jobName,"config.xml"));
		WsConnector connector = getConnector();
		try{
			WsResponse response = connector.call(get).failIfNotSuccessful();
			return response.content();
		}catch(Exception e){
			return "";
		}
	}
	
	
	/**
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws UnsupportedEncodingException 
	 * @throws HttpException 
	* @throws TransformerException 
	* @throws TransformerFactoryConfigurationError 
	* @throws TransformerConfigurationException 
	* @Title: updateSourceConfig 
	* @author weiyujia
	* @Description: 修改config.xml源码配置信息
	* @param @param jobName  jenkins App名称
	* @param @param type 源码管理类型 ，分 svn,git,cvs等
	* @param @param url 源码管理URL  
	* @param @param credentials  验证信息
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	*/
	public static String updateSourceConfig(String jenkinsXml,String type ,String url ,String credentialsId) throws TransformerFactoryConfigurationError, TransformerException, HttpException, ParserConfigurationException, SAXException, IOException{
		return ParseXmlUtil.updateSourceConfig(jenkinsXml, type, url);
	}
	
	/**
	 * @throws UnsupportedEncodingException 
	 * @throws HttpException 
	* @throws TransformerException 
	* @throws TransformerFactoryConfigurationError 
	* @Title: updateMavenConfig 
	* @author weiyujia
	* @Description: 更新Jenkins maven 配置 
	* @param @param jobName Job名称
	* @param @param type 源码管理类型
	* @param @param url 源码路径
	* @param @param credentials  认证key值
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String updateMavenConfig(String jenkinsXml, String pom ,String mavenGoal) throws TransformerFactoryConfigurationError, TransformerException, HttpException, UnsupportedEncodingException{
//		String jenkinsXml = JenkinsRestUtil.getConfig(jobName);
		return ParseXmlUtil.updateMavenConfig(jenkinsXml, pom, mavenGoal);
	}
	/**
	 * 更新job 配置
	 * @author chenjinfan
	 * @Description
	 * @param jobName
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean updateConfig(String jobName,final String content) throws HttpException, UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		PostRequest post = new PostRequest(path("job", jobName,"config.xml")){
			@Override
			public boolean setRequestProperties(Request.Builder okRequestBuilder){
				okRequestBuilder.post(RequestBody.create(MediaType.parse("application/xml"), content));
				return true;
			}
		};
		WsConnector connector = getConnector();
		WsResponse response = connector.call(post).failIfNotSuccessful();
		return response.isSuccessful();
	}
	
	/**
	 * 删除job
	 * @author chenjinfan
	 * @Description
	 * @param jobName
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean deleteJob(String jobName) throws HttpException, UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		PostRequest post = new PostRequest(path("job", jobName,"doDelete"));
		WsConnector connector = getConnector();
		WsResponse response = connector.call(post).failIfNotSuccessful();
		return response.isSuccessful();
	}
	
	/**
	 * 创建job
	 * @author chenjinfan
	 * @Description
	 * @param jobName
	 * @param mode
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static boolean createJob(String jobName, String mode) throws HttpException, UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required when create job.");
//		jobName = encodeUrlCompnent(jobName);
		if(StringUtil.isNull(mode)){
			mode = encodeUrlCompnent("hudson.maven.MavenModuleSet");
		}
		PostRequest post = new PostRequest("createItem"){
			@Override
			public boolean setRequestProperties(Request.Builder okRequestBuilder){
				okRequestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), ""));
//				okRequestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
				return true;
			}
		};
		post.setParam("name", jobName);
		post.setParam("mode", mode);
		WsConnector connector = getConnector();
		WsResponse response = connector.call(post).failIfNotSuccessful();
		return response.isSuccessful();
	}
	
	/**
	 * 构建。成功返回201，location 头中为状态URL
	 * @author chenjinfan
	 * @Description
	 * @param jobName
	 * @return  状态URL
	 * @throws HttpException
	 * @throws UnsupportedEncodingException
	 */
	public static String build(String jobName) throws HttpException,UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		PostRequest post = new PostRequest(path("job", jobName,"build"));
		post.setParam("delay", "0sec");
		WsConnector connector = getConnector();
		WsResponse response = connector.call(post).failIfNotSuccessful();
		if(201==response.code()){
			String url = response.header("location");
			return url;
		}
		return "";
	}
	
	/**
	 * 获取日志（文本格式）
	 * @author chenjinfan
	 * @Description
	 * @param jobName
	 * @param number
	 * @return
	 * @throws HttpException
	 * @throws UnsupportedEncodingException
	 */
	public static String getConsole(String jobName, String number) throws HttpException,UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		GetRequest get = new GetRequest(path("job", jobName,number,"consoleText"));
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		return response.content();
	}
	
	/**
	 * @author chenjinfan
	 * @Description 增量获取日志（html格式）
	 * @param buildUrl
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static Map<String, Object> getLogStream(String jobName, Integer number, Integer start) throws HttpException, UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		checkArgument(number!=null, "Number is required.");
		String buildUrl = genBuildUrl(jobName, number);
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtil.isNotNull(buildUrl)){
			buildUrl += "/logText/progressiveHtml";
			GetRequest get = new GetRequest(buildUrl);
			get.setParam("start", start);
			WsConnector connector = getConnector();
			WsResponse response = connector.call(get).failIfNotSuccessful();
			String hasMoreString = response.header("X-More-Data");
			boolean hasMore = Boolean.parseBoolean(hasMoreString);
			String lineNumber = response.header("X-Text-Size");
			String conString = response.content();
			map.put("hasMore", hasMore);
			map.put("lineNumber", lineNumber);
			map.put("log", conString);
		}
		return map;
	}
	/**
	 * @author chenjinfan
	 * @Description 判断job是否存在
	 * @param jobName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static boolean isJobExists(String jobName) throws UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		GetRequest get = new GetRequest(path("job", jobName));
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get);
		return response.isSuccessful();
	}
	protected static String encodeUrlCompnent(String jobName)
			throws UnsupportedEncodingException {
		jobName = UriUtils.encodePathSegment(jobName, "utf-8");
		jobName = jobName.replaceAll("%2F", "/");
		jobName = jobName.replaceAll("%252F", "/");
		jobName = jobName.replaceAll("%253A", ":");
//		jobName = URLEncoder.encode(jobName,"utf-8");%253
		return jobName;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 分页查询job 构建历史数据
	 * @param jobName
	 * @param pageNo 当前页number
	 * @param pageSize 每页数量
	 * @return data:结果集JSON字符串， total:总数量
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Page<Map<String, Object>> getBuildHistory(String jobName,int pageNo,int pageSize) throws HttpException, UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		checkArgument(pageNo>=0, "Page number should >= 0.");
		checkArgument(pageSize>=0, "Page size should >= 0.");
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNo,pageSize);
		String properties = "modules[name],allBuilds[displayName,duration,building,result,timestamp,url,number,artifacts[*]]";
		jobName = encodeUrlCompnent(jobName);
		String pageParam = "{"+((pageNo-1)*pageSize)+","+(pageNo*pageSize)+"}";
		GetRequest get = new GetRequest(addApiUrlSuffix(path("job", jobName)));
		get.setParam("tree", properties+pageParam);
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		String dataString = response.content();
		JSONArray array = JSON.parseObject(dataString).getJSONArray("allBuilds");
		Iterator ita = array.iterator();
//		String artifactModule = "com.harmazing:ob-framework";//
		String artifactModule = "";
		try {
			artifactModule = getFileBelongModel(jobName);
			if(StringUtil.isNull(artifactModule)){
				JSONArray modules = JSON.parseObject(dataString).getJSONArray("modules");
				if(modules!=null && modules.size()==1){
					artifactModule = modules.getJSONObject(0).getString("name");
				}
			}
		} catch (Exception e1) {
			
		}
		while (ita.hasNext()) {
			JSONObject object = (JSONObject) ita.next();
			JSONArray jsonArray = object.getJSONArray("artifacts");
			if(jsonArray!=null){
				Iterator it2 = jsonArray.iterator();
				while (it2.hasNext()) {
					JSONObject object2 = (JSONObject) it2.next();
					String fileName = object2.getString("fileName");
					if(!(fileName.endsWith(".war") || fileName.endsWith(".jar"))){
						it2.remove();
					}
				}
			}
			if(StringUtil.isNotNull(artifactModule) && !artifactModule.equals(jobName)){
				try{
					jsonArray.addAll(getJobArtifact(jobName+"/"+artifactModule, object.getString("number")));
				}catch(HttpException e){}
			}
			Map foo  = JSON.toJavaObject(object, Map.class);
			foo = handleBuildRecord(object);
			page.add(foo);
		}
		//get builds total number
		get = new GetRequest(addApiUrlSuffix(path("job", jobName)));
		get.setParam("tree", "lastBuild[number]");
		response = connector.call(get).failIfNotSuccessful();
		/*int total = 0;
		if(response.isSuccessful()){
			String lastBuiltString = response.content();
			if(StringUtil.isNotNull(lastBuiltString)){
				JSONObject lastBuild = JSONObject.parseObject(lastBuiltString).getJSONObject("lastBuild");
				if(lastBuild!=null){
					total = lastBuild.getInteger("number");
				}
			}
		}
		page.setRecordCount(total);*/
		return page;
	}
	/**
	 * @author chenjinfan
	 * @Description 数据展示格式处理，如时长、时间转为多久以前
	 * @param foo
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected static Map handleBuildRecord(Map foo) {
		if(foo == null)	return foo;
		Integer duration = (Integer) foo.get("duration");
		if(duration != null){
			foo.put("durationShow", DateUtil.showDueSecondsNature((int)(Math.round(duration/1000.0)), 2));
		}
		Long timestamp = (Long) foo.get("timestamp");
		if(timestamp != null){
			foo.put("timestamp", new Date(timestamp));
			foo.put("daysAgo", DateUtil.showDueSecondsNature(((int)(Math.round((System.currentTimeMillis()-timestamp)/1000.0))), 2));
		}
		return foo;
	}
	/**
	 * @author chenjinfan
	 * @Description 获取正在进行的构建
	 * @param jobName
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String, Object>> getBuildingRecores(String jobName) throws HttpException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		List<Map<String, Object>> list  = new ArrayList<Map<String, Object>>();
		String properties = "builds[displayName,building,timestamp,number]";
		GetRequest get = new GetRequest(addApiUrlSuffix(path("job", jobName)));
		get.setParam("tree", properties+"{0,5}");
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		String dataString = response.content();
		JSONArray array = JSON.parseObject(dataString).getJSONArray("builds");
		Iterator ita = array.iterator();
		while (ita.hasNext()) {
			JSONObject object = (JSONObject) ita.next();
			if(object.getBooleanValue("building")){
				Map foo  = JSON.toJavaObject(object, Map.class);
				Long timestamp = object.getLong("timestamp");
				if(timestamp!=null){
					foo.put("startAgo", DateUtil.showDueSecondsNature((int)Math.round((System.currentTimeMillis()-timestamp)/1000.0),2));
				}
				list.add(foo);
			}
		}
		return list;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 获取job 信息
	 * @param jobName
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String, Object> getJobData(String jobName) throws HttpException{
		String buildP = "[displayName,timestamp,number,result,artifacts[*]]";
		String tree = "buildable,description,lastBuild"+buildP+","
				+ "lastStableBuild"+buildP+",lastFailedBuild"+buildP+",lastSuccessfulBuild"+buildP+","
				+ "lastUnstableBuild"+buildP+",lastUnsuccessfulBuild"+buildP+","
				+ "lastCompletedBuild"+buildP+",downstreamProjects[name,color],upstreamProjects[name,color],"
				+ "modules[buildable,name,color,displayName,healthReport[*],lastBuild[displayName,duration,result,number],"
				+ "lastFailedBuild"+buildP+",lastSuccessfulBuild"+buildP+"]";
		GetRequest get = new GetRequest(addApiUrlSuffix(path("job", jobName)));
		get.setParam("tree", tree);
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		String dataString = response.content();
		Map<String, Object> result = new HashMap<String, Object>();
		JSONObject resultObject = JSON.parseObject(dataString);
		if(resultObject==null)	return result;
		result  = JSON.toJavaObject(resultObject, Map.class);
		result.put("lastBuild", handleBuildRecord((Map) result.get("lastBuild")));
		result.put("lastStableBuild", handleBuildRecord((Map) result.get("lastStableBuild")));
		result.put("lastFailedBuild", handleBuildRecord((Map) result.get("lastFailedBuild")));
		result.put("lastSuccessfulBuild", handleBuildRecord((Map) result.get("lastSuccessfulBuild")));
		result.put("lastUnstableBuild", handleBuildRecord((Map) result.get("lastUnstableBuild")));
		result.put("lastUnsuccessfulBuild", handleBuildRecord((Map) result.get("lastUnsuccessfulBuild")));
		result.put("lastCompletedBuild", handleBuildRecord((Map) result.get("lastCompletedBuild")));
		//模块处理
		JSONArray array = resultObject.getJSONArray("modules");
		if(array!=null){
			List<Map<String, Object>> modules = new ArrayList<Map<String, Object>>();
			Iterator ita = array.iterator();
			while (ita.hasNext()) {
				JSONObject object = (JSONObject) ita.next();
				Map foo  = JSON.toJavaObject(object, Map.class);
				Map lastBuild = (Map) foo.get("lastBuild");
				foo.put("lastBuild", handleBuildRecord(lastBuild));
				foo.put("lastStableBuild", handleBuildRecord((Map) foo.get("lastStableBuild")));
				foo.put("lastFailedBuild", handleBuildRecord((Map) foo.get("lastFailedBuild")));
				foo.put("lastSuccessfulBuild", handleBuildRecord((Map) foo.get("lastSuccessfulBuild")));
				foo.put("lastUnstableBuild", handleBuildRecord((Map) foo.get("lastUnstableBuild")));
				foo.put("lastUnsuccessfulBuild", handleBuildRecord((Map) foo.get("lastUnsuccessfulBuild")));
				foo.put("lastCompletedBuild", handleBuildRecord((Map) foo.get("lastCompletedBuild")));
				modules.add(foo);
			}
			result.put("modules", modules);
		}
		return result;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 获取war 包文件的路径
	 * @param jobName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Map> getJobArtifact(String jobName, String number){
		if(StringUtil.isNull(number)){
			number = "lastSuccessfulBuild";
		}
		List<Map> list = new ArrayList<Map>();
		GetRequest get = new GetRequest(addApiUrlSuffix(path("job", jobName, number)));
		get.setParam("tree", "artifacts[*]");
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		String dataString = response.content();
		JSONObject resultObject = JSON.parseObject(dataString);
		if(resultObject!=null){
			JSONArray array = resultObject.getJSONArray("artifacts");
			Iterator iterator  = array.iterator();
			while (iterator.hasNext()) {
				JSONObject object = (JSONObject) iterator.next();
				String fileName = object.getString("fileName");
				object.put("jobName", jobName);
				if(fileName.endsWith(".war") || fileName.endsWith(".zip") || fileName.endsWith(".jar")){
					list.add(JSON.toJavaObject(object, Map.class));
				}
			}
		}
		return list;
	}
	
	public static List<Map<String, Object>> getArtifacts(String jobName, String number){
		List<String> pathList = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		try {
			pathList = getArtifact(jobName,number.toString());
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
		return result;
	}
	@SuppressWarnings("rawtypes")
	public static List<String> getArtifact(String jobName, String number){
		List<String> list = new ArrayList<String>();
		if(jobName.indexOf("/")!=-1){//只查询子生成
			List<Map> artifacts = getJobArtifact(jobName,number);
			for(Map map : artifacts){
				list.add(map.get("relativePath")+"_-_"+jobName);
			}
		}
		GetRequest get = new GetRequest(addApiUrlSuffix(path("job", jobName)));
		get.setParam("tree", "modules[name]");
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		String dataString = response.content();
		JSONObject resultObject = JSON.parseObject(dataString);
		if(resultObject!=null){
			JSONArray modules = resultObject.getJSONArray("modules");
			if(modules!=null){
				Iterator iterator = modules.iterator();
				while (iterator.hasNext()) {
					JSONObject object = (JSONObject) iterator.next();
					list.addAll(getArtifact(jobName+"/"+object.getString("name"),number));
				}
			}
  		}
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<Map<String, Object>> getQueueTasks(String jobName) throws HttpException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		List<Map<String, Object>> list  = new ArrayList<Map<String, Object>>();
		GetRequest get = new GetRequest(addApiUrlSuffix("queue"));
		get.setParam("tree", "items[actions[causes[shortDescription]],inQueueSince,task[name],why]");
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		String dataString = response.content();
		JSONArray array = JSON.parseObject(dataString).getJSONArray("items");
		Iterator iterator = array.iterator();
		while (iterator.hasNext()) {
			JSONObject object = (JSONObject) iterator.next();
			Map foo  = JSON.toJavaObject(object, Map.class);
			if(jobName.equals(object.getJSONObject("task").getString("name"))){
				long inQueueSince = object.getLong("inQueueSince");
				foo.put("waited", DateUtil.showDueSecondsNature((int)Math.round((System.currentTimeMillis()-inQueueSince)/1000.0),2));
				list.add(foo);
			}
		}
		return list;
	}
	
	/**
	 * @author chenjinfan
	 * @Description 是否正在构建
	 * @param jobName job名称
	 * @param number 构建编号
	 * @return
	 * @throws HttpException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean isBuilding(String jobName,Integer number) throws HttpException,UnsupportedEncodingException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		checkArgument(number!=null, "Number is required.");
		String key = "building";
		String buildUrl = genBuildUrl(jobName, number);
		GetRequest get = new GetRequest(addApiUrlSuffix(buildUrl));
		get.setParam("tree", key);
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		String dataString = response.content();
		return JSON.parseObject(dataString).getBooleanValue(key);
	}
	
	/**
	 * @author chenjinfan
	 * @Description 修改job 的描述
	 * @param jobName
	 * @param description
	 * @return
	 */
	public static void updateDscription(String jobName,String description) throws HttpException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		PostRequest get = new PostRequest(path("job",jobName,"description"));
		get.setParam("description", description);
		WsConnector connector = getConnector();
		connector.call(get).failIfNotSuccessful();
	}
	
	/**
	 * @author chenjinfan
	 * @Description 启用/禁用 
	 * @param jobName
	 * @param enable
	 * @throws HttpException
	 */
	public static void switchJob(String jobName,boolean enable) throws HttpException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		PostRequest post = new PostRequest(path("job",jobName,enable?"enable":"disable"));
		WsConnector connector = getConnector();
		connector.call(post).failIfNotSuccessful();
	}
	
	/**
	 * @author chenjinfan
	 * @Description 查看/下载文件
	 * @param jobName 
	 * @param number 构建号
	 * @param path 文件路径
	 * @param out 接收输出流
	 * @throws IOException
	 */
	public static void viewFile(String jobName, String number, String path,OutputStream out) throws IOException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		checkArgument(number!=null, "number is required.");
		checkArgument(!isNullOrEmpty(path), "path is required.");
		GetRequest get = new GetRequest(path("job",jobName,number.toString(),"artifact",path));
		writeRequestToOutput(out, get);
	}
	protected static void writeRequestToOutput(OutputStream out, GetRequest get)
			throws IOException {
		WsConnector connector = getConnector();
		InputStream contentStream = connector.call(get).failIfNotSuccessful().contentStream();
		byte[] buffer  = new byte[10*1024];
		int c=0;
		while((c = contentStream.read(buffer))>0){
			out.write(buffer, 0, c);
			out.flush();
		}
	}
	
	/**
	 * @author chenjinfan
	 * @Description 测试趋势map地图
	 * @param jobName
	 * @param queryString
	 * @param out
	 * @throws IOException
	 */
	public static String testTrendMap(String jobName, String queryString) {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		GetRequest get = new GetRequest(path("job",jobName,"test","trendMap")+"?"+queryString);
		WsConnector connector = getConnector();
		WsResponse wsresponse = connector.call(get);
		String con = wsresponse.content();
//		con = con.replaceAll("testReport", "testReport.do");
		if(wsresponse.isSuccessful()){
			return con;
		}
		return "";
	}
	/**
	 * @author chenjinfan
	 * @Description 测试趋势图
	 * @param jobName
	 * @param queryString 请求url参数
	 * @param out
	 * @throws IOException
	 */
	public static void testTrend(String jobName, String queryString, OutputStream out) throws IOException{
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		GetRequest get = new GetRequest(path("job",jobName,"test","trend")+"?"+queryString);
		try{
			writeRequestToOutput(out, get);
		}catch(HttpException e){
			
		}
	}
	
	/**
	* @Title: updateJenkinsConfigByVO 
	* @author weiyujia
	 * @param jobName 
	* @Description: 全局更新配置组织文件，VO里的字段对应config.xml里的节点字段
	* @param configVO 配置文件VO，
	* @return  string 返回xml字符串
	 * @throws HttpException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	* @date 2016年4月12日 下午5:54:18
	 */
	public static String updateJenkinsConfigByVO(String jobName, JenkinsConfigVO configVO) throws HttpException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		String configXml = JenkinsRestUtil.getConfig(jobName);
		return ParseXmlUtil.updateXmlStr(configVO,configXml);
	}
	
	/**
	* @Title: getChangeHistory 
	* @author weiyujia
	* @Description:获取job变更记录 
	* @param jobName jobName
	* @return List<Map<String,Object>>  返回结果集
	 * @throws UnsupportedEncodingException 
	* @date 2016年4月13日 下午3:49:58
	 */
	public static String getChangeHistory(String jobName) throws UnsupportedEncodingException {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		String properties = "builds[changeSet[items[affectedPaths,author[fullName],msg,id]],number,url,timestamp,displayName]";
		GetRequest get = new GetRequest(addApiUrlSuffix(path("job", jobName)));
		get.setParam("tree", properties);
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get);
		return response.content();
		
	}
	
	/**
	* @Title: getChangeHistoryData 
	* @author weiyujia
	* @Description: 封装变更记录界面数据 
	* @param content
	* @return  设定文件 
	* @return List<Map<String,Object>>    返回类型 
	* @throws 
	* @date 2016年4月13日 下午8:51:57
	 */
	public static List<Map<String,Object>> getChangeHistoryData(String content) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		JSONArray array = JSON.parseObject(content).getJSONArray("builds");
		Iterator ita = array.iterator();
		Map itemMap = new HashMap();
		while (ita.hasNext()) {
			JSONObject object = (JSONObject) ita.next();
			JSONObject changeSetObject = object.getJSONObject("changeSet");
			if(!changeSetObject.getString("items").equals("[]")){
				String itemDisplayName = object.getString("displayName");
				object.put("displayName", itemDisplayName); 
				Long timestamp = (Long) object.get("timestamp");
				object.put("timestamp", new Date(timestamp)); 
				JSONArray itemArr = changeSetObject.getJSONArray("items");
				Iterator changeSetIter = itemArr.iterator();
				while(changeSetIter.hasNext()){
					String a = "";
				}
				resultList.add(itemMap);
			}
		}
		return null;
	}
	
	
	
	
	
	
	/**
	 * 查看任务在构建队列中的状态
	 * @author chenjinfan
	 * @Description
	 * @param ResourceUrl 构建时返回的queue中任务的URL
	 * @return 状态json字符串
	 */
	public static String getQueueStatus(String ResourceUrl) throws HttpException{
		return getByUrl(addApiUrlSuffix(ResourceUrl));
	}
	protected static String addApiUrlSuffix(String ResourceUrl) {
		return path(ResourceUrl,"/api/json");
	}
//	public static String getBuildStatus(String ResourceUrl) throws HttpException{
//		return getByUrl(addApiUrlSuffix(ResourceUrl));
//	}
	/**
	 * @author chenjinfan
	 * @Description 是否正在构建
	 * @param buildStatus
	 * @return
	 */
	/*public static boolean isBuilding(String buildStatus){
		if(StringUtil.isNull(buildStatus)){
			return false;
		}
		JSONObject parseObject = JSON.parseObject(buildStatus);
		return parseObject.getBooleanValue("building");
	}*/
	/**
	 * @author chenjinfan
	 * @Description
	 * @param queueStatus 状态json字符串
	 * @return buildUrl
	 */
//	public static String getBuildUrl(String queueStatus) {
//		JSONObject executable = JSON.parseObject(queueStatus).getJSONObject("executable");
//		if(executable != null){
//			String buildUrl = executable.getString("url");
//			return buildUrl;
//		}
//		return "";
//	}
	

	/**
	 * @author chenjinfan
	 * @Description
	 * @param buildUrl 
	 * @return 日志（文本格式）
	 */
//	protected static String getConsole(String buildUrl) {
//		if(StringUtil.isNotNull(buildUrl)){
//			buildUrl += "/consoleText";
//			GetRequest get = new GetRequest(buildUrl);
//			WsConnector connector = getConnector();
//			WsResponse response = connector.call(get).failIfNotSuccessful();
//			String conString = response.content();
//			return conString;
//		}
//		return "";
//	}
	protected static String getByUrl(String ResourceUrl) {
		checkArgument(!isNullOrEmpty(ResourceUrl), "Resource url is null.");
		GetRequest get = new GetRequest(ResourceUrl);
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get).failIfNotSuccessful();
		String conString = response.content();
		return conString;
	}
	
	/**
	* @Title: checkRemote 
	* @author weiyujia
	* @Description: 校验url是否通过代码pull校验
	* @param appName   jobName
	* @param remote   url
	* @param type   scm类型
	* @return  设定文件 
	* @return String    返回类型 
	* @throws 
	* @date 2016年4月15日 下午6:30:28
	 */
	public static String checkRemote(String jobName, String remote, String type) {
		String scmClass = "";
		if("SVN".equals(type)){
			scmClass = "hudson.scm.SubversionSCM";
		}
		GetRequest get = new GetRequest(path(path("job", jobName,"descriptorByName",scmClass,"checkRemote")));
		get.setParam("value", "http://192.168.231.254:88/svn1/crmproject/trunk");
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get);
		return response.content();
	}
	
	/**
	* @Title: getTestReportData 
	* @author weiyujia
	* @Description: 获取测试报告数据
	* @param jobName  
	* @param number 
	* @return String    返回类型 
	 * @throws UnsupportedEncodingException 
	* @throws 
	* @date 2016年4月20日 下午8:04:07
	*/
	public static String getTestReportData(String jobName, String number) throws UnsupportedEncodingException {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		jobName = encodeUrlCompnent(jobName);
		GetRequest get = new GetRequest(path("job",jobName, number,"testReport","api/json"));
		get.setParam("depth", "1");
		WsConnector connector = getConnector();
		WsResponse response = connector.call(get);
		if(200 == response.code()){
			return response.content(); 
		};
		return "[]";
	}
	
	/**
	* @Title: getFileBelongModel 
	* @author weiyujia
	* @Description: 获取job配置文件里 FileBelongModel 节点值
	* @param jobName
	* @return
	* @throws HttpException
	* @return String    返回类型 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	* @throws 
	* @date 2016年4月22日 下午2:53:18
	 */
	public static String getFileBelongModel(String jobName) throws HttpException, ParserConfigurationException, SAXException, IOException{
		String configXml = JenkinsRestUtil.getConfig(jobName);
		return ParseXmlUtil.getFileBelongModel(configXml);
	}
	
	/**
	* @Title: testHistoryTrend 
	* @author weiyujia
	* @Description: 获取测试结果历史趁势图
	* @param jobName
	* @param number
	 * @param type 
	* @param queryString
	* @param outputStream  设定文件 
	* @return void    返回类型 
	 * @throws IOException 
	* @throws 
	* @date 2016年4月25日 下午6:25:33
	 */
	public static void testHistoryTrendPng(String jobName, String number, String type, ServletOutputStream outputStream) throws IOException {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		GetRequest get = null;
		if("count".equals(type)){
			get = new GetRequest(path("job",jobName,number,"testReport/history/countGraph/png") + "?start=0&end=30");
		}else{
			get = new GetRequest(path("job",jobName,number,"testReport/history/durationGraph/png") + "?start=0&end=30");
		}
		try{
			writeRequestToOutput(outputStream, get);
		}catch(HttpException e){
			
		}
	}
	public static String testHistoryTrendMap(String jobName, String number) {
		checkArgument(!isNullOrEmpty(jobName), "Name is required.");
		GetRequest get = new GetRequest(path("job",jobName,number,"testReport/history/durationGraph/map") + "?start=0&end=30");
		WsConnector connector = getConnector();
		WsResponse wsresponse = connector.call(get);
		String con = wsresponse.content();
		if(wsresponse.isSuccessful()){
			return con;
		}
		return "";
	}
	
	/**
	* @Title: updateJenkinsConfigScmUrl 
	* @author weiyujia
	* @Description: 更新
	* @param jobConfig
	* @param scmUrl
	* @return  设定文件 
	* @return String    返回类型 
	* @throws HttpException 
	* @throws TransformerException 
	* @throws TransformerFactoryConfigurationError 
	* @throws IOException 
	* @throws SAXException 
	* @throws ParserConfigurationException 
	* @throws 
	* @date 2016年5月12日 上午10:05:10
	 */
	public static void updateJenkinsConfigScmUrl(String jobName, String scmUrl) throws HttpException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		String jobConfig = JenkinsRestUtil.getConfig(jobName);
		if(StringUtil.isNotNull(jobConfig)){
			jobConfig = ParseXmlUtil.updateSourceConfig(jobConfig,"svn",scmUrl);
			JenkinsRestUtil.updateConfig(jobName, jobConfig);
		}
	}
	
	/**
	* @Title: updateCustomizedElement 
	* @author weiyujia
	* @Description: 更新
	* @param request  设定文件 
	* @return void    返回类型 
	 * @throws HttpException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	* @throws 
	* @date 2016年5月26日 上午11:36:00
	 */
	public static void updateCustomizedElement(HttpServletRequest request) throws HttpException, ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		String jobName = request.getParameter("jobName");
		String sourceFromType = request.getParameter("sourceFromType");
		String postBuildUrl = request.getParameter("postBuildUrl");
		String jobConfig = JenkinsRestUtil.getConfig(jobName);
		Document document = ParseXmlUtil.getDocumentByConfigxmlStr(jobConfig);
		ParseXmlUtil.updateXmlElement("sourceFromType",sourceFromType,document);
		ParseXmlUtil.updateXmlElement("postBuildUrl",postBuildUrl,document);
		JenkinsRestUtil.updateConfig(jobName,ParseXmlUtil.fromDocumentToStr(document));
	}
	
	/**
	* @Title: pageResult 
	* @author weiyujia
	* @Description: 组装数据，将PAASOS的镜像构建状态组装到page
	* @param page 构建历史数据
	* @param res  镜像构建状态JSON
	* @throws 
	* @date 2016年5月26日 下午5:44:21
	 */
	public static void pageResult(Page<Map<String, Object>> page, String res) {
		JSONObject resultObject = JSON.parseObject(res).getJSONObject("data");
		if(resultObject != null){
			JSONArray listArr = resultObject.getJSONArray("list");
			Iterator<Object> it = listArr.iterator();
			JSONObject itemObject = null;
			while(it.hasNext()){
				itemObject = (JSONObject) it.next();
				setJsonDataToPage(page,itemObject);
			}
		}
	}
	
	/**
	* @Title: setJsonDataToPage 
	* @author weiyujia
	* @Description: 将JSON属性赋值给PAGE
	* @param page
	* @param itemObject  设定文件 
	* @throws 
	* @date 2016年5月27日 上午10:54:32
	 */
	private static void setJsonDataToPage(Page<Map<String, Object>> page, JSONObject itemObject) {
//		String buildId = itemObject.getString("buildId");
		Integer buildNo = Integer.parseInt(itemObject.getString("buildNo") == null ? "-1" : itemObject.getString("buildNo"));
		String status = itemObject.getString("status");
		String buildName = itemObject.getString("buildName");
		String buildVersion = itemObject.getString("buildVersion");
		String buildSuccess = itemObject.getString("buildSuccess");
		for(Map<String,Object> map : page){
			if(map.get("number").equals(buildNo)){
				map.put("buildStatus", status);
				map.put("buildName", buildName);
				map.put("buildVersion", buildVersion);
				map.put("buildSuccess", buildSuccess);
			}
		}
	}
}
