package com.lachesis.mnis.core.constants;


public class MnisConstants {

	/**空字符串*/
	public static final String EMPTY = " ";
	/**逗号*/
	public static final String COMMA = ",";
	/**横线*/
	public static final String LINE = "-";
	/**冒号*/
	public static final String COLON = ":";
	//分号
	public static final String SEMI_COLON = ";";
	//星号
	public static final String STAR = "\\*";
	
	public static final String LINE_COLON = "-:";
	
	public static final String LINE_SEMI_COLON = "-;";
	/**0*/
	public static final String ZERO = "0";
	/**竖线*/
	public static final String VERTICAL_LINE = "\\|";
	/**默认字符编码*/
	public static final String DEFAULT_CHARSET = "UTF-8";
	/**identity*/
	public static final String ID = "id";
	
	/**输液剂量单位*/
	public static final String INFUSION_DOSAGE_UNIT = "ml";
	
	public static final String INFUSION_DEFAULT_SPEED_UNIT = "滴/分";
	
	public static final String SKIN_TEST_POSITIVE = "p";
	/**时分为0*/
	public static final String ZERO_HM_TIME = "00:00";
	public static final String ZERO_HMS_TIME = "00:00:00";
	/**
	 * 生命体征手术时间规则
	 * 1：当在手术14天内进行第二次手术，则停写第一次术后日数，第二次手术 当日填写1-0，
	 * 依次填入第二次手术后第14天
	 * 2：当在手术14天内进行第二次手术，第二次术后日期/第一次术后日期，依次填入第二次手术后第14天
	 * (1:[1-0],2[1/2])
	 */
	public static final int BODY_SIGN_EVENT_RULE = 2;

	
	public static final String ORDER_EXEC_TYPE_BLOOD = "BLOOD";
	public static final String ORDER_EXEC_TYPE_BLOOD_NAME = "输血";
	public static final String ORDER_EXEC_TYPE_ORAL = "ORAL";
	public static final String ORDER_EXEC_TYPE_ORAL_NAME = "口服药";
	public static final String ORDER_EXEC_TYPE_INFUSION = "INFUSION";
	public static final String ORDER_EXEC_TYPE_INFUSION_NAME = "输液";
	public static final String ORDER_EXEC_TYPE_INJECT = "INJECT";
	public static final String ORDER_EXEC_TYPE_INJECT_NAME = "注射";
	public static final String ORDER_EXEC_TYPE_SKINTEST = "SKINTEST";
	public static final String ORDER_EXEC_TYPE_SKINTEST_NAME = "皮试";
	public static final String ORDER_EXEC_TYPE_LAB = "LAB";
	public static final String ORDER_EXEC_TYPE_LAB_NAME = "检验";
	public static final String ORDER_EXEC_TYPE_INSPECTION = "UC";
	public static final String ORDER_EXEC_TYPE_INSPECTION_NAME = "检查";
	public static final String ORDER_EXEC_TYPE_TREATMENT = "UZ";
	public static final String ORDER_EXEC_TYPE_TREATMENT_NAME = "治疗";
	public static final String ORDER_EXEC_TYPE_WARDPATROL = "WARDPATROL";
	public static final String ORDER_EXEC_TYPE_WARDPATROL_NAME = "巡视";
	public static final String ORDER_EXEC_TYPE_BODYSIGN = "BODYSIGN";
	public static final String ORDER_EXEC_TYPE_BODYSIGN_NAME = "体征";
	public static final String ORDER_EXEC_TYPE_NURSE_DOC = "NURSE_DOC";
	public static final String ORDER_EXEC_TYPE_NURSE_DOC_NAME = "文书";
	public static final String ORDER_EXEC_TYPE_OTHER = "UT";
	public static final String ORDER_EXEC_TYPE_OTHER_NAME = "其他";
	public static final String ORDER_EXEC_TYPE_DISCHARGE = "CY";
	public static final String ORDER_EXEC_TYPE_DISCHARGE_NAME = "出院";
	public static final String ORDER_EXEC_TYPE_BLOODSUGAR = "BS";
	public static final String ORDER_EXEC_TYPE_BLOODSUGAR_NAME = "血糖测量";
	public static final String ORDER_EXEC_TYPE_NURSING = "NS";
	public static final String ORDER_EXEC_TYPE_NURSING_NAME = "护理级别";
	public static final String ORDER_EXEC_TYPE_CRITICAL = "BW";
	public static final String ORDER_EXEC_TYPE_CRITICAL_NAME = "病危";
	public static final String ORDER_EXEC_TYPE_SERIOUS = "BZ";
	public static final String ORDER_EXEC_TYPE_SERIOUS_NAME = "病重";
	public static final String ORDER_EXEC_TYPE_OTHER_DRUG = "OTHERDRUG";
	public static final String ORDER_EXEC_TYPE_OTHER_DRUG_NAME = "其他药物类";
	public static final String ORDER_EXEC_TYPE_ENEMA = "ENEMA"; 
	public static final String ORDER_EXEC_TYPE_ENEMA_NAME = "灌肠";


	public static final String ORDER_STATUS_NEW = "N";
	public static final String ORDER_STATUS_NEW_NAME = "新开立";

	public static final String ORDER_STATUS_CONFIRMED = "C";
	public static final String ORDER_STATUS_CONFIRMED_NAME = "待执行";

	public static final String ORDER_STATUS_EXECUTED = "E";
	public static final String ORDER_STATUS_EXECUTED_NAME = "已执行";

	public static final String ORDER_STATUS_FINISHED = "F";
	public static final String ORDER_STATUS_FINISHED_NAME = "已完成";

	public static final String ORDER_STATUS_STOPPED = "S";
	public static final String ORDER_STATUS_STOPPED_NAME = "已停止";
	
	public static final String ORDER_STATUS_UNSTOPPED = "U";
	public static final String ORDER_STATUS_UNSTOPPED_NAME = "未停止";
	
	public static final String ORDER_STATUS_MONITOR = "I";
	public static final String ORDER_STATUS_MONITOR_NAME = "输液中";
	
	public static final String ORDER_STATUS_PAUSED = "P";
	public static final String ORDER_STATUS_PAUSED_NAME = "已暂停";
	
	public static final String ORDER_STATUS_END = "E";
	public static final String ORDER_STATUS_END_NAME = "已拔针";

	public static final int WORK_UNIT_USER = 0;

	public static final int WORK_UNIT_DEPT = 1;

	public static final int WORK_UNIT_WARD = 2;

	public static final String AGE_DURATION_YOUNG_CODE = "Y";

	public static final String AGE_DURATION_MAN_CODE = "M";

	public static final int APPROVE_BY_EMPLCODE = 0;

	public static final int APPROVE_BY_LOGIN = 0;

	public static final int APPROVE_BY_FINGER = 1;

	public static final String ACKRESULT_SUCCESS = "0";

	// public static final int ACKRESULT_ERROR = -1;

	// public static final int ACKRESULT_TOKEN_WRONG = -2;

	// public static final int ACKRESULT_TOKEN_TIMEOUT = -3;

	// public static final int ACKRESULT_LOGOUT = -4;
	/**是否启用交班功能*/
	public static final String SYS_IS_NURSE_SHIFT= "isNurseShift";
	
	/***/
	public static final int CODE_NULL_ARGUMENT = -1;
	/**医院名称*/
	public static final String SYS_CONFIG_HOSPITAL_NAME = "hospitalName";
	/**条码类型*/
	public static final String SYS_CONFIG_LABEL_BARCODE_TYPE = "labelBarcodeType";
	/**密码是否加密*/
	public static final String SYS_CONFIG_ENCRYPT_TYPE = "encryptType";
	/**配液管理是否需要长嘱*/
	public static final String SYS_LIQUOR_ORDER_TYPE = "liquorOrderType";
	/**交班本时间点*/
	public static final String SYS_NURSE_SHIFT_TIME = "nurseShiftTime";
	/**是否开启输液管理系统功能*/
	public static final String SYS_ISOPEN_IS = "infusionEnable";
	/**医嘱,生命体征是否同步到文书*/
	public static final String SYS_IS_SYNC_DOC_REPORT= "isSyncDocReport";
	//执行医嘱是否只有在包含出入量信息的情况下才同步到文书
	public static final String SYS_IS_SYNC_ORDER_WITH_INOUT= "isOrderInOutCopy";
	/**闭环输液是否同步*/
	public static final String SYS_IS_SYNC_INFUSION_MANAGER = "isSyncInfusionManager";
	/**闭环输液是否推送*/
	public static final String SYNC_INFUSION= "syncInfusion";
	/**皮试过滤药物*/
	public static final String SYS_SKIN_TEST_DRUG_CODES= "skinTestDrugCodes";
	/**生命体征数据选择策略*/
	public static final String SYS_BODY_SIGN_DATA_STRATEGY = "bodySignDataStrategy";
	/**长嘱*/
	public static final String LONG_ORDER = "CZ";
	/**临嘱*/
	public static final String TEMP_ORDER = "LZ";
	/**已备液*/
	public static final String LIQUOR_PREPARE = "P";
	/**已审核*/
	public static final String LIQUOR_VERIFY = "V";
	/**已配液*/
	public static final String LIQUOR_FINISH = "F";
	/**1ml总共15滴*/
	public static final int INFUSION_SPEED_PER_ML = 15;
	
	
	/*** 条码规则  barType=LAB,issub=1:截取,len=条码长度,sublen=数据保存长度,
	 * sbustart=截取开始值,subend=截取结束值,barlens=该类型条码最小值,barlene=最大值,index=索引值 */
	public static final String BARCODE="barCode";
	/**
	 *  value =1 所有长期医嘱自动分解，条码由his提供
	 */
	
	public static final String QUERYBYORDERGROUP="queryByOrderGroup";
	//转抄出量名称
	public static String COPY_DOC_OUT_NAME = "out_name";
	//转抄出量code
	public static String COPY_DOC_OUT_TAKE = "outTake";
	//转抄入量名称
	public static String COPY_DOC_INPUT_NAME = "input_name";
	//转抄入量code
	public static String COPY_DOC_INPUT_TAKE = "inputTake";
	//转抄医嘱入量code
	public static String COPY_DOC_IN_TAKE = "inTake";
	//性状
	public static String COPY_DOC_SHAPE = "out_shape";
	//颜色
	public static String COPY_DOC_COLOR = "out_color";
	
	/**
	 * 1:12小时制  其他：24小时制
	 */
	public static String SYS_CONFIG_HOURTYPE="hourType";

	//体温单数据录入时间
	public static String SYS_CONFIG_TMPRT_TIME = "tempInputTimeSelector";
	
	/**
	 * 检验类的医嘱，日期往前推
	 */
	public static String LAB_BEFORE_DAY="labBeforeDay";
	/**
	 * 是否需要双核对
	 */
	public static String IS_NEED_DOUBLE_CHECK = "isNeedDoubleCheck";
	/**
	 * 用户密码错误
	 */
	public static int DOUBLE_CHECK_PASSOWRD_ERR = 10;
	/**
	 * 部门不一致错误
	 */
	public static int DOUBLE_CHECK_NOT_SAME_DEPT_ERR = 11;
	/**
	 * 同一护士错误
	 */
	public static int DOUBLE_CHECK_SAME_NURSER_ERR = 12;
	/**
	 * 医嘱执行单类型-输液卡
	 */
	public static String ORD_DOC_TYPE_INF_CARD = "INF_CARD";
	/**
	 * 医嘱执行单类型-执行单
	 */
	public static String ORD_DOC_TYPE_EXECUTION = "EXECUTION";
	/**
	 * 医嘱执行单类型-瓶签
	 */
	public static String ORD_DOC_TYPE_LABEL = "LABEL";
	/**
	 * 医嘱执行单类型-普通
	 */
	public static String ORD_DOC_TYPE_NORMAL = "NORMAL";
	
	public static String QUERY_EDIT_NWB_DATE = "queryEditNwbDate";
	
	public static String QUERY_NWB_DATE = "queryNwbDate";
	
	public static String PRE_QUERY_NWB_DATE = "preQueryNwbDate";
	
	public static String DOC_REPORT_STATISTIC_TIME = "07:00:00";
	
	public static String NWB_EDIT = "edit";
	public static String NWB_DOSAGE = "dosage";
	
	public static String NWB_METADATA_CACHE = "NWB_METADATA_CACHE";
	
	public static String NWB_METADATA_TV_CACHE = "NWB_METADATA_TV_CACHE";
	
	public static String NWB_METADATA_TEMPLATE_CACHE = "NWB_METADATA_TEMPLATE_CACHE";
	//闭环输液路径
	public static String PROP_INFUSION_MANAGER_SAVEEXECORDER_URL = "infusionManager.saveExecOrderInfo.url";
	public static String INFUSION_MANAGER_SAVEEXECORDER_URL = "/infusion/nur/infusionmanager/saveExecOrderInfo";
	public static String INFUSION_IP = "infusionIp";
	public static String HTTP = "http://";
	//闭环输液请求参数
	public static String PROP_INFUSION_MANAGER_SAVEEXECORDER_PARAM = "orderExecInfo";
	//配置文件路径
	public static String PROP_PATH = "/mnis.properties";
	
	public static String PROP_CRITICAL_START_DATE_KEY = "critical.start.date";
	
	//用户生命外出、请假状态
	public static String PAT_STATUS_OUT="01";//外出
	public static String PAT_STATUS_LEA="02";//请假
	
	//医嘱用法配置表 转抄和配液
	public static String COM_DIC_USAGE_COPY = "copy";
	public static String COM_DIC_USAGE_LIQUOR = "liquor";
	
	//是否文书表头
	public static String IS_HEADER = "1";
	//web service包体,参数通过param替换
	public static String WEB_SERVICE_PARAM_VALUES = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservice.inter.datasync.mnis.lachesis.com/\">"
			+ "  <soapenv:Header/>"
			+ "  <soapenv:Body>"
			+ "   <web:#{param1}>"
			+ "  <barcode>#{param2}</barcode>"
			+ "</web:#{param1}>"
			+ "</soapenv:Body>" + "</soapenv:Envelope>";
	
	public static String WEB_SERVICE_URL = "/data_sync/services/hisDataService?wsdl";

	public static String WEB_SERVICE_HIS_ALL_ORDER_DATA = "getHisAllOrderData";
	public static String WEB_SERVICE_HIS_ORDER_DATA = "getHisOrderData";
	public static String WEB_SERVICE_HIS_DRUG_BAG_DATA = "getHisDrugBagData";
	public static String WEB_SERVICE_HIS_LAB_TEST_DATA = "getHisLabTestData";
	public static String WEB_SERVICE_HIS_BLOOD_DATA = "getHisBloodData";
	
	//条码空
	public static String BAR_EMPTY = "BAR_EMPTY";
	//医嘱不存在
	public static String ORD_NOT_EXIST = "ORD_NOT_EXIST";
	//医嘱停止
	public static String ORD_STOP = "ORD_STOP";
	//HIS医嘱不存在
	public static String ORD_HIS_NOT_EXIST = "ORD_HIS_NOT_EXIST";
	//医嘱重整
	public static String ORD_REFORM = "ORD_REFORM";
	//医嘱已执行
	public static String ORD_EXECED = "ORD_EXECED";
	//医嘱已备药
	public static String ORD_LIQ_PREPAREED = "ORD_LIQ_PREPAREED";
	//医嘱已审核
	public static String ORD_LIQ_VERIFIED = "ORD_LIQ_VERIFIED";
	//医嘱已配药
	public static String ORD_LIQ_EXECED = "ORD_LIQ_EXECED";
	//医嘱未备药
	public static String ORD_LIQ_UN_PREPARE = "ORD_LIQ_UN_PREPARE";

	public static String PAT_NOT_EXIST = "PATT_NOT_EXIST";
	public static String PAT_ERROR = "PATT_ERROR";
	
	public static String SCAN_LIQUOR = "LIQUOR";
	public static String SCAN_PATIENT = "PATIENT";
	
	//顺序配置表，手术模块名
	public static String SURGERY_ORDER = "SURGERY_ORDER";

	public static final int DOC2BODYSIGN_OTHEROUT_COUNT = 3;//文书转抄到生命体征的其他类型出量数据个数

	//文书转抄到生命体征，出量大便次数的项目名称
	public static final String DOC_OUT_NAME_STOOL = "out_name_stool";

	//小白板PC界面框架json文件的总目录地址
	public static String WHITE_BOARD_STRUCTURE_ADDRESS = "WHITE_BOARD_STRUCTURE_ADDRESS";
}
