package com.harmazing.openbridge.paas.nginx.vo;

/**
 * @author Garen
 *
 */
public class BalanceConfVo {

	private String ip;

	private String port;

	/**
	 * 权重，备用
	 */
	private String weight;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @param ip
	 * @param port
	 */
	public BalanceConfVo(String ip, String port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	/**
	 * 
	 */
	public BalanceConfVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

}