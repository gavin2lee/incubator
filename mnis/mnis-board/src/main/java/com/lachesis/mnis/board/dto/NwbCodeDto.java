package com.lachesis.mnis.board.dto;

/**
 * 小白板所有项目
 * @author ThinkPad
 *
 */
public class NwbCodeDto {
	private String code;
	private String name;
	/**
	 * 是否选择
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
