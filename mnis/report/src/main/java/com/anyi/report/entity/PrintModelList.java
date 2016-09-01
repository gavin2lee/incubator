package com.anyi.report.entity;

import java.util.List;

public class PrintModelList {
	private String record_id;
	private List<PrintModelListDetail> list_detail;//对于有复选框的情况需要显示所有的值
	
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public List<PrintModelListDetail> getList_detail() {
		return list_detail;
	}
	public void setList_detail(List<PrintModelListDetail> list_detail) {
		this.list_detail = list_detail;
	}
	
}
