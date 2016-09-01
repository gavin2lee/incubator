package com.lachesis.mnis.core.util;


public final class NurseConstants {
	private NurseConstants() {
	}
	
	public static final String PATIENT_CONDITION_PT = "普通";
	
	public static final String PATIENT_CONDITION_BZ = "病重";
	
	public static final String PATIENT_CONDITION_BW = "病危";
	
	public static final String[] PATIENT_CONDITION = {
		PATIENT_CONDITION_PT, 
		PATIENT_CONDITION_BZ, 
		PATIENT_CONDITION_BW};
	
	public static final String RESULT_SUCESS = "sucess";
	
	public static final String RESULT_FAIL = "fail";
	
	public static final String ORDER_STATUS_CHECKED = "checked";
	
	public static final String ORDER_STATUS_EXECTED = "executed";
	
	public static final String ORDER_STATUS_STOPED = "stopped";
	
	public static final String PATIENT_PAY_KIND_SELF = "自费";
	
	public static final String PATIENT_PAY_KIND_ENSURE = "保险";
	
	public static final String PATIENT_PAY_KIND_STUFF = "公费在职";
	
	public static final String PATIENT_PAY_KIND_RETIRE = "公费退休";
	
	public static final String PATIENT_PAY_KIND_SUPERLEADER = "公费高干";
	
	static final String[] PATIENT_PAY_KIND = {
		PATIENT_PAY_KIND_SELF,
		PATIENT_PAY_KIND_ENSURE,
		PATIENT_PAY_KIND_STUFF,
		PATIENT_PAY_KIND_RETIRE,
		PATIENT_PAY_KIND_SUPERLEADER};
	
	public static final int AGE_DURATION_BABY = 3;
	
	public static final int AGE_DURATION_YOUNG = 14;
	
	public static final String AGE_DURATION_BABY_VALUE = "B";
	
	public static final String AGE_DURATION_YOUNG_VALUE = "Y";
	
	public static final String AGE_DURATION_MAN_VALUE = "M";
	
	//护理等级
	public static final String TEND_SUPER_LEVEL = "护理特级";
	
	public static final String TEND_FIRST_LEVEL = "护理I级";
	
	public static final String TEND_SECND_LEVEL = "护理II级";

	public static final String TEND_THIRD_LEVEL = "护理III级";
	
	static final String[] TEND_LEVEL = {
		TEND_SUPER_LEVEL,
		TEND_FIRST_LEVEL, 
		TEND_SECND_LEVEL,
		TEND_THIRD_LEVEL
		};
	
	//医嘱执行状态常量
	public static final String ORDER_EXEC_STATUS_CHECKED = "已审核";
	
	public static final String ORDER_EXEC_STATUS_EXEC = "已执行";
	
	public static final String ORDER_EXEC_STATUS_STOP = "已停止";
	
	public static final String ORDER_EXEC_FLAG = "1";
	
	//异常结构信息常量
	public static final String EXCEPTIONMSG_MODE = "001";
	
	public  static final String DRUG_TYPE_ORAL = "ORAL";
	public static final String DRUG_TYPE_INFUSION = "INFUSION";
	//交班本时段划分
	public static int[] SHIFT_NURSE_TIME_INDEX = { 0, 8, 16 };
}

