package com.lachesis.mnis.core.skintest.entity;

import java.io.Serializable;
import java.util.List;

public class SkinTestInfoLx implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String stLxGroupId;
	private String patientId; // 病人Id
	private String patientName; // 病人姓名
	private String inHospNo; // 住院号
	private String bedNo; //窗口

	private List<SkinTestGroup> skinTestGroups;

	
	public String getStLxGroupId() {
		return stLxGroupId;
	}

	public void setStLxGroupId(String stLxGroupId) {
		this.stLxGroupId = stLxGroupId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public List<SkinTestGroup> getSkinTestGroups() {
		return skinTestGroups;
	}

	public void setSkinTestGroups(List<SkinTestGroup> skinTestGroups) {
		this.skinTestGroups = skinTestGroups;
	}
}
