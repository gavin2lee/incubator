package com.anyi.report.entity;

import com.lachesis.mnis.core.util.StringUtil;

import java.util.Date;

/**
 * 文书统计
 * @author ThinkPad
 *
 */
public class DocRecordStatistic {
	private int id;
	private String patId;
	private String deptCode;
	/**
	 * 统计类型：总入量,总出量
	 */
	private String staticType;
	private String staticTypeName;
	/**
	 * 子类型：尿量,其他出量等
	 */
	private String staticItemType;
	private String staticItemTypeName;
	/**
	 * 时差
	 */
	private String hourDiff;
	/**
	 * 单位类型：ml,mg等
	 */
	private String unit;
	private String staticValue;
	private String staticDate;
	private String staticTime;
	private Date createDate;
	private String templateID;//统计对应的模板
	private int pageNo; //统计记录打印所在的页
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
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getStaticType() {
		return staticType;
	}
	public void setStaticType(String staticType) {
		this.staticType = staticType;
	}
	public String getStaticItemType() {
		return staticItemType;
	}
	public void setStaticItemType(String staticItemType) {
		this.staticItemType = staticItemType;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getStaticValue() {
		return StringUtil.getStringValue(new Double(staticValue));
	}
	public String getStaticRawValue(){
		return this.staticValue;
	}
	public void setStaticValue(String staticValue) {
		this.staticValue = staticValue;
	}
	public String getStaticDate() {
		return staticDate;
	}
	public void setStaticDate(String staticDate) {
		this.staticDate = staticDate;
	}
	public String getStaticTime() {
		return staticTime;
	}
	public void setStaticTime(String staticTime) {
		this.staticTime = staticTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getStaticTypeName() {
		return staticTypeName;
	}
	public void setStaticTypeName(String staticTypeName) {
		this.staticTypeName = staticTypeName;
	}
	public String getStaticItemTypeName() {
		return staticItemTypeName;
	}
	public void setStaticItemTypeName(String staticItemTypeName) {
		this.staticItemTypeName = staticItemTypeName;
	}
	public String getHourDiff() {
		return hourDiff;
	}
	public void setHourDiff(String hourDiff) {
		this.hourDiff = hourDiff;
	}

	public String getTemplateID() {
		return templateID;
	}

	public void setTemplateID(String templateID) {
		this.templateID = templateID;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
}
