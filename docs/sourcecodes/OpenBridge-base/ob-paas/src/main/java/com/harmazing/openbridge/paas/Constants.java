package com.harmazing.openbridge.paas;


/**
 * 系统常量
 * 
 * @author Garen
 *
 */
public enum Constants {

	DEV("dev", "开发环境"), TEST("test", "测试环境"), LIVE("live", "正式环境"),

	BUILD("build", "编译服务器"),

	DOCKER("docker", "Docker服务器"),

	NGINX("nginx", "Nginx服务器"),

	SVN("svn", "Svn服务器"),

	MAX_MYSQL_WEIGTH("1000", "最大Mysql负载"),

	MAX_NGINX_WEIGTH("1000", "最大Nginx负载");

	private Constants(String name, String value) {
		this.name = name;
		this.value = value;
	}

	private String name;

	private String value;

	public static String getValue(String name) {
		for (Constants con : Constants.values()) {
			if (con.name.equals(name)) {
				return con.value;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
