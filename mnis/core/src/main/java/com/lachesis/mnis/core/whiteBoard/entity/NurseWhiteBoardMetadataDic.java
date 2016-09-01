package com.lachesis.mnis.core.whiteBoard.entity;
/**
 * 小白板项目 元数据配置字典表
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardMetadataDic {
	/**
	 * 项目编号
	 */
	private String code;
	/**
	 * 项目名称
	 */
	private String name;
	/**
	 * 项目是否选中
	 */
	private boolean isSelected;
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
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
}
