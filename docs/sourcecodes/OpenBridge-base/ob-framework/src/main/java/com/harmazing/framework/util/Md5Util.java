package com.harmazing.framework.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Md5Util {

	public static String HMAC_MD5_NAME = "HmacMD5";
	/**
	 * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
	 */
	protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private final static ThreadLocal<MessageDigest> messagedigest = new ThreadLocal<MessageDigest>() {
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				System.err.println(Md5Util.class.getName()
						+ "初始化失败，MessageDigest不支持MD5Util。");
				return null;
			}
		}
	};
	private final static ThreadLocal<Mac> macsignature = new ThreadLocal<Mac>() {
		protected Mac initialValue() {
			try {
				return Mac.getInstance(HMAC_MD5_NAME);
			} catch (NoSuchAlgorithmException e) {
				System.err.println(Md5Util.class.getName()
						+ "初始化失败，Mac不支持MD5Util。");
				return null;
			}
		}
	};

	public static String macMd5Encode(String key, String str)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec sk = new SecretKeySpec(key.getBytes(), HMAC_MD5_NAME);
		Mac mac = macsignature.get();
		mac.reset();
		mac.init(sk);
		return bufferToHex(mac.doFinal(str.getBytes()));
	}

	/**
	 * 生成字符串的md5校验值
	 *
	 * @param s
	 * @return
	 */
	public static String getMD5String(String s) {
		return getMD5String(s.getBytes());
	}

	/**
	 * 生成字符串的md5校验值
	 *
	 * @param s
	 * @return
	 */
	public static String getMD5String(String s, String encoding) {
		try {
			return getMD5String(s.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public static String getPassword(String str) {
		if (str.startsWith("md5:") || str.startsWith("MD5:")) {
			str = str.substring(4);
		} else {
			str = getMD5String(str.getBytes());
		}
		str = str + StringUtil.getTime33(str) + "@hemeicloud.COM";
		return getMD5String(str.getBytes());
	}

	/**
	 * 生成字节的md5校验码
	 *
	 * @param bytes
	 * @return
	 */
	public static String getMD5String(byte[] bytes) {
		messagedigest.get().reset();
		messagedigest.get().update(bytes);
		return bufferToHex(messagedigest.get().digest());
	}

	public static String bufferToHex(byte bytes[]) {
		return bufferToHex(bytes, 0, bytes.length);
	}

	private static String bufferToHex(byte bytes[], int m, int n) {
		StringBuffer stringbuffer = new StringBuffer(2 * n);
		int k = m + n;
		for (int l = m; l < k; l++) {
			appendHexPair(bytes[l], stringbuffer);
		}
		return stringbuffer.toString();
	}

	private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
		char c0 = hexDigits[(bt & 0xf0) >> 4];// 取字节中高 4 位的数字转换, >>>
		// 为逻辑右移，将符号位一起右移,此处未发现两种符号有何不同
		char c1 = hexDigits[bt & 0xf];// 取字节中低 4 位的数字转换
		stringbuffer.append(c0);
		stringbuffer.append(c1);
	}

	public static void main(String[] args) {
		// Md5Util md5 = new Md5Util();
		// for (int i = 0; i < 2000; i++) {
		// R r = md5.new R();
		// Thread t = new Thread(r);
		// t.start();
		// }
		System.out.println(getMD5String("123456"));
		System.out.println(getMD5String("123456"));
		System.out.println(getPassword("123456"));
		System.out.println(getPassword("md5:e10adc3949ba59abbe56e057f20f883e"));
	}

	class R implements Runnable {
		public void run() {
			System.out.println(Md5Util.getMD5String(new Random().nextFloat()
					+ ""));
		}
	}

	public static String getSH1String(String str) {
		if (str == null) {
			return null;
		}
		return getSH1String(str, "UTF-8");
	}

	public static String getSH1String(String str, String encoding) {
		if (str == null) {
			return null;
		}
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(str.getBytes(encoding));
			return bufferToHex(messageDigest.digest());
		} catch (Exception e) {
			return null;
		}
	}

}
