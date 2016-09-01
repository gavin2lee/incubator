package com.lachesis.mnis.core;

import java.util.List;

import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.order.entity.HisOrderGroup;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.redismsg.entity.RedisMsgRecord;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;

public interface RedisMsgService {
	
	int batchSaveRedisMsgRecords(List<RedisMsgRecord> redisMsgRecords) throws Exception;
	
	List<CriticalValueRecord> getCriticalValueRecords(String deptCode) throws Exception;
	List<LabTestRecord> getPublishLabTests(String deptCode) throws Exception;
	List<InspectionRecord> getPublishInspections(String deptCode) throws Exception;
	List<WardPatrolInfo> getPublishWardPatrols(String deptCode) throws Exception;
	List<SkinTestInfoLx> getPublishSkinTests(String deptCode) throws Exception;
	List<HisOrderGroup> getPublishChangeOrders(String deptCode) throws Exception;
	
}
