package com.lachesis.mnisqm.configuration;

/**
 * 数据库配置类
 * @author Paul Xu
 *
 */
public class DatabaseConfiguration {
	private String driverClass;
	private String dbUrl;
	private String user;
	private String password;

	public DatabaseConfiguration(String driverClass, String dbUrl, String user,
			String password) {
		this.driverClass = driverClass;
		this.dbUrl = dbUrl;
		this.user = user;
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
