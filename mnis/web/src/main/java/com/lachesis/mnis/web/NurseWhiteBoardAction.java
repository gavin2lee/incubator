package com.lachesis.mnis.web;

import static com.lachesis.mnis.core.util.GsonUtils.fromJson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.board.dto.NwbRecordDto;
import com.lachesis.mnis.board.entity.NwbMetadata;
import com.lachesis.mnis.board.entity.NwbRecord;
import com.lachesis.mnis.board.nurse.service.NurseWhiteBoardNurseService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.NurseWhiteBoardService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.PatientBaseInfo;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.core.util.SuperCacheUtil;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardInBo;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadata;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardMetadataDic;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecord;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardRecordItem;
import com.lachesis.mnis.core.whiteBoard.entity.NurseWhiteBoardTemplate;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.PatientUtils;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;
import com.lachesis.mnis.web.vo.BedPatientInfo;
import com.lachesis.mnis.web.vo.BedPatientInfoResult;

/**
 * 小白板,任务清单
 * 
 * @author ThinkPad
 *
 */
@Controller
@RequestMapping("/nur/nurseWhiteBoard")
public class NurseWhiteBoardAction {
	static final Logger log = Logger.getLogger(NurseWhiteBoardAction.class);
	@Autowired
	private NurseWhiteBoardService nurseWhiteBoardService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private NurseWhiteBoardNurseService nurseWhiteBoardNurseService;

	/**
	 * PC端进入小白板编辑主界面
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/noteEdit")
	public String noteEdit(ModelMap modelMap, HttpServletRequest request)
			throws Exception {
		// TODO 传入界面需要的数据
		return "/nur/noteEdit";
	}
	
	@RequestMapping(value = "/noteShow")
	public String noteShow(ModelMap model, HttpServletRequest request)
			throws Exception {
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
		Date date = new Date();
		String today = DateUtil.format(date, DateFormat.YMDC) + MnisConstants.EMPTY + DateUtil.getWeek(date);
		model.addAttribute("today", today);
		model.addAttribute("currentMinDate", DateUtil.format(DateUtil.getCurDateWithMinTime(), DateFormat.YMDHM));
		model.addAttribute("currentMaxDate", DateUtil.format(DateUtil.getCurDateWithMaxTime(), DateFormat.YMDHM));
		
		// TODO 传入界面需要的数据
		return "/nur/noteShow";
	}
	
	@RequestMapping(value = "/nurseWhiteBoardMain")
	public String nurseWhiteBoardMain(ModelMap modelMap, HttpServletRequest request)
			throws Exception {
		// TODO 传入界面需要的数据
		return "/nur/nurseWhiteBoardMain";
	}
	
	/**
	 * 进入小白板一览界面
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/nurseWhiteBoardShowMain")
	public String nurseWhiteBoardShowMain(ModelMap model, HttpServletRequest request)
			throws Exception {
		
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
				
				model.addAttribute("deptCode", userInfo.getDeptCode());
				model.addAttribute("deptName", userInfo.getDeptName());
				model.addAttribute("user", userInfo);
				model.addAttribute("hospitalName", identityService.getHospitalName());
			}
		}
		Date date = new Date();
		String today = DateUtil.format(date, DateFormat.YMDC) + MnisConstants.EMPTY + DateUtil.getWeek(date);
		model.addAttribute("today", today);
		model.addAttribute("currentMinDate", DateUtil.format(DateUtil.getCurDateWithMinTime(), DateFormat.YMDHM));
		model.addAttribute("currentMaxDate", DateUtil.format(DateUtil.getCurDateWithMaxTime(), DateFormat.YMDHM));
		
		
		// TODO 传入界面需要的数据
		return "/nur/nurseWhiteBoardShowMain";
	}
	/**********************元数据***************************************/
	/**
	 * 根据部门和指定item类型获取元数据
	 * 
	 * @param deptCode
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getNurseWhiteBoardMetadatas")
	@ResponseBody
	public BaseMapVo getNurseBoardWhiteMetadatas(String deptCode, String code) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = nurseWhiteBoardService
				.getNurseWhiteBoardMetadatas(deptCode, code,false);

		Map<String,Object> metaValues = nurseWhiteBoardService.getNurseWhiteBoardMetaValues();
		vo.addData("list", nurseWhiteBoardMetadatas);
		vo.addData("freq", metaValues);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 根据部门和指定item类型获取元数据
	 * 
	 * @param deptCode
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getNurseWhiteBoardDatas")
	@ResponseBody
	public BaseMapVo getNurseWhiteBoardDatas(String deptCode, String code,boolean isCache) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = nurseWhiteBoardService
				.getNurseWhiteBoardMetadatas(deptCode, code,false);

		Map<String,Object> metaValues = nurseWhiteBoardService.getNurseWhiteBoardMetaValues();
		
		Date date = new Date();
		String dateStr = DateUtil.format(date,DateFormat.FULL);
		//上一次查询时间
		String preQueryOrderDate =(String) SuperCacheUtil.CACHE_DATA.
				get(MnisConstants.QUERY_NWB_DATE + deptCode);
		
		if( null == preQueryOrderDate){
			preQueryOrderDate = dateStr;
		}
		SuperCacheUtil.CACHE_DATA.put(MnisConstants.PRE_QUERY_NWB_DATE + deptCode, 
				preQueryOrderDate);
		
		SuperCacheUtil.CACHE_DATA.put(MnisConstants.QUERY_NWB_DATE + deptCode, 
				dateStr);
		//缓存查询时间
		if(isCache){
			SuperCacheUtil.CACHE_DATA.put(MnisConstants.QUERY_EDIT_NWB_DATE + deptCode, dateStr);
		}
		
		List<NurseWhiteBoardRecord> nurseWhiteBoardRecords = nurseWhiteBoardService
				.getNurseWhiteBoardDynamicRecords(deptCode,code);
		
		String today = DateUtil.format(date, DateFormat.YMDC) + MnisConstants.EMPTY + DateUtil.getWeek(date);
		vo.addData("metadatas", nurseWhiteBoardMetadatas);
		vo.addData("freq", metaValues);
		vo.addData("records", nurseWhiteBoardRecords);
		vo.addData("today", today);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}


	/**
	 * 根据部门和code获取最顶层元数据
	 * 
	 * @param deptCode
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getNurseWhiteBoardTopMetadatas")
	@ResponseBody
	public BaseMapVo getNurseWhiteBoardTopMetadatas(String deptCode, String code) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = nurseWhiteBoardService
				.getNurseWhiteBoardTopMetadatas(deptCode, code);

		vo.addData("list", nurseWhiteBoardMetadatas);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 获取基本元数据
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getBaseDataByDeptCode")
	@ResponseBody
	public BaseMapVo getBaseDataByDeptCode(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		//频次
		Map<String,Object> metaValues = nurseWhiteBoardService.getNurseWhiteBoardMetaValues();
		//患者基本信息
		List<PatientBaseInfo> patientBaseInfos = patientService.getPatientBaseInfoByDeptCode(deptCode,null);
		//子项信息
		Map<String,Object> bodySingMeta = nurseWhiteBoardService.getSubMetadatas(deptCode);
		vo.addData("patient", patientBaseInfos);
		vo.addData("freq", metaValues);
		vo.addData("item", bodySingMeta);
		return vo;
		
	}
	/**********************小白板记录***************************************/
	
	/**
	 * 根据部门和时间获取记录
	 * 
	 * @param deptCode
	 * @param recordDate
	 * @return
	 */
	@RequestMapping(value = "/getNurseWhiteBoardRecords")
	@ResponseBody
	public BaseMapVo getNurseWhiteBoardRecords(String deptCode,String code, String nurseCode,
			String recordDate) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		//频次
		Map<String,Object> metaValues = nurseWhiteBoardService.getNurseWhiteBoardMetaValues();
		List<NurseWhiteBoardRecord> nurseWhiteBoardRecords = nurseWhiteBoardService
				.getNurseWhiteBoardDynamicRecords(deptCode,code);

		vo.addData("list", nurseWhiteBoardRecords);
		vo.addData("freq", metaValues);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 根据部门和时间获取任务清单数据
	 * 
	 * @param deptCode
	 * @param recordDate
	 * @return
	 */
	@RequestMapping(value = "/getNurseWhiteBoardRecordItems")
	@ResponseBody
	public BaseMapVo getNurseWhiteBoardRecordItems(String deptCode,
			String recordDate) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		List<NurseWhiteBoardRecordItem> nurseWhiteBoardRecordItems = nurseWhiteBoardService
				.getNurseWhiteBoardRecordItems(deptCode, recordDate);

		vo.addData("list", nurseWhiteBoardRecordItems);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 保存一个小白板项目记录
	 * @param recordInfo NurseWhiteBoardInBo实体
	 * @return
	 */
	@RequestMapping(value = "/saveNurseWhiteBoardRecord")
	@ResponseBody
	public BaseMapVo saveNurseWhiteBoardRecord(String recordInfo,boolean isOnlySave) {
		BaseMapVo vo = new BaseMapVo();
		try{
			log.debug(recordInfo);
			if (StringUtils.isBlank(recordInfo)) {
				throw new MnisException("参数不能为空!");
			}
			//获取传入的数据对象
			NurseWhiteBoardInBo inbo = GsonUtils.fromJson(recordInfo,NurseWhiteBoardInBo.class);
			//session信息
			UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
			nurseWhiteBoardService.saveNurseWhiteBoardRecord(inbo,isOnlySave,sessionUserInfo.getUser(),sessionUserInfo.getDeptCode());
	
			vo.setRslt(ResultCst.SUCCESS);
			vo.setMsg("保存成功!");
		}catch (MnisException e) {
			log.error(e);
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
		}catch (Exception e) {
			log.error(e);
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("保存失败!");
		}
		return vo;
	}
	
	/**
	 * 保存多个小白板项目记录
	 * @param recordInfo NurseWhiteBoardInBo实体
	 * @return
	 */
	@RequestMapping(value = "/batchSaveNurseWhiteBoardRecord")
	@ResponseBody
	public BaseMapVo batchSaveNurseWhiteBoardRecord(String recordInfo,boolean isOnlySave) {
		BaseMapVo vo = new BaseMapVo();
		try{
			log.debug(recordInfo);
			if (StringUtils.isBlank(recordInfo)) {
				throw new MnisException("参数不能为空!");
			}
			Type type = new TypeToken<List<NurseWhiteBoardInBo>>(){}.getType();
			//获取传入的数据对象
			List<NurseWhiteBoardInBo> nurseWhiteBoardInBos = fromJson(recordInfo, type);
			UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
			nurseWhiteBoardService.batchSaveNurseWhiteBoardRecord(nurseWhiteBoardInBos,isOnlySave,sessionUserInfo.getUser(),sessionUserInfo.getDeptCode());
	
			vo.setRslt(ResultCst.SUCCESS);
			vo.setMsg("保存成功!");
		}catch (MnisException e) {
			log.error(e);
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
		}catch (Exception e) {
			log.error(e);
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("保存失败!");
		}
		return vo;
	}
	
	/**
	 * 小白板任务执行功能
	 * @param recordId 小白板记录Id
	 * @return
	 */
	@RequestMapping(value = "/execWhiteBoardRecord")
	@ResponseBody
	public BaseVo execWhiteBoardRecord(String recordId){
		BaseVo outVo = new BaseVo();
		try{
			if(StringUtils.isEmpty(recordId)){
				throw new MnisException("请传入要执行的任务!");
			}
			/*nurseWhiteBoardService.execWhiteBoard(recordInfoList);*/
			nurseWhiteBoardService.execWhiteBoardItem(recordId);
			outVo.setRslt(ResultCst.SUCCESS);
		}catch (MnisException e) {
			log.error(e);
			outVo.setMsg(e.getMessage());
			outVo.setRslt(ResultCst.ALERT_ERROR);
		}catch (Exception e) {
			log.error(e);
			outVo.setMsg("操作失败!");
			outVo.setRslt(ResultCst.ALERT_ERROR);
		}
		return outVo;
	}
	
	
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
				bedPatientInfo = PatientUtils.copyBedPatientInfoProperties(patient, currentMinDate, currentMaxDate);
				bedPatList.add(bedPatientInfo);
			}
		}

		rst.setBedInfoList(bedPatList);
		rst.setWorkUnitStatistics(stat);
		return rst;
	}
	/******************模板编辑*************************/
	/**
	 * 进入模板编辑界面
	 * @param modelMap
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/nurseWhiteBoardConfig")
	public String nurseWhiteBoardConfig(ModelMap modelMap, HttpServletRequest request)
			throws Exception {
		// TODO 传入界面需要的数据
		UserInformation userInformation = WebContextUtils.getSessionUserInfo();
		if(null != userInformation){
			modelMap.addAttribute("deptCode", userInformation.getDeptCode());
			modelMap.addAttribute("deptName", userInformation.getDeptName());
			modelMap.addAttribute("user", userInformation);
			modelMap.addAttribute("hospitalName", identityService.getHospitalName());
		}
		
		return "/nur/nurseWhiteBoardConfig";
	}
	
	/**
	 * 获取元数据项目配置信息
	 * @param deptCode
	 * @return
	 */
	@RequestMapping(value="/getNwbMetadataDics")
	@ResponseBody
	public BaseMapVo getNwbMetadataDics(String deptCode,String templateId){
		BaseMapVo vo = new BaseMapVo();
		List<NurseWhiteBoardMetadataDic> nwbMetadataDics = new ArrayList<NurseWhiteBoardMetadataDic>();
		List<NurseWhiteBoardTemplate> templates = new ArrayList<NurseWhiteBoardTemplate>();
		NurseWhiteBoardTemplate template = null;
		HashMap<String, Object> templateMap = new HashMap<String, Object>();
		try {
			templates = nurseWhiteBoardService.getNwbTemplates(deptCode, templateId, false);
			for (NurseWhiteBoardTemplate nurseWhiteBoardTemplate : templates) {
				templateMap.put(nurseWhiteBoardTemplate.getId(), nurseWhiteBoardTemplate.getTitle());
			}
			if(templates.size() > 0){
				template = templates.get(0);
			}
			nwbMetadataDics = nurseWhiteBoardService.getNwbMetadataDics(deptCode);
			
		} catch (Exception e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
		}
		
		vo.setRslt(ResultCst.SUCCESS);
		vo.addData("data", nwbMetadataDics);
		vo.addData("template", template);
		vo.addData("templateSelect", templateMap);
		return vo;
	}
	
	/**
	 * 保存元数据
	 * @param nwbMetadata
	 * @return
	 */
	@RequestMapping(value = "/saveNwbMetadata")
	@ResponseBody
	public BaseMapVo saveNwbMetadata(String nwbMetadata,boolean isAddRow){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(nwbMetadata)){
			vo.setMsg("保存信息为空!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		
		NurseWhiteBoardMetadata nurseWhiteBoardMetadata = fromJson(nwbMetadata, NurseWhiteBoardMetadata.class);
		if(null == nurseWhiteBoardMetadata){
			vo.setMsg("保存信息为空!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		
		int count = nurseWhiteBoardService.saveNwbMetadata(nurseWhiteBoardMetadata,isAddRow);
		
		if(count > 0){
			vo.addData("data",nurseWhiteBoardService.getNurseWhiteBoardMetadatas(nurseWhiteBoardMetadata.getDeptCode(),null,true));
		}
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("保存成功!");
		return vo;
	}
	
	@RequestMapping(value="/batchSaveNwbMetadatas")
	@ResponseBody
	public BaseMapVo batchSaveNwbMetadatas(String nwbMetadatas,String template){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(nwbMetadatas)){
			vo.setMsg("传入数据为空");
			vo.setRslt(ResultCst.FAILURE);
		}
		
		try {
			Type type = new TypeToken<List<NurseWhiteBoardMetadata>>(){}.getType();
			List<NurseWhiteBoardMetadata> nurseWhiteBoardMetadatas = fromJson(nwbMetadatas, type);
			NurseWhiteBoardTemplate nurseWhiteBoardTemplate = null;
			if(null != template){
				nurseWhiteBoardTemplate = fromJson(template, NurseWhiteBoardTemplate.class);
			}
			nurseWhiteBoardService.batchSaveNwbMetadata(nurseWhiteBoardMetadatas,nurseWhiteBoardTemplate,false);
			//缓存tv
			nurseWhiteBoardService.getNurseWhiteBoardTVMetadatas(nurseWhiteBoardMetadatas.get(0).getDeptCode(), null, true);
			vo.addData("data",nurseWhiteBoardService.getNurseWhiteBoardMetadatas(nurseWhiteBoardMetadatas.get(0).getDeptCode(),null,true));
			vo.setMsg("保存成功");
			vo.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.FAILURE);
		}
		return vo;
	}
	
	/**
	 * 修改元数据
	 * @param nwbMetadata
	 * @return
	 */
	@RequestMapping(value = "/updateNwbMetadata")
	@ResponseBody
	public BaseMapVo updateNwbMetadata(String nwbMetadata){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(nwbMetadata)){
			vo.setMsg("保存信息为空!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		
		NurseWhiteBoardMetadata nurseWhiteBoardMetadata = fromJson(nwbMetadata, NurseWhiteBoardMetadata.class);
		if(null == nurseWhiteBoardMetadata){
			vo.setMsg("修改信息为空!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		
		int count = nurseWhiteBoardService.updateNwbMetadata(nurseWhiteBoardMetadata);
		
		if(count > 0){
			//缓存tv
			nurseWhiteBoardService.getNurseWhiteBoardTVMetadatas(nurseWhiteBoardMetadata.getDeptCode(), null, true);
			vo.addData("data",nurseWhiteBoardService.getNurseWhiteBoardMetadatas(nurseWhiteBoardMetadata.getDeptCode(),null,true));
		}
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("修改成功!");
		return vo;
	}
	
	/**
	 * 根据id删除元数据
	 * @param nwbMetadata
	 * @return
	 */
	@RequestMapping(value = "/deleteNwbMetadataById")
	@ResponseBody
	public BaseMapVo deleteNwbMetadataById(String deptCode,String id){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(id)){
			vo.setMsg("删除信息为空!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		
		int count = nurseWhiteBoardService.deleteNwbMetadataById(id);
		
		if(count > 0){
			nurseWhiteBoardService.getNurseWhiteBoardTVMetadatas(deptCode, null, true);
			vo.addData("data",nurseWhiteBoardService.getNurseWhiteBoardMetadatas(deptCode,null,true));
		}
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("保存成功!");
		return vo;
	}
	
	
	
	
	/****************************新接口*******************************/
	
	/**
	 * 根据科室和模板获取小白板数据
	 * @param deptCode
	 * @param templateId
	 * @return
	 */
	@RequestMapping(value = "/getNwbRecordDtos")
	@ResponseBody
	public BaseMapVo getNwbRecordDtos(String deptCode, String templateId) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		List<NwbRecordDto> list = nurseWhiteBoardNurseService.getNwbRecordDtos(deptCode, templateId);
		if(null != list && list.size() != 0){
			Map<String,Object> map = new HashMap<String,Object>();
			for (NwbRecordDto nwbRecordDto : list) {
				map.put(nwbRecordDto.getName(), nwbRecordDto.getNwbRecordItemDtos());
			}
			vo.addData("map", map);
			vo.setRslt(ResultCst.SUCCESS);
		}
		return vo;
	}
	
	/**
	 * 获取所有指定部门的模板id
	 * @param deptCode
	 * @return
	 */
	@RequestMapping(value = "/queryTempIdByDeptCode")
	@ResponseBody
	public BaseMapVo queryTempIdByDeptCode(String deptCode) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			throw new AlertException("部门参数不能为空!");
		}
		vo.addData("result", nurseWhiteBoardNurseService.queryTempIdByDeptCode(deptCode));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 保存元数据（new）
	 * @param nwbMetadatas
	 * @return
	 */
	@RequestMapping(value = "/saveNwbMetadataNew")
	@ResponseBody
	public BaseMapVo saveNwbMetadataNew(String nwbMetadatas){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(nwbMetadatas)){
			vo.setMsg("保存信息为空!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		Type type = new TypeToken<List<NwbMetadata>>(){}.getType();
		List<NwbMetadata> list = fromJson(nwbMetadatas, type);
		nurseWhiteBoardNurseService.batchSaveNwbMetadata(list);
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("保存成功!");
		return vo;
	}
	
	/**
	 * 更新元数据（new）
	 * @param nwbMetadatas
	 * @return
	 */
	@RequestMapping(value = "/updateNwbMetadataNew")
	@ResponseBody
	public BaseMapVo updateNwbMetadataNew(String recodes){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(recodes)){
			vo.setMsg("更新信息为空!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		Type type = new TypeToken<NwbMetadata>(){}.getType();
		NwbMetadata nwbMetadata = fromJson(recodes, type);
		nurseWhiteBoardNurseService.saveNwbMetadata(nwbMetadata);
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("更新成功!");
		return vo;
	}
	
	/**
	 * 根据id删除元数据（new）
	 * @param metadataId 元数据id
	 * @return
	 */
	@RequestMapping(value = "/deleteNwbMetadata")
	@ResponseBody
	public BaseMapVo deleteNwbMetadata(String metadataId){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(metadataId)){
			vo.setMsg("删除信息为空!");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		nurseWhiteBoardNurseService.deleteNwbMetadata(metadataId);
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("删除成功!");
		return vo;
	}
	
	/**
	 * 获取本地与his的code对应信息
	 * @return
	 */
	@RequestMapping(value = "/queryAllWhiteBoardItem")
	@ResponseBody
	public BaseMapVo queryAllWhiteBoardItem() {
		BaseMapVo vo = new BaseMapVo();
		vo.addData("date", nurseWhiteBoardNurseService.queryAllWhiteBoardItem());
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 录入本地小白板数据
	 * @param nwbRecords
	 * @return
	 */
	@RequestMapping(value = "/saveNwbRecord")
	@ResponseBody
	public BaseMapVo saveNwbRecord(String nwbRecords){
		BaseMapVo vo = new BaseMapVo();
		if(!StringUtil.hasValue(nwbRecords)){
			vo.setMsg("未检测到传入信息！");
			vo.setRslt(ResultCst.FAILURE);
			return vo;
		}
		Type TypeOne = new TypeToken<List<NwbRecord>>(){}.getType();
		List<NwbRecord> nwbRecordList = fromJson(nwbRecords, TypeOne);
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		for (NwbRecord nwbRecord : nwbRecordList) {
			nwbRecord.setNurseCode(sessionUserInfo.getUser().getCode());
			nwbRecord.setNurseName(sessionUserInfo.getUser().getName());
			nwbRecord.setCreateTime(new Date());
			nurseWhiteBoardNurseService.saveNwbRecord(nwbRecord);
		}
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
}