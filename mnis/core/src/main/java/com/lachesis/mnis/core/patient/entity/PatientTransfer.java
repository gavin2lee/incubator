package com.lachesis.mnis.core.patient.entity;

import java.util.Date;

/**
 * 患者转床转科
 *
 * @author yuliang.xu
 * @date 2015年6月11日 上午10:24:13
 */
public class PatientTransfer {
	
	/**出科记录自增长号*/
	private int id;
	/**住院流水号*/
	private String patId;
	/**执行日期*/
	private Date executeDate;
	/**执行人工号*/
	private String executor;
	/**病区代码*/
	private String wardCode;
	/**床位号码*/
	private String bedCode;
	/**新病区代码*/
	private String newWardCode;
	/**新床位号码*/
	private String newBedCode;
	/**标记(0:无效,1:有效)*/
	private boolean flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public Date getExecuteDate() {
		return executeDate;
	}

	public void setExecuteDate(Date executeDate) {
		this.executeDate = executeDate;
	}

	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}

	public String getWardCode() {
		return wardCode;
	}

	public void setWardCode(String wardCode) {
		this.wardCode = wardCode;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public String getNewWardCode() {
		return newWardCode;
	}

	public void setNewWardCode(String newWardCode) {
		this.newWardCode = newWardCode;
	}

	public String getNewBedCode() {
		return newBedCode;
	}

	public void setNewBedCode(String newBedCode) {
		this.newBedCode = newBedCode;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

}
