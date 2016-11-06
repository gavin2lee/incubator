package com.harmazing.framework.extension.interfaces;

import java.util.List;

import org.springframework.core.io.Resource;

public interface IExtension {
	String getPointID();

	/**
	 * 返回扩展所在的资源，比如：所在配置文件。
	 */
	Resource getResource();

	IExtensionPoint getExtensionPoint();

	List<INode> getNodes();
}
