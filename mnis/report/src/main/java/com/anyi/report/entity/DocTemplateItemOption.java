package com.anyi.report.entity;

public class DocTemplateItemOption {
	private String code;
	private String value;
	private String option_ord;
	private String option_input_name;
	private String option_input_unit;
	private String children;//子节点
	private String  metadata_name_pinyin;//分数项说明（nda端对于评估记录时，下拉选择分数需要展示的描述）
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOption_ord() {
		return option_ord;
	}

	public void setOption_ord(String option_ord) {
		this.option_ord = option_ord;
	}

	public String getOption_input_name() {
		return option_input_name;
	}

	public void setOption_input_name(String option_input_name) {
		this.option_input_name = option_input_name;
	}

	public String getOption_input_unit() {
		return option_input_unit;
	}

	public void setOption_input_unit(String option_input_unit) {
		this.option_input_unit = option_input_unit;
	}

	public String getChildren() {
		return children;
	}

	public void setChildren(String children) {
		this.children = children;
	}

	public String getMetadata_name_pinyin() {
		return metadata_name_pinyin;
	}

	public void setMetadata_name_pinyin(String metadata_name_pinyin) {
		this.metadata_name_pinyin = metadata_name_pinyin;
	}


}
