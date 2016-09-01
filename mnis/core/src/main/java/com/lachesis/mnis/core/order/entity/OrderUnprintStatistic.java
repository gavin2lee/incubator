package com.lachesis.mnis.core.order.entity;

/**
 * 未打印医嘱统计
 * @author liangming.deng
 *
 */
public class OrderUnprintStatistic {
	/**
	 * 患者id
	 */
	private String patId;
	/**
	 * 床位信息
	 */
	private String bedCode;
	/**
	 * 非打印医嘱个数
	 */
	private int unprintOrderCount;
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getBedCode() {
		return bedCode;
	}
	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}
	public int getUnprintOrderCount() {
		return unprintOrderCount;
	}
	public void setUnprintOrderCount(int unprintOrderCount) {
		this.unprintOrderCount = unprintOrderCount;
	}
	
	
}
