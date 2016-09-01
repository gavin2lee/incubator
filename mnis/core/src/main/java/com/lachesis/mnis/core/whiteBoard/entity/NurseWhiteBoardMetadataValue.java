package com.lachesis.mnis.core.whiteBoard.entity;

/**
 * 元数据中默认值
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardMetadataValue {
	/**
	 * 主键id
	 */
	private int id;
	private String code;
	private String name;
	
	private String value;
	private String remark;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
