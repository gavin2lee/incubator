package com.harmazing.openbridge.mod.operations.elasticsearch.bean.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FilterInfoVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyword;
	
	private String operator;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
	
}
