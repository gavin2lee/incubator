package com.lachesis.mnis.core.redis;

public class RedisServerInfo{
	private String host;
	private int port;
	private String password;

	public void setHost(String host){
		this.host = host;
	}
	public void setPort(int port){
		this.port = port;
	}
	public void setPassword(String password){
		this.password = password;
	}
	public String getHost(){
		return host;
	}
	public int getPort(){
		return port;
	}
	public String getPassword(){
		return password;
	}
}