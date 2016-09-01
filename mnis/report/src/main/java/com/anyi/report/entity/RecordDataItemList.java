package com.anyi.report.entity;

import java.util.List;

public class RecordDataItemList {

	private List<RecordDataItem> list_item;//针对前台分页打印，需要把每个模板的基本信息展示出来，用于比较诊断是否变更
	private String template_id;//模板ID
	private String inpatient_no;//病人ID
	private String record_id;//记录的流水号
	private String isHeader;//是否表头信息
	private String approve_status;//--审核状态
	public List<RecordDataItem> getList_item() {
		return list_item;
	}
	public void setList_item(List<RecordDataItem> list_item) {
		this.list_item = list_item;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getInpatient_no() {
		return inpatient_no;
	}
	public void setInpatient_no(String inpatient_no) {
		this.inpatient_no = inpatient_no;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getApprove_status() {
		return approve_status;
	}
	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}

	public String getIsHeader() {
		return isHeader;
	}

	public void setIsHeader(String isHeader) {
		this.isHeader = isHeader;
	}
}
