package com.anyi.report.entity;

import java.util.List;

public class DocRecordDataStatistic {
	private String staticDate;
	private List<DocRecordStatistic> docRecordStatistics;
	public String getStaticDate() {
		return staticDate;
	}
	public void setStaticDate(String staticDate) {
		this.staticDate = staticDate;
	}
	public List<DocRecordStatistic> getDocRecordStatistics() {
		return docRecordStatistics;
	}
	public void setDocRecordStatistics(List<DocRecordStatistic> docRecordStatistics) {
		this.docRecordStatistics = docRecordStatistics;
	}
}
