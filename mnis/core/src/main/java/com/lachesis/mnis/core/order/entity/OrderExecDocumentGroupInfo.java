package com.lachesis.mnis.core.order.entity;

import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorRecord;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorItem;
public class OrderExecDocumentGroupInfo {
	/**
	 * 医嘱主键
	 */
	@SerializedName("ordGroupNo")
	private String orderGroupNo;
	/**
	 * 医嘱条码
	 */
	private String barcode;
	/**
	 * 医嘱计划时间
	 */
	private Date planDate;
	/**
	 * 执行计划
	 */
	@SerializedName("perfSchd")
	private String performSchedule;
	
	/**
	 * 是否已执行
	 */
	private boolean isExeced;
	/**
	 * 医嘱频次
	 */
	private String freq;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 医嘱执行信息
	 */
	@SerializedName("ordExecLog")
	private OrderExecLog orderExecLog;
	/**
	 * 医嘱巡视信息
	 */
	@SerializedName("infMntRecs")
	private List<InfusionMonitorRecord> infusionMonitorRecords;
	//配液信息
	@SerializedName("ordLiqItems")
	private List<OrderLiquorItem> orderLiquorItems;
	
	public String getOrderGroupNo() {
		return orderGroupNo;
	}
	public void setOrderGroupNo(String orderGroupNo) {
		this.orderGroupNo = orderGroupNo;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
	public OrderExecLog getOrderExecLog() {
		return orderExecLog;
	}
	public void setOrderExecLog(OrderExecLog orderExecLog) {
		this.orderExecLog = orderExecLog;
	}
	public List<InfusionMonitorRecord> getInfusionMonitorRecords() {
		return infusionMonitorRecords;
	}
	public void setInfusionMonitorRecords(
			List<InfusionMonitorRecord> infusionMonitorRecords) {
		this.infusionMonitorRecords = infusionMonitorRecords;
	}
	public List<OrderLiquorItem> getOrderLiquorItems() {
		return orderLiquorItems;
	}
	public void setOrderLiquorItems(List<OrderLiquorItem> orderLiquorItems) {
		this.orderLiquorItems = orderLiquorItems;
	}
	public boolean isExeced() {
		return isExeced;
	}
	public void setExeced(boolean isExeced) {
		this.isExeced = isExeced;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getPerformSchedule() {
		return performSchedule;
	}
	public void setPerformSchedule(String performSchedule) {
		this.performSchedule = performSchedule;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
