package com.anyi.report.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseDataVo extends BaseVo {
	private static final long serialVersionUID = -8448782546547543833L;

	@JsonProperty("data")
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
