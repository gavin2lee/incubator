package com.anyi.report.entity;

public class DocReportPrintDataDetail {
	
	private String template_item_id; //数据源编码
	private String record_value;//记录值
	private String metadata_simple_name;//数据源名字（用于展示下来菜单，复选框等数值）
	private String data_type;//控件类型，OPT,SEL,STR等（需要说明的是，对于用户自定义数据这个类型值是不准确的，后续用的时候要注意）
	private String raw_value;//DOC_REPORT_DATA_ITEM表中的原始数据
	private String metadata_code;//元数据

	public String getRaw_value() {
		return raw_value;
	}

	public void setRaw_value(String raw_value) {
		this.raw_value = raw_value;
	}

	public String getMetadata_code() {
		return metadata_code;
	}

	public void setMetadata_code(String metadata_code) {
		this.metadata_code = metadata_code;
	}

	public String getTemplate_item_id() {
		return template_item_id;
	}
	public void setTemplate_item_id(String template_item_id) {
		this.template_item_id = template_item_id;
	}
	public String getRecord_value() {
		return record_value;
	}
	public void setRecord_value(String record_value) {
		this.record_value = record_value;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public String getMetadata_simple_name() {
		return metadata_simple_name;
	}
	public void setMetadata_simple_name(String metadata_simple_name) {
		this.metadata_simple_name = metadata_simple_name;
	}
	
}
