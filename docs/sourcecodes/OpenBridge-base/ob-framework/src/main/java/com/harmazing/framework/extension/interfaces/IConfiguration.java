package com.harmazing.framework.extension.interfaces;

import java.io.Serializable;

public interface IConfiguration extends Serializable {
	INode getNode();

	String getId();
}
