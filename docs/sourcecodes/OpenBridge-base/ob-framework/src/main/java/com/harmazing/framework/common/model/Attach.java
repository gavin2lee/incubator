package com.harmazing.framework.common.model;

import java.util.HashMap;

@SuppressWarnings("serial")
public class Attach extends HashMap<String, Object> {
	public Attach() {

	}

	public Attach(Object obj) {
		this.put("__root", obj);
	}

	public Object getRoot() {
		return get("__root");
	}
}
