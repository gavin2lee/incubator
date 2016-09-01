package com.anyi.report.entity;

import java.util.List;

public class DcoTreeTemplate {
	private String template_id;  //模板编号
	private String template_name;  //模板名称
	private String report_type;//模板分类
	private List<DocTreeRecord> record_list;//护理记录集合
	
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getTemplate_name() {
		return template_name;
	}
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}
	
	public List<DocTreeRecord> getRecord_list() {
		return record_list;
	}
	public void setRecord_list(List<DocTreeRecord> record_list) {
		this.record_list = record_list;
	}
	public String getReport_type() {
		return report_type;
	}
	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}
	
	
	
}
