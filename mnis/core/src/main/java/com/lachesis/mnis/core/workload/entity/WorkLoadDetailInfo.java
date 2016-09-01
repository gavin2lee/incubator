package com.lachesis.mnis.core.workload.entity;

import com.google.gson.annotations.SerializedName;

public class WorkLoadDetailInfo {
	@SerializedName("nurCod")
	private String nurseCode;
	/**
	 * 统计项目
	 */
	@SerializedName("wlTyp")
	private String workLoadType;
	@SerializedName("wlTypNam")
	private String workLoadTypeName;
	/**
	 * 统计数量
	 */
	@SerializedName("stCount")
	private int statisticCount;
	public String getNurseCode() {
		return nurseCode;
	}
	public void setNurseCode(String nurseCode) {
		this.nurseCode = nurseCode;
	}
	public String getWorkLoadType() {
		return workLoadType;
	}
	public void setWorkLoadType(String workLoadType) {
		this.workLoadType = workLoadType;
	}
	public int getStatisticCount() {
		return statisticCount;
	}
	public void setStatisticCount(int statisticCount) {
		this.statisticCount = statisticCount;
	}
	public String getWorkLoadTypeName() {
		return workLoadTypeName;
	}
	public void setWorkLoadTypeName(String workLoadTypeName) {
		this.workLoadTypeName = workLoadTypeName;
	}
}
