package com.lachesis.mnisqm.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期、时间工具类
 *
 * @author Paul Xu.
 * @since 1.0
 */
public class DateTimeUtils {
	public static String YY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    private static final Logger LOG = LoggerFactory.getLogger(DateTimeUtils.class);

    private static DateTimeZone utcZone = DateTimeZone.UTC;
    private static Map<String, SimpleDateFormat> dateFormats = new HashMap<>();

    public static final long MILISECONDS_IN_A_DAY = 1000 * 60 * 60 * 24;

    /**
     * Trim hour-minute-second of date instance value to zero.
     *
     * @param value
     * @return
     */
    public static Date trimHMSOfDate(Date value) {
        return new LocalDate(value).toDate();
    }

    public static Date getCurrentDateWithoutMS() {
        return new LocalDate().toDate();
    }

    public static Date convertDateByString(String strDate, String format) {
        if (!StringUtils.isBlank(strDate)) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(format);
                return formatter.parse(strDate);
            } catch (ParseException e) {
                LOG.error("Error while parse date", e);
            }
        }
        return new Date();
    }

    public static String converToStringWithUserTimeZone(String dateVal, String dateFormat, TimeZone userTimeZone) {
        Date date = parseDateByW3C(dateVal);
        return converToStringWithUserTimeZone(date, dateFormat, userTimeZone);
    }

    /**
     * @param strDate
     * @return
     */
    public static Date parseDateByW3C(String strDate) {
        String formatW3C = "yyyy-MM-dd'T'HH:mm:ss";
        if (strDate != null && !strDate.equals("")) {
            SimpleDateFormat formatter = new SimpleDateFormat(formatW3C);
            try {
                return formatter.parse(strDate);
            } catch (ParseException e) {
                LOG.error("Error while parse date", e);
            }
        }
        return null;
    }

    public static String converToStringWithUserTimeZone(Date date, String dateFormat, TimeZone userTimeZone) {
        if (date == null)
            return "";
        return formatDate(date, dateFormat, userTimeZone);
    }

    /**
     * @param date
     * @param duration Example: Date date = subtractOrAddDayDuration(new Date(), -2);
     *                 // common: the last 2 days
     *
     *                 Date date = subtractOrAddDayDuration(new Date(), 2); //
     *                 common: the next 2 days
     * @return
     */
    public static Date subtractOrAddDayDuration(Date date, int duration) {
        LocalDate localDate = new LocalDate(date);
        return localDate.plusDays(duration).toDate();
    }

    public static String formatDate(Date date, String dateFormat) {
        return formatDate(date, dateFormat, null);
    }

    public static String formatDate(Date date, String dateFormat, TimeZone timezone) {
        if (date == null) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = getDateFormat(dateFormat);
        if (timezone != null) {
            simpleDateFormat.setTimeZone(timezone);
        }

        return simpleDateFormat.format(date);
    }

    private static SimpleDateFormat getDateFormat(String format) {
        SimpleDateFormat dateFormat = dateFormats.get(format);
        if (dateFormat != null) {
            return dateFormat;
        } else {
            dateFormat = new SimpleDateFormat(format);
            dateFormats.put(format, dateFormat);
            return dateFormat;
        }
    }

    /**
     * @param date
     * @return
     */
    public static Date convertDateTimeToUTC(Date date) {
        return convertDateTimeByTimezone(date, DateTimeZone.getDefault().toTimeZone());
    }

    public static Date convertDateTimeByTimezone(Date date, TimeZone timeZone) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toDateTime(DateTimeZone.forTimeZone(timeZone)).toLocalDateTime().toDate();
    }

    /**
     * Convert from UTC time to default time zone of system
     *
     * @param timeInMillis
     * @return
     */
    public static Date convertTimeFromUTCToSystemTimezone(long timeInMillis) {
        DateTime dt = new DateTime();
        dt = dt.withMillis(DateTimeZone.getDefault().getOffset(timeInMillis) + timeInMillis);
        dt = dt.withZone(utcZone);
        return dt.toDate();
    }

    /**
     * @param date
     * @return array of two date elements, first is the first day of week, and
     * the second is the end week date
     */
    public static Date[] getBounceDateofWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date begin = calendar.getTime();

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date end = calendar.getTime();
        return new Date[]{begin, end};
    }

    public static LocalDate min(LocalDate... values) {
        LocalDate minVal = values[0];
        for (int i = 1; i < values.length; i++) {
            if (minVal.isAfter(values[i])) {
                minVal = values[i];
            }
        }
        return minVal;
    }

    public static LocalDate max(LocalDate... values) {
        LocalDate max = values[0];
        for (int i = 1; i < values.length; i++) {
            if (max.isBefore(values[i])) {
                max = values[i];
            }
        }
        return max;
    }
    
    public static int getYear(Date date){
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	return c.get(Calendar.YEAR);
    }

	public static String getMonthDay(Date date) {
		String p = "MM-dd";
		SimpleDateFormat format = new SimpleDateFormat(p);
		return format.format(date);
	}
	
	/**
	 * 获取指定月的天数
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getSumDatOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		}else if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}else{
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
	}
	
	
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}
	
	/**
	 * 将字符串时间转换为日期
	 * @param time 日期时间
	 * @param format 格式：如 yyyy-MM-dd HH:mm:ss
	 * 					   yyyy-MM-dd
	 * @return
	 */
	public static Date toDate(String time, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * 获取指定日期前、后月的日期，如得到指定时间的3个月前的日期，则month传入 -3
	 * @param date 指定日期
	 * @param month 变化月份
	 * @return
	 */
	public static Date getDateByMonth(Date date,int month){
		Date resultDate = new Date();
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(calendar.MONTH, month); 
		resultDate = calendar.getTime(); 
		return resultDate;
	}
	
	/**
	 * 获取指定日期前、后天数的日期，如得到指定时间的前30天的日期，则day传入 -30
	 * @param date 指定日期
	 * @param day 变化天数
	 * @return
	 */
	public static Date getDateByDay(Date date, int day){
		Date resultDate = new Date();
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
		calendar.add(calendar.DATE, day); 
		resultDate = calendar.getTime(); 
		return resultDate;
	}
	
	/**
	 * 获取指定日期前、后分钟数的日期，如得到指定时间的前10分钟的日期，则day传入 -10
	 * @param date 指定日期
	 * @param minute 变化分钟
	 * @return
	 */
    public static Date getDateByMinute(Date date, int minute) {
    	Date resultDate = new Date();
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(date);
    	calendar.add(Calendar.MINUTE, minute);
    	resultDate = calendar.getTime(); 
    	return resultDate;
    }
}
