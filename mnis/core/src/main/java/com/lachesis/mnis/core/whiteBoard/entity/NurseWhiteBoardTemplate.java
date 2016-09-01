package com.lachesis.mnis.core.whiteBoard.entity;

import com.google.gson.annotations.SerializedName;

public class NurseWhiteBoardTemplate {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 部门
	 */
	private String deptCode;
	/**
	 * 行数
	 */
	private int rowCount;
	/**
	 * 显示标题
	 */
	private String title;
	/**
	 * 标题字体大小
	 */
	@SerializedName("titleFts")
	private int titleFontSize;
	/**
	 * 模板背景色
	 */
	@SerializedName("bground")
	private String background;
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
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getTitleFontSize() {
		return titleFontSize;
	}
	public void setTitleFontSize(int titleFontSize) {
		this.titleFontSize = titleFontSize;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
}
