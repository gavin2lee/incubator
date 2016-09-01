package com.anyi.report.entity;

/**
 * 文书出入量统计到体温单
 * @author ThinkPad
 *
 */
public class DocDataReportSync {
	private String patId;
	/*
	 * 统计出入量值
	 */
	private String value;
	/**
	 * 统计出入量类型(inTake,outTake)
	 */
	private String valueType;
	private String deptCode;
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
}
