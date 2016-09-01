package com.lachesis.mnisqm.vo.smart;

public class MnisVoList {
	private String lc;//流程
	private String lcfg;//流程覆盖
	private String orderNum;//医嘱总数
	private String execNum;//执行医嘱
	private String docName;//文书名称
	private String cNum;//创建数量
	private String zcNum;//转抄数量
	public String getLc() {
		return lc;
	}
	public void setLc(String lc) {
		this.lc = lc;
	}
	public String getLcfg() {
		return lcfg;
	}
	public void setLcfg(String lcfg) {
		this.lcfg = lcfg;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getExecNum() {
		return execNum;
	}
	public void setExecNum(String execNum) {
		this.execNum = execNum;
	}
	public String getDocName() {
		return docName;
	}
	public void setDocName(String docName) {
		this.docName = docName;
	}
	public String getcNum() {
		return cNum;
	}
	public void setcNum(String cNum) {
		this.cNum = cNum;
	}
	public String getZcNum() {
		return zcNum;
	}
	public void setZcNum(String zcNum) {
		this.zcNum = zcNum;
	}
}
