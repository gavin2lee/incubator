package com.harmazing.framework.extension.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.harmazing.framework.extension.interfaces.INode;

public class Node implements INode {
	private String nodeName;
	private String nodeText;
	private List<INode> children = new ArrayList<INode>();
	private Map<String, String> attributes = new HashMap<String, String>();

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getNodeText() {
		return nodeText;
	}

	public void setNodeText(String nodeText) {
		this.nodeText = nodeText;
	}

	public List<INode> getChildren() {
		return children;
	}

	public void setChildren(List<INode> children) {
		this.children = children;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
