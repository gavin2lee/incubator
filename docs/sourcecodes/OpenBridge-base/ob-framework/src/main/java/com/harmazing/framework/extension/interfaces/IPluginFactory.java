package com.harmazing.framework.extension.interfaces;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

public interface IPluginFactory {
	/**
	 * 插件工厂初始化
	 */
	void init(List<Resource> configLocations) throws Exception;

	/**
	 * 返回指定扩展点和指定使用范围的扩展集。若没找到，则返回<code>null</code>。
	 */
	Collection<IExtension> getExtensions(String extensionPointId);

	/**
	 * 获得扩展点。
	 */
	IExtensionPoint getExtensionPoint(String extensionPointId);

	/**
	 * 获得所有扩展点。
	 */
	Map<String, IExtensionPoint> getExtensionPoints();
}
