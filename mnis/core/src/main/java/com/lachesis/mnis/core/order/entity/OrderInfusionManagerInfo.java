package com.lachesis.mnis.core.order.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
/**
 * 闭环输液信息
 * @author ThinkPad
 *
 */
public class OrderInfusionManagerInfo {
	/**
	 * 闭环输液患者信息
	 */
	@SerializedName("patInfo")
	private OrderInfusionManagerPatInfo orderInfusionManagerPatInfo;
	/**
	 * 医嘱信息
	 */
	@SerializedName("orderInfo")
	private OrderInfusionManagerOrderInfo orderInfusionManagerOrderInfo;
	/**
	 * 医嘱项目信息
	 */
	@SerializedName("orderItems")
	private List<OrderInfusionManagerOrderItem> orderInfusionManagerOrderItems = new ArrayList<OrderInfusionManagerOrderItem>();;
	public OrderInfusionManagerPatInfo getOrderInfusionManagerPatInfo() {
		return orderInfusionManagerPatInfo;
	}
	public void setOrderInfusionManagerPatInfo(
			OrderInfusionManagerPatInfo orderInfusionManagerPatInfo) {
		this.orderInfusionManagerPatInfo = orderInfusionManagerPatInfo;
	}
	public OrderInfusionManagerOrderInfo getOrderInfusionManagerOrderInfo() {
		return orderInfusionManagerOrderInfo;
	}
	public void setOrderInfusionManagerOrderInfo(
			OrderInfusionManagerOrderInfo orderInfusionManagerOrderInfo) {
		this.orderInfusionManagerOrderInfo = orderInfusionManagerOrderInfo;
	}
	public List<OrderInfusionManagerOrderItem> getOrderInfusionManagerOrderItems() {
		return orderInfusionManagerOrderItems;
	}
	public void setOrderInfusionManagerOrderItems(
			List<OrderInfusionManagerOrderItem> orderInfusionManagerOrderItems) {
		this.orderInfusionManagerOrderItems = orderInfusionManagerOrderItems;
	}
}
