package com.lachesis.mnis.board.dto;


/**
 * 小白板记录子项
 * @author ThinkPad
 *
 */
public class NwbRecordItemDto {
	private String itemId;
	/**
	 * 本地保存的记录id
	 */
	private String recordId;
	private String code;
	private String patId;
	private String patName;
	private String bedCode;
	private String patInfo;
	private String value;
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPatId() {
		return patId;
	}
	public void setPatId(String patId) {
		this.patId = patId;
	}
	public String getPatName() {
		return patName;
	}
	public void setPatName(String patName) {
		this.patName = patName;
	}
	public String getBedCode() {
		return bedCode;
	}
	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRecordId() {
		return recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	public String getPatInfo() {
		return patInfo;
	}
	public void setPatInfo(String patInfo) {
		this.patInfo = patInfo;
	}
	
}

