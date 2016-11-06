package com.harmazing.framework.extension.interfaces;

import java.util.List;
import java.util.Map;

public interface INode {
	String getNodeName();

	String getNodeText();

	List<INode> getChildren();

	Map<String, String> getAttributes();
}
