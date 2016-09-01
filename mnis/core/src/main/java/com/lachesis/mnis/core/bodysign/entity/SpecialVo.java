package com.lachesis.mnis.core.bodysign.entity;

/**
 *取第一次录入生命体征数据，体征元素与文书元素共用
 *取key：value		
 * @author qingzhi.liu
 *
 */
public class SpecialVo {
	private String record_key;   //数据源
	private String record_value;   //数据值
	
	public SpecialVo() {
		super();
	}
	
	
	public SpecialVo(String record_key, String record_value) {
		super();
		this.record_key = record_key;
		this.record_value = record_value;
	}

	public String getRecord_key() {
		return record_key;
	}
	public void setRecord_key(String record_key) {
		this.record_key = record_key;
	}
	public String getRecord_value() {
		return record_value;
	}
	public void setRecord_value(String record_value) {
		this.record_value = record_value;
	}
	
	
}
