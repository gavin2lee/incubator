package com.lachesis.mnisqm.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * java生成唯一编号
 * 后续优化
 * @author Administrator
 *
 */
public class CodeUtils {
	
	private static DateFormat df = new SimpleDateFormat("YYMMDD");
	private static AtomicInteger atomic = new AtomicInteger(0);
	
	/**
	 * 获取角色编号
	 * @return
	 */
	public static String getRoleCode(){
		return df.format(new Date())+getIndex();
	}
	
	/**
	 * 获取系统员工编号
	 * @return
	 */
	public static String getUserCode(){
		return "U"+df.format(new Date())+getIndex();
	}
	
	/**
	 * 获取序号
	 * @return
	 */
	private static int getIndex(){
		if(atomic.get()==0){
			//如果为0，那么以当前的时分秒为起始
			atomic.set(getStartIndex());
		}
		if(atomic.get()>=99999){
			atomic.set(1);
		}
		return atomic.getAndIncrement();
	}
	/**
	 * 获取起始的序号
	 * @return
	 */
	private static int getStartIndex(){
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int min = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		//时分秒
		return hour*min*sec;
	} 
	
	/**
	 * 系统后台接口调用
	 * @return
	 */
	public static String getSysInvokeId(){
		return df.format(new Date())+getIndex();
	}
}