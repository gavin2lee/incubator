package com.anyi.report.entity;

import java.util.List;

public class RecordDataList {
	private String record_id;
	private String isHeader;
	private String data_index;
	private String record_item_id;
	private List <RecordDataListDetail> list_detail;
	private String approve_status;//审核状态
	private String create_person;//创建人
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getData_index() {
		return data_index;
	}
	public void setData_index(String data_index) {
		this.data_index = data_index;
	}
	public String getRecord_item_id() {
		return record_item_id;
	}
	public void setRecord_item_id(String record_item_id) {
		this.record_item_id = record_item_id;
	}
	public List<RecordDataListDetail> getList_detail() {
		return list_detail;
	}
	public void setList_detail(List<RecordDataListDetail> list_detail) {
		this.list_detail = list_detail;
	}
	public String getApprove_status() {
		return approve_status;
	}
	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}
	public String getCreate_person() {
		return create_person;
	}
	public void setCreate_person(String create_person) {
		this.create_person = create_person;
	}

	public String getIsHeader() {
		return isHeader;
	}

	public void setIsHeader(String isHeader) {
		this.isHeader = isHeader;
	}
}
