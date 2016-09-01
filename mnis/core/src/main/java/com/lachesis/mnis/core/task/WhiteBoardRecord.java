package com.lachesis.mnis.core.task;

import java.io.Serializable;

public class WhiteBoardRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	private String deptId;
	private String itemCode;
	private String itemName;
	private String itemValue;
	private String showDate;
	
	public WhiteBoardRecord(){
		
	}
	
	public WhiteBoardRecord(String deptId, String itemCode, String itemName, String itemValue, String showDate){
		this.deptId = deptId;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.itemValue = itemValue;
		this.showDate = showDate;
	}
	
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemValue() {
		return itemValue;
	}
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	public String getShowDate() {
		return showDate;
	}
	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}


}
