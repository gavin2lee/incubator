package com.lachesis.mnis.core.inspection.entity;

import java.util.Date;
import java.util.List;

public class InspectionRecord {
	  private String masterId; // required
	  /**
	   * 报告者
	   */
	  private String reporter; // required
	  /**
	   * 报告时间
	   */
	  private Date reportTime; // required
	  /**
	   * 检查时间
	   */
	  private Date inspectionTime; // required
	  /**
	   * 检查项
	   */
	  private String subject; // required
	  private String patId; // required
	  private String patName; // required
	  private String bedNo; // required
	  private String inHospNo; // required
	 /**
	  * 申请者
	  */
	  private String applicant; // required
	  /**
	   * 申请时间
	   */
	  private Date applicantTime;
	  /**
	   * 审核者
	   */
	  private String checkName;
	  private Date checkDate;
	  /**
	   * 申请状态
	   */
	  private String status;
	  private int priFlag;
	  private List<InspectionRecordDetail> detailList; // required
	  
	  public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getInspectionTime() {
		return inspectionTime;
	}

	public void setInspectionTime(Date inspectionTime) {
		this.inspectionTime = inspectionTime;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPatId() {
		return patId;
	}

	public void setPatId(String patId) {
		this.patId = patId;
	}

	public String getPatName() {
		return patName;
	}

	public void setPatName(String patName) {
		this.patName = patName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getInHospNo() {
		return inHospNo;
	}

	public void setInHospNo(String inHospNo) {
		this.inHospNo = inHospNo;
	}

	public Date getApplicantTime() {
		return applicantTime;
	}

	public void setApplicantTime(Date applicantTime) {
		this.applicantTime = applicantTime;
	}

	public String getReporter() {
		return reporter;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getPriFlag() {
		return priFlag;
	}

	public void setPriFlag(int priFlag) {
		this.priFlag = priFlag;
	}

	public List<InspectionRecordDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<InspectionRecordDetail> detailList) {
		this.detailList = detailList;
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


	public static class InspectionRecordDetail {
		  public String getDetailRecordId() {
			return detailRecordId;
		}
		public void setDetailRecordId(String detailRecordId) {
			this.detailRecordId = detailRecordId;
		}
		public String getMasRecordId() {
			return masRecordId;
		}
		public void setMasRecordId(String masRecordId) {
			this.masRecordId = masRecordId;
		}
		public String getBodyParts() {
			return bodyParts;
		}
		public void setBodyParts(String bodyParts) {
			this.bodyParts = bodyParts;
		}
		public String getInspectionResult() {
			return inspectionResult;
		}
		public void setInspectionResult(String inspectionResult) {
			this.inspectionResult = inspectionResult;
		}
		public String getInspSuggestion() {
			return inspSuggestion;
		}
		public void setInspSuggestion(String inspSuggestion) {
			this.inspSuggestion = inspSuggestion;
		}
		private String detailRecordId; // required
		  private String masRecordId; // required
		  private String bodyParts; // required
		  private String inspectionResult; // required
		  private String inspSuggestion; // required
	  }
}
