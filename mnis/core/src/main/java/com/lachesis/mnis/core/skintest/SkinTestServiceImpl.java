package com.lachesis.mnis.core.skintest;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.SkinTestService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.event.DocReportEvent;
import com.lachesis.mnis.core.event.MnisEventPublisher;
import com.lachesis.mnis.core.event.entity.DocReportEventEntity;
import com.lachesis.mnis.core.event.entity.DocReportEventItemEntity;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.nursing.NurseRecordService;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientSkinTest;
import com.lachesis.mnis.core.skintest.entity.SkinTestDrugInfo;
import com.lachesis.mnis.core.skintest.entity.SkinTestGroup;
import com.lachesis.mnis.core.skintest.entity.SkinTestInfoLx;
import com.lachesis.mnis.core.skintest.entity.SkinTestItem;
import com.lachesis.mnis.core.skintest.repository.SkinTestRepository;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.core.util.SuperCacheUtil;
/**
 * 皮试接口实现
 * 
 * @author liangming.deng
 * 
 */
@Service("skinTestService")
public class SkinTestServiceImpl implements SkinTestService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(SkinTestServiceImpl.class);

	@Autowired
	SkinTestRepository skinTestRepository;

	@Autowired
	OrderService orderService;

	@Autowired
	private NurseRecordService nurseRecordService;

	@Autowired
	private BodySignService bodySignService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	private MnisEventPublisher mnisEventPulisher;

	@Override
	public List<SkinTestInfoLx> getSkinTestInfos(String patId, String nurseId,
			String deptId, String selectDate) {
		List<String> skinTestDrugCodes = identityService.getSkinTestDrugCodes();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		Date queryDate = null;
		if (StringUtils.isBlank(selectDate)) {
			queryDate = new Date();
		}else{
			if(selectDate.length() < DateFormat.FULL.getFormatName().length())
				queryDate = DateUtil.parse(selectDate, DateFormat.YMD);
			
			else {
				queryDate = DateUtil.parse(selectDate, DateFormat.FULL);
			}
		}
		paramMap.put("minSelectDate", DateUtil.setDateToDay(queryDate));// 2014-12-11 00:00:00
		paramMap.put("maxSelectDate", DateUtil.setNextDayToDate(queryDate));// 2014-12-12 00:00:00
		paramMap.put("skinTestDrugCodes", skinTestDrugCodes);
		paramMap.put("wardCode", deptId);
		
		if(StringUtils.isEmpty(deptId) && StringUtils.isEmpty(nurseId)){
			if(StringUtils.isEmpty(patId))
				throw new MnisException("部门ID,护士ID和病人Id参数至少需要一个");
			else{
				paramMap.put("patId", patId);
			}
		}/*else{
			List<String> patients = patientService.getPatientByDeptCodeOrUserCode(nurseId, deptId);
			paramMap.put("patients", patients);
		}*/   /*全科室根据科室编号查询*/
		
		List<SkinTestInfoLx> skinTestInfoTs = this.skinTestRepository
				.getSkinTestInfos(paramMap);

		return skinTestInfoTs;
	}

	@Override
	public int saveSkinTestItem(SkinTestItem skinTestItem) {
		if (skinTestItem == null)
			throw new RuntimeException("皮试信息不存在!");
		return this.skinTestRepository.saveSkinTestItem(skinTestItem);
	}

	@Override
	public int updateSkinTestItem(SkinTestItem skinTestItem) {
		if (skinTestItem == null)
			throw new RuntimeException("皮试信息不存在!");
		return this.skinTestRepository.updateSkinTestItem(skinTestItem);
	}

	@Override
	public SkinTestItem getSkinTestItemByStItemId(String skItemId) {
		return this.skinTestRepository.getSkinTestItemByStItemId(skItemId);
	}

	/**
	 * 执行皮试医嘱
	 */
	@Override
	public int execSkinTestInfo(final SkinTestItem skinTestItem,
			String execNurseId, String execNurseName, String stBeforeImg) {
		int execFlag = 0;

		Patient patient = patientService.getPatientByPatId(skinTestItem.getPatId());
		
		if(patient == null){
			throw new AlertException("该病人不存在!");
		}
		boolean isUpdate = false;
		
		// 带皮试状态->皮试中
		// 1.保存皮试执行,2.保存皮试完成,3.录入，核对
		SkinTestItem existSkinTestItem = skinTestRepository.getSkinTestItemByStId(skinTestItem.getSkinTestId());
		if (existSkinTestItem != null){
			if(!"0".equals(existSkinTestItem.getSkinTestItemStatus())){
				throw new AlertException("皮试已执行!");
			}
			isUpdate = true;
		}
		
		if(skinTestItem.getSkinTestItemExecNurseId() == null){
			skinTestItem.setSkinTestItemExecNurseId(execNurseId);
			skinTestItem.setSkinTestItemExecNurseName(execNurseName);
		}
		
		skinTestItem.setDrugName(getDrugName(skinTestItem.getDrugName()));
		skinTestItem.setSkinTestItemExecDate(new Date());

		
		// base64将String转为byte[]
		if (StringUtils.isNotEmpty(stBeforeImg)){
			skinTestItem.setSkinTestItemImgBefore(StringUtil.stringToByte(stBeforeImg));
		}
		skinTestItem.setSkinTestItemStatus("1");// 状态：皮试中
		//判断是插入还是修改
		if(isUpdate){
			execFlag = skinTestRepository.updateSkinTestItem(skinTestItem);
		}else{
			execFlag = skinTestRepository.saveSkinTestItem(skinTestItem);
		}
	
		if (execFlag == 0) {
			throw new AlertException("医嘱执行不成功");
		}

		// 执行医嘱
//		orderService.execOrderGroupWithPDA(skinTestItem.getSkinTestId(), skinTestItem.getPatId(), execNurseId,null,null,null);
		
		if (!isUpdate && skinTestItem != null ) {
			new Thread() {
				@Override
				public void run() {
					//文书转抄--阴性和阳性都转
					publishDocReportEvent(skinTestItem,0);
				}
			}.start();
			
		}
		
		return execFlag;
	}

	/**
	 * 双核确认皮试
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public int approveSkinTestInfo(final SkinTestItem skinTestItem, String stAfterImg,
			UserInformation sessionUserInfo,String approveUserLoginName,String approveNursePwd) {
		// First verify approve-nurse's identity
		UserInformation user = identityService.verifyUser(
				approveUserLoginName, approveNursePwd,null,false);
		if (null != SuperCacheUtil.getSYSTEM_CONFIGS().get("isNeedDoubleCheck")
				&& "1".equals(SuperCacheUtil.getSYSTEM_CONFIGS().get(
						MnisConstants.IS_NEED_DOUBLE_CHECK))) {
			if (user == null) {
				return MnisConstants.DOUBLE_CHECK_PASSOWRD_ERR;
			}

			if (!user.getDeptCode().equals(sessionUserInfo.getDeptCode())) {
				return MnisConstants.DOUBLE_CHECK_NOT_SAME_DEPT_ERR;
			}

			if (user.getCode().equals(sessionUserInfo.getCode())) {
				return MnisConstants.DOUBLE_CHECK_SAME_NURSER_ERR;
			}
			
			
		}
		//不管需不需要双核对，如果用户进行了双核对，那么核对人需要保存
		if(null != user){
			// Second save skin test result once approved
			skinTestItem.setSkinTestItemApproveNurseId(user.getCode());
			skinTestItem.setSkinTestItemApproveNurseName(user.getName());
		}
		skinTestItem.setSkinTestItemApproveDate(new Date());
		skinTestItem.setSkinTestItemInputNurseId(sessionUserInfo.getCode());
		skinTestItem.setSkinTestItemInputNurseName(sessionUserInfo.getName());
				
				
		int execFlag = 0;

		// base64将String转为byte[]
		if (StringUtils.isNotEmpty(stAfterImg)){
			skinTestItem.setSkinTestItemImgAfter(StringUtil.stringToByte(stAfterImg));
		}
		
		// 判断皮试信息是否存在
		if (skinTestRepository.getSkinTestItemByStId(skinTestItem
				.getSkinTestId()) == null)
			throw new AlertException("皮试不存在!");
		
		String drugName = skinTestItem.getDrugName();
		String showDrugName = drugName;
		if(StringUtils.isNotBlank(drugName) 
				&& drugName.indexOf("[") >= 0){
			showDrugName = drugName.substring(drugName.indexOf("["),StringUtils.lastIndexOf(drugName, "]")+1);
		}
		skinTestItem.setDrugName(showDrugName);
		// 皮试中->已皮试
		skinTestItem.setSkinTestItemStatus("2");
		skinTestItem.setSkinTestItemResult(skinTestItem.getSkinTestItemResult().toLowerCase());
		// 1.保存皮试执行,2.保存皮试完成,3.录入，核对
		execFlag = skinTestRepository.updateSkinTestItem(skinTestItem);

		if (execFlag == 0)
			throw new AlertException("皮试信息更新失败");

		SkinTestInfoLx skinTestInfoLx = this.skinTestRepository
				.getSkinTestInfoByStId(skinTestItem.getSkinTestId());
		if (skinTestInfoLx == null)
			throw new AlertException("皮试信息错误");
		final PatientSkinTest skinTestInfo = skinTestInfoLxConvertSkinTestInfo(
				skinTestInfoLx, skinTestItem.getSkinTestId());
		
		if (skinTestInfo != null ) {
			new Thread() {
				@Override
				public void run() {
					// Third: 阳性保存皮试结果到体温单
					bodySignService.saveSkinTestInfoToBodySign(skinTestInfo);
					//文书转抄--阴性和阳性都转
					publishDocReportEvent(skinTestItem,1);
				}
			}.start();
			
		}

		return execFlag;
	}
	
	/**
	 * 发布 文书皮试结果
	 * @param skinTestItem
	 */
	private void publishDocReportEvent(SkinTestItem skinTestItem,int type) {
		DocReportEventEntity docReportEventEntity = new DocReportEventEntity();
		
		String createPerson = MnisConstants.EMPTY;
		String approvePerson = MnisConstants.EMPTY;
		Date createTime = null;
		String execDateStr = MnisConstants.EMPTY;
		String val = MnisConstants.EMPTY;
 		
		if(0 == type){
			createPerson = skinTestItem.getSkinTestItemExecNurseId()
					+ "-" + skinTestItem.getSkinTestItemExecNurseName();
			approvePerson = skinTestItem.getSkinTestItemExecNurseName();
			createTime = skinTestItem.getSkinTestItemExecDate();
			execDateStr = DateUtil.format(skinTestItem.getSkinTestItemExecDate(),
					DateFormat.FULL);
			
			val = "执行" + skinTestItem.getDrugName()+"皮试";
		}else{
			createPerson = skinTestItem.getSkinTestItemApproveNurseId()
					+ "-" + skinTestItem.getSkinTestItemInputNurseName();
			approvePerson = skinTestItem.getSkinTestItemApproveNurseName();
			createTime = skinTestItem.getSkinTestItemApproveDate();
			execDateStr = DateUtil.format(skinTestItem.getSkinTestItemApproveDate(),
					DateFormat.FULL);
			val = skinTestItem.getDrugName()+"皮试结果："+("N".equalsIgnoreCase(skinTestItem.getSkinTestItemResult())?"阴性":"阳性");
		}
		
		
		docReportEventEntity.setCreate_person(createPerson);
		docReportEventEntity.setApprove_person(approvePerson);
		docReportEventEntity.setCreateTime(createTime);
		String[] execDateStrs = execDateStr.split(" ");
		docReportEventEntity.setDate_list(execDateStrs[0]);
		docReportEventEntity.setTime_list(execDateStrs[1]);
		docReportEventEntity.setInpatient_no(skinTestItem.getPatId());
		docReportEventEntity.setDept_code(skinTestItem.getDeptCode());

		List<DocReportEventItemEntity> docReportEventItemEntities = new ArrayList<DocReportEventItemEntity>();
		DocReportEventItemEntity nameDocReportEventItemEntity = new DocReportEventItemEntity();
		
		nameDocReportEventItemEntity.setRecord_value(val);
		nameDocReportEventItemEntity.setTemplate_item_id("remark1");    
		docReportEventItemEntities.add(nameDocReportEventItemEntity);
		
		docReportEventEntity.setData_item(docReportEventItemEntities);
		mnisEventPulisher
				.publish(new DocReportEvent(this, docReportEventEntity));
	}
	
	
	@Override
	public void updateSkinTestImg(String patId,String stItemId,String imageString,int imageType){
		
		if(StringUtils.isBlank(stItemId)){
			throw new AlertException("指定皮试参数错误!");
		}
		if(StringUtils.isBlank(imageString)){
			return ;
		}
		//将字符串图片转为字节流数组byte[]
		byte[] imageBytes = StringUtil.stringToByte(imageString);
		
		SkinTestItem existSkinTestItem = this.skinTestRepository.getSkinTestItemByStId(stItemId);
		//判断是修改还是插入
		boolean isUpdate = true;
		
		
		if(imageType == 0){
			if(existSkinTestItem == null){
				isUpdate = false;
			}else{
				//皮试执行后无法插入皮试前图片
//				if("1".equals(existSkinTestItem.getSkinTestItemStatus())){
//					LOGGER.debug("SkinTestServiceImpl updateSkintTestImg:皮试执行后无法插入皮试前图片!");
//					throw new AlertException("皮试执行后无法插入皮试前图片!");
//				}
			}
		}else{
			if(existSkinTestItem == null){
				throw new AlertException("指定皮试不存在!");
			}else{
				//皮试中，不能修改皮试后图片
				if("0".equals(existSkinTestItem.getSkinTestItemStatus())){
					LOGGER.debug("SkinTestServiceImpl updateSkintTestImg:未执行皮试不能保存皮试后图片!");
					throw new AlertException("未执行皮试不能保存皮试后图片!");
				}
			}
			
		}
		
		SkinTestItem skinTestItem = new SkinTestItem();
		skinTestItem.setSkinTestId(stItemId);
		skinTestItem.setPatId(patId);
		if(0 == imageType){
			skinTestItem.setSkinTestItemImgBefore(imageBytes);
		}else{
			skinTestItem.setSkinTestItemImgAfter(imageBytes);
		}
		
		if(isUpdate){
			this.skinTestRepository.updateSkinTestImg(skinTestItem);
		}else{
			this.skinTestRepository.saveSkinTestItemImg(skinTestItem);
		}
	}

	/**
	 * 根据图片标示，返回对应标示的皮试图片,0或null，返回前，1返回后
	 */
	@Override
	public byte[] getSkinTestItemImg(String stItemId, String imageNo) {
		byte[] imgBytes = null;
		if (StringUtils.isEmpty(stItemId))
			throw new AlertException("传入参数错误");
		SkinTestItem skinTestItem = null;
		if (StringUtils.isBlank(imageNo) || "0".equals(imageNo)) {
			skinTestItem = skinTestRepository.getImgBeforeByStId(stItemId);
			if (skinTestItem != null) {
				imgBytes = skinTestItem.getSkinTestItemImgBefore();
			}
		} else {
			skinTestItem = skinTestRepository.getImgAfterByStId(stItemId);
			if (skinTestItem != null) {
				imgBytes = skinTestItem.getSkinTestItemImgAfter();
			}
		}
		return imgBytes;
	}

	/**
	 * 将皮试模块类转化为生命体征皮试类
	 * 
	 * @param skinTestInfoT
	 * @return
	 */
	private PatientSkinTest skinTestInfoLxConvertSkinTestInfo(
			SkinTestInfoLx skinTestInfoLx, String stId) {

		SkinTestGroup skinTestGroup = null;
		/**
		 * 根据stId获取skinTestGroup
		 */
		for (SkinTestGroup stGroup : skinTestInfoLx.getSkinTestGroups()) {
			if (stGroup.getSkinTestId().equals(stId)) {
				skinTestGroup = stGroup;
				break;
			}
		}
		if (skinTestGroup == null) {
			return null;
		}
		SkinTestItem skinTestItem = skinTestGroup.getSkinTestItem();
		SkinTestDrugInfo skinTestDrugInfo = skinTestGroup.getSkinTestDrugInfo();

		PatientSkinTest skinTestInfo = new PatientSkinTest();

		skinTestInfo.setApproveNurseId(skinTestItem
				.getSkinTestItemApproveNurseId());
		skinTestInfo.setApproveNurseName(skinTestItem
				.getSkinTestItemApproveNurseName());
		skinTestInfo.setDrugBatchNo(skinTestItem.getSkinTestItemDrugBatchNo());
		skinTestInfo.setDrugCode(skinTestDrugInfo.getDrugCode());
		
		skinTestInfo.setDrugName(getDrugName(skinTestDrugInfo.getDrugName()));
		//skinTestInfo.setInHospNo(skinTestInfoLx.getInHospNo());
		skinTestInfo.setMasterRecordId(skinTestGroup.getMasterRecordId());
		skinTestInfo.setOrderExecId(skinTestGroup.getSkinTestId());
		skinTestInfo.setPatientId(skinTestInfoLx.getPatientId());
		skinTestInfo.setPatientName(skinTestInfoLx.getPatientName());
		skinTestInfo.setId(Integer.parseInt(skinTestGroup.getSkinTestItem().getSkinTestItemId()));
		skinTestInfo.setExecDate(skinTestGroup.getSkinTestItem().getSkinTestItemExecDate());
		skinTestInfo.setTestNurseId(skinTestGroup.getSkinTestItem().getSkinTestItemExecNurseId());
		skinTestInfo.setTestNurseName(skinTestGroup.getSkinTestItem().getSkinTestItemExecNurseName());
		skinTestInfo.setTestResult(skinTestItem.getSkinTestItemResult().toLowerCase());
		//skinTestInfo.setDeptCode(skinTestItem.getDeptCode());

		return skinTestInfo;
	}
	
	private String getDrugName(String drugName){
		String showDrugName = drugName;
		if(StringUtils.isNotBlank(drugName) 
				&& drugName.indexOf("[") >= 0){
			showDrugName = drugName.substring(drugName.indexOf("["),StringUtils.lastIndexOf(drugName, "]")+1);
		}
		return showDrugName;
	}

	@Override
	public List<SkinTestInfoLx> getPublishSkinTests(String deptCode,
			Date startTime, Date endTime) {
		
		if(StringUtils.isBlank(deptCode)){
			throw new AlertException("部门参数为空!");
			
		}
		List<String> skinTestDrugCodes = identityService.getSkinTestDrugCodes();
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		
		if(null == startTime){
			startTime = DateUtils.addDays(new Date(), -1);
		}
		
		paramMap.put("skinTestDrugCodes", skinTestDrugCodes);
		paramMap.put("wardCode", deptCode);
		paramMap.put("startTime", startTime);
		paramMap.put("endTime", endTime);
		
		
		List<SkinTestInfoLx> skinTestInfoTs = this.skinTestRepository
				.getPublishSkinTests(paramMap);
		return skinTestInfoTs;
	}
}
