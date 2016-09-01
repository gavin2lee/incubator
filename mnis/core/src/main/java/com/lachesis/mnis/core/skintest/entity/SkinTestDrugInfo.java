package com.lachesis.mnis.core.skintest.entity;

import java.io.Serializable;
import java.util.Date;

public class SkinTestDrugInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String drugName; // 皮试药物名字
	private String drugCode; // 皮试药物编号

	private String testNurseId; // 皮试医生号
	private String testNurseName; // 皮试医生姓名
	private Date testDate; // 医嘱开立时间

	private String skinTestId; //皮试医嘱id
	private String skinTestDrugInfoId;//皮试药物信息Id
	
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getDrugCode() {
		return drugCode;
	}
	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}
	public String getTestNurseId() {
		return testNurseId;
	}
	public void setTestNurseId(String testNurseId) {
		this.testNurseId = testNurseId;
	}
	public String getTestNurseName() {
		return testNurseName;
	}
	public void setTestNurseName(String testNurseName) {
		this.testNurseName = testNurseName;
	}
	
	public Date getTestDate() {
		return testDate;
	}
	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}
	public String getSkinTestId() {
		return skinTestId;
	}
	public void setSkinTestId(String skinTestId) {
		this.skinTestId = skinTestId;
	}
	public String getSkinTestDrugInfoId() {
		return skinTestDrugInfoId;
	}
	public void setSkinTestDrugInfoId(String skinTestDrugInfoId) {
		this.skinTestDrugInfoId = skinTestDrugInfoId;
	}
	
	
}
