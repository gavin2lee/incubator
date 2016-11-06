package com.harmazing.framework.util;

import java.math.BigDecimal;

public abstract class NumberUtil {
	public static String rounded(Object xx, int x) {
		BigDecimal bd = new BigDecimal(Double.parseDouble(xx.toString()));
		return (bd.setScale(x, BigDecimal.ROUND_HALF_UP).doubleValue()) + "";
	}

	public static void main(String[] args) {
		System.out.println(rounded(0.0355534534, 2));
	}
}
