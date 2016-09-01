package com.anyi.report.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos;

import com.lachesis.mnis.core.util.PropertiesUtils;

public class ReportUtil {

	/**
	 * 判断是否包含有汉字
	 * @param str
	 * @return
	 */
	public static boolean isChineseChar(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
        Matcher m=p.matcher(str); 
        if(m.find()){ 
            temp =  true;
        }
        return temp;
    }	


    /** 
     * 获得指定日期的前一天 
     *  
     * @param specifiedDay 
     * @return 
     * @throws Exception 
     */  
    public static String getSpecifiedDayBefore(String specifiedDay,String datetype) {//可以用new Date().toLocalString()传递参数  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat(datetype).parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
        String dayBefore = new SimpleDateFormat(datetype).format(c.getTime());  
        return dayBefore;  
    }  

    /** 
     * 获得指定日期的后一天 
     *  
     * @param specifiedDay 
     * @return 
     */  
    public static String getSpecifiedDayAfter(String specifiedDay,String datetype) {  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat(datetype).parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day + 1); 
  
        String dayAfter = new SimpleDateFormat(datetype)  .format(c.getTime());  
        return dayAfter;  
    }  	
	
    /**
     * 两个日期比较
     * @param before_date
     * @param after_date
     * @return
     */
    public static int compare_date(String before_date, String after_date) {
        
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            
        	java.util.Calendar c1=java.util.Calendar.getInstance();
        	java.util.Calendar c2=java.util.Calendar.getInstance();
        	
        	c1.setTime(df.parse(before_date));    	
        	c2.setTime(df.parse(after_date)); 
        	
        	int result=c1.compareTo(c2);        	
        	
            if (result>0) {
                return -1;
            } else if (result<0) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static long getDistanceTimes(String str1, String str2) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date one;  
        Date two;  
        long hour = 0;  
        try {  
            one = df.parse(str1);  
            two = df.parse(str2);  
            long time1 = one.getTime();  
            long time2 = two.getTime();  
            long diff ;  
            if(time1<time2) {  
                diff = time2 - time1;  
            } else {  
                diff = time1 - time2;  
            }  
            hour = (diff / (60 * 60 * 1000));  
           
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        return hour;  
    }   
    
    /**
     * 根据时间计算区间
     * @param time
     * @return
     */
    public static String caclTimeRegion(String time,String beforeTime,String afterTime){
        if(time.compareTo(beforeTime) > 0 && time.compareTo(afterTime) <= 0){
        	return afterTime;
        }
        
        return beforeTime;
    }
    

    /**
     * 根据时间计算区间
     * @param time 0 (0-7):1(7-15):2(15-00)
     * @return
     */
    public static int caclRegion(String time,String beforeTime,String afterTime){
        if(time.compareTo(beforeTime) > 0 && time.compareTo(afterTime) <= 0){
        	return 1;
        }else if (time.compareTo(beforeTime)<=0){
        	return 0;
        }else {
        	return 2;
        }
    }
    
    
    public static double StringToDouble(String str){
    	if(StringUtils.isBlank(str)){
    		str = "0";
    	}
    	return Double.valueOf(str);
    }
    
  public static void main(String [] arg){
	  //System.out.println(getDistanceTimes("2015-12-07 07:00:00", "2015-12-06 08:00:00"));
	  //System.out.println(compare_date("2015-11-26 12:58:00", "2015-11-27 07:00:00"));
	  System.out.println(compare_date("2015-11-27 12:58:00", "2015-11-27 07:00:00"));
  }
    
}
