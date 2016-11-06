package com.harmazing.framework.authorization.filter;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.harmazing.framework.util.XmlUtil;

public class SecurityConfigVO {

	private List<Module> modules = new ArrayList<Module>();

	public List<Module> getModules() {
		return this.modules;
	}

	public SecurityConfigVO(Document doc) {
		List<Element> items = XmlUtil.getChildElementsByTagName(
				doc.getDocumentElement(), "module");
		for (int i = 0; i < items.size(); i++) {
			modules.add(parseModuleElement(items.get(i), ""));
		}
	}

	private Module parseModuleElement(Element item, String pre) {
		Module module = new Module();
		module.prefix = pre + item.getAttribute("prefix");

		List<Element> urls = XmlUtil.getChildElementsByTagName(item, "url");
		for (int i = 0; i < urls.size(); i++) {
			module.interceptUrl.add(parseUrlElement(urls.get(i)));
		}
		List<Element> modules = XmlUtil.getChildElementsByTagName(item,
				"module");
		for (int i = 0; i < modules.size(); i++) {
			module.modules
					.add(parseModuleElement(modules.get(i), module.prefix));
		}
		return module;
	}

	private Url parseUrlElement(Element item) {
		Url url = new Url();
		url.pattern = item.getAttribute("pattern");
		url.validator = item.getAttribute("validator");

		return url;
	}

	public static class Module {
		private String prefix;
		private List<Url> interceptUrl = new ArrayList<Url>();
		private List<Module> modules = new ArrayList<Module>();

		public String getPrefix() {
			return this.prefix;
		}

		public List<Url> getUrls() {
			return this.interceptUrl;
		}

		public List<Module> getModules() {
			return this.modules;
		}
	}

	public static class Url {
		private String pattern;
		private String validator;

		public String getPattern() {
			return pattern;
		}

		public String getValidator() {
			return validator;
		}
	}
}
