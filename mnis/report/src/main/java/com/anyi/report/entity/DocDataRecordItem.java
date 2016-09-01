package com.anyi.report.entity;

/**
 * 
 * @author yanchang.chen
 *
 */
public class DocDataRecordItem {
	
	/**
	 * 此类用于把所有数据查询出来，再做数据递归处理，避免SQL性能问题
	 */
	
	private String record_item_id;//流水号
	private String record_id;//记录ID
	private String template_item_id;//数据源名称
	private String record_value;//数据值
	private String parent_id;//父节点ID
	
	public String getRecord_item_id() {
		return record_item_id;
	}
	public void setRecord_item_id(String record_item_id) {
		this.record_item_id = record_item_id;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getTemplate_item_id() {
		return template_item_id;
	}
	public void setTemplate_item_id(String template_item_id) {
		this.template_item_id = template_item_id;
	}
	public String getRecord_value() {
		return record_value;
	}
	public void setRecord_value(String record_value) {
		this.record_value = record_value;
	}
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}
	
	
}
