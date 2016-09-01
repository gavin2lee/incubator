package com.lachesis.mnis.core.labtest.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class LabTestRecord  implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String masterId; 
    private String subject; 
    private String specimen; 
    /**
     * 申请者
     */
    private String orderDoctorName; 
    private Date requestDate; 
    /**
     * 报告者
     */
    private String reporterName; 
    private Date reportTime; 
    /**
     * 检验者
     */
    private String testName; 
    private Date testDate;
    /**
     * 审核者
     */
    private String checkName;
    private Date checkDate;
    private List<LabTestItem> itemList; 
    private String status; 
    private String patientName; 
    private int recordFlag; 
    private String bedCode;
    private String hospNo;
    private int priFlag;
	  
	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSpecimen() {
		return specimen;
	}

	public void setSpecimen(String specimen) {
		this.specimen = specimen;
	}

	public String getOrderDoctorName() {
		return orderDoctorName;
	}

	public void setOrderDoctorName(String orderDoctorName) {
		this.orderDoctorName = orderDoctorName;
	}

	

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	public Date getTestDate() {
		return testDate;
	}

	public void setTestDate(Date testDate) {
		this.testDate = testDate;
	}

	public List<LabTestItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<LabTestItem> itemList) {
		this.itemList = itemList;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public int getRecordFlag() {
		return recordFlag;
	}

	public void setRecordFlag(int recordFlag) {
		this.recordFlag = recordFlag;
	}

	public String getBedCode() {
		return bedCode;
	}

	public void setBedCode(String bedCode) {
		this.bedCode = bedCode;
	}

	public String getHospNo() {
		return hospNo;
	}

	public void setHospNo(String hospNo) {
		this.hospNo = hospNo;
	}

	public int getPriFlag() {
		return priFlag;
	}

	public void setPriFlag(int priFlag) {
		this.priFlag = priFlag;
	}

	public static class LabTestItem  implements Serializable{
		  /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String getDetailRecordId() {
			return detailRecordId;
		}
		public void setDetailRecordId(String detailRecordId) {
			this.detailRecordId = detailRecordId;
		}
		public String getItemName() {
			return itemName;
		}
		public void setItemName(String itemName) {
			this.itemName = itemName;
		}
		public String getItemCode() {
			return itemCode;
		}
		public void setItemCode(String itemCode) {
			this.itemCode = itemCode;
		}
		public String getResult() {
			return result;
		}
		public void setResult(String result) {
			this.result = result;
		}
		public String getResultUnit() {
			return resultUnit;
		}
		public void setResultUnit(String resultUnit) {
			this.resultUnit = resultUnit;
		}
		public String getNormalFlag() {
			return normalFlag;
		}
		public void setNormalFlag(String normalFlag) {
			this.normalFlag = normalFlag;
		}
		public String getRanges() {
			return ranges;
		}
		public void setRanges(String ranges) {
			this.ranges = ranges;
		}
		public String getMasterId() {
			return masterId;
		}
		public void setMasterId(String masterId) {
			this.masterId = masterId;
		}
		private String detailRecordId; 
		private String itemName; 
		private String itemCode; 
		private String result; 
		private String resultUnit; 
		private String normalFlag; 
		private String ranges; 
		private String masterId; 
	  }
}
