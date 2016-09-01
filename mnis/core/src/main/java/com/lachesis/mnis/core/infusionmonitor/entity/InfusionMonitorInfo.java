package com.lachesis.mnis.core.infusionmonitor.entity;

import java.util.List;

import com.lachesis.mnis.core.order.entity.OrderItem;

public class InfusionMonitorInfo {

	private String orderExecId;
	private String orderContent;
	private String deliverFreq;
	private String usageName;
	private List<OrderItem> items;
	private String patientId;
	private String deptId;
	private String bedNo;
	private String patientName;
	private String status;
	private String speedUnit;
	private List<InfusionMonitorRecord> records;
	private InfusionMonitorRecord currentRecord;

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

	public String getOrderExecId() {
		return orderExecId;
	}

	public void setOrderExecId(String orderExecId) {
		this.orderExecId = orderExecId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public List<InfusionMonitorRecord> getRecords() {
		return records;
	}

	public void setRecords(List<InfusionMonitorRecord> records) {
		this.records = records;
	}

	public InfusionMonitorRecord getCurrentRecord() {
		return currentRecord;
	}

	public void setCurrentRecord(InfusionMonitorRecord currentRecord) {
		this.currentRecord = currentRecord;
	}

	public String getOrderContent() {
		return orderContent;
	}

	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public String getDeliverFreq() {
		return deliverFreq;
	}

	public void setDeliverFreq(String deliverFreq) {
		this.deliverFreq = deliverFreq;
	}

	public String getUsageName() {
		return usageName;
	}

	public void setUsageName(String usageName) {
		this.usageName = usageName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSpeedUnit() {
		return speedUnit;
	}

	public void setSpeedUnit(String speedUnit) {
		this.speedUnit = speedUnit;
	}

}
