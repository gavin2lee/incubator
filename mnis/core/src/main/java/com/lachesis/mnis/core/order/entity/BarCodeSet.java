package com.lachesis.mnis.core.order.entity;

/**
 * 条码设置  沈阳胸科医院，静配条码长度为13位（条码生成时加了一位验证码），数据库实际保存为12位
 * 可否根据条码类型查找相应的医嘱
 * 医院如果规范不同系统条码长度
 * @author qingzhi.liu
 *
 */
public class BarCodeSet {
	//barType=INFUSION,issub=1,len=13,sublen=12,sbustart=0,subend=12,barlens=13,barlene=14,index=;
	private String barType;   //条码类型 检验：LAB 输液：INFUSION 口服：ORAL
	private int issub;   //1：截取  0：不截取
	private int len;         //条码实际长度
	private int sublen;      //数据库保存长度
	private int substart;    //从条码截取开始长度
	
	private int subend;   //从条码截取最后长度
	private int barlens;  //该类型条码最少长度
	private int barlene;  //该类型条码最大长度
	private String index;  //条码中包含该字段
	
	
	public BarCodeSet() {
		super();
	}
	public String getBarType() {
		return barType;
	}
	public void setBarType(String barType) {
		this.barType = barType;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public int getSublen() {
		return sublen;
	}
	public void setSublen(int sublen) {
		this.sublen = sublen;
	}
	public int getSubstart() {
		return substart;
	}
	public void setSubstart(int substart) {
		this.substart = substart;
	}
	public int getSubend() {
		return subend;
	}
	public void setSubend(int subend) {
		this.subend = subend;
	}
	public int getBarlens() {
		return barlens;
	}
	public void setBarlens(int barlens) {
		this.barlens = barlens;
	}
	public int getBarlene() {
		return barlene;
	}
	public void setBarlene(int barlene) {
		this.barlene = barlene;
	}
	public int getIssub() {
		return issub;
	}
	public void setIssub(int issub) {
		this.issub = issub;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	@Override
	public String toString() {
		return "BarCodeSet [barType=" + barType + ", issub=" + issub + ", len="
				+ len + ", sublen=" + sublen + ", substart=" + substart
				+ ", subend=" + subend + ", barlens=" + barlens + ", barlene="
				+ barlene + ", index=" + index + "]";
	}
	
}
