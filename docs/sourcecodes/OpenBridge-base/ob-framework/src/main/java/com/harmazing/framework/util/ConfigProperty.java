package com.harmazing.framework.util;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

public class ConfigProperty extends PropertiesLoaderSupport implements
		PlaceholderResolver {
	private static final Log logger = LogFactory.getLog(ConfigProperty.class);

	private static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

	private static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";
	private static final String DEFAULT_VALUE_SEPARATOR = ":";
	private boolean ignoreUnresolvablePlaceholders = false;

	private ConfigurableEnvironment environment = null;

	protected String resolveSystemProperty(String key) {
		try {
			String value = System.getenv(key);

			if (value == null) {
				value = System.getProperty(key);
			}

			return value;
		} catch (Throwable ex) {
			if (logger.isDebugEnabled()) {
				logger.debug("Could not access system property '" + key + "': "
						+ ex);
			}
			return null;
		}
	}

	@Override
	public String resolvePlaceholder(String placeholder) {

		String propVal = resolveSystemProperty(placeholder);
		if (propVal == null) {
			if (environment != null) {
				propVal = environment.getProperty(placeholder);
			}
		}
		if (propVal == null) {
			propVal = config.getProperty(placeholder);
		}
		return propVal;
	}

	private PropertyPlaceholderHelper helper;
	private Properties config = null;

	public ConfigProperty(String configLocation) {
		this(configLocation, null);
	}

	public ConfigProperty(String configLocation,
			ConfigurableEnvironment environment) {
		try {
			this.environment = environment;
			this.setLocations(ResourceUtil.getResources(configLocation));
			this.config = this.mergeProperties();
			this.helper = new PropertyPlaceholderHelper(
					DEFAULT_PLACEHOLDER_PREFIX, DEFAULT_PLACEHOLDER_SUFFIX,
					DEFAULT_VALUE_SEPARATOR, ignoreUnresolvablePlaceholders);
		} catch (IOException e) {
			if (logger.isDebugEnabled()) {
				logger.error("Could not load properties: '" + configLocation
						+ "' ", e);
			}
		}
	}

	public void setProperty(String key, String value) {
		config.put(key, value);
	}

	public Map<String, String> getAllConfig() {
		Map<String, String> x = new HashMap<String, String>();
		Enumeration<Object> it = config.keys();
		while (it.hasMoreElements()) {
			Object key = it.nextElement();
			x.put(key.toString(), config.getProperty(key.toString()));
		}
		return x;
	}

	private String resolveStringValue(String strVal) {
		return this.helper.replacePlaceholders(strVal, this);
	}

	public String getProperty(String key) {
		String val = resolvePlaceholder(key);
		if (val != null) {
			return resolveStringValue(val);
		}
		return null;
	}

	public static void main(String[] args) {
		ConfigProperty config = new ConfigProperty(
				"classpath:application.properties");
		System.out.println(config.getProperty("service.port"));

	}
}
