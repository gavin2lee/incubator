package com.lachesis.mnis.core.whiteBoard.entity;

import java.util.List;

/**
 * 小白板项和小白板子项记录集合
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardRecordFreqInfo {
	/**
	 * 随机code主键
	 */
	private String randomId;
	/**
	 * 执行计划
	 */
	private String performSchedule;
	/**
	 * 记录值
	 */
	private String recordValue;
	/**
	 * 记录排序
	 */
	private int orderNo;
	/**
	 * 患者信息
	 */
	private List<NurseWhiteBoardRecordFreqPatInfo> nurseWhiteBoardRecordFreqPatInfos;
	/**
	 * 子项目数据
	 */
	private List<NurseWhiteBoardRecordFreqItem> nurseWhiteBoardRecordFreqItems;
	public String getRandomId() {
		return randomId;
	}
	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}
	public String getPerformSchedule() {
		return performSchedule;
	}
	public void setPerformSchedule(String performSchedule) {
		this.performSchedule = performSchedule;
	}
	public String getRecordValue() {
		return recordValue;
	}
	public void setRecordValue(String recordValue) {
		this.recordValue = recordValue;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public List<NurseWhiteBoardRecordFreqPatInfo> getNurseWhiteBoardRecordFreqPatInfos() {
		return nurseWhiteBoardRecordFreqPatInfos;
	}
	public void setNurseWhiteBoardRecordFreqPatInfos(
			List<NurseWhiteBoardRecordFreqPatInfo> nurseWhiteBoardRecordFreqPatInfos) {
		this.nurseWhiteBoardRecordFreqPatInfos = nurseWhiteBoardRecordFreqPatInfos;
	}
	public List<NurseWhiteBoardRecordFreqItem> getNurseWhiteBoardRecordFreqItems() {
		return nurseWhiteBoardRecordFreqItems;
	}
	public void setNurseWhiteBoardRecordFreqItems(
			List<NurseWhiteBoardRecordFreqItem> nurseWhiteBoardRecordFreqItems) {
		this.nurseWhiteBoardRecordFreqItems = nurseWhiteBoardRecordFreqItems;
	}
}



