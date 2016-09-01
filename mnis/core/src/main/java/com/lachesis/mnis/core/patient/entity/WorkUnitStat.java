package com.lachesis.mnis.core.patient.entity;

public class WorkUnitStat {

	/**
	 * 住院病人数
	 */
	private int inpatientCount;
	/**
	 * 入院病人
	 */
	private int inDebtCount;
	/**
	 * 空床位
	 */
	private int emptyBedCount;
	/**
	 * 特级护理
	 */
	private int tendLevelSuperCount;
	/**
	 * 一级护理
	 */
	private int tendLevelOneCount;
	/**
	 * 二级护理
	 */
	private int tendLevelTwoCount;
	/**
	 * 三级护理
	 */
	private int tendLevelThreeCount;
	/**
	 * 危重病人
	 */
	private int criticalPatientCount;
	/**
	 * 严重病人
	 */
	private int seriousPatientCount;
	/**
	 * 出院病人数
	 */
	private int formerPatientCount;
	/**
	 * 新病人数
	 */
	private int newPatientCount;
	/**
	 * 入科病人数
	 */
	private int transInPatientCount;
	/**
	 * 转科病人数
	 */
	private int transOutPatientCount;
	/**
	 * 死亡病人
	 */
	private int deadPatientCount;
	/**
	 * 手术病人数
	 */
	private int inSurgeryPatientCount;
	/**
	 * 分娩病人数
	 */
	private int inDeliveryPatientCount;
	/**
	 * 欠费病人数
	 */
	private int dischargeCount;
	/**
	 * 发热病人数
	 */
	private int highTempCount;
	private String criticalPatient;
	private String seriousPatient;
	private String newPatient;
	private String discharge;

	public int getInpatientCount() {
		return inpatientCount;
	}

	public void setInpatientCount(int inpatientCount) {
		this.inpatientCount = inpatientCount;
	}

	public int getInDebtCount() {
		return inDebtCount;
	}

	public void setInDebtCount(int inDebtCount) {
		this.inDebtCount = inDebtCount;
	}

	public int getEmptyBedCount() {
		return emptyBedCount;
	}

	public void setEmptyBedCount(int emptyBedCount) {
		this.emptyBedCount = emptyBedCount;
	}

	public int getTendLevelSuperCount() {
		return tendLevelSuperCount;
	}

	public void setTendLevelSuperCount(int tendLevelSuperCount) {
		this.tendLevelSuperCount = tendLevelSuperCount;
	}

	public int getTendLevelOneCount() {
		return tendLevelOneCount;
	}

	public void setTendLevelOneCount(int tendLevelOneCount) {
		this.tendLevelOneCount = tendLevelOneCount;
	}

	public int getTendLevelTwoCount() {
		return tendLevelTwoCount;
	}

	public void setTendLevelTwoCount(int tendLevelTwoCount) {
		this.tendLevelTwoCount = tendLevelTwoCount;
	}

	public int getTendLevelThreeCount() {
		return tendLevelThreeCount;
	}

	public void setTendLevelThreeCount(int tendLevelThreeCount) {
		this.tendLevelThreeCount = tendLevelThreeCount;
	}

	public int getCriticalPatientCount() {
		return criticalPatientCount;
	}

	public void setCriticalPatientCount(int criticalPatientCount) {
		this.criticalPatientCount = criticalPatientCount;
	}

	public int getSeriousPatientCount() {
		return seriousPatientCount;
	}

	public void setSeriousPatientCount(int seriousPatientCount) {
		this.seriousPatientCount = seriousPatientCount;
	}

	public int getFormerPatientCount() {
		return formerPatientCount;
	}

	public void setFormerPatientCount(int formerPatientCount) {
		this.formerPatientCount = formerPatientCount;
	}

	public int getNewPatientCount() {
		return newPatientCount;
	}

	public void setNewPatientCount(int newPatientCount) {
		this.newPatientCount = newPatientCount;
	}

	public int getTransInPatientCount() {
		return transInPatientCount;
	}

	public void setTransInPatientCount(int transInPatientCount) {
		this.transInPatientCount = transInPatientCount;
	}

	public int getTransOutPatientCount() {
		return transOutPatientCount;
	}

	public void setTransOutPatientCount(int transOutPatientCount) {
		this.transOutPatientCount = transOutPatientCount;
	}

	public int getDeadPatientCount() {
		return deadPatientCount;
	}

	public void setDeadPatientCount(int deadPatientCount) {
		this.deadPatientCount = deadPatientCount;
	}

	public int getInSurgeryPatientCount() {
		return inSurgeryPatientCount;
	}

	public void setInSurgeryPatientCount(int inSurgeryPatientCount) {
		this.inSurgeryPatientCount = inSurgeryPatientCount;
	}

	public int getInDeliveryPatientCount() {
		return inDeliveryPatientCount;
	}

	public void setInDeliveryPatientCount(int inDeliveryPatientCount) {
		this.inDeliveryPatientCount = inDeliveryPatientCount;
	}

	public int getDischargeCount() {
		return dischargeCount;
	}

	public void setDischargeCount(int dischargeCount) {
		this.dischargeCount = dischargeCount;
	}

	public int getHighTempCount() {
		return highTempCount;
	}

	public void setHighTempCount(int highTempCount) {
		this.highTempCount = highTempCount;
	}

	public String getCriticalPatient() {
		return criticalPatient;
	}

	public void setCriticalPatient(String criticalPatient) {
		this.criticalPatient = criticalPatient;
	}

	public String getSeriousPatient() {
		return seriousPatient;
	}

	public void setSeriousPatient(String seriousPatient) {
		this.seriousPatient = seriousPatient;
	}

	public String getNewPatient() {
		return newPatient;
	}

	public void setNewPatient(String newPatient) {
		this.newPatient = newPatient;
	}

	public String getDischarge() {
		return discharge;
	}

	public void setDischarge(String discharge) {
		this.discharge = discharge;
	}

}
