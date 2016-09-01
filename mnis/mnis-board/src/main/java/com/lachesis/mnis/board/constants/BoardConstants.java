package com.lachesis.mnis.board.constants;

import java.util.HashMap;

import com.lachesis.mnis.board.entity.NwbHisCode;

public class BoardConstants {
	/**
	 * 医嘱部门--code对应映射缓存
	 */
	public static final HashMap<String, HashMap<String, String>> ORDER_CODE_MAPPING_CACHE = new HashMap<String, HashMap<String,String>>();
	/**
	 * 医嘱 部门--NwbHisCode实体
	 */
	public static final HashMap<String, NwbHisCode> ORDER_CODES_CACHE = new HashMap<String, NwbHisCode>();
}
