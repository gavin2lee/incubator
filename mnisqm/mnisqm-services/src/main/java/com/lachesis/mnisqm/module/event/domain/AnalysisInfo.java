package com.lachesis.mnisqm.module.event.domain;

import java.util.List;

public class AnalysisInfo {

	private String title;
	
	List<AnalysisMeasures> analysisMeasuresList;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<AnalysisMeasures> getAnalysisMeasuresList() {
		return analysisMeasuresList;
	}

	public void setAnalysisMeasuresList(List<AnalysisMeasures> analysisMeasuresList) {
		this.analysisMeasuresList = analysisMeasuresList;
	}
	
	
	
}
