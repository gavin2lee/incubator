package com.lachesis.mnis.board.entity;

import java.util.Date;
/**
 * 各个科室元数据
 * 
 * @author ThinkPad
 *
 */
public class NwbMetadata {
	/**
	 * 主键id
	 */
	private String id;
	/**
	 * 父id
	 */
	private String parentId;
	/**
	 * 部门
	 */
	private String deptCode;
	/**
	 * 数据code
	 */
	private String code;
	/**
	 * 数据name
	 */
	private String name;
	/**
	 * 数据深度
	 */
	private int level;
	/**
	 * 数据类型(text,checkbox,option,value,radio等)
	 */
	private String inputType;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 是否自动加载(自动和手动)
	 */
	private boolean isAuto;

	/**
	 * code是否可以编辑
	 */
	private boolean isEdit;
	/**
	 * 患者信息只包含床位
	 */
	private boolean isBedCode;

	/**
	 * NDA是否可执行
	 */
	private boolean isExec;

	/**
	 * 行编号
	 */
	private int rowNo;
	/**
	 * 列标号
	 */
	private int columnNo;
	/**
	 * 列类型(0:一列一行,大于0：一列多行)
	 */
	private String columnType;
	/**
	 * 背景颜色
	 */
	private String backgroundColor;

	/**
	 * code显示类型
	 */
	private String showType;
	/**
	 * 是否包含完整列
	 */
	private boolean isColspan;

	private double height;

	private double width;
	/**
	 * 标题宽度
	 */
	private double titleWidth;
	/**
	 * 主数据源配置id
	 */
	private String templateId;
	/**
	 * 所占行数
	 */
	private int includeRow;
	/**
	 * 仅显示数据
	 */
	private boolean isShowData;
	/**
	 * 仅显示标题
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
	
	private boolean isDosage;
	/**
	 * 是否显示标题与数据分隔线
	 */
	private boolean isShowLineT;

	private int titleFontSize;
	private int contentFontSize;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public int getRowNo() {
		return rowNo;
	}

	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	public int getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(int columnNo) {
		this.columnNo = columnNo;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public boolean isBedCode() {
		return isBedCode;
	}

	public void setBedCode(boolean isBedCode) {
		this.isBedCode = isBedCode;
	}

	public boolean isColspan() {
		return isColspan;
	}

	public void setColspan(boolean isColspan) {
		this.isColspan = isColspan;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public boolean isExec() {
		return isExec;
	}

	public void setExec(boolean isExec) {
		this.isExec = isExec;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
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
