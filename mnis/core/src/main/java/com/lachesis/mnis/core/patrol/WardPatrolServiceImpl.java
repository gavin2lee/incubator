package com.lachesis.mnis.core.patrol;

import java.text.ParseException;
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

import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.WardPatrolService;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.patrol.repository.WardPatrolRepository;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Service("wardPatrolService")
public class WardPatrolServiceImpl implements WardPatrolService {
	@Autowired
	private WardPatrolRepository wardPatrolRepository;
	@Autowired
	private TaskService taskService;
	@Autowired
	private PatientService patientService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(WardPatrolServiceImpl.class);
	private static final long ONE_HOUR_MILLS = 1000L * 60 * 60;

	@Override
	public List<WardPatrolInfo> getWardPatrolPlan(String deptCode,
			String nurseCode, String tendLevel, String strDate) {
		Map<String, Object> mapValue = new HashMap<String, Object>();
		// mapValue.put("deptCode", deptCode);
		if (StringUtils.isEmpty(deptCode) && StringUtils.isEmpty(nurseCode)) {
			throw new MnisException("部门ID和护士ID参数至少需要一个");
		}


		//查询关注患者
		if(!StringUtils.isBlank(nurseCode)){
			List<String> patients = patientService.getPatientByDeptCodeOrUserCode(
					nurseCode, deptCode);
			mapValue.put("patientIds", patients);
		}
		
		Date date = null;
		if(StringUtils.isEmpty(strDate)){
			date = new Date();
		}
		else {
			date = DateUtil.parse(strDate.substring(0,10), DateFormat.YMD);	
		}
		Date[] dates = DateUtil.getQueryRegionDates(date);
		mapValue.put("tendLevel", tendLevel);
		mapValue.put("deptCode", deptCode);
		mapValue.put("startTime", dates[0]);
		mapValue.put("endTime", dates[1]);
		List<WardPatrolInfo> list = wardPatrolRepository
				.getWardPatrolPlan(mapValue);
		try {
			setWardPatrolInfo(list);
		} catch (ParseException e) {
			LOGGER.error(e.toString());
		}
		
		//如果是历史查询，则不存在待巡视的情况，均为已巡视
		if(null !=date && date.before(DateUtil.getCurDateWithMinTime())){
			for(WardPatrolInfo item:list){
				if(-1 == item.getFlag()){
					item.setFlag(1);
				}
			}
		}
		
		return list;
	}

	private void setWardPatrolInfo(List<WardPatrolInfo> list)
			throws ParseException {
		List<String> listPatient = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			WardPatrolInfo wardPatrolInfo = list.get(i);
			if (!listPatient.contains(wardPatrolInfo.getPatientId())) {
				Patient patient = patientService
						.getPatientByPatId(wardPatrolInfo.getPatientId());
				if (patient != null) {
					wardPatrolInfo.setInpatientInfo(patient);
					listPatient.add(wardPatrolInfo.getPatientId());
					disposeWardPatrol(wardPatrolInfo);
				} else {
					list.remove(i);
					i--;
				}
			} else {
				list.remove(i);
				i--;
			}
		}
	}

	private void disposeWardPatrol(WardPatrolInfo wardPatrolInfo) {
		wardPatrolInfo.setFlag(-1);
		
		// 特级护理不显示下一次巡视时间
		if (wardPatrolInfo.getTendLevel() == null
				|| wardPatrolInfo.getTendLevel() == 0) {
			//wardPatrolInfo.setPatrolDate(wardPatrolInfo.getPatrolDate());
			wardPatrolInfo.setNextPatrolDate(null);
			return;
		}
		// 设置flag，-1：待巡视，1：已巡视。特级护理患者永远为-1
		// (根据病人护理等级计算，比较本次巡视时间与系统当前时间之差，若差值在X(X=护理等级)小时之内则表示已巡视，否则为待巡视。)
		if (wardPatrolInfo.getPatrolDate() != null) {
			long diffMillis = System.currentTimeMillis()
					- wardPatrolInfo.getPatrolDate().getTime();
			long hourMillis = wardPatrolInfo.getTendLevel() * ONE_HOUR_MILLS;
			if (diffMillis <= hourMillis) {
				wardPatrolInfo.setFlag(1);
			} else {
				wardPatrolInfo.setFlag(-1);
			}
		}
	}

	@Override
	public WardPatrolInfo saveWardPatrolInfo(String nurseCode, String deptCode,
			String patientId) {
		if (StringUtils.isEmpty(nurseCode) || StringUtils.isEmpty(deptCode)
				|| StringUtils.isEmpty(patientId)) {
			return null;
		}
		Patient inpatientInfo = patientService.getPatientByPatId(patientId);
		if (inpatientInfo != null) {
			return saveWardPatrolInfoForPatient(inpatientInfo, nurseCode,
					deptCode);
		} else {
			return null;
		}
	}

	@Override
	public List<WardPatrolInfo> getWardPatrolLogByPatId(String patientId,
			String day) {
		List<WardPatrolInfo> wpList = null;

		Date date = null;
		if (StringUtils.isBlank(day)) {
			date = new Date();
		} else {
			date = DateUtil.parse(day.substring(0, 10), DateFormat.YMD);
		}
		Date[] dates = DateUtil.getQueryRegionDates(date);
		if (patientId != null) {
			List<String> patients = new ArrayList<String>();
			patients.add(patientId);
			wpList = wardPatrolRepository.selectWardPatrolByPatId(patients,
					dates[0], dates[1]);
		}
		return wpList;
	}

	@Override
	public WardPatrolInfo saveWardPatrolInfoForPatient(Patient inpatientInfo,
			String nurseCode, String deptCode) {
		if (null == inpatientInfo
				|| StringUtils.isBlank(inpatientInfo.getPatId())) {
			throw new IllegalArgumentException("无效的病人信息");
		}

		Date patrolDate = new Date();
		int tendLevel = inpatientInfo.getTendLevel();
		long nextDateTimeMills = patrolDate.getTime();
		switch (tendLevel) {
		case 1:
			nextDateTimeMills += ONE_HOUR_MILLS;
			break;
		case 2:
			nextDateTimeMills += ONE_HOUR_MILLS * 2;
			break;
		case 3:
			nextDateTimeMills += ONE_HOUR_MILLS * 3;
			break;
		default:
			break;
		}
		Date nextPatrolDate = new Date(nextDateTimeMills);

		WardPatrolInfo info = new WardPatrolInfo();
		info.setPatientId(inpatientInfo.getPatId());
		info.setNurseEmplCode(nurseCode);
		info.setDeptId(deptCode);
		info.setTendLevel(tendLevel);
		info.setPatrolDate(patrolDate);
		info.setBedCode(inpatientInfo.getBedCode());
		if (tendLevel >= 1 && tendLevel <= 3) {
			info.setNextPatrolDate(nextPatrolDate);
		}

		wardPatrolRepository.saveWardPatrolInfo(info);

		disposeWardPatrol(info);

		if (info.getTendLevel() == 0) {
			info.setFlag(1);
		}
		return info;
	}

	@Override
	public List<WardPatrolInfo> getWardPatrolLogByPatients(
			List<String> patients, String day) {
		List<WardPatrolInfo> wpList = null;
		if (patients != null) {
			Date date = null;
			if (StringUtils.isBlank(day)) {
				date = new Date();
			} else {
				date = DateUtil.parse(day.substring(0, 10), DateFormat.YMD);
			}
			Date[] dates = DateUtil.getQueryRegionDates(date);
			wpList = wardPatrolRepository.selectWardPatrolByPatId(patients,
					dates[0], dates[1]);
		}
		return wpList;
	}

	@Override
	public List<WardPatrolInfo> getPublishWardPatrols(String deptCode,
			Date startTime, Date endTime) {
		if(StringUtils.isBlank(deptCode)){
			throw new AlertException("科室参数为空!");
		}
		
		if(startTime == null){
			Date date = new Date();
			startTime = DateUtils.addHours(date, -2);
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("deptCode", deptCode);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return wardPatrolRepository.getPublishWardPatrols(params);
	}
}
