package com.harmazing.framework.authorization.impl;

import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.harmazing.framework.util.Base58;
import com.harmazing.framework.util.ConfigUtil;
import com.harmazing.framework.util.CookieUtil;
import com.harmazing.framework.util.Md5Util;
import com.harmazing.framework.util.StringUtil;

@SuppressWarnings("serial")
public class YiheTokenGenerator implements Serializable {
	private static final Log logger = LogFactory
			.getLog(YiheTokenGenerator.class);

	public static String getTokenCookieName() {
		return ConfigUtil.getOrElse("paasos.token.cookie", "paasos-token");
	}

	public static String getTokenHeaderName() {
		return ConfigUtil.getOrElse("paasos.token.header", "paasos-token");
	}

	public static String getTokenSecretKey() {
		return ConfigUtil.getOrElse("paasos.token.secret", "yihecloud.com");
	}

	// 12 * 60 * 60 = 43200 秒
	public static int getTokenExpires() {
		return Integer.valueOf(ConfigUtil.getOrElse("paasos.token.expires",
				"43200")) * 1000;
	}

	private YiheTokenGenerator() {

	}

	private static YiheTokenGenerator generator;

	public static YiheTokenGenerator getInstance() {
		if (generator == null) {
			generator = new YiheTokenGenerator();
		}
		return generator;
	}

	public String getTokenByRequest(HttpServletRequest request) {
		String token = null;
		Cookie cookie = CookieUtil.getCookie(request, getTokenCookieName());
		if (cookie != null && StringUtil.isNotNull(cookie.getValue())) {
			token = cookie.getValue();
		}
		if (StringUtil.isNull(token)) {
			if (StringUtil.isNotNull(request.getHeader(getTokenHeaderName()))) {
				token = request.getHeader(getTokenHeaderName());
			}
		}
		return token;
	}

	public String signature(String userId) {
		// 数据签名放入请求头中
		try {
			String timestamp = System.currentTimeMillis() + "";
			String noncestr = StringUtil.getUUID();
			String data = userId + "|" + timestamp + "|" + noncestr;
			String sign = Md5Util.macMd5Encode(getTokenSecretKey(), data);
			data += "|" + sign;
			data = Base58.encode(data.getBytes());
			return data;
		} catch (InvalidKeyException e) {
			throw new RuntimeException("数据签名失败", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("数据签名失败", e);
		}
	}

	/**
	 * 成功返回用户ID，否则返回null
	 * 
	 * @param token
	 * @return
	 */
	public String verifyToken(String token) {
		if (StringUtil.isNull(token)) {
			return null;
		}
		try {
			String tokenStr = new String(Base58.decode(token));
			if (StringUtil.isNull(tokenStr)) {
				return null;
			}
			String[] temp = tokenStr.split("\\|");
			if (temp.length != 4) {
				return null;
			}
			if (Math.abs(Long.valueOf(temp[1]) - System.currentTimeMillis()) > getTokenExpires()) {
				return null;
			}
			String data = temp[0] + "|" + temp[1] + "|" + temp[2];
			String sign = Md5Util.macMd5Encode(getTokenSecretKey(), data);
			if (sign.equals(temp[3])) {
				return temp[0];
			}
			return null;
		} catch (Exception e) {
			logger.warn("paasos-token解析出错", e);
			return null;
		}
	}
}
