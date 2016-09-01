package com.lachesis.mnis.core.util;

public class ZiZhongEncryptUtil {
	public static String Encrypt(String ss){
		String allchar = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789";

		char[] result = ss.toCharArray();
		int l = result.length;
		int lac = allchar.length();

		int sp = 0;
		
		while (sp < l){
			int cp = 0;
			while ((cp < lac) && (allchar.charAt(cp) != ss.charAt(sp)))	{
				cp++;
			}

			if (cp == lac){
				result[sp] = '*';
			}else{
				result[sp] = allchar.charAt(lac - cp - 1);
			}
			sp++;
		}
		
		return new String(result);
	}

}
