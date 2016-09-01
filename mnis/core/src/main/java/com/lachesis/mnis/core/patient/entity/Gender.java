/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.core.patient.entity;

/**
 * 存储性别
 * 
 * @author: yanhui.wang
 * @since: 2014-3-28
 * @version: $Revision$ $Date$ $LastChangedBy$
 * 
 */
public enum Gender {
	M("男"), F("女"), OTHER("其它");

	String displayName;

	Gender(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
