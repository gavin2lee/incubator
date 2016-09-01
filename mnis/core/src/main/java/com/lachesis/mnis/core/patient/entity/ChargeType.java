/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.core.patient.entity;

/**
 * The class ChargeType.
 * 
 * 存储费用类型
 * 
 * @author: yanhui.wang
 * @since: 2014-6-17	
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
public enum ChargeType {
	//01 自费 02 保险  03公费在职    04公费退休  05公费高干
	
	
	//0-自费, 1-医保, 2-其他
	SELF("0"), MEDICARE("1"), FREE("2");

	private final String state;

	private ChargeType(String state) {

		this.state = state;

	}

	/**
	 *获取对应枚举的存储
	 * @return
	 */
	public String getState() {
		return this.state;
	}

	/**
	 * 获取存储字符对应的枚举
	 * @param vState
	 * @return
	 */
	public static ChargeType getState(String vState) {
		if (vState == null) {
			return null;
		}
		for (ChargeType myState : ChargeType.values()) {
			if (myState.getState().equalsIgnoreCase(vState)) {
				return myState;
			}
		}
		return null;

	}

	/**
	 * 获取存储对应的枚举的字符显示
	 * @param vState
	 * @return
	 */
	public static String getDisplay(String vState) {
		for ( ChargeType myState : ChargeType.values()) {
			if (myState.getState().equalsIgnoreCase(vState)) {
				return String.valueOf(myState);
			}
		}
		return null;
	}

	@Override
	public String toString() {
		switch (this) {
			case SELF:
				return "自费";
			case MEDICARE:
				return "医保";
			case FREE:
				return "公费";
			default:
				return "其它";
		}
	}

	/**
	 * 
	 * @param i: the quick input number
	 * @return
	 */
	public static ChargeType getSexByQuickInput(int i) {
		switch (i) {
			case 1:
				return ChargeType.SELF;
			case 2:
				return ChargeType.MEDICARE;
			default:
				return ChargeType.FREE;

		}
	}
}
