package com.lachesis.mnis.core.patient;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lachesis.mnis.core.util.DateUtil;

public final class PatientHelper {
	
	static final Logger LOGGER = LoggerFactory.getLogger(PatientHelper.class);
	
	public static final int AGE_DURATION_BABY = 28;
	
	public static final int AGE_DURATION_CHILD = 14;
	
	public static final int AGE_DURATION_YOUNG = 18;
	
	public static final int AGE_DURATION_OLD = 60;
	
	public static final String AGE_DURATION_BABY_CODE = "B";
	
	public static final String AGE_DURATION_YOUNG_CODE = "Y";
	
	public static final String AGE_DURATION_MAN_CODE = "M";
	
	public static final String AGE_DURATION_OLD_CODE = "O";
	
	private PatientHelper(){}
	
	/**
	 * 床号格式应为**_**, 或deptCode,roomCode与bedNo的拼接
	 * @param rawBedCode
	 * @param deptCode
	 * @param roomCode
	 * @return
	 */
	public static String resolveBedCode(String bedCode) {
		if(StringUtils.isNotBlank(bedCode) && bedCode.length()>5){
			bedCode = bedCode.substring(4);
		}
		return bedCode;
	}
	
	/**
	 * 计算出生不满一年的婴孩的年龄段
	 * 
	 * @param patAge
	 * @return
	 */
	public static String getAgeDurationForDay(int days){
		String ageDuration = null;
		if(days <= AGE_DURATION_BABY){
			ageDuration = AGE_DURATION_BABY_CODE;
		}
		ageDuration = AGE_DURATION_YOUNG_CODE;
		return ageDuration;
	}
	
	/**
	 * 设置病人的年龄范畴:儿童(0-14),成人(14以上).
	 * 
	 * @param days 年龄相差的天数
	 * @return
	 */
	public static String getAgeDuration(int days) {
		if (days <= AGE_DURATION_CHILD * DateUtil.DAY_PER_YEAR) { // 14岁以下
			return AGE_DURATION_YOUNG_CODE;
		}else {
			return AGE_DURATION_MAN_CODE;
		}
	}
}
