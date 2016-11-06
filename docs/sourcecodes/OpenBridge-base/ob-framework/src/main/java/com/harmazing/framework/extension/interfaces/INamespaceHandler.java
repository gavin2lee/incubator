package com.harmazing.framework.extension.interfaces;

import com.harmazing.framework.extension.ParserContext;
import com.harmazing.framework.extension.config.Configuration;
import com.harmazing.framework.extension.config.Node;


public abstract interface INamespaceHandler {
	public abstract void init();

	public abstract Configuration parse(Node node, ParserContext context);

	public abstract void afterLoad();

	public abstract void beforeLoad();
}
