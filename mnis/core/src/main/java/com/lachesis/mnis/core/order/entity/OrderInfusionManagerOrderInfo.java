package com.lachesis.mnis.core.order.entity;
/**
 * 闭环输液医嘱信息
 * @author ThinkPad
 *
 */
public class OrderInfusionManagerOrderInfo {
	private String orderNo;
	/**
	 * 总共多少袋
	 */
	private int orderCount;
	/**
	 * 正执行第几袋
	 */
	private int orderExeIndex;
	private String orderBarcode;
	private String specCapacity;
	private String frequency;
	private String usage;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
	public int getOrderExeIndex() {
		return orderExeIndex;
	}
	public void setOrderExeIndex(int orderExeIndex) {
		this.orderExeIndex = orderExeIndex;
	}
	public String getOrderBarcode() {
		return orderBarcode;
	}
	public void setOrderBarcode(String orderBarcode) {
		this.orderBarcode = orderBarcode;
	}
	public String getSpecCapacity() {
		return specCapacity;
	}
	public void setSpecCapacity(String specCapacity) {
		this.specCapacity = specCapacity;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
}
