package com.anyi.report.entity;

import java.util.List;

public class DocApprove {
	
	private String approve_status;//--审签状态
	private String approve_person;//--审签人
	private String approve_time;//--审签时间
	private String now_time;//当前时间
	private List <DocApproveRecord> list;//记录ID
	
	public String getApprove_status() {
		return approve_status;
	}
	public void setApprove_status(String approve_status) {
		this.approve_status = approve_status;
	}
	public String getApprove_person() {
		return approve_person;
	}
	public void setApprove_person(String approve_person) {
		this.approve_person = approve_person;
	}
	public String getApprove_time() {
		return approve_time;
	}
	public void setApprove_time(String approve_time) {
		this.approve_time = approve_time;
	}
	public List<DocApproveRecord> getList() {
		return list;
	}
	public void setList(List<DocApproveRecord> list) {
		this.list = list;
	}
	public String getNow_time() {
		return now_time;
	}
	public void setNow_time(String now_time) {
		this.now_time = now_time;
	}
	
}
