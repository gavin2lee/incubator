package com.lachesis.mnis.core.doctree.entity;

public class ComNavTree {
	/*ID, SHOWNAME, FIELDTYPE, ORD, PARENT_ID, TYPE, STATUS*/
	
	private int id;    //主键，自增长
	private String showname;   //显示名称
	private String fieldtype;   //类型名称
	private int ord;   //序号
	private int parent_id;   //父节点  根节点为0
	private int type;   //1：文书节点
	private String url;  //前端请求路径
	
	
	public ComNavTree() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getShowname() {
		return showname;
	}


	public void setShowname(String showname) {
		this.showname = showname;
	}


	public String getFieldtype() {
		return fieldtype;
	}


	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}


	public int getOrd() {
		return ord;
	}


	public void setOrd(int ord) {
		this.ord = ord;
	}


	public int getParent_id() {
		return parent_id;
	}


	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
