package com.anyi.report.entity;

import java.util.List;

public class PrintModelItem {
	
	private String item_id;//控件ID
	private String option_name;//下拉菜单名字
	private String value;//客户填写的数据
	private String record_id;
	private List<PrintModelCheckBox> checkbox_item;//对于有复选框的情况需要显示所有的值
	private String data_type;//数据类型
	public String getOption_name() {
		return option_name;
	}
	public void setOption_name(String option_name) {
		this.option_name = option_name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public List<PrintModelCheckBox> getCheckbox_item() {
		return checkbox_item;
	}
	public void setCheckbox_item(List<PrintModelCheckBox> checkbox_item) {
		this.checkbox_item = checkbox_item;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	

}
