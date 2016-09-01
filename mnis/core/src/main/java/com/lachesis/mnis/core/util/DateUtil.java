package com.lachesis.mnis.core.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/***
 * 
 * 日期相关工具类
 *
 * @ClassName: DateUtil
 * @author yuliang.xu
 * @date 2015年4月30日 下午6:08:09
 *
 * @see org.apache.commons.lang.time.DateUtils
 * @see org.apache.commons.lang.time.DateFormatUtils
 */
public class DateUtil {

	private static final Logger LOG = LoggerFactory.getLogger(DateUtil.class);

	private static final Pattern ymdPattern = Pattern
			.compile("\\d{4}-\\d{2}-\\d{2}");

	/** 一周的天数 */
	public static final int DAY_PER_WEEK = Calendar.DAY_OF_WEEK;
	/** 一月的天数 */
	public static final int DAY_PER_MONTH = 30;
	/** 一年的天数 */
	public static final int DAY_PER_YEAR = 365;
	/** 起始日期时间格式 */
	public static final String START_MIN_MS = "00:00";
	public static final String START_MIN_TIMES = ":00:00";
	public static final String START_TIMES = " 00" + START_MIN_TIMES;
	/** 结束日期时间格式 */
	public static final String END_MIN_TIMES = ":59:59";
	public static final String END_TIMES = " 23" + END_MIN_TIMES;
	
	public static final String[] weeks = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };

	/** format : yyyy-MM-dd HH:mm:ss */
	public static final java.text.DateFormat FORMAT_FULL = new SimpleDateFormat(
			DateFormat.FULL.getFormatName());
	/** format : yyyy-MM-dd */
	public static final java.text.DateFormat FORMAT_YMD = new SimpleDateFormat(
			DateFormat.YMD.getFormatName());

	/***
	 * 格式化时间到指定的格式
	 * 
	 * @param date
	 * @param format
	 * @return String date string
	 */
	public static String format(Date date, DateFormat format) {
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, format.getFormatName());
	}

	/***
	 * 格式化时间到指定的格式
	 * 
	 * @param timeMillis
	 *            时间毫秒数
	 * @param format
	 * @return String date string
	 */
	public static String format(long timeMillis, DateFormat format) {
		return format(new Date(timeMillis), format);
	}

	/***
	 * 将时间类型的字符串转换为format格式的字符串
	 * 
	 * @param date
	 * @param format
	 * @return String date string
	 */
	public static String format(String date, DateFormat oldFormat,
			DateFormat newFormat) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		return format(parse(date, oldFormat), newFormat);
	}

	/***
	 * 格式化当前时间到指定的格式
	 * 
	 * @param format
	 * @return String date string
	 */
	public static String format(DateFormat format) {
		return format(new Date(), format);
	}

	/***
	 * 格式化当前时间为 yyyy-MM-dd HH:mm:ss 的格式
	 * 
	 * @return String date string
	 */
	public static String format() {
		return format(DateFormat.FULL);
	}

	/***
	 * 格式化字符串为yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss的时间
	 * 
	 * @param date
	 *            yyyy-MM-dd 或 yyyy-MM-dd HH:mm:ss 格式的字符串
	 * @return Date 时间
	 */
	public static Date parse(String date) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		return parse(
				date,
				new String[] { DateFormat.FULL.getFormatName(),
						DateFormat.YMD.getFormatName(),
						DateFormat.YMDHM.getFormatName(),
						DateFormat.HM.getFormatName(),
						DateFormat.HMS.getFormatName(), });
	}

	/***
	 * 格式化字符串到指定格式的时间
	 * 
	 * @param date
	 * @param format
	 * @return Date 时间
	 */
	public static Date parse(String date, DateFormat format) {
		if (StringUtils.isBlank(date)) {
			return null;
		}
		return parse(date, new String[] { format.getFormatName() });
	}

	private static Date parse(String date, String[] parsePatterns) {
		try {
			return DateUtils.parseDate(date, parsePatterns);
		} catch (ParseException e) {
			LOG.error("String parse to Date error !", e);
			return null;
		}
	}

	/***
	 * 当前天最小时间点 如： 2015-05-04 00:00:00
	 * 
	 * @return Date 当前天最小时间点
	 */
	public static Date getCurDateWithMinTime() {
		return getDateWithMinTime((Date) null);
	}

	/***
	 * 获取某一天最小时间点 如： 2015-05-04 00:00:00
	 * 
	 * @param date
	 *            2015-05-04
	 * @return Date 获取某一天最小时间点
	 */
	public static Date getDateWithMinTime(Date date) {
		return getDateWithMinTime(date, 0);
	}

	public static Date getDateWithMinTime(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/***
	 * 获取某一天最小时间点 如： 2015-05-04 00:00:00
	 * 
	 * @param date
	 *            2015-05-04
	 * @return Date 获取某一天最小时间点
	 */
	public static Date getDateWithMinTime(String date) {
		return getDateWithMinTime(parse(date));
	}

	/***
	 * 当前天最大时间点 如： 2015-05-04 23:59:59
	 * 
	 * @return Date 当前天最大时间点
	 */
	public static Date getCurDateWithMaxTime() {
		return getDateWithMaxTime((Date) null);
	}

	/***
	 * 获取某一天最大时间点 如： 2015-05-04 00:00:00
	 * 
	 * @param date
	 *            2015-05-04
	 * @return Date 获取某一天最大时间点
	 */
	public static Date getDateWithMaxTime(Date date) {
		return getDateWithMaxTime(date, 23);
	}

	public static Date getDateWithMaxTime(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}

	/***
	 * 获取某一天最大时间点 如： 2015-05-04 00:00:00
	 * 
	 * @param date
	 *            2015-05-04
	 * @return Date 获取某一天最大时间点
	 */
	public static Date getDateWithMaxTime(String date) {
		return getDateWithMaxTime(parse(date));
	}

	/**
	 * 截取指定位数长度的时间
	 * 
	 * @param fullDateStr
	 * @return
	 */
	public static String truncateDateString(String date, DateFormat format) {
		if (StringUtils.isBlank(date)) {
			return null;
		}

		int length = format.getFormatName().length();
		return date.length() >= length ? date.substring(0, length) : date;
	}

	/**
	 * 计算两个日期之间相差多少天
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static int calDatePoor(Date startDate, Date endDate) {
		if (endDate == null || startDate == null) {
			return 0;
		}
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - startDate.getTime();

		return (int) (diff / DateUtils.MILLIS_PER_DAY);
	}

	/***
	 * 将时间的时、分、秒设置为0
	 *
	 * @param date
	 * @return
	 */
	public static Date setDateToDay(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取下一天的零点时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date setNextDayToDate(Date date) {
		Calendar c = Calendar.getInstance();
		if (date != null) {
			c.setTime(date);
		}
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 计算两个日期之间相差多少天
	 * 
	 * @param endDate
	 * @param startDate
	 * @return
	 */
	public static int calDatePoor(String startDate, String endDate) {
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			return 0;
		}

		return calDatePoor(parse(startDate, DateFormat.YMD),
				parse(endDate, DateFormat.YMD));
	}

	/**
	 * 获取一天的0点和23点59分的时间 如果传入null，则两个端点时间都为null
	 * 
	 * @param day
	 *            : yyyy-MM-dd
	 * @return
	 */
	public static String[] getTimeEndPoints(String day) {
		String[] endPoints = new String[2];
		if (day != null && ymdPattern.matcher(day).matches()) {
			endPoints[0] = day + " 00:00:00";
			endPoints[1] = day + " 23:59:59";
		}
		return endPoints;
	}

	/**
	 * 字符串转为中文日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getChineseHourMinute(String date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse(date));
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		StringBuilder sb = new StringBuilder();
		sb.append(NumberUtil.convertNumberToChinese(hour,true));

		sb.append("时");
		sb.append(NumberUtil.convertNumberToChinese(minute,false));
		sb.append("分");

		return sb.toString();
	}
	
	/**
	 * 字符串转为中文日期
	 * 
	 * @param date
	 * @param bool true:12小时制
	 * @return
	 */
	public static String getChineseHourMinute(String date,boolean bool) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parse(date));
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		if(bool){
			if(hour>12){
				hour = hour-12;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(NumberUtil.convertNumberToChinese(hour,true));

		sb.append("时");
		//如果分钟为0的时候，不计算分
		if(minute!=0){
			sb.append(NumberUtil.convertNumberToChinese(minute,false));
			sb.append("分");
		}

		return sb.toString();
	}
	
	/**
	 * Date转为中文日期
	 * 
	 * @param date
	 * @return
	 */
	public static String getChineseHourMinute(Date date,boolean bool) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		if(bool){
			if(hour>12){
				hour = hour-12;
			}
		}
		StringBuilder sb = new StringBuilder();
		sb.append(NumberUtil.convertNumberToChinese(hour,true));

		sb.append("时");
		sb.append(NumberUtil.convertNumberToChinese(minute,false));
		sb.append("分");

		return sb.toString();
	}

	public static Date clearMilliSecond(Date date) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 比较两个日期，startDate早于endDate返回true，反之,false
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static boolean before(Date startDate, Date endDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		long startL = cal.getTimeInMillis();
		cal.setTime(endDate);
		long endL = cal.getTimeInMillis();

		if (startL >= endL) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 比较两个字符串类型的日期，其中一个是否早于另外一个
	 * @param startDate 第一个日期
	 * @param endDate	第二个日期
	 * @param format	日期格式
     * @return 如果第一个日期早于第二个返回true，否则返回false
     */
    public static boolean before(String startDate, String endDate, DateFormat format){
		Date sDate = parse(startDate, format);
		Date eDate = parse(endDate, format);
		if(null==sDate || null==eDate){
			return false;
		}else {
			return before(sDate, eDate);
		}
	}

	/**
	 * 比较两个字符串类型的日期，其中一个是否晚于另外一个
	 * @param startDate 第一个日期
	 * @param endDate	第二个日期
	 * @param format	日期格式
	 * @return 如果第一个日期晚于第二个返回true，否则返回false
	 */
	public static boolean after(String startDate, String endDate, DateFormat format){
		Date sDate = parse(startDate, format);
		Date eDate = parse(endDate, format);
		if(null==sDate || null==eDate){
			return false;
		}else {
			return sDate.after(eDate);
		}
	}

	/**
	 * 获取查询日期区间：当前零点和下一天零点
	 * @param curDate
	 * @return
	 */
	public static Date[] getQueryRegionDates(Date curDate) {
		Date[] dates = new Date[2];
		if (curDate == null) {
			curDate = new Date();
		}

		dates[0] = setDateToDay(curDate);
		dates[1] = setNextDayToDate(curDate);
		return dates;
	}
	
	/**
	 * 当前时间更新指定时间
	 * @param curDate
	 * @param hourCount
	 * @return
	 */
	public static Date getDateByHour(Date curDate,int hourCount){
		Calendar c = Calendar.getInstance();
		if (curDate == null) {
			c.setTime(new Date());
		}else{
			c.setTime(curDate);
		}
		c.add(Calendar.HOUR_OF_DAY, hourCount);
		return c.getTime();
		
	}
	
	/**
	 * 获取当前时间的前后指定小时区间
	 * @param curDate
	 * @return
	 */
	public static Date[] getQueryHourRegionDates(Date curDate,int startHourCount,int endHourCount){
		Date[] dates = new Date[2];
		if (curDate == null) {
			curDate = new Date();
		}

		dates[0] = getDateByHour(curDate,startHourCount);
		dates[1] = getDateByHour(curDate,endHourCount);
		return dates;
	}
	
	/**
	 * 给日期指定小时
	 * @param curDate
	 * @param hour
	 * @return
	 */
	public static Date setAssignTime(Date curDate,int hour){
		if (curDate == null) {
			curDate = new Date();
		}
		
		Calendar c = Calendar.getInstance();
		c.setTime(curDate);
		
		c.set(Calendar.HOUR_OF_DAY, hour);
		
		return c.getTime();
	}
	
	private DateUtil() {
	}

	public enum DateFormat {
		YMDHMSS("yyyy-MM-dd HH:mm:ss:sss"), YMD("yyyy-MM-dd"), // e.g.
																// 2012-11-01
		FULL("yyyy-MM-dd HH:mm:ss"), // e.g. 2012-11-01 23:12:15(24小时制)
		YMDHM("yyyy-MM-dd HH:mm"), // e.g. 2012-11-01 23:12(24小时制)
		HMS("HH:mm:ss"), HM("HH:mm"),YMDC("yyyy年MM月dd日"),
		MS("mm:ss");//分秒

		private String format;

		private DateFormat(String format) {
			this.format = format;
		}

		public String getFormatName() {
			return format;
		}
	}
	/**
	 * 获取时间制定类型的时，分，秒
	 * @param time
	 * @param type 
	 * @return
	 */
	public static int getFixTypeFromTime(String time,int type){
		Calendar c = Calendar.getInstance();
		Date date = parse(time);
		c.setTime(date);
		return c.get(type);
	}
	
	/**
	 * 根据日期取得星期几
	 * @param date
	 * @return
	 */
	public static String getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return weeks[week_index];
	}
	
	public static int getHour(String dateStr){
		Date date = parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		return hour;
	}


	static int[] MAXDAY = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	/**
	 * 是否合法的日期时间，字符串格式为yyyy-MM-dd HH:mm:ss
	 * @param date
	 * @return
	 */
	public static boolean isValidDateTime(String date) {
		try {
			int year = Integer.parseInt(date.substring(0, 4));
			if (year <= 0)
				return false;
			int month = Integer.parseInt(date.substring(5, 7));
			if (month <= 0 || month > 12)
				return false;
			int day = Integer.parseInt(date.substring(8, 10));
			if (day <= 0 || day > MAXDAY[month])
				return false;
			if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
				return false;
			}
			int hour = Integer.parseInt(date.substring(11, 13));
			if (hour < 0 || hour > 23)
				return false;
			int minute = Integer.parseInt(date.substring(14, 16));
			if (minute < 0 || minute > 59)
				return false;
			int second = Integer.parseInt(date.substring(17, 19));
			if (second < 0 || second > 59)
				return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 日期是否合法，格式为yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static boolean isValidDate(String date) {
		try {
			int year = Integer.parseInt(date.substring(0, 4));
			if (year <= 0)
				return false;
			int month = Integer.parseInt(date.substring(5, 7));
			if (month <= 0 || month > 12)
				return false;
			int day = Integer.parseInt(date.substring(8, 10));
			if (day <= 0 || day > MAXDAY[month])
				return false;
			if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 时间是否合法，格式为HH:mm:ss
	 * @param time
	 * @return
	 */
	public static boolean isValidTime(String time) {
		try {
			if(8 != time.length()){
				return false;
			}
			int hour = Integer.parseInt(time.substring(0, 2));
			if (hour < 0 || hour > 23)
				return false;
			int minute = Integer.parseInt(time.substring(3, 5));
			if (minute < 0 || minute > 59)
				return false;
			int second = Integer.parseInt(time.substring(6, 8));
			if (second < 0 || second > 59)
				return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	/**
	 * 是否闰年
	 * @param year
	 * @return
	 */
	public static final boolean isGregorianLeapYear(int year) {
		return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
	}

	public static int diffDate(Date date1,Date date2){
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		
		return (int)(cal2.getTimeInMillis()-cal1.getTimeInMillis())/(1000*60*60*24);
	}
	
	public static void main(String[] args){
		String performSchedule = "[中心]1111";
		String test ="";
		String performSchedule1 = "[中心][234]1111";
		String performSchedule2 = "1111[444]";
		if(StringUtils.isNotBlank(performSchedule)){
			test = performSchedule.substring(performSchedule.indexOf("["),StringUtils.lastIndexOf(performSchedule, "]")+1);
		}
		System.out.println(test);
		test = performSchedule1.substring(performSchedule1.indexOf("["),StringUtils.lastIndexOf(performSchedule1, "]")+1);
		System.out.println(test);
		test = performSchedule2.substring(performSchedule2.indexOf("["),StringUtils.lastIndexOf(performSchedule2, "]")+1);
		System.out.println(test);
		
		Date date1 = DateUtil.parse("2016-05-04");
		Date date2 = DateUtil.parse("2016-05-05");
		System.err.println(DateUtil.diffDate(date1, date2));
	}
	
	/**
	 * 获取小时相加
	 * @param date
	 * @param hour
	 * @return
	 */
	public static String addHour(String date,int hour){
		Date modifyDate = DateUtil.parse(date);
		Calendar c = Calendar.getInstance();
		c.setTime(modifyDate);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.HOUR, hour);
		return DateUtil.format( c.getTime(), DateFormat.FULL);
	}
	
	/**
	 * +-日期
	 * @param date
	 * @param hour
	 * @return
	 */
	public static String addDate(String date,int day){
		Date modifyDate = DateUtil.parse(date);
		Calendar c = Calendar.getInstance();
		c.setTime(modifyDate);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.add(Calendar.DATE, day);
		return DateUtil.format( c.getTime(), DateFormat.YMD);
	}
	
}
