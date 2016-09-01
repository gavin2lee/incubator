package com.lachesis.mnis.core.task;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.nursing.NurseShiftService;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

@Service("taskService")
public class TaskServiceImpl implements TaskService {
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private PatientService patientService;
	@Autowired
	private OrderService orderService;

	@Autowired
	private NurseShiftService nurseShiftService;
	

	@Override
	public void addWhiteBoardRecord(WhiteBoardRecord record) {
		if (StringUtils.isEmpty(record.getShowDate())) {
			record.setShowDate(DateUtil.format(DateFormat.YMD));
		}
		WhiteBoardRecord r = taskRepository.getWhiteBoard(record.getItemCode(),
				record.getDeptId(), record.getShowDate());
		if (r != null) {
			taskRepository.deleteWhiteBoard(record.getItemCode(),
					record.getDeptId(), record.getShowDate());
		}
		if(StringUtils.isNotBlank(record.getItemValue())){
			taskRepository.addWhiteBoardRecord(record);
		}
	}

	@Override
	public List<WhiteBoardRecord> getWhiteList(String deptId, String showDate) {

		if (StringUtils.isEmpty(showDate)) {
			showDate = DateUtil.format(DateFormat.YMD);
		}
		WorkUnitStat stat = patientService.getPatientStatisticByDeptCode(deptId);
		List<WhiteBoardRecord> records = taskRepository.getWhiteList(deptId, showDate);
		boolean addCritical = false;
		boolean addHard = false;
		boolean addNewP = false;
		boolean addOutP = false;
/*		boolean t24hinoutP = false;
		boolean t24hurineP = false;
		boolean abdomaenP = false;
		boolean weightP = false;
		boolean bodysighP = false;
		boolean cinnamonP = false;*/
		for (Iterator<WhiteBoardRecord> iterator = records.iterator(); iterator.hasNext();) {
			WhiteBoardRecord whiteBoardRecord = iterator
					.next();
			if(whiteBoardRecord.getItemCode().equals("crisis_patient")){
				addCritical = true;
				whiteBoardRecord.setItemValue(whiteBoardRecord.getItemValue() + (stat.getCriticalPatient()== null ? "　":"　," + stat.getCriticalPatient()));
			}
			if(whiteBoardRecord.getItemCode().equals("hard_patient")){
				addHard = true;
				whiteBoardRecord.setItemValue(whiteBoardRecord.getItemValue() + (stat.getSeriousPatient()== null ? "　":"　," + stat.getSeriousPatient()));
			}
			if(whiteBoardRecord.getItemCode().equals("new_patient")){
				addNewP = true;
				whiteBoardRecord.setItemValue(whiteBoardRecord.getItemValue() + (stat.getNewPatient()== null ? "　":"　," + stat.getNewPatient()));
			}
			if(whiteBoardRecord.getItemCode().equals("out_patient")){
				addOutP = true;
				whiteBoardRecord.setItemValue(whiteBoardRecord.getItemValue() + (stat.getDischarge()== null ? "　":"　," + stat.getDischarge()));
			}
			/*if(whiteBoardRecord.getItemCode().equals("24hinout")){
				t24hinoutP = true;
				List<String> patientIds = patientService.getPatientIDByDeptCodeOrNurseId(deptId, null);
				orderService.getBodysignOrder(patientIds, DateUtil.getCurDateWithMinTime(), DateUtil.getCurDateWithMaxTime());
				whiteBoardRecord.setItemValue(whiteBoardRecord.getItemValue() + (stat.getDischarge()== null ? "　":"　," + stat.getDischarge()));
			}*/
		}
		if(!addCritical){
			records.add(new WhiteBoardRecord(deptId,"crisis_patient","病危",stat.getCriticalPatient()== null ? "　":"　" + stat.getCriticalPatient(),null));
		}
		if(!addHard){
			records.add(new WhiteBoardRecord(deptId,"hard_patient","病重",stat.getSeriousPatient()== null ? "　":"　" + stat.getSeriousPatient(),null));
		}
		if(!addNewP){
			records.add(new WhiteBoardRecord(deptId,"new_patient","新入病人",stat.getNewPatient()== null ? "　":"　" + stat.getNewPatient(),null));
		}
		if(!addOutP){
			records.add(new WhiteBoardRecord(deptId,"out_patient","出院病人",stat.getDischarge()== null ? "　":"　" + stat.getDischarge(),null));
		}
		records.add(new WhiteBoardRecord(deptId,"total_patient","病人总数",String.valueOf(stat.getInpatientCount()),null));
		return records;
		
	}
}
