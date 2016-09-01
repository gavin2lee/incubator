/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web.vo;

import java.io.Serializable;

import com.lachesis.mnis.core.patient.entity.Patient;

/**
 * The class BedPatientInfo.
 * 
 *
 * @author: yanhui.wang
 * @since: 2014-6-17	
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
public class PatientInfoResult implements Serializable {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -5435768091302964244L;
	private Patient inpatientInfo;
	private String allergyStr;
	private String admitDiags;
	
	public Patient getInpatientInfo() {
		return inpatientInfo;
	}
	
	public void setInpatientInfo(Patient inpatientInfo) {
		this.inpatientInfo = inpatientInfo;
	}
	
	public String getAllergyStr() {
		return allergyStr;
	}
	
	public void setAllergyStr(String allergyStr) {
		this.allergyStr = allergyStr;
	}
	
	public String getAdmitDiags() {
		return admitDiags;
	}
	
	public void setAdmitDiags(String admitDiags) {
		this.admitDiags = admitDiags;
	}
	
	

}
