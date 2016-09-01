package com.lachesis.mnis.web.common.vo;

import java.util.HashMap;
import java.util.Map;

public class MapPo {
	private Map<String,Object> data;
	
	public MapPo(){
		data = new HashMap<String,Object>();
	}

	public Map<String,Object> getData() {
		return data;
	}

	public void setData(Map<String,Object> data) {
		this.data = data;
	}
	
}
