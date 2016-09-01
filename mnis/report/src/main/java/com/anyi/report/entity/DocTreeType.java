package com.anyi.report.entity;

import java.util.List;

public class DocTreeType {
	
	private String type_id;   //树结构第一层编码
	private String type_name;  //树结构第一层显示名称
	private String show_type;//展示方式（fixed固定页数  list页数不固定）
	private List<DcoTreeTemplate> template_list;//模板集合
	
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public List<DcoTreeTemplate> getTemplate_list() {
		return template_list;
	}
	public void setTemplate_list(List<DcoTreeTemplate> template_list) {
		this.template_list = template_list;
	}
	public String getShow_type() {
		return show_type;
	}
	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}

}
