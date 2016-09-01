package com.lachesis.mnis.core.mybatis.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendRecordDetail;
import com.lachesis.mnis.core.labtest.entity.LabTestSendStatistic;

public interface LabTestServiceMapper {
	List<LabTestRecord> getLabTestRecord(Map<String, Object> paramMap);

	int updateLabTestRecordPriFlag(Map<String, Object> paramMap);

	List<LabTestRecord> getLabTestRecordById(String barCode);
	
	/**
	 * 统计带送检患者数
	 * @param deptCode
	 * @return
	 */
	List<LabTestSendStatistic> getLabTestSendStatistic(String deptCode,String startDate,String endDate,String allType);
	/**
	 * 获取所有送检
	 * @param deptCode
	 * @return
	 */
	List<LabTestSendRecord> getLabTestSendRecords(String deptCode,String patId,String startDate,String endDate,String allType);
	/**
	 * 根据条码获取送检个数
	 * @param barcode
	 * @return
	 */
	Integer getLabTestSendCountByBarcode(String barcode);
	/**
	 * 根据条码获取患者
	 * @param barcode
	 * @return
	 */
	String getLabTestSendPatIdByBarcode(String barcode);
	
	int insertLabTestSend(LabTestSendRecordDetail labTestSendRecordDetail);
	/**
	 * 获取推送的信息
	 * @param params
	 * @return
	 */
	List<LabTestRecord> getPublishLabTests(HashMap<String, Object> params);
}
