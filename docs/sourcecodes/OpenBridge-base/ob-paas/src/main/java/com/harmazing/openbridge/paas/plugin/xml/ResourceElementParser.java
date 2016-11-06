package com.harmazing.openbridge.paas.plugin.xml;

import java.util.List;
import java.util.Map;

import com.harmazing.framework.extension.ParserContext;
import com.harmazing.framework.extension.config.Configuration;
import com.harmazing.framework.extension.config.Node;
import com.harmazing.framework.extension.interfaces.INode;
import com.harmazing.framework.extension.interfaces.INodeParser;

public class ResourceElementParser implements INodeParser {

	@Override
	public Configuration parse(Node node, ParserContext context) {
		ResourceNode resource = new ResourceNode();
		Map<String, String> attr = node.getAttributes();
		resource.setId(attr.get("id"));
		resource.setName(attr.get("name"));
		resource.setCategory(attr.get("category"));

		resource.setBean(attr.get("bean"));

		resource.setConfig(attr.get("config"));
		resource.setShow(attr.get("show"));
		resource.setNode(node);

		List<INode> summary = node.getChildren();
		if (summary.size() > 0) {
			resource.setSummary(summary.get(0).getNodeText());
			resource.setHelp(summary.get(0).getAttributes().get("help"));
			resource.setIcon(summary.get(0).getAttributes().get("icon"));
		}
		return resource;
	}
}
