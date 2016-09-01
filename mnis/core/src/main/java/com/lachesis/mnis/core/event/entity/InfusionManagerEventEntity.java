package com.lachesis.mnis.core.event.entity;

public class InfusionManagerEventEntity { 
	private String patId;
	private String inHospNo;
	private String bedCode;
	private String deptCode;
	private String orderGroupNo;
	private String infusionStatus;
	private double actTotalCapacity;
	private int orderCount;
	private int orderExecCount;

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
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

	public String getOrderGroupNo() {
		return orderGroupNo;
	}

	public void setOrderGroupNo(String orderGroupNo) {
		this.orderGroupNo = orderGroupNo;
	}

	public String getInfusionStatus() {
		return infusionStatus;
	}

	public void setInfusionStatus(String infusionStatus) {
		this.infusionStatus = infusionStatus;
	}

	public double getActTotalCapacity() {
		return actTotalCapacity;
	}

	public void setActTotalCapacity(double actTotalCapacity) {
		this.actTotalCapacity = actTotalCapacity;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public int getOrderExecCount() {
		return orderExecCount;
	}

	public void setOrderExecCount(int orderExecCount) {
		this.orderExecCount = orderExecCount;
	}

}
