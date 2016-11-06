package com.harmazing.framework.extension.config;

import com.harmazing.framework.extension.interfaces.IConfiguration;
import com.harmazing.framework.extension.interfaces.INode;

public abstract class Configuration implements IConfiguration {
	private INode node;

	@Override
	public INode getNode() {
		return node;
	}

	public void setNode(INode node) {
		this.node = node;
	}
}
