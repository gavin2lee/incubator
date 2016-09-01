package com.lachesis.mnis.core.bodysign;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lachesis.mnis.core.bodysign.BodySignConstants.BodySignStatus;
import com.lachesis.mnis.core.bodysign.entity.BodySignConfig;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.SuperCacheUtil;

public final class BodySignUtil {
	static final Logger LOGGER = LoggerFactory.getLogger(BodySignUtil.class);

	private BodySignUtil() {
	}

	/**
	 * 根据时间点获取时间区间
	 * [3,7,11,15,19,23] before
	 * [23,3,3,7,7,11,11,15,15,19,19,23]
	 * 
	 * @param hours
	 * @return
	 */
	public static List<Date> getTimePointsByHour(Date time, List<String> bodySignDataStrategy) {
		
		int[] hours = null;
		switch (Integer.valueOf(bodySignDataStrategy.get(2))) {
		case 0:
			hours = BodySignConstants.BODY_SIGN_ZERO_TIME;
			break;
		case 1:
			hours = BodySignConstants.BODY_SIGN_FIRST_TIME;
			break;
		case 2:
			hours = BodySignConstants.BODY_SIGN_SECOND_TIME;
			break;
		case 3:
			hours = BodySignConstants.BODY_SIGN_THREE_TIME;
			break;
		default:
			throw new IllegalArgumentException("体征时间点策略错误");
		}
		
		List<Date> hourPoints = new ArrayList<>(12);
		//[2, 6, 10, 14, 18, 22]
		for (int i = 0; i < hours.length; i++) {
			Date datePoint =  DateUtil.getDateWithMinTime(time, hours[i]);
//			Date end = DateUtil.getDateWithMaxTime(time , hours[i]);
			switch (Integer.valueOf(bodySignDataStrategy.get(1))) {
			case 0:
				hourPoints.add(DateUtils.addHours(datePoint, -4));
				hourPoints.add(datePoint);
				break;
			case 1:
				hourPoints.add(DateUtils.addHours(datePoint, -2));
				hourPoints.add(DateUtils.addHours(datePoint, 2));
				break;
			default:
				//AFTER
				hourPoints.add(datePoint);
				hourPoints.add(DateUtils.addHours(datePoint, 4));
				break;
			}
		}
		
		return hourPoints;
	}
	
	/***
	 * 根据时间计算当前time所处的index
	 *
	 * @param time    
	 * @param hours
	 * @param times   分割类型
	 * @return
	 * @throws
	 */
	public static int getIndexByDivideHour(Date time , List<Date> hours, int times) {
		int segIndex = -1;
		//x = 6  3   2 
		int model = hours.size() / times; // 2  4   6
		for(int i = 0; i < times; i++){
			segIndex++;
			//6      0                          1    2   3  4 5 6                  hours.get(i*2), hours.get(i*2+2-1)
			//3      0[0]   3[1]        4[0]  7[1]      8[0]  11[1]       hours.get(i*4), hours.get(i*4+4-1)
			//2      0[0]   5[1]        6[0]  11[1]								hours.get(i*6), hours.get(i*6+6-1)			
			if (isTimeInRange(time,  hours.get(i*model), hours.get((i+1)*model -1))) {
				break;
			}
		}
		
		return segIndex;
	}
	
	/***
	 * 根据时间计算当前time所处的index
	 *
	 * @param time    
	 * @param hours
	 * @param times   分割类型
	 * @return
	 * @throws
	 */
	public static int getEventIndex(Date time,String eventHours) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(time);
		int hour = ca.get(Calendar.HOUR_OF_DAY);
		int segIndex = -1;
		String[] event = eventHours.split(",");
		for(int i = 0; i < event.length-1; i++){
			segIndex++;
			if(hour>=Integer.parseInt(event[i])  && hour <Integer.parseInt(event[i+1])){
				break;
			}
		}
		return segIndex;
	}
	
	/**
	 * 判断时间位置(startRange<time<=endRange)
	 * 1.startRange不能在time之后
	 * 2.endRange不能在time之前
	 * @param time
	 * @param startRange
	 * @param endRange
	 * @return
	 */
	public static boolean isTimeInRange(Date time, Date startRange, Date endRange) {
		return (time.after(startRange) && !endRange.before(time));
	}
	
	/**
	 * 获取索引，根据记录时间
	 * @param recordDate:记录时间，格式(YYYY-MM-DD HH：mm:ss)
	 * @return int[]:返回集合  从前到后依次为 一天6次，一天3次，一天2次，一天1次
	 */
	public static int[] getIndexByrecordDate(String recordDate){
		//日期为空校验
		if(StringUtils.isEmpty(recordDate)){
			throw new MnisException("日期["+recordDate+"]格式错误,需要格式(YYYY-MM-DD HH：mm:ss)!");
		}
		//格式化日期
		Date date = null;
		try{
			date = DateUtil.parse(recordDate.trim(), DateFormat.FULL);
		}catch (Exception e) {
			throw new MnisException("日期["+recordDate+"]格式错误,需要格式(YYYY-MM-DD HH：mm:ss)!");
		}
		//日期为空校验
		if(null == date){
			throw new MnisException("记录时间为空！");
		}
		//配置表校验
		Map<String, BodySignConfig> cfgMap = SuperCacheUtil.BODY_SIGN_CONFIGS;
		if(null == cfgMap || cfgMap.isEmpty()){
			throw new MnisException("生命体征配置信息不全,缺少BODY_SIGN_CONFIGS的配置!");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);//获取小时
		BodySignConfig config = cfgMap.get(hour+"");
		if(null == config){
			throw new MnisException("生命体征配置信息不全,缺少时间点["+hour+"]的配置!");
		}
		/*
		 * 判断是否包含正点,如果有些需求是3点和3点过1分是不同的，3点之后要算在下一个时间点去
		 * 如果不包含正点，那么对于非正点的数据进行+1处理
		 */
		if(!"1".equals(config.getReserved5())){
			//如果不是整点
			String ms =DateUtil.format(date, DateFormat.MS);
			if(!DateUtil.START_MIN_MS.equals(ms)){
				hour = hour+1;
				config = cfgMap.get(hour+"");
			}
		}

		int[] rsIndex = new int[4];//数据返回
		rsIndex[0] = Integer.parseInt(config.getReserved1());//一天6次
		rsIndex[1] = Integer.parseInt(config.getReserved2());//一天3次
		rsIndex[2] = Integer.parseInt(config.getReserved3());//一天2次
		rsIndex[3] = Integer.parseInt(config.getReserved4());//一天1次
		return rsIndex;
	}
	
	/**
	 * 获取同一索引的所有时间，根据记录时间
	 * @param recordDate:记录时间，格式(YYYY-MM-DD HH：mm:ss)
	 * @return List<List<String>>[]:返回集合  从前到后依次为 一天6次，一天3次，一天2次，一天1次
	 */
	public static Map<String,List<String>> getAllDateByrecordDate(String recordDate){
		//日期为空校验
		if(StringUtils.isEmpty(recordDate)){
			throw new MnisException("日期["+recordDate+"]格式错误,需要格式(YYYY-MM-DD HH：mm:ss)!");
		}
		//格式化日期
		Date date = null;
		try{
			date = DateUtil.parse(recordDate.trim(), DateFormat.FULL);
		}catch (Exception e) {
			throw new MnisException("日期["+recordDate+"]格式错误,需要格式(YYYY-MM-DD HH：mm:ss)!");
		}
		//日期为空校验
		if(null == date){
			throw new MnisException("记录时间为空！");
		}
		//配置表校验
		Map<String, BodySignConfig> cfgMap = SuperCacheUtil.BODY_SIGN_CONFIGS;
		if(null == cfgMap || cfgMap.isEmpty()){
			throw new MnisException("生命体征配置信息不全,缺少BODY_SIGN_CONFIGS的配置!");
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);//获取小时
		BodySignConfig config = cfgMap.get(hour+"");
		if(null == config){
			throw new MnisException("生命体征配置信息不全,缺少时间点["+hour+"]的配置!");
		}
		/*
		 * 判断是否包含正点,如果有些需求是3点和3点过1分是不同的，3点之后要算在下一个时间点去
		 * 如果不包含正点，那么对于非正点的数据进行+1处理
		 */
		if(!"1".equals(config.getReserved5())){
			//如果不是整点
			String ms =DateUtil.format(date, DateFormat.MS);
			if(!DateUtil.START_MIN_MS.equals(ms)){
				hour = hour+1;//如果存在超过23的，那么请自行配置一个24的参数进去,以确定23点之后的一个小时是属于哪一index
				config = cfgMap.get(hour+"");
			}
		}
		//每一类型的所有的记录时间
		List<String> ones = new ArrayList<String>();
		List<String> twos = new ArrayList<String>();
		List<String> threes = new ArrayList<String>();
		List<String> sixs = new ArrayList<String>();
		//索引
		int oneIndex = Integer.parseInt(config.getReserved4());//一天1次
		int twoIndex = Integer.parseInt(config.getReserved3());//一天2次
		int threeIndex = Integer.parseInt(config.getReserved2());//一天3次
		int sixIndex = Integer.parseInt(config.getReserved1());//一天6次
		Set<String> hours = new HashSet<String>();
		hours.add("0");//0点，一天一条的记录
		for(String h : SuperCacheUtil.getSYSTEM_CONFIGS().get("vs_batch_timepoints").split(",")){
			hours.add(h);
		}
		//记录点上的时间
		List<String> hourList = new ArrayList<String>();
		hourList.addAll(hours);
		Map<String,String> dateMap = getTimeByHours(recordDate, hourList);
		
		//获取时间点
		for(Iterator<String> iter = cfgMap.keySet().iterator();iter.hasNext();){
			BodySignConfig cfg = cfgMap.get(iter.next());
			//不在整点内，那么下一条
			if("0".equals(cfg.getValue()) || !hours.contains(cfg.getValue())){
				continue;
			}
			//正点的记录时间
			String fullDate = dateMap.get(cfg.getValue());
			//一天6次
			if(sixIndex == Integer.parseInt(cfg.getReserved1().trim())){
				sixs.add(fullDate);
			}
			//一天3次
			if(threeIndex == Integer.parseInt(cfg.getReserved2().trim())){
				threes.add(fullDate);
			}
			//一天2次
			if(twoIndex == Integer.parseInt(cfg.getReserved3().trim())){
				twos.add(fullDate);
			}
			//一天1次
			if(oneIndex == Integer.parseInt(cfg.getReserved4().trim())){
				ones.add(fullDate);
			}
		}
		//一天一次的，补上0点的记录
		ones.add(dateMap.get("0"));
		//数据返回
		Map<String,List<String>> rsMap = new HashMap<String, List<String>>();
		rsMap.put("sixs", sixs);
		rsMap.put("threes", threes);
		rsMap.put("twos", twos);
		rsMap.put("ones", ones);
		return rsMap;
	}
	
	/**
	 * 根据小时点，获取所有的正点时间
	 * @param date :日期
	 * @param hours:小时
	 * @return : 返回小时对应的时间点
	 * @return
	 */
	public static Map<String,String> getTimeByHours(String date,List<String> hours){
		Date fullDate = DateUtil.parse(date, DateFormat.FULL) ;
		Calendar cal = Calendar.getInstance();
		cal.setTime(fullDate);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Map<String,String> rsMap = new HashMap<String,String>();
		if(null != hours){
			for(String hour: hours){
				cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
				rsMap.put(String.valueOf(hour), DateUtil.format(cal.getTime(),DateFormat.FULL));
			}
		}
		return rsMap;
	}
	
	/**
	 * 获取一天一次的体征项目
	 * @return
	 */
	public static List<String> getOncePerItemCodes(){
		String oncePerItemCodeString = SuperCacheUtil.getSYSTEM_CONFIGS().get("oncePerDayItems");
		List<String> oncePerItemCodeList = new ArrayList<String>();
		if(StringUtils.isNotBlank(oncePerItemCodeString)){
			Collections.addAll(oncePerItemCodeList, oncePerItemCodeString.split(MnisConstants.COMMA));
		}else{
			 oncePerItemCodeList = BodySignConstants.ONCE_PER_DAY_ITEMS;
		}
		
		return oncePerItemCodeList;

	}
	
	/**
	 * 获取手术罗马数字
	 * @param days
	 * @return
	 */
	public static String getSSDay(String days){
		Map<String,String> map = new HashMap<String,String>();
		//map.put("1", "I");
		map.put("2", "II");
		map.put("3", "III");
		map.put("4", "IV");
		map.put("5", "V");
		map.put("6", "VI");
		map.put("7", "VII");
		map.put("8", "VIII");
		map.put("9", "IX");
		map.put("10", "X");
		return map.get(days);
	}
	
	/**
	 * 拆分记录，把0点的归为一条
	 * @return
	 */
	public static List<BodySignRecord> splitRecord(BodySignRecord record,String hourType,String eventHours){
		
		BodySignRecord zeroRecord = new BodySignRecord();
		zeroRecord.setModifyNurseCode(record.getModifyNurseCode());
		zeroRecord.setModifyNurseName(record.getModifyNurseName());
		//主记录中创建人信息
		zeroRecord.setRecordNurseCode(record.getRecordNurseCode());
		zeroRecord.setRecordNurseName(record.getRecordNurseName());
		Date zeroDate = DateUtil.parse(record.getRecordDay() + " " + "00:00:00",DateFormat.FULL);//零点
		zeroRecord.setFullDateTime(zeroDate);
		zeroRecord.setPatientId(record.getPatientId());
		
		//明细item校验
		if(null != record.getBodySignItemList()){
			List<BodySignItem> items = new ArrayList<BodySignItem>();
			List<BodySignItem> zeroItems = new ArrayList<BodySignItem>();
			for(BodySignItem item : record.getBodySignItemList()){
				//空明细项目
				if(null == item.getBodySignDict() || StringUtils.isEmpty(item.getBodySignDict().getItemCode())){
					throw new MnisException("患者["+record.getPatientName()+"]明细中项目不允许为空!");
				}
				//校验记录日期
				if(null == item.getRecordDate()){
					throw new MnisException("患者["+record.getPatientName()+"]明细中记录日期不允许为空!");
				}
				if(StringUtils.isEmpty(item.getPatId())){
					item.setPatId(record.getPatientId());
				}
				//明细项目的index
				String recordDate = DateUtil.format(item.getRecordDate(),DateFormat.FULL);
				//计算明细的index
				int[] index = BodySignUtil.getIndexByrecordDate(recordDate);
				String itemCode = item.getBodySignDict().getItemCode();
				if(BodySignConstants.ONCE_PER_DAY_ITEMS.contains(itemCode)){
					//一天一次的项目
					item.setIndex(index[3]);
					item.setRecordDate(zeroDate);//当天0点
					zeroItems.add(item);//一天一次的项目
				}else if(BodySignConstants.TWICE_PER_DAY_ITEMS.contains(itemCode)){
					//一天两次的项目
					item.setIndex(index[2]);
					items.add(item);//一天多次的项目
				}else if(BodySignConstants.THREE_TIMES_PER_DAY_ITEMS.contains(itemCode)){
					//一天三次的项目
					item.setIndex(index[1]);
					items.add(item);//一天多次的项目
				}else if(BodySignConstants.SIX_TIMES_PER_DAY_ITEMS.contains(itemCode)){
					//一天6次的项目
					item.setIndex(index[0]);
					items.add(item);//一天多次的项目
				}
				zeroRecord.setBodySignItemList(zeroItems);
				record.setBodySignItemList(items);
			}
		}
		//事件
		if(null != record.getEvent()){
			PatientEvent event = record.getEvent();
			//校验事件编号是否为空
			if(StringUtils.isEmpty(event.getEventCode())){
				throw new MnisException("患者["+record.getPatientName()+"]事件中项目编号不允许为空!");
			}
			//校验事件记录日期
			if(null == event.getRecord_date()){
				throw new MnisException("患者["+record.getPatientName()+"]事件中记录时间不允许为空!");
			}
			//校验事件事件时间
			if(null == event.getRecordDate()){
				throw new MnisException("患者["+record.getPatientName()+"]事件中事件时间不允许为空!");
			}
			
			event.setPatientId(record.getPatientId());//患者流水号
			event.setPatientName(record.getPatientName());//患者姓名
			event.setEventName(BodySignConstants.MAP_BODYSIGN_ITEM.get(event.getEventCode()));
			event.setRecorderId(record.getModifyNurseCode());//护士员工编号
			event.setRecorderName(record.getModifyNurseName());//员工姓名
			
			//计算index
		    int[] index = BodySignUtil.getIndexByrecordDate(event.getRecordDate());
		    Date eventDate = DateUtil.parse(record.getEvent().getRecordDate());
		    if(!StringUtils.isEmpty(eventHours)){
		    	//如果存在配置
		    	event.setIndex(BodySignUtil.getEventIndex(eventDate,eventHours));
		    }else{
		    	event.setIndex(index[0]);//体温单上显示在一天6次的格子上
		    }
		    //事件编号
		    if(StringUtils.isEmpty(event.getEventCode())){
		    	event.setStatus(BodySignStatus.DELETE.getValue());
		    }else{
		    	event.setStatus(BodySignStatus.NEW.getValue());
		    }
		    
		    //设置中文时间
			if (StringUtils.isNotBlank(event.getRecordDate())) {
				if (event.getRecordDate().length() > 10) {
					event.setRecordDate(event.getRecordDate().substring(11,16));
				}
				//为事件计算中文显示
				boolean bool = (!StringUtils.isEmpty(hourType) && "1".equals(hourType)) ? true : false;
				event.setChineseEventDate(DateUtil.getChineseHourMinute(event.getRecordDate(),bool));
			}
			
			//把事件归在0点
			zeroRecord.setEvent(event);
			record.setEvent(null);
		}
		
		//拆分后的生命体征
		List<BodySignRecord> records = new ArrayList<BodySignRecord>();
		records.add(zeroRecord);
		records.add(record);
		return records;
	}
}