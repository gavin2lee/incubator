/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web.vo;

import java.io.Serializable;
import java.util.List;

import com.lachesis.mnis.core.patient.entity.WorkUnitStat;

/**
 * The class BedPatientInfo.
 * 
 * 病人一览查询结果
 *
 * @author: yanhui.wang
 * @since: 2014-6-17	
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
public class BedPatientInfoResult implements Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 992038355045356558L;

	private List<BedPatientInfo> bedInfoList;
	private WorkUnitStat workUnitStatistics;

	public List<BedPatientInfo> getBedInfoList() {
		return bedInfoList;
	}

	public void setBedInfoList(List<BedPatientInfo> bedInfoList) {
		this.bedInfoList = bedInfoList;
	}

	public WorkUnitStat getWorkUnitStatistics() {
		return workUnitStatistics;
	}

	public void setWorkUnitStatistics(WorkUnitStat workUnitStatistics) {
		this.workUnitStatistics = workUnitStatistics;
	}

}
