package com.lachesis.mnis.core.whiteBoard.entity;

import com.google.gson.annotations.SerializedName;

/**
 * 瘦客户端显示的元数据
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardMetadataTV {
	/**
	 * 主键
	 */
	private String id;
	/**
	 * 主数据源配置id
	 */
	private String templateId;
	/**
	 * 部门
	 */
	private String deptCode;
	/**
	 * 元数据编号
	 */
	private String code;
	private String name;
	
	private double width;
	private double titleWidth;
	
	/**
	 * 列
	 */
	private int columnNo;
	/**
	 * 行
	 */
	private int rowNo;
	/**
	 * 所占行数
	 */
	private int includeRow;
	/**
	 * 仅显示数据
	 */
	private boolean isShowData;
	/**
	 *仅显示标题
	 */
	private boolean isShowTitle;
	/**
	 * 显示右分割线
	 */
	private boolean isShowLineR;
	/**
	 * 显示下分割线
	 */
	private boolean isShowLineB;
	/**
	 * 是否显示标题与数据分隔线
	 */
	private boolean isShowLineT;
	
	private boolean isBedCode;
	
	private boolean isDosage;
	
	@SerializedName("titleFos")
	private int titleFontSize;
	@SerializedName("contFos")
	private int contentFontSize;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
	public int getColumnNo() {
		return columnNo;
	}
	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public int getIncludeRow() {
		return includeRow;
	}
	public void setIncludeRow(int includeRow) {
		this.includeRow = includeRow;
	}
	public boolean isShowData() {
		return isShowData;
	}
	public void setShowData(boolean isShowData) {
		this.isShowData = isShowData;
	}
	public boolean isShowTitle() {
		return isShowTitle;
	}
	public void setShowTitle(boolean isShowTitle) {
		this.isShowTitle = isShowTitle;
	}
	public boolean isShowLineR() {
		return isShowLineR;
	}
	public void setShowLineR(boolean isShowLineR) {
		this.isShowLineR = isShowLineR;
	}
	public boolean isShowLineB() {
		return isShowLineB;
	}
	public void setShowLineB(boolean isShowLineB) {
		this.isShowLineB = isShowLineB;
	}
	public boolean isShowLineT() {
		return isShowLineT;
	}
	public void setShowLineT(boolean isShowLineT) {
		this.isShowLineT = isShowLineT;
	}
	public int getTitleFontSize() {
		return titleFontSize;
	}
	public void setTitleFontSize(int titleFontSize) {
		this.titleFontSize = titleFontSize;
	}
	public int getContentFontSize() {
		return contentFontSize;
	}
	public void setContentFontSize(int contentFontSize) {
		this.contentFontSize = contentFontSize;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public boolean isBedCode() {
		return isBedCode;
	}
	public void setBedCode(boolean isBedCode) {
		this.isBedCode = isBedCode;
	}
	public boolean isDosage() {
		return isDosage;
	}
	public void setDosage(boolean isDosage) {
		this.isDosage = isDosage;
	}
	public double getTitleWidth() {
		return titleWidth;
	}
	public void setTitleWidth(double titleWidth) {
		this.titleWidth = titleWidth;
	}
}
