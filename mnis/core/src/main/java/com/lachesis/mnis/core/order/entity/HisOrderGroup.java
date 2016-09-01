package com.lachesis.mnis.core.order.entity;

import java.util.List;

/**
 * 原始医嘱
 * @author ThinkPad
 *
 */
public class HisOrderGroup {
	private String orderGroupNo;
	/**
	 * 原始医嘱信息
	 */
	private OrderGroup orderGroup;
	/**
	 * 医嘱执行信息
	 */
	private List<OrderExecLog> orderExecList;
	
	public List<OrderExecLog> getOrderExecList() {
		return orderExecList;
	}
	public void setOrderExecList(List<OrderExecLog> orderExecList) {
		this.orderExecList = orderExecList;
	}
	public String getOrderGroupNo() {
		return orderGroupNo;
	}
	public void setOrderGroupNo(String orderGroupNo) {
		this.orderGroupNo = orderGroupNo;
	}
	public OrderGroup getOrderGroup() {
		return orderGroup;
	}
	public void setOrderGroup(OrderGroup orderGroup) {
		this.orderGroup = orderGroup;
	}
	
}
