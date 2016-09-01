package com.lachesis.mnis.core.util;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public final class CommonHelper {
	private static final String[] USAGE_ORAL_ARRAY = {"P.O", "含服", "煎服", "冲服"};
	private CommonHelper(){
	}
	/**
	 * 格式化病人过敏药物信息成字符串
	 * 
	 * @param hisPatientInfoList
	 * @return
	 */
	public static String formatAllergeDrug(List<String> drugNameList) {

		StringBuilder allergDrugSb = new StringBuilder(10);
		// 过敏药物数据
		int i = 1;
		for (String drugName:drugNameList) {
			allergDrugSb.append(i);
			allergDrugSb.append(". ");
			allergDrugSb.append( drugName );
			allergDrugSb.append("\n");
			i++;
		}
		// delete the last "\n" symbol previously appended
		allergDrugSb.delete(allergDrugSb.length()-2, allergDrugSb.length() ); 

		return allergDrugSb.toString();
	}

	/**
	 * 设置病人的年龄范畴:婴儿,儿童,成人.
	 * 
	 * @param patAge
	 * @return
	 */
	public static String getAgeDuration(int patAge) {
		String ageDuration = null;
		if (patAge <= NurseConstants.AGE_DURATION_BABY) {
			ageDuration = NurseConstants.AGE_DURATION_BABY_VALUE;
		}else if(patAge > NurseConstants.AGE_DURATION_BABY && patAge <= NurseConstants.AGE_DURATION_YOUNG ){
			ageDuration = NurseConstants.AGE_DURATION_YOUNG_VALUE;
		}else {
			ageDuration = NurseConstants.AGE_DURATION_MAN_VALUE;
		}

		return ageDuration;
	}

	/**
	 * 设置病人的付费类型
	 * 
	 * @param chargeKind
	 * @return
	 */
	public static String getChargeType(String chargeKind) {
		int payKind = 0;
		try{
			payKind = Integer.parseInt(chargeKind);
		}catch(NumberFormatException e){

		}
		if (payKind>5 || payKind<1){
			payKind = 1;
		}
		return NurseConstants.PATIENT_PAY_KIND[payKind-1];
	}


	public static String getPatientCondition(int conditionFlag) {
		int cond = conditionFlag;
		if (conditionFlag !=1 && conditionFlag !=2){
			cond = 0;
		}
		return NurseConstants.PATIENT_CONDITION[ cond ];	
	}

	/**
	 * 获取病人的护理等级数据
	 * 
	 * @param tend
	 * @return
	 */
	public static int getNurseLevel(String tend) {
		for(int level = 0; level<4; level++) {
			if( NurseConstants.TEND_LEVEL[level].equals(tend) ){
				return level;
			}
		}
		return -1;
	}

	/**
	 * 根据用法判断医嘱是否是输液医嘱
	 * 
	 * @param useMethod
	 * @return
	 */
	public static boolean isInfusionOrder(String useMethod) {
		if(StringUtils.isNotEmpty(useMethod)&&(useMethod.contains("iv") || useMethod.contains("IV"))) {
			return true;
		}
		return false;
	}

	/**
	 * 根据用法判断医嘱是否是口服药医嘱
	 * 
	 * @param useMethod
	 * @return
	 */
	public static boolean isOralOrder(String useMethod) {
		if( StringUtils.isNotEmpty(useMethod) &&  Arrays.asList( USAGE_ORAL_ARRAY).indexOf(useMethod)>=0 ) {
			return true;
		}
		return false;
	}

	/**
	 * 根据typecode得到医嘱类型(长期医嘱;临时医嘱)
	 * 
	 * @param typecode
	 * @return
	 */
	public static String getOrderType(String typecode) {
		String typeRs = "";
		if("CZ".equalsIgnoreCase(typecode)){
			typeRs =  "长期医嘱";
		}else if ("LZ".equalsIgnoreCase(typecode)) {
			typeRs =  "临时医嘱";
		}
		return typeRs;
	}
	
	/**
	 * 设置医嘱执行状态编码
	 * 
	 * @param execDate
	 * @param moDate
	 * @param execFlag
	 * @param execUserCD
	 */
	public static String getOrderExecStatusCode(Date execDate, Date moDate, String execFlag, String execUserCD) {
		if (execDate != null 
				&& moDate != null 
				&& execDate.after(moDate)
				&& NurseConstants.ORDER_EXEC_FLAG.equals(execFlag)) {
			return NurseConstants.ORDER_STATUS_EXECTED;
		} else {
			return NurseConstants.ORDER_STATUS_CHECKED;
		}
	}

	/**
	 * 设置医嘱执行状态名称
	 * 
	 * @param execDate
	 * @param moDate
	 * @param execFlag
	 * @param execUserCD
	 */
	public static String getOrderExecStatusName(Date execDate, Date moDate, String execFlag, String execUserCD) {
		if (execDate != null 
				&& moDate != null 
				&& execDate.after(moDate)
				&& NurseConstants.ORDER_EXEC_FLAG.equals(execFlag)) {
			return NurseConstants.ORDER_EXEC_STATUS_EXEC;
		} else {
			return NurseConstants.ORDER_EXEC_STATUS_CHECKED;
		}
	}
}
