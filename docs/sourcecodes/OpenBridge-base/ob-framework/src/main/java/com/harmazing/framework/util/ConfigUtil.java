package com.harmazing.framework.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import com.harmazing.framework.util.JdbcUtil.IResultSetCallback;

public abstract class ConfigUtil {
	// 默认配置文件路径
	private static String CLASSPATH_CONFIG_PROPERTIES_PATH = "/application.properties";
	private static final Log logger = LogFactory.getLog(ConfigUtil.class);
	private static boolean isInit = false;
	// 配置文件内容
	private static ConfigProperty config = null;

	public static final void init() throws RuntimeException {
		if (isInit) {
			return;
		}
		loadProperties(null);
	}

	public static final void init(ConfigurableEnvironment environment)
			throws RuntimeException {
		if (isInit) {
			return;
		}
		loadProperties(environment);
	}

	public static void updateConfig(Map<String, String> config) {
		init();
		Iterator<Entry<String, String>> it = config.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> type = it.next();
			ConfigUtil.config.setProperty(type.getKey(), type.getValue());
		}
	}

	public static Map<String, String> getAllConfig() {
		init();
		return config.getAllConfig();
	}

	/**
	 * 系统启动时会调用该方法。该方法会先加载配置文件，然后再加载数据库的sys_config表的内容。
	 * 
	 * @throws RuntimeException
	 */
	public static void loadPropertiesOnStartup() throws RuntimeException {
		loadPropertiesOnStartup(null);
	}

	public static void loadPropertiesOnStartup(
			ConfigurableEnvironment environment) throws RuntimeException {
		init(environment);
		String driver = ConfigUtil.getConfigString("jdbc.driver");
		String url = ConfigUtil.getConfigString("jdbc.url");
		String username = ConfigUtil.getConfigString("jdbc.username");
		String password = ConfigUtil.getConfigString("jdbc.password");
		Connection conn = null;
		try {
			try {
				conn = JdbcUtil.getConnection(driver, url, username, password);
			} catch (Exception e) {
				throw new Exception("jdbc error", e);
			}
			final Map<String, String> config = new HashMap<String, String>();
			JdbcUtil.getResultSet(conn, "select * from sys_config",
					new IResultSetCallback() {
						public void exec(ResultSet rs) throws SQLException {
							while (rs.next()) {
								String key = rs.getString("conf_key");
								String value = rs.getString("conf_value");
								config.put(key, value);
							}
						}
					});
			JdbcUtil.getResultSet(conn, "select * from sys_core_config",
					new IResultSetCallback() {
						public void exec(ResultSet rs) throws SQLException {
							while (rs.next()) {
								String key = rs.getString("key");
								String value = rs.getString("value");
								config.put(key, value);
							}
						}
					});
			updateConfig(config);
		} catch (Exception e) {
			if (e.getMessage().equals("jdbc error")) {
				throw new RuntimeException("数据库连连接配置信息 jdbc error=|" + driver
						+ "|" + url + "|" + username + "|" + password + "|", e);
			} else {
				throw new RuntimeException("读取配置信息出错，请检查数据库配置信息", e);
			}
		} finally {
			JdbcUtil.closeConnection(conn);
		}
	}

	public static void loadProperties(ConfigurableEnvironment environment)
			throws RuntimeException {
		ConfigUtil.config = new ConfigProperty(
				CLASSPATH_CONFIG_PROPERTIES_PATH, environment);
		isInit = true;
	}

	/**
	 * 获取 .proerties的信息
	 *
	 * @param key
	 * @return
	 */
	public static String getConfigString(String key) {
		return getOrElse(key, null);
	}

	/**
	 * 获取 .proerties的信息
	 *
	 * @param key
	 * @return
	 */
	public static String getOrElse(String key, String defaultValue) {
		init();
		try {
			String val = config.getProperty(key);
			if (val == null)
				return defaultValue;
			else
				return val;
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Long getConfigLong(String key, Long defaultValue) {
		String value = getOrElse(key, null);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.valueOf(value);
		} catch (NumberFormatException e) {
			logger.error("format number error,key:" + key + " value" + value, e);
			return defaultValue;
		}
	}

	public static Integer getConfigInt(String key, Integer defaultValue) {
		String value = getOrElse(key, null);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException e) {
			logger.error("format number error,key:" + key + " value" + value, e);
			return defaultValue;
		}
	}

	public static Float getConfigFloat(String key, Float defaultValue) {
		String value = getOrElse(key, null);
		if (value == null) {
			return defaultValue;
		}
		try {
			return Float.valueOf(value);
		} catch (NumberFormatException e) {
			logger.error("format number error,key:" + key + " value" + value, e);
			return defaultValue;
		}
	}

	public static void main(String[] args) {
		String s = "192.168.11.53";
		Pattern pa = Pattern.compile("([0-9]{1,3}\\.{0,1}){4}");
		Matcher ma = pa.matcher(s);
		// System.out.println(ma.find());
		if (ma.find()) {
			System.out.println(ma.group());
		}
	}
}
