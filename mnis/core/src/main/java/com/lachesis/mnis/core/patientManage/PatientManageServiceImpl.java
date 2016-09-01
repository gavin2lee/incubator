package com.lachesis.mnis.core.patientManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.PatientManageService;
import com.lachesis.mnis.core.SysDicService;
import com.lachesis.mnis.core.bodysign.repository.BodySignRepository;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientEventDetail;
import com.lachesis.mnis.core.patientManage.entity.PatCureLocalInfo;
import com.lachesis.mnis.core.patientManage.entity.PatLeaveGoout;
import com.lachesis.mnis.core.patientManage.entity.PatNdaManageInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationStatus;
import com.lachesis.mnis.core.patientManage.entity.PatOrderConfiguration;
import com.lachesis.mnis.core.patientManage.reposity.PatientManageRepository;
import com.lachesis.mnis.core.util.CodeUtils;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.StringUtil;

@Service("patientManageService")
public class PatientManageServiceImpl implements PatientManageService {
	
	@Autowired
	private PatientManageRepository patientManageRepository;
	
	@Autowired
	private SysDicService sysDicService;
	
	@Autowired
	private BodySignRepository bodySignRepository;
	
	@Override
	public int savePatOperationInfo(PatOperationInfo patOperationInfo) {
		patOperationInfo.setOperationId(CodeUtils.getSysInvokeId());
		return patientManageRepository.savePatOperationInfo(patOperationInfo);
	}

	@Override
	public String savePatOperationStatus(PatOperationStatus patOperationStatus) {
		//获取最新状态
		PatOperationStatus p =patientManageRepository.getRecentlyPatOperationByPatId(patOperationStatus.getPatId());
		if(null != p){
			String nearStatus = patientManageRepository.getRecentlyPatOperationByPatId(patOperationStatus.getPatId()).getStatus()+"";
			//获取最新状态的后置状态
			String nextStatusTemp = sysDicService.getBackStatus(MnisConstants.SURGERY_ORDER, nearStatus);
			if(StringUtil.hasValue(nextStatusTemp)){
				String nextStatus = nextStatusTemp.split("&")[0];
				String[] temp = nextStatus.split(",");
				for (String status : temp) {
					//当前要执行的操作与后置状态相同，则允许执行
					if(status.equals(patOperationStatus.getStatus()+"")){
						patientManageRepository.savePatOperationStatus(patOperationStatus);
						return "0";
					}
				}
			}
			return nextStatusTemp.split("&")[1]+"未执行！";
		}
		return patientManageRepository.savePatOperationStatus(patOperationStatus)+"";
	}

	@Override
	public PatOperationInfo getPatOperationInfoByPatId(String patId) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("patId", patId);
		conditionMap.put("moduleName", MnisConstants.SURGERY_ORDER);
		return patientManageRepository.getPatOperationInfoByPatId(conditionMap);
	}

	@Override
	public List<PatOperationInfo> queryAllPatOperationInfo() {
		return patientManageRepository.queryAllPatOperationInfo();
	}

	@Override
	public List<PatOperationInfo> queryPatOperationInfoByStatusOrDate(String status, String beginTime,String endTime) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("status", status);
		conditionMap.put("startDate", beginTime);
		conditionMap.put("endDate", endTime);
		conditionMap.put("moduleName", MnisConstants.SURGERY_ORDER);
		return patientManageRepository.queryByStatusOrDate(conditionMap);
	}

	@Override
	public int savePatOrderConfiguration(
			PatOrderConfiguration patOrderConfiguration) {
		return patientManageRepository.savePatOrderConfiguration(patOrderConfiguration);
	}

	@Override
	public List<PatOrderConfiguration> queryAllPatOrderConfiguration() {
		return patientManageRepository.queryAllPatOrderConfiguration();
	}

	@Override
	public int savePatCureLocalInfo(PatCureLocalInfo patCureLocalInfo) {
		return patientManageRepository.savePatCureLocalInfo(patCureLocalInfo);
	}

	@Override
	public int savePatLeaveGoout(PatLeaveGoout patLeaveGoout) {
		return patientManageRepository.savePatLeaveGoout(patLeaveGoout);
	}

	@Override
	public List<PatCureLocalInfo> queryAllOutPatientByDateAndStatus(String beginTime, String endTime, String status) {
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("beginTime", beginTime);
		conditionMap.put("endTime", endTime);
		conditionMap.put("status", status);
		return patientManageRepository.queryAllOutPatientByDateAndStatus(conditionMap);
	}

	@Override
	public void savePatientEvent(PatientEvent record){
		//生命体征事件表，查询是否已经存在，如果存在则更新，如果不存在则新增
		if(null != record){
			List<PatientEvent> dbevent = bodySignRepository.queryEvent(record);
			//更新的事件
			List<PatientEvent> upEventList = new ArrayList<PatientEvent>();
			//新增的事件
			List<PatientEvent> itEventList = new ArrayList<PatientEvent>();
			if(null == dbevent || dbevent.isEmpty()){
				itEventList.add(record);
			}else{
				upEventList.add(record);
			}
			//更新事件
			if(null != upEventList){
				for(PatientEvent event : upEventList){
					bodySignRepository.updatePatEvent(event);
				}
			}
			//插入事件
			if(null != itEventList){
				for(PatientEvent event : itEventList){
					bodySignRepository.insertPatEvent(event);
				}
			}
		}
	}

	@Override
	public List<PatientEventDetail> queryInOutEventByTime(String eventCode,
			String beginTime, String endTime) {
		return bodySignRepository.queryInOutEventByTime(eventCode, beginTime, endTime);
	}

	@Override
	public PatNdaManageInfo queryNdaManageInfo(String beginTime, String endTime) {
		Map<String, String> conditionMap = new HashMap<String, String>();
		conditionMap.put("beginTime", beginTime);
		conditionMap.put("endTime", endTime);
		return patientManageRepository.queryNdaManageInfo(conditionMap);
	}
}
