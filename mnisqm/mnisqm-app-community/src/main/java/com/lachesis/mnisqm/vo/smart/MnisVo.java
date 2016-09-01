package com.lachesis.mnisqm.vo.smart;

import java.util.List;

public class MnisVo {
	private String model;//覆盖模块
	private String orderNum;//医嘱执行总数
	private String errOrderNum;//执行错误数
	private String docNum;//文书总数
	private String hj;//护记条数
	private String tys;//同意书条数
	private String pgd;//评估单条数
	private List<MnisVoList> lst;
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getErrOrderNum() {
		return errOrderNum;
	}
	public void setErrOrderNum(String errOrderNum) {
		this.errOrderNum = errOrderNum;
	}
	public String getDocNum() {
		return docNum;
	}
	public void setDocNum(String docNum) {
		this.docNum = docNum;
	}
	public String getHj() {
		return hj;
	}
	public void setHj(String hj) {
		this.hj = hj;
	}
	public String getTys() {
		return tys;
	}
	public void setTys(String tys) {
		this.tys = tys;
	}
	public String getPgd() {
		return pgd;
	}
	public void setPgd(String pgd) {
		this.pgd = pgd;
	}
	public List<MnisVoList> getLst() {
		return lst;
	}
	public void setLst(List<MnisVoList> lst) {
		this.lst = lst;
	}
}
