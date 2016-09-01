package com.lachesis.mnis.core.documents.repository;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.documents.entity.DocumentInfo;


public interface DocumentsRepository {

	List<DocumentInfo> selectLiquorDocument(String patientId,Date startDate,Date endDate);
	
	/**
	 * 查询指定日期内 指定患者 已执行的口服液医嘱
	 * @param patientId
	 * @param time
	 * @return
	 */
	List<DocumentInfo> selectPersralDocument(List<String> orderSubNos,String patientId,Date startDate,Date endDate);
	
	List<DocumentInfo> selectDrugBagDocument(String patientId,Date startDate,Date endDate);
}
