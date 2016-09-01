package com.lachesis.mnis.core;

import java.util.List;

import com.lachesis.mnis.core.bloodSugarMonitor.entity.PatBloodSugarMonitor;

public interface BloodSugarMonitorService {
	public List<PatBloodSugarMonitor> queryBloodSugarMonitor(String deptCode, String patId,
			String recordDate, boolean provisions);
	public void saveBloodSugarMonitor(List<PatBloodSugarMonitor> datas);
}
