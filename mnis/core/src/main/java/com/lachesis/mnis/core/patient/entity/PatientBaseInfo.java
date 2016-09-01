package com.lachesis.mnis.core.patient.entity;

import java.util.Date;


/**
 * 患者基本信息
 * @author ThinkPad
 *
 */
public class PatientBaseInfo {
	/*********** 核心数据 **************/
	/** 住院流水号 */
	private String patId;
	/** 姓名 */
	private String name;
	/** 住院号 */
	private String inHospNo;
	/**患者编号*/
	private String inpNo;
	/** 床号 */
	private String bedCode;
	/** 病区代码 */
	private String deptCode;
	/**
	 * 出院状态
	 */
	private boolean status;
	/** 入院诊断 */
	private String age;
	private String sex;
	private String patBarcode;
	private Date inDate;
	private Date outDate;
	/*********** 核心数据 **************/
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInHospNo() {
		return inHospNo;
	}
	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}
	public String getInpNo() {
		return inpNo;
	}
	public void setInpNo(String inpNo) {
		this.inpNo = inpNo;
	}
	public String getBedCode() {
		return bedCode;
	}
	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPatBarcode() {
		return patBarcode;
	}
	public void setPatBarcode(String patBarcode) {
		this.patBarcode = patBarcode;
	}
	public Date getInDate() {
		return inDate;
	}
	public void setInDate(Date inDate) {
		this.inDate = inDate;
	}
	public Date getOutDate() {
		return outDate;
	}
	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
}
