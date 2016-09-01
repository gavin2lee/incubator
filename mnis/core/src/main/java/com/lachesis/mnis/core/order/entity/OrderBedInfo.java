package com.lachesis.mnis.core.order.entity;

public class OrderBedInfo {
	/**
	 * 病人床号
	 * */
	private String bedCode;
	/**
	 * 病人Id
	 */
	private String patId;
	/**
	 * 医嘱执行类型(N:新医嘱,S：停止医嘱,C：待执行医嘱，E：已执行医嘱)
	 * 新医嘱
	 */
	private int newOrderCount;
	/**
	 * 已执行医嘱
	 */
	private int execedOrderCount;
	/**
	 * 待执行医嘱
	 */
	private int unexecedOrderCount;
	/**
	 * 停止医嘱
	 */
	private int stopOrderCount;
	/**
	 * 未停止医嘱
	 */
	private int unstopOrderCount;
	/**
	 * 医嘱类型("CZ"：长嘱,"LZ":临嘱)
	 */
	private String orderType;
	/**
	 * 是否紧急医嘱0：否,1：是
	 */
	private int isEmergent;
	
	private String nurseLevel;
	
	public String getBedCode() {
		return bedCode;
	}
	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	
	public int getNewOrderCount() {
		return newOrderCount;
	}
	public void setNewOrderCount(int newOrderCount) {
		this.newOrderCount = newOrderCount;
	}
	public int getExecedOrderCount() {
		return execedOrderCount;
	}
	public void setExecedOrderCount(int execedOrderCount) {
		this.execedOrderCount = execedOrderCount;
	}
	public int getUnexecedOrderCount() {
		return unexecedOrderCount;
	}
	public void setUnexecedOrderCount(int unexecedOrderCount) {
		this.unexecedOrderCount = unexecedOrderCount;
	}
	public int getStopOrderCount() {
		return stopOrderCount;
	}
	public void setStopOrderCount(int stopOrderCount) {
		this.stopOrderCount = stopOrderCount;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public int getIsEmergent() {
		return isEmergent;
	}
	public void setIsEmergent(int isEmergent) {
		this.isEmergent = isEmergent;
	}
	public int getUnstopOrderCount() {
		return unstopOrderCount;
	}
	public void setUnstopOrderCount(int unstopOrderCount) {
		this.unstopOrderCount = unstopOrderCount;
	}
	public String getNurseLevel() {
		return nurseLevel;
	}
	public void setNurseLevel(String nurseLevel) {
		this.nurseLevel = nurseLevel;
	}
}
