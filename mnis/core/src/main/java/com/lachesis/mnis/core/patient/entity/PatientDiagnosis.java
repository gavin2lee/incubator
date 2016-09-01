package com.lachesis.mnis.core.patient.entity;

import java.util.Date;

/**
 * 患者诊断
 *
 * @author yuliang.xu
 * @date 2015年6月11日 上午10:17:07
 */
public class PatientDiagnosis {

	/** 患者诊断记录自增长号 */
	private int id;
	/** 住院流水号 */
	private String patId;
	/** 诊断信息 */
	private String info;
	/** 诊断日期 */
	private Date date;
	/** 数据类型   01：入院诊断  02：主诉 */
	private String dataType; 
	/** 记录人工号 */
	private String recordUser;
	/** 记录日期 */
	private Date recordDate;
	/** 标记(0:无效,1:有效) */
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

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRecordUser() {
		return recordUser;
	}

	public void setRecordUser(String recordUser) {
		this.recordUser = recordUser;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	
}