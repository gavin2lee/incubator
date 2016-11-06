package com.harmazing.openbridge.paas.plugin.xml;

import com.harmazing.framework.extension.config.AbstractNamespaceHandler;

public class ResourceNamespaceHandler extends AbstractNamespaceHandler {

	@Override
	public void init() {
		registerBeanDefinitionParser("resource", new ResourceElementParser());
	}

}
