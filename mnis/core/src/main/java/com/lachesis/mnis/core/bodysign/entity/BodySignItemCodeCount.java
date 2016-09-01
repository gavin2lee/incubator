package com.lachesis.mnis.core.bodysign.entity;

/***
 * 
 * 体征项统计信息
 *
 * @author yuliang.xu
 * @date 2015年6月17日 上午10:25:14
 *
 */
public class BodySignItemCodeCount {

	private String itemCode;

	private int times;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
