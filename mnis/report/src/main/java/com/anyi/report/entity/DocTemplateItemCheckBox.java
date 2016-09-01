package com.anyi.report.entity;

import java.util.List;

public class DocTemplateItemCheckBox {

	private String checkbox_name;
	private String checkbox_code;
	private String checkbox_ord;//排序
	private String data_type;//数据类型
	private String show_type;//展示类型ROMAN:罗马 ENGLISH:英文 ARAB：阿拉伯数字 RING：带圆圈的阿拉伯数字	
	private List<DocTemplateItemCheckBox> checkbox_son; 
	private List<DocTemplateItemCheckBoxText> checkbox_text;
		
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
	public List<DocTemplateItemCheckBox> getCheckbox_son() {
		return checkbox_son;
	}
	public void setCheckbox_son(List<DocTemplateItemCheckBox> checkbox_son) {
		this.checkbox_son = checkbox_son;
	}
	public List<DocTemplateItemCheckBoxText> getCheckbox_text() {
		return checkbox_text;
	}
	public void setCheckbox_text(List<DocTemplateItemCheckBoxText> checkbox_text) {
		this.checkbox_text = checkbox_text;
	}
	public String getCheckbox_ord() {
		return checkbox_ord;
	}
	public void setCheckbox_ord(String checkbox_ord) {
		this.checkbox_ord = checkbox_ord;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getShow_type() {
		return show_type;
	}
	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}

}
