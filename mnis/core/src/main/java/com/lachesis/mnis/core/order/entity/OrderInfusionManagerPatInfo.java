package com.lachesis.mnis.core.order.entity;

/**
 * 闭环输液患者信息
 * @author ThinkPad
 *
 */
public class OrderInfusionManagerPatInfo {
	private String patId;
	private String patInhospNo;
	private String patSex;
	private String patAge;
	private String patName;
	private String patBarcode;
	private String deptCode;
	private String patBedcode;
	private String dutyNurseCode;
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	
	public String getPatInhospNo() {
		return patInhospNo;
	}
	public void setPatInhospNo(String patInhospNo) {
		this.patInhospNo = patInhospNo;
	}
	public String getPatSex() {
		return patSex;
	}
	public void setPatSex(String patSex) {
		this.patSex = patSex;
	}
	public String getPatAge() {
		return patAge;
	}
	public void setPatAge(String patAge) {
		this.patAge = patAge;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getPatBarcode() {
		return patBarcode;
	}
	public void setPatBarcode(String patBarcode) {
		this.patBarcode = patBarcode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getPatBedcode() {
		return patBedcode;
	}
	public void setPatBedcode(String patBedcode) {
		this.patBedcode = patBedcode;
	}
	public String getDutyNurseCode() {
		return dutyNurseCode;
	}
	public void setDutyNurseCode(String dutyNurseCode) {
		this.dutyNurseCode = dutyNurseCode;
	}
}
