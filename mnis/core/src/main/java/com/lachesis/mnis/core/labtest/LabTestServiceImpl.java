package com.lachesis.mnis.core.labtest;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.LabTestService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.labtest.entity.LabTestRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendRecord;
import com.lachesis.mnis.core.labtest.entity.LabTestSendRecordDetail;
import com.lachesis.mnis.core.labtest.entity.LabTestSendStatistic;
import com.lachesis.mnis.core.labtest.repository.LabTestRepository;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.redis.RedisUtil;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Service("labTestService")
public class LabTestServiceImpl implements LabTestService {
	final static Logger LOGGER = LoggerFactory
			.getLogger(LabTestServiceImpl.class);
	@Autowired
	private LabTestRepository labTestRepository;
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private RedisUtil redisUtil;

	@Override
	public List<LabTestRecord> getLabTestRecord(String date, String patientIds,
			String status, String deptCode) {
		if (StringUtils.isBlank(date)) {
			Date now = new Date();
			Date beginDate = DateUtils.addDays(now, -7);

			return getLabTestRecord(
					DateUtil.format(beginDate, DateFormat.YMD), 
					DateUtil.format(now, DateFormat.YMD), 
					patientIds, status, deptCode);
		} else {
			return getLabTestRecord(date, date, patientIds, status, deptCode);
		}

	}

	@Override
	public List<LabTestRecord> getLabTestRecord(String startDate,
			String endDate, String patientId, String status, String deptCode) {
		if (StringUtils.isEmpty(patientId)) {
			return new ArrayList<>();
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		/* paramMap.put("patientIds", patientIds.split(",")); */
		paramMap.put("patientId", patientId);
		if (StringUtils.isNotEmpty(startDate)) {
			paramMap.put("startTime", DateUtil.parse(startDate + DateUtil.START_TIMES));
		}
		if (StringUtils.isNotEmpty(endDate)) {
			paramMap.put("endTime", DateUtil.parse(endDate + DateUtil.END_TIMES));
		}
		
		
		Patient inpatientInfo = patientService.getPatientByPatId(patientId);
		if (inpatientInfo == null) {
			throw new RuntimeException("沒有此病人");
		}
		//检验可以根据住院号来关联查询，不需要按照住院唯一标识，三院检验没有保存住院唯一标识
		//关联患者表只查询床号、姓名 是否可以取消
		paramMap.put("in_hosp_no", inpatientInfo.getInHospNo());
		
		paramMap.put("status", status);
		paramMap.put("deptCode", deptCode);
		List<LabTestRecord> labTestRecords = labTestRepository
				.getLabTestRecord(paramMap);
		return labTestRecords;
	}

	@Override
	public void formatLabTestRecordTime(List<LabTestRecord> labTestRecords) {
		if (labTestRecords == null) {
			return;
		}
		/*for (LabTestRecord labTestRecord : labTestRecords) {
			if (labTestRecord != null) {
				labTestRecord.setReportTime(DateUtil.truncateDateString(labTestRecord.getReportTime(), DateFormat.YMDHM));
				labTestRecord.setRequestDate(DateUtil.truncateDateString(labTestRecord.getRequestDate(), DateFormat.YMDHM));
				labTestRecord.setTestDate(DateUtil.truncateDateString(labTestRecord.getTestDate(), DateFormat.YMDHM));
			}
		}*/
	}

	@Override
	public int updateLabTestRecordPriFlag(String id, Integer priFlag) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		paramMap.put("priFlag", priFlag);

		return labTestRepository.updateLabTestRecordPriFlag(paramMap);
	}

	@Override
	public List<LabTestSendStatistic> getLabTestSendStatistic(String deptCode,
			String startDate, String endDate, boolean isAll) {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("LabTestServiceImpl getLabTestSendStatistic deptCode is null");
			throw new AlertException("科室为空!");
		}
		
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			Date[] dates = DateUtil.getQueryRegionDates(new Date());
			startDate = DateUtil.format(dates[0], DateFormat.FULL);
			endDate = DateUtil.format(dates[1], DateFormat.FULL);
		}
		
		String allType = MnisConstants.EMPTY;
		if(isAll){
			allType = null;
		}
		
		return labTestRepository.getLabTestSendStatistic(deptCode, startDate, endDate, allType);
	}

	@Override
	public List<LabTestSendRecord> execLabTestSendByBarcode(String barcode,UserInformation user) {
		//1.判断barcode是否存在
		String patIdSource = labTestRepository.getLabTestSendPatIdByBarcode(barcode);
		if(StringUtils.isBlank(patIdSource)){
			LOGGER.error("LabTestServiceImpl execLabTestSendByBarcode barcode not exist");
			throw new AlertException("条码不存在!");
		}
		
		String[] patIdSourceArray = patIdSource.split(MnisConstants.LINE);
		//数据源1:未采集,2:采集
		int source = 1;
		if(patIdSourceArray.length > 1){
			source = Integer.valueOf(patIdSourceArray[1]);
		}
		String patId = patIdSourceArray[0];
		Integer count = labTestRepository.getLabTestSendCountByBarcode(barcode);
		if(null != count && count > 0){
			LOGGER.error("LabTestServiceImpl execLabTestSendByBarcode LabTestSendRecordDetail exist");
			throw new AlertException("条码已送检!");
		}
		LabTestSendRecordDetail labTestSendRecordDetail = new LabTestSendRecordDetail();
		labTestSendRecordDetail.setBarcode(barcode);
		labTestSendRecordDetail.setPatId(patId);
		labTestSendRecordDetail.setStatus(source);
		labTestSendRecordDetail.setSendDate(new Date());
		labTestSendRecordDetail.setSendNurseName(user.getName());
		labTestSendRecordDetail.setSendNurseCode(user.getCode());
		labTestRepository.insertLabTestSend(labTestSendRecordDetail);
		
		return getLabTestSendRecords(user.getDeptCode(),patId,null,null,true);
	}

	@Override
	public List<LabTestSendRecord> getLabTestSendRecords(String deptCode,
			String patId,String startDate,String endDate, boolean isAll) {
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("LabTestServiceImpl getLabTestSendRecords deptCode is null");
			throw new AlertException("科室为空!");
		}
		
		if(StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
			Date[] dates = DateUtil.getQueryRegionDates(new Date());
			startDate = DateUtil.format(dates[0], DateFormat.FULL);
			endDate = DateUtil.format(dates[1], DateFormat.FULL);
		}
		
		String allType = MnisConstants.EMPTY;
		if(isAll){
			allType = null;
		}
		return labTestRepository.getLabTestSendRecords(deptCode, patId, startDate, endDate, allType);
	}

	@Override
	public List<LabTestRecord> getLabTestRecordById(String barCode) {
		if(StringUtils.isBlank(barCode)){
			return null;
		}
		return labTestRepository.getLabTestRecordById(barCode);
	}

	@Override
	public List<LabTestRecord> getPublishLabTests(String deptCode,
			Date startTime, Date endTime) {
		
		if(StringUtils.isBlank(deptCode)){
			return null;
		}
		
		if(null == startTime){
			Date date = new Date();
			startTime = DateUtils.addHours(date, -2);
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		params.put("deptCode", deptCode);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return labTestRepository.getPublishLabTests(params);
	}

}
