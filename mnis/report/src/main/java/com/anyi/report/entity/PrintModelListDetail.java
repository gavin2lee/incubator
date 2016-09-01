package com.anyi.report.entity;

import java.util.List;

public class PrintModelListDetail {

	private String data_key;//控件id
	private String data_value;//控件值
	private String data_type;//控件类型
	private String metadata_name;//下拉列表中的名字
	private String item_type;//控件类型
	private String ord;//排序,打印时如果是单选或多选,则显示顺序号
	
	private List<PrintModelListDetailOrd> list_ord;
	
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
	public List<PrintModelListDetailOrd> getList_ord() {
		return list_ord;
	}
	public void setList_ord(List<PrintModelListDetailOrd> list_ord) {
		this.list_ord = list_ord;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getMetadata_name() {
		return metadata_name;
	}
	public void setMetadata_name(String metadata_name) {
		this.metadata_name = metadata_name;
	}
	public String getItem_type() {
		return item_type;
	}
	public void setItem_type(String item_type) {
		this.item_type = item_type;
	}
	public String getOrd() {
		return ord;
	}
	public void setOrd(String ord) {
		this.ord = ord;
	}

}
