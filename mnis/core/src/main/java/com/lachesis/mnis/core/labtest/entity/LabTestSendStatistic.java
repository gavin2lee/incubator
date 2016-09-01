package com.lachesis.mnis.core.labtest.entity;
/**
 * 统计
 * @author ThinkPad
 *
 */

public class LabTestSendStatistic {
	/**
	 * 统计类型
	 */
	private String type;
	/**
	 * 统计类型名称
	 */
	private String name;
	/**
	 * 统计数量
	 */
	private int count;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
