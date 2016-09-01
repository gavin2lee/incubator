package com.lachesis.mnisqm.core.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lachesis.mnisqm.core.CommRuntimeException;

public class DateUtils {
	
	public static String YMD = "yyyy-MM-dd";
	
	/**
	 * 日期转换
	 * @param date
	 * @param format
	 * @return
	 */
	public static String format(Date date,String format){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	
	/**
	 * 日期
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date parseDate(String date,String format){
		DateFormat df = new SimpleDateFormat(format);
		Date out = null;
		try {
			out = df.parse(date);
		} catch (ParseException e) {
			throw new CommRuntimeException("日期格式错误");
		}
		return out;
	}
	
	public enum Menu{
		week1("周一",1),
		week2("周二",2),
		week3("周三",3),
		week4("周四",4),
		week5("周五",5),
		week6("周六",6),
		week7("周日",7);
		private String name;
		private int index;
		
		Menu(String name,int index){
			this.name = name;
			this.index = index;
		}
		
		// 普通方法
        public static String getName(int index) {
            for (Menu c : Menu.values()) {
                if (c.getIndex() == index) {
                    return c.name;
                }
            }
            return null;
        }


		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
}
