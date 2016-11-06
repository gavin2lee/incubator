package com.harmazing.framework.extension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.Resource;

import com.harmazing.framework.extension.interfaces.IExtension;
import com.harmazing.framework.extension.interfaces.IExtensionPoint;
import com.harmazing.framework.extension.interfaces.IPluginFactory;

public class PluginContext implements IPluginFactory {
	private final Map<String, IExtensionPoint> extensionPointMap = new HashMap<String, IExtensionPoint>();

	@Override
	public void init(List<Resource> configLocations) throws Exception {
		for (int i = 0; i < configLocations.size(); i++) {
			PluginXmlParser.parseExtensionPoint(this, configLocations.get(i),
					extensionPointMap);
		}

		Iterator<IExtensionPoint> points = extensionPointMap.values()
				.iterator();
		while (points.hasNext()) {
			IExtensionPoint point = points.next();
			point.getNamespaceHandler().beforeLoad();
		}
		for (int i = 0; i < configLocations.size(); i++) {
			PluginXmlParser.parseExtension(configLocations.get(i),
					extensionPointMap);
		}
		points = extensionPointMap.values().iterator();
		while (points.hasNext()) {
			IExtensionPoint point = points.next();
			point.getNamespaceHandler().afterLoad();
		}
	}

	@Override
	public Collection<IExtension> getExtensions(String extensionPointId) {
		return getExtensionPoint(extensionPointId).getExtensions();
	}

	@Override
	public Map<String, IExtensionPoint> getExtensionPoints() {
		return extensionPointMap;
	}

	@Override
	public IExtensionPoint getExtensionPoint(String extensionPointId) {
		return extensionPointMap.get(extensionPointId);
	}

}
