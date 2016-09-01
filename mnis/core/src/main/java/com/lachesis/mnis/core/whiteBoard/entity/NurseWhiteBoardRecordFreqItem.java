package com.lachesis.mnis.core.whiteBoard.entity;
/**
 * 小白板子项信息
 * @author ThinkPad
 *
 */
public class NurseWhiteBoardRecordFreqItem {
	private String itemId;
	/**
	 * 子项code
	 */
	private String recordItemCode;
	/**
	 * 子项name
	 */
	private String recordItemName;
	/**
	 * 子项简写name
	 */
	private String simpleRecordItemName;
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getRecordItemCode() {
		return recordItemCode;
	}
	public void setRecordItemCode(String recordItemCode) {
		this.recordItemCode = recordItemCode;
	}
	public String getRecordItemName() {
		return recordItemName;
	}
	public void setRecordItemName(String recordItemName) {
		this.recordItemName = recordItemName;
	}
	public String getSimpleRecordItemName() {
		return simpleRecordItemName;
	}
	public void setSimpleRecordItemName(String simpleRecordItemName) {
		this.simpleRecordItemName = simpleRecordItemName;
	}
}
