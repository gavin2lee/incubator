package com.lachesis.mnis.core.bodysign;

import com.lachesis.mnis.core.constants.MnisConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class BodySignConstants {

	/** 血压 */
	public static final String BLOODPRESS = "bloodPress";
	/** 血压 */
	public static final String BLOODPRESS1 = "bloodPress1";
	/** 血压 */
	public static final String BLOODPRESS2 = "bloodPress2";
	/** 呼吸 */
	public static final String BREATH = "breath";
	/** 心率/心搏 */
	public static final String HEARTRATE = "heartRate";
	/** 身高 */
	public static final String HEIGHT = "height";
	/** 入量 */
	public static final String INTAKE = "inTake";
	/** 其他出量 */
	public static final String OTHEROUTPUT = "otherOutput";
	/** 其他 */
	public static final String OTHER = "other";
	/** 备注 */
	public static final String REMARK = "remark";
	/** 脉搏 */
	public static final String PULSE = "pulse";
	/** 大便次数 */
	public static final String STOOL = "stool";
	/** 体温 */
	public static final String TEMPERATURE = "temperature";
	/** 降温后体温 */
	public static final String COOLED_TEMP = "cooledTemperature";
	//降温方式
	public static final String COOL_WAY = "coolway";
	/** 总出量 */
	public static final String TOTALOUTPUT = "totalOutput";
	/** 尿量 */
	public static final String URINE = "urine";
	/** 疼痛等级 */
	public static final String PAIN = "pain";
	/** 体重 */
	public static final String WEIGHT = "weight";
	/** 总入量 */
	public final static String TOTAL_INPUT = "totalInput";
	/** 皮试 */
	public static final String SKIN_TEST = "skinTest";
	/** 血氧饱和度 */
	public static final String OXYGEN_SATURATION = "oxygenSaturation";
	/** 指测血糖 */
	public static final String BLOOD_GLU = "bloodGlu";
	/** 腹围 */
	public static final String ABDOMINAL_CIR = "abdominalCir";
	/** 其他一 */
	public static final String OTHER_ONE = "OtherOne";
	/** 其他二*/
	public static final String OTHER_TWO = "OtherTwo";
	/** PDD皮试 */
	public static final String PPD = "PPD";
	//小便次数
	public static final String  URINE_TIMES="urineTimes";

	public static final String BODYSIGN_MEASURE_NOTE_ZTH = "zth";
	public static final String BODYSIGN_MEASURE_NOTE_ZTH_CN = "止痛后";

	/** 腋温 */
	public static final String BODYSIGN_ITEM_YW = "yw";
	/** 口温 */
	public static final String BODYSIGN_ITEM_KW = "kw";
	/** 肛温 */
	public static final String BODYSIGN_ITEM_GW = "gw";

	/** 上肢 */
	public static final String BODYSIGN_ITEM_SZ = "sz";
	/** 下肢 */
	public static final String BODYSIGN_ITEM_XZ = "xz";

	/** 不升 */
	public static final String BODYSIGN_ITEM_BS = "bs";
	/** 请假 */
	public static final String BODYSIGN_ITEM_QJ = "qj";
	/** 拒测 */
	public static final String BODYSIGN_ITEM_JC = "jc";
	/** 外出 */
	public static final String BODYSIGN_ITEM_OUT = "out";
	/** 测不出 */
	public static final String BODYSIGN_ITEM_CBC = "cbc";

	public static final String BODYSIGN_ITEM_QTTSZ = "qttsz";

	public static final String BODYSIGN_ITEM_HXJ = "hxj";

	public static final String BODYSIGN_ITEM_DN = "dn";

	public static final String BODYSIGN_ITEM_SJ = "sj";

	public static final String BODYSIGN_ITEM_GCH = "gch";

	public static final String BODYSIGN_ITEM_RGGM = "rggm";

	public static final String BODYSIGN_ITEM_WC = "wc";

	/** 入院 */
	public static final String BODYSIGN_ITEM_RY = "ry";
	/** 出院 */
	public static final String BODYSIGN_ITEM_CY = "cy";
	/** 手术 */
	public static final String BODYSIGN_ITEM_SS = "ss";

	public static final String BODYSIGN_ITEM_ZR = "zr";

	public static final String BODYSIGN_ITEM_ZC = "zc";

	public static final String BODYSIGN_ITEM_DIED = "sw";

	public static final String BODYSIGN_ITEM_FM = "fm";

	public static final String BODYSIGN_ITEM_CS = "cs";

	public static final String BODYSIGN_ITEM_BW = "bw";

	public static final String BODYSIGN_ITEM_BZ = "bz";
	/** 皮试结果为阴性("n")、药物可以给病人使用 */
	public static final String BODYSIGN_ITEM_NGTV = "n";
	/** 皮试结果为阳性("p")、药物过敏禁止使用 */
	public static final String BODYSIGN_ITEM_POSTV = "p";

	public static final String BODYSIGN_ITEM_TS = "ts";
	/** 自行排便 */
	public static final String BODYSIGN_ITEM_ZXPB = "zxpb";

	public static final String BODYSIGN_ITEM_MB = "mb";

	public static final String BODYSIGN_ITEM_XL = "xl";

	public static final String BODYSIGN_ITEM_HX = "hx";

	public static final String BODYSIGN_ITEM_WEIGHT = "tz";

	public static final String BODYSIGN_ITEM_HEIGHT = "sg";

	public static final String BODYSIGN_ITEM_RL = "rl";

	public static final String BODYSIGN_ITEM_ZRL = "zrl";

	public static final String BODYSIGN_ITEM_JMSZ = "jmsz";

	public static final String BODYSIGN_ITEM_JBWGSZ = "jbwgsz";

	public static final String BODYSIGN_ITEM_JCGSZ = "jcgsz";

	public static final String BODYSIGN_ITEM_KF = "kf";

	public static final String BODYSIGN_ITEM_URINE = "nl";

	public static final String BODYSIGN_ITEM_CXL = "cxl";

	public static final String BODYSIGN_ITEM_OTW = "otw";

	public static final String BODYSIGN_ITEM_YLW = "ylw";

	public static final String BODYSIGN_ITEM_OTHEROUTPUT = "qtcl";

	public static final String BODYSIGN_ITEM_TOTALOUTPUT = "zcl";

	public static final String BODYSIGN_ITEM_SHIT = "db";

	public static final String BODYSIGN_ITEM_MOREN = "moren";

	public static final String BODYSIGN_ITEM_PS = "ps";

	// 新入院
	public static final String NEWINHOS = "newInHos";
	// 手术后
	public static final String AFTERSURG = "afterSurg";
	// 高热
	public static final String HIGHTEMP = "highTemp";
	// 常规
	public static final String NORMAL_MSMENT = "normal";

	public static final Map<String, String> MAP_BODYSIGN_ITEM = new HashMap<String, String>();
	static {
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_YW, "腋温");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_KW, "口温");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_GW, "肛温");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_BS, "不升");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_QJ, "请假");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_JC, "拒测");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_OUT, "外出");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_QTTSZ, "其他特殊值");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_HXJ, "呼吸机");
		MAP_BODYSIGN_ITEM.put("zjhx", "自主呼吸");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_SZ, "上肢");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_XZ, "下肢");
		MAP_BODYSIGN_ITEM.put("cbc", "测不出");
		MAP_BODYSIGN_ITEM.put("rl", "入量");
		MAP_BODYSIGN_ITEM.put("zrl", "总入量");
		MAP_BODYSIGN_ITEM.put("jmsz", "静脉输注");
		MAP_BODYSIGN_ITEM.put("jbwgsz", "经鼻胃管输注");
		MAP_BODYSIGN_ITEM.put("jcgsz", "经肠管输注");
		MAP_BODYSIGN_ITEM.put("kf", "口服");
		MAP_BODYSIGN_ITEM.put("dn", "导尿");
		MAP_BODYSIGN_ITEM.put("zjpn", "自主排尿");
		MAP_BODYSIGN_ITEM.put("sj", "失禁");
		MAP_BODYSIGN_ITEM.put("gch", "灌肠后");
		MAP_BODYSIGN_ITEM.put("rggm", "人工肛门");
		MAP_BODYSIGN_ITEM.put("wc", "卧床");
		MAP_BODYSIGN_ITEM.put("ry", "入院");
		MAP_BODYSIGN_ITEM.put("cy", "出院");
		MAP_BODYSIGN_ITEM.put("zr", "转入");
		MAP_BODYSIGN_ITEM.put("zc", "转出");
		MAP_BODYSIGN_ITEM.put("ss", "手术");
		MAP_BODYSIGN_ITEM.put("sw", "死亡");
		MAP_BODYSIGN_ITEM.put("fm", "分娩");
		MAP_BODYSIGN_ITEM.put("cs", "出生");
		MAP_BODYSIGN_ITEM.put("wc", "外出");
		MAP_BODYSIGN_ITEM.put("zh", "召回");
		MAP_BODYSIGN_ITEM.put("hs", "回室");
		MAP_BODYSIGN_ITEM.put("bw", "病危");
		MAP_BODYSIGN_ITEM.put("bz", "病重");
		MAP_BODYSIGN_ITEM.put("n", "阴性");
		MAP_BODYSIGN_ITEM.put("p", "阳性");
		MAP_BODYSIGN_ITEM.put("ts", "特殊值");
		MAP_BODYSIGN_ITEM.put("zxpb", "自行排便");
		MAP_BODYSIGN_ITEM.put("mb", "脉搏");
		MAP_BODYSIGN_ITEM.put("xl", "心率");
		MAP_BODYSIGN_ITEM.put("hx", "呼吸");
		MAP_BODYSIGN_ITEM.put("nl", "尿量");
		MAP_BODYSIGN_ITEM.put("cxl", "出血量");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_OTW, "呕吐物");
		MAP_BODYSIGN_ITEM.put("ylw", "引流物");
		MAP_BODYSIGN_ITEM.put("qtcl", "其他出量");
		MAP_BODYSIGN_ITEM.put("zcl", "总出量");
		MAP_BODYSIGN_ITEM.put("tz", "体重");
		MAP_BODYSIGN_ITEM.put("sg", "身高");
		MAP_BODYSIGN_ITEM.put("db", "大便");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_ITEM_MOREN, "默认");
		MAP_BODYSIGN_ITEM.put("ps", "皮试");
		MAP_BODYSIGN_ITEM.put(BODYSIGN_MEASURE_NOTE_ZTH, "止痛后");
		MAP_BODYSIGN_ITEM.put("remark", "备注");
	}

	//文书转抄来的生命体征信息是否备份，对应的配置ID
	public static final String DOC_BODYSIGN_BACKUP = "doc2BodysignBackup";

	//皮试药物信息，与前端的显示约定
	public static final String SKINTEST_DRUGCODE_DEFAULT = "ts";
	public static final Map<String, String> SKINTEST_DRUG_DIC = new HashMap<String, String>();
	static {
		SKINTEST_DRUG_DIC.put("青霉素", "qms");
		SKINTEST_DRUG_DIC.put("新朗欧", "xlo");
		SKINTEST_DRUG_DIC.put("头孢孟多酯", "tbmdz");
		SKINTEST_DRUG_DIC.put("特治星", "tzx");
		SKINTEST_DRUG_DIC.put("罗氏芬", "lsf");
		SKINTEST_DRUG_DIC.put("头孢呋辛", "tbfx");
		SKINTEST_DRUG_DIC.put("头孢他啶", "tbtd");
		SKINTEST_DRUG_DIC.put("舒普深", "sps");
		SKINTEST_DRUG_DIC.put("链霉素", "lms");
		SKINTEST_DRUG_DIC.put("特殊值", "ts");
	}

	public static final String[] OTHER_OUT_ITEMNAME = {"otherOutput", "OtherOne", "OtherTwo"};
	//文书出入量项目与生命体征出入量项目名称的对应关系
	public static final Map<String, String> DOC_ITEM_TO_BODYSIGN = new HashMap<>();
	static {
		DOC_ITEM_TO_BODYSIGN.put("inTake", TOTAL_INPUT);
		DOC_ITEM_TO_BODYSIGN.put("总入量", TOTAL_INPUT);
		DOC_ITEM_TO_BODYSIGN.put("out_name_01", URINE);
		DOC_ITEM_TO_BODYSIGN.put(MnisConstants.DOC_OUT_NAME_STOOL, STOOL);
	}

	public static final Map<String, String> DOC_ITEM_TO_OTHER_OPT = new HashMap<>();
	static {
//		DOC_ITEM_TO_OTHER_OPT.put("out_name_01","urine");
//		DOC_ITEM_TO_OTHER_OPT.put("out_name_02","gtb");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_03","otw");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_04","ylw");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_05","fs");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_06","hydrothorax");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_07","dz");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_08","ox");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_09","kx");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_10","phlegm");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_11","ytb");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_12","gtb");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_13","csf");
		DOC_ITEM_TO_OTHER_OPT.put("out_name_14","xbjy");
//		DOC_ITEM_TO_OTHER_OPT.put("out_name_15","bhz");
		//总入量和尿项目
		DOC_ITEM_TO_OTHER_OPT.put("inTake",BODYSIGN_ITEM_MOREN);
		DOC_ITEM_TO_OTHER_OPT.put("总入量",BODYSIGN_ITEM_MOREN);
		DOC_ITEM_TO_OTHER_OPT.put("out_name_01",BODYSIGN_ITEM_MOREN);
		DOC_ITEM_TO_OTHER_OPT.put(MnisConstants.DOC_OUT_NAME_STOOL,"zxpb");
	}

	public static final Map<String, String> OTHER_OPT_CODE_NAME = new HashMap<>();
	static {
		//OTHER_OPT_CODE_NAME.put("urine", "尿");
		//OTHER_OPT_CODE_NAME.put("gtb","固态便");
		OTHER_OPT_CODE_NAME.put("otw","呕吐物");
		OTHER_OPT_CODE_NAME.put("ylw","引流物");
		OTHER_OPT_CODE_NAME.put("fs","腹水");
		OTHER_OPT_CODE_NAME.put("hydrothorax","胸水");
		OTHER_OPT_CODE_NAME.put("dz","胆汁");
		OTHER_OPT_CODE_NAME.put("ox","呕血");
		OTHER_OPT_CODE_NAME.put("kx","咯血");
		OTHER_OPT_CODE_NAME.put("phlegm","痰液");
		OTHER_OPT_CODE_NAME.put("ytb","便(水样)");
		OTHER_OPT_CODE_NAME.put("gtb","便(成形)");
		OTHER_OPT_CODE_NAME.put("csf","脑脊液");
		OTHER_OPT_CODE_NAME.put("xbjy","心包积液");
//		OTHER_OPT_CODE_NAME.put("bhz","便(糊状)");
		OTHER_OPT_CODE_NAME.put(BODYSIGN_ITEM_MOREN, "默认");
		OTHER_OPT_CODE_NAME.put("zxpb", "自主排便");
	}

	private BodySignConstants() {
		throw new AssertionError();
	}

	public enum BodySignDataChoiceStrategy {
		EARLIEST(0), // 最早earliest
		LATEST(1), // 最新Latest
		SIX_POINT(2); // 六个时间点

		private int value;
		private BodySignDataChoiceStrategy(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
	}

	public enum BodySignIndexStrategy {
		BEFORE(0), // 设置的时间点为结束时间点，往前推4小时
		MIDDLE(1), // 设置时间点为中间时间点，往前后个推2小时
		AFTER(2); // 设置时间点为结束时间点，往后推4小时
		private int value;
		private BodySignIndexStrategy(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
	}

	public enum BodySignTimePointStrategy {
		ZERO(0), // 对应 BODY_SIGN_ZERO_TIME
		FIRST(1), // 对应 BODY_SIGN_FIRST_TIME
		SECOND(2), // 对应 BODY_SIGN_SECOND_TIME
		THREE(3); // 对应 BODY_SIGN_THREE_TIME
		private int value;

		private BodySignTimePointStrategy(int value) {
			this.value = value;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}

	}
	//生命体征默认选择策略(最新数据,前4小时,3-7-11-15-19-23)
	public static List<String> BODY_SIGN_DATA_DEFAULT_STRATEGY = Arrays.asList(
			String.valueOf(2), String.valueOf(0), String.valueOf(3));

	// 体温单时间点常量
	public static final int[] BODY_SIGN_ZERO_TIME = { 0, 4, 8, 12, 16, 20 };
	public static final int[] BODY_SIGN_FIRST_TIME = { 1, 5, 9, 13, 17, 21 };
	public static final int[] BODY_SIGN_SECOND_TIME = { 2, 6, 10, 14, 18, 22 };
	public static final int[] BODY_SIGN_THREE_TIME = { 3, 7, 11, 15, 19, 23 };
	
	//事件显示时间段
	public static final String BODY_SIGN_EVENT_TIME = "eventHours";
	
	//生命体征批量录入的项目
	public static final String BATCH_BODYSIGN_ITEM = "batchBodysignItem";

	/** 一天6次的项目 */
	public static final List<String> SIX_TIMES_PER_DAY_ITEMS = Arrays.asList(
			TEMPERATURE, COOLED_TEMP,COOL_WAY, HEARTRATE, PULSE, PAIN, BREATH);
	/** 一天1次的项目 */
	public static final List<String> ONCE_PER_DAY_ITEMS = Arrays.asList(
			TOTAL_INPUT, STOOL, URINE, OTHEROUTPUT, WEIGHT, OTHER,
			INTAKE, HEIGHT, BLOOD_GLU, ABDOMINAL_CIR, TOTALOUTPUT,OTHER_ONE,OTHER_TWO,PPD,URINE_TIMES,
			BLOODPRESS1,BLOODPRESS2);
	/** 一天2次的项目 */
	public static final List<String> TWICE_PER_DAY_ITEMS = Arrays
			.asList(BLOODPRESS,OXYGEN_SATURATION);
	/** 一天3次的项目 */
	public static final List<String> THREE_TIMES_PER_DAY_ITEMS = Arrays
			.asList();

	// 事件录入，限制一个病人只能录入一次的事件
	public static final List<String> EVENT_UNIQUE_LIST = Arrays.asList(
			BODYSIGN_ITEM_DIED, BODYSIGN_ITEM_RY, BODYSIGN_ITEM_CY);
	// 护理文书转抄项
	public static final List<String> DOC_REPORT_BODYSIGN_ITEM = Arrays.asList(
			BLOODPRESS, BREATH, HEARTRATE, PULSE, TEMPERATURE,OXYGEN_SATURATION);
	
	public enum BodySignStatus {
		NEW("01"), // 新建
		DELETE("09"); // 已删除
		
		private String value;

		private BodySignStatus(String value) {
			this.value = value;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}

	}
	
	/**
	 * 获取罗马数字
	 * @author Administrator
	 *
	 */
	public enum SS_LM {
		SS0(0,""), 
		SS1(1,"I"), 
		SS2(2,"II"),
		SS3(3,"III"),
		SS4(4,"IV"),
		SS5(5,"V"),
		SS6(6,"VI"),
		SS7(7,"VII"),
		SS8(8,"VIII"),
		SS9(9,"IX"),
		SS10(10,"X");
		
		private int value;
		private String day;

		private SS_LM(int value,String day) {
			this.value = value;
		}
		public String getDay(int value) {
			return day;
		}
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public String getDay() {
			return day;
		}
		public void setDay(String day) {
			this.day = day;
		}
	}
	
	public static int RY_IN_OUT_PUT_STATISTIC_HOUR = 7;
	
/*	public static final List<String> BODY_SIGN_RECORD_CODE = Arrays.asList("ZERO_POINT","FIRST_POINT","SECOND_POINT",
			"THIRD_POINT","FOURTH_POINT","FIFTH_POINT","SIX_POINT");*/
	
}
