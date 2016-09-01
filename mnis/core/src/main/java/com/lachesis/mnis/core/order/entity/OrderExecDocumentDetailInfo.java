package com.lachesis.mnis.core.order.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OrderExecDocumentDetailInfo {
	/**
	 * 医嘱组号
	 */
	@SerializedName("ordNo")
	private String orderNo;
	
	@SerializedName("ordStatus")
	private String orderStatus;
	
	/**
	 * 是否核对
	 */
	private boolean isApproved;
	/**
	 * 医嘱药物详情
	 */
	@SerializedName("ordItems")
	private List<OrderItem> orderItems;
	/**
	 * 医嘱详情
	 */
	@SerializedName("ordGroupInfs")
	private List<OrderExecDocumentGroupInfo> orderExecDocumentGroupInfos;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public List<OrderExecDocumentGroupInfo> getOrderExecDocumentGroupInfos() {
		return orderExecDocumentGroupInfos;
	}
	public void setOrderExecDocumentGroupInfos(
			List<OrderExecDocumentGroupInfo> orderExecDocumentGroupInfos) {
		this.orderExecDocumentGroupInfos = orderExecDocumentGroupInfos;
	}
	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public boolean isApproved() {
		return isApproved;
	}
	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
}
