package com.lachesis.mnis.core.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.lachesis.mnis.core.util.NumberUtil;

public class NumberUtilTest {
	
	@Test
	public void testConvertChineseToNumber() {
		String[] inputs = new String[] {"零", "一", "十", "十一", "二十", "二十一"};
		String[] outputs = new String[] {"00", "01", "10", "11", "20", "21"};
		for(int i=0; i < inputs.length; i++) {
			assertEquals(outputs[i], NumberUtil.convertChineseToNumber(inputs[i]));
		}
	}
	
	@Test
	public void testConvertNumberToChinese() {
		int[] inputs = new int[] {0, 1, 10, 11, 20, 21};
		String[] outputs = new String[] {"零", "一", "十", "十一", "二十", "二十一"};
		for(int i=0; i < inputs.length; i++) {
			assertEquals(outputs[i], NumberUtil.convertNumberToChinese(inputs[i],true));
		}
	}
}
