package com.lachesis.mnis.core.event.entity;

import java.util.Date;
import java.util.List;

/**
 * 文书转抄 事件实体
 * @author ThinkPad
 *
 */
public class DocReportEventEntity {
	private Date createTime;
	private String create_person;//创建人
	private String inpatient_no;
	private String dept_code;
	private List<DocReportEventItemEntity> data_item;//仅提供给NDA，本来可以与data_item_list合并的，但是NDA不愿改其结构，只好做两个了
	private String date_list;//日期
	private String time_list ;//时间 
	private String barcode;//条码
	
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	private String approve_person;  //核对护士
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreate_person() {
		return create_person;
	}
	public void setCreate_person(String create_person) {
		this.create_person = create_person;
	}
	public String getInpatient_no() {
		return inpatient_no;
	}
	public void setInpatient_no(String inpatient_no) {
		this.inpatient_no = inpatient_no;
	}
	public List<DocReportEventItemEntity> getData_item() {
		return data_item;
	}
	public void setData_item(List<DocReportEventItemEntity> data_item) {
		this.data_item = data_item;
	}
	public String getDate_list() {
		return date_list;
	}
	public void setDate_list(String date_list) {
		this.date_list = date_list;
	}
	public String getTime_list() {
		return time_list;
	}
	public void setTime_list(String time_list) {
		this.time_list = time_list;
	}
	public String getApprove_person() {
		return approve_person;
	}
	public void setApprove_person(String approve_person) {
		this.approve_person = approve_person;
	}

	public String getDept_code() {
		return dept_code;
	}

	public void setDept_code(String dept_code) {
		this.dept_code = dept_code;
	}
	
}
