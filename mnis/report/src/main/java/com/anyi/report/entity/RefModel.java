package com.anyi.report.entity;

import java.util.List;

//需要反填的数据（如，专科评估需要反填到首次护理记录单中）
public class RefModel {

	private String inpatient_no;//病人ID
	private List<String> code_list;//数据源编号
	
	public String getInpatient_no() {
		return inpatient_no;
	}
	public void setInpatient_no(String inpatient_no) {
		this.inpatient_no = inpatient_no;
	}
	public List<String> getCode_list() {
		return code_list;
	}
	public void setCode_list(List<String> code_list) {
		this.code_list = code_list;
	}
	
}
