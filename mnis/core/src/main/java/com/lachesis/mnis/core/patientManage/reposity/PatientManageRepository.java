package com.lachesis.mnis.core.patientManage.reposity;

import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.patientManage.entity.PatCureLocalInfo;
import com.lachesis.mnis.core.patientManage.entity.PatLeaveGoout;
import com.lachesis.mnis.core.patientManage.entity.PatNdaManageInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationStatus;
import com.lachesis.mnis.core.patientManage.entity.PatOrderConfiguration;

public interface PatientManageRepository {

	
	int savePatOperationInfo(PatOperationInfo patOperationInfo);
	
	int savePatOperationStatus(PatOperationStatus patOperationStatus);
	
	int savePatOrderConfiguration(PatOrderConfiguration patOrderConfiguration);
	
	List<PatOrderConfiguration> queryAllPatOrderConfiguration();
	
	PatOperationInfo getPatOperationInfoByPatId(Map<String, Object> conditionMap);
	
	PatOperationStatus getRecentlyPatOperationByPatId(String patId);
	
	List<PatOperationInfo> queryAllPatOperationInfo();
	
	List<PatOperationInfo> queryByStatusOrDate(Map<String, Object> conditionMap);
	
	int savePatCureLocalInfo(PatCureLocalInfo patCureLocalInfo);
	
	int savePatLeaveGoout(PatLeaveGoout patLeaveGoout);
	
	List<PatCureLocalInfo> queryAllOutPatientByDateAndStatus(Map<String, String> conditionMap);
	
	PatNdaManageInfo queryNdaManageInfo(Map<String, String> conditionMap);
	
}
