package com.lachesis.mnis.core.util;

import java.util.HashMap;
import java.util.Map;

import com.lachesis.mnis.core.constants.MnisConstants;

public final class OrderUtil {
	private OrderUtil() {
	}

	/** F00000038521 血浆200ml */
	public static final String BLOOD_ITEM_CODE = "F00000038521";
	public static final String USAGE_PO = "p.o";
	public static final String USAGE_IV = "iv.drip";
	public static final String CLASS_CODE_LAB = "ul";
	public static final String CLASS_CODE_DIET = "MF";
	public static final String CLASS_CODE_TEND = "UN";
	public static final String CLASS_CODE_CURE = "UZ";
	public static final String CLASS_CODE_INSPECTION = "UZ";
	public static final String CLASS_CODE_OTHER = "UT";

	public enum OrderType {
		DRUG_INFUSION, DRUG_ORAL, LAB_TEST, CURE_BLOOD, CURE, INSPECTION, OTHER
	}

	public static final String ORDER_NEW = "0";
	public static final String ORDER_CONFIRMED = "1";
	public static final String ORDER_EXECUTED = "2";
	public static final String ORDER_STOPPED = "3";
	public static final String ORDER_FINISHED = "4";
	private static final Map<String, String> ORDER_STATUS_MAP = new HashMap<String, String>();
	private static final Map<String, String> ORDER_STATUS_CODE_MAP = new HashMap<String, String>();
	public static final String ORDER_TYPE_LONG = "CZ";
	public static final String ORDER_TYPE_TEMP = "LZ";
	public static final String ORDER_FREQ_ST = "ST";

	static {
		/*ORDER_STATUS_CODE_MAP.put("0", MnisConstants.ORDER_STATUS_NEW);
		ORDER_STATUS_CODE_MAP.put("1", MnisConstants.ORDER_STATUS_CONFIRMED);
		ORDER_STATUS_CODE_MAP.put("2", MnisConstants.ORDER_STATUS_EXECUTED);
		ORDER_STATUS_CODE_MAP.put("3", MnisConstants.ORDER_STATUS_STOPPED);
		ORDER_STATUS_CODE_MAP.put("4", MnisConstants.ORDER_STATUS_FINISHED);
		ORDER_STATUS_MAP.put("0", "新开立");
		ORDER_STATUS_MAP.put("1", "新审核");
		ORDER_STATUS_MAP.put("2", "已执行");
		ORDER_STATUS_MAP.put("3", "已停止");
		ORDER_STATUS_MAP.put("4", "已完成");*/
		//新开立，未执行，停止，已执行
		ORDER_STATUS_CODE_MAP.put("1", MnisConstants.ORDER_STATUS_NEW);
		ORDER_STATUS_CODE_MAP.put("2", MnisConstants.ORDER_STATUS_CONFIRMED);
		ORDER_STATUS_CODE_MAP.put("3", MnisConstants.ORDER_STATUS_STOPPED);
		ORDER_STATUS_CODE_MAP.put("4", MnisConstants.ORDER_STATUS_EXECUTED);
		ORDER_STATUS_CODE_MAP.put("5", MnisConstants.ORDER_STATUS_UNSTOPPED);
		
		ORDER_STATUS_MAP.put("1", MnisConstants.ORDER_STATUS_NEW_NAME);
		ORDER_STATUS_MAP.put("2", MnisConstants.ORDER_STATUS_CONFIRMED_NAME);
		ORDER_STATUS_MAP.put("3", MnisConstants.ORDER_STATUS_STOPPED_NAME);
		ORDER_STATUS_MAP.put("4", MnisConstants.ORDER_STATUS_EXECUTED_NAME);
		ORDER_STATUS_MAP.put("5", MnisConstants.ORDER_STATUS_UNSTOPPED_NAME);
		
	}

	/**
	 * 获取医嘱状态code
	 * @param key
	 * @return
	 */
	public static String getOrderStatusCode(String key) {
		return ORDER_STATUS_CODE_MAP.get(key);
	}
	
	/**
	 * 获取医嘱状态name
	 * @param key
	 * @return
	 */
	public static String getOrderStatusName(String key){
		return ORDER_STATUS_MAP.get(key);
	}

	/**
	 * 根据用法判断医嘱是否是口服药医嘱
	 * 
	 * @param usage
	 * @return
	 */
	public static boolean isOralOrder(String usage) {
		if (usage == null || usage.isEmpty()) {
			return false;
		}
		if (usage.toLowerCase().contains(USAGE_PO) || usage.contains("含服")
				|| usage.contains("煎服") || usage.contains("冲服")) {
			return true;
		}
		return false;
	}

	/**
	 * 根据用法判断医嘱是否是输液医嘱
	 * 
	 * @param usage
	 * @return
	 */
	public static boolean isInfusionOrder(String usage) {
		if (usage == null || usage.isEmpty()) {
			return false;
		}
		if (usage.toLowerCase().contains(USAGE_IV)) {
			return true;
		}
		return false;
	}

	/**
	 * 根据医嘱类别编码判断是否实验室诊断医嘱
	 * 
	 * @param classCode
	 * @return
	 */
	public static boolean isLabTestOrder(String classCode) {
		if (classCode == null || classCode.isEmpty()) {
			return false;
		} else if (classCode.equalsIgnoreCase(CLASS_CODE_LAB)) {
			return true;
		}
		return false;

	}

	public static OrderType parseOrderType(String usage, String classCode) {
		if (isLabTestOrder(classCode)) {
			return OrderType.LAB_TEST;
		}
		if (isOralOrder(usage)) {
			return OrderType.DRUG_ORAL;
		}
		if (isInfusionOrder(usage)) {
			if (isCureOrder(classCode)) {
				// 输血医嘱
				return OrderType.CURE_BLOOD;
			}
			return OrderType.DRUG_INFUSION;
		}
		if (isCureOrder(classCode)) {
			// 输血医嘱
			return OrderType.CURE;
		}
		if (isInspection(classCode)) {
			return OrderType.INSPECTION;
		}
		if (isOther(classCode)) {
			return OrderType.OTHER;
		}
		return null;
	}

	/**
	 * C和E
	 * @param orderExecId(待执行，已执行)
	 * @return
	 */
	public static String processOrderExecStatusCode(String orderExecId) {
		
		String statusCode = MnisConstants.ORDER_STATUS_EXECUTED;
		if (orderExecId == null) {
			statusCode = MnisConstants.ORDER_STATUS_CONFIRMED;
		}
		return statusCode;
	}

	/**
	 * 待执行和已执行
	 * @param orderExecId
	 * @return
	 */
	public static String processOrderExecStatusName(String orderExecId) {
		String statusName = MnisConstants.ORDER_STATUS_EXECUTED_NAME;
		if (orderExecId == null) {
			statusName = MnisConstants.ORDER_STATUS_CONFIRMED_NAME;
		}
		return statusName;
	}
	
	/**
	 * S和C
	 * @param orderStatus
	 * @return
	 */
	public static String processOrderStatusCode(String orderStatus) {
		String statusName = "";
		if(orderStatus == null)
			statusName = MnisConstants.ORDER_STATUS_CONFIRMED;
		else{
			
			if(MnisConstants.ORDER_STATUS_NEW.equals(getOrderStatusCode(orderStatus)) 
					|| MnisConstants.ORDER_STATUS_CONFIRMED.equals(getOrderStatusCode(orderStatus))){
				statusName = MnisConstants.ORDER_STATUS_CONFIRMED;
			}else if(MnisConstants.ORDER_STATUS_STOPPED.equals(getOrderStatusCode(orderStatus))){
				statusName = MnisConstants.ORDER_STATUS_STOPPED;
			}
		}
		return statusName;
	}
	
	/**
	 * 已停止和已审核
	 * @param orderStatus
	 * @return
	 */
	public static String processOrderStatusName(String orderStatus) {
		String statusName = "";
		if(orderStatus == null)
			statusName = MnisConstants.ORDER_STATUS_CONFIRMED_NAME;
		else{
			
			if(MnisConstants.ORDER_STATUS_NEW.equals(getOrderStatusCode(orderStatus)) 
					|| MnisConstants.ORDER_STATUS_CONFIRMED.equals(getOrderStatusCode(orderStatus))){
				statusName = MnisConstants.ORDER_STATUS_CONFIRMED_NAME;
			}else if(MnisConstants.ORDER_STATUS_STOPPED.equals(getOrderStatusCode(orderStatus))){
				statusName = MnisConstants.ORDER_STATUS_STOPPED_NAME;
			}
		}
		return statusName;
	}

	public static boolean isDietOrder(String classCode) {
		return CLASS_CODE_DIET.equalsIgnoreCase(classCode);
	}

	public static boolean isTendOrder(String classCode) {
		return CLASS_CODE_TEND.equalsIgnoreCase(classCode);
	}

	public static boolean isCureOrder(String classCode) {
		return CLASS_CODE_CURE.equalsIgnoreCase(classCode);
	}

	private static boolean isInspection(String classCode) {
		return CLASS_CODE_INSPECTION.equalsIgnoreCase(classCode);
	}

	private static boolean isOther(String classCode) {
		return CLASS_CODE_OTHER.equalsIgnoreCase(classCode);
	}

	public static boolean isTempOrder(String orderTypeCode) {
		return MnisConstants.TEMP_ORDER.equalsIgnoreCase(orderTypeCode);
	}

	public static boolean isLongOrder(String orderTypeCode) {
		return MnisConstants.LONG_ORDER.equalsIgnoreCase(orderTypeCode);
	}
	
	public static String longTermType(String orderTypeCode){
		String isLong = "1";
		if(MnisConstants.LONG_ORDER.equalsIgnoreCase(orderTypeCode) || ("1".equalsIgnoreCase(orderTypeCode))){
			isLong = "1";
		}else if(MnisConstants.TEMP_ORDER.equalsIgnoreCase(orderTypeCode) || ("0".equalsIgnoreCase(orderTypeCode))){
			isLong = "0";
		}else{
			isLong = null;
		}
		
		return isLong;
	}

	/**
	 * 判断某医嘱项是否是输液医嘱。具体逻辑需根据医院决定。
	 * 
	 * @param orderItemCode
	 * @return
	 */
	public static boolean isBloodOrder(String orderItemCode) {
		return BLOOD_ITEM_CODE.equals(orderItemCode);
	}

	/**
	 * 将医嘱可执行频次 转换为具体的执行次数
	 * 
	 * @param deliverFreq
	 * @return int
	 */
	public static int convertFreqCodeToNumber(String deliverFreq) {
		switch (deliverFreq.toUpperCase()) {
		case "ST":
		case "QD":
		case "QD(12)":
		case "Q24H":
		case "24H":
			return 1;
		case "BID":
		case "Q12H":
			return 2;
		case "TID":
		case "Q8H":
			return 3;
		case "QID":
		case "Q6H":
			return 4;
		case "Q2H":
			return 12;
			// ..... pls add more
		default:
			return 0;
		}
	}

	public static String[] setDefaultOrderPlanTimes(int i) {
		String[] times = null;
		switch (i) {
		case 1:
			times = new String[1];
			times[0] = "10:00";
			break;
		case 2:
			times = new String[2];
			times[0] = "10:00";
			times[1] = "20:00";
			break;
		case 3:
			times = new String[3];
			times[0] = "10:00";
			times[1] = "15:00";
			times[2] = "21:00";
			break;
		case 4:
			times = new String[4];
			times[0] = "10:00";
			times[1] = "14:00";
			times[2] = "18:00";
			times[3] = "22:00";
			break;
		// pls add more
		default:
			times = new String[24];
			times[0] = "00:00";
			times[1] = "01:00";
			times[2] = "02:00";
			times[3] = "03:00";
			times[4] = "04:00";
			times[5] = "05:00";
			times[6] = "06:00";
			times[7] = "07:00";
			times[8] = "08:00";
			times[9] = "09:00";
			times[10] = "10:00";
			times[11] = "11:00";
			times[12] = "12:00";
			times[13] = "13:00";
			times[14] = "14:00";
			times[15] = "15:00";
			times[16] = "16:00";
			times[17] = "17:00";
			times[18] = "18:00";
			times[19] = "19:00";
			times[20] = "20:00";
			times[21] = "21:00";
			times[22] = "22:00";
			times[23] = "23:00";
			break;
		}

		return times;
	}
}
