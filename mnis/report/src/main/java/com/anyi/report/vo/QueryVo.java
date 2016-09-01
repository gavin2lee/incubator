package com.anyi.report.vo;

/**
 * 查询字段接受类
 * @author qingzhi.liu 2015.06.19
 *
 */
public class QueryVo {
	private String template_id;  //模板id
	private String record_id;    //记录id
	private String data_value;   //日期
	private String begin_date;   //条件：查询开始时间
	private String end_date;   //条件：查询结束时间
	public QueryVo() {
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
	public String getBegin_date() {
		return begin_date;
	}
	public void setBegin_date(String begin_date) {
		this.begin_date = begin_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	@Override
	public String toString() {
		return "QueryVo [template_id=" + template_id + ", record_id="
				+ record_id + ", data_value=" + data_value + ", begin_date="
				+ begin_date + ", end_date=" + end_date + "]";
	}
	
	
}
