package com.lachesis.mnis.core.old.doc;

import java.util.ArrayList;
import java.util.List;

public class ExecutiveOrderSummaryAndItem {
	private String summaryId;
	private String name;
	private String categoryName;
	private String emptyShowFlag;
	private int ord;
	private String itemCode;
	private String itemName;
	private String freqCode;
	List<String> bedCodeList;
	
	public ExecutiveOrderSummaryAndItem(){
		bedCodeList = new ArrayList<String>();//new SortedArrayList();		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getEmptyShowFlag() {
		return emptyShowFlag;
	}
	public void setEmptyShowFlag(String emptyShowFlag) {
		this.emptyShowFlag = emptyShowFlag;
	}
	public int getOrd() {
		return ord;
	}
	public void setOrd(int ord) {
		this.ord = ord;
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
	public String getFreqCode() {
		return freqCode;
	}
	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
	}
	public List<String> getBedCodeList() {
		return bedCodeList;
	}
	public void setBedCodeList(List<String> bedCodeList) {
		this.bedCodeList = bedCodeList;
	}

	public String getSummaryId() {
		return summaryId;
	}

	public void setSummaryId(String summaryId) {
		this.summaryId = summaryId;
	}
	
	public void addBedCode(String bedCode){
		bedCodeList.add(bedCode);
	}
	
}
