package com.lachesis.mnis.core.event.entity;

/**
 * 文书转抄 事件实体子项
 * @author ThinkPad
 *
 */
public class DocReportEventItemEntity {
	private String record_value;//客户录入的数据
	private Long bodySignId;//生命体征item明细ID
	private String status;//状态
	private String template_item_id;
	
	public DocReportEventItemEntity(){}
	
	public DocReportEventItemEntity(String template_item_id,String record_value){
		this.record_value = record_value;
		this.template_item_id = template_item_id;
	}
	
	public String getRecord_value() {
		return record_value;
	}
	public void setRecord_value(String record_value) {
		this.record_value = record_value;
	}
	public String getTemplate_item_id() {
		return template_item_id;
	}
	public void setTemplate_item_id(String template_item_id) {
		this.template_item_id = template_item_id;
	}

	public Long getBodySignId() {
		return bodySignId;
	}

	public void setBodySignId(Long bodySignId) {
		this.bodySignId = bodySignId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
