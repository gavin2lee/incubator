package com.lachesis.mnis.core;

import java.util.List;

import com.lachesis.mnis.core.documents.entity.DocumentInfo;


public interface DocumentsService {
	
	/**
	 * 查询指定日期内 指定患者 已执行的配液医嘱 执行单
	 * @param patientId
	 * @param time
	 * @return
	 */
	public List<DocumentInfo> selectLiquorDocument(String patientId,String time);
	
	/**
	 * 查询指定日期内 指定患者 已执行的口服液医嘱
	 * @param patientId
	 * @param time
	 * @return
	 */
	public List<DocumentInfo> selectPersralDocument(String patientId,String time);
	
}
