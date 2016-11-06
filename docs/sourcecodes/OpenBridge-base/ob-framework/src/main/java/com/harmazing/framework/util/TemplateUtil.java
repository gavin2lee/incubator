package com.harmazing.framework.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class TemplateUtil {
	private static Configuration cfg;
	private static StringTemplateLoader loader = new StringTemplateLoader();

	public static Configuration getConfiguration() {
		if (cfg == null) {
			cfg = new Configuration(Configuration.VERSION_2_3_23);
			cfg.setTemplateLoader(loader);
		}
		return cfg;
	}

	public static Template getTemplate(String name) throws IOException {
		return getConfiguration().getTemplate(name);
	}

	public static Template buildTemplate(String templateStr) throws IOException {
		String name = Md5Util.getMD5String(templateStr);
		Template template = null;
		try {
			template = getTemplate(name);
		} catch (TemplateNotFoundException e) {
			template = null;
		}
		if (template != null) {
			return template;
		} else {
			return new Template(name, templateStr, cfg);
		}
	}

	public static String getOutputByTemplate(String templateStr,
			Map<String, Object> model) throws IOException {
		Template template = buildTemplate(templateStr);
		StringWriter out = new StringWriter();
		try {
			template.process(model, out);
		} catch (TemplateException e) {
			throw new IOException(e);
		}
		return out.toString();
	}

	public static String getOutputByName(String name, Map<String, Object> model)
			throws IOException {
		Template template = getTemplate(name);
		StringWriter out = new StringWriter();
		try {
			template.process(model, out);
		} catch (TemplateException e) {
			throw new IOException(e);
		}
		return out.toString();
	}

	public static void main(String[] srgs) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ss", "aaaaaa");
		System.out.println(getOutputByTemplate("ss${ss}ss", map));
		System.out.println(getOutputByTemplate("ss${ss}ss", map));
	}
}
