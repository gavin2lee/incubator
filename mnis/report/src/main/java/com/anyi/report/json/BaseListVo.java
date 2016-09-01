package com.anyi.report.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseListVo extends BaseVo{
	@JsonProperty("data")
	private List<? extends Object> data;

	public List<? extends Object> getData() {
		return data;
	}

	public void setData(List<? extends Object> data) {
		this.data = data;
	}
	
}
