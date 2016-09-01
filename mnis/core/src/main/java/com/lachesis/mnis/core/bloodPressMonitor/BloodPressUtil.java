package com.lachesis.mnis.core.bloodPressMonitor;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.lachesis.mnis.core.bloodPressMonitor.entity.PatBloodPressMonitor;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

public class BloodPressUtil {
	/**
	 * 校验数据是否正确
	 * @param press
	 */
	public static void validBloodPress(PatBloodPressMonitor press){
		//校验科室编号是否为空
		if(StringUtils.isEmpty(press.getDeptCode())){
			throw new MnisException("科室编号不允许为空!");
		}
		//校验患者流水号是否为空
		if(StringUtils.isEmpty(press.getPatId())){
			throw new MnisException("患者住院流水号不允许为空!");
		}
		//校验记录时间是否为空
		if(StringUtils.isEmpty(press.getRecordTime())){
			throw new MnisException("患者["+press.getPatId()+"]记录时间不允许为空!");
		}
		try{
			//校验日期格式是否正确
			DateUtil.parse(press.getRecordTime(),DateFormat.FULL);
		}catch (Exception e) {
			throw new MnisException("患者["+press.getPatId()+"]记录时间格式不正确。正确格式为[YYYY-MM-DD HH:MM:SS]!");
		}
	}
	
	/**
	 * 批量校验数据
	 * @param lst
	 */
	public static void validBloodPressList(List<PatBloodPressMonitor> lst){
		if(null != lst && !lst.isEmpty()){
			for(PatBloodPressMonitor press : lst){
				validBloodPress(press);
			}
		}
	}
}