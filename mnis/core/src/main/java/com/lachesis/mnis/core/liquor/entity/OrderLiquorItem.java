package com.lachesis.mnis.core.liquor.entity;

import java.util.Date;

/**
 * 配液医嘱执行信息
 * 
 * @author lei.lei
 *
 */
public class OrderLiquorItem {

	private String execOrderId; // 执行医嘱ID
	private String orderId; // 医嘱ID
	private boolean isStop;//是否停止医嘱
	private String prepareNurseId; // 备药护士ID
	private String prepareNurseName; // 备药护士姓名
	private Date prepareTime;// 备药时间

	private String execNurseId; // 配液护士ID
	private String execNurseName; // 配液护士姓名
	private Date execTime;// 配液时间
	
	private String verifyNurseId; // 审核护士ID
	private String verifyNurseName; // 审核护士姓名
	private Date verifyTime;// 审核时间

	private String state; // 当前状态 P：备药	V:审核 F：配液
	
	private String liquor;//是否需要配液

	public String getExecOrderId() {
		return execOrderId;
	}

	public void setExecOrderId(String execOrderId) {
		this.execOrderId = execOrderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getPrepareNurseId() {
		return prepareNurseId;
	}

	public void setPrepareNurseId(String prepareNurseId) {
		this.prepareNurseId = prepareNurseId;
	}

	public String getPrepareNurseName() {
		return prepareNurseName;
	}

	public void setPrepareNurseName(String prepareNurseName) {
		this.prepareNurseName = prepareNurseName;
	}

	public Date getPrepareTime() {
		return prepareTime;
	}

	public void setPrepareTime(Date prepareTime) {
		this.prepareTime = prepareTime;
	}

	public String getExecNurseId() {
		return execNurseId;
	}

	public void setExecNurseId(String execNurseId) {
		this.execNurseId = execNurseId;
	}

	public String getExecNurseName() {
		return execNurseName;
	}

	public void setExecNurseName(String execNurseName) {
		this.execNurseName = execNurseName;
	}

	public Date getExecTime() {
		return execTime;
	}

	public void setExecTime(Date execTime) {
		this.execTime = execTime;
	}
	
	public String getVerifyNurseId() {
		return verifyNurseId;
	}

	public void setVerifyNurseId(String verifyNurseId) {
		this.verifyNurseId = verifyNurseId;
	}

	public String getVerifyNurseName() {
		return verifyNurseName;
	}

	public void setVerifyNurseName(String verifyNurseName) {
		this.verifyNurseName = verifyNurseName;
	}

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isStop() {
		return isStop;
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}

	public String getLiquor() {
		return liquor;
	}

	public void setLiquor(String liquor) {
		this.liquor = liquor;
	}

	@Override
	public String toString() {
		return "OrderLiquorItem [execOrderId=" + execOrderId + ", orderId="
				+ orderId + ", prepareNurseId=" + prepareNurseId
				+ ", prepareNurseName=" + prepareNurseName + ", prepareTime="
				+ prepareTime + ", execNurseId=" + execNurseId
				+ ", execNurseName=" + execNurseName + ", execTime=" + execTime
				+ ", verifyNurseId=" + verifyNurseId + ", verifyNurseName="
				+ verifyNurseName + ", verifyTime=" + verifyTime + ", state="
				+ state + "]";
	}

}
