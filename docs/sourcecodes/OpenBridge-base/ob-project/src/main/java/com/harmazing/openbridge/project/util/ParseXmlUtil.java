package com.harmazing.openbridge.project.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.http.client.HttpException;
import com.harmazing.openbridge.project.vo.JenkinsConfigVO;


/**
* @ClassName: ParseXmlUtil 
* @Description: 解析XML工具类
* @author A18ccms a18ccms_gmail_com 
* @date 2016年4月8日 下午5:12:25 
*
 */
public class ParseXmlUtil {
	
	public static void removeChild(String key ,Element topElement){
		if(topElement.getElementsByTagName(key).getLength() != 0){
			topElement.removeChild(topElement.getElementsByTagName(key).item(0));
		}
	}
	
	public static String updateSourceConfig(String jenkinsXml, String type,
			String url) throws ParserConfigurationException, SAXException,
			IOException, TransformerFactoryConfigurationError,
			TransformerException {
		Document document = ParseXmlUtil.getDocumentByConfigxmlStr(jenkinsXml);
		Element modElement = (Element) document.getElementsByTagName("maven2-moduleset").item(0);
		Element scmElement = (Element) document.getElementsByTagName(Constant.Jenkins.JENKINS_SVN_SCM).item(0);
		if (scmElement != null) {
			String scmClass = scmElement.getAttribute(Constant.Jenkins.CLASS);
			if (Constant.Jenkins.JENKINS_SVN_SCM_HUDSON.equals(scmClass) || "hudson.scm.NullSCM".equals(scmClass)) {
				scmElement.removeAttribute("plugin");
				ParseXmlUtil.removeChild("locations", scmElement);
				ParseXmlUtil.removeChild("excludedRegions", scmElement);
				ParseXmlUtil.removeChild("includedRegions", scmElement);
				ParseXmlUtil.removeChild("excludedUsers", scmElement);
				ParseXmlUtil.removeChild("excludedRevprop", scmElement);
				ParseXmlUtil.removeChild("excludedCommitMessages", scmElement);
				ParseXmlUtil.removeChild("workspaceUpdater", scmElement);
				ParseXmlUtil.removeChild("ignoreDirPropChanges", scmElement);
				ParseXmlUtil.removeChild("filterChangelog", scmElement);
			} else if (Constant.Jenkins.JENKINS_SVN_SCM_HUDSON.equals(scmClass) || "hudson.scm.NullSCM".equals(scmClass)) {
				ParseXmlUtil.removeChild("configVersion", scmElement);
				ParseXmlUtil.removeChild("userRemoteConfigs", scmElement);
				ParseXmlUtil.removeChild("branches", scmElement);
				ParseXmlUtil.removeChild("doGenerateSubmoduleConfigurations", scmElement);
				ParseXmlUtil.removeChild("submoduleCfg", scmElement);
				ParseXmlUtil.removeChild("extensions", scmElement);
			}
		}else{
			scmElement = document.createElement("scm");
			modElement.appendChild(scmElement);
		}
		if (type.toLowerCase().equals(Constant.Jenkins.JENKINS_SVN)) {
			scmElement.setAttribute("class", "hudson.scm.SubversionSCM");
			// locations 节点
			Element locations = document.createElement("locations");
			Element moduleLocation = document.createElement(Constant.Jenkins.JENKINS_SVN_SCM_HUDSON_MODULELOCATION);
			Element remoteElement = document.createElement("remote");
			remoteElement.setTextContent(url);
			moduleLocation.appendChild(remoteElement);
			Element localElement = document.createElement("local");
			localElement.setTextContent(".");
			moduleLocation.appendChild(localElement);
			Element depthOption = document.createElement("depthOption");
			depthOption.setTextContent("infinity");
			moduleLocation.appendChild(depthOption);
			Element ignoreExternalsOption = document.createElement("ignoreExternalsOption");
			ignoreExternalsOption.setTextContent("false");
			moduleLocation.appendChild(ignoreExternalsOption);
			locations.appendChild(moduleLocation);
			scmElement.appendChild(locations);
			// workspaceUpdater 节点
			Element workspaceUpdater = document.createElement("workspaceUpdater");
			workspaceUpdater.setAttribute(Constant.Jenkins.CLASS, "hudson.scm.subversion.UpdateUpdater");
			scmElement.appendChild(workspaceUpdater);
			// ignoreDirPropChanges 节点
			Element ignoreDirPropChanges = document.createElement("ignoreDirPropChanges");
			ignoreDirPropChanges.setTextContent("false");
			scmElement.appendChild(ignoreDirPropChanges);
			return ParseXmlUtil.fromDocumentToStr(document);
		}
		if (type.toLowerCase().equals(Constant.Jenkins.JENKINS_GIT)) {
			scmElement.setAttribute("class", "hudson.plugins.git.GitSCM");
			Element userRemoteConfigs = document.createElement("userRemoteConfigs");
			Element hudsonElement = document.createElement(Constant.Jenkins.JENKINS_GIT_SCM_HUDSON_USERREMOTECONFIG);
			Element urlElement = document.createElement(Constant.Jenkins.JENKINS_URL);
			urlElement.setTextContent(url);
			hudsonElement.appendChild(urlElement);
			userRemoteConfigs.appendChild(hudsonElement);
			scmElement.appendChild(userRemoteConfigs);
			Element doGenerateSubmoduleConfigurations = document.createElement("doGenerateSubmoduleConfigurations");
			doGenerateSubmoduleConfigurations.setTextContent("false");
			scmElement.appendChild(doGenerateSubmoduleConfigurations);
			Element submoduleCfg = document.createElement("submoduleCfg");
			submoduleCfg.setAttribute(Constant.Jenkins.CLASS, "list");
			scmElement.appendChild(submoduleCfg);
			Element branches = document.createElement("branches");
			Element branchSpec = document.createElement("hudson.plugins.git.BranchSpec");
			Element name = document.createElement("name");
			name.setTextContent("*/master");
			branchSpec.appendChild(name);
			branches.appendChild(branchSpec);
			scmElement.appendChild(branches);
			return ParseXmlUtil.fromDocumentToStr(document);
		}
		return null;
	}

//	public static String updateSourceComfig(String jenkinsXml,String type ,String url ,String credentialsId) throws TransformerFactoryConfigurationError, TransformerException{
//		try {
//			Document document = ParseXmlUtil.getDocumentByConfigxmlStr(jenkinsXml);
//			Element scmElement = (Element) document.getElementsByTagName(Constant.Jenkins.JENKINS_SVN_SCM).item(0);
//			String scmClass = scmElement.getAttribute(Constant.Jenkins.CLASS);
//			Element modElement = (Element) document.getElementsByTagName("maven2-moduleset").item(0);
//			if(Constant.Jenkins.JENKINS_SVN_SCM_HUDSON.equals(scmClass) || scmClass.equals("hudson.scm.NullSCM")){//已是SVN作为管理工具
//				if(type.toLowerCase().equals(Constant.Jenkins.JENKINS_SVN)){
//					Element moduleLocation = (Element) document.getElementsByTagName(Constant.Jenkins.JENKINS_SVN_SCM_HUDSON_MODULELOCATION).item(0);
//					if(moduleLocation == null){
//						moduleLocation = document.createElement(Constant.Jenkins.JENKINS_SVN_SCM_HUDSON_MODULELOCATION);
////						Element locationElement = (Element) document.getElementsByTagName("locations").item(0);
//						Element remoteElement = document.createElement("remote");
//						remoteElement.setTextContent(url);
//						moduleLocation.appendChild(remoteElement);
//						Element depthOption = document.createElement("depthOption");
//						depthOption.setTextContent("infinity");
//						moduleLocation.appendChild(depthOption);
//						Element ignoreExternalsOption = document.createElement("ignoreExternalsOption");
//						ignoreExternalsOption.setTextContent("false");
//						moduleLocation.appendChild(ignoreExternalsOption);
//						modElement.appendChild(moduleLocation);
//					}else{
//						Element remoteElement = (Element) document.getElementsByTagName("remote").item(0);
//						remoteElement.setTextContent(url);
//					}
//					return ParseXmlUtil.fromDocumentToStr(document);
//				}else if(type.toLowerCase().equals(Constant.Jenkins.JENKINS_GIT)  ){
//					scmElement.setAttribute(Constant.Jenkins.CLASS, Constant.Jenkins.JENKINS_GIT_SCM_HUDSON);
//					scmElement.removeAttribute("plugin");
//					scmElement.removeChild(scmElement.getElementsByTagName("locations").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("excludedRegions").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("includedRegions").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("excludedUsers").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("excludedRevprop").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("excludedCommitMessages").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("workspaceUpdater").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("ignoreDirPropChanges").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("filterChangelog").item(0));	
//					
//					Element userRemoteConfigs = document.createElement("userRemoteConfigs");
//					Element hudsonElement = document.createElement(Constant.Jenkins.JENKINS_GIT_SCM_HUDSON_USERREMOTECONFIG);
//					Element urlElement = document.createElement(Constant.Jenkins.JENKINS_URL);
//					urlElement.setTextContent(url);
//					hudsonElement.appendChild(urlElement);
//					Element credentialsElement = document.createElement("credentialsId");
//					credentialsElement.setTextContent(credentialsId);
//					hudsonElement.appendChild(credentialsElement);
//					userRemoteConfigs.appendChild(hudsonElement);
//					scmElement.appendChild(userRemoteConfigs);
//					Element doGenerateSubmoduleConfigurations = document.createElement("doGenerateSubmoduleConfigurations");
//					doGenerateSubmoduleConfigurations.setTextContent("false");
//					scmElement.appendChild(doGenerateSubmoduleConfigurations);
//					Element submoduleCfg = document.createElement("submoduleCfg");
//					submoduleCfg.setAttribute(Constant.Jenkins.CLASS, "list");
//					scmElement.appendChild(submoduleCfg);
//					
//					Element branches = document.createElement("branches");
//					Element branchSpec = document.createElement("hudson.plugins.git.BranchSpec");
//					Element name = document.createElement("name");
//					name.setTextContent("*/master");
//					branchSpec.appendChild(name);
//					branches.appendChild(branchSpec);
//					scmElement.appendChild(branches);
//					return ParseXmlUtil.fromDocumentToStr(document);
//				}
//			}else if(Constant.Jenkins.JENKINS_GIT_SCM_HUDSON.equals(scmClass)){
//				if(type.toLowerCase().equals(Constant.Jenkins.JENKINS_GIT)){//当前已是GIT管理
//					Element svnElement = (Element) document.getElementsByTagName(Constant.Jenkins.JENKINS_GIT_SCM_HUDSON_USERREMOTECONFIG).item(0);
//					Element remote = (Element) svnElement.getElementsByTagName(Constant.Jenkins.JENKINS_URL).item(0);
//					if(remote == null){
//						remote = document.createElement(Constant.Jenkins.JENKINS_URL);
//						remote.setTextContent(url);
//						svnElement.appendChild(remote);
//					}
//					remote.setTextContent(url);
//					Element credentialsElement = (Element) svnElement.getElementsByTagName(Constant.Jenkins.JENKINS_GIT_SCM_HUDSON_CREDENTIALSID).item(0);
//					if(credentialsElement == null){
//						credentialsElement = document.createElement(Constant.Jenkins.JENKINS_GIT_SCM_HUDSON_CREDENTIALSID);
//						credentialsElement.setTextContent(credentialsId);
//						svnElement.appendChild(credentialsElement);
//					}
//					credentialsElement.setTextContent(credentialsId);
//					return ParseXmlUtil.fromDocumentToStr(document);
//				}else if(type.toLowerCase().equals(Constant.Jenkins.JENKINS_SVN)){//当前是GIT管理，但是需要切换管理工具为SVN
//					scmElement.setAttribute(Constant.Jenkins.CLASS, Constant.Jenkins.JENKINS_SVN_SCM_HUDSON);
//					//先移除所有SCM下所有节点，再重新生成SVN对应的节点,除了Remote节点外全设置为默认值切该接口不支持修改
//					scmElement.removeChild(scmElement.getElementsByTagName("configVersion").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("userRemoteConfigs").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("branches").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("doGenerateSubmoduleConfigurations").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("submoduleCfg").item(0));	
//					scmElement.removeChild(scmElement.getElementsByTagName("extensions").item(0));
//					//locations 节点
//					Element locations = document.createElement("locations");
//					Element moduleLocation = document.createElement(Constant.Jenkins.JENKINS_SVN_SCM_HUDSON_MODULELOCATION);
//					Element remoteElement = document.createElement("remote");
//					remoteElement.setTextContent(url);
//					moduleLocation.appendChild(remoteElement);
//					Element localElement = document.createElement("local");
//					moduleLocation.appendChild(localElement);
//					Element depthOption = document.createElement("depthOption");
//					depthOption.setTextContent("infinity");
//					moduleLocation.appendChild(depthOption);
//					Element ignoreExternalsOption = document.createElement("ignoreExternalsOption");
//					ignoreExternalsOption.setTextContent("false");
//					moduleLocation.appendChild(ignoreExternalsOption);
//					locations.appendChild(moduleLocation);
//					scmElement.appendChild(locations);
//					//workspaceUpdater  节点
//					Element workspaceUpdater = document.createElement("workspaceUpdater");
//					workspaceUpdater.setAttribute(Constant.Jenkins.CLASS, "hudson.scm.subversion.UpdateUpdater");
//					scmElement.appendChild(workspaceUpdater);
//					//ignoreDirPropChanges 节点
//					Element ignoreDirPropChanges = document.createElement("ignoreDirPropChanges");
//					ignoreDirPropChanges.setTextContent("false");
//					scmElement.appendChild(ignoreDirPropChanges);
//					return ParseXmlUtil.fromDocumentToStr(document);
//				}
//			}
////			if(nList == null || nList.getLength() == 0){
////				
////			}
//		} catch (ParserConfigurationException e) {
//			e.printStackTrace();
//		} catch (SAXException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	} 

	/**
	* @Title: getDocumentByConfigxmlStr 
	* @author weiyujia
	* @Description: 将XML的String转换为Docment对象
	* @param @param jenkinsXml 
	* @param @return
	* @param @throws ParserConfigurationException
	* @param @throws SAXException
	* @param @throws IOException    设定文件 
	* @return Document    返回类型 
	* @throws
	 */
	public static Document getDocumentByConfigxmlStr(String jenkinsXml) throws ParserConfigurationException, SAXException, IOException{
		StringReader sr = new StringReader(jenkinsXml); 
		InputSource is = new InputSource(sr); 
		DocumentBuilder dbBuilder = ParseXmlUtil.getDocumentBuilderInstance();
		return dbBuilder.parse(is);
	}
	
	/**
	* @Title: fromDocumentToStr 
	* @author weiyujia
	* @Description: 将Document对象转为String
	* @param @param document
	* @param @return
	* @param @throws TransformerFactoryConfigurationError
	* @param @throws TransformerException    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String fromDocumentToStr(Document document) throws TransformerFactoryConfigurationError, TransformerException{
		StringWriter sw = new StringWriter();
		Transformer serializer = TransformerFactory.newInstance().newTransformer();
		serializer.transform(new DOMSource(document), new StreamResult(sw));
		return sw.toString();
	}
	
	/**
	* @Title: getDocumentBuilderInstance 
	* @author weiyujia
	* @Description: 获取Builder对象
	* @param @return
	* @param @throws ParserConfigurationException    设定文件 
	* @return DocumentBuilder    返回类型 
	* @throws
	 */
	public static DocumentBuilder getDocumentBuilderInstance() throws ParserConfigurationException{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder();
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
		try {
			Document document = ParseXmlUtil.getDocumentByConfigxmlStr(jenkinsXml);
			Element modElement = (Element) document.getElementsByTagName("maven2-moduleset").item(0);
			Element rootPOM = (Element) document.getElementsByTagName("rootPOM").item(0);
			if(rootPOM == null){
				rootPOM = document.createElement("rootPOM");
				rootPOM.setTextContent(pom);
				modElement.appendChild(rootPOM);
			}
			rootPOM.setTextContent(pom);
			
			Element mavenGoalElement = (Element) document.getElementsByTagName("goals").item(0);
			if(mavenGoalElement == null){
				mavenGoalElement = document.createElement("goals");
				mavenGoalElement.setTextContent(mavenGoal);
				modElement.appendChild(mavenGoalElement);
			}
			mavenGoalElement.setTextContent(mavenGoal);
			return ParseXmlUtil.fromDocumentToStr(document);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * @Title: updateXmlStr 
	 * @author weiyujia
	 * @Description: 根据VO更新配置文件
	 * @param configVO
	 * @param configXml
	 * @return  设定文件 
	 * @return String    返回类型 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws TransformerException 
	 * @throws TransformerFactoryConfigurationError 
	 * @throws 
	 * @date 2016年4月12日 下午6:02:54  
	 */
	public static String updateXmlStr(JenkinsConfigVO configVO, String configXml) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException {
		if(!StringUtils.isEmpty(configVO.getRemote())){
			configXml=ParseXmlUtil.updateSourceConfig(configXml, "svn", configVO.getRemote());
		}
		Document document = ParseXmlUtil.getDocumentByConfigxmlStr(configXml);
		if(!StringUtils.isEmpty(configVO.getConcurrentBuild())){
			ParseXmlUtil.updateXmlElement("concurrentBuild",configVO.getConcurrentBuild(),document);
		}else{
			ParseXmlUtil.updateXmlElement("concurrentBuild","false",document);
		}
		if(!StringUtils.isEmpty(configVO.getDescription())){
			ParseXmlUtil.updateXmlElement("description",configVO.getDescription(),document);
		}
		if(!StringUtils.isEmpty(configVO.getDisabled())){
			ParseXmlUtil.updateXmlElement("disabled",configVO.getDisabled(),document);
		}else{
			ParseXmlUtil.updateXmlElement("disabled","false",document);
		}
		if(!StringUtils.isEmpty(configVO.getGoals())){
			ParseXmlUtil.updateXmlElement("goals",configVO.getGoals(),document);
		}
		if(!StringUtils.isEmpty(configVO.getRootPOM())){
			ParseXmlUtil.updateXmlElement("rootPOM",configVO.getRootPOM(),document);
		}
		if(!StringUtils.isEmpty(configVO.getRunPostStepsIfResult())){
			ParseXmlUtil.updateRunPostSteps(configVO.getRunPostStepsIfResult(),document);
		}
		if (!StringUtils.isEmpty(configVO.getHudsonTasksBatchFile())) {
			ParseXmlUtil.updatePostbuildersElement("hudson.tasks.BatchFile","command",configVO.getHudsonTasksBatchFile(),document);
		}else{
			ParseXmlUtil.removeXmlChild("hudson.tasks.BatchFile",document);
		}
		if (!StringUtils.isEmpty(configVO.getHudsonTasksShell())) {
			ParseXmlUtil.updatePostbuildersElement("hudson.tasks.Shell","command",configVO.getHudsonTasksShell(),document);
		}else{
			ParseXmlUtil.removeXmlChild("hudson.tasks.Shell",document);
		}
		if (!StringUtils.isEmpty(configVO.getHudsonTasksAnt())) {
			ParseXmlUtil.updatePostbuildersElement("hudson.tasks.Ant","targets",configVO.getHudsonTasksAnt(),document);
		}else{
			ParseXmlUtil.removeXmlChild("hudson.tasks.Ant",document);
		}
		if(!StringUtils.isEmpty(configVO.getFileBelongModel())){
			ParseXmlUtil.updateXmlElement("fileBelongModel",configVO.getFileBelongModel(),document);
		}else{
			ParseXmlUtil.updateXmlElement("fileBelongModel","",document);
		}
		if(!StringUtils.isEmpty(configVO.getDisplayName())){
			ParseXmlUtil.updateXmlElement("displayName",configVO.getDisplayName(),document);
		}else{
			ParseXmlUtil.updateXmlElement("displayName","",document);
		}
		
		
		//此处用于处理EnvPropagatorBuilder节点的值
		String envPropagatorBuilderValue = "";
		if(!StringUtils.isEmpty(configVO.getSourceFromType())){
			envPropagatorBuilderValue = "SOURCE_FROM_TYPE=" + configVO.getSourceFromType();
//			ParseXmlUtil.updateEnvPropagatorBuilder("sourceFromType",configVO.getSourceFromType(),document);
		}
		if(!StringUtils.isEmpty(configVO.getPostBuildUrl())){
			envPropagatorBuilderValue = envPropagatorBuilderValue + ":POST_BUILD_URL=" + configVO.getPostBuildUrl();
//			ParseXmlUtil.updateEnvPropagatorBuilder("postBuildUrl",configVO.getPostBuildUrl(),document);
		}
		if(!StringUtils.isEmpty(envPropagatorBuilderValue)){
			ParseXmlUtil.updateEnvPropagatorBuilder(envPropagatorBuilderValue,document);
		}
		
		if(!StringUtils.isEmpty(configVO.getNumToKeep())){
			ParseXmlUtil.updateStrategyElement(configVO.getNumToKeep(),document);
		}else{
			ParseXmlUtil.updateStrategyElement("60",document);
		}
		return ParseXmlUtil.fromDocumentToStr(document);
	}

	/**
	* @Title: updateEnvPropagatorBuilder 
	* @author weiyujia
	* @Description: 更新jenkins EnvPropagatorBuilder 节点 ，用于标识项目来源
	* @param string
	* @param sourceFromType
	* @param document  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年6月21日 上午10:03:09
	 */
	private static void updateEnvPropagatorBuilder(String value, Document document) {
//		Element element = (Element) document.getElementsByTagName("postbuilders").item(0);
//		if(element == null){
//			element = document.createElement("postbuilders");
//			
//			modElement.appendChild(element);
//		}
		Element modElement = (Element) document.getElementsByTagName("org.jenkinsci.plugins.envpropagator.EnvPropagatorBuilder").item(0);
		if(modElement == null){
			modElement = document.createElement("org.jenkinsci.plugins.envpropagator.EnvPropagatorBuilder");
			Element envVariableString = document.createElement("envVariableString");
			envVariableString.setTextContent(value);
			modElement.appendChild(envVariableString);
		}else{
			Element envVariableString = (Element) document.getElementsByTagName("envVariableString").item(0);
			envVariableString.setTextContent(value);
		}
		Element postElement = (Element) document.getElementsByTagName("postbuilders").item(0);
		postElement.appendChild(modElement);
		
	}

	/**
	* @Title: updateStrategyElement 
	* @author weiyujia
	* @Description: 更新config.xml的Strategy节点(保持最大构建记录)
	* @param string	
	* @param numToKeep
	* @param document  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年5月27日 下午2:44:07
	 */
	public static void updateStrategyElement(String number, Document document) {
		Element properties = (Element) document.getElementsByTagName("properties").item(0);
		if(properties == null || StringUtils.isEmpty(properties.getTextContent())){
//			document.removeChild(properties);
//			properties = document.createElement("properties");
			Element buildDiscarderProperty = document.createElement("jenkins.model.BuildDiscarderProperty");
			Element strategy = document.createElement("strategy");
			strategy.setAttribute("class", "hudson.tasks.LogRotator");
			Element numToKeepItem = document.createElement("numToKeep");
			numToKeepItem.setTextContent(StringUtils.isEmpty(number) ? "15" : number);
			Element daysToKeep = document.createElement("daysToKeep");
			daysToKeep.setTextContent("-1");
			Element artifactDaysToKeep = document.createElement("artifactDaysToKeep");
			artifactDaysToKeep.setTextContent("-1");
			Element artifactNumToKeep = document.createElement("artifactNumToKeep");
			artifactNumToKeep.setTextContent("-1");
			strategy.appendChild(numToKeepItem);
			strategy.appendChild(daysToKeep);
			strategy.appendChild(artifactDaysToKeep);
			strategy.appendChild(artifactNumToKeep);
			buildDiscarderProperty.appendChild(strategy);
			properties.appendChild(buildDiscarderProperty);
		}else{
			if(StringUtils.isEmpty(number)){
				properties.setTextContent(null);
			}else{
				Element numToKeepItem = (Element) document.getElementsByTagName("numToKeep").item(0);
				numToKeepItem.setTextContent(number);
			}
		}
	}

	/**
	* @Title: updateRunPostSteps 
	* @author weiyujia
	* @Description: 更新/设置 post run节点
	* @param runPostStepsIfResult
	* @param document  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年4月18日 下午2:55:57
	 */
	public static void updateRunPostSteps(String runPostStepsIfResult, Document document) {
		Element name=(Element) document.getElementsByTagName("name").item(0);
		Element ordinal=(Element) document.getElementsByTagName("ordinal").item(0);
		Element color=(Element) document.getElementsByTagName("color").item(0);
		if("0".equals(runPostStepsIfResult)){
			ParseXmlUtil.setRunPostStepElement(name, "name", "SUCCESS", document);
			ParseXmlUtil.setRunPostStepElement(ordinal, "ordinal", "0", document);
			ParseXmlUtil.setRunPostStepElement(color, "color", "BLUE", document);
		}else if("1".equals(runPostStepsIfResult)){
			ParseXmlUtil.setRunPostStepElement(name, "name", "UNSTABLE", document);
			ParseXmlUtil.setRunPostStepElement(ordinal, "ordinal", "1", document);
			ParseXmlUtil.setRunPostStepElement(color, "color", "YELLOW", document);
		}else if("2".equals(runPostStepsIfResult)){
			ParseXmlUtil.setRunPostStepElement(name, "name", "FAILURE", document);
			ParseXmlUtil.setRunPostStepElement(ordinal, "ordinal", "2", document);
			ParseXmlUtil.setRunPostStepElement(color, "color", "RED", document);
		}
	}

	/**
	* @Title: setRunPostStepElement 
	* @author weiyujia
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param childElement
	* @param childName
	* @param value
	* @param document  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年4月18日 下午3:10:54
	 */
	public static void setRunPostStepElement(Element childElement,String childName,String value,Document document){
		Element modElement = (Element) document.getElementsByTagName("maven2-moduleset").item(0);
		Element runPostStepsIfResultElement = (Element) document.getElementsByTagName("runPostStepsIfResult").item(0);
		if(runPostStepsIfResultElement == null){
			runPostStepsIfResultElement = document.createElement("runPostStepsIfResult");
			modElement.appendChild(runPostStepsIfResultElement);
		}
		if(childElement == null){
			childElement = document.createElement(childName);
			childElement.setTextContent(value);
			runPostStepsIfResultElement.appendChild(childElement);
		}else{
			childElement.setTextContent(value);
		}
	}
	
	/**
	* @Title: removeXmlChild 
	* @author weiyujia
	* @Description: 移除指定子节点
	* @param string
	* @param document  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年4月13日 上午11:12:17
	 */
	public static void removeXmlChild(String type, Document document) {
		Element postbuilders = (Element) document.getElementsByTagName("postbuilders").item(0);
		Element fatherElement = (Element) document.getElementsByTagName(type).item(0);
		if (fatherElement != null) {
			if (type.equals("hudson.tasks.BatchFile") || type.equals("hudson.tasks.Shell")) {
				Element command = (Element) fatherElement.getElementsByTagName("command").item(0);
				fatherElement.removeChild(command);
			} else if (type.equals("hudson.tasks.Ant")) {
				Element targets = (Element) fatherElement.getElementsByTagName("targets").item(0);
				fatherElement.removeChild(targets);
			}
			postbuilders.removeChild(fatherElement);
		}
	}

	/**
	* @Title: updatePostbuildersElement 
	* @author weiyujia
	* @Description: 更新Postbuilders节点
	* @param type  节点类型
	* @param target	节点子类型
	* @param value   子类型值 
	* @param document  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年4月13日 上午10:48:17
	*/
	public static void updatePostbuildersElement(String type, String target, String value, Document document) {
		Element postbuilders = (Element) document.getElementsByTagName("postbuilders").item(0);
		Element hudsonTasksShell = (Element) document.getElementsByTagName(type).item(0);
		if (hudsonTasksShell == null) {
			hudsonTasksShell = document.createElement(type);
			Element command = document.createElement(target);
			command.setTextContent(value);
			hudsonTasksShell.appendChild(command);
			postbuilders.appendChild(hudsonTasksShell);
		}else{
			Element command = (Element) hudsonTasksShell.getElementsByTagName(target).item(0);
			command.setTextContent(value);
		}
	}

	/**
	* @Title: updateXmlElement 
	* @author weiyujia
	 * @param value 
	* @Description: 更新/插入xml element节点
	* @param string
	* @param document  设定文件 
	* @return void    返回类型 
	* @throws 
	* @date 2016年4月12日 下午6:28:25
	 */
	public  static void updateXmlElement(String tagName, String value, Document document) {
		Element element = (Element) document.getElementsByTagName(tagName).item(0);
		if(element==null){
			element=document.createElement(tagName);
			element.setTextContent(value);
			Element modElement = (Element) document.getElementsByTagName("maven2-moduleset").item(0);
			if(modElement == null){
				modElement = (Element) document.getElementsByTagName("maven2").item(0);
			}
			modElement.appendChild(element);
		}
		element.setTextContent(value);
	}
	
	
	/**
	 * @author weiyujia
	 * @Description: 将JSON串转换为Map
	 * @param @param resultJson
	 * @param @return
	 * @date 2016年3月14日 下午8:34:01
	 * @return
	 * @throws
	 */
	public static Map<String, Object> fromJsonToObject(String resultJson) {
		JSONObject jsonObject = JSONObject.parseObject(resultJson);
		Map<String, Object> resultList = parseJSON2List(jsonObject);
		return resultList;
	}

	/**
	 * @author weiyujia
	 * @Description: 将JSONObject转换为List
	 * @param @param jsonObject
	 * @param @return
	 * @date 2016年3月14日 上午11:55:53
	 * @return
	 * @throws
	 */
	private static Map<String, Object> parseJSON2List(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		Set<?> keys = jsonObject.keySet();
		for (Object key : keys) {
			Object o = jsonObject.get(key);
			if (o instanceof JSONObject)
				map.put((String) key, parseJSON2List((JSONObject) o));
			else
				map.put((String) key, o);
		}
		return map;
	}

	/**
	* @Title: getFileBelongModel 
	* @author weiyujia
	* @Description: 获取 fileBelongModel 节点
	* @param configXml
	* @return
	* @throws ParserConfigurationException
	* @throws SAXException
	* @throws IOException  设定文件 
	* @throws 
	* @date 2016年4月22日 下午3:05:59
	 */
	public static String getFileBelongModel(String configXml) throws ParserConfigurationException, SAXException, IOException {
		Document document = ParseXmlUtil.getDocumentByConfigxmlStr(configXml);
		Element fileBelongModelElement = (Element) document.getElementsByTagName("fileBelongModel").item(0);
		return fileBelongModelElement == null ? "" : fileBelongModelElement.getTextContent();
	}
}
