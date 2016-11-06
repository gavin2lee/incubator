package com.harmazing.framework.extension;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import com.harmazing.framework.extension.config.Configuration;
import com.harmazing.framework.extension.config.Extension;
import com.harmazing.framework.extension.config.ExtensionPoint;
import com.harmazing.framework.extension.config.Node;
import com.harmazing.framework.extension.interfaces.IConfiguration;
import com.harmazing.framework.extension.interfaces.IExtensionPoint;
import com.harmazing.framework.extension.interfaces.INamespaceHandler;
import com.harmazing.framework.extension.interfaces.INode;
import com.harmazing.framework.extension.interfaces.IPluginFactory;
import com.harmazing.framework.util.StringUtil;
import com.harmazing.framework.util.XmlUtil;

public class PluginXmlParser {
	private static final Log logger = LogFactory.getLog(PluginXmlParser.class);
	private static String XML_EXTENSION_POINT = "extension-point";
	private static String XML_EXTENSION = "extension";

	public static void parseExtensionPoint(IPluginFactory pluginFactory,
			Resource resource, Map<String, IExtensionPoint> extensionPointMap)
			throws Exception {
		InputStream stream = null;
		try {
			stream = resource.getInputStream();
			Document doc = XmlUtil.buildDocument(stream);
			List<Element> points = XmlUtil.getChildElementsByTagName(
					doc.getDocumentElement(), XML_EXTENSION_POINT);
			for (int i = 0; i < points.size(); i++) {
				parseExtensionPoint(pluginFactory, resource, points.get(i),
						extensionPointMap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			stream.close();
		}

	}

	public static void parseExtension(Resource resource,
			Map<String, IExtensionPoint> extensionPointMap) throws Exception {
		InputStream stream = null;
		try {
			stream = resource.getInputStream();
			Document doc = XmlUtil.buildDocument(stream);
			List<Element> exts = XmlUtil.getChildElementsByTagName(
					doc.getDocumentElement(), XML_EXTENSION);
			ParserContext context = new ParserContext();
			context.setResource(resource);
			for (int i = 0; i < exts.size(); i++) {
				parseExtension(resource, exts.get(i), extensionPointMap,
						context);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			stream.close();
		}
	}

	private static void parseExtensionPoint(IPluginFactory pluginFactory,
			Resource resource, Element element,
			Map<String, IExtensionPoint> extensionPointMap) throws Exception {
		try {
			ExtensionPoint point = new ExtensionPoint();
			point.setPointID(element.getAttribute("id"));
			point.setPointName(element.getAttribute("name"));
			point.setResource(resource);
			point.setPluginFactory(pluginFactory);
			String namespaceHandler = element.getAttribute("handler");

			INamespaceHandler handler = (INamespaceHandler) Class.forName(
					namespaceHandler).newInstance();
			handler.init();
			point.setNamespaceHandler(handler);
			extensionPointMap.put(point.getPointID(), point);
		} catch (Exception e) {
			throw new Exception("文件URL=" + resource.getURL().getFile()
					+ "加载异常,异常信息：" + e.getMessage());
		}
	}

	private static void parseExtension(Resource resource, Element element,
			Map<String, IExtensionPoint> extensionPointMap,
			ParserContext context) throws Exception {
		Extension ext = new Extension();
		String point = element.getAttribute("point");
		try {
			ext.setPointID(point);
			if (StringUtil.isNull(ext.getPointID())) {
				throw new Exception("扩展点属性point不能为空。");
			}
			if (!extensionPointMap.containsKey(point)) {
				throw new Exception("没有扩展点id=" + point + "的定义。");
			}
			IExtensionPoint ip = extensionPointMap.get(point);
			ext.setExtensionPoint(ip);
			List<Element> eles = XmlUtil.getChildElements(element);
			List<INode> nodes = new ArrayList<INode>();
			for (int i = 0; i < eles.size(); i++) {
				Element elem = eles.get(i);
				Node node = convertElement2ConfigNode(elem);
				context.setElement(elem);
				Configuration config = ip.getNamespaceHandler().parse(node,
						context);
				config.setNode(node);
				if (ip.getAllConfigNodes().containsKey(elem.getLocalName())) {
					ip.getConfigNodes(elem.getLocalName()).put(config.getId(),
							config);
				} else {
					Map<String, IConfiguration> configs = new HashMap<String, IConfiguration>();
					configs.put(config.getId(), config);
					ip.getAllConfigNodes().put(elem.getLocalName(), configs);
				}
			}
			ext.setNodes(nodes);
			extensionPointMap.get(point).getExtensions().add(ext);
		} catch (Exception e) {
			ext = null;
			logger.warn("文件URL=" + resource.getURL().getFile()
					+ "中,该扩展项被抛弃。异常信息：" + e.getMessage());
		}
	}

	private static Node convertElement2ConfigNode(Element element) {
		Node node = new Node();
		node.setNodeName(element.getLocalName());
		node.setNodeText(element.getTextContent());
		NamedNodeMap nodeMap = element.getAttributes();
		Map<String, String> attributes = new HashMap<String, String>();
		for (int i = 0; i < nodeMap.getLength(); i++) {
			attributes.put(nodeMap.item(i).getNodeName(), nodeMap.item(i)
					.getNodeValue());
		}
		node.setAttributes(attributes);
		List<Element> eles = XmlUtil.getChildElements(element);
		if (eles != null) {
			List<INode> children = new ArrayList<INode>();
			for (int i = 0; i < eles.size(); i++) {
				children.add(convertElement2ConfigNode(eles.get(i)));
			}
			node.setChildren(children);
		}
		return node;
	}
}
