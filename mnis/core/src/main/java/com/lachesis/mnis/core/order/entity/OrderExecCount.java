package com.lachesis.mnis.core.order.entity;

public class OrderExecCount {
	@Override
	public String toString() {
		return orderGroupId + ":" + orderExecTimes;
	}
	
	public String getOrderGroupId() {
		return orderGroupId;
	}
	public void setOrderGroupId(String orderGroupId) {
		this.orderGroupId = orderGroupId;
	}
	public Integer getOrderExecTimes() {
		return orderExecTimes;
	}
	public void setOrderExecTimes(Integer orderExecTimes) {
		this.orderExecTimes = orderExecTimes;
	}
	private String orderGroupId;
	private Integer orderExecTimes;
}
