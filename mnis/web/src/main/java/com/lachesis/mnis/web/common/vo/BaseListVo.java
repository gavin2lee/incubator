package com.lachesis.mnis.web.common.vo;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class BaseListVo extends BaseVo{
	private static final long serialVersionUID = 1L;
	
	@SerializedName("lst")
	private List<? extends Object> list;

	public List<? extends Object> getList() {
		return list;
	}

	public void setList(List<? extends Object> list) {
		this.list = list;
	}
	
}
