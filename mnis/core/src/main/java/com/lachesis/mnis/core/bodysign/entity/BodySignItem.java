package com.lachesis.mnis.core.bodysign.entity;

import java.util.Date;


/**
 * 注意：每个体征可以输入正常值，也可以输入特殊值（拒测等）和补充说明（说明体温是腋温等）
 * <ul>
 * <li>itemName - 项目的名称，如体温</li>
 * <li>itemCode - 项目的编码，如temperature</li>
 * <li>itemValue - 项目的正常数字取值, 如37</li>
 * <li>measureNoteName - 测量说明，如腋温，外出等。当为外出一类的说明是itemvalue为空</li>
 * <li>measureNoteCode - 测量说明的编码</li>
 * <ul>
 * transient ：序列化时排除该字段
 * @author wenhuan.cui
 *
 */
public class BodySignItem {

	/** 项目记录id */
	private int detailRecordId;
	/** 主表记录id */
	private int masterRecordId;
	/** 项目字段 */
	private BodySignDict bodySignDict;
	/** 项目取值，为null时表示未测量，原因从measureNote中获取 */
	private String itemValue;
	//入院出入量时间差
	private String ryHourDiff;
	/** 测量说明编码 */
	private String measureNoteCode;
	/** 测量说明 */
	private String measureNoteName;
	/** 异常标记(-1-偏低,0-正常,1-偏高) */
	private int abnormalFlag;
	/** 默认true 正常标记，false 特殊处理标记 */
	private boolean specMark;
	/** 该项是否显示在体温单上，默认true都显示 **/
	private boolean show = true;
	/** 项目索引 */
	private int index;
	 
	private String patId;  //患者id
	private Date recordDate;   //体征记录时间点
	
	private transient boolean isAdd; // 是否已添加
	
	private String status;//事件状态   01:新建  09:已删除 
	
	private String unit;//体征item单位

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BodySignItem() {
		this.bodySignDict = new BodySignDict();
	}

	public BodySignItem(BodySignItem other) {
		this.itemValue = other.itemValue;
		this.bodySignDict = BodySignDict.create(
				other.bodySignDict.getItemCode(),
				other.bodySignDict.getItemName(),
				other.bodySignDict.getItemUnit());
		this.measureNoteCode = other.measureNoteCode;
		this.measureNoteName = other.measureNoteName;
		this.abnormalFlag = other.abnormalFlag;
		this.index = other.index;
		this.detailRecordId = other.detailRecordId;
		this.masterRecordId = other.masterRecordId;
	}


	public void setItemCode(String itemCode) {
		bodySignDict.setItemCode(itemCode);
	}

	public void setItemUnit(String itemUnit) {
		bodySignDict.setItemUnit(itemUnit);
	}

	public String getItemCode() {
		return bodySignDict == null ? null : bodySignDict.getItemCode();
	}

	public String getItemUnit() {
		return bodySignDict.getItemUnit();
	}

	public String getItemName() {
		return bodySignDict.getItemName();
	}

	public void setItemName(String itemName) {
		bodySignDict.setItemName(itemName);
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public BodySignDict getBodySignDict() {
		return bodySignDict;
	}

	public void setBodySignDict(BodySignDict bodySignDict) {
		this.bodySignDict = bodySignDict;
	}

	public String getMeasureNoteCode() {
		return measureNoteCode;
	}

	public void setMeasureNoteCode(String measureNoteCode) {
		this.measureNoteCode = measureNoteCode;
	}

	public String getMeasureNoteName() {
		return measureNoteName;
	}

	public void setMeasureNoteName(String measureNoteName) {
		this.measureNoteName = measureNoteName;
	}

	public int getAbnormalFlag() {
		return abnormalFlag;
	}

	public void setAbnormalFlag(int abnormalFlag) {
		this.abnormalFlag = abnormalFlag;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getDetailRecordId() {
		return detailRecordId;
	}

	public void setDetailRecordId(int detailRecordId) {
		this.detailRecordId = detailRecordId;
	}

	public int getMasterRecordId() {
		return masterRecordId;
	}

	public void setMasterRecordId(int masterRecordId) {
		this.masterRecordId = masterRecordId;
	}

	public boolean isSpecMark() {
		return specMark;
	}

	public void setSpecMark(boolean specMark) {
		this.specMark = specMark;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	@Override
	public boolean equals(Object otherObject) {
		if (this == otherObject) {
			return true;
		}

		if (otherObject instanceof String) {
			BodySignItem another = (BodySignItem) otherObject;
			if (this.getItemCode() == another.getItemCode()) {
				return true;
			}
		}
		return false;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public boolean isAdd() {
		return isAdd;
	}

	public void setAdd(boolean isAdd) {
		this.isAdd = isAdd;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getRyHourDiff() {
		return ryHourDiff;
	}

	public void setRyHourDiff(String ryHourDiff) {
		this.ryHourDiff = ryHourDiff;
	}
}
