package com.lachesis.mnis.core.old.doc;


/**
 * @author qi.liu
 *
 */
public class DepartmentPatientSummary extends BaseEntity{
	/** 
	 * @Fields serialVersionUID : TODO
	 */ 
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private String deptRefid; //科室编码
	private String summaryTime;	//统计时间
	private String summary; //患者摘要
	
	
	
	public String getDeptRefid() {
		return deptRefid;
	}
	public void setDeptRefid(String deptRefid) {
		this.deptRefid = deptRefid;
	}
	public String getSummaryTime() {
		return summaryTime;
	}
	public void setSummaryTime(String summaryTime) {
		this.summaryTime = summaryTime;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
}
