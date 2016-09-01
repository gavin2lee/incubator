package com.anyi.report.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期、时间树结构
 * @author qingzhi.liu 2015.06.19
 *
 */
public class TimeTreeVo {
	private String template_id;   //模板id
	private String record_id;  //主记录id
	private String data_value;   //日期、时间
	private String data_index;//记录编号
	private String pat_id;  //患者id
	private String type;  //NDA、PC
	private List<TimeTreeVo> data=new ArrayList<TimeTreeVo>();

	public TimeTreeVo() {
		super();
	}

	public String getTemplate_id() {
		return template_id;
	}

	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	public String getData_value() {
		return data_value;
	}

	public void setData_value(String data_value) {
		this.data_value = data_value;
	}

	public List<TimeTreeVo> getData() {
		return data;
	}

	public void setData(List<TimeTreeVo> data) {
		this.data = data;
	}

	public String getPat_id() {
		return pat_id;
	}

	public void setPat_id(String pat_id) {
		this.pat_id = pat_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getData_index() {
		return data_index;
	}

	public void setData_index(String data_index) {
		this.data_index = data_index;
	}

	@Override
	public String toString() {
		return "TimeTreeVo [template_id=" + template_id + ", record_id="
				+ record_id  + ", data_value="
				+ data_value + ", pat_id=" + pat_id + ", data=" + data + "]";
	}

	
	
}
