package com.lachesis.mnis.core.whiteBoard.entity;

import java.util.List;

/**
 * 护理小白板数据录入传入对象
 *
 */
public class NurseWhiteBoardItemInBo {
	private String itemCode;//元数据项目编号
	private String itemName;//元数据项目名称
	private List<String> itemValues;//录入结果
//	private String itemValueCode;//录入项目编号
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public List<String> getItemValues() {
		return itemValues;
	}
	public void setItemValues(List<String> itemValues) {
		this.itemValues = itemValues;
	}
	/*public String getItemValueCode() {
		return itemValueCode;
	}
	public void setItemValueCode(String itemValueCode) {
		this.itemValueCode = itemValueCode;
	}*/
}