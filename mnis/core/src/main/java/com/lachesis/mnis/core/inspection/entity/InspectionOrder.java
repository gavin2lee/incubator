package com.lachesis.mnis.core.inspection.entity;

import java.util.Date;

public class InspectionOrder {
	private String inspecOrderId;  //预约检查流水号
	private String orderId;		//医嘱ID
	private String inHospNo;
	private String patientId;
	private String deptNo;
	private String patientName;
	private String sex;
	private String bedNo;
	private String diseaseinfo;  //病史
	private String itemNote;   //检查部位
	private boolean isCritical;  //是否危重
	private boolean isEmc;    //是否急诊
	private Date inspectionOrderDate;   //预约开始时间
	private String doctorId;   //开嘱医生 
	private String orderDate;    //医嘱开立时间
	private String orderType;	//医嘱类型
	private String inspec_code;  //检查项目代码
	private String inspec_name;   //检查项目名称
	private float qty;      //数量
	private String freq;		//频次
	private String unit;		//单位
	private String unitPrice;   //单价
	private float cose;		//金额
	private String operatorId;  //操作人
	private String execDeptNo;	//执行科室
	private String operateDate;  //执行时间
	private String dcId;		//作废人
	private boolean isExec;    //是否已执行
	private String execManId;	//执行人
	private String execDate;	//执行时间
	private boolean isCheck;    //是否已确定
	private String checkManId;	//确定人
	private String checkDate;	//确定时间
	private Date endDate;  //预约结束时间
	private String state;   //状态
	public String getInspecOrderId() {
		return inspecOrderId;
	}
	public void setInspecOrderId(String inspecOrderId) {
		this.inspecOrderId = inspecOrderId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getInHospNo() {
		return inHospNo;
	}
	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getDiseaseinfo() {
		return diseaseinfo;
	}
	public void setDiseaseinfo(String diseaseinfo) {
		this.diseaseinfo = diseaseinfo;
	}
	public String getItemNote() {
		return itemNote;
	}
	public void setItemNote(String itemNote) {
		this.itemNote = itemNote;
	}
	public boolean isCritical() {
		return isCritical;
	}
	public void setCritical(boolean isCritical) {
		this.isCritical = isCritical;
	}
	public boolean isEmc() {
		return isEmc;
	}
	public void setEmc(boolean isEmc) {
		this.isEmc = isEmc;
	}
	public Date getInspectionOrderDate() {
		return inspectionOrderDate;
	}
	public void setInspectionOrderDate(Date inspectionOrderDate) {
		this.inspectionOrderDate = inspectionOrderDate;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getInspec_code() {
		return inspec_code;
	}
	public void setInspec_code(String inspec_code) {
		this.inspec_code = inspec_code;
	}
	public String getInspec_name() {
		return inspec_name;
	}
	public void setInspec_name(String inspec_name) {
		this.inspec_name = inspec_name;
	}
	public float getQty() {
		return qty;
	}
	public void setQty(float qty) {
		this.qty = qty;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public float getCose() {
		return cose;
	}
	public void setCose(float cose) {
		this.cose = cose;
	}
	public String getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}
	public String getExecDeptNo() {
		return execDeptNo;
	}
	public void setExecDeptNo(String execDeptNo) {
		this.execDeptNo = execDeptNo;
	}
	public String getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}
	public String getDcId() {
		return dcId;
	}
	public void setDcId(String dcId) {
		this.dcId = dcId;
	}
	public boolean isExec() {
		return isExec;
	}
	public void setExec(boolean isExec) {
		this.isExec = isExec;
	}
	public String getExecManId() {
		return execManId;
	}
	public void setExecManId(String execManId) {
		this.execManId = execManId;
	}
	public String getExecDate() {
		return execDate;
	}
	public void setExecDate(String execDate) {
		this.execDate = execDate;
	}
	public boolean isCheck() {
		return isCheck;
	}
	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}
	public String getCheckManId() {
		return checkManId;
	}
	public void setCheckManId(String checkManId) {
		this.checkManId = checkManId;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
