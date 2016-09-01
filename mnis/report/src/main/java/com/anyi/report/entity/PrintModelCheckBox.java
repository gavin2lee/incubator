package com.anyi.report.entity;

import java.util.List;

public class PrintModelCheckBox {
	
	private String checkbox_name;//复选框显示内容
	private String checkbox_code;//复选框代码
	private String checkbox_value;//选中的值
	private String checkbox_id;
	private List <PrintModelCheckBoxSon> checkbox_son;
	private List <PrintModelCheckBoxText> checkbox_text;
	public String getCheckbox_name() {
		return checkbox_name;
	}
	public void setCheckbox_name(String checkbox_name) {
		this.checkbox_name = checkbox_name;
	}
	public String getCheckbox_code() {
		return checkbox_code;
	}
	public void setCheckbox_code(String checkbox_code) {
		this.checkbox_code = checkbox_code;
	}
	public List<PrintModelCheckBoxSon> getCheckbox_son() {
		return checkbox_son;
	}
	public void setCheckbox_son(List<PrintModelCheckBoxSon> checkbox_son) {
		this.checkbox_son = checkbox_son;
	}
	public List<PrintModelCheckBoxText> getCheckbox_text() {
		return checkbox_text;
	}
	public void setCheckbox_text(List<PrintModelCheckBoxText> checkbox_text) {
		this.checkbox_text = checkbox_text;
	}
	public String getCheckbox_id() {
		return checkbox_id;
	}
	public void setCheckbox_id(String checkbox_id) {
		this.checkbox_id = checkbox_id;
	}
	public String getCheckbox_value() {
		return checkbox_value;
	}
	public void setCheckbox_value(String checkbox_value) {
		this.checkbox_value = checkbox_value;
	}
		
}
