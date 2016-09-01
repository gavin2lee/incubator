/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.WardPatrolService;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.Dict;
import com.lachesis.mnis.core.identity.entity.SysDept;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.nursing.NurseShiftService;
import com.lachesis.mnis.core.patient.entity.Bed;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientAllergy;
import com.lachesis.mnis.core.patient.entity.PatientBaseInfo;
import com.lachesis.mnis.core.patient.entity.PatientDiagnosis;
import com.lachesis.mnis.core.patient.entity.PatientPrint;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.task.TaskRepository;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.PatientUtils;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;
import com.lachesis.mnis.web.vo.BedPatientInfo;
import com.lachesis.mnis.web.vo.BedPatientInfoResult;
import com.lachesis.mnis.web.vo.PatientInfoResult;

/**
 * The class PatientGlanceAction.
 * 
 * 病人床位一览
 * 
 * @author: yanhui.wang
 * @since: 2014-6-10
 * @version: $Revision$ $Date$ $LastChangedBy$
 * 
 */
@Controller
@RequestMapping("/nur/patientGlance")
public class PatientGlanceAction {
	static final Logger LOGGER = LoggerFactory.getLogger(PatientGlanceAction.class);

	@Autowired
	private PatientService patientService;
	@Autowired
	private NurseShiftService nurseShiftService;
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private TaskService taskService;

	@Autowired
	private BodySignService bodySignService;
	
	@Autowired
	private WardPatrolService wardPatrolService;
	
	@Autowired
	private TaskRepository taskRepository;
	
	/**
	 * 
	 * 进入病人一览界面，默认获取当前登录用户所在科室所有的病床信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/patientGlanceMain")
	public String patientGlanceMain(Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> redirect to patient glance page.");
		}

		Set<String> beds = null;
		UserInformation userInfo = WebContextUtils.getSessionUserInfo();
		if (userInfo != null) {
			// BedPatientInfoResult  完全没有存在的必要，真心复杂
			BedPatientInfoResult result = getBedPatientList(1, userInfo.getDeptCode());
			
			List<BedPatientInfo> bedList = result.getBedInfoList();
			if (bedList != null && bedList.size() >0) {
				beds = new HashSet<>(bedList.size());
				// 选择全科为关注信息
				for (BedPatientInfo bedInfo : bedList) {
					beds.add(bedInfo.getBedCode());
				}
				model.addAttribute("selectedBedPatSet", beds);

				model.addAttribute("bedList", bedList);
				model.addAttribute("workUnitStatis", result.getWorkUnitStatistics());
			}
			model.addAttribute("deptCode", userInfo.getDeptCode());
			model.addAttribute("deptName", userInfo.getDeptName());
			model.addAttribute("user", userInfo);
			model.addAttribute("hospitalName", identityService.getHospitalName());
		}
		
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("currentMinDate", DateUtil.format(DateUtil.getCurDateWithMinTime(), DateFormat.YMDHM));
		model.addAttribute("currentMaxDate", DateUtil.format(DateUtil.getCurDateWithMaxTime(), DateFormat.YMDHM));
		
		return "/nur/patientGlance";
	}
	
	@RequestMapping("/getPateintBedList")
	@ResponseBody
	public BaseMapVo getPateintBedList(String deptCode ){
		BaseMapVo vo = new BaseMapVo();
		BedPatientInfoResult result = getBedPatientList(1, deptCode);
		
		List<BedPatientInfo> bedList = result.getBedInfoList();
		vo.addData("bedList", bedList);
		return vo;
	}

	/**
	 * 转向患者过敏史修改页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/patAllergHistEdit")
	public String patAllergHistEdit(ModelMap modelMap) {
		List<Dict> drugList = identityService.queryDrugList();
		int rows = 0;
		if (drugList.size() % 5 == 0) {
			rows = drugList.size() / 5;
		} else {
			rows = (drugList.size() / 5) + 1;
		}
		modelMap.addAttribute("totalRows", rows);
		modelMap.addAttribute("drugList", drugList);

		return "/nur/patAllergHistEdit";
	}

	/**
	 * 转向患者诊断信息修改页面
	 * 
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/patientDiagEdit")
	public String patientDiagEdit(ModelMap modelMap) {
		List<Dict> diagList = identityService.queryDiagList();
		int rows = 0;
		if (diagList.size() % 5 == 0) {
			rows = diagList.size() / 5;
		} else {
			rows = (diagList.size() / 5) + 1;
		}
		modelMap.addAttribute("totalRows", rows);
		modelMap.addAttribute("diagList", diagList);

		return "/nur/patientDiagEdit";
	}

	/**
	 * 
	 * 根据输入的床位编号，筛选数据；如果查询的数据为空，则取科室下所有的病床列表；
	 * 
	 * @return
	 */
	@RequestMapping("/queryBedPatListByCode")
	public @ResponseBody
	List<BedPatientInfo> queryBedPatListByCode(@RequestParam("data") String data)
			throws Throwable {
		
		UserInformation userInfo = WebContextUtils.getSessionUserInfo();
		List<BedPatientInfo> list = new ArrayList<BedPatientInfo>();

		if (userInfo != null) {
			BedPatientInfoResult result = getBedPatientList(1, userInfo.getDeptCode());
			if (result != null && !result.getBedInfoList().isEmpty()) {
				list.addAll(result.getBedInfoList());
			}
		}

		if (list == null || list.isEmpty()) {
			return null;
		}

		if (data != null && data.trim().length() != 0) {
			for (BedPatientInfo bedPatientInfo : list) {
				if(data.equals(bedPatientInfo.getBedCode())){
					list.clear();
					list.add(bedPatientInfo);
					break;
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * 根据输入的患者姓名，筛选数据；如果查询的数据为空，则取科室下所有的病床列表；
	 * 
	 * @return
	 */
	@RequestMapping("/queryBedPatListByName")
	public @ResponseBody
	List<BedPatientInfo> queryBedPatListByName(@RequestParam("data") String data)
			throws Throwable {
		UserInformation userInfo = WebContextUtils.getSessionUserInfo();
		List<BedPatientInfo> list = new ArrayList<BedPatientInfo>();

		if (userInfo != null) {
			BedPatientInfoResult result = getBedPatientList(1, userInfo.getDeptCode());
			if (result != null && !result.getBedInfoList().isEmpty()) {
				list.addAll(result.getBedInfoList());
			}
		}

		if (list == null || list.isEmpty()) {
			return null;
		}

		if (data != null && data.trim().length() != 0) {
			for (BedPatientInfo bedPatientInfo : list) {
				if(data.equals(bedPatientInfo.getPatientName())){
					list.clear();
					list.add(bedPatientInfo);
					break;
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * 根据输入的患者住院号，筛选数据；如果查询的数据为空，则取科室下所有的病床列表；
	 * 
	 * @return
	 */
	@RequestMapping("/queryBedPatListByHospNo")
	public @ResponseBody
	List<BedPatientInfo> queryBedPatListByHospNo(@RequestParam("data") String data){
		UserInformation userInfo = WebContextUtils.getSessionUserInfo();
		List<BedPatientInfo> list = new ArrayList<BedPatientInfo>();

		if (userInfo != null) {
			BedPatientInfoResult result = getBedPatientList(1, userInfo.getDeptCode());
			if (result != null && !result.getBedInfoList().isEmpty()) {
				list.addAll(result.getBedInfoList());
			}
		}

		if (list == null || list.isEmpty()) {
			return null;
		}

		if (data != null && data.trim().length() != 0) {
			for (BedPatientInfo bedPatientInfo : list) {
				if(data.equals(bedPatientInfo.getInHospitalNo())){
					list.clear();
					list.add(bedPatientInfo);
					break;
				}
			}
		}
		return list;
	}

	/**
	 * 
	 * 查询当前登录用户所在的科室列表；
	 * 
	 * @return
	 */
	@RequestMapping("/queryLoginUserDeptList")
	public @ResponseBody
	List<SysDept> queryLoginUserDeptList() throws Throwable {

		return new ArrayList<SysDept>();
	}

	/**
	 * 根据患者ID查询患者信息
	 * @param id 患者ID
	 * @return
	 */
	@RequestMapping("/getInpatInfoByPatId")
	@ResponseBody
	public BaseDataVo getInpatientInfoByPatId(String id, String deptId){
		BaseDataVo vo = new BaseDataVo();
		
		Patient patient = patientService.getPatientByPatId(id);
		if (patient == null) {
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("没有此患者");
			return vo;
		}
		
		PatientInfoResult result = new PatientInfoResult();
		String admitDiags = "";
		String allergyStr = "";
		
		UserInformation userInfo = WebContextUtils.getSessionUserInfo();
		String currDept = deptId;
		if(StringUtils.isEmpty(currDept)){
			currDept = userInfo.getDeptCode();
		}
		if (!patient.getDeptCode().equals(currDept)) {
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("此患者不属于此科室");
			return vo;
		}
		/*Executors.newCachedThreadPool().execute(new Runnable() {

			@Override
			public void run() {
				// 所有扫描患者腕带的记录自动同步到巡视记录里
				if (StringUtils.isNotEmpty(currUser)) {
					wardPatrolService.saveWardPatrolInfoForPatient(
							inpatientWithOrders.getInpatientInfo(),
							currUser, currDept);
				}
			}
		});*/
		

		formatPatientTime(patient);
/*		List<String> allergyList = patientInfo.getAllergen();
		if (allergyList != null && !allergyList.isEmpty()) {
			for (Iterator<String> iterator = allergyList.iterator(); iterator.hasNext();) {
				String allergy = (String) iterator.next();
				if(allergy == null || allergy.contains("-") || allergy.contains("—")){
					iterator.remove();
				}
			}
		}*/
		/*List<DiagRecordEntity> diagList = diagRecordService
				.selectDiagRecord(id);
		if (allergyList != null && !allergyList.isEmpty()) {
			allergyStr = GsonUtils.toJSon(allergyList);
		}
		if (diagList != null && !diagList.isEmpty()) {
			admitDiags = GsonUtils.toJSon(diagList);
		}*/
		result.setInpatientInfo(patient);
		result.setAdmitDiags(admitDiags);
		result.setAllergyStr(allergyStr);
		vo.setRslt(ResultCst.SUCCESS);
		vo.setData(result);

		return vo;
	}
	
	/**
	 * 根据患者ID,患者姓名,患者住院号查询患者基本信息
	 * @param patId
	 * @param patName
	 * @param inHospNo
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getPatientBaseInfoByPatInfo")
	@ResponseBody
	public BaseMapVo getPatientBaseInfoByPatInfo(String patId,String patName,String inHospNo, String deptCode){
		BaseMapVo vo = new BaseMapVo();
		
		List<PatientBaseInfo> patientBaseInfos;
		try {
			patientBaseInfos = patientService.getPatientBaseInfoByPatInfo(patId, patName, inHospNo, deptCode);
			vo.setRslt(ResultCst.SUCCESS);
			vo.addData("patients", patientBaseInfos);
		} catch (AlertException e) {
			LOGGER.error("PatientAction getPatientBaseInfoByPatInfo error:" + e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg(e.getMessage());
		}catch (Exception e) {
			LOGGER.error("PatientAction getPatientBaseInfoByPatInfo error:" + e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("获取患者失败!");
		}
		
		return vo;
	}

	/**
	 * 
	 * 根据当前登录信息，获取床位列表(根据配置，显示是否显示空床)
	 * 
	 * @param workUnitType
	 *            类型
	 * @param workUnitCode
	 *            部门编码
	 * @return
	 */
	@RequestMapping("/queryBedPatientList")
	public @ResponseBody
	BaseDataVo queryBedPatientList(
			@RequestParam("workUnitType") Integer workUnitType,
			@RequestParam("workUnitCode") String workUnitCode,String outFlag) throws Throwable {
		BaseDataVo json = new BaseDataVo();
		//Set<String> selectedBedPatSet = new HashSet<String>();

		BedPatientInfoResult result = getBedPatientList(workUnitType,workUnitCode);
		/*if (result != null && !result.getBedInfoList().isEmpty()) {
			// 选择全科为关注信息
			for (BedPatientInfo bedInfo : result.getBedInfoList()) {
				selectedBedPatSet.add(bedInfo.getBedCode());
			}
		}*/
		
		json.setRslt(ResultCst.SUCCESS);
		json.setData(result);

		return json;
	}
	
	/**
	 * 
	 * 根据当前登录信息，获取三天内出院病人列表
	 * 
	 * @param workUnitType
	 *            类型
	 * @param workUnitCode
	 *            部门编码
	 * @return
	 */
	@RequestMapping("/queryOutPatientList")
	public @ResponseBody BaseDataVo queryOutPatientList(
			@RequestParam("workUnitType") Integer workUnitType,
			@RequestParam("workUnitCode") String workUnitCode) throws Throwable {
		BaseDataVo json = new BaseDataVo();
		// Set<String> selectedBedPatSet = new HashSet<String>();

		BedPatientInfoResult result = new BedPatientInfoResult();
		result.setWorkUnitStatistics(new WorkUnitStat());
		result.setBedInfoList(new ArrayList<BedPatientInfo>());
		
		BedPatientInfo emptyPat = new BedPatientInfo();
		emptyPat.setPatientId("");
		emptyPat.setShowBedCode("");
		emptyPat.setBedCode("");
		emptyPat.setPatientName("");
		result.getBedInfoList().add(emptyPat);

		List<Patient> list = patientService.getTransferPatientList(workUnitType,
				workUnitCode);
		if (list != null) {
			for (Iterator<Patient> iterator = list.iterator(); iterator
					.hasNext();) {
				Patient patient = (Patient) iterator.next();
				BedPatientInfo pat = new BedPatientInfo();
				pat.setPatientId(patient.getPatId());
				pat.setShowBedCode(patient.getBedCode());
				pat.setBedCode(patient.getBedCode());
				pat.setPatientName(patient.getName());
				result.getBedInfoList().add(pat);
			}
		}

		json.setRslt(ResultCst.SUCCESS);
		json.setData(result);

		return json;
	}

	/**
	 * 
	 * 根据当前登录信息，获取床位列表(根据配置，显示是否显示空床),不返回床位列表
	 * 
	 * @param workUnitType
	 *            类型
	 * @param workUnitCode
	 *            部门编码
	 * @return
	 */
	@RequestMapping("/queryDeptSummary")
	public @ResponseBody
	BaseDataVo queryDeptSummary(
			@RequestParam("workUnitType") Integer workUnitType,
			@RequestParam("workUnitCode") String workUnitCode) {
		BaseDataVo vo = new BaseDataVo();
		vo.setRslt(ResultCst.SUCCESS);
		vo.setData(patientService.getPatientStatisticByDeptCode(workUnitCode));
		return vo;
	}

	/**
	 * 
	 * 保存修改后的患者信息，允许修改名字、床号、性别、年龄、出生日期、地址、电话、联系人、关系、诊断、饮食、护理级别、过敏史、 评估、付费方式、备注
	 * 
	 * @return
	 */
	@RequestMapping("/updatePatInfo")
	public @ResponseBody
	BaseVo updatePatInfo(Patient inPatient, String allergyListStr, String admitDiagStr) {
		BaseVo vo = new BaseVo();

		// 从当前session获取用户相关信息
		UserInformation userInfo = WebContextUtils.getSessionUserInfo();
		PatientAllergy[] allergyArray = null;
		PatientDiagnosis[] diagArray = null;
		if (StringUtils.isNotEmpty(allergyListStr)) {
			allergyArray = GsonUtils.fromJson(allergyListStr, PatientAllergy[].class);
		}
		if (StringUtils.isNotEmpty(admitDiagStr)) {
			diagArray = GsonUtils.fromJson(admitDiagStr, PatientDiagnosis[].class);
		}
		
		try{
			patientService.updatePatientInfo(inPatient, allergyArray, diagArray, userInfo.getCode());
			vo.setRslt(ResultCst.SUCCESS);
		}catch(Exception e){
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.FAILURE);
		}

		return vo;
	}

	/**
	 * 根据设置的出生日期，获取年龄
	 * 
	 * @param birthday
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getPatientAge")
	public @ResponseBody
	BaseVo getPatientAge(String birthday) throws Exception {
		BaseDataVo vo = new BaseDataVo();

		if (StringUtils.isNotEmpty(birthday)) {
			//Date birthDayDate = DateUtil.parse(birthday, DateFormat.YMD);
			// vo.setData(AgeEntity.calculateAge(birthDayDate).getAgeString());
			vo.setRslt(ResultCst.SUCCESS);
		} else {
			vo.setMsg("出生日期为空!");
			vo.setRslt(ResultCst.FAILURE);
		}

		return vo;
	}
	
	/**
	 * 获取病区统计一览信息
	 * @param token
	 * @param workUnitCode
	 * @param dutyNurseId
	 * @return
	 */
	@RequestMapping(value = "/getWorkUnitStatistics")
	public @ResponseBody 
	BaseDataVo getWorkUnitStatistics(String token, String workUnitCode, String dutyNurseId){
		BaseDataVo vo = new BaseDataVo();
		//List<BedEntity> bedBaseList = patientService.getBedPatientList(MnisConstants.WORK_UNIT_DEPT, workUnitCode);
		WorkUnitStat stat = patientService.getPatientStatisticByDeptCode(workUnitCode);

		vo.setData(stat);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	

	/**
	 * Nda端取床位列表
	 * @param token
	 * @param workUnitType
	 * @param workUnitCode
	 * @param showEmptyBeds
	 * @return
	 */
	@RequestMapping(value = "/getBedPatientList")
	public @ResponseBody 
	BaseMapVo getBedPatientList(Integer workUnitType,
			String workUnitCode, String nurseId, boolean showEmptyBeds){
		BaseMapVo vo = new BaseMapVo();
		
		List<Patient> list = patientService.getWardPatientList(workUnitType, workUnitCode);
		vo.addData("bedBaseList", list);
		// workUnitCode 为deptCode???
		List<String> ids = patientService.getAttention(nurseId, workUnitCode);
		List<HashMap<String,String>> resultIds = new ArrayList<HashMap<String,String>>();
		for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
			String id = (String) iterator.next();
			HashMap<String,String> entity = new HashMap<String,String>();
			entity.put("patientId", id);
			resultIds.add(entity);
		}
		vo.addData(BaseVo.VO_KEY, resultIds);
		vo.setRslt(ResultCst.SUCCESS);
		
		return vo;
	}
	
	/**
	 * NDA端获取患者信息
	 * @param token
	 * @param patientId
	 * @return
	 */
	@RequestMapping(value = "/getInpatientInfo")
	public @ResponseBody 
	BaseVo getInpatientInfo(String token, String patientId){
		BaseDataVo vo = new BaseDataVo(); 
		
		Patient patient = patientService.getPatientByPatId(patientId);
		//patientService.formatPatientTime(patient);
		
/*		List<String> allergyList = patient.getAllergen();
		
		for (Iterator<String> iterator = allergyList.iterator(); iterator.hasNext();) {
			String allergy = (String) iterator
					.next();
			if(allergy == null || allergy.contains("-") || allergy.contains("—")){
				iterator.remove();
			}
			
		}*/
		
		vo.setData(patient);
		vo.setRslt(ResultCst.SUCCESS);
		
		return vo;
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
	// 可以考虑移除该方法，莫名其妙
	private BedPatientInfoResult getBedPatientList(Integer workUnitType,
			String workUnitCode) {
		List<Patient> list = patientService.getWardPatientList(workUnitType, workUnitCode);
		
		// stat查询出错，之后在调整
		WorkUnitStat stat = patientService.getPatientStatisticByDeptCode(workUnitCode);

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

		String currentMinDate = DateUtil.format(DateUtil.getCurDateWithMinTime(), DateFormat.YMDHM);
		String currentMaxDate = DateUtil.format(DateUtil.getCurDateWithMaxTime(), DateFormat.YMDHM);

		BedPatientInfo bedPatientInfo = null;
		if (list != null && list.size() > 0) {
			for (Patient patient : list) {
				bedPatientInfo = new BedPatientInfo();
				bedPatientInfo = PatientUtils.copyBedPatientInfoProperties(patient, currentMinDate, currentMaxDate);
				bedPatList.add(bedPatientInfo);
			}
		}

		rst.setBedInfoList(bedPatList);
		rst.setWorkUnitStatistics(stat);
		return rst;
	}


	private void formatPatientTime(Patient inpatient) {
		if (inpatient == null) {
			return;
		}
		inpatient.setInDate( inpatient.getInDate());
		inpatient.setBirthday(inpatient.getBirthday());
		inpatient.setSurgeryDate(
				DateUtil.truncateDateString(inpatient.getSurgeryDate(), DateFormat.YMD));
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
	
	/**
	 * NDA端   床位管理_获取病床列表信息
	 * @param token
	 * @param workUnitCode
	 * @param nurseCode
	 * @param tendLevel
	 * @param strDate 查询日期
	 * @return
	 */
	@RequestMapping("/getWardPatrolPlan")
	@ResponseBody
	public BaseMapVo getWardPatrolPlan(
			@RequestParam(value = "token", required = false) String token,
			@RequestParam(value = "workUnitCode", required = false) String workUnitCode, 
			@RequestParam(value = "nurseCode", required = false) String nurseCode, 
			@RequestParam(value = "tendLevel", required = false) String tendLevel,
			@RequestParam(value = "date", required = false) String strDate){
		BaseMapVo vo = new BaseMapVo();
		vo.addData(BaseVo.VO_KEY,  wardPatrolService.getWardPatrolPlan(workUnitCode, nurseCode,tendLevel, strDate));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * NDA端  床位管理_保存病床列表
	 * @param token
	 * @param nurseCode 执行护士ID
	 * @param deptCode 部门ID
	 * @param barcode 病人ID
	 * @return
	 */
	@RequestMapping("/saveWardPatrolInfo")
	@ResponseBody
	public BaseDataVo saveWardPatrolInfo(
			@RequestParam(value = "token", required = true) String token,
			@RequestParam(value = "nurseCode", required = false) String nurseCode, 
			@RequestParam(value = "deptCode", required = false) String deptCode, 
			@RequestParam(value = "barcode", required = false) String patientId){
		BaseDataVo  vo = new BaseDataVo ();
		
		WardPatrolInfo wardPatrolInfo = wardPatrolService.saveWardPatrolInfo(nurseCode, deptCode, patientId);
		if(wardPatrolInfo!=null){
			vo.setData(wardPatrolInfo);
			vo.setRslt(ResultCst.SUCCESS);
		}else{
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("病人不存在");
		}
		return vo;
	}

	/**
	 * NDA护理单据_获取病房巡视单数据
	 * @param token
	 * @param patId
	 * @param day
	 */
	@RequestMapping("/getWardPatrolLogByPatId")
	@ResponseBody
	public BaseMapVo getWardPatrolLogByPatId(String token, String patientId, String day){
		BaseMapVo vo = new BaseMapVo();
		vo.addData(BaseVo.VO_KEY, wardPatrolService.getWardPatrolLogByPatId(patientId, day));
		vo.setRslt(ResultCst.SUCCESS);
		
		return vo;
	}
	
	/**
	 * 用于盛京演示时清除测试数据
	 */
	@RequestMapping(value = "/deleteTestDate")
	@ResponseBody
	public void deleteTestDate(){
		taskRepository.deleteForShow();
	}
	
	/**
	 * 保存打印信息
	 * @param patientPrint
	 * @return
	 */
	@RequestMapping("/savePatientPrint")
	@ResponseBody
	public BaseDataVo savePatientPrint(String patientPrints,boolean isBarcode ){
		BaseDataVo vo = new BaseDataVo();
		

		if (StringUtils.isBlank(patientPrints)) {
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("传入参数不能为空");
			return vo;
		}
		
		Type type = new TypeToken<List<PatientPrint>>(){}.getType();
		List<PatientPrint> patPrints = GsonUtils.fromJson(patientPrints, type);		
		
		try {
			if(patientService.savePatientPrints(patPrints,isBarcode) > 0){
				vo.setRslt(ResultCst.SUCCESS);
			}else{
				vo.setRslt(ResultCst.FAILURE);
				vo.setMsg("打印信息保存失败!");
			}
		} catch (MnisException e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
		}
		return vo;
	}
}
