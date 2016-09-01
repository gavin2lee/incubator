package com.lachesis.mnis.core;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendStatistic;


/**
 * @author xin.chen
 *
 */
public interface LabTestService {
	/**
	 * 获取检验报告
	 * @param date          报告日期
	 * @param patientIds     病人id,病人id，多个病人以,号分隔
	 * @param status 		报告状态
	 * @param deptCode		部门Code
	 * @return
	 */
	List<LabTestRecord> getLabTestRecord(String date,String patientIds, String status, String deptCode);
	
	/**
	 * 获取检验报告
	 * @param startDate     查询报告开始日期
	 * @param endDate		查询报告结束日期
	 * @param patientIds     病人id，多个病人以,号分隔
	 * @param status 		报告状态
	 * @param deptCode		部门Code
	 * @return
	 */
	List<LabTestRecord> getLabTestRecord(String startDate, String endDate, String patientIds, String status, String deptCode);
	
	/**
	 * 变更检验报告优先级
	 * @param id 检验报告Id
	 * @param priFlag 优先标志。1表示优先，0表示不优先
	 * @return
	 */
	int updateLabTestRecordPriFlag(String id, Integer priFlag);
	
	void formatLabTestRecordTime(List<LabTestRecord> labTestRecords);

	/**
	 * 统计带送检患者数
	 * 
	 * @param deptCode
	 * @return
	 */
	List<LabTestSendStatistic> getLabTestSendStatistic(String deptCode,
			String startDate, String endDate, boolean isAll) throws Exception;

	/**
	 * 执行送检
	 * 
	 * @param barcode
	 * @return
	 */
	List<LabTestSendRecord> execLabTestSendByBarcode(String barcode,
			UserInformation user)  throws Exception;

	/**
	 * 获取所有送检
	 * 
	 * @param deptCode
	 * @return
	 */
	List<LabTestSendRecord> getLabTestSendRecords(String deptCode,
			String patId, String startDate, String endDate, boolean isAll) throws Exception;
	
	List<LabTestRecord> getLabTestRecordById(String barCode);
	
	/**
	 * 获取推送的信息
	 * @param deptCode
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<LabTestRecord> getPublishLabTests(String deptCode,Date startTime,Date endTime);
}
