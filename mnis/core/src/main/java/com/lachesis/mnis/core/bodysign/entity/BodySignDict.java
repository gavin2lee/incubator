package com.lachesis.mnis.core.bodysign.entity;

/***
 * 
 * 生命体征项字典项目
 *
 * @author yuliang.xu
 * @date 2015年6月4日 下午4:46:47
 *
 */
public class BodySignDict {

	/** 项目代码 */
	private String itemCode;
	/** 项目名称 */
	private String itemName;
	/** 项目单位 */
	private String itemUnit;

	/**
	 * 创建字典项目
	 * 
	 * @param itemCode
	 * @param itemName
	 * @param itemUnit
	 * @return BodySignDict
	 * @throws
	 */
	public static BodySignDict create(String itemCode, String itemName, String itemUnit){
		BodySignDict dict = new BodySignDict();
		dict.setItemCode(itemCode);
		dict.setItemName(itemName);
		dict.setItemUnit(itemUnit);
		return dict;
	}
	
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

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}
}
