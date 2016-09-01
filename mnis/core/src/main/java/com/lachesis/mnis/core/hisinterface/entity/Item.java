package com.lachesis.mnis.core.hisinterface.entity;

public class Item {
	public String patientno ="0810137";
	public String inputoperid="709";
	public String inputdeptid="2051";
	
	public String measuredate="2015-12-01";
	public String timepoint="1";
	public String operationid="1";
	public String operationtext="入院于九时十二分";
	
	public String operationtime="2015-12-01 09:12:00";
	public String t="40";
	public String p="70";
	public String r="50";
	
	public String hr="50";
	public String ispc="是";
	public String pt="37.8";
	public String isbm="否";
	
	public String ispm="否";
	public String status="1";
	public String drink="1200";
	public String transfuse="300";
	
	public String otherin="";
	public String allin ="1500";
	public String stool="1 3/e";
	
	public String pee="1000";
	public String drainage="";
	public String sputum="";
	public String otherout="";
	
	public String allout="1400";
	public String bp1="120/74";
	public String bp2="121/82";
	public String height="173";
	public String weight="73.5";
	public Item() {
		super();
	}
	public String getPatientno() {
		return patientno;
	}
	public void setPatientno(String patientno) {
		this.patientno = patientno;
	}
	public String getInputoperid() {
		return inputoperid;
	}
	public void setInputoperid(String inputoperid) {
		this.inputoperid = inputoperid;
	}
	public String getInputdeptid() {
		return inputdeptid;
	}
	public void setInputdeptid(String inputdeptid) {
		this.inputdeptid = inputdeptid;
	}
	public String getMeasuredate() {
		return measuredate;
	}
	public void setMeasuredate(String measuredate) {
		this.measuredate = measuredate;
	}
	public String getTimepoint() {
		return timepoint;
	}
	public void setTimepoint(String timepoint) {
		this.timepoint = timepoint;
	}
	public String getOperationid() {
		return operationid;
	}
	public void setOperationid(String operationid) {
		this.operationid = operationid;
	}
	public String getOperationtext() {
		return operationtext;
	}
	public void setOperationtext(String operationtext) {
		this.operationtext = operationtext;
	}
	public String getOperationtime() {
		return operationtime;
	}
	public void setOperationtime(String operationtime) {
		this.operationtime = operationtime;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getP() {
		return p;
	}
	public void setP(String p) {
		this.p = p;
	}
	public String getR() {
		return r;
	}
	public void setR(String r) {
		this.r = r;
	}
	public String getHr() {
		return hr;
	}
	public void setHr(String hr) {
		this.hr = hr;
	}
	public String getIspc() {
		return ispc;
	}
	public void setIspc(String ispc) {
		this.ispc = ispc;
	}
	public String getPt() {
		return pt;
	}
	public void setPt(String pt) {
		this.pt = pt;
	}
	public String getIsbm() {
		return isbm;
	}
	public void setIsbm(String isbm) {
		this.isbm = isbm;
	}
	public String getIspm() {
		return ispm;
	}
	public void setIspm(String ispm) {
		this.ispm = ispm;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDrink() {
		return drink;
	}
	public void setDrink(String drink) {
		this.drink = drink;
	}
	public String getTransfuse() {
		return transfuse;
	}
	public void setTransfuse(String transfuse) {
		this.transfuse = transfuse;
	}
	public String getOtherin() {
		return otherin;
	}
	public void setOtherin(String otherin) {
		this.otherin = otherin;
	}
	public String getAllin() {
		return allin;
	}
	public void setAllin(String allin) {
		this.allin = allin;
	}
	public String getStool() {
		return stool;
	}
	public void setStool(String stool) {
		this.stool = stool;
	}
	public String getPee() {
		return pee;
	}
	public void setPee(String pee) {
		this.pee = pee;
	}
	public String getDrainage() {
		return drainage;
	}
	public void setDrainage(String drainage) {
		this.drainage = drainage;
	}
	public String getSputum() {
		return sputum;
	}
	public void setSputum(String sputum) {
		this.sputum = sputum;
	}
	public String getOtherout() {
		return otherout;
	}
	public void setOtherout(String otherout) {
		this.otherout = otherout;
	}
	public String getAllout() {
		return allout;
	}
	public void setAllout(String allout) {
		this.allout = allout;
	}
	public String getBp1() {
		return bp1;
	}
	public void setBp1(String bp1) {
		this.bp1 = bp1;
	}
	public String getBp2() {
		return bp2;
	}
	public void setBp2(String bp2) {
		this.bp2 = bp2;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	@Override
	public String toString() {
		return "Item [patientno=" + patientno + ", inputoperid=" + inputoperid
				+ ", inputdeptid=" + inputdeptid + ", measuredate="
				+ measuredate + ", timepoint=" + timepoint + ", operationid="
				+ operationid + ", operationtext=" + operationtext
				+ ", operationtime=" + operationtime + ", t=" + t + ", p=" + p
				+ ", r=" + r + ", hr=" + hr + ", ispc=" + ispc + ", pt=" + pt
				+ ", isbm=" + isbm + ", ispm=" + ispm + ", status=" + status
				+ ", drink=" + drink + ", transfuse=" + transfuse
				+ ", otherin=" + otherin + ", allin=" + allin + ", stool="
				+ stool + ", pee=" + pee + ", drainage=" + drainage
				+ ", sputum=" + sputum + ", otherout=" + otherout + ", allout="
				+ allout + ", bp1=" + bp1 + ", bp2=" + bp2 + ", height="
				+ height + ", weight=" + weight + "]";
	}
	
	
	
}
