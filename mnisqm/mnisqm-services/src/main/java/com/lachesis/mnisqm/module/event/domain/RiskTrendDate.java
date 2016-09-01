package com.lachesis.mnisqm.module.event.domain;

import java.util.List;

public class RiskTrendDate {

	String deptName;
	
	List<RiskTrend> riskTrendList;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<RiskTrend> getRiskTrendList() {
		return riskTrendList;
	}

	public void setRiskTrendList(List<RiskTrend> riskTrendList) {
		this.riskTrendList = riskTrendList;
	}
	
}
