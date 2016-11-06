package com.harmazing.openbridge.project.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.FileUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.ZipUtil;
import com.harmazing.openbridge.project.dao.ApiGuideMapper;
import com.harmazing.openbridge.project.service.IApiGuideService;
import com.harmazing.openbridge.project.vo.ApiVersionProtocol;

@Service
public class ApiGuideServiceImpl implements IApiGuideService{
	
	@Autowired
	private ApiGuideMapper apiGuideMapper;
	
	/**
	 * 根据APP版本ID获取依赖API服务的信息
	 */
	public List<ApiVersionProtocol> getDepenciesApiInfoList(String versionId,String type) {
		return apiGuideMapper.getDepenciesApiInfoList(versionId,type);
	}

	public String initApiDevelopSdk(List<ApiVersionProtocol> apiInfoList) {
		if(apiInfoList != null && apiInfoList.size() > 0){
			try{
				String basePath =   ConfigUtil.getConfigString("file.storage") +"/temp/"+StringUtil.getUUID();
				String createCodePath = basePath + "/clientsdk";
				//生成相关代码,java xml txt等
				this.generateServiceSourceCode(apiInfoList,createCodePath);
				//将依赖的java工具类解压到生成代码的文件夹，用于一起打包下载
				String versionApiFolderZip = basePath + "/api.zip";
				//将token.zip包解压到生成代码的文件夹
				String apidemoZip = downZipFromUrlStrem(basePath);
				ZipUtil.unZip(apidemoZip, createCodePath);
				//解压完成后删除token.zip
				File tokenFile = new File(apidemoZip);
				if(tokenFile.exists()){
					tokenFile.delete();
				}
				File zipFile = new File(versionApiFolderZip);
				if (!zipFile.getParentFile().exists()) {
					zipFile.getParentFile().mkdirs();
				} /*else {
					FileUtils.cleanDirectory(zipFile.getParentFile());
				}*/
				ZipUtil.doZip(createCodePath, zipFile.getAbsolutePath());
				return zipFile.getAbsolutePath();
			}catch(Exception e){
				throw new RuntimeException("生成开发指引出错");
			}
		}else{
			throw new RuntimeException("无服务版本依赖或者API版本接口为空");
		}
	}
	
	/**
	* @Title: unZip 
	* @author weiyujia
	* @Description: 解压指定的zip输入流
	* @param openStream 
	* 				：需要解压的zip输入流
	* @param path
	* 				：需要解压的路径
	* @date 2016年7月26日 上午11:42:42
	 */
	private String downZipFromUrlStrem(String path){
		//获取token.zip包
		URL url = ApiGuideServiceImpl.class.getClassLoader().getResource("apidemo.zip");
		InputStream openStream = null;
		OutputStream os = null;
		String zipPath = path + "/apidemo.zip";
		File tokenFile = new File(zipPath);
		try {
			openStream = url.openStream();
			os = new FileOutputStream(tokenFile);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = openStream.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				openStream.close();
				os.close();
			} catch (Exception e2) { 
			}
		}
		return zipPath;
	}
//
//	private String getLocalBasePath() {
//		return ConfigUtil.getConfigString("file.storage") + "/projects";
//	}
	
	private void generateServiceSourceCode(List<ApiVersionProtocol> apiInfoList, String tempFloder) {
		try{ 
			File folder = new File(tempFloder);
			if (!folder.exists()) {
				folder.mkdirs();
			} else {
				FileUtils.cleanDirectory(folder);
			}
			//每有一个依赖API，每个API的每个接口都对生成一个demo
			Map<String, String> apiSourceCode = this.getJavaDemoSource(apiInfoList);
			//pom.xml代码
//			String pomStr = this.createPomSourceCode(app);
//			FileUtil.write(new File(sdkPath + "/pom.xml"), pomStr, "UTF-8");
			//生成spring.xml样例代码
			String springStr = this.createSpringSourceCode(apiInfoList);
			if (springStr.length() > 0) {
				FileUtil.write(new File(tempFloder + "/spring.xml"), springStr, "UTF-8");
			}
			//生成readme.txt代码
			String readmeStr = this.createReadmeStr();
			FileUtil.write(new File(tempFloder + "/readme.txt"), readmeStr, "UTF-8");
			for (Map.Entry<String, String> apiSource : apiSourceCode.entrySet()) {
				// 生成接口实现的java代码
				String path = getProjectPackage(apiSource.getKey(),apiInfoList).replaceAll("\\.", "/");
				File pack = new File(tempFloder + "/" + path + "/service");
				if (!pack.exists()) {
					pack.mkdirs();
				}
				FileUtil.write(new File(tempFloder + "/" + path + "/service/" + getJavaClassName(apiSource.getKey()) + ".java"), apiSource.getValue(), "UTF-8");
			}
//			return folder.getAbsolutePath();
		}catch (Exception e){
			if (e.getMessage().indexOf("File already exists") >= 0) {
				throw new RuntimeException("初始化SDK时出错,该项目再SVN上已经初始化过一次了", e);
			}
			throw new RuntimeException("初始化SDK时出错" + e.getMessage(), e);
		}
	}

	/**
	* @Title: createReadmeStr 
	* @author weiyujia
	* @Description: 生成readme文档
	* @date 2016年7月20日 下午3:42:43
	 */
	private String createReadmeStr() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(" xxx.java文件是接口服务的调用DEMO。												   ");
		stringBuilder.append(" pom.xml中描述了当前服务对接口定义和其他服务的依赖，请将其拷贝到项目的pom.xml中               ");
		stringBuilder.append(" spring.xml中描述了当前服务基于spring bean的dubbo bean，请将其拷贝到项目的spring配置文件中    ");
		return stringBuilder.toString();
	}

	/**
	* @Title: createSpringSourceCode 
	* @author weiyujia
	* @Description: 生成spring.xml代码
	* @param apiInfoList
	* @date 2016年7月20日 下午3:06:53
	 */
	private String createSpringSourceCode(List<ApiVersionProtocol> apiInfoList) {
		StringBuilder stringBuilder = new StringBuilder();
		for(ApiVersionProtocol versionProtocol : apiInfoList){
			if("dubbo".equals(versionProtocol.getProtocolType())){
				stringBuilder.append("   <!-- 请将如下代码拷贝到 provider 端的 spring.xml文件中 --> \r\n");
				stringBuilder.append("  <dubbo:service interface=\"" + versionProtocol.getProtocolName() + "\" \r\n");
				stringBuilder.append("  	protocol=\"" + versionProtocol.getProtocolType() + "\" ref=\"" + getInterfaceName(versionProtocol.getProtocolName()) + "Impl\" version=\"1.0\" /> \r\n");
				stringBuilder.append("                                                            \r\n");
				stringBuilder.append("  <!-- 请将如下代码拷贝到 consumer 端的 spring.xml文件中 -->  \r\n");
				stringBuilder.append("  <dubbo:reference interface=\"" + versionProtocol.getProtocolName() + "\" \r\n");
				stringBuilder.append("  	protocol=\"" + versionProtocol.getProtocolType() + "\" id=\"" + getInterfaceName(versionProtocol.getProtocolName()) + "Impl\" version=\"1.0\" />  \r\n");
			}
		}
		return stringBuilder.toString();
	}

	/**
	* @Title: getInterfaceName 
	* @author weiyujia
	* @Description: 截取接口名称
	* @param protocolName
	* @date 2016年7月20日 下午3:24:53
	 */
	private String getInterfaceName(String protocolName) {
		String newProtocolName = "";
		int lastDotIndex = protocolName.lastIndexOf(".");
		if (lastDotIndex > 0) {
			newProtocolName = protocolName.substring(lastDotIndex + 1);
		}
		return newProtocolName.substring(0, 1).toLowerCase() + newProtocolName.substring(1);
	}

	/**
	* @Title: getJavaClassName 
	* @author weiyujia
	* @Description: 截取类名称
	* @param protocolName
	* @date 2016年7月20日 下午3:24:53
	 */
	private String getJavaClassName(String protocolName) {
		String newProtocolName = "";
		int lastDotIndex = protocolName.lastIndexOf(".");
		if (lastDotIndex > 0) {
			newProtocolName = protocolName.substring(lastDotIndex + 1);
		}
		return newProtocolName;
	}
	
	/**
	* @Title: createPomSourceCode 
	* @author weiyujia
	* @Description: 生成POM文件
	* @param app  APP版本信息
	 * @return 
	* @date 2016年7月20日 下午2:35:34
	 */
//	private String createPomSourceCode(AppModel app) {
//		AppSource appSource = app.getAppSource();
//		StringBuilder stringBuilder = new StringBuilder();
//		stringBuilder.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\r\n");
//		stringBuilder.append("  	xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\"> \r\n");
//		stringBuilder.append("  	<modelVersion>4.0.0</modelVersion>                                                                    \r\n");
//		stringBuilder.append("                                                                                                            \r\n");
//		stringBuilder.append("  	<groupId>" + appSource.getProjectPackage() + "</groupId>                                              \r\n");
//		stringBuilder.append("  	<artifactId>" + appSource.getProjectName() + "</artifactId>                                           \r\n");
//		stringBuilder.append("  	<version>0.0.1-SNAPSHOT</version>                                                                     \r\n");
//		stringBuilder.append("  	<packaging>jar</packaging>                                                                            \r\n");
//		stringBuilder.append("                                                                                                            \r\n");
//		stringBuilder.append("  	<name>" + appSource.getProjectName() + "</name>                                                       \r\n");
//		stringBuilder.append("  	<url>http://maven.apache.org</url>                                                                    \r\n");
//		stringBuilder.append("                                                                                                            \r\n");
//		stringBuilder.append("  	<properties>                                                                                          \r\n");
//		stringBuilder.append("  		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>                                \r\n");
//		stringBuilder.append("  	</properties>                                                                                         \r\n");
//		stringBuilder.append("                                                                                                            \r\n");
//		stringBuilder.append("  	<dependencies>                                                                                        \r\n");
//		stringBuilder.append("  		<dependency>                                                                                      \r\n");
//		stringBuilder.append("  			<groupId>org.apache.httpcomponents</groupId>                                                  \r\n");
//		stringBuilder.append("  			<artifactId>httpclient</artifactId>                                                           \r\n");
//		stringBuilder.append("  			<version>4.5.2</version>                                                                      \r\n");
//		stringBuilder.append("  		</dependency>                                                                                     \r\n");
//		stringBuilder.append("  		<dependency>                                                                                      \r\n");
//		stringBuilder.append("  			<groupId>org.apache.httpcomponents</groupId>                                                  \r\n");
//		stringBuilder.append("  			<artifactId>httpmime</artifactId>                                                             \r\n");
//		stringBuilder.append("  			<version>4.5.2</version>                                                                      \r\n");
//		stringBuilder.append("  		</dependency>                                                                                     \r\n");
//		stringBuilder.append("  	</dependencies>                                                                                       \r\n");
//		stringBuilder.append("  </project>                                                                                                \r\n");
//		return stringBuilder.toString();
//	}

	/**
	* @Title: getProjectPackage 
	* @author weiyujia
	* @Description: 获取包路径
	* @date 2016年7月20日 下午2:06:38
	*/
	private String getProjectPackage(String key, List<ApiVersionProtocol> apiInfoList) {
		String projectPackage = "";
		for(ApiVersionProtocol protocol : apiInfoList){
			if(protocol.getProtocolName().equals(key.replace("Client", ""))){
				projectPackage = protocol.getProjectPackage();
				break;
			}
		}
		return projectPackage;
	}
	
	/**
	* @Title: getJavaDemoSource 
	* @author weiyujia
	* @Description: 生成DEMO代码
	* @param protocol API接口信息
	* @param model APP版本信息
	 * @param appModel 
	* @return Map<String,String>    返回类型 
	* @date 2016年7月19日 上午11:42:50
	 */
	private Map<String, String> getJavaDemoSource(List<ApiVersionProtocol> apiInfoList) {
		Map<String,String> serviceCodes = new HashMap<String,String>();
		for(ApiVersionProtocol protocol : apiInfoList){
			if(protocol.getProtocolType().equals("rest")){
				serviceCodes.put(protocol.getProtocolName() + "Client", this.createJavaCodeByProtocol(protocol));
			}
		}
		return serviceCodes;
	}

	/**
	* @Title: createJavaCodeByProtocol 
	* @author weiyujia
	* @Description: 根据API接口信息生成JAVA代码
	* @param apiInfoList API接口信息
	* @param model App版本信息
	* @param appModel 
	* @return String    返回JAVA code
	* @date 2016年7月19日 上午11:48:17
	 */
	private String createJavaCodeByProtocol(ApiVersionProtocol protocol) {
		
		StringBuilder builder = new StringBuilder();
		//获取API接口JSON
		String protocolApi = protocol.getProtocolApi();
		if (StringUtil.isNull(protocolApi)) {
			protocolApi = "{}";
		}
		//API服务是否公开
		boolean servicePublicFlag = protocol.getServicePublic().equals("0") ? true : false;
		JSONObject apiProtocolJson = JSONObject.parseObject(protocolApi);
		JSONArray methods = apiProtocolJson.containsKey("methods") ? apiProtocolJson .getJSONArray("methods") : new JSONArray();
		//rest类型的服务才需要DEMO
		if(protocol.getProtocolType().equals("rest")){
			builder.append("package "+protocol.getProjectPackage()+".service;\r\n\r\n");
			builder.append("import java.util.HashMap;\r\n");
			builder.append("import java.util.Map;\r\n");
			builder.append("import com.harmazing.openbridge.RestUtil;\r\n");
			builder.append("import com.harmazing.openbridge.RestUtil.DataType;\r\n");
			builder.append("import java.io.UnsupportedEncodingException;\r\n");
			if(!servicePublicFlag){
				builder.append("import com.harmazing.openbridge.AuthToken;\r\n");
				builder.append("import com.harmazing.openbridge.AuthTokenUtil;\r\n");
//				builder.append("import com.harmazing.openbridge.HttpUtil;\r\n");
			}
			builder.append("public class " + getJavaClassName(protocol.getProtocolName()) + "Client {\r\n");
			builder.append("\tprivate String baseUrl = \"http://localhost:8080\";\r\n");
			if(!servicePublicFlag){
				builder.append("\t/**                                                                                                                             \r\n");    
				builder.append("\t * 鉴权服务基础地址(外部导入服务，可以将此配置放在配置文件中读取；开发型服务此配置可以初始化在配置文件中，或直接初始化在此文件)         \r\n");
				builder.append("\t */                                                                                                                             \r\n");
				builder.append("\tprivate static final String authUrl = \"http://openbridge.f3322.net:88/api/auth/getToken.do\";                                  \r\n");
				builder.append("\t                                                                                                                                \r\n");
				builder.append("\t/**                                                                                                                             \r\n");
				builder.append("\t * 调用 mod_app的app_id,                                                                                                         \r\n");
				builder.append("\t */                                                                                                                             \r\n");
				builder.append("\tprivate static final String appId = \"70milmlmlj6cg2z2cwpffcetv8netr7\";                                                        \r\n");
				builder.append("\t                                                                                                                                \r\n");
				builder.append("\t/**                                                                                                                             \r\n");
				builder.append("\t * 调用mod_app的secure_key,                                                                                                      \r\n");
				builder.append("\t */                                                                                                                             \r\n");
				builder.append("\tprivate static final String appSecure = \"70neil8b2yvjx9wuuagvetqdcgn7zjp\";                                                    \r\n");
				builder.append("\tprivate Map<String,String> getAuthHeaders(){																					  \r\n");
				builder.append("\t\t// 获取令牌                                                                                                                                                                                                                                                                                                                    \r\n");
				builder.append("\t\tAuthToken authToken = AuthTokenUtil.getToken(authUrl,appId,appSecure);                                                        \r\n");
				builder.append("\t\t// 准备HTTP请求Header                                                                                                          \r\n");
				builder.append("\t\tMap<String, String> headers = new HashMap<String, String>();                                                                  \r\n");
				builder.append("\t\theaders.put(\"auth-token\", authToken.getAuthToken());                                                                        \r\n");
				builder.append("\t\theaders.put(\"auth-appid\", authToken.getAuthAppId());                                                                        \r\n");
				builder.append("\t\treturn headers;                                                                                                               \r\n");
				builder.append("\t}                                                                                                                               \r\n");
			}
			List<String> methodList = new ArrayList<String>();
			for (int i = 0; i < methods.size(); i++) {
				JSONObject method = methods.getJSONObject(i);
				//方法名称，需要首字母大写
				String methodName = method.getString("name").substring(0, 1).toUpperCase() + method.getString("name").substring(1);
				methodList.add(methodName);
				if(!StringUtil.isEmpty(method.getString("desc")) || !StringUtil.isEmpty(method.getString("businessDesc"))){
					builder.append("\t/**\r\n");
					String[] descArr = method.getString("desc").split("\n");
					if(descArr.length > 0){
						builder.append("\t*描述信息如下:\r\n");
						for(String desc : descArr){
							builder.append("\t*" + desc + "\r\n");
						}
						builder.append("\t* \r\n");
					}
					String[] businessDescArr = method.getString("businessDesc").split("\n");
					if(descArr.length > 0){
						builder.append("\t*业务描述如下:\r\n");
						for(String desc : businessDescArr){
							builder.append("\t*" + desc + "\r\n");
						}
					}
					builder.append("\t*/\r\n");
				}
				builder.append("\tpublic void get" + methodName + "() throws UnsupportedEncodingException {\r\n");
				String type = method.getString("type");//方法请求类型,post/put/delete/get
				String consumes = method.getString("consumes");//入参类型 json/form/xml
				String url = "baseUrl + \"/" + apiProtocolJson.getString("path") + "/"  + method.getString("path") + "";//请求url
				String dataType = "DataType." + consumes.toUpperCase();//DataType
				boolean existHeaderFlag = checkHasParamType(method,"header");
				boolean existBodyFlag = checkHasParamType(method,"requestbody");
				if(!servicePublicFlag){
					builder.append("\t\t//如果服务定义有header类型的参数，请将该参数put到headerParams\r\n");
					builder.append("\t\tMap<String,String> headerParams = getAuthHeaders();\r\n");
				}else if(existHeaderFlag){
					builder.append("\t\t//如果服务定义有header类型的参数，请将该参数put到headerParams\r\n");
					builder.append("\t\tMap<String,String> headerParams = new HashMap<String,String>();\r\n");
				}
				if("post".equals(type) || "put".equals(type)){
					if("json".equals(consumes)){
						builder.append("\t\tString params = \"{}\";//json 数据入参;\r\n");
						if(existBodyFlag){
							builder.append("\t\t//如果服务定义有requestbody类型的参数，请将该参数put到params\r\n");
							builder.append("\t\tMap<String,String> params = new HashMap<String,String>();\r\n");
						}
					}else if("form".equals(consumes)){
						builder.append("\t\t//如果服务定义有requestbody类型的参数，请将该参数put到params\r\n");
						builder.append("\t\tMap<String,String> params = new HashMap<String,String>();\r\n");
					}else if("xml".equals(consumes)){
						builder.append("\t\tString params = \"<test></test>\";//xml 数据入参;\r\n");
						if(existBodyFlag){
							builder.append("\t\t//如果服务定义有requestbody类型的参数，请将该参数put到params\r\n");
							builder.append("\t\tMap<String,String> params = new HashMap<String,String>();\r\n");
						}
					}
					
				}
				if(method.containsKey("params")){
					JSONArray params = method.getJSONArray("params");
					for (int j = 0; j < params.size(); j++) {
						JSONObject param = params.getJSONObject(j);
						String paramType = param.getString("source");
						if("path".equals(paramType)){
							url = url.replace("{name}", param.getString("name"));
						}else if("form".equals(paramType) || "requestbody".equals(paramType)){
							builder.append("\t\tparams.put(\"" + param.getString("name") + "\", \"test_" + param.getString("name") + "\");\r\n");
						}else if("query".equals(paramType)){
							if(!url.contains("?")){
								url = url + "?" + param.getString("name") + "=test_query_" + param.getString("name");
							}else{
								url = url + "&" + param.getString("name") + "=test_query_" + param.getString("name");
							}
//											builder.append("\t\tparams.put(\"" + param.getString("name") + "\", \"test_query_" + param.getString("name") + "\");\r\n ");
						}else if("header".equals(paramType)){
							builder.append("\t\theaderParams.put(\"" + param.getString("name") + "\", \"test_" + param.getString("name") + "\");\r\n");
						}
					}
				}
				url = url + "\"";
				if("post".equals(type) || "put".equals(type)){
					builder.append("\t\tString res = RestUtil." + method.getString("type") + "("+url+"");
					if(!servicePublicFlag || existHeaderFlag){
						builder.append(",headerParams,"+dataType+", params);\r\n");
					}else{
						builder.append(","+dataType+",  params);\r\n");
					}
				}else{
					builder.append("\t\tString res = RestUtil." + method.getString("type") + "("+url+"");
					if(!servicePublicFlag || existHeaderFlag){
						builder.append(",headerParams);\r\n");
					}else{
						builder.append(");\r\n");
					}
				}
				builder.append("\t\tSystem.out.println(res);\r\n");
				builder.append("\t}\r\n");
			}
			builder.append("\tpublic static void main(String[] args) throws UnsupportedEncodingException {\r\n");
			builder.append("\t\t" + getJavaClassName(protocol.getProtocolName()) + "Client a = new " + getJavaClassName(protocol.getProtocolName()) + "Client();\r\n");
			for(String itemName : methodList){
				builder.append("\t\ta.get" + itemName + "();\r\n");
			}
			builder.append("\t}\r\n");
//			builder.append("    private String toUrlParams(Map<String,String> paramMap) throws UnsupportedEncodingException {\r\n");
//			builder.append("    	String resultStr = \"\";                                                                 \r\n");
//			builder.append("    	Set<String> paramSet = paramMap.keySet();                                                \r\n");
//			builder.append("    	if(paramSet.size() > 0){                                                                 \r\n");
//			builder.append("    		resultStr = \"?\";                                                                   \r\n");
//			builder.append("    		for(String para : paramSet){                                                         \r\n");
//			builder.append("    			resultStr = resultStr + para + \"=\" + URLEncoder.encode(paramMap.get(para),\"UTF-8\") + \"&\";               \r\n");
//			builder.append("    		}                                                                                    \r\n");
//			builder.append("    	}                                                                                        \r\n");
//			builder.append("    	return resultStr.length() > 0 ? resultStr.substring(0, resultStr.length() - 1) : \"\";   \r\n");
//			builder.append("    };																							 \r\n");
			builder.append("}");
		}
		return builder.toString();
	}

	/**
	* @Title: checkHasHeaderParam 
	* @author weiyujia
	* @Description: TODO
	* @param jsonObject
	* @return  设定文件 
	* @return boolean    返回类型 
	* @throws 
	* @date 2016年7月25日 下午2:07:35
	 */
	private boolean checkHasParamType(JSONObject method,String _paramType) {
		boolean flag = false;
		if(method.containsKey("params")){
			JSONArray params = method.getJSONArray("params");
			for (int j = 0; j < params.size(); j++) {
				JSONObject param = params.getJSONObject(j);
				String paramType = param.getString("source");
				if(_paramType.equals(paramType)){
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

}
