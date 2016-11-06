package com.harmazing.framework.extension;

import java.util.Collection;
import java.util.Map;

import com.harmazing.framework.extension.interfaces.IExtension;
import com.harmazing.framework.extension.interfaces.IExtensionPoint;
import com.harmazing.framework.extension.interfaces.IPluginFactory;

public class Plugin {
	private static Long startTime;
	private static final IPluginFactory pluginContext = new PluginContext();

	static {
		startTime = System.currentTimeMillis();
	}

	public static IPluginFactory getPluginContext() {
		return pluginContext;
	}

	public static Collection<IExtension> getExtensions(String extensionPointId) {
		return pluginContext.getExtensions(extensionPointId);
	}

	public static IExtensionPoint getExtensionPoint(String extensionPointId) {
		return pluginContext.getExtensionPoint(extensionPointId);
	}

	public static Map<String, IExtensionPoint> getExtensionPoints() {
		return pluginContext.getExtensionPoints();
	}

	public static Long getStartTime() {
		return startTime;
	}
}
