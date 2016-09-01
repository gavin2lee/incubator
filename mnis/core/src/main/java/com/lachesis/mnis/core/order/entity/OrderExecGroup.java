package com.lachesis.mnis.core.order.entity;

import java.util.Date;

/**
 * 医嘱执行组：一次医嘱执行的完整信息，医嘱信息{@link OrderGroup}+执行信息({@link OrderExecLog})
 * @author wenhuan.cui
 *
 */
public class OrderExecGroup {
	
	private OrderGroup orderGroup;
	private OrderExecLog orderExecLog;
	/**
	 * 针对输液医嘱(C,I,S,P,E:待执行,输液中,已停止,已暂停,已拔针)
	 */
	private String orderExecStatusCode; 
	private String orderExecStatusName; 
	private String orderExecBarcode; 
	
	private Date planExecTime;
	private String performSchedule;
	
	private int pendingOrderCount;
	private int finishedOrderCount;
	
	public String getPatientId() {
		return getOrderGroup().getPatientId();
	}
	public OrderGroup getOrderGroup() {
		return orderGroup;
	}
	public void setOrderGroup(OrderGroup orderGroup) {
		this.orderGroup = orderGroup;
	}
	public OrderExecLog getOrderExecLog() {
		return orderExecLog;
	}
	public void setOrderExecLog(OrderExecLog orderExecLog) {
		this.orderExecLog = orderExecLog;
	}
	public String getOrderExecStatusCode() {
		return orderExecStatusCode;
	}
	public void setOrderExecStatusCode(String orderExecStatusCode) {
		this.orderExecStatusCode = orderExecStatusCode;
	}
	public String getOrderExecStatusName() {
		return orderExecStatusName;
	}
	public void setOrderExecStatusName(String orderExecStatusName) {
		this.orderExecStatusName = orderExecStatusName;
	}
	public String getOrderExecBarcode() {
		return orderExecBarcode;
	}
	public void setOrderExecBarcode(String orderExecBarcode) {
		this.orderExecBarcode = orderExecBarcode;
	}
	public Date getPlanExecTime() {
		return planExecTime;
	}
	public void setPlanExecTime(Date planExecTime) {
		this.planExecTime = planExecTime;
	}
	public int getPendingOrderCount() {
		return pendingOrderCount;
	}
	public void setPendingOrderCount(int pendingOrderCount) {
		this.pendingOrderCount = pendingOrderCount;
	}
	public int getFinishedOrderCount() {
		return finishedOrderCount;
	}
	public void setFinishedOrderCount(int finishedOrderCount) {
		this.finishedOrderCount = finishedOrderCount;
	}
	public String getPerformSchedule() {
		return performSchedule;
	}
	public void setPerformSchedule(String performSchedule) {
		this.performSchedule = performSchedule;
	}
	
}
