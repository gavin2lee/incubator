package com.anyi.report.entity;

import java.util.List;

public class DocDataRecord {
	private String approve_person;//审核人
	private String approve_status;//审核状态
	private String approve_time;//审核时间
	private String inpatient_no;//病人ID
	private String create_time;//创建时间
	private String create_person;//创建时间
	private String modify_time;//修改时间
	private String printflag;//打印状态
	private String record_id;//记录ID
	private String template_id;//模板ID
	private String startDate;
	private String endDate;
	private String isHeader;//是否表头记录
	private String dateList;//记录日期
	private List<DocDataRecordItem> list;//数据集合
	
	public String getApprove_person() {
		return approve_person;
	}
	public void setApprove_person(String approve_person) {
		this.approve_person = approve_person;
	}
	public String getApprove_status() {
		return approve_status;
	}
	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}
	public String getApprove_time() {
		return approve_time;
	}
	public void setApprove_time(String approve_time) {
		this.approve_time = approve_time;
	}
	public String getInpatient_no() {
		return inpatient_no;
	}
	public void setInpatient_no(String inpatient_no) {
		this.inpatient_no = inpatient_no;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getModify_time() {
		return modify_time;
	}
	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}
	public String getPrintflag() {
		return printflag;
	}
	public void setPrintflag(String printflag) {
		this.printflag = printflag;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public List<DocDataRecordItem> getList() {
		return list;
	}
	public void setList(List<DocDataRecordItem> list) {
		this.list = list;
	}
	public String getCreate_person() {
		return create_person;
	}
	public void setCreate_person(String create_person) {
		this.create_person = create_person;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getIsHeader() {
		return isHeader;
	}

	public void setIsHeader(String isHeader) {
		this.isHeader = isHeader;
	}
	public String getDateList() {
		return dateList;
	}
	public void setDateList(String dateList) {
		this.dateList = dateList;
	}
}
