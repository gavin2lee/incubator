package com.lachesis.mnis.core.mybatis.mapper;

import java.util.Date;
import java.util.List;

import com.lachesis.mnis.core.documents.entity.DocumentInfo;

public interface DocumentsMapper {
	
	/**
	 * 获取输液单
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DocumentInfo> selectLiquorDocument(String patientId,Date startDate,Date endDate);
	/**
	 * 获取口服药单
	 * @param patientId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DocumentInfo> selectPersralDocument(List<String> orderSubNos,String patientId,Date startDate,Date endDate);
	
	List<DocumentInfo> selectDrugBagDocument(String patientId,Date startDate,Date endDate);
	
}
