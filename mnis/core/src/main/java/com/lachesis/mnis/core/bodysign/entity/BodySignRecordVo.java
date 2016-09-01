package com.lachesis.mnis.core.bodysign.entity;
/**
 * 体温单新增生命体征实体(体温单6个点和一天一次的点)
 * @author ThinkPad
 *
 */
public class BodySignRecordVo {
	/**
	 * 6个点和一天一次对应的code
	 */
	private String recordCode;
	/**
	 * 对应的时间点
	 */
	private String recordName;
	/**
	 * 时间点对应的序号
	 */
	private int timeId;
	
	/**
	 * 时间点对应的生命体征值
	 */
	private BodySignRecord bodySignRecord;
	
	public String getRecordCode() {
		return recordCode;
	}
	public void setRecordCode(String recordCode) {
		this.recordCode = recordCode;
	}
	public BodySignRecord getBodySignRecord() {
		return bodySignRecord;
	}
	public void setBodySignRecord(BodySignRecord bodySignRecord) {
		this.bodySignRecord = bodySignRecord;
	}
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	public int getTimeId() {
		return timeId;
	}
	public void setTimeId(int timeId) {
		this.timeId = timeId;
	}
}
