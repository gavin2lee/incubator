package com.anyi.report.entity;

import java.util.List;

public class DocReportCountInOut {

	private String id;//编号
	private String name;//名称
	private String value;//数据
	private List<DocReportCountInOut> out_list;//出量分类
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<DocReportCountInOut> getOut_list() {
		return out_list;
	}
	public void setOut_list(List<DocReportCountInOut> out_list) {
		this.out_list = out_list;
	}
	
	
}
