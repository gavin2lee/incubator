package com.harmazing.openbridge.monitor.service.impl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.FileUtil;
import com.harmazing.framework.util.PaasAPIUtil;
import com.harmazing.framework.util.PaasAPIUtil.DataType;
import com.harmazing.framework.util.SpringUtil;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.XmlUtil;
import com.harmazing.openbridge.monitor.model.vo.GraphParam;
import com.harmazing.openbridge.monitor.model.vo.GraphVo;
import com.harmazing.openbridge.monitor.service.IMonitorArchitectureService;
import com.harmazing.openbridge.util.KubernetesUtil;

import io.fabric8.kubernetes.api.model.NodeAddress;

@Service
public class MonitorArchitectureServiceImpl implements
		IMonitorArchitectureService {

	private static Log logger = LogFactory
			.getLog(MonitorArchitectureServiceImpl.class);

	private static final String XML_PATH = "META-INF/architecture/architecture.xml";

	@Override
	public List<GraphVo> getGraphByType(String type, String userId) {
		List<GraphVo> temp = new ArrayList<GraphVo>();
		GraphVo parserXml = parserXml(type, userId);
		temp.add(parserXml);
		return temp;
	}

	private void parseProperties(Element root, GraphVo result,
			Document document, String userId) {
		if (root == null || root.getChildNodes().getLength() == 0) {
			return;
		}
		NodeList childNodes = root.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (Node.ELEMENT_NODE != item.getNodeType()
					|| !item.getNodeName().equals("property")) {
				continue;
			}
			Element el = (Element) item;
			String k = el.getAttribute("key");
			String v = el.getAttribute("value");
			if (StringUtils.isEmpty(v)) {
				v = el.getChildNodes().item(0).getNodeValue();
			}
			result.getProperty().put(k, v);
		}
	}

	private List<GraphVo> parseMethod(Element root, Document document,
			String userId) {
		String classname = root.getAttribute("class");
		String method = root.getAttribute("method");
		try {
			Class clazz = Class.forName(classname);
			Object o = SpringUtil.getBean(clazz);
			Method m = clazz.getMethod(method, new Class[] { String.class });
			Object r = m.invoke(o, new Object[] { userId });
			if (r == null) {
				return new ArrayList<GraphVo>();
			}
			return (List<GraphVo>) r;
		} catch (ClassNotFoundException e) {
			logger.error(classname + "无法识别", e);
		} catch (Exception e) {
			logger.error(classname + " 反射出现问题", e);
		}
		return new ArrayList<GraphVo>();
	}

	private List<GraphVo> parseImport(Element root, Document document,
			String userId) {
		List<GraphVo> r = new ArrayList<GraphVo>();
		if (root == null) {
			return r;
		}
		String ref = root.getAttribute("ref");
		Element template = document.getElementById(ref);
		if (template == null) {
			logger.error("ref " + ref + "没有对应节点");
			return r;
		}
		NodeList childNodes = template.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (Node.ELEMENT_NODE != item.getNodeType()) {
				continue;
			}
			if (item.getNodeName().equals("architecture")) {
				r.add(parseArchitecture((Element) item, document, userId));
			} else if (item.getNodeName().equals("import")) {
				r.addAll(parseImport((Element) item, document, userId));
			} else if (item.getNodeName().equals("method")) {
				r.addAll(parseMethod((Element) item, document, userId));
			} else {
				logger.debug(item.getNodeName() + " 无法识别");
				continue;
			}
		}
		return r;
	}

	private void parseChildren(Element root, GraphVo result, Document document,
			String userId) {
		if (root == null || root.getChildNodes().getLength() == 0) {
			return;
		}
		NodeList childNodes = root.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (Node.ELEMENT_NODE != item.getNodeType()) {
				continue;
			}
			if (item.getNodeName().equals("architecture")) {
				result.getChildren().add(
						parseArchitecture((Element) item, document, userId));
			} else if (item.getNodeName().equals("import")) {
				result.getChildren().addAll(
						parseImport((Element) item, document, userId));
			} else if (item.getNodeName().equals("method")) {
				result.getChildren().addAll(
						parseMethod((Element) item, document, userId));
			} else {
				logger.debug(item.getNodeName() + " 无法识别");
				continue;
			}
		}
	}

	private GraphVo parseArchitecture(Element root, Document document,
			String userId) {
		String name = root.getAttribute("name");
		String code = root.getAttribute("code");
		String cluster = root.getAttribute("cluster");
		String spec = root.getAttribute("spec");
		NodeList childNodes = root.getChildNodes();

		GraphVo graphVo = new GraphVo();
		graphVo.setName(name);
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (Node.ELEMENT_NODE != item.getNodeType()) {
				continue;
			}
			String nodeName = item.getNodeName();
			if ("properties".equals(nodeName)) {
				parseProperties((Element) item, graphVo, document, userId);
			} else if ("children".equals(nodeName)) {
				parseChildren((Element) item, graphVo, document, userId);
			}
		}

		if (StringUtils.isEmpty(spec)) {
			if (StringUtils.isEmpty(code)) {
				graphVo.getProperty().put("error", -5);
				graphVo.getProperty().put("msg", "该节点没有设置code");
			} else if (StringUtils.isEmpty(ConfigUtil.getConfigString(code))) {
				graphVo.getProperty().put("error", 2);
				graphVo.getProperty().put("msg", code + "->对应的值位空");
			} else if (StringUtil.split(ConfigUtil.getConfigString(code)).length == 1) {
				graphVo.getProperty().put("url",
						ConfigUtil.getConfigString(code));
			} else if (StringUtils.hasText(cluster) && "true".equals(cluster)
					&& graphVo.getChildren().size() == 0) {
				String[] cs = StringUtil
						.split(ConfigUtil.getConfigString(code));
				for (String s : cs) {
					if (StringUtils.isEmpty(s)) {
						continue;
					}
					GraphVo child = new GraphVo();
					child.setName(s.split(":")[0]);
					for (Map.Entry<String, Object> en : graphVo.getProperty()
							.entrySet()) {
						child.getProperty().put(en.getKey() + "",
								en.getValue() + "");
					}
					child.getProperty().put("url", s);
					graphVo.getChildren().add(child);
				}
			}
		} else if ("redis".equals(spec)) {
			String[] c = StringUtil.split(code);
			String ip = ConfigUtil.getConfigString(c[0]);
			String port = ConfigUtil.getConfigString(c[1]);
			if (StringUtils.isEmpty(ip) || StringUtils.isEmpty(port)) {
				graphVo.getProperty().put("error", 2);
				graphVo.getProperty().put("msg", "redis设置有问题");
			}
			graphVo.getProperty().put("url", ip + ":" + port);
		}
		return graphVo;
	}

	private GraphVo parserXml(String bussiness_type, String userId) {
		Document document = null;
		try {
			String c = FileUtil.getFileString(this.getClass().getClassLoader()
					.getResource(XML_PATH));
			c = c.replaceAll("\\{bussiness_type\\}", bussiness_type);
			document = XmlUtil.buildDocumentForString(c);
			logger.debug(document.getClass().getName());
			Element root = document.getElementById(bussiness_type);
			GraphVo result = parseArchitecture(root, document, userId);
			return result;
		} catch (Exception e) {
			logger.error("解析xml失败", e);
			throw new RuntimeException(e);
		} finally {
			if (document != null) {
				document = null;
			}
		}
	}

	@Override
	public List<GraphVo> getPaasNodes(String userId) {
		io.fabric8.kubernetes.api.model.NodeList nl = KubernetesUtil.getNodes();
		if (nl == null) {
			return null;
		}
		List<GraphVo> t = new ArrayList<GraphVo>();
		for (io.fabric8.kubernetes.api.model.Node h : nl.getItems()) {
			GraphVo v = new GraphVo();
			if (h.getMetadata() == null) {
				continue;
			}
			v.setName(h.getMetadata().getName());
			if (h.getStatus() == null || h.getStatus() == null
					|| h.getStatus().getAddresses() == null
					|| h.getStatus().getAddresses().size() == 0) {
				continue;
			}
			List<NodeAddress> addresses = h.getStatus().getAddresses();
			String ip = null;
			for (NodeAddress a : addresses) {
				if ("InternalIP".equals(a.getType())) {
					ip = a.getAddress();
					break;
				}
			}
			if (StringUtil.isEmpty(ip)) {
				logger.debug(v.getName() + "没有内部IP");
				continue;
			}
			v.getProperty().put("url", ip + ":4194");
			v.getProperty().put("protocol", "tcp");
			t.add(v);
		}

		return t;
	}

	@Override
	public List<GraphVo> getPaasProxys(String userId) {

		String osPath = ConfigUtil.getConfigString("paasOsRestUrlprefix");
		if (!osPath.endsWith("/")) {
			osPath = osPath + "/";
		}
		// 镜像获取
		osPath = osPath + "paas/nginx/rest/host/list.do";
		if (logger.isDebugEnabled()) {
			logger.debug(osPath);
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("pageNo", 1);
		param.put("pageSize", 99999);
		String msg = PaasAPIUtil.post(userId, osPath, DataType.FORM, param);
		if (logger.isDebugEnabled()) {
			logger.debug(msg);
		}
		JSONObject jo = JSONObject.parseObject(msg);
		int code = jo.getIntValue("code");
		if (code != 0) {
			String error = jo.getString("msg");
			throw new RuntimeException(error);
		}
		JSONObject data = jo.getJSONObject("data");
		JSONArray ja = data.getJSONArray("list");
		if (ja == null || ja.size() == 0) {
			return null;
		}
		List<GraphVo> t = new ArrayList<GraphVo>();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject host = ja.getJSONObject(i);

			GraphVo v = new GraphVo();
			v.setName(host.getString("hostIp"));
			v.getProperty().put("url", host.getString("hostIp") + ":80");
			v.getProperty().put("protocol", "tcp");
			t.add(v);
		}
		return t;
	}

	@Override
	public List<GraphVo> getPaasResources(String userId) {
		List<GraphVo> t = new ArrayList<GraphVo>();
		GraphVo v = new GraphVo();
		v.setName("资源1");
		v.getProperty().put("protocol", "http");
		t.add(v);

		v = new GraphVo();
		v.setName("资源2");
		v.getProperty().put("protocol", "http");
		t.add(v);

		v = new GraphVo();
		v.setName("资源3");
		v.getProperty().put("protocol", "http");
		t.add(v);

		v = new GraphVo();
		v.setName("资源4");
		v.getProperty().put("protocol", "http");
		t.add(v);

		v = new GraphVo();
		v.setName("资源5");
		v.getProperty().put("protocol", "http");
		t.add(v);

		v = new GraphVo();
		v.setName("资源6");
		v.getProperty().put("protocol", "http");
		t.add(v);

		return t;
	}

	@Override
	public List<GraphVo> getPaas(String userId) {
		GraphVo paasos = parserXml("paasos", userId);
		List<GraphVo> r = new ArrayList<GraphVo>();
		r.add(paasos);
		return r;
	}

	@SuppressWarnings({ "unused", "deprecation" })
	private String httpGet(String url) {
		String response = null;
		SSLContext sslContext = null;
		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null,
					new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext);
		HttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf)
				.build();
		HttpGet request = new HttpGet(url);
		try {
			HttpResponse httpResponse = client.execute(request);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				return EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (IOException ex) {
			logger.error("PAASOS API调用失败", ex);
			throw new RuntimeException(ex);
		} finally {
			request.releaseConnection();
		}
		return response;
	}

	public Map<String, Object> getAppAndNodeStatus(GraphParam param) {
		Map<String, Object> result = new HashMap<String, Object>();
		if ("http".equals(param.getProtocol().toLowerCase())) {
			String url = param.getUrl();
			try {
				if (url.startsWith("http://") || url.startsWith("https://")) {
				} else {
					url = "http://" + param.getUrl();
				}
				httpGet(url);
				result.put("msg", "成功");
			} catch (Exception e) {
				logger.error(url + "(对代码没影响) http error");
				result.put("error", "1");
				result.put("msg", url + "访问失败");
			}
		} else if ("tcp".equals(param.getProtocol().toLowerCase())) {
			Pattern pa = Pattern
					.compile("([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,6})");
			Matcher ma = pa.matcher(param.getUrl());
			String url = param.getUrl();
			if (ma.find()) {
				url = ma.group(1);
			}
			logger.debug(url);
			String[] is = url.split(":");
			Socket socket = null;
			try {
				socket = new Socket(is[0], Integer.parseInt(is[1]));
				socket.setSoTimeout(1000);
				result.put("msg", "成功");
			} catch (UnknownHostException e) {
				logger.error(param.getUrl()
						+ "(对代码没影响UnknownHostException) http error");
				result.put("error", "1");
				result.put("msg", url + "-->socket失败");

			} catch (IOException e2) {
				logger.error(param.getUrl() + "(对代码没影响IOException) http error");
				result.put("error", "1");
				result.put("msg", url + "-->socket失败");
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						socket = null;
					}
				}
			}

		}
		return result;
	}
}
