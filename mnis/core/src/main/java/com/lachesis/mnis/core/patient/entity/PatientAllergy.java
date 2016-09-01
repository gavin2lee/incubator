package com.lachesis.mnis.core.patient.entity;

import java.util.Date;

/**
 * 患者过敏信息
 *
 * @author yuliang.xu
 * @date 2015年6月12日 上午10:17:07
 */
public class PatientAllergy {

	private int allergyId;
	private Long skinTestId;
	private String drugCode;
	private String drugName;
	private String patientId;
	private Date allergyDate;


	public int getAllergyId() {
		return allergyId;
	}

	public void setAllergyId(int allergyId) {
		this.allergyId = allergyId;
	}

	public String getDrugCode() {
		return drugCode;
	}

	public void setDrugCode(String drugCode) {
		this.drugCode = drugCode;
	}

	public String getDrugName() {
		return drugName;
	}

	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public Date getAllergyDate() {
		return allergyDate;
	}

	public void setAllergyDate(Date allergyDate) {
		this.allergyDate = allergyDate;
	}

	public Long getSkinTestId() {
		return skinTestId;
	}

	public void setSkinTestId(Long skinTestId) {
		this.skinTestId = skinTestId;
	}
}