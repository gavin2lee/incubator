package com.harmazing.framework.util;

import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;

import org.springframework.core.io.Resource;

public abstract class MimeTypeUtil {
	private static FileTypeMap defaultMap = null;
	private static boolean isInit = false;

	private synchronized static void init() {
		try {
			Resource[] r = ResourceUtil
					.getResources("classpath:config/mime.types");
			defaultMap = new MimetypesFileTypeMap(r[0].getInputStream());
		} catch (Exception e) {
			LogUtil.error(e);
		}
		isInit = true;
	}

	public static String getContentType(String filename) {
		if (isInit == false) {
			init();
		}
		return defaultMap.getContentType(filename);
	}
}
