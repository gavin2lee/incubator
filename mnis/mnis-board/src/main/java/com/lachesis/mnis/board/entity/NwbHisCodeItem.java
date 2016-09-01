package com.lachesis.mnis.board.entity;

/**
 * code映射详细
 * @author ThinkPad
 *
 */
public class NwbHisCodeItem {
	/**
	 * 自增长唯一码
	 */
	private String id;
	private String deptCode;
	private String templateId;
	private String code;
	private String name;
	private String hisCode;
	private String hisCodeName;
	/**
	 * 频次code
	 */
	private String freq;
	/**
	 * code对应的同一父目录
	 */
	private String parentCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHisCode() {
		return hisCode;
	}
	public void setHisCode(String hisCode) {
		this.hisCode = hisCode;
	}
	public String getHisCodeName() {
		return hisCodeName;
	}
	public void setHisCodeName(String hisCodeName) {
		this.hisCodeName = hisCodeName;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
}
