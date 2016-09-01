package com.lachesis.mnis.core.order.entity;

/**
 * 医嘱根据频次获取到初始的执行时间
 * 
 * @author yuliang.xu
 * 
 */
public class OrderFreqPlanTime {
	private String freqCode;
	private String deptCode;
	private String deptName;
	/**多个时间时间逗号分隔，例：8:00,20:00,21:00*/
	private String planTime;
	private String createTime;

	public String getFreqCode() {
		return freqCode;
	}

	public void setFreqCode(String freqCode) {
		this.freqCode = freqCode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getPlanTime() {
		return planTime;
	}

	public void setPlanTime(String planTime) {
		this.planTime = planTime;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

}
