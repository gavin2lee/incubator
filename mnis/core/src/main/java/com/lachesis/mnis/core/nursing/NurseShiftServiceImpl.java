package com.lachesis.mnis.core.nursing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.patient.entity.Bed;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.NurseConstants;
import com.lachesis.mnis.core.util.StringUtil;

@Service("nurseShiftService")
public class NurseShiftServiceImpl implements NurseShiftService {
	@Autowired
	private NurseShiftRepository nurseShiftRepository;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private PatientService patientService;

	@Autowired
	OrderService orderService;

	@Autowired
	private BodySignService bodySignService;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NurseShiftServiceImpl.class);

	// 24小时的字符型表示
	private static final int FULL_HOURS = 24;
	private static String[] h24 = new String[FULL_HOURS];
	static {
		for (int i = 0; i < FULL_HOURS; i++) {
			h24[i] = String.format("%02d:00", i);
		}
	}
	/**
	 * 时段区间差,第一个时段为上一天00:00-8:00,8-16:00,16-00:00
	 */
	private final int[] nurseShiftHourOffsets = new int[NurseConstants.SHIFT_NURSE_TIME_INDEX.length];

	/**
	 * 初始化时段区间差10,7,7
	 */
	public void initTimeRanges() {
		final int l = nurseShiftHourOffsets.length;
		for (int i = 0; i < l; i++) {
			nurseShiftHourOffsets[i] = (NurseConstants.SHIFT_NURSE_TIME_INDEX[i
					% l]
					- NurseConstants.SHIFT_NURSE_TIME_INDEX[(i + l - 1) % l] + FULL_HOURS)
					% FULL_HOURS;
		}
	}

	/**
	 * 计算上一次当班（相对于当前时间）的时间段 假定事件节点之间相差的是整数倍小时
	 * 
	 * @param includeCurrent
	 *            是否包含当前时间段
	 * @return
	 */
	public String[] calcLastShiftTimeRange(boolean includeCurrent) {
		initTimeRanges();
		String[] lstRange = new String[2];
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		String hm = DateUtil.format(date, DateFormat.HM);
		
		/**
		 * STEP1: 当前时间落在哪个区间
		 */
		int rangeIndex = 0;
		int l = NurseConstants.SHIFT_NURSE_TIME_INDEX.length;
		for (int i = 0; i < NurseConstants.SHIFT_NURSE_TIME_INDEX.length; i++) {
			if (isInCircularRange(h24[NurseConstants.SHIFT_NURSE_TIME_INDEX[i
					% l]], h24[NurseConstants.SHIFT_NURSE_TIME_INDEX[(i + 1)
					% l]], hm)) {
				rangeIndex = i;
				break;
			}
		}

		/**
		 * STEP2： 计算上一个时间区间，即上一个值班时间段，因为可能跨天，所以用Calendar计算
		 */
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY,
				NurseConstants.SHIFT_NURSE_TIME_INDEX[rangeIndex]);

		if (includeCurrent) {// 当前时段
			lstRange[0] = DateUtil.format(cal.getTime(), DateFormat.FULL);
			// 加时间差
			cal.add(Calendar.HOUR_OF_DAY, nurseShiftHourOffsets[rangeIndex]);
			lstRange[1] = DateUtil.format(cal.getTime(), DateFormat.FULL);
		} else {// 上一个时段
			lstRange[1] = DateUtil.format(cal.getTime(), DateFormat.FULL);
			// 减时间差
			cal.add(Calendar.HOUR_OF_DAY, -nurseShiftHourOffsets[rangeIndex]);

			lstRange[0] = DateUtil.format(cal.getTime(), DateFormat.FULL);
		}

		return lstRange;
	}

	private boolean isInCircularRange(String lower, String upper, String value) {
		if (lower.compareTo(upper) > 0) {
			return value.compareTo(lower) > 0 || value.compareTo(upper) < 0;
		} else {
			return value.compareTo(lower) > 0 && value.compareTo(upper) < 0;
		}
	}

	/**
	 * 获取所有的交班本信息
	 */
	@Override
	public List<NurseShiftEntity> getNurseShiftList(String deptCode,
			String nurseId, String patId, String rangeType) {
		return getNurseShiftListByTime(deptCode, nurseId, patId, null, null,
				rangeType);
	}

	/**
	 * 获取指定时段的交班本信息(交接班信息和记录信息)
	 */
	@Override
	public List<NurseShiftEntity> getNurseShiftListByTime(String deptCode,
			String nurseId, String patId, String startDateStr,
			String endDateStr, String rangeType) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		List<String> patients = null;
		if(StringUtils.isEmpty(deptCode) && StringUtils.isEmpty(nurseId) ){
			if(StringUtils.isEmpty(patId))
				throw new MnisException("部门ID,护士ID和病人Id参数至少需要一个");
			else{
				paramsMap.put("patId", patId);
			}
		}else{
			patients = patientService.getPatientByDeptCodeOrUserCode(nurseId, deptCode);
			
			if (patients == null || patients.size() == 0) {
				patients = new ArrayList<String>();
				patients.add("-1");
			}
			paramsMap.put("patients", patients);
		}
		
		Date startDate = DateUtil.parse(startDateStr, DateFormat.FULL);
		Date endDate = DateUtil.parse(endDateStr, DateFormat.FULL);
		// 默认为当前时段
		if (null == startDate || null == endDate) {
			String[] preShiftTimeRange = this.calcLastShiftTimeRange(true);
			startDate = DateUtil.parse(preShiftTimeRange[0], DateFormat.FULL);
			endDate = DateUtil.parse(preShiftTimeRange[1], DateFormat.FULL);
		}

		if (StringUtils.isBlank(rangeType)) {
			rangeType = "0";
		}

		paramsMap.put("rangeType", rangeType);
		paramsMap.put("startDate", startDate);
		paramsMap.put("endDate", endDate);

		List<NurseShiftEntity> nurseShiftEntities = this.nurseShiftRepository
				.getNurseShifts(paramsMap);
		return sortNurseShiftListByBedNo(nurseShiftEntities);
	}

	@Transactional
	@Override
	public int saveShiftRecord(NurseShiftRecordEntity nurseShiftRecordEntity) {
		String patId = nurseShiftRecordEntity.getPatientId();
		String deptCode = nurseShiftRecordEntity.getDeptCode();
		if (StringUtils.isBlank(patId) || StringUtils.isBlank(deptCode)) {
			LOGGER.debug("saveShiftRecord patId or deptCode is null");
			throw new AlertException("病人参数错误");
		}

		int flag = 0;

		// 获取当前时间区间
		String[] curShiftBrTimeRange = this.calcLastShiftTimeRange(true);
		nurseShiftRecordEntity.setShiftRecordStartDate(
				DateUtil.parse(curShiftBrTimeRange[0], DateFormat.FULL));
		nurseShiftRecordEntity.setShiftRecordEndDate(
				DateUtil.parse(curShiftBrTimeRange[1], DateFormat.FULL));
		if (StringUtils.isBlank(nurseShiftRecordEntity.getNurseShiftId())) {
			nurseShiftRecordEntity.setNurseShiftId(getNurseShiftId(patId,
					curShiftBrTimeRange[0], curShiftBrTimeRange[1]));
		}
		// 主键为UUID
		nurseShiftRecordEntity.setShiftRecordId(StringUtil.getUUID());

		// 1.保存交班本记录信息
		flag = this.nurseShiftRepository
				.insertShiftRecord(nurseShiftRecordEntity);
		// 2.根据病人id，判断交班本是否存在
		if ((this.nurseShiftRepository
				.getNurseShiftEntityById(nurseShiftRecordEntity
						.getNurseShiftId())) == null)
		// 3.不存在，创建新的交班本
		{
			NurseShiftEntity nurseShiftEntity = new NurseShiftEntity();
			nurseShiftEntity.setNurseShiftId(getNurseShiftId(patId,
					curShiftBrTimeRange[0], curShiftBrTimeRange[1]));
			nurseShiftEntity.setPatientId(patId);
			nurseShiftEntity.setDeptCode(deptCode);
			nurseShiftEntity.setShiftStartDate(
					DateUtil.parse(curShiftBrTimeRange[0], DateFormat.FULL));
			nurseShiftEntity.setShiftEndDate(
					DateUtil.parse(curShiftBrTimeRange[1], DateFormat.FULL));
			flag = this.saveNurseShift(nurseShiftEntity);
		}

		return flag;
	}

	@Override
	public int updateShiftRecord(NurseShiftRecordEntity nurseShiftRecordEntity) {
		int flag = 0;
		NurseShiftRecordEntity entity = getShiftRecordById(nurseShiftRecordEntity
				.getShiftRecordId());
		if (entity != null) {
			// 修改时间没有，则默认为当前时间
			if (null == (entity.getShiftRecordTime())) {
				nurseShiftRecordEntity.setShiftRecordTime(new Date());
				String[] curShiftTimeRange = this.calcLastShiftTimeRange(true);
				nurseShiftRecordEntity.setShiftRecordStartDate(
						DateUtil.parse(curShiftTimeRange[0], DateFormat.FULL));
				nurseShiftRecordEntity.setShiftRecordEndDate(
						DateUtil.parse(curShiftTimeRange[1], DateFormat.FULL));
			} else {
				nurseShiftRecordEntity.setShiftRecordStartDate(entity
						.getShiftRecordStartDate());
				nurseShiftRecordEntity.setShiftRecordEndDate(entity
						.getShiftRecordEndDate());
			}

			flag = this.nurseShiftRepository
					.updateShiftRecord(nurseShiftRecordEntity);
		} else {
			LOGGER.debug("updateShiftRecord nurseShiftRecordEntity not exist");
			throw new AlertException("修改的记录不存在!");
		}

		return flag;
	}

	@Override
	public int deleteShiftRecord(String shiftRecordId) {
		int flag = 0;
		// 1.判断记录是否存在
		NurseShiftRecordEntity nurseShiftRecordEntity = getShiftRecordById(shiftRecordId);
		if (nurseShiftRecordEntity != null) {
			LOGGER.debug("deleteShiftRecord nurseShiftRecordEntity.getNurseShiftId():"
					+ nurseShiftRecordEntity.getNurseShiftId());
			if (nurseShiftRecordEntity.getNurseShiftId() == null) {
				return flag;
			}
			flag = this.nurseShiftRepository
					.deleteShiftRecordById(shiftRecordId);
			// 2.根据nurseShiftId判断记录是否存在，若不存在，根据nurseShiftId删除交班本

			List<NurseShiftRecordEntity> nurseShiftRecordEntities = this.nurseShiftRepository
					.getShiftRecordsByNurseShiftId(nurseShiftRecordEntity
							.getNurseShiftId());
			if (nurseShiftRecordEntities == null
					|| nurseShiftRecordEntities.size() == 0) {
				flag = this.nurseShiftRepository
						.deleteNurseShiftById(nurseShiftRecordEntity
								.getNurseShiftId());
			}
		}

		return flag;
	}

	/**
	 * 交班本id有patId和startDate数字和endDate数字组合
	 * 
	 * @param patId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private String getNurseShiftId(String patId, String startDate, String endDate) {
		return patId + StringUtil.replace(startDate) + StringUtil.replace(endDate);
	}

	/**
	 * 设置交班本中病人信息
	 * 
	 * @param nurseShiftEntity
	 * @return
	 */
	private NurseShiftEntity setNurseShiftPatientInfo(
			NurseShiftEntity nurseShiftEntity) {
		if (StringUtils.isBlank(nurseShiftEntity.getPatientId())) {
			throw new AlertException("病人参数错误");
		}
		Patient patient = patientService
				.getPatientByPatId(nurseShiftEntity.getPatientId());
		if (patient == null) {
			LOGGER.debug("setNurseShiftPatientInfo patient not exist");
			throw new AlertException("病人不存在");
		}
		nurseShiftEntity.setPatientName(patient.getName());
		//nurseShiftEntity.setDeptName(inpatientEntity.getDeptName());
		nurseShiftEntity.setHospNo(patient.getInHospNo());
		nurseShiftEntity.setBedNo(patient.getBedCode());
		// 怎么获取病人病危状态和诊断结果
		nurseShiftEntity.setDiagnose(patient.getInDiag());

		// 将病危'普通"，"危重"等转为"0"，"1"等
		String criticalStatus = "0";
		//TODO 
		/*if (inpatientEntity.getPatientCondition() != null) {
			if (NurseConstants.PATIENT_CONDITION[0].equals(inpatientEntity
					.getPatientCondition())) {
				criticalStatus = "0";
			}
			if (NurseConstants.PATIENT_CONDITION[1].equals(inpatientEntity
					.getPatientCondition())) {
				criticalStatus = "1";
			}
			if (NurseConstants.PATIENT_CONDITION[2].equals(inpatientEntity
					.getPatientCondition())) {
				criticalStatus = "2";
			}
		}*/

		nurseShiftEntity.setCriticalStatus(criticalStatus);
		nurseShiftEntity.setTend(patient.getTendLevel());
		return nurseShiftEntity;
	}

	/**
	 * 设置交班本相关信息
	 * 
	 * @param nurseShiftEntity
	 *            前台传入的实体
	 * @param existNurseShiftEntity
	 *            数据库查询的实体
	 * @param shiftType
	 *            交接班类型(0:交班,1:接班)
	 * @return
	 */
	private NurseShiftEntity getNurseShiftEntity(
			NurseShiftEntity nurseShiftEntity,
			NurseShiftEntity existNurseShiftEntity, String shiftType) {
		String patientId = nurseShiftEntity.getPatientId();
		String deptCode = nurseShiftEntity.getDeptCode();
		if (StringUtils.isBlank(patientId) || StringUtils.isBlank(deptCode)) {
			throw new AlertException("病人参数错误");
		}

		// 新增
		if (existNurseShiftEntity == null) {
			// 设置时段(新增)
			nurseShiftEntity = setNurseShiftPatientInfo(nurseShiftEntity);
			if (null == (nurseShiftEntity.getShiftStartDate())
					|| null == (nurseShiftEntity.getShiftEndDate())) {
				String[] currentShiftTimeRange = this
						.calcLastShiftTimeRange(true);
				nurseShiftEntity.setShiftStartDate(
						DateUtil.parse(currentShiftTimeRange[0], DateFormat.FULL));
				nurseShiftEntity.setShiftEndDate(
						DateUtil.parse(currentShiftTimeRange[0], DateFormat.FULL));
			}

			if (StringUtils.isBlank(nurseShiftEntity.getNurseShiftId())) {
				nurseShiftEntity.setNurseShiftId(
						getNurseShiftId(
								patientId,
								DateUtil.format( nurseShiftEntity.getShiftStartDate(), DateFormat.FULL), 
								DateUtil.format(nurseShiftEntity.getShiftEndDate(), DateFormat.FULL)));
			}
			nurseShiftEntity.setShiftStatus("0");

		} else {// 修改
			// existNurseShiftEntity =
			// setNurseShiftPatientInfo(existNurseShiftEntity);
			if ("0".equals(shiftType)) {// 交班
				nurseShiftEntity.setShiftNurseId(nurseShiftEntity
						.getShiftNurseId());
				nurseShiftEntity.setShiftNurseName(nurseShiftEntity
						.getShiftNurseName());
				nurseShiftEntity.setShiftDate(nurseShiftEntity.getShiftDate());
				nurseShiftEntity.setShiftStatus("1");
			} else if ("1".equals(shiftType)) {// 接班
				nurseShiftEntity.setSuccessionNurseId(nurseShiftEntity
						.getSuccessionNurseId());
				nurseShiftEntity.setSuccessionNurseName(nurseShiftEntity
						.getSuccessionNurseName());
				nurseShiftEntity.setSuccessionDate(nurseShiftEntity
						.getSuccessionDate());
				nurseShiftEntity.setShiftStatus("2");
			}

		}
		return nurseShiftEntity;
	}

	@Override
	public int saveNurseShift(NurseShiftEntity nurseShiftEntity) {
		// 补充相关记录
		nurseShiftEntity = getNurseShiftEntity(nurseShiftEntity, null, null);
		return this.nurseShiftRepository.insertNurseShift(nurseShiftEntity);
	}

	/**
	 * shiftType:交接班类型
	 */
	@Override
	public int updateNurseShift(NurseShiftEntity nurseShiftEntity,
			String shiftType) {
		int flag = 0;
		String nurseShiftId = nurseShiftEntity.getNurseShiftId();

		if (nurseShiftId == null) {
			throw new AlertException("交班本参数错误!");
		}
		NurseShiftEntity existNurseShiftEntity = this.nurseShiftRepository
				.getNurseShiftEntityById(nurseShiftId);
		// 判断该交班本是否存在
		if (existNurseShiftEntity != null) {
			nurseShiftEntity = getNurseShiftEntity(nurseShiftEntity,
					existNurseShiftEntity, shiftType);
			flag = this.nurseShiftRepository.updateNurseShift(nurseShiftEntity);
		}

		return flag;
	}

	@Override
	public int deleteNurseShiftById(String nurseShiftId) {
		int flag = 0;
		// 获取交接班
		NurseShiftEntity nurseShiftEntity = this.nurseShiftRepository
				.getNurseShiftEntityById(nurseShiftId);
		if (nurseShiftEntity != null) {
			// 删除
			flag = this.nurseShiftRepository.deleteNurseShiftById(nurseShiftId);
			if (flag == 0) {
				throw new RuntimeException("交接班数据删除失败!");
			}

			// 判断交班本记录是否存在
			List<NurseShiftRecordEntity> NurseShiftRecordEntities = this.nurseShiftRepository
					.getShiftRecordsByNurseShiftId(nurseShiftEntity
							.getNurseShiftId());
			if (NurseShiftRecordEntities != null
					&& NurseShiftRecordEntities.size() > 0) {
				for (NurseShiftRecordEntity nurseShiftRecordEntity : NurseShiftRecordEntities) {
					// 删除交班本记录数据
					if (StringUtils.isNotBlank(nurseShiftRecordEntity
							.getShiftRecordId()))
						flag = deleteShiftRecord(nurseShiftRecordEntity
								.getShiftRecordId());
				}
			}
		} else {
			throw new AlertException("删除信息不存在!");
		}

		return flag;
	}

	// @Override
	// public List<PatientEventInfo> getPatientEventInfoList(String deptCode,
	// String nurseCode) {
	// List<PatientEventInfo> peiList = new ArrayList<PatientEventInfo>();
	// if (deptCode != null) {
	// String[] timeRange = calcLastShiftTimeRange(INCLUDE_CURRENT);
	// // peiList.addAll(nurseShiftRepository.selectNurseShiftRecord(deptCode,
	// // timeRange[0], timeRange[1]));
	// }
	// Collections.sort(peiList, new Comparator<PatientEventInfo>() {
	// @Override
	// public int compare(PatientEventInfo o1, PatientEventInfo o2) {
	// return o1.getInpatientInfo().getBedCode()
	// .compareTo(o2.getInpatientInfo().getBedCode());
	// }
	// });
	// return peiList;
	// }
	//
	// @Override
	// public List<PatientEventInfo> getPatientEventInfoListByTime(
	// String deptCode, String nurseCode, String time1, String time2) {
	// List<PatientEventInfo> peiList = new ArrayList<PatientEventInfo>();
	// if (deptCode != null) {
	// // peiList.addAll(nurseShiftRepository.selectNurseShiftRecord(deptCode,
	// // time1, time2));
	// }
	// Collections.sort(peiList, new Comparator<PatientEventInfo>() {
	// @Override
	// public int compare(PatientEventInfo o1, PatientEventInfo o2) {
	// return o1.getInpatientInfo().getBedCode()
	// .compareTo(o2.getInpatientInfo().getBedCode());
	// }
	// });
	// return peiList;
	// }

	/**
	 * 按 床号排序
	 * 
	 * @param nurseShiftEntities
	 * @return
	 */
	private List<NurseShiftEntity> sortNurseShiftListByBedNo(
			List<NurseShiftEntity> nurseShiftEntities) {
		Collections.sort(nurseShiftEntities,
				new Comparator<NurseShiftEntity>() {
					@Override
					public int compare(NurseShiftEntity o1, NurseShiftEntity o2) {
						return o1.getBedNo().compareTo(o2.getBedNo());
					}
				});

		return nurseShiftEntities;
	}

	//
	// @Override
	// public void updateNurseShiftRecord(String shiftBookId, String problem){
	// // nurseShiftRepository.updateNurseShiftRecord(shiftBookId, problem);
	// }
	// @Override
	// public void deleteNurseShift(String shiftBookId){
	// // nurseShiftRepository.deleteNurseShift(shiftBookId);
	// }
	//
	// @Override
	// public int saveEventInfoInNurseShift(EventInfo eventInfo, String
	// deptCode) {
	// int i = CrudConstants.CODE_NULL_ARGUMENT;
	// if (validateEventInfo(eventInfo) && deptCode != null) {
	// // i = saveNurseShiftRecord(new NurseShiftEntity(eventInfo, deptCode));
	// }
	// return i;
	// }

	@Override
	public int copyEventInfoFromNurseRecord(NurseRecord nurseRecord,
			String deptCode) {
		throw new UnsupportedOperationException();
	}

	// /**
	// * 保存护理交班实体，如果不设置时间，则记录时间为当前时间
	// *
	// * @param nsEntity
	// * @return
	// */
	// public int saveNurseShiftRecord(NurseShiftEntity nsEntity) {
	// int i = CrudConstants.CODE_NULL_ARGUMENT;
	// if (nsEntity != null) {
	// // if (nsEntity.getRecordDate() == null) {
	// // nsEntity.setRecordDate(DateUtil.formatDateToString(new Date(),
	// // DateUtil.DATE_FORMAT_FULL));
	// // }
	// // i = nurseShiftRepository.insertNurseShiftRecord(nsEntity);
	// //
	// // // 返回插入后生成的id
	// // if (i > 0) {
	// // i = nsEntity.getRecordId();
	// // }
	// }
	//
	// return i;
	// }

	boolean validateEventInfo(PatientEvent eventInfo) {
		if (eventInfo != null) {
			return eventInfo.getPatientId() != null
					&& eventInfo.getRecorderId() != null;
		}
		return false;
	}


	// @Override
	// public PatientEventInfo getPatientEventInfoByPatId(String patId, String
	// day)
	// throws ParseException {
	// String[] dateRange = DateUtil.getTimeEndPoints(day);
	//
	// // return nurseShiftRepository.selectNurseShiftRecordForPatient(patId,
	// // dateRange[0], dateRange[1]);
	// return null;
	// }

	/**
	 * 判断交班本记录是否存在
	 * 
	 * @param shiftBrId
	 * @return
	 */
	private NurseShiftRecordEntity getShiftRecordById(String shiftRecordId) {
		NurseShiftRecordEntity nurseShiftRecordEntity = null;
		if (StringUtils.isBlank(shiftRecordId)) {
			throw new AlertException("参数不正确!");
		}
		// 判断是否存在
		nurseShiftRecordEntity = this.nurseShiftRepository
				.getShiftRecordById(shiftRecordId);
		if (nurseShiftRecordEntity == null) {
			LOGGER.debug("getShiftRecordById nurseShiftRecordEntity not exist");
			throw new AlertException("交班本记录不存在!");
		}
		return nurseShiftRecordEntity;
	}

	/**
	 * startDate或endDate为空获取病人当前时段和上一个时段的所有记录信息 ;否则获取指定时段的记录信息
	 * 
	 */
	@Override
	public List<NurseShiftRecordEntity> getNurseShiftRecordEntitiesByPatId(
			String patId, String startDate, String endDate) {
		List<NurseShiftRecordEntity> nurseShiftRecordEntities = new ArrayList<NurseShiftRecordEntity>();
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)
				&& !"null".equals(startDate) && !"null".equals(endDate)) {
			// 指定时段记录信息
			nurseShiftRecordEntities = this.nurseShiftRepository
					.getShiftRecordsByNurseShiftId(getNurseShiftId(patId,
							startDate, endDate));
		} else {
			// 当前时段
			String[] currenShiftTimeRange = this.calcLastShiftTimeRange(true);
			// 上一个时段
			String[] preShiftTimeRange = this.calcLastShiftTimeRange(false);

			List<NurseShiftRecordEntity> currentNurseShiftRecordEntities = this.nurseShiftRepository
					.getShiftRecordsByNurseShiftId(getNurseShiftId(patId,
							currenShiftTimeRange[0], currenShiftTimeRange[1]));

			List<NurseShiftRecordEntity> preNurseShiftRecordEntities = this.nurseShiftRepository
					.getShiftRecordsByNurseShiftId(getNurseShiftId(patId,
							preShiftTimeRange[0], preShiftTimeRange[1]));

			if (currentNurseShiftRecordEntities != null
					&& currentNurseShiftRecordEntities.size() > 0) {
				nurseShiftRecordEntities
						.addAll(currentNurseShiftRecordEntities);
			}

			if (preNurseShiftRecordEntities != null
					&& preNurseShiftRecordEntities.size() > 0) {
				nurseShiftRecordEntities.addAll(preNurseShiftRecordEntities);
			}
		}

		// 按时间排序
		Collections.sort(nurseShiftRecordEntities,
				new Comparator<NurseShiftRecordEntity>() {
					@Override
					public int compare(NurseShiftRecordEntity o1,
							NurseShiftRecordEntity o2) {
						return o1.getShiftRecordTime().compareTo(
								o2.getShiftRecordTime());
					}
				});
		return nurseShiftRecordEntities;
	}

}
