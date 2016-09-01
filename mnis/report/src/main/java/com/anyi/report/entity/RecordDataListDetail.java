package com.anyi.report.entity;

import java.util.List;

public class RecordDataListDetail {
	private String data_key;
	private String data_value;
	private String view_value;//显示值
	private List<DynaMetadata> list_select;//动态列表中，填写数据时的选择项
	private String record_id;//为了传递给动态选项用做参数
	
	public String getData_key() {
		return data_key;
	}
	public void setData_key(String data_key) {
		this.data_key = data_key;
	}
	public String getData_value() {
		return data_value;
	}
	public void setData_value(String data_value) {
		this.data_value = data_value;
	}
	public List<DynaMetadata> getList_select() {
		return list_select;
	}
	public void setList_select(List<DynaMetadata> list_select) {
		this.list_select = list_select;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getView_value() {
		return view_value;
	}
	public void setView_value(String view_value) {
		this.view_value = view_value;
	}
}
