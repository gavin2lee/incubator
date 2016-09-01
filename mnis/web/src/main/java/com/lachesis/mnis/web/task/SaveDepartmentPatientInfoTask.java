package com.lachesis.mnis.web.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.identity.entity.SysDept;
import com.lachesis.mnis.core.nursing.NurseShiftService;
import com.lachesis.mnis.core.old.doc.DepartmentPatientSummary;
import com.lachesis.mnis.core.old.doc.service.DepartmentPatientSummaryService;
import com.lachesis.mnis.core.patient.entity.Bed;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.common.util.PatientUtils;
import com.lachesis.mnis.web.vo.BedPatientInfo;
import com.lachesis.mnis.web.vo.BedPatientInfoResult;

@Component
@Lazy
public class SaveDepartmentPatientInfoTask {
	
	@Autowired
	private BodySignService bodySignService;
	@Autowired
	private NurseShiftService nurseShiftService;
	@Autowired
	private PatientService patientService;
	@SuppressWarnings("rawtypes")
	@Autowired
	private DepartmentPatientSummaryService departmentPatientSummaryService;
	@Autowired
	private IdentityService identityService;
	
	
	
	@SuppressWarnings("unchecked")
	//@Scheduled(cron="0 0 0,8,16 * * ?")
	// 每天0点 8点 16点触发 
	public void executive() {		
		List<SysDept> list = identityService.queryDeptments();
		String item;
		int nowNum;
		DepartmentPatientSummary departmentPatientSummary;
		String date;
		for (SysDept sysDept : list) {
			BedPatientInfoResult result = getBedPatientList(1,sysDept.getCode(), true);
			WorkUnitStat data = result.getWorkUnitStatistics();
			if(null != data){
				nowNum = data.getInpatientCount() - data.getEmptyBedCount();
				item="原有："+data.getInpatientCount()+"人;" +
						"现有："+nowNum+"人;" +
						"入院："+data.getNewPatientCount()+"人;" +
						"出院："+data.getDischargeCount()+"人;"+
						"转入："+data.getTransInPatientCount()+"人;" +
						"转院："+data.getTransOutPatientCount()+"人;" +
						"死亡："+data.getDeadPatientCount()+"人;" +
						"分娩："+data.getInDeliveryPatientCount()+"人;" +
						"病重："+data.getSeriousPatientCount()+"人;" +
						"病危："+data.getCriticalPatientCount()+"人;" +
						"手术："+data.getInSurgeryPatientCount()+"人;";
				departmentPatientSummary = new DepartmentPatientSummary();
				departmentPatientSummary.setSummary(item);
				departmentPatientSummary.setDeptRefid(sysDept.getCode());
				date = DateUtil.format();
				departmentPatientSummary.setSummaryTime(date);
				/************检查是否已经插入该数据*****************/
				String startTime = date.substring(0,15) + 0;
				String endTime = date.substring(0,15) + 2;
				List<DepartmentPatientSummary> summaryList = departmentPatientSummaryService.getDepartmentPatientSummaryListByTime(startTime, endTime, sysDept.getCode());
				if(null == summaryList || summaryList.isEmpty()){					
					departmentPatientSummaryService.insert(departmentPatientSummary);
				}
			}			
		}
	}
	
	/**
	 * 
	 * 获取床位列表
	 * 
	 * @param workUnitType
	 * @param workUnitCode
	 * @param showEmptyBeds
	 * @return
	 */
	private BedPatientInfoResult getBedPatientList(Integer workUnitType,
			String workUnitCode, boolean showEmptyBeds) {
		List<Patient> list = patientService.getWardPatientList(workUnitType, workUnitCode);
		BedBaseInfoComparator bedBaseInfoComparator = new BedBaseInfoComparator();
		Collections.sort(list, new Comparator<Patient>() {
		    public int compare(Patient p1, Patient p2){
		        int code1 = Integer.parseInt(p1.getBedCode());
		        int code2 = Integer.parseInt(p2.getBedCode());
		        if(code1 > code2){
		        	return 1;
		        }else{
		        	return -1;
		        }
		    }
		});
		WorkUnitStat stat = patientService.getPatientStatisticByDeptCode(workUnitCode);
//				nurseShiftService.calcWorkUnitStatistics(workUnitType, workUnitCode, 0);

		int highTempCount = 0;
/*		List<BedEntity> bedBaseInfos = patientService.getBedPatientList(workUnitType, workUnitCode, false);
		BodySignMsmentRule bodySignMsmentRule = bodySignService.getBodySignMsmentRule(BodySignConstants.HIGHTEMP);
		for (BedEntity bedBaseInfo : bedBaseInfos) {
			InpatientEntity inpatientInfo = bedBaseInfo.getInpatientInfo();
			if(inpatientInfo == null || StringUtils.isEmpty(inpatientInfo.getAdmitDate())) {
				continue;
			}
			/// 体温超标要提醒
			// 如果最近一次体征测量得到的体温值超标，则进行测量提醒
			if(bodySignMsmentRule != null) {
				double ruleValue = Double.parseDouble(bodySignMsmentRule.getRuleValue());
				double bodyTemp = bodySignService.getLatestBodyTemperature(inpatientInfo.getPatientBaseInfo().getPatientId());
				if (bodyTemp >= ruleValue) {
					highTempCount ++;
				}
			}
		}*/
		stat.setHighTempCount(highTempCount);

		return handleDataConvertDetail(list, stat);
	}
	
	/**
	 * 
	 * 根据查询的数据结果，处理相关输出，供界面数据显示
	 * 
	 * @param reult
	 * @return
	 */
	private BedPatientInfoResult handleDataConvertDetail(List<Patient> list, WorkUnitStat stat) {
		BedPatientInfoResult rst = new BedPatientInfoResult();
		List<BedPatientInfo> bedPatList = new ArrayList<BedPatientInfo>();

		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		String currentMinDate = DateUtil.format(c.getTime(), DateFormat.YMDHM);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		String currentMaxDate = DateUtil.format(c.getTime(), DateFormat.YMDHM);

		BedPatientInfo bedPatientInfo = null;
		if (list != null && !list.isEmpty()) {
			for (Patient patient : list) {
				bedPatientInfo = PatientUtils.copyBedPatientInfoProperties(patient, currentMinDate, currentMaxDate);
				bedPatList.add(bedPatientInfo);
			}
		}

		rst.setBedInfoList(bedPatList);
		rst.setWorkUnitStatistics(stat);
		return rst;
	}
	
	public class BedBaseInfoComparator implements Comparator<Bed>{
	    @Override
	    public int compare(Bed p1, Bed p2){
	        int code1 = Integer.parseInt(p1.getCode());
	        int code2 = Integer.parseInt(p2.getCode());
	        if(code1 > code2){
	        	return 1;
	        }else{
	        	return -1;
	        }
	    }
	}
}
