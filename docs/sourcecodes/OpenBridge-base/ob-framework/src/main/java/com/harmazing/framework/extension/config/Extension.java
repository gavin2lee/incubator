package com.harmazing.framework.extension.config;

import java.util.List;

import org.springframework.core.io.Resource;

import com.harmazing.framework.extension.interfaces.IExtension;
import com.harmazing.framework.extension.interfaces.IExtensionPoint;
import com.harmazing.framework.extension.interfaces.INode;

public class Extension implements IExtension {
	private Resource resource;
	private String pointID;
	private IExtensionPoint extensionPoint;
	private List<INode> nodes;

	@Override
	public Resource getResource() {
		return resource;
	}

	@Override
	public IExtensionPoint getExtensionPoint() {
		return extensionPoint;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void setExtensionPoint(IExtensionPoint extensionPoint) {
		this.extensionPoint = extensionPoint;
	}

	public String getPointID() {
		return pointID;
	}

	public void setPointID(String pointID) {
		this.pointID = pointID;
	}

	public List<INode> getNodes() {
		return nodes;
	}

	public void setNodes(List<INode> nodes) {
		this.nodes = nodes;
	}

}
