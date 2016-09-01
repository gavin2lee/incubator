package com.anyi.report.entity;

import java.util.List;

//后台打印PDF主表
public class PrintModelMain {

	private String template_id;
	private String inpatient_no;
	private String template_file_name;
	private List <PrintModelList> data_list;
	private List <PrintModelItem> data_item;
	

	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public List<PrintModelList> getData_list() {
		return data_list;
	}
	public void setData_list(List<PrintModelList> data_list) {
		this.data_list = data_list;
	}
	public List<PrintModelItem> getData_item() {
		return data_item;
	}
	public void setData_item(List<PrintModelItem> data_item) {
		this.data_item = data_item;
	}
	public String getTemplate_file_name() {
		return template_file_name;
	}
	public void setTemplate_file_name(String template_file_name) {
		this.template_file_name = template_file_name;
	}
	public String getInpatient_no() {
		return inpatient_no;
	}
	public void setInpatient_no(String inpatient_no) {
		this.inpatient_no = inpatient_no;
	}
	
}
