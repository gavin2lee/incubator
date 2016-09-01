package com.lachesis.mnis.core.labtest.entity;

import java.util.List;

/**
 * 送检记录信息
 * @author ThinkPad
 *
 */
public class LabTestSendRecord {
	private String deptCode;
	private String patId;
	private String patName;
	private String inHospNo;
	private String age;
	private String bedCode;
	private String sex;
	
	
	private List<LabTestSendRecordDetail> labTestSendRecordDetails;

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getPatName() {
		return patName;
	}

	public void setPatName(String patName) {
		this.patName = patName;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public List<LabTestSendRecordDetail> getLabTestSendRecordDetails() {
		return labTestSendRecordDetails;
	}

	public void setLabTestSendRecordDatails(
			List<LabTestSendRecordDetail> labTestSendRecordDetails) {
		this.labTestSendRecordDetails = labTestSendRecordDetails;
	}
}
