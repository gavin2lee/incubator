package com.harmazing.framework.extension.interfaces;

import java.util.Collection;
import java.util.Map;

import org.springframework.core.io.Resource;

public interface IExtensionPoint {
	/**
	 * 扩展点名称
	 */
	String getPointName();

	/**
	 * 扩展点id
	 */
	String getPointID();

	/**
	 * 返回扩展所在的资源，比如：所在配置文件。
	 */
	Resource getResource();

	/**
	 * 返回指定范围的扩展集
	 */
	Collection<IExtension> getExtensions();

	/**
	 * 获取插件所在的插件工厂
	 * 
	 * @return
	 */
	IPluginFactory getPluginFactory();

	/**
	 * 获取插件的XMl解析器
	 * 
	 * @return
	 */
	INamespaceHandler getNamespaceHandler();

	Map<String, Map<String, IConfiguration>> getAllConfigNodes();

	Map<String, IConfiguration> getConfigNodes(String elementName);

	IConfiguration getConfigNode(String elementName, String id);

}
