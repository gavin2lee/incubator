package com.lachesis.mnis.web.common.vo;

import com.google.gson.annotations.SerializedName;


public class BaseDataVo extends BaseVo {
	private static final long serialVersionUID = -8448782546547543833L;

	@SerializedName("data")
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
