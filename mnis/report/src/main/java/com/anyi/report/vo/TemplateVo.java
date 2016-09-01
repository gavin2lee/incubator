package com.anyi.report.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 模板视图显示vo
 * @author qingzhi.liu 2015.06.18
 *
 */
public class TemplateVo {
	private String template_id;  //模板编号
	private String template_name;  //模板名称
	private String pat_id;  //患者id
	private String record_id;  //保存表单数据记录id  为空：该模板未编辑保存过
	private String report_type;//模板分类
	private String type;  //NDA、PC
	private List<TimeTreeVo> data=new ArrayList<TimeTreeVo>();
	public TemplateVo() {
		super();
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getTemplate_name() {
		return template_name;
	}
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
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
	@Override
	public String toString() {
		return "TemplateVo [template_id=" + template_id + ", template_name="
				+ template_name + ", pat_id=" + pat_id + ", record_id="
				+ record_id + ", data=" + data + "]";
	}
	public String getReport_type() {
		return report_type;
	}
	public void setReport_type(String report_type) {
		this.report_type = report_type;
	}
	
}
