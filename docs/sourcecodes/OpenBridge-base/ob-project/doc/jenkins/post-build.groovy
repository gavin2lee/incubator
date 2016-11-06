

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.*;


import java.util.regex.Matcher;
import java.util.regex.Pattern;




println "------post build------"
//def fs = File.separator
//def filePath = binding.variables.JENKINS_HOME+fs+"jobs"+fs+binding.variables.JOB_NAME+fs+"config.xml"

def postBuildUrl = ""

//try {
	//读取book.xml到内存
//	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//	DocumentBuilder dbd = dbf.newDocumentBuilder();
//	Document doc = dbd.parse(new FileInputStream(filePath));
//	println("file : "+filePath)
//	println("exist:"+(new File(filePath).exists()))
//
//	def element = doc.getDocumentElement();// 得到一个elment根元素  
//	def source = element.getElementsByTagName("sourceFromType")
//	def nodes = element.getElementsByTagName("postBuildUrl")
//	if(nodes.getLength()>0){
//		postBuildUrl = nodes.item(0).getTextContent();
//	}
//	if(source.getLength()>0){
//		println("source:"+source.item(0).getTextContent());        
//	}
	String source  = binding.variables.SOURCE_FROM_TYPE;
	println("source:"+source);
	postBuildUrl  = binding.variables.POST_BUILD_URL;

    if(postBuildUrl){
		Pattern pattern = Pattern.compile("\\\$\\{([^\\}]+)\\}");
		Matcher matcher = pattern.matcher(postBuildUrl);
		while(matcher.find()){
			String var = matcher.group(1);
			println(var+'---'+binding.variables.get(var));
			postBuildUrl = postBuildUrl.replace(matcher.group(0),binding.variables.get(var));
		}
		
		if(postBuildUrl.indexOf("?")==-1){
			postBuildUrl += "?";
		}else{
			postBuildUrl += "&";
		}
		def post_url = postBuildUrl+"jobName="+binding.variables.JOB_NAME+"&number="+binding.variables.BUILD_NUMBER
		println("post_url:"+post_url)
		def connection = new URL(post_url).openConnection()
		connection.setRequestMethod('GET')
		connection.setRequestProperty('contentType','text/plain;charset=utf-8')
		connection.doOutput = true
		def writer = new OutputStreamWriter(connection.outputStream)
		writer.flush()
		writer.close()
		connection.connect()
		def respText = connection.content.text
		println respText
	}else{
		println('postBuildUrl is not defined.');
	}
//} catch (Exception e) {
//	println("--xml read error--"+e.getMessage())
//	e.printStackTrace();
//}  

//def apiServerUri = binding.variables.API_SERVER_URI

//def appServerUri = binding.variables.APP_SERVER_URI

//println jenkins.model.Jenkins.instance.getItem("APP Web 快速构建").lastBuild.number;

