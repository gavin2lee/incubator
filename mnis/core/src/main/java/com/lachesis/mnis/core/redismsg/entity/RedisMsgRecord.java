package com.lachesis.mnis.core.redismsg.entity;

import java.util.Date;
/**
 * 消息处理记录
 * @author ThinkPad
 *
 */
public class RedisMsgRecord {
	private int id;
	private String operNurseCode;
	private String operNurseName;
	/**
	 * 消息类型：1:ORDER,2:LABTEST,3:INSPCTION,4:SKINTEST,5:wardPatrol
	 */
	private String operType;
	private String operId;
	private Date operDate;
	private String operValue;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getOperNurseCode() {
		return operNurseCode;
	}
	public void setOperNurseCode(String operNurseCode) {
		this.operNurseCode = operNurseCode;
	}
	public String getOperNurseName() {
		return operNurseName;
	}
	public void setOperNurseName(String operNurseName) {
		this.operNurseName = operNurseName;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getOperId() {
		return operId;
	}
	public void setOperId(String operId) {
		this.operId = operId;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getOperValue() {
		return operValue;
	}
	public void setOperValue(String operValue) {
		this.operValue = operValue;
	}
}
