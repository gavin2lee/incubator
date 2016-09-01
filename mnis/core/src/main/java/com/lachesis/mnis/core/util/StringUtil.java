package com.lachesis.mnis.core.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.lachesis.mnis.core.constants.MnisConstants;

/***
 * 
 * 字符串相关工具类
 *
 * @ClassName: DateUtil 
 * @author yuliang.xu
 * @date 2015年4月30日 下午6:08:09 
 *
 * @see org.apache.commons.lang.StringUtils
 */
public class StringUtil {
	
	public static final String UNDER_SCORE = "_";
	public static final String COMMA = ",";
	/**换行符*/
	public static final String LINE_BREAK = "\n";
	
	/**
	 * 获取 UUID
	 * @return UUID
	 */
	public static String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");   
	}
	
	/***
	 * 替换字符串中的字符
	 * @param text
	 * @return String    返回类型 
	 */
	public static String replace(String text){
		return StringUtils.replaceEach(text, new String[]{"-", ":", " "}, new String[]{"","",""});
	}
	
	/**
	 * 计算字符串是否包含在一个字符串集合中
	 * 
	 * @param texts
	 * @param text
	 * @return
	 */
	public static boolean isContainText(List<String> texts, String text) {
		if(texts == null || StringUtils.isBlank(text)){
			return false;
		}
		
		boolean flag = false;
		for (int i = 0; i < texts.size(); i++) {
			if (text.equals(texts.get(i))) {
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	/**
	 * 正则表达式匹配
	 * 
	 * @param regex
	 * @param flags
	 * @param input
	 * @return
	 */
	public static boolean regxMatch(String regex, int flags, String input) {
		return Pattern.compile(regex, flags).matcher(input).find();
	}
	
	/**
	 * 验证字符长度
	 * 
	 * @param string
	 *            验证字符，中文占2个字符
	 * @param maxLenth
	 *            最大允许长度
	 * @return boolean(true：string长度在maxLength范围；false：超出maxLength)
	 */
	public static boolean validationiCharLength(String string, int maxLenth) {
		int length = 0;
		for (String str : string.split("")) {
			if (regxMatch("[^u4E00-u9FA5]", Pattern.CASE_INSENSITIVE, str)) {
				length += 2;
			} else {
				length++;
			}
		}
		if (length - 1 > maxLenth) {
			return false;
		}
		return true;
	}

	/**
	 * 验证一个字符串是否为数字
	 * 
	 * @param value
	 * @return true-数字，false-非数字
	 */
	public static boolean isANumber(String value) {
		if (regxMatch("[^0-9]", Pattern.CASE_INSENSITIVE, value)) {
			return false;
		}
		return true;
	}
	
	/**
	 * 将图片byte[]转化为字符串
	 * 
	 * @param imgBytes
	 * @return
	 */
	public static String byteToString(byte[] imgBytes) {
		// decoding byte array into base64
		// byte->加密byte
		return Base64.encodeBase64String(imgBytes);
	}

	/**
	 * 将图片字符串转化为字节byte[]
	 * 
	 * @param imgStr
	 * @return
	 */
	public static byte[] stringToByte(String imgStr) {
		// encoding byte array into base 64
		// 加密字符转为加密byte
		return Base64.decodeBase64(imgStr);
	}
	
	/**
	 * 把是整数的数据，小数点去除
	 * @param v
	 * @return
	 */
	public static String getStringValue(double v){
		String sv = String.valueOf(v);
		String[] svs = sv.split("\\.");
		String out="";
		if(svs.length<=1){
			out = sv;
		}else{
			int xs = Integer.parseInt(svs[1]);
			if(xs>0){
				out=sv;
			}else{
				out = svs[0];
			}
		}
		return out;
	}
	
	/***
	 * 将字符串转为boolean
	 * @param strValue
	 * @return  boolean    返回类型 
	 */
	public static boolean stringToBoolean(String strValue) {
		return ((strValue != null) && (strValue.equalsIgnoreCase("true")
				|| strValue.equalsIgnoreCase("on")
				|| strValue.equalsIgnoreCase("yes")
				|| strValue.toLowerCase().startsWith("y") || strValue
				.toLowerCase().startsWith("1")));
	}
	
	/**
	 * 
	 * 将列表转化成字符串，中间以特定字符隔开
	 * 
	 * @param list
	 * @return
	 */
	public static String formatListToString(List<String> list, String digit) {
		StringBuffer result = new StringBuffer();
		if (list == null || list.isEmpty()) {
			return null;
		}
		for (String s : list) {
			result.append(s).append(digit);
		}
		result.deleteCharAt(result.length() - 1);
		return result.toString();
	}
	
	public static boolean hasValue(String val){
		if(val==null||"".equals(val)||"".equals(val.replace(" ", "　 ").trim())){
			return false;
		}
		return true;
	}
	
	public static String validEmpty(String str){
		if(StringUtils.isBlank(str)){
			return "";
		}
		
		return str;
	}
	
	public static void writeStrToFile(String writeStr,String fileName){
		 FileWriter writer;
	        try {
	            writer = new FileWriter(fileName);
	            writer.write(writeStr);
	            writer.flush();
	            writer.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	
	/**
	 * 生成异常消息
	 * @param orderType
	 * @param code
	 * @param msg
	 * @return
	 */
	public static String createErrorMsg(String orderType,
			String code,String msg){
		StringBuilder stringBuilder = new StringBuilder();
		if(StringUtils.isBlank(code)){
			code = MnisConstants.EMPTY;
		}
		if(StringUtils.isBlank(msg)){
			msg = MnisConstants.EMPTY;
		}
		
		if(StringUtils.isBlank(orderType)){
			orderType = MnisConstants.EMPTY;
		}
		stringBuilder.append(orderType).append(MnisConstants.LINE)
		.append(code).append(MnisConstants.LINE)
		.append(msg);
		return stringBuilder.toString();
	}
	
	/**
	 * 获取异常消息
	 * @param msg
	 * @return
	 */
	public static String getErrorMsg(String msg) {
		if (StringUtils.isBlank(msg)) {
			return msg;
		}
		String[] msgs = msg.split(MnisConstants.LINE);
		if (msgs.length > 2) {
			msg = msgs[2];
		}
		return msg;
	}
	
	/**
	 * 生成http url
	 * @param address
	 * @param url
	 * @return
	 */
	public static String createHttpUrl(String address,String url){
		StringBuilder stringBuilder = new StringBuilder();
		if(StringUtils.isBlank(address)){
			return stringBuilder.toString();
		}
		stringBuilder.append(address);
		if(StringUtils.isNotBlank(url)){
			stringBuilder.append(url);
		}
		
		if(stringBuilder.indexOf(MnisConstants.HTTP)== -1){
			stringBuilder.insert(0, MnisConstants.HTTP);
		}
		
		return stringBuilder.toString();
		
	}
	
	private StringUtil(){}
}
