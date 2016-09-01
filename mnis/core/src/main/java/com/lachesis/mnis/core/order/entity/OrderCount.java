package com.lachesis.mnis.core.order.entity;

/**
 * 扫描返回统计数据 
 * @author qingzhi.liu
 *
 */
public class OrderCount {
	private String pat_id;
	private int pendingOrderCount;
	private int finishedOrderCount;
	
	public OrderCount() {
		super();
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
	public String getPat_id() {
		return pat_id;
	}
	public void setPat_id(String pat_id) {
		this.pat_id = pat_id;
	}
	
	
}
