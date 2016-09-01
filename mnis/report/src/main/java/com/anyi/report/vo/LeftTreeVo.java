package com.anyi.report.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 左边树结构视图显示类
 * @author qingzhi.liu 2015.06.18
 *
 */
public class LeftTreeVo {
	private String type_id;   //树结构第一层编码
	private String type_name;  //树结构第一层显示名称
	private String pat_id;  //患者id
	private String type;   //NDA、PC
	private String show_type;//展示方式（fixed固定页数  list页数不固定）
	List<TemplateVo> data = new ArrayList<TemplateVo>();  //节点下的模板
	
	public LeftTreeVo() {
		super();
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}


	public List<TemplateVo> getData() {
		return data;
	}

	public void setData(List<TemplateVo> data) {
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
		return "LeftTreeVo [type_id=" + type_id + ", type_name=" + type_name
				+ ", pat_id=" + pat_id + ", data=" + data + "]";
	}

	public String getShow_type() {
		return show_type;
	}

	public void setShow_type(String show_type) {
		this.show_type = show_type;
	}

	
}
