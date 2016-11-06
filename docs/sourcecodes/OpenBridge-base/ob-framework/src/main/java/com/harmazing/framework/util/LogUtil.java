package com.harmazing.framework.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LogUtil {
	private static final Log logger = LogFactory.getLog(LogUtil.class);

	public static void info(Object arg0) {
		if (logger.isInfoEnabled())
			logger.info(arg0);
	}

	public static void info(Object arg0, Throwable t) {
		if (logger.isInfoEnabled())
			logger.info(arg0, t);
	}

	public static void warn(Object arg0) {
		if (logger.isWarnEnabled())
			logger.warn(arg0);
	}

	public static void warn(Object arg0, Throwable t) {
		if (logger.isWarnEnabled())
			logger.warn(arg0);
	}

	public static void debug(Object arg0) {
		if (logger.isDebugEnabled())
			logger.debug(arg0);
	}

	public static void debug(Object arg0, Throwable t) {
		if (logger.isDebugEnabled())
			logger.debug(arg0, t);
	}

	public static void error(Object arg0) {
		if (logger.isErrorEnabled())
			logger.error(arg0);
	}

	public static void error(Object arg0, Throwable t) {
		if (logger.isErrorEnabled())
			logger.error(arg0, t);
	}
}
