package com.lachesis.mnis.core.inspection;

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

import com.lachesis.mnis.core.InspectionService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.inspection.repository.InspectionRepository;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Service("inspectionSypacsService")
public class InspectionServiceImpl implements InspectionService {
	private final static Logger LOGGER = LoggerFactory.getLogger(InspectionServiceImpl.class);
	@Autowired
	private InspectionRepository inspectionSypacsRepository;
	@Autowired
	private PatientService patientService;
	
	@Override
	public List<InspectionRecord> getInspectionRecords(String date,
			String patientId) {
		if (StringUtils.isBlank(date)) {
			Date now = new Date();
			Date beginDate = DateUtils.addDays(now, -7);
			return getInspectionRecords(
					DateUtil.format(beginDate, DateFormat.YMD), 
					DateUtil.format(now, DateFormat.YMD), 
					patientId);
		} else {
			return getInspectionRecords(date, date, patientId);
		}
	}

	@Override
	public List<InspectionRecord> getInspectionRecords(String startDate,
			String endDate, String patientId) {
		if (StringUtils.isEmpty(patientId)) {
			throw new RuntimeException("病人ID不能為空");
		}
		
		//是否有必要再查询一次该患者是否存在，前端已经发出请求  END BY Qingzhi.Liu
		Patient inpatientInfo = patientService.getPatientByPatId(patientId);
		if (inpatientInfo == null) {
			throw new RuntimeException("沒有此病人");
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
//		patientId = patientId.substring(4);
		LOGGER.info("InspectionSypacsServiceImpl patientId:"+patientId);
		paramMap.put("patientId", patientId);
		
		//检查可以根据住院号来关联查询，不需要按照住院唯一标识，三院检查没有保存住院唯一标识
		//关联患者表只查询床号、姓名 是否可以取消
		paramMap.put("in_hosp_no", inpatientInfo.getInHospNo());
		if (StringUtils.isNotEmpty(startDate)) {
			paramMap.put("startTime", DateUtil.parse(startDate + DateUtil.START_TIMES));
		}
		if (StringUtils.isNotEmpty(endDate)) {
			paramMap.put("endTime", DateUtil.parse(endDate + DateUtil.END_TIMES));
		}
			
		List<InspectionRecord> inspectionRecords = inspectionSypacsRepository.getInspectionRecord(paramMap);
		LOGGER.info("InspectionSypacsServiceImpl inspectionRecords.size:"+ inspectionRecords.size());
		return inspectionRecords;
	}

	@Override
	public List<InspectionRecord> getInspectionRecordByPatients(
			List<String> patients, String execDate) {
		String[] patientIds = new String[patients.size()] ;
		patients.toArray(patientIds);
		return getInspectionRecordByPatients(patientIds,execDate,execDate,null);
	}

	@Override
	public List<InspectionRecord> getInspectionRecordByPatients(
			String[] patients, String startDate, String endDate,
			String status) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String[] patientIds = new String[patients.length];
		for(int i=0; i<patients.length;i++){
			patientIds[i] = patients[i].substring(4);
		}
		
		paramMap.put("patientIds", patientIds);
		if (StringUtils.isNotEmpty(startDate)) {
			paramMap.put("startTime", DateUtil.parse(startDate + DateUtil.START_TIMES));
		}
		if (StringUtils.isNotEmpty(endDate)) {
			paramMap.put("endTime", DateUtil.parse(endDate + DateUtil.END_TIMES));
		}
		//paramMap.put("status", status);
		
		List<InspectionRecord> inspectionRecords = inspectionSypacsRepository.getInspectionRecordByPatients(paramMap);
		for(InspectionRecord record:inspectionRecords){
			String bedCode = record.getBedNo();
			if(StringUtils.isNotEmpty(bedCode) && bedCode.indexOf("_")>0){
				record.setBedNo(bedCode.split("_")[1]);
			}
		}
		return inspectionRecords;
	}


	@Override
	public int updateInspectionPriFlag(String id, Integer priFlag) {
		/*Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", id);
		paramMap.put("priFlag", priFlag);
		
		return inspectionSypacsRepository.updateInspectionPriFlag(paramMap);*/
		return 0;
	}

	@Override
	public List<InspectionRecord> getPublishInspections(String deptCode,
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
		return inspectionSypacsRepository.getPublishInspections(params);
	}
}
