package com.lachesis.mnis.core.old.doc.service;

import java.util.List;

import com.lachesis.mnis.core.old.doc.DepartmentPatientSummary;

public interface DepartmentPatientSummaryService<T>{
	
	/**
	 * 根据时间查询部门人数记录
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<DepartmentPatientSummary> getDepartmentPatientSummaryListByTime(String startTime, String endTime, String deptCode);

	public T insert(T t);
}
