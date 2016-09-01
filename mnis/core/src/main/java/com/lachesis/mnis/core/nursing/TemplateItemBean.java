package com.lachesis.mnis.core.nursing;

import java.io.Serializable;
import java.util.Date;

public class TemplateItemBean implements Serializable {

	private static final long serialVersionUID = -1151947300323514013L;
	private String jsnPth;
	private transient Integer recordItemId;
	private transient Integer recordId;
	private String nam;
	private transient String sName;
	private String inptTyp;
	private String datTyp;
	private Double minValue;
	private Double maxValue;
	private Integer mstFlg; // 1为必填， 0为非必填
	private Integer edtFlg; // 1为禁止编辑 0为可以编辑
	private Integer actFlg; // 1为活动列 0为固定列
	private Integer shwFlg = 1;
	private transient Integer colWidth;
	private transient Integer ymclfs;
	private transient Integer hhjg;
	private transient Integer modifyFlag;
	private String fillFlag;
	private Integer recordStatus;

	private String elementId;
	private String showFormat;

	private Date recordTime;

	private transient String selectItems;
	private transient String deftItemValue;
	private transient Integer startCol;
	private transient Integer endCol;

	private transient String value;

	public Integer getRecordItemId() {
		return recordItemId;
	}

	public void setRecordItemId(Integer recordItemId) {
		this.recordItemId = recordItemId;
	}

	public String getDeftItemValue() {
		return deftItemValue;
	}

	public void setDeftItemValue(String deftItemValue) {
		this.deftItemValue = deftItemValue;
	}

	public String getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(String selectItems) {
		this.selectItems = selectItems;
	}

	public String getJsnPth() {
		return jsnPth;
	}

	public void setJsnPth(String jsnPth) {
		this.jsnPth = jsnPth;
	}

	public String getNam() {
		return nam;
	}

	public void setNam(String nam) {
		this.nam = nam;
	}

	public String getInptTyp() {
		return inptTyp;
	}

	public void setInptTyp(String inptTyp) {
		this.inptTyp = inptTyp;
	}

	public String getDatTyp() {
		return datTyp;
	}

	public void setDatTyp(String datTyp) {
		this.datTyp = datTyp;
	}

	public Double getMinValue() {
		return minValue;
	}

	public void setMinValue(Double minValue) {
		this.minValue = minValue;
	}

	public Double getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(Double maxValue) {
		this.maxValue = maxValue;
	}

	public Integer getStartCol() {
		return startCol;
	}

	public void setStartCol(Integer startCol) {
		this.startCol = startCol;
	}

	public Integer getEndCol() {
		return endCol;
	}

	public void setEndCol(Integer endCol) {
		this.endCol = endCol;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getMstFlg() {
		return mstFlg;
	}

	public void setMstFlg(Integer mstFlg) {
		this.mstFlg = mstFlg;
	}

	public Integer getEdtFlg() {
		return edtFlg;
	}

	public void setEdtFlg(Integer edtFlg) {
		this.edtFlg = edtFlg;
	}

	public Integer getActFlg() {
		return actFlg;
	}

	public void setActFlg(Integer actFlg) {
		this.actFlg = actFlg;
	}

	public Integer getShwFlg() {
		return shwFlg;
	}

	public void setShwFlg(Integer shwFlg) {
		this.shwFlg = shwFlg;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Integer getColWidth() {
		return colWidth;
	}

	public void setColWidth(Integer colWidth) {
		this.colWidth = colWidth;
	}

	public Integer getRecordId() {
		return recordId;
	}

	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}

	public Integer getYmclfs() {
		return ymclfs;
	}

	public void setYmclfs(Integer ymclfs) {
		this.ymclfs = ymclfs;
	}

	public Integer getHhjg() {
		return hhjg;
	}

	public void setHhjg(Integer hhjg) {
		this.hhjg = hhjg;
	}

	public Integer getModifyFlag() {
		return modifyFlag;
	}

	public void setModifyFlag(Integer modifyFlag) {
		this.modifyFlag = modifyFlag;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	public String getFillFlag() {
		return fillFlag;
	}

	public void setFillFlag(String fillFlag) {
		this.fillFlag = fillFlag;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getShowFormat() {
		return showFormat;
	}

	public void setShowFormat(String showFormat) {
		this.showFormat = showFormat;
	}
}
