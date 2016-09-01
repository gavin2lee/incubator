package com.lachesis.mnis.core.bodysign;

import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.bodysign.entity.*;
import com.lachesis.mnis.core.bodysign.repository.BodySignRepository;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.event.DocReportEvent;
import com.lachesis.mnis.core.event.MnisEventPublisher;
import com.lachesis.mnis.core.event.entity.DocReportEventEntity;
import com.lachesis.mnis.core.event.entity.DocReportEventItemEntity;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientAllergy;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;
import com.lachesis.mnis.core.skintest.repository.SkinTestRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.NumberUtil;
import com.lachesis.mnis.core.util.SuperCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.*;

/***
 * 
 * 生命体征Facade接口
 *
 * @author yuliang.xu
 * @date 2015年6月4日 下午3:16:30
 *
 */
@Service("bodySignService")
public class BodySignServiceImpl implements BodySignService {
	static final Logger LOGGER = LoggerFactory
			.getLogger(BodySignServiceImpl.class);

	@Autowired
	private BodySignRepository bodySignRepository;

	@Autowired
	private PatientService patientService;

	@Autowired
	private BodySignDataProcessor dataProcessor;

	@Autowired
	private MnisEventPublisher mnisEventPublisher;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private SkinTestRepository skinTestRepository;

	/***
	 * 从数据库中初始化bodysign item dictionary
	 */
	@PostConstruct
	void init() {
		LOGGER.info("init bodysign item dictionary form db");
		List<BodySignDict> list = bodySignRepository.getBodySignDicts();
		if (list != null) {
			for (BodySignDict dict : list) {
				BODY_SIGN_DICT_MAP.put(dict.getItemCode(), dict);
			}
		}
	}

	@Override
	public List<BodySignDict> getBodySignDicts() {
		return new ArrayList<>(BODY_SIGN_DICT_MAP.values());
	}

	@Override
	public String getBodySignItemNameByCode(String itemCode) {
		BodySignDict dict = BODY_SIGN_DICT_MAP.get(itemCode);
		if (dict == null)
			return null;
		return dict.getItemName();
	}

	@Override
	public List<BodySignRecord> getBodySignRecord(Date date, String patientId,boolean isSpecialDay) {
		return this.getBodySignRecord(date, new String[] { patientId },isSpecialDay);
	}

	@Override
	public List<BodySignRecord> getBodySignRecord(Date date, String[] patientIds,boolean isSpecialDay) {
		List<BodySignRecord> bodySignRecords = null;
		//指定时间日期
		if(isSpecialDay){
			bodySignRecords = bodySignRepository
					.getBodySignRecordByPatIdAndDate(date, patientIds);
		}else{
			bodySignRecords = bodySignRepository
					.getBodySignRecord(date, patientIds);
			
		}
		
		if (bodySignRecords == null) {
			return null;
		}

		List<BodySignItem> removeEmptyItems = new ArrayList<BodySignItem>();
		List<BodySignItem> items = null;
		for (BodySignRecord bodySignRecord : bodySignRecords) {
			items = bodySignRecord.getBodySignItemList();
			if (items == null || items.size() == 0) {
				bodySignRecord.setBodySignItemList(null);
			}
			removeEmptyItems.clear();
			for (BodySignItem bodySignItem : items) {
				if(StringUtils.isBlank(bodySignItem.getItemCode())){
					removeEmptyItems.add(bodySignItem);
				}
			}
			//删除空的体征项目
			bodySignRecord.getBodySignItemList().removeAll(removeEmptyItems);
		}

		return bodySignRecords;
	}

	@Override
	@Transactional
	public void saveBodySignRecord(BodySignRecord record,boolean isCopy) {
		
		if(!record.hasData()){
			record.setFullDateTime(DateUtil.parse(record.getRecordDay() 
					+ MnisConstants.EMPTY + record.getRecordTime()));
			List<BodySignRecord> bodySignRecords = bodySignRepository.getBodySignRecordByRecordDate(
					DateUtil.format(record.getFullDateTime(),DateFormat.FULL),
					record.getPatientId(),record.getDeptCode());
			dataProcessor.handleDeletePatBodySignDetail(bodySignRecords);
		}else {
			//如果是第一次保存病人的体征记录，则需要自动生成入院事件
			createAdmissionEventIfNecessary(record);
		}
		
		dataProcessor.handleBodySign(record);
		//数据处理-明细合并
		dataProcessor.assertPersistRule(record,true);
		//数据删除
		bodySignRepository.delete(record);
		//生命体征item单位
		if(null != record.getBodySignItemList()){
			for(BodySignItem item : record.getBodySignItemList()){
				if(StringUtils.isEmpty(item.getUnit())&& BODY_SIGN_DICT_MAP.containsKey(item.getItemCode())){
					item.setUnit(BODY_SIGN_DICT_MAP.get(item.getItemCode()).getItemUnit());
				}
			}
		}
		
		if (record.hasData()) {
			bodySignRepository.save(record);
			// 皮试结果为阳性，同步
			if (record.getSkinTestInfo() != null
					&& MnisConstants.SKIN_TEST_POSITIVE.equals(record.getSkinTestInfo().getTestResult()
							.toLowerCase())) {
				syncSkinTestToAllergy(record.getSkinTestInfo());
				skinTestRepository.updateAllergy(record.getPatientId(), record.getSkinTestInfo().getDrugCode());
			}
			//修改过敏信息
			skinTestRepository.deleteAllergy(record.getPatientId());

			// 转抄到护理单据
			if (identityService.isSyncDocReport() && isCopy && record.hasBodySignItems()) {
				copyBodySignItemToDocReport(record,true);
			}
		}
	}

	/**
	 * 必要的情况下，自动生成入院事件
	 * @param record 体征记录
     */
    private void createAdmissionEventIfNecessary(BodySignRecord record) {
		if(null == record || StringUtils.isEmpty(record.getPatientId())){
			return;
		}
		//获取患者名下已有的体征记录数目
		Integer recordsCnt = bodySignRepository.getBodySignRecordCountForPatient(record.getPatientId());
		if(0>=recordsCnt){
            //患者名下还没有体征记录，说明是第一次录入，需要自动生成入院事件
            PatientEvent admissionEvent = createEventForAdmission(record);
            //NOTE:如果当前记录中已有事件怎么办？
            //感觉这个地方好像设计有点问题，一个体征录入时段内只能记录一条事件？
            record.setEvent(admissionEvent);
        }
	}

	/**
	 * 根据记录信息生成对应的入院事件
	 * @param record 体征记录
	 * @param inDate 入院时间
     * @return 生成的入院事件
     */
    private PatientEvent createEventForAdmission(BodySignRecord record){
		if(null==record || StringUtils.isEmpty(record.getPatientId())){
			return null;
		}
		Patient patient = patientService.getPatientByPatId(record.getPatientId());
		if (patient == null) {
			return null;
		}
		PatientEvent event = new PatientEvent();
		event.setPatientId(record.getPatientId());
		event.setPatientName(patient.getName());
		event.setEventCode(BodySignConstants.BODYSIGN_ITEM_RY);
		event.setEventName(BodySignConstants.MAP_BODYSIGN_ITEM.get(BodySignConstants.BODYSIGN_ITEM_RY));
		event.setRecordDate(DateUtil.format(patient.getInDate(), DateFormat.HM));
		event.setChineseEventDate(DateUtil.getChineseHourMinute(event.getRecordDate()));
		event.setRecorderId(record.getRecordNurseCode());
		event.setRecorderName(record.getRecordNurseName());
		event.setIndex(0);//入院事件作为第一个事件
		event.setRecord_date(record.getFullDateTime());
		return event;
	}

	/**
	 * 将文书转抄过来的体征记录保存到备份表中
	 * @param record
     */
    private void saveBodySignItemBackup(BodySignRecord record) {
		if(!record.hasData()){
			return;
		}
		dataProcessor.handleBodySign(record);
		//数据处理-明细合并
		//dataProcessor.assertPersistRule(record,true);
		//数据删除
		bodySignRepository.deleteBodySignItemBackup(record);
		//生命体征item单位
		if(null != record.getBodySignItemList()){
			for(BodySignItem item : record.getBodySignItemList()){
				if(StringUtils.isEmpty(item.getUnit())&& BODY_SIGN_DICT_MAP.containsKey(item.getItemCode())){
					item.setUnit(BODY_SIGN_DICT_MAP.get(item.getItemCode()).getItemUnit());
				}
			}
		}

		bodySignRepository.saveBodySignItemBackup(record);
	}

	/**
	 * 生命体征子项转抄到护理文书
	 * 
	 * @param record
	 */
	private void copyBodySignItemToDocReport(BodySignRecord record,boolean isSave) {
		DocReportEventEntity docReportEventEntity = new DocReportEventEntity();
		docReportEventEntity.setCreate_person(record.getRecordNurseCode()
				+ MnisConstants.LINE + record.getRecordNurseName());
		Date date = null;
		String recordTime = DateUtil.format(record.getFullDateTime(), DateFormat.HMS);
		if(MnisConstants.ZERO_HMS_TIME.equals(recordTime)){
			date = record.getModifyTime();
			if(null == date){
				date = record.getFirstDate();
			}
		}else{
			date = record.getFullDateTime();
		}
		//零点项目设置事实时间,其他项目设置录入时间
		docReportEventEntity.setCreateTime(date);
		docReportEventEntity.setDate_list(DateUtil.format(date, DateFormat.YMD));
		docReportEventEntity.setTime_list(DateUtil.format(date, DateFormat.HMS));
		docReportEventEntity.setInpatient_no(record.getPatientId());
		docReportEventEntity.setDept_code(record.getDeptCode());
		List<DocReportEventItemEntity> docReportEventItemEntities = new ArrayList<DocReportEventItemEntity>();
		for (BodySignItem bodySignItem : record.getBodySignItemList()) {
			String itemCode = bodySignItem.getItemCode();
			//需要转抄项
			if (itemCode == null
					|| !BodySignConstants.DOC_REPORT_BODYSIGN_ITEM
							.contains(itemCode)) {
				continue;
			}
			
			//已添加项或非数字不转抄
			if(bodySignItem.isAdd() || !NumberUtil.isNumeric(bodySignItem.getItemValue())){
				continue;
			}
			// 出量
			if (itemCode.equals(BodySignConstants.URINE)) {
				docReportEventItemEntities.add(new DocReportEventItemEntity(
						MnisConstants.COPY_DOC_OUT_NAME, itemCode));
				docReportEventItemEntities.add(new DocReportEventItemEntity(
						MnisConstants.COPY_DOC_OUT_TAKE, bodySignItem.getItemValue()));
			} else if (itemCode.equals(BodySignConstants.OTHEROUTPUT)) {
				if (StringUtils.isBlank(bodySignItem.getMeasureNoteCode())
						|| BodySignConstants.BODYSIGN_ITEM_MOREN.equals(itemCode)) {
					continue;
				}
				//其他出入量(传小项目)
				itemCode = bodySignItem.getMeasureNoteCode();
				
				docReportEventItemEntities.add(new DocReportEventItemEntity(
						MnisConstants.COPY_DOC_OUT_NAME, itemCode));
				docReportEventItemEntities.add(new DocReportEventItemEntity(
						MnisConstants.COPY_DOC_OUT_TAKE, bodySignItem.getItemValue()));
				
			}else {
				// 其他
				docReportEventItemEntities.add(new DocReportEventItemEntity(
						itemCode, bodySignItem.getItemValue()));
			}

		}
		docReportEventEntity.setData_item(docReportEventItemEntities);
		if(!docReportEventItemEntities.isEmpty()){
			// 发布护理文书转抄事件
			mnisEventPublisher.publish(new DocReportEvent(this,
					docReportEventEntity));
		}
	}

	@Override
	@Transactional
	public void deleteBodySignRecord(int id) {
		bodySignRepository.delete(id);
	}

	@Override
	@Transactional
	public void updateBodySignRecord(BodySignRecord record,boolean isCopy) {
		if(!record.hasData()){
			//无数据时,删除该条记录
			record.setFullDateTime(DateUtil.parse(record.getRecordDay() 
					+ MnisConstants.EMPTY + record.getRecordTime()));
			List<BodySignRecord> bodySignRecords = bodySignRepository.getBodySignRecordByRecordDate(
					DateUtil.format(record.getFullDateTime(),DateFormat.FULL),
					record.getPatientId(),record.getDeptCode());
			dataProcessor.handleDeletePatBodySignDetail(bodySignRecords);
			bodySignRepository.delete(record);
			return;
		}
		dataProcessor.handleBodySign(record);
		// 增加规则限制,判断?  end by qingzhi.liu   修改入院事件，需要添加规则判断 2015.12.03
		dataProcessor.assertPersistRule(record,false);
		bodySignRepository.delete(record);
		//生命体征item单位
		if(null != record.getBodySignItemList()){
			for(BodySignItem item : record.getBodySignItemList()){
				if(StringUtils.isEmpty(item.getUnit())&& BODY_SIGN_DICT_MAP.containsKey(item.getItemCode())){
					item.setUnit(BODY_SIGN_DICT_MAP.get(item.getItemCode()).getItemUnit());
				}
			}
		}
		if (record.hasData()) {
			
			bodySignRepository.save(record);
			// 皮试结果为阳性，同步
			if (record.getSkinTestInfo() != null
					&& MnisConstants.SKIN_TEST_POSITIVE.equals(record.getSkinTestInfo().getTestResult()
							.toLowerCase())) {
				syncSkinTestToAllergy(record.getSkinTestInfo());
				skinTestRepository.updateAllergy(record.getPatientId(), record.getSkinTestInfo().getDrugCode());
			}
			//修改过敏信息
			skinTestRepository.deleteAllergy(record.getPatientId());
			
			// 转抄到护理单据
			if (identityService.isSyncDocReport() && isCopy && record.hasBodySignItems()) {
				copyBodySignItemToDocReport(record,false);
			}
		}
	}

	/**
	 * 皮试模块同步保存体征
	 */
	@Override
	public void saveSkinTestInfoToBodySign(PatientSkinTest test) {
		if (test == null) {
			throw new IllegalArgumentException("skin test is null");
		}

		Patient patient = patientService.getPatientByPatId(test.getPatientId());
		if (patient == null) {
			throw new IllegalArgumentException("patient is null");
		}
		//1.判断零点的生命体征记录是否存在
		List<BodySignRecord> bodySignRecords = bodySignRepository.getBodySignRecordByRecordDate(
					DateUtil.format(test.getExecDate(),DateFormat.YMD), test.getPatientId(),patient.getDeptCode());
		BodySignRecord record = null;
		if(null != bodySignRecords && bodySignRecords.size() > 0){
			record = bodySignRecords.get(0);
		}else{
			record = new BodySignRecord();
			record.setPatientId(test.getPatientId());
			record.setPatientName(test.getPatientName());
			record.setBedCode(patient.getBedCode());
			record.setDeptCode(patient.getDeptCode());
			record.setInHospNo(patient.getInHospNo());
			record.setRecordNurseCode(test.getTestNurseId());
			record.setRecordNurseName(test.getTestNurseName());

			record.setSkinTestInfo(test);
			Date recordDate = DateUtil.setDateToDay(test.getExecDate());
			record.setRecordDay(DateUtil.format(recordDate,DateFormat.YMD));
			record.setRecordTime(DateUtil.format(recordDate,DateFormat.HMS));
			record.setFullDateTime(recordDate);
			
			record.setModifyTime(test.getExecDate());
			
			bodySignRepository.saveBodySignMaster(record);
			
		}

		// 2.修改皮试的体征记录
		bodySignRepository.updateSkinTestMasterRecordIdByOrderId(
				record.getMasterRecordId(), test.getOrderExecId());
		// 3.同步到过敏信息
		if(MnisConstants.SKIN_TEST_POSITIVE.equals(test.getTestResult().toLowerCase())){
			syncSkinTestToAllergy(test);
		}
	}

	/**
	 * 同步过敏信息到患者过敏
	 * 
	 * @param patId
	 * @param allergy
	 */
	private void syncSkinTestToAllergy(PatientSkinTest patientSkinTest) {

		if (null == patientSkinTest
				|| StringUtils.isEmpty(patientSkinTest.getDrugName()))
			return;
		PatientAllergy allergy = new PatientAllergy();
		allergy.setPatientId(patientSkinTest.getPatientId());
		allergy.setDrugCode(patientSkinTest.getDrugCode());
		allergy.setDrugName(patientSkinTest.getDrugName());
		allergy.setSkinTestId(new Long(patientSkinTest.getId()));

		allergy.setAllergyDate(patientSkinTest.getExecDate() == null ? new Date()
				: patientSkinTest.getExecDate());
		// 存在的过敏药
		PatientAllergy existAllergy = patientService
				.getAllergyByPatIdAndDrugName(allergy.getPatientId(),
						allergy.getDrugName());
		// 已存在,不添加
		if (existAllergy != null) {
			return;
		}

		// 更新患者信息的过敏信息
		List<PatientAllergy> patientAllergies = new ArrayList<PatientAllergy>();
		patientAllergies.add(allergy);

		patientService.saveAllergyRecord(patientAllergies);
	}
	
	/**
	 * 同步过敏信息到患者过敏
	 * 
	 * @param patId
	 * @param allergy
	 */
	private void updateSkinTestToAllergy(PatientSkinTest patientSkinTest) {

		if (null == patientSkinTest
				|| StringUtils.isEmpty(patientSkinTest.getDrugName()))
			return;
		PatientAllergy allergy = new PatientAllergy();
		allergy.setPatientId(patientSkinTest.getPatientId());
		allergy.setDrugCode(patientSkinTest.getDrugCode());
		allergy.setDrugName(patientSkinTest.getDrugName());
		allergy.setSkinTestId(new Long(patientSkinTest.getId()));

		allergy.setAllergyDate(patientSkinTest.getExecDate() == null ? new Date()
				: patientSkinTest.getExecDate());
		// 存在的过敏药
		PatientAllergy existAllergy = patientService
				.getAllergyByPatIdAndDrugName(allergy.getPatientId(),
						allergy.getDrugName());
		// 已存在,不添加
		if (existAllergy != null) {
			return;
		}

		// 更新患者信息的过敏信息
		List<PatientAllergy> patientAllergies = new ArrayList<PatientAllergy>();
		patientAllergies.add(allergy);

		patientService.saveAllergyRecord(patientAllergies);
	}

	@Override
	public void batchBodySignRecord(List<BodySignRecord> bodySignRecords,
			boolean isSave, UserInformation userInformation,boolean isCopy) {
		if (bodySignRecords == null || bodySignRecords.isEmpty()) {
			return;
		}

		for (BodySignRecord record : bodySignRecords) {
			if(null == record){
				continue;
			}
			if(StringUtils.isEmpty(record.getRecordDay())){
				throw new MnisException("记录日期不允许为空!");
			}

			if(record.hasData()){
				updateBodySignRecordUser(record, userInformation);
			}

			// try{
			if (isSave) {
				saveBodySignRecord(record,isCopy);
			} else {
				updateBodySignRecord(record,isCopy);
			}
			// }catch(Exception e){
			// LOGGER.error("", e);
			// }
		}
	}

	public void updateBodySignRecordUser(BodySignRecord record,
			UserInformation user) {
		

		/*record.setModifyTime(new Date()); // 最后一次操作时间 end by Qingzhi.Liu
											// 2015-09-02 深圳三院
*/		Patient patient = patientService.getPatientByPatId(record
				.getPatientId());
		record.setBedCode(patient.getBedCode());
		record.setPatientName(patient.getName());
		record.setInHospNo(patient.getInHospNo());
		record.setDeptCode(patient.getDeptCode());
		
		if(null != user){
			if(record.getFirstDate()==null){
				record.setRecordNurseCode(user.getCode());
				record.setRecordNurseName(user.getName());
			}else{
				record.setModifyNurseCode(user.getCode());
				record.setModifyNurseName(user.getName());
			}
			if (record.getEvent() != null) {
				record.getEvent().setRecorderId(user.getCode());
				record.getEvent().setRecorderName(user.getName());
			}
			if (record.getSkinTestInfo() != null) {
				record.getSkinTestInfo().setTestNurseId(user.getCode());
				record.getSkinTestInfo().setTestNurseName(user.getName());
			}
			
		}
		
	}

	@Override
	public BodyTempSheet getBodyTempSheet(Date day, Patient patient,
			int weekOffset) {
		if (patient.getInDate() == null) {
			throw new IllegalArgumentException("病人入院日期不能为空！");
		}

		BodyTempSheet bodyTempSheet = new BodyTempSheet(patient, 0);

		//入院日期
		Date inHospDate = DateUtil.setDateToDay(patient.getInDate());
		// day : yyyy-MM-dd
		Date refDate = dataProcessor.getRefDate(day, inHospDate,
				patient.getOutDate());

		// 获取指定日期对应周的所有日期
		// 规则：入院当天必须为所计算周的第一天，必须包含星期天且是完整的7天
		Date[] recordWeekDays = dataProcessor.getWeekDaysAroundDate(refDate,
				weekOffset, inHospDate);
		if (recordWeekDays == null) {
			throw new IllegalArgumentException("病人体温单日期不能为空！");
		}

		// 所有日期对应的入院天数
		List<String> daysAfterAdmission = dataProcessor
				.calcDaysAfterByInAndOutDay(inHospDate, patient.getOutDate(),
						recordWeekDays);
		if (daysAfterAdmission == null || daysAfterAdmission.isEmpty()) {
			throw new IllegalArgumentException("病人体温单日期天数不能为空！");
		}

		// 获取手术日期: 若在14天内进行第2次手术，则将第1次手术天数作为分母，第2次手术天数作为分子填写。
		List<String> daysAfterSurgery = null;
		List<Date> surgeryDate = this.getSurgeryDate(patient.getPatId(),
				BodySignConstants.BODYSIGN_ITEM_SS,BodySignConstants.BODYSIGN_ITEM_FM, recordWeekDays);
		if (surgeryDate != null && surgeryDate.size() > 0) {
			switch (MnisConstants.BODY_SIGN_EVENT_RULE) {
			case 1:
				daysAfterSurgery = dataProcessor.calcDaysAfterEventRuleOne(
						surgeryDate, patient.getOutDate(), recordWeekDays);
				break;

			case 2:
				daysAfterSurgery = dataProcessor.calcDaysAfterEventRuleTwo(
						surgeryDate, patient.getOutDate(), recordWeekDays);
				break;
			}
		}

		// TODO 转科信息

		boolean existSurgery = daysAfterSurgery != null
				&& daysAfterSurgery.size() == recordWeekDays.length;
		BodySignDailyRecord bodySignDailyRecord = null;
		List<BodySignRecord> bodySignRecords = null;
		boolean isInHospArea = false; //是否在入院时间区域
		//入院到今天是否有入院事件
		int eventCount = bodySignRepository.getRyEventCount(patient.getPatId(), BodySignConstants.BODYSIGN_ITEM_RY, 
				DateUtil.setDateToDay(inHospDate), DateUtil.setNextDayToDate(new Date()));
		boolean isNeedRyEvent = false; //是否有入院事件
		if(Arrays.asList(recordWeekDays).contains(inHospDate)){
			isInHospArea = true;
		}
		
		if(eventCount == 0){
			isNeedRyEvent = true;
		}
		// 获取记录日期对应的生命体征每日记录
		for (int i = 0; i < recordWeekDays.length; i++) {
			
			bodySignDailyRecord = new BodySignDailyRecord();
			// 住院第几天
			bodySignDailyRecord.setDaysInHospital(daysAfterAdmission.get(i));
			// 手术第几天
			if (existSurgery) {
				bodySignDailyRecord
						.setDaysAfterSurgery(daysAfterSurgery.get(i));
			}

			// 用户不一定手术
			// 获取并处理一天的生命体征记录列表
			// TODO 需要根据BodySignTimePointStrategy.THREE,
			// BodySignIndexStrategy.BEFORE 2个策略来进行数据查询
			bodySignRecords = this.getBodySignRecord(recordWeekDays[i],
					patient.getPatId(),false);
			
			if (!isInHospArea && !isNeedRyEvent && bodySignRecords.size() == 0) {
				bodySignDailyRecord.setBodySignRecordList(bodySignRecords);
				bodyTempSheet.addToBodySignDailyRecordList(bodySignDailyRecord);
				continue;
			}
			

			//默认入院事件
			if(isInHospArea && isNeedRyEvent && i == 0){
				for (BodySignRecord bodySignRecord : bodySignRecords) {
					if(bodySignRecord.getEvent() == null){
						continue;
					}
					if(bodySignRecord.getEvent().getId() > 0){
						isNeedRyEvent = false;
						break;
					}
				}
				
			}
			//入院事件区域和需要入院事件
			if(i == 0 && isInHospArea && isNeedRyEvent){
				//去除自动生成入院事件
				//bodySignRecords = setRyEventToBodySignRecords(bodySignRecords, patient);
			}
			// 删除无效记录
			dataProcessor.normalizeBodySignRecord(bodySignRecords);
			
			dataProcessor.processBloodPressToRecord(bodySignRecords);

			dataProcessor.processTempSheetDataForDay(bodySignRecords, i,
					recordWeekDays[i]);
			
			
			bodySignDailyRecord.setBodySignRecordList(bodySignRecords);

			bodyTempSheet.addToBodySignDailyRecordList(bodySignDailyRecord);
		}
		
		// 设置基本属性
		dataProcessor.setCommonProperties(bodyTempSheet, recordWeekDays,
				daysAfterAdmission);
		return bodyTempSheet;
	}

	/**
	 * 获取手术日期: 若在14天内进行第2次手术，则将第1次手术天数作为分母，第2次手术天数作为分子填写。
	 * 
	 * @param patId
	 * @param eventCode
	 * @return
	 */
	private List<Date> getSurgeryDate(String patId, String eventCode,String fmEvent,
			Date[] weekLastDay) {
		List<PatientEvent> list = bodySignRepository
				.getEventInfosByPatIdAndCode(patId, eventCode,null);
		list.addAll(bodySignRepository
				.getEventInfosByPatIdAndCode(patId, fmEvent,null));
		if (list == null || list.size() == 0) {
			return null;
		}

		// date[0] 上一次手术事件
		// date[1] 最新一次手术事件
		List<Date> date = new ArrayList<>();
		/*
		 * PatientEvent event = null; for (BodySignRecord record : list) { event
		 * = record.getEvent(); if(event == null || !event.isValid()){ continue;
		 * }
		 * 
		 * if(DateUtils.isSameDay(record.getFullDateTime(),
		 * weekLastDay[weekLastDay.length - 1]) ||
		 * DateUtils.isSameDay(record.getFullDateTime(), weekLastDay[0])){
		 * 
		 * }
		 * 
		 * }
		 */

		// 只有一个手术事件，直接输出
		for (PatientEvent event : list) {
			date.add(DateUtil.setDateToDay(event.getRecord_date()));
		}

		return date;
	}

	@Override
	public List<BodySignSpeedy> getBodySignSpeedy(String deptCode,
			String nurseCode) {
		List<BodySignSpeedy> list = null;
		List<Patient> patientList = null;
		if (StringUtils.isNotBlank(deptCode)) {
			patientList = patientService.getWardPatientList(2, deptCode);
		} else {
			patientList = patientService.getWardPatientList(0, nurseCode);
		}

		if (patientList == null || patientList.size() == 0) {
			return list;
		}

		list = new ArrayList<BodySignSpeedy>(patientList.size());
		BodySignSpeedy bodySignSpeedy = null;
		for (Patient patient : patientList) {
			
			if(!StringUtils.isNotBlank(patient.getBedCode())){//没有分配床位号，不录入生命体征  end by Qingzhi.Liu 2015.10.10
				continue;
			}
			bodySignSpeedy = new BodySignSpeedy();
			bodySignSpeedy.setPatientId(patient.getPatId());
			bodySignSpeedy.setPatientName(patient.getName());
			bodySignSpeedy.setBedNo(patient.getBedCode());
			bodySignSpeedy.setInHospitalNo(patient.getInHospNo());
			bodySignSpeedy.setTendLevel(patient.getTendLevel());
			bodySignSpeedy.setDaysInHospital(DateUtil.calDatePoor(
					DateUtil.setDateToDay(patient.getInDate()),
					DateUtil.setDateToDay(new Date())) + 1);

			BodySignRecord reocrd = bodySignRepository.getLastTemperatureItem(
					patient.getPatId(), BodySignConstants.TEMPERATURE);
			if (reocrd != null && reocrd.getFullDateTime() != null
					&& reocrd.getFullDateTime().after(new Date())) {
				bodySignSpeedy.setLastMsmentTime(reocrd.getFullDateTime());
			}

			list.add(bodySignSpeedy);
		}

		return list;
	}

	@Override
	public List<SpecialVo> getFirstBodySigns(String pat_id) {
		List<SpecialVo> list = null;
		if (pat_id != null) {
			list = bodySignRepository.getFirstBodySigns(pat_id);
		}
		return list;
	}

	@Override
	public List<BodySignItem> getBodySignItemByCode(String recordDate,
			String itemCode, String deptCode, String patId, Date startDate,
			Date endDate) {
		if (StringUtils.isBlank(recordDate) || StringUtils.isBlank(itemCode)) {
			LOGGER.debug("BodySignServiceImpl ->getBodySignItemByCode"
					+ "参数传入为null!");
			return null;
		}

		return bodySignRepository.getBodySignItemByCode(recordDate, itemCode,
				deptCode, patId, startDate, endDate);
	}

	@Override
	public List<BodySignRecord> getBodySignRecordByRecordDate(
			String recordDate, String patId, String deptCode) {
		if (StringUtils.isBlank(recordDate)) {
			LOGGER.debug("BodySignServiceImpl ->getBodySignRecordByRecordDate"
					+ " recordDate参数传入为null!");
			return null;
		}
		// 根据时间点查询体征数据
		List<BodySignRecord> bodySignRecords = bodySignRepository
				.getBodySignRecordByRecordDate(recordDate, patId, deptCode);
		// 定义一天一次的项目
		List<BodySignRecord> oncePerBodySignRecords = new ArrayList<BodySignRecord>();
		String dateString = recordDate.substring(0, 10);
		// 查询时间不为零点时
		if (DateUtil.parse(recordDate) != DateUtil.parse(dateString)) {
			// 获取零点的项目
			oncePerBodySignRecords = bodySignRepository
					.getBodySignRecordByRecordDate(dateString, patId, deptCode);

			if (bodySignRecords.size() == 0) {
				bodySignRecords.addAll(oncePerBodySignRecords);
			} else {

				for (BodySignRecord oncePerBodySignRecord : oncePerBodySignRecords) {
					// 将零点的项目加载到指定时间点查询的记录
					bodySignRecords.get(0).addToBodySignItems(
							oncePerBodySignRecord.getBodySignItemList());
					// 将皮试加载到指定时间记录
					if (null == bodySignRecords.get(0).getSkinTestInfo()) {
						bodySignRecords.get(0).setSkinTestInfo(
								oncePerBodySignRecord.getSkinTestInfo());
					}
				}
			}
		}
		//去除item空的项目
		List<BodySignItem> removeEmptyItems = new ArrayList<BodySignItem>();
		List<BodySignItem> items = null;
		for (BodySignRecord bodySignRecord : bodySignRecords) {
			items = bodySignRecord.getBodySignItemList();
			if (items == null || items.size() == 0) {
				bodySignRecord.setBodySignItemList(null);
			}
			removeEmptyItems.clear();
			for (BodySignItem bodySignItem : items) {
				if(StringUtils.isBlank(bodySignItem.getItemCode())){
					removeEmptyItems.add(bodySignItem);
				}
			}
			//删除空的体征项目
			bodySignRecord.getBodySignItemList().removeAll(removeEmptyItems);
		}
		return bodySignRecords;
	}
	
	/**
	 * 设置默认入院事件
	 * @param bodySignRecords
	 * @param patient
	 * @return
	 */
	private List<BodySignRecord> setRyEventToBodySignRecords(
			List<BodySignRecord> bodySignRecords, Patient patient) {
		BodySignRecord bodySignRecord = null;
		if (bodySignRecords.size() == 0) {
			bodySignRecord = new BodySignRecord();
		} else {
			bodySignRecord = bodySignRecords.get(0);
		}

		
		// 中文入院时间(hh:mm)
		String hourType = identityService
				.getConfigure(MnisConstants.SYS_CONFIG_HOURTYPE);
		boolean bool = (!StringUtils.isEmpty(hourType) && "1".equals(hourType)) ? true
				: false;
		String inHospTime = DateUtil.getChineseHourMinute(patient.getInDate(),
				bool);
		PatientEvent event = new PatientEvent();
		event.setChineseEventDate(inHospTime);
		event.setEventCode(BodySignConstants.BODYSIGN_ITEM_RY);
		event.setEventName(BodySignConstants.MAP_BODYSIGN_ITEM
				.get(BodySignConstants.BODYSIGN_ITEM_RY));
		// 设置区域(当前时间后4个小时)
		List<String> bodySignDataStrategys = identityService.getBodySignDataStrategy();
		bodySignDataStrategys.remove(1);
		bodySignDataStrategys.add(1,"2");
		List<Date> hours = BodySignUtil.getTimePointsByHour(
				patient.getInDate(), bodySignDataStrategys);
		event.setIndex(BodySignUtil.getIndexByDivideHour(patient.getInDate(),
				hours, 6));

		bodySignRecord.setEvent(event);
		if (bodySignRecords.size() > 0) {
			bodySignRecords.remove(0);
		}

		bodySignRecords.add(0, bodySignRecord);
		
		return bodySignRecords;
	}
	
	@Transactional
	@Override
	public void batchSaveFixTimeBodySignRecords(List<BodySignRecord> bodySignRecords,UserInformation userInformation) {
		for (BodySignRecord bodySignRecord : bodySignRecords) {
			saveFixTimeBodySignRecord(bodySignRecord,userInformation);
		}
	}

	@Override
	public List<BodySignRecordVo> getBodySignRecordVosToSheet(String patId,
			String date) {
		List<BodySignRecordVo> bodySignRecordVos = new ArrayList<BodySignRecordVo>();
		List<BodySignRecord> bodySignRecords = new ArrayList<BodySignRecord>();
		String timesString = MnisConstants.ZERO_HM_TIME + MnisConstants.COMMA + identityService
				.getConfigure("tempInputTimeSelector");
		List<String> recordNameList = new ArrayList<String>();
		Collections.addAll(recordNameList, timesString.split(
						MnisConstants.COMMA));
		if (StringUtils.isBlank(date)) {
			date = DateUtil.format(new Date(), DateFormat.YMD);
		}
		Date queryDate = null;
		//填充空的体征数据
		BodySignRecord emptyBodySignRecord = new BodySignRecord();
		emptyBodySignRecord.setBodySignItemList(new ArrayList<BodySignItem>());
		for (int i = 0; i < recordNameList.size(); i++) {
			BodySignRecordVo bodySignRecordVo = new BodySignRecordVo();
			bodySignRecords.clear();
			// 时间点
			queryDate = DateUtil.parse(date + MnisConstants.EMPTY
					+ recordNameList.get(i));
			bodySignRecordVo.setRecordName(recordNameList.get(i));
			bodySignRecordVo.setTimeId(DateUtil.getFixTypeFromTime(recordNameList.get(i), Calendar.HOUR_OF_DAY));
			// 根据指定时间点获取生命体征数据
			bodySignRecords = getBodySignRecord(queryDate, patId, true);
//			bodySignRecordVo
//					.setRecordCode(BodySignConstants.BODY_SIGN_RECORD_CODE
//							.get(i));

			if (null == bodySignRecords || bodySignRecords.size() == 0) {
				bodySignRecordVo.setBodySignRecord(emptyBodySignRecord);
			} else {
				bodySignRecordVo.setBodySignRecord(bodySignRecords.get(0));
			}
			//bug, 打个补丁，处理皮试信息的显示
			//如果皮试信息是从DNA端录入的，则需要调整其drugCode和drugName信息，以适应前端显示
			if(MnisConstants.ZERO_HM_TIME.equals(recordNameList.get(i))
					&& null!=bodySignRecordVo.getBodySignRecord().getSkinTestInfo()
					&& !StringUtils.isEmpty(bodySignRecordVo.getBodySignRecord().getSkinTestInfo().getMasterRecordId())){
				processSkinTestInfoForTemperatureSheet(bodySignRecordVo.getBodySignRecord().getSkinTestInfo());
			}
			bodySignRecordVos.add(bodySignRecordVo);
		}

		return bodySignRecordVos;
	}

	/**
	 * 处理皮试信息，以方便前端进行显示
	 * @param testInfo 皮试具体信息
     */
    private void processSkinTestInfoForTemperatureSheet(PatientSkinTest testInfo){
		if(null == testInfo || StringUtils.isEmpty(testInfo.getMasterRecordId())
				||null==testInfo.getDrugCode() || null==testInfo.getDrugName()){
			return;
		}

		//如果drugCode不符合前端约定，则根据drugName获取DrugCode
		if (!BodySignConstants.SKINTEST_DRUG_DIC.containsValue(testInfo.getDrugCode())){
			boolean bShoot = false;
			for (String drugName : BodySignConstants.SKINTEST_DRUG_DIC.keySet()) {
				//皮试录入的实际药物名称一般会比约定的drugName长，姑且使用包含语句判定，并不精准。
				if(testInfo.getDrugName().contains(drugName)){
					testInfo.setDrugCode(BodySignConstants.SKINTEST_DRUG_DIC.get(drugName));
					bShoot = true;
					break;
				}
			}
			if(!bShoot){
				testInfo.setDrugCode(BodySignConstants.SKINTEST_DRUGCODE_DEFAULT);
			}
		}
	}

	@Override
	public void saveFixTimeBodySignRecord(BodySignRecord bodySignRecord,UserInformation userInformation) {
		//获取预处理的生命体征
		List<BodySignRecord> bodySignRecords = dataProcessor.assertFixTimeBodySignRecords(bodySignRecord);
		batchBodySignRecord(bodySignRecords,false,userInformation,true);
	}

	@Override
	public List<BodySignRecord> getSingleFixTimeBodySignRecords(String[] patIds,
			Date date, boolean isSpecialDay) {
		List<BodySignRecord> records = new ArrayList<BodySignRecord>();
		//获取指定时间点生命体征
		List<BodySignRecord> bodySignRecords = getBodySignRecord(date, patIds, isSpecialDay);
		//获取零点数据
		Date zeroQueryDate = DateUtil.setDateToDay(date);
		List<BodySignRecord> zeroBodySignRecords = getBodySignRecord(zeroQueryDate, patIds, isSpecialDay);
		
		//指定点记录为空
		if(bodySignRecords.isEmpty()){
			records = zeroBodySignRecords;
		//零点记录为空
		}else if(zeroBodySignRecords.isEmpty()){
			records = bodySignRecords;
		}else{
			//已添加的一天一次记录
			List<BodySignRecord> addedBodySignRecords = new ArrayList<BodySignRecord>();
			for (BodySignRecord bodySignRecord : bodySignRecords) {
				for (BodySignRecord zeroBodySignRecord : zeroBodySignRecords) {
					if(zeroBodySignRecord.getPatientId().equals(bodySignRecord.getPatientId())){
						//记录加入一天一次项目
						bodySignRecord.addToBodySignItems(zeroBodySignRecord.getBodySignItemList());
						if(zeroBodySignRecord.getSkinTestInfo() != null
								&& StringUtils.isNotBlank(zeroBodySignRecord.getSkinTestInfo().getDrugCode())){
							bodySignRecord.setSkinTestInfo(zeroBodySignRecord.getSkinTestInfo());
						}
						//已添加记录--添加记录
						addedBodySignRecords.add(zeroBodySignRecord);
						break;
					}
				}
			}
			
			records.addAll(bodySignRecords);
			//删除已添加的一天一次项目
			zeroBodySignRecords.removeAll(addedBodySignRecords);
			records.addAll(zeroBodySignRecords);
		}
		return records;
	}

	@Override
	public void delete(BodySignRecord bodySignRecord) {
		if(null == bodySignRecord ||
				StringUtils.isBlank(bodySignRecord.getPatientId())
				|| null == bodySignRecord.getFullDateTime()){
			return;
		}
		
		bodySignRepository.delete(bodySignRecord);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void deletePartByTime(String patId,String recordDate){
		Map<String,List<String>> itemMap = getItemCodeForBatch();
		//一天多次的项目
		List<String> moreItems = itemMap.get("six");
		//一天一次的项目
		List<String> oneItems = itemMap.get("one");
		//一天两次的项目
		List<String> twoItems = itemMap.get("two");
		//一天三次的项目
		List<String> threeItems = itemMap.get("three");
		moreItems.addAll(twoItems);
		twoItems.clear();
		moreItems.addAll(threeItems);
		threeItems.clear();

		//获取记录时间点
		Map<String,List<String>> recordMap = BodySignUtil.getAllDateByrecordDate(recordDate);
		//删除一天六次的项目
		if(!moreItems.isEmpty()){
			List<String> recordDates= new ArrayList<String>();
			recordDates.add(recordDate);//时间
			bodySignRepository.deleteBodysingItemByTime(patId, recordDates,moreItems);
		}
		//删除一天三次的项目
		if(!threeItems.isEmpty()){
			List<String> recordDates = recordMap.get("threes");
			bodySignRepository.deleteBodysingItemByTime(patId, recordDates,threeItems);
		}
		//删除一天两次的项目
		if(!twoItems.isEmpty()){
			List<String> recordDates = recordMap.get("twos");
			bodySignRepository.deleteBodysingItemByTime(patId, recordDates,twoItems);
		}
		//删除一天一次的项目
		if(!oneItems.isEmpty()){
			List<String> recordDates = recordMap.get("ones");
			bodySignRepository.deleteBodysingItemByTime(patId, recordDates,oneItems);
		}
	}
	
	/**
	 * 获取批量体征的项目
	 * @return
	 */
	private Map<String,List<String>> getItemCodeForBatch(){
		String item = identityService.getConfigure(BodySignConstants.BATCH_BODYSIGN_ITEM);
		
		//一天多次的项目
		List<String> moreItems = new ArrayList<String>();
		//一天一次的项目
		List<String> oneItems = new ArrayList<String>();
		Set<String> once = new HashSet<String>();
		once.addAll(BodySignUtil.getOncePerItemCodes());
		//一天两次的项目
		List<String> twoItems = new ArrayList<String>();
		Set<String> twice = new HashSet<String>();
		twice.addAll(BodySignConstants.TWICE_PER_DAY_ITEMS);
		//一天三次的项目
		List<String> threeItems = new ArrayList<String>();
		Set<String> three = new HashSet<String>();
		three.addAll(BodySignConstants.THREE_TIMES_PER_DAY_ITEMS);
		//获取一种类型的项目
		if(null != item){
			String[] ss = item.split(",");
			for(String s :ss){
				if(once.contains(s)){
					oneItems.add(s);
				}else if(twice.contains(s)) {
					twoItems.add(s);
				}else if(three.contains(s)){
					threeItems.add(s);
				}else{
					moreItems.add(s);
				}
			}
		}
		Map<String,List<String>> rsMap = new HashMap<String,List<String>>();
		rsMap.put("one", oneItems);
		rsMap.put("two", twoItems);
		rsMap.put("three", threeItems);
		rsMap.put("six", moreItems);
		return rsMap;
	}

	@Override
	public List<BodySignRecord> queryBodySignList(String deptCode,String patId,String recordDate){
		List<BodySignRecord> records = new ArrayList<BodySignRecord>();
		Map<String,List<String>> itemMap = getItemCodeForBatch();
		//一天多次的项目
		List<String> moreItems = itemMap.get("six");
		//一天一次的项目
		List<String> oneItems = itemMap.get("one");
		//一天两次的项目
		List<String> twoItems = itemMap.get("two");
		//一天三次的项目
		List<String> threeItems = itemMap.get("three");
		
		
		moreItems.addAll(twoItems);
		twoItems.clear();
		moreItems.addAll(threeItems);
		threeItems.clear();
		
		
		//正点小时对应的时间
		Map<String,List<String>> dateMap = BodySignUtil.getAllDateByrecordDate(recordDate);
		
		//数据查询-查询生命体征
		records.addAll(bodySignRepository.queryBodySignList(deptCode,patId,recordDate,moreItems));
		Date fullDate = DateUtil.parse(recordDate, DateFormat.FULL) ;//记录时间    年月日时分秒
		String recordDay = DateUtil.format(fullDate,DateFormat.YMD);//记录日期    年月日
		String recordTime = DateUtil.format(fullDate,DateFormat.HMS);//记录时间    时分秒
		
		//其他点的数据
		List<BodySignRecord> itemList = new ArrayList<BodySignRecord>();
		//每天一次的项目
		if(!oneItems.isEmpty()){
			//获取其实和结束记录时间
			List<String> recordDates = dateMap.get("ones");//一天一次的项目的时间点
			itemList.addAll(bodySignRepository.queryBodySignItemList(deptCode, patId, recordDates, oneItems));
		}
		//每天两次的项目
		if(!twoItems.isEmpty()){
			List<String> recordDates = dateMap.get("twos");//一天两次的项目的时间点
			itemList.addAll(bodySignRepository.queryBodySignItemList(deptCode, patId,recordDates, twoItems));
			
		}
		//每天三次的项目
		if(!threeItems.isEmpty()){
			List<String> recordDates = dateMap.get("threes");//一天三次的项目的时间点
			itemList.addAll(bodySignRepository.queryBodySignItemList(deptCode, patId, recordDates, threeItems));
		}
		
		//数据收集
		Map<String,BodySignRecord> recordMap = new HashMap<String, BodySignRecord>();
		Set<String> itemCodeSet = new HashSet<String>();//患者流水和项目编号唯一
		if(!records.isEmpty()){
			for(BodySignRecord sign :records){
				String key = sign.getPatientId();
				recordMap.put(key, sign);
				List<BodySignItem> details = sign.getBodySignItemList();
				if(null != details){
					for(BodySignItem ite : details){
						itemCodeSet.add(key+ite.getItemCode());
					}
				}
			}
		}
		//数据合并
		for(BodySignRecord record : itemList) {
			String key = record.getPatientId();
			if(recordMap.containsKey(key)){
				BodySignRecord rs = recordMap.get(key);
				//明细信息合并
				List<BodySignItem> details = record.getBodySignItemList();
				if(null != details){
					for(BodySignItem ite : details){
						String itemKey = key+ite.getItemCode();
						if(!itemCodeSet.contains(itemKey)){
							itemCodeSet.add(key+ite.getItemCode());
							rs.getBodySignItemList().add(ite);
						}
					}
				}
				recordMap.put(key, rs);
			}else{
				record.setRecordDay(recordDay);//记录日期
				record.setRecordTime(recordTime);//记录时间
				record.setFullDateTime(fullDate);//记录时间
				//明细信息处理
				List<BodySignItem> details = record.getBodySignItemList();
				if(null != details){
					for(BodySignItem ite : details){
						itemCodeSet.add(key+ite.getItemCode());
					}
				}
				recordMap.put(key, record);
			}
		}
		records.clear();
		records.addAll(recordMap.values());
		//缓存清除
		itemList = null;
		//结果返回
		return records;
	}

	@Override
	public List<BodySignRecord> getFixTimeBodySignRecords(String[] patIds,
			String date, boolean isNda, boolean isAllItems) {
		List<BodySignRecord> bodySignRecords = new ArrayList<BodySignRecord>();
		if(!isNda){
			Date queryDate = new Date();
			if(StringUtils.isNotBlank(date)){
				queryDate = DateUtil.parse(date);
			}
			//获取多个患者一天一次的项目
			bodySignRecords = getSingleFixTimeBodySignRecords(patIds, queryDate, true);
		}else{
			//1.获取所有时间点
			String timesString = MnisConstants.ZERO_HM_TIME + MnisConstants.COMMA + 
					SuperCacheUtil.getSYSTEM_CONFIGS().get("tempInputTimeSelector");
			String[] times = timesString.split(MnisConstants.COMMA);
			List<Date> queryDates = new ArrayList<Date>();
			for (String string : times) {
				queryDates.add(DateUtil.parse(date.substring(0,10) + MnisConstants.EMPTY + string));
			}
			//2.获取需要的项目
			List<String> itemCodes = null;
			if(!isAllItems){
				String itemCodeString = SuperCacheUtil.getSYSTEM_CONFIGS().get("batchBodysignItem");
				if(StringUtils.isNotBlank(itemCodeString)){
					itemCodes = new ArrayList<String>();
					Collections.addAll(itemCodes, itemCodeString.split(MnisConstants.COMMA));
				}
			}
			//3.获取数据
			bodySignRecords = bodySignRepository.getFixTimeBodySignRecords(patIds, queryDates, itemCodes);
			
		}
		return bodySignRecords;
	}
	
	/**
	 * 保存生命体征信息
	 * 事物配置传递级别为REQUIRED
	 * @param records
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void saveBodySignRecords(List<BodySignRecord> records){
		if(null == records || records.isEmpty()){
			return;
		}
		//如果是第一次保存病人的体征记录，则需要自动生成入院事件
		BodySignRecord firstRecord = null;
		if(MnisConstants.ZERO_HM_TIME.equals(DateUtil.format(records.get(0).getFullDateTime(), DateFormat.HM))
				&& null!=records.get(1)){
			//第一条是出入量数据，则采用第二条记录
			//入院事件需要记录在生命体征记录中
			firstRecord = records.get(1);
		}else {
			firstRecord = records.get(0);
		}
		createAdmissionEventIfNecessary(firstRecord);

		//更新的生命体征item
		List<BodySignItem> upItemList = new ArrayList<BodySignItem>();
		//新增的生命体征item
		List<BodySignItem> itItemList = new ArrayList<BodySignItem>();
		//更新的事件
		List<PatientEvent> upEventList = new ArrayList<PatientEvent>();
		//新增的事件
		List<PatientEvent> itEventList = new ArrayList<PatientEvent>();
		//更新的生命体征主表
		List<BodySignRecord> upRecordList = new ArrayList<BodySignRecord>();
		//更新的皮试信息
		List<PatientSkinTest> upSkinTests = new ArrayList<PatientSkinTest>();
		//新增的皮试信息
		List<PatientSkinTest> itSkinTests = new ArrayList<PatientSkinTest>();
		
		
		//数据判断，是新增还是修改
		for(BodySignRecord record : records){
			//生命体征主表，查询是否已经存在，如果存在则更新，如果不存在则新增
			BodySignRecord dbRecord = bodySignRepository.queryBodySignRecord(record);
			if(null == dbRecord){
				Patient patient = patientService.getPatientByPatId(record.getPatientId());
				record.setBedCode(patient.getBedCode());//床位号
				record.setDeptCode(patient.getDeptCode());//科室编号
				record.setPatientName(patient.getName());
				int recordId = bodySignRepository.insertBodySignRecord(record);
				record.setMasterRecordId(recordId);
			}else{
				record.setMasterRecordId(dbRecord.getMasterRecordId());
				record.setBedCode(dbRecord.getBedCode());//床位信息
				record.setDeptCode(dbRecord.getDeptCode());//科室编号
				record.setPatientName(dbRecord.getPatientName());//患者姓名
				upRecordList.add(record);
			}
			//生命体征item表，查询是否已经存在，如果存在则更新，如果不存在则新增
			if(null != record.getBodySignItemList()){
				for(BodySignItem item : record.getBodySignItemList()){
					List<BodySignItem> itemList = bodySignRepository.queryBodySignItem(item);
					item.setMasterRecordId(record.getMasterRecordId());
					if (null == itemList || itemList.isEmpty()){
						itItemList.add(item);
					}else{
						item.setAdd(true);
						upItemList.add(item);
					}
				}
			}
			//生命体征事件表，查询是否已经存在，如果存在则更新，如果不存在则新增
			if(null != record.getEvent()){
				List<PatientEvent> dbevent = bodySignRepository.queryEvent(record.getEvent());
				record.getEvent().setMasterRecordId(String.valueOf(record.getMasterRecordId()));
				if(null == dbevent || dbevent.isEmpty()){
					itEventList.add(record.getEvent());
				}else{
					upEventList.add(record.getEvent());
				}
			}
			if(null != record.getSkinTestInfo()){
				record.getSkinTestInfo().setMasterRecordId(String.valueOf(record.getMasterRecordId()));
				List<PatientSkinTest> tests = bodySignRepository.querySkinTest(record.getSkinTestInfo());
				if(null == tests || tests.isEmpty()){
					itSkinTests.add(record.getSkinTestInfo());
				}else{
					upSkinTests.add(record.getSkinTestInfo());
				}
				
			}
		}
		//更新生命体征主表
		if(null != upRecordList){
			for(BodySignRecord record : upRecordList){
				bodySignRepository.updateBodySignMaster(record);
			}
		}
		//更新生命体征item表
		if(null != upItemList){
			for(BodySignItem item : upItemList){
				bodySignRepository.updateBodysignItem(item);
			}
		}
		//插入生命体征item表
		if(null != itItemList && !itItemList.isEmpty()){
			bodySignRepository.insertBodySignItems(itItemList);
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
		
		//更新皮试
		if(null != upSkinTests){
			for(PatientSkinTest test : upSkinTests){
				bodySignRepository.updateSkinTest(test);
				//如果皮试为阳性，那么转抄到过敏史
				if (MnisConstants.SKIN_TEST_POSITIVE.equals(test.getTestResult().toLowerCase())) {
					syncSkinTestToAllergy(test);
				}
			}
		}
		//插入皮试
		if(null != itSkinTests){
			for(PatientSkinTest test : itSkinTests){
				bodySignRepository.insertPatientSkinTest(test);
				//如果皮试为阳性，那么转抄到过敏史
				if (MnisConstants.SKIN_TEST_POSITIVE.equals(test.getTestResult().toLowerCase())) {
					syncSkinTestToAllergy(test);
				}
			}
		}
		// 转抄到护理单据
		for(BodySignRecord record : records){
			//如果配置需要转抄文书，并且存在生命体征item信息，那么转抄到护理文书
			if (identityService.isSyncDocReport() && record.hasBodySignItems()) {
				copyBodySignItemToDocReport(record,true);
			}
		}
	}
	
	@Override
	public List<BodySignRecord> queryBodyTemperature(String deptCode,String patId,String recordDate){
		List<BodySignRecord> records = new ArrayList<BodySignRecord>();
		//正点小时对应的时间
		Map<String,List<String>> dateMap = BodySignUtil.getAllDateByrecordDate(recordDate);
		
		//获取其实和结束记录时间
		List<String> recordDates = dateMap.get("ones");//一天一次的项目的时间点
		records.addAll(bodySignRepository.queryBodySignItemList(deptCode, patId, recordDates, null));
		//获取事件
		for(BodySignRecord record : records){
			PatientEvent parmEvent = new PatientEvent();
			parmEvent.setPatientId(record.getPatientId());
			parmEvent.setRecord_date(record.getFullDateTime());
			List<PatientEvent> event = bodySignRepository.queryEvent(parmEvent);
			if(null != event && !event.isEmpty()){
				record.setEvent(event.get(0));
			}
			PatientSkinTest test = new PatientSkinTest();
			test.setMasterRecordId(String.valueOf(record.getMasterRecordId()));
			List<PatientSkinTest> tests = bodySignRepository.querySkinTest(test);
			if(null != tests && !tests.isEmpty()){
				record.setSkinTestInfo(tests.get(0));
			}
		}
		
		//结果返回
		return records;
	}

	@Override
	public List<BodySignItemConfig> getBodySignConfigByDeptCode(String deptCode) {
		if(StringUtils.isBlank(deptCode)){
			return null;
		}
		List<BodySignItemConfig> bodySignSysConfigs = bodySignRepository.getBodySignConfigByDeptCode(deptCode);
		return bodySignSysConfigs;
	}
	
	@Override
	public void copyBodySignFromDoc(){
		String templeteId = "";
		String recordId = "";
		Map<String,String> parm = new HashMap<String,String>();
		parm.put("templeteId", templeteId);
		parm.put("recordId", recordId);
		List<BodySignItem> items = bodySignRepository.selectBodysignFromDoc(parm);
	}

	/**
	 * 更新（文书记录中的）生命体征信息到数据库体征表
	 * 1、如果参数指定时间的生命体征记录并不不存在，则创建之
	 * 2、如果体征记录已经存在，则仅更新对应的体征测量值，其余部分保持不变
	 * @param record 传入的生命体征信息
	 */
	@Override
	public void updateBodySignRecordValue(BodySignRecord record){
		//将文书方面的体征项目信息进行转换
		processBodySignItemFromDocInfo(record);
		//获取目标声明体征数据
		List<BodySignRecord> recordList = getBodySignRecord(record.getFullDateTime(), record.getPatientId(), true);
		if (recordList == null || 0>=recordList.size()) {
			//体征记录不存在，新插入一条
			record.setModifyTime(null);
			record.setModifyNurseCode(null);
			record.setModifyNurseName(null);

			saveBodySignRecord(record, false);
		}else {
			//存在记录，则更新传入的生命体征值
			BodySignRecord theRecord = recordList.get(0);
			theRecord.setModifyTime(record.getModifyTime());
			theRecord.setModifyNurseCode(record.getModifyNurseCode());
			theRecord.setModifyNurseName(record.getModifyNurseName());
			//先把原有的体征项组织成map，方便后续取数据
			HashMap<String, BodySignItem> origItemMap = new HashMap<>();
			for (BodySignItem origItem : theRecord.getBodySignItemList()) {
				origItemMap.put(origItem.getItemCode(), origItem);
			}
			//体征值更新到原有记录中
			for (BodySignItem bodySignItem : record.getBodySignItemList()) {
				if (origItemMap.get(bodySignItem.getItemCode()) != null) {
					//原有记录已有该项目，则更新值
					origItemMap.get(bodySignItem.getItemCode()).setItemValue(bodySignItem.getItemValue());
					origItemMap.get(bodySignItem.getItemCode()).setMeasureNoteCode(bodySignItem.getMeasureNoteCode());
					origItemMap.get(bodySignItem.getItemCode()).setMeasureNoteName(bodySignItem.getMeasureNoteName());
				}else {
					//原有记录不存在该项目，则新增
					bodySignItem.setMasterRecordId(theRecord.getMasterRecordId());
					theRecord.getBodySignItemList().add(bodySignItem);
				}
			}
			//没有用的数据置为空
			if(null!=theRecord.getEvent() && 0>=theRecord.getEvent().getId()){
				theRecord.setEvent(null);
			}
			if(null!=theRecord.getSkinTestInfo() && 0>=theRecord.getSkinTestInfo().getId()){
				theRecord.setSkinTestInfo(null);
			}
			updateBodySignRecord(theRecord, false);
		}

		//如果设置了备份，那么需要把文书转抄来的数据保存到备份表里
		//不太理解这样的需求，本着开放封闭原则，不去改动原有的处理流程，另外再写一个函数单独处理
		if("1".equals(identityService.getConfigure(BodySignConstants.DOC_BODYSIGN_BACKUP))){
			saveBodySignItemBackup(record);
		}
	}

	/**
	 * 文书转抄记录信息转换为对应的体征信息
	 * @param record 文书转抄信息
     */
    private void processBodySignItemFromDocInfo(BodySignRecord record){
		//出入量数据
		if(MnisConstants.ZERO_HM_TIME.equals(record.getRecordTime())){
			processStatisticBodySignItemFromDocInfo(record.getBodySignItemList());
		}else {
			//生命体征数据
			for (BodySignItem item : record.getBodySignItemList()) {
				item.setMeasureNoteCode(BodySignConstants.BODYSIGN_ITEM_MOREN);
				item.setMeasureNoteName(BodySignConstants.MAP_BODYSIGN_ITEM.get(BodySignConstants.BODYSIGN_ITEM_MOREN));
			}
		}

	}

	/**
	 * 出入量转抄信息转换为对应的体征信息
	 * @param itemList 出入量项目列表
     */
    private void processStatisticBodySignItemFromDocInfo(List<BodySignItem> itemList) {
		if (itemList == null || itemList.isEmpty()) {
			return;
		}
		int itemCnt = 0;
		for (BodySignItem item : itemList) {
			//转换itemcode
			String itemCode = BodySignConstants.DOC_ITEM_TO_BODYSIGN.get(item.getItemCode());
			if(!StringUtils.isEmpty(itemCode)){
				item.setItemCode(itemCode);
			}else if (itemCnt < MnisConstants.DOC2BODYSIGN_OTHEROUT_COUNT){
				item.setItemCode(BodySignConstants.OTHER_OUT_ITEMNAME[itemCnt++]);
			}
			//转换measurenote
			String noteCode = BodySignConstants.DOC_ITEM_TO_OTHER_OPT.get(item.getMeasureNoteCode());
			if(!StringUtils.isEmpty(noteCode)){
				item.setMeasureNoteCode(noteCode);
				item.setMeasureNoteName(BodySignConstants.OTHER_OPT_CODE_NAME.get(noteCode));
			}else {
				//没有找到对应项的，默认设置为特殊治疗
				item.setMeasureNoteCode("ts");
				item.setMeasureNoteName("特殊治疗");
			}

		}
	}

	/**
	 * 获取指定时间内进行了降温处理的生命体征信息
	 * @param patID 患者住院号
	 * @param deptCode 病区编号
	 * @param date 指定日期
	 * @param time 指定时间，约束为体温单上的时间节点
	 * @return 生命体征信息记录，不包含具体数据
	 */
	@Override
	public List<BodySignRecord> getBodySignRecordsWithCoolingProcess(String patID, String deptCode,
																	 String date, String time){
		List<BodySignRecord> records = new ArrayList<BodySignRecord>();
		if(StringUtils.isEmpty(patID) || StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(date)){
			return  records;
		}
		List<String> dateList = new ArrayList<String>();
		if(StringUtils.isEmpty(time)){
			//获取一天的时间节点
			String[] timeArray = identityService.getTempratureInputTimeArray();
			for (String timePoint : timeArray) {
				dateList.add(date + " " + timePoint);
			}
		}else {
			dateList.add(date + " " + time);
		}
		List<String> items = new ArrayList<String>();
		//含有降温方式的认为是进行了降温处理
		items.add(BodySignConstants.COOL_WAY);
		records = bodySignRepository.queryBodySignItemList(deptCode, patID, dateList, items);
		//由于降温选项为空值的数据也保存到了数据库，这里要进行过滤
		HashMap<String, BodySignItemConfig.BodySignItemConfigOpts> coolOptMap =
				new HashMap<String, BodySignItemConfig.BodySignItemConfigOpts>();
		List<BodySignItemConfig> itemConfigs = getBodySignConfigByDeptCode(deptCode);
		for (BodySignItemConfig config : itemConfigs) {
			if(config.getCode().equals(BodySignConstants.COOL_WAY)){
				for (BodySignItemConfig.BodySignItemConfigOpts opt : config.getBodySignItemConfigOpts()) {
					if(opt.getCode() != null && !StringUtils.isEmpty(opt.getCode())){
						coolOptMap.put(opt.getText(), opt);
					}
				}
			}
		}
		//转成数组列表删除掉降温方式为空的记录
		records = new ArrayList<BodySignRecord>(records);
		for (int i = 0; i < records.size(); i++) {
			//此处生命体征记录只有一项降温处理
			String itemValue = records.get(i).getBodySignItemList().get(0).getItemValue();
				if(null == itemValue || null == coolOptMap.get(itemValue)){
					records.remove(i);
					i--;
			}
		}

		return records;
	}

	/**
	 * 把生命体征记录数据组织成VO形式，方便输出到体温单
	 * @param records 生命体征记录
	 * @return 生命体征VO形式数据集合
     */
    @Override
	public List<BodySignRecordVo> getBodySignRecordVosFromBodySignRecords(List<BodySignRecord> records){
		ArrayList<BodySignRecordVo> recordsVo = new ArrayList<>();

		//把传入的生命体征数据根据时间点组织成MAP，方便后续取数据
		HashMap<String, BodySignRecord> bodySignRecordMap = new HashMap<String, BodySignRecord>();
		for (BodySignRecord record : records) {
			String strTime = DateUtil.format(record.getFullDateTime(), DateFormat.HM);
			bodySignRecordMap.put(strTime, record);
		}

		String timesString = MnisConstants.ZERO_HM_TIME + MnisConstants.COMMA
							+ identityService.getConfigure("tempInputTimeSelector");
		List<String> recordNameList = new ArrayList<String>();
		Collections.addAll(recordNameList, timesString.split(MnisConstants.COMMA));
		//无体征数据的时间点填充为空
		BodySignRecord emptyBodySignRecord = new BodySignRecord();
		emptyBodySignRecord.setBodySignItemList(new ArrayList<BodySignItem>());
		for (int i = 0; i < recordNameList.size(); i++) {
			BodySignRecordVo bodySignRecordVo = new BodySignRecordVo();
			bodySignRecordVo.setRecordName(recordNameList.get(i));
			bodySignRecordVo.setTimeId(DateUtil.getFixTypeFromTime(recordNameList.get(i), Calendar.HOUR_OF_DAY));
			// 根据指定时间点获取生命体征数据
			BodySignRecord bodySignRecord = bodySignRecordMap.get(recordNameList.get(i));
			if (null == bodySignRecord) {
				bodySignRecordVo.setBodySignRecord(emptyBodySignRecord);
			} else {
				bodySignRecordVo.setBodySignRecord(bodySignRecord);
			}
			recordsVo.add(bodySignRecordVo);
		}

		return recordsVo;
	}

	/**
	 * 转抄指定的生命体征数据到护理文书
	 * @param patIDs 指定患者的住院号集合
	 * @param deptCode 科室编号
	 * @param date 指定日期
	 * @param time 指定体征录入时间
     * @throws Exception
     */
    public void copyBodySignRecordsToDocRecords(List<String> patIDs, String deptCode,
												String date, String time)throws Exception{
		if(patIDs.isEmpty() || StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(date)
				|| StringUtils.isEmpty(time)){
			throw new MnisException("参数信息不全。");
		}
		//获取生命体征数据记录
		Date theDate = DateUtil.parse(date + " " + time, DateFormat.YMDHM);
		String[] strPatIDs = patIDs.toArray(new String[patIDs.size()]);
		List<BodySignRecord> bodySignRecords = getBodySignRecord(theDate, strPatIDs, true);
		//转抄到护理文书
		for (BodySignRecord record : bodySignRecords) {
			copyBodySignItemToDocReport(record, false);
		}
	}

}
