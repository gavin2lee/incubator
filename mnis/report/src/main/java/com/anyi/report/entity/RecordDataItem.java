package com.anyi.report.entity;

import java.util.List;

public class RecordDataItem {
	private String template_item_id;//数据源代码
	private String record_value;//客户录入的数据
	private String record_item_id;//流水号
	private String record_id;//数据ID(关联主表流水号)
	private String parent_id;//父数据节点
	/*private List <RecordDataItemList> list_item;//针对复选框多个选项值，保存多条数据
*/	private List<RecordDataItem> list_item;
	
	
	public List<RecordDataItem> getList_item() {
		return list_item;
	}
	public void setList_item(List<RecordDataItem> list_item) {
		this.list_item = list_item;
	}
	/*public List<RecordDataItemList> getList_item() {
		return list_item;
	}
	public void setList_item(List<RecordDataItemList> list_item) {
		this.list_item = list_item;
	}*/
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
	public String getParent_id() {
		return parent_id;
	}
	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

}
