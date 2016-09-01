package com.lachesis.mnis.core.liquor.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 未执行医嘱记录信息
 * @author ThinkPad
 *
 */
public class OrderUnExecRecord {
	private String patId;
	private String patName;
	private String bedCode;
	private String deptCode;
	private List<OrderUnExecRecordItem> orderUnExecRecordItems = new ArrayList<OrderUnExecRecordItem>();
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
	public String getBedCode() {
		return bedCode;
	}
	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public List<OrderUnExecRecordItem> getOrderUnExecRecordItems() {
		return orderUnExecRecordItems;
	}
	public void setOrderUnExecRecordItems(
			List<OrderUnExecRecordItem> orderUnExecRecordItems) {
		this.orderUnExecRecordItems = orderUnExecRecordItems;
	}
}
