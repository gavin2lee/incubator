package com.lachesis.mnis.core.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/***
 * 
 * 数字相关工具类
 *
 * @ClassName: NumberUtil 
 * @author yuliang.xu
 * @date 2015年4月30日 下午6:08:09 
 *
 * @see org.apache.commons.lang.math.NumberUtils
 */
public class NumberUtil {
	
	private static final Map<String, String> textNumberMap = new HashMap<>();
	private static final Map<Integer, String> numberTextMap = new HashMap<>();
	static {
		final String[] cnNumbers = new String[] {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
		for(int i=0; i< cnNumbers.length; i++) {
			textNumberMap.put(cnNumbers[i], String.valueOf(i));
			numberTextMap.put(i, cnNumbers[i]);
		}
	}
	
	/**
	 * 将中文时间转换为数字时间，如一时十一分
	 * @param hmCn
	 * @return
	 */
	public static String convertChineseHMToNum(String hmCn) {
		if (StringUtils.isBlank(hmCn)) {
			return hmCn;
		}
		String[] hmArray = hmCn.split("时");
		String numHm = "";
		for (int i = 0; i < hmArray.length; i++) {
			if (i == 0) {
				numHm = convertChineseToNumber(hmArray[i]);
			} else {
				numHm += ":" + convertChineseToNumber(hmArray[i].substring(0, hmArray[i].length() - 1));
			}
		}
		return numHm;
	}

	/**
	 * 将100以内的中文数字转成两位阿拉伯数字
	 * @param numberCn 中文数字，主要如一，十，十一，二十，二十一之类
	 * @return
	 */
	public static String convertChineseToNumber(String numberCn) {
		if(StringUtils.isEmpty(numberCn)) {
			return numberCn;
		}
		int tenInd = numberCn.indexOf("十");
		if(tenInd == -1) {
			// 说明是小于10的数
			if(numberCn.length() > 1) {
				throw new RuntimeException("小于10的数应该只有一位！");
			}
			return "0" + textNumberMap.get(numberCn);
		}
		if(tenInd == 0) {
			// 要区分十和十一两类情况
			String suf = (numberCn.length() > 1) ? numberCn.substring(1) : "零";
			return "1" + textNumberMap.get(suf);
		}
		// 处理二十和二十一两类情况
		String pre = numberCn.substring(0, tenInd);
		String suf = numberCn.length() > 2 ? numberCn.substring(tenInd + 1) : "零";

		return textNumberMap.get(pre) + textNumberMap.get(suf);
	}
	
	/**
	 * 与 {@link #convertChineseToNumber(String)}相反，把数字转成汉字
	 * @param value 
	 * @return
	 */
	public static String convertNumberToChinese(int value,boolean isHour){
		if(value < 0 || value > 99){
			return String.valueOf(value);
		}
		//两位数字（小于10的十位为0）转汉字
		if(value == 0) {
			if(isHour)
				return "零";
			else {
				return "零零";
			}
		}
		StringBuilder sbud = new StringBuilder();
		// 十位数
		if(value >= 10) {
			// 大于等于十的数,1x为十x，2x为二十x
			if(value >= 20) {
				sbud.append(numberTextMap.get(value/10));
			}
			sbud.append("十");
		}else{
			if(!isHour)
				sbud.append("零");
		}
		// 个位数
		if(value % 10 != 0) {
			sbud.append(numberTextMap.get(value % 10));
		}
		return sbud.toString();
	}	
	
	/**
	 * 判断是否为数字(整数或小数)
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		if(StringUtils.isBlank(str)){
			return false;
		}
		
		String reg = "\\d+(\\.\\d+)?";
		
		if(str.matches(reg)){
			return true;
		}else {   //添加血压类型判断
			return str.matches("\\d+(\\/\\d+)?");
		}
			
	}
	
	private NumberUtil(){}
	
}
