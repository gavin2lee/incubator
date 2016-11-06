package com.harmazing.framework.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.taglibs.standard.tag.common.core.Util;
import org.springframework.util.StringUtils;

public abstract class StringUtil extends StringUtils {
	public static String urlEncode(Object obj) {
		if (obj == null)
			return "";
		try {
			return java.net.URLEncoder.encode(obj.toString(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return e.getMessage();
		}
	}

	public static String escapeXml(String str){
		return Util.escapeXml(str);
	}
	public static boolean isNull(String str) {
		if (str == null || str.trim().length() <= 0)
			return true;
		else
			return false;
	}

	public static boolean isNotNull(String str) {
		return !isNull(str);
	}

	public static int getTime33(String str) {
		int hash = 5381;
		for (int i = 0, len = str.length(); i < len; ++i) {
			hash += (hash << 5) + str.charAt(i);
		}
		return hash & 0x7fffffff;
	}

	public static String upperFirst(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	public static String lowerFirst(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	public static String[] split(String str) {
		if (StringUtil.isNull(str))
			return null;
		else
			return str.split("\\s*[,;]\\s*");
	}

	public static boolean isMobileNO(String mobiles) {
		if (isEmpty(mobiles)) {
			return false;
		}
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static String randomCode() {
		return randomCode(999999, 111111);
	}

	public static String randomCode(int max, int min) {
		return randomInt(max, min) + "";
	}

	public static int randomInt(int max, int min) {
		Random random = new Random();
		return (random.nextInt(max) % (max - min + 1) + min);
	}

	public static String getUUID(Date date, UUID uuid) {
		ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
		bb.putLong(uuid.getMostSignificantBits());
		bb.putLong(uuid.getLeastSignificantBits());
		long time = date.getTime();
		String temp = Long.toString(time, 26) + Base58.encode(bb.array());
		return temp.toLowerCase();
	}

	public static String getUUID() {
		return getUUID(new Date(), UUID.randomUUID());
	}

	public static Date getUUIDDate(String uuid) {
		String str = uuid.substring(0, 9);
		return new Date(Long.valueOf(str, 26));
	}

	public static String replaceArgs(String template, Map<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		try {
			Pattern pattern = Pattern.compile("\\!\\{(.+?)\\}");
			Matcher matcher = pattern.matcher(template);
			while (matcher.find()) {
				String name = matcher.group(1);
				String value = map.get(name) == null ? "" : map.get(name)
						.toString();
				if (value == null) {
					value = "";
				} else {
					value = value.replaceAll("\\$", "\\\\\\$");
				}
				matcher.appendReplacement(sb, value);
			}
			matcher.appendTail(sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static int getIntParam(HttpServletRequest request, String name,
			int defaultValue) {
		if (request.getParameter(name) == null) {
			return defaultValue;
		} else {
			try {
				return Integer.valueOf(request.getParameter(name)).intValue();
			} catch (Exception e) {
				return defaultValue;
			}
		}
	}

	
	/**
	 * 计算指定字符串出现的次数
	 * @param orgin 原字符串
	 * @param chars 指定字符串
	 * @return
	 */
	public static int getOccurTimes(String orgin,String chars){
		if(isNull(orgin) || isNull(chars)){
			return 0;
		}else{
			String temp=orgin.replace(chars, "");
			return (orgin.length()-temp.length())/chars.length();
		}
	}
	/**
	 * 获取简短的唯一串（当且仅当总数量小时使用）
	 * @return
	 */
	public static String getUUIDShort(){
		StringBuffer str=new StringBuffer();
		String uuid=getUUID(new Date(), UUID.randomUUID());
		//随机截取中间的一段
		int start=RandomUtil.getInstance().randomInt(0, uuid.length()-4);
		str.append(RandomUtil.getInstance().randAlpha(6));
		str.append(uuid.substring(start, start+4));
		return str.toString();
	}

	public static void main(String[] args) {
		String uuid = getUUID();
		System.out.println(uuid);
		System.out.println(getUUIDDate(uuid));
	}
}
