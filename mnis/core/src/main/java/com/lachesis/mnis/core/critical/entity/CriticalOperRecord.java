package com.lachesis.mnis.core.critical.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 危急值操作记录
 * @author liangming.deng
 *
 */
public class CriticalOperRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String operNurseCode;
	private String operNurseName;
	private Date operTime;
	private String receiveName;
	private String doctorName;
	private boolean isRepeat;
	/**
	 * 操作意见
	 */
	private String operValue;
	/**
	 * 关联条码
	 */
	private String barcode;
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
	public Date getOperTime() {
		return operTime;
	}
	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	public String getOperValue() {
		return operValue;
	}
	public void setOperValue(String operValue) {
		this.operValue = operValue;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getReceiveName() {
		return receiveName;
	}
	public void setReceiveName(String receiveName) {
		this.receiveName = receiveName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public boolean isRepeat() {
		return isRepeat;
	}
	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}
}
