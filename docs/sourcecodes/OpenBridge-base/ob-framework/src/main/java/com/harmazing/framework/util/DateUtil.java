package com.harmazing.framework.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public abstract class DateUtil {
	public static final long Second = 1000;
	public static final long Minute = 60 * Second;
	public static final long HalfHour = 30 * Minute;
	public static final long Hour = 60 * Minute;
	public static final long Day = 24 * Hour;

	public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String YYYYMMDD_HHMMSS = "yyyyMMdd HHmmss";
	public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
	public static final String YYMMDD = "yyMMdd";
	public static final String HHMMSS = "HHmmss";
	public static final String YYYYMMDD = "yyyyMMdd";
	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static Date parse(String str, String format) {
		if (StringUtil.isEmpty(str)) {
			return null;
		}
		String tmpFormat = format;
		if (StringUtil.isEmpty(tmpFormat)) {
			tmpFormat = YYYY_MM_DD_HH_MM_SS;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(tmpFormat);
		try {
			return simpleDateFormat.parse(str);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return "";
		}
		String tmpFormat = format;
		if (StringUtil.isEmpty(tmpFormat)) {
			tmpFormat = YYYY_MM_DD_HH_MM_SS;
		}

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(tmpFormat);
		return simpleDateFormat.format(date);
	}

	/**
	 * 获得指定日期的指定前 或者后天 +1 后 一天 -1 前一天
	 *
	 * @param specifiedDay
	 * @return
	 * @throws Exception
	 */
	public static Date getSpecifiedDay(Date date, int offset) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + offset);
		return c.getTime();
	}

	/**
	 * 得到某月最后一天的结束时间
	 * 
	 * @param offYear
	 * @param offMonth
	 * @return
	 */
	public static Date getLastDayOfMonth(int offYear, int offMonth) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		c.set(Calendar.YEAR, offYear);
		c.set(Calendar.MONTH, offMonth);
		c.add(Calendar.DAY_OF_YEAR, -1);
		// 月加1，日减1 得到当前月最后1天
		return c.getTime();
	}

	/**
	 * 得到某月第一天的开始时间
	 * 
	 * @param offYear
	 * @param offMonth
	 * @return
	 */
	public static Date getFirstDayOfMonth(int offYear, int offMonth) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.set(Calendar.YEAR, offYear);
		c.set(Calendar.MONTH, offMonth - 1);
		return c.getTime();
	}
	/**
	 * 把以秒表示的时长，转换为带固定格式展示。工作日
	 * @author chenjinfan
	 * @Description
	 * @param time
	 * @return
	 */
	public static String showDueSeconds(Integer time) {
		String[] units = new String[]{"个月","天","小时","分","秒"};
		int[] standards = new int[]{30,8,60,60,1};
		String formatTime = showDueSeconds(time,standards,units);
		return formatTime;
	}
	/**
	 * 把以秒表示的时长，转换为带固定格式展示。自然日
	 * @author chenjinfan
	 * @Description
	 * @param time
	 * @param precision 精度，带几个单位
	 * @return
	 */
	public static String showDueSecondsNature(Integer time,int precision) {
		String[] units = new String[]{"月","天","小时","分","秒"};
		if(precision<=0 || precision>5)		precision=units.length;
		int[] standards = new int[]{30,24,60,60,1};
		String formatTime = showDueSeconds(time,standards,units);
		List<Integer> indexs = new ArrayList<Integer>();
		for(String unit : units){
			int i = formatTime.indexOf(unit);
			if(i>0)	indexs.add(i+unit.length());
		}
		if(indexs.size()>precision){
			Collections.sort(indexs);
			formatTime = formatTime.substring(0, indexs.get(precision-1));
		}
		return formatTime;
	}
	/**
	 * 把以秒表示的时长，转换为带单位的格式
	 * @author chenjinfan
	 * @Description
	 * @param time
	 * @param unitScale 每种单位换算为下个单位的数量，最后一个单位需换算为秒的度量，如：
	 * ["个月","天","小时","分","秒"],{30,8,60,60,1}
	 * ["个月","天","小时","秒"],{30,8,60*60,1}
	 * @param units
	 * @return
	 */
	public static String showDueSeconds(Integer time,int[] unitScale,String[] units) {
		String eta = "";
		if(time==null)	return eta;
		int[] standards = new int[unitScale.length];
		int j=1;
		for(int i=unitScale.length-1;i>=0;i--){
			j = j*unitScale[i];
			standards[i] = j;
		}
		int length = Math.min(units.length, unitScale.length);
		for(int i=0;i<length;i++){
			String unit = units[i];
			if(StringUtil.isNotNull(unit)){
				int yushu = time;
				if(i>0){
					yushu = time % (standards[i-1]);
				}
				int value = yushu / standards[i];
				if(value>0){
					eta += value + unit;
				}
			}
		}
		return eta;
	}
	public static void main(String[] args) {
		System.out.println(format(getFirstDayOfMonth(2015, 12),
				YYYY_MM_DD_HH_MM_SS_SSS));
		System.out.println(format(getLastDayOfMonth(2015, 12),
				YYYY_MM_DD_HH_MM_SS_SSS));
	}
}
