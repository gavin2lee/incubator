package com.lachesis.mnis.core.liquor.entity;

public class OrderLiquorStatistic {
	/**
	 * 待备药人数
	 */
	private int prepareCount;
	/**
	 * 待审核人数
	 */
	private int verifyCount;
	/**
	 * 待配液人数
	 */
	private int execCount;
	public int getPrepareCount() {
		return prepareCount;
	}
	public void setPrepareCount(int prepareCount) {
		this.prepareCount = prepareCount;
	}
	public int getVerifyCount() {
		return verifyCount;
	}
	public void setVerifyCount(int verifyCount) {
		this.verifyCount = verifyCount;
	}
	public int getExecCount() {
		return execCount;
	}
	public void setExecCount(int execCount) {
		this.execCount = execCount;
	}
	
}
