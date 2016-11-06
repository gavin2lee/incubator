package com.harmazing.framework.extension.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.harmazing.framework.extension.ParserContext;
import com.harmazing.framework.extension.interfaces.INamespaceHandler;
import com.harmazing.framework.extension.interfaces.INodeParser;

public abstract class AbstractNamespaceHandler implements INamespaceHandler {
	private final Log logger = LogFactory.getLog(getClass());
	private final Map<String, INodeParser> parsers = new HashMap<String, INodeParser>();

	public Configuration parse(Node node, ParserContext context) {
		INodeParser parser = this.parsers.get(node.getNodeName());
		if (parser == null) {
			parser = this.parsers.get("*");
		}
		if (parser != null) {
			return parser.parse(node, context);
		} else {
			return null;
		}
	}

	protected final void registerBeanDefinitionParser(String elementName,
			INodeParser parser) {
		this.parsers.put(elementName, parser);
	}

	public void afterLoad() {

	}

	public void beforeLoad() {

	}
}
