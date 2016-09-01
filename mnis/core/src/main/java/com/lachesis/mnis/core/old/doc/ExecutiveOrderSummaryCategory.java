package com.lachesis.mnis.core.old.doc;

import java.util.ArrayList;
import java.util.List;



public class ExecutiveOrderSummaryCategory {

	private String categroyName;
	private List<ExecutiveOrderSummaryAndItem> list;

	public ExecutiveOrderSummaryCategory(){
		list = new ArrayList<ExecutiveOrderSummaryAndItem>();
	}	
	public ExecutiveOrderSummaryCategory(ExecutiveOrderSummaryAndItem summaryItem){
		this.categroyName = summaryItem.getCategoryName();
		list = new ArrayList<ExecutiveOrderSummaryAndItem>();
		addSummaryItem(summaryItem);
	}	
	
	public String getCategroyName() {
		return categroyName;
	}

	public void setCategroyName(String categroyName) {
		this.categroyName = categroyName;
	}

	public List<ExecutiveOrderSummaryAndItem> getList() {
		return list;
	}

	public void setList(List<ExecutiveOrderSummaryAndItem> list) {
		this.list = list;
	}
	
	public void addSummaryItem(ExecutiveOrderSummaryAndItem summaryItem){
		list.add(summaryItem);
	}
}
