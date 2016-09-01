package com.anyi.report.constants;

/**
 * 
 * 文书打印常量类
 * @author chensongxing
 *
 */
public class DocPrintConstants {
	public static boolean bRecordAction = true;//是否记录操作行为
	public static String inOutTimeMorning = "07:00";//出入量统计时间，上午
	public static String inOutTimeAfternoon = "15:00";//出入量统计时间，上午
	public static int printResolution = 75;//文书打印前端的分辨率
	public static String enCharType = "微软雅黑";//文书打印的字体类型，英文
	public static String chCharType = "微软雅黑";//文书打印的字体类型，中文
	public static int charSize = 12 ;//文书打印的字体大小，单位：像素
	public static int lineHight = 1;//文书打印中表格线的高度，单位：像素
	public static int rowMargin = 6;//行中字体的边距，单位：像素
	public static int charHight = 18;//字体高度，单位：像素
	public static int oneRowHight = 20;//单行高度，单位：像素
	public static String NON_START_CHAR = "，。、；）】}%/,.;)]}%";//不能作为开头的字符
	public static String NON_END_CHAR = ".";//不能作为结尾的字符
}