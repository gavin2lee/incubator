package com.lachesis.mnis.core.nursing;

public class NurseItem {
    private String itemName; // required
    private String typeCode; // required
    private boolean chosen; // required
    private String detailRecordId; // required
    private String itemCode; // required
    
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public boolean isChosen() {
		return chosen;
	}
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}
	public String getDetailRecordId() {
		return detailRecordId;
	}
	public void setDetailRecordId(String detailRecordId) {
		this.detailRecordId = detailRecordId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
}
