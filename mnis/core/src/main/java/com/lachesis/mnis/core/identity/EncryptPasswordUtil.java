package com.lachesis.mnis.core.identity;



public final class EncryptPasswordUtil {
	private static char[] KEY;

	static {
		KEY = IdentityConstants.ENCRYPT_PWD_KEY.toCharArray();
	}

	/**
	 * 字符解密
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String str) throws Exception {
		if (str.trim().isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		short num3 = (short) (str.length() - 1);
		for (short i = 0; i <= num3; i = (short) (i + 1)) {
			short charPos = getCharPos(str.substring(i, i + 1).charAt(0));
			if (charPos == 0) {
				charPos = (short) (KEY.length + 1);
			}
			builder.append(getChar((short) (((short) (charPos - i)) - 1)));
		}
		return builder.toString();
	}

	/**
	 * 字符加密
	 * 
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String encrypt(String str) throws Exception {
		if (str.trim().isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		short num3 = (short) (str.length() - 1);
		for (short i = 0; i <= num3; i = (short) (i + 1)) {
			short charPos = getCharPos(str.substring(i, i + 1).charAt(0));
			if (charPos == KEY.length) {
				charPos = -1;
			}
			builder.append(getChar((short) (((short) (charPos + i)) + 1)));
		}

		return builder.toString();
	}

	private static char getChar(short Pos) {
		while (Pos < 0) {
			if (Pos < 0) {
				Pos = (short) (96 + Pos);
			}
		}
		while (Pos >= 96) {
			if (Pos >= 96) {
				Pos = (short) (Pos - 96);
			}
		}
		return KEY[Pos];
	}

	private static short getCharPos(char ch) {
		short num = 0;
		short num3 = (short) (KEY.length - 1);
		for (short i = 0; i <= num3; i = (short) (i + 1)) {
			if (ch == KEY[i]) {
				return i;
			}
		}
		return num;
	}
	
	public static void main(String[] args) {
		
		try {
			System.out.println(encrypt("123"));
			System.out.println(decrypt("246"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
