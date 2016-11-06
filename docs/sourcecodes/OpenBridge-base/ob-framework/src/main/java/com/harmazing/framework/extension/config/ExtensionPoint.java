package com.harmazing.framework.extension.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.harmazing.framework.extension.interfaces.IConfiguration;
import com.harmazing.framework.extension.interfaces.IExtension;
import com.harmazing.framework.extension.interfaces.IExtensionPoint;
import com.harmazing.framework.extension.interfaces.INamespaceHandler;
import com.harmazing.framework.extension.interfaces.IPluginFactory;

public class ExtensionPoint implements IExtensionPoint {

	private String pointName;

	private String pointID;

	private Resource resource;

	private Collection<IExtension> extensions = new ArrayList<IExtension>();

	private Map<String, Map<String, IConfiguration>> configs = new HashMap<String, Map<String, IConfiguration>>();

	private IPluginFactory pluginFactory;

	private INamespaceHandler namespaceHandler;

	@Override
	public String getPointID() {
		return pointID;
	}

	@Override
	public Resource getResource() {
		return resource;
	}

	@Override
	public IPluginFactory getPluginFactory() {
		return pluginFactory;
	}

	@Override
	public Collection<IExtension> getExtensions() {
		return extensions;
	}

	@Override
	public String getPointName() {
		return pointName;
	}

	public void setPointName(String pointName) {
		this.pointName = pointName;
	}

	public void setPointID(String pointID) {
		this.pointID = pointID;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setExtensions(Collection<IExtension> extensions) {
		this.extensions = extensions;
	}

	public void setPluginFactory(IPluginFactory pluginFactory) {
		this.pluginFactory = pluginFactory;
	}

	@Override
	public INamespaceHandler getNamespaceHandler() {
		return this.namespaceHandler;
	}

	public void setNamespaceHandler(INamespaceHandler namespaceHandler) {
		this.namespaceHandler = namespaceHandler;
	}

	@Override
	public Map<String, IConfiguration> getConfigNodes(String elementName) {
		return this.configs.get(elementName);
	}

	@Override
	public Map<String, Map<String, IConfiguration>> getAllConfigNodes() {
		return this.configs;
	}

	@Override
	public IConfiguration getConfigNode(String elementName, String id) {
		Map<String, IConfiguration> elements = this.configs.get(elementName);
		if (elements == null) {
			throw new RuntimeException(elementName + ":没有注册解释程序");
		}
		IConfiguration config = elements.get(id);
		if (config == null) {
			throw new RuntimeException(id + ":没有注册");
		}
		return config;
	}
}
