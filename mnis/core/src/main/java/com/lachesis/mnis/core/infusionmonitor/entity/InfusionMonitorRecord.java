package com.lachesis.mnis.core.infusionmonitor.entity;

import java.util.Date;

public class InfusionMonitorRecord {
	private String orderExecId;
	private String recordNurseId;
	private Date recordDate;
	private int deliverSpeed;
	private boolean abnormal;
	private String anomalyMsg;
	private String anomalyDisposal;
	private String recordNurseName;
	private int residue;
	private Integer id;
	private String status;
	private String speedUnit;

	public String getRecordNurseId() {
		return recordNurseId;
	}

	public void setRecordNurseId(String recordNurseId) {
		this.recordNurseId = recordNurseId;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public int getDeliverSpeed() {
		return deliverSpeed;
	}

	public void setDeliverSpeed(int deliverSpeed) {
		this.deliverSpeed = deliverSpeed;
	}

	public boolean isAbnormal() {
		return abnormal;
	}

	public void setAbnormal(boolean abnormal) {
		this.abnormal = abnormal;
	}

	public String getAnomalyMsg() {
		return anomalyMsg;
	}

	public void setAnomalyMsg(String anomalyMsg) {
		this.anomalyMsg = anomalyMsg;
	}

	public String getAnomalyDisposal() {
		return anomalyDisposal;
	}

	public void setAnomalyDisposal(String anomalyDisposal) {
		this.anomalyDisposal = anomalyDisposal;
	}

	public String getRecordNurseName() {
		return recordNurseName;
	}

	public void setRecordNurseName(String recordNurseName) {
		this.recordNurseName = recordNurseName;
	}

	public int getResidue() {
		return residue;
	}

	public void setResidue(int residue) {
		this.residue = residue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOrderExecId() {
		return orderExecId;
	}

	public void setOrderExecId(String orderExecId) {
		this.orderExecId = orderExecId;
	}

	public String getSpeedUnit() {
		return speedUnit;
	}

	public void setSpeedUnit(String speedUnit) {
		this.speedUnit = speedUnit;
	}

}
