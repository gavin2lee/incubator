package com.lachesis.mnis.core.nursing;

import java.util.List;

import com.lachesis.mnis.core.bodysign.entity.BodySignDict;

public class NurseRecordSpecItem {
    private String itemCode; // required
    private String itemName; // required
    private String itemValue; // required
    private String itemValueCode; // required
    private String detailRecordId; // required
    private List<BodySignDict> subItemList; // required
    
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
	public String getItemValueCode() {
		return itemValueCode;
	}
	public void setItemValueCode(String itemValueCode) {
		this.itemValueCode = itemValueCode;
	}
	public String getDetailRecordId() {
		return detailRecordId;
	}
	public void setDetailRecordId(String detailRecordId) {
		this.detailRecordId = detailRecordId;
	}
	public List<BodySignDict> getSubItemList() {
		return subItemList;
	}
	public void setSubItemList(List<BodySignDict> subItemList) {
		this.subItemList = subItemList;
	}
}
