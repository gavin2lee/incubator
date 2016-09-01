package com.lachesis.mnis.core.skintest.entity;

import java.io.Serializable;

public class SkinTestGroup implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String stLxGroupId;
	
	private String orderExecId; // 医嘱id
	private String skinTestId; // 皮试id
	
	private String masterRecordId; //
	private String orderGroupNo; // 医嘱组号
	
	private SkinTestItem skinTestItem;
	private SkinTestDrugInfo skinTestDrugInfo;
	
	
	public String getStLxGroupId() {
		return stLxGroupId;
	}
	public void setStLxGroupId(String stLxGroupId) {
		this.stLxGroupId = stLxGroupId;
	}
	public String getOrderExecId() {
		return orderExecId;
	}
	public void setOrderExecId(String orderExecId) {
		this.orderExecId = orderExecId;
	}
	public String getSkinTestId() {
		return skinTestId;
	}
	public void setSkinTestId(String skinTestId) {
		this.skinTestId = skinTestId;
	}
	public String getMasterRecordId() {
		return masterRecordId;
	}
	public void setMasterRecordId(String masterRecordId) {
		this.masterRecordId = masterRecordId;
	}
	public String getOrderGroupNo() {
		return orderGroupNo;
	}
	public void setOrderGroupNo(String orderGroupNo) {
		this.orderGroupNo = orderGroupNo;
	}
	public SkinTestItem getSkinTestItem() {
		return skinTestItem;
	}
	public void setSkinTestItem(SkinTestItem skinTestItem) {
		this.skinTestItem = skinTestItem;
	}
	public SkinTestDrugInfo getSkinTestDrugInfo() {
		return skinTestDrugInfo;
	}
	public void setSkinTestDrugInfo(SkinTestDrugInfo skinTestDrugInfo) {
		this.skinTestDrugInfo = skinTestDrugInfo;
	}
	
}
