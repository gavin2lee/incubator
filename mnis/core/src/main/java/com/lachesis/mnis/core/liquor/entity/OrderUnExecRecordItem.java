package com.lachesis.mnis.core.liquor.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 未执行医嘱具体项目信息
 * @author ThinkPad
 *
 */

public class OrderUnExecRecordItem {
	private int id;
	private String patId;
	private String barcode;
	private String deptCode;
	private String recordNurseCode;
	private String recordNurseName;
	private Date recordDate;
	/**
	 * 药物信息字符串：药物间"-;"隔开,药物与剂量"-:"隔开
	 */
	private String drugInfos;
	private List<DrugItem> drugItems = new ArrayList<DrugItem>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getRecordNurseCode() {
		return recordNurseCode;
	}
	public void setRecordNurseCode(String recordNurseCode) {
		this.recordNurseCode = recordNurseCode;
	}
	public String getRecordNurseName() {
		return recordNurseName;
	}
	public void setRecordNurseName(String recordNurseName) {
		this.recordNurseName = recordNurseName;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public List<DrugItem> getDrugItems() {
		return drugItems;
	}
	public void setDrugItems(List<DrugItem> drugItems) {
		this.drugItems = drugItems;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getDrugInfos() {
		return drugInfos;
	}
	public void setDrugInfos(String drugInfos) {
		this.drugInfos = drugInfos;
	}
	
}
