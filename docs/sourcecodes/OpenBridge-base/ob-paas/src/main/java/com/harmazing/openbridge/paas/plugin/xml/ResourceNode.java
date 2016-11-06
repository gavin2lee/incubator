package com.harmazing.openbridge.paas.plugin.xml;

import com.harmazing.framework.extension.config.Configuration;
import com.harmazing.framework.util.SpringUtil;

@SuppressWarnings("serial")
public class ResourceNode extends Configuration {
	public static final String pointId = "com.harmazing.paas.resource";
	public static final String nodeName = "resource";
	private String id;
	private String name;
	private String category;
	private String config;
	private String show;
	private String bean;
	private String summary;
	private String help;
	private String icon;

	@Override
	public String getId() {
		return this.id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public ResourceBridge getResourceBridge() {
		return (ResourceBridge) SpringUtil.getBean(bean);
	}

	public String getShow() {
		return show;
	}

	public String getBean() {
		return bean;
	}

	public void setBean(String bean) {
		this.bean = bean;
	}

	public void setShow(String show) {
		this.show = show;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void setId(String id) {
		this.id = id;
	}
}
