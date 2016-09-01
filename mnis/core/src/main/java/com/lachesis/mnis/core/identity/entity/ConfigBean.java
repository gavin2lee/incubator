package com.lachesis.mnis.core.identity.entity;


public class ConfigBean{

	public enum ConfigType{
		S, //system config
		D, //deptment config
		U, // user config
		PS //pc system
	}
	
	private int id;
	private String code;
	private String name;
	private ConfigType type;
	/**拥有者code，可能为user code 或demtment code*/
	private String owner;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConfigType getType() {
		return type;
	}

	public void setType(ConfigType type) {
		this.type = type;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
