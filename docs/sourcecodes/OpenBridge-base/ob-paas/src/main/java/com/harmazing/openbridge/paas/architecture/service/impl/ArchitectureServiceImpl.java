package com.harmazing.openbridge.paas.architecture.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLInputFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.authorization.IUserService;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.FileUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.XmlUtil;
import com.harmazing.openbridge.paas.architecture.service.IArchitectureService;
import com.harmazing.openbridge.paas.architecture.vo.GraphParam;
import com.harmazing.openbridge.paas.architecture.vo.GraphVo;
import com.harmazing.framework.util.PaasAPIUtil.DataType;


public abstract class ArchitectureServiceImpl implements IArchitectureService{

	private static Log logger = LogFactory
			.getLog(ArchitectureServiceImpl.class);
	
	
	private static final String XML_PATH = "META-INF/architecture/architecture.xml";

	@Override
	public List<GraphVo> getGraphByType(String type,String userId) {
		List<GraphVo> temp = new ArrayList<GraphVo>();
		GraphVo parserXml = parserXml(type,userId);
		temp.add(parserXml);
		return temp;
	}
	
	
	
	
	
	private void parseProperties(Element root,GraphVo result,Document document,String userId){
		if(root==null || root.getChildNodes().getLength()==0){
			return ;
		}
		NodeList childNodes = root.getChildNodes();
		for(int i=0;i<childNodes.getLength();i++){
			Node item = childNodes.item(i);
			if(Node.ELEMENT_NODE!=item.getNodeType() || !item.getNodeName().equals("property")){
				continue ;
			}
			Element el = (Element)item;
			String k = el.getAttribute("key");
			String v = el.getAttribute("value");
			if(StringUtils.isEmpty(v)){
				v = el.getChildNodes().item(0).getNodeValue();
			}
			result.getProperty().put(k, v);
		}
	}
	private List<GraphVo> parseMethod(Element root,Document document,String userId){
		String classname = root.getAttribute("class");
		String method = root.getAttribute("method");
		try {
			Class clazz = Class.forName(classname);
			Object o = SpringUtil.getBean(clazz);
			Method m = clazz.getMethod(method, new Class[]{String.class});
			Object r = m.invoke(o, new Object[]{userId});
			if(r==null){
				return new ArrayList<GraphVo>();
			}
			return (List<GraphVo>)r;
		} catch (ClassNotFoundException e) {
			logger.error(classname+"无法识别",e);
		} catch(Exception e){
			logger.error(classname+" 反射出现问题",e);
		}
		return new ArrayList<GraphVo>();
	}
	
	private List<GraphVo> parseImport(Element root,Document document,String userId){
		List<GraphVo> r = new ArrayList<GraphVo>();
		if(root==null){
			return r;
		}
		String ref = root.getAttribute("ref");
		Element template = document.getElementById(ref);
		if(template==null){
			logger.error("ref "+ref+"没有对应节点");
			return r;
		}
		NodeList childNodes = template.getChildNodes();
		for(int i=0;i<childNodes.getLength();i++){
			Node item = childNodes.item(i);
			if(Node.ELEMENT_NODE!=item.getNodeType()){
				continue ;
			}
			if(item.getNodeName().equals("architecture")){
				r.add(parseArchitecture((Element)item, document,userId));
			}
			else if(item.getNodeName().equals("import")){
				r.addAll(parseImport((Element)item, document,userId));
			}
			else if(item.getNodeName().equals("method")){
				r.addAll(parseMethod((Element)item, document,userId));
			}
			else{
				logger.debug(item.getNodeName()+" 无法识别");
				continue ;
			}
		}
		return r;
	}
	private void parseChildren(Element root,GraphVo result,Document document,String userId){
		if(root==null || root.getChildNodes().getLength()==0){
			return ;
		}
		NodeList childNodes = root.getChildNodes();
		for(int i=0;i<childNodes.getLength();i++){
			Node item = childNodes.item(i);
			if(Node.ELEMENT_NODE!=item.getNodeType()){
				continue ;
			}
			if(item.getNodeName().equals("architecture")){
				result.getChildren().add(parseArchitecture((Element)item,document,userId));
			}
			else if(item.getNodeName().equals("import")){
				result.getChildren().addAll(parseImport((Element)item,document,userId));
			}
			else if(item.getNodeName().equals("method")){
				result.getChildren().addAll(parseMethod((Element)item,document,userId));
			}
			else{
				logger.debug(item.getNodeName()+" 无法识别");
				continue ;
			}
		}
	}
	private GraphVo parseArchitecture(Element root,Document document,String userId){
		String name = root.getAttribute("name");
		String code = root.getAttribute("code");
		String cluster = root.getAttribute("cluster");
		String spec = root.getAttribute("spec");
		NodeList childNodes = root.getChildNodes();
		
		GraphVo graphVo = new GraphVo();
		graphVo.setName(name);
		for(int i=0;i<childNodes.getLength();i++){
			Node item = childNodes.item(i);
			if(Node.ELEMENT_NODE!=item.getNodeType()){
				continue ;
			}
			String nodeName = item.getNodeName();
			if("properties".equals(nodeName)){
				parseProperties((Element)item,graphVo,document,userId);
			}
			else if("children".equals(nodeName)){
				parseChildren((Element)item,graphVo,document,userId);
			}
		}
		
		if(StringUtils.isEmpty(spec)){
			String c = ConfigUtil.getConfigString(code);
			if(StringUtils.isEmpty(c)){
				graphVo.getProperty().put("error", 2);
			}
			else if(StringUtil.split(c).length==1){
				graphVo.getProperty().put("url", c);
			}
			else if(StringUtils.hasText(cluster) && "true".equals(cluster) && graphVo.getChildren().size()==0){
				String[] cs = StringUtil.split(c);
				for(String s : cs){
					if(StringUtils.isEmpty(s)){
						continue;
					}
					GraphVo child = new GraphVo();
					child.setName(s.split(":")[0]);
					for(Map.Entry<String, Object> en : graphVo.getProperty().entrySet()){
						child.getProperty().put(en.getKey()+"", en.getValue()+"");
					}
					child.getProperty().put("url", s);
					graphVo.getChildren().add(child);
				}
			}
		}
		else if("redis".equals(spec)){
			String[] c = StringUtil.split(code);
			String ip = ConfigUtil.getConfigString(c[0]);
			String port = ConfigUtil.getConfigString(c[1]);
			if(StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)){
				graphVo.getProperty().put("error", 2);
			}
			graphVo.getProperty().put("url", ip+":"+port);
		}
		return graphVo;
	}
	
	
	
	private GraphVo parserXml(String bussiness_type,String userId){
		Document document =null;
		try{
			String c = FileUtil.getFileString(this.getClass().getClassLoader().getResource(XML_PATH));
			c = c.replaceAll("\\{bussiness_type\\}", bussiness_type);
			document = XmlUtil.buildDocumentForString(c);
			logger.debug(document.getClass().getName());
			Element root = document.getElementById(bussiness_type);
			GraphVo result = parseArchitecture(root,document,userId);
			return result;
		}
		catch(Exception e){
			logger.error("解析xml失败",e);
			throw new RuntimeException(e);
		}
		finally{
			if(document!=null){
				document = null;
			}
		}
	}
	
	@Override
	public List<GraphVo> getPaasNodes(String userId){
		return new ArrayList<GraphVo>();
	}
	@Override
	public List<GraphVo> getPaasProxys(String userId){
		return new ArrayList<GraphVo>();
	}
	@Override
	public List<GraphVo> getPaasResources(String userId){
		return new ArrayList<GraphVo>();
	}
	@Override
	public List<GraphVo> getPaas(String userId){
		List<GraphVo> t = new ArrayList<GraphVo>();
		String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		try{
			if(StringUtils.isEmpty(osPath)){
				throw new RuntimeException("没有配置os");
			}
			if (!osPath.endsWith("/")) {
				osPath = osPath + "/";
			}
			osPath = osPath+"sys/architecture/graph.do";
			if (logger.isDebugEnabled()) {
				logger.debug(osPath);
			}
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("business_type", "paasos");
			String msg = PaasAPIUtil.post("admin", osPath,DataType.FORM, param);
			if (logger.isDebugEnabled()) {
				logger.debug(msg);
			}
			JSONObject jo = JSONObject.parseObject(msg);
			int code = jo.getIntValue("code");
			if (code != 0) {
				throw new RuntimeException("从os中返回的数据为" + code);
			}
			GraphVo d = JSONObject.toJavaObject(jo.getJSONObject("data"), GraphVo.class);
			
			
			t.add(d);
		}
		catch(Exception e){
			logger.debug(e);
			GraphVo d = new GraphVo();
			d.setName("Paasos");
			d.getProperty().put("error", "1");
			d.getProperty().put("msg", osPath);
			t.add(d);
		}
		return t;
	}
	
	public Map<String, Object> getAppAndNodeStatus(GraphParam param){
		Map<String, Object> result = new HashMap<String, Object>();
		if("http".equals(param.getProtocol().toLowerCase())){
			String url = param.getUrl();
			try{
				if(url.startsWith("http://") || url.startsWith("https://")){
				}
				else{
					url = "http://"+param.getUrl();
				}
				PaasAPIUtil.post(param.getUserId(), url, DataType.FORM, param.getParam());
			}
			catch(Exception e){
				logger.error(url+"(对代码没影响) http error");
				result.put("error", "1");
			}
		}
		else if("tcp".equals(param.getProtocol().toLowerCase())){
			Pattern pa = Pattern.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,6})");
			Matcher ma = pa.matcher(param.getUrl());
			String url = param.getUrl();
			if(ma.find()){
				url = ma.group(1);
			}
			logger.debug(url);
			String[] is = url.split(":");
			Socket socket = null;
			try{
				socket = new Socket(is[0], Integer.parseInt(is[1]));
			}
			catch(UnknownHostException e){
				logger.error(param.getUrl()+"(对代码没影响) http error");
				result.put("error", "1");
			}
			catch(IOException e2){
				logger.error(param.getUrl()+"(对代码没影响) http error");
				result.put("error", "1");
			}
			finally{
				if(socket !=null){
					try {
						socket.close();
					} catch (IOException e) {
						
					}
				}
			}
			result.put("error", "-1");
		}
		return result;
	}
}
