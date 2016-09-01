/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import static com.lachesis.mnis.core.util.DateUtil.parse;
import static com.lachesis.mnis.core.util.GsonUtils.fromJson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.bodysign.BodySignConstants;
import com.lachesis.mnis.core.bodysign.BodySignUtil;
import com.lachesis.mnis.core.bodysign.entity.BodySignItem;
import com.lachesis.mnis.core.bodysign.entity.BodySignItemConfig;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.bodysign.entity.BodySignSpeedy;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseListVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;

/***
 * 
 * 生命体征查询及录入
 *
 * @author yuliang.xu
 * @date 2015年6月4日 下午2:33:57 
 *
 */
@Controller
@RequestMapping("/nur/bodySign")
public class BodySignRecordAction  {
	static final Logger LOGGER = LoggerFactory.getLogger(BodySignRecordAction.class);

	@Autowired
	private PatientService patientService;
	
	@Autowired
	private BodySignService bodySignService;
	
	@Autowired
	private IdentityService identityService;

/*	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtil.FORMAT_YMD, true));
		binder.registerCustomEditor(String.class, "date", new CustomDateEditor(DateUtil.FORMAT_YMD, true));
	}*/
	
	/**
	 * 跳转到体温单录入界面
	 * @return
	 */
	@RequestMapping("/bodyTempSheet")
	public String forwardBodyTempSheet(){		
		return "/nur/bodyTempSheet";
	}
	
	/**
	 * 跳转到体温单查看页面
	 * @return
	 */
	@RequestMapping("/showBodyTempSheet")
	public String forwardShowBodyTempSheet(){		
		return "/nur/showBodyTempSheet";
	}
	
	@RequestMapping("/showBodyTempSheetPDF")
	public String forwardShowBodyTempSheetPDF(){		
		return "/nur/showBodyTempSheetPDF";
	}

	/**
	 * 根据患者信息，查询患者本周体温表表单
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/{id}/bodyTempSheetPdf")
	public String bodyTempSheetPdf(@PathVariable String id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("offsetWeek", 0);
		return "/nur/bodyTempSheetPdf";
	}

	/**
	 * 根据患者编号及日期，查询生命体征记录
	 * 
	 * @param id  患者编号
	 * @param date 查询日期
	 * @param isSpecialDay 指定时间
	 * @return
	 */
	@RequestMapping("/getBodySignRecord")
	@ResponseBody
	public BaseMapVo getBodySignRecord(String id, @RequestParam String date,boolean isSpecialDay){
		LOGGER.debug(date);
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(id)) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("病人编号不能为空！");
			return vo;
		}
		
		Date day = null;
		if(StringUtils.isBlank(date)){
			day = new Date();
		}else{
			day = parse(date);
		}
		
		vo.addData("lst", bodySignService.getBodySignRecord(day, id.split(MnisConstants.LINE),isSpecialDay));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 
	 * 保存随意时间点体征数据
	 * 
	 * @param id  患者编号
	 * @param boySign 生命体征信息
	 * @return
	 */
	@RequestMapping("/saveBodySignRecord")
	public @ResponseBody
	BaseDataVo saveBodySignRecord(String item){
		BaseDataVo vo = new BaseDataVo();
		LOGGER.error("saveBodySignRecord->" + item);
		//需增加 deptCode, bedCode
		BodySignRecord record =  fromJson(item, BodySignRecord.class);
		
		if(record == null){
			vo.setMsg("json解析失败!");
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			vo.setData(null);
			return vo;
		}
		
		if (!record.hasData() && StringUtils.isBlank(record.getRemark())) {
			vo.setMsg("没有数据");
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			return vo;
		}
		
		bodySignService.updateBodySignRecordUser(record, WebContextUtils.getSessionUserInfo());
		try{
			bodySignService.saveBodySignRecord(record,true);
		}catch(Exception e){
			LOGGER.error("", e);
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.BODY_SIGN_ERROR);
			return vo;
		}
		
		vo.setMsg("生命体征保存成功");
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 根据日期，患者ID查询体温单
	 * 
	 * @param id 患者编号
	 * @param date 查询日期
	 * @param week 查询周偏移量 -1:上周 0:本周 1:下周
	 * @return
	 */
	@RequestMapping(value = "/getBodyTempSheet")
	@ResponseBody
	public BaseDataVo getBodyTempSheet(String id, String date, Integer week)  {
		BaseDataVo vo = new BaseDataVo();
		if (date == null || StringUtils.isEmpty(id)) {
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("查询日期或患者编号不能为空");
			return vo;
		}
		
		Patient patient = patientService.getPatientByPatId(id);
		if (patient == null) {
			vo.setRslt(ResultCst.SYSCONFIG_INVALID_CONFIG);
			vo.setMsg("此患者不存在");
			return vo;
		}
		
		vo.setData(bodySignService.getBodyTempSheet(parse(date), patient, week == null ? 0 : week));
		vo.setRslt(ResultCst.SUCCESS);
		LOGGER.debug(GsonUtils.toJson(vo));
		return vo;
	}

	/**
	 * 查询所有体征项目
	 * 
	 * @return
	 */
	@RequestMapping("/bodysignDict")
	@ResponseBody
	public BaseListVo getBodySignDict() {
		BaseListVo vo = new BaseListVo();
		vo.setRslt(ResultCst.SUCCESS);
		vo.setList(bodySignService.getBodySignDicts());
		return vo;
	}

	/**
	 * 
	 * 更新体征记录
	 * 
	 * @param id 患者编号
	 * @param boySign 生命体征信息
	 * @return
	 */
	@RequestMapping("/updateBodySignRecord")
	@ResponseBody
	public BaseDataVo updateBodySignRecord(String item){
		LOGGER.error("updateBodySignRecord->" + item);
		BaseDataVo vo = new BaseDataVo();
		BodySignRecord record = fromJson(item, BodySignRecord.class);
		if(record == null){
			vo.setMsg("json解析失败!");
			vo.setData(null);
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			return vo;
		}

		if (!record.hasData() &&  StringUtils.isBlank(record.getRemark())) {
			record.setFullDateTime(DateUtil.parse(record.getRecordDay() + MnisConstants.EMPTY + record.getRecordTime()));
			bodySignService.delete(record);
			vo.setMsg("体征删除成功！");
			vo.setRslt(ResultCst.SUCCESS);
			return vo;
		}
		
		bodySignService.updateBodySignRecordUser(record, WebContextUtils.getSessionUserInfo());
		try{
			bodySignService.updateBodySignRecord(record,true);
		}catch(Exception e){
			LOGGER.error("", e);
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.BODY_SIGN_ERROR);
			return vo;
		}
		
		vo.setMsg("体征更新成功！");
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 
	 * 批量保存体征记录
	 * 
	 * @param id 患者编号
	 * @param boySign 生命体征信息
	 * @return
	 */
	@RequestMapping("/batchSaveBodySignRecord")
	public @ResponseBody BaseDataVo batchSaveBodySignRecord(String item, boolean isSave, String copyFlag){
		BaseDataVo vo = new BaseDataVo();
		LOGGER.error("batchSaveBodySignRecord->" + item);
		Type type = new TypeToken<List<BodySignRecord>>(){}.getType();
		List<BodySignRecord> itemList = fromJson(item, type);
		if(itemList == null) {
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			vo.setMsg("json解析失败!");
			return vo;
		}
		//是否转抄
		boolean isCopy = false;
		if("Y".equals(copyFlag)){
			isCopy = true;
		}
		
		try{
			bodySignService.batchBodySignRecord(itemList, isSave, WebContextUtils.getSessionUserInfo(),isCopy);
		}catch(Exception e){
			LOGGER.error("", e);
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.BODY_SIGN_ERROR);
			return vo;
		}
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("录入成功！");
		return vo;
	}
	
	/**
	 * 根据部门号和护士编号查询生命体征
	 * 
	 * @param deptCode
	 * @param nurseCode
	 * @return
	 */
	@RequestMapping("/getBodySignSpeedy")
	public @ResponseBody
	BaseMapVo getBodySignSpeedy(String deptCode, String nurseCode) {
		BaseMapVo baseMapVo = new BaseMapVo();
		List<BodySignSpeedy> list = bodySignService.getBodySignSpeedy(deptCode, nurseCode);
		baseMapVo.addData("lst", list);
		baseMapVo.setRslt(ResultCst.SUCCESS);
		return baseMapVo;
	}
	
	/**
	 * 根据记录时间和体征项获取记录
	 * 
	 * @param queryDate
	 * @param itemCode
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getBodySignItemByCode")
	public @ResponseBody BaseMapVo getBodySignItemByCode(String queryDate,
			String itemCode, String deptCode) {
		BaseMapVo baseMapVo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode)) {
			deptCode = WebContextUtils.getSessionUserInfo().getDeptCode();
		}
		List<BodySignItem> bodySignItems = bodySignService
				.getBodySignItemByCode(queryDate, itemCode, deptCode, null,
						null, null);
		baseMapVo.addData("lst", bodySignItems);
		baseMapVo.setRslt(ResultCst.SUCCESS);
		return baseMapVo;
	}
	
	/**
	 * 根据记录时间获取体征记录
	 * @param recordDate
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getBodySignRecordByRecordDate")
	public @ResponseBody
	BaseMapVo getBodySignRecordByRecordDate(String recordDate,String patId, String deptCode) {
		BaseMapVo baseMapVo = new BaseMapVo();
		if(StringUtils.isBlank(deptCode)){
			deptCode = WebContextUtils.getSessionUserInfo().getDeptCode();
		}
		List<BodySignRecord> list = bodySignService.getBodySignRecordByRecordDate(recordDate,patId, deptCode);
		baseMapVo.addData("lst", list);
		baseMapVo.setRslt(ResultCst.SUCCESS);
		return baseMapVo;
	}
	
	/**
	 * 查询科室/患者的生命体征
	 * @param deptCode：科室编号
	 * @param patId：患者ID
	 * @param recordDate：记录日期
	 * @param recordTime：记录时间
	 * @return
	 */
	@RequestMapping("/queryBodySignList")
	public @ResponseBody BaseMapVo queryBodySignList(String deptCode,String patId,String recordDate){
		//数据输出
		BaseMapVo outVo = new BaseMapVo();
		try{
			/*
			 * 解析传入数据--数据校验
			 */
			//记录日期不允许为空
			if(StringUtils.isEmpty(recordDate)){
				throw new MnisException("记录日期不允许为空!");
			}
			if(recordDate.trim().length()==10){
				recordDate = recordDate.trim() +" 00:00:00";
			}else if(recordDate.trim().length()==13){
				recordDate = recordDate.trim() +":00:00";
			}else if(recordDate.trim().length()==16){
				recordDate = recordDate.trim() +":00";
			}else if(recordDate.trim().length()==19){
				recordDate = recordDate.trim();
			}else{
				throw new MnisException("记录时间["+recordDate+"]格式错误!");
			}
			//科室编号不允许为空
			if(StringUtils.isEmpty(deptCode)){
				throw new MnisException("科室编号不允许为空!");
			}
			//数据查询
			List<BodySignRecord> records = bodySignService.queryBodySignList(deptCode, patId, recordDate);
			outVo.addData("lst", records);
			outVo.setRslt(ResultCst.SUCCESS);
			outVo.setMsg("操作成功！");
		}catch (MnisException e) {
			LOGGER.error("queryBodySignList",e);
			outVo.setRslt(ResultCst.ALERT_ERROR);
			outVo.setMsg(e.getMessage());
		}catch (Exception e) {
			LOGGER.error("queryBodySignList",e);
			outVo.setRslt(ResultCst.ALERT_ERROR);
			outVo.setMsg("操作失败!");
		}
		LOGGER.debug(GsonUtils.toJson(outVo));
		return outVo;
	}
	
	/**
	 * 批量保存固定体温单时间点生命体征
	 * 
	 * @param bodySignRecords
	 * @return
	 */
	@RequestMapping("/batchSaveFixTimeBodySignRecords")
	@ResponseBody
	public BaseDataVo batchSaveFixTimeBodySignRecords(String bodySignRecords) {
		LOGGER.error("saveBodySignRecord->" + bodySignRecords);
		BaseDataVo vo = new BaseDataVo();
		if(StringUtils.isBlank(bodySignRecords)){
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			vo.setMsg("传入参数为空!");
			return vo;
		}

		Type type = new TypeToken<List<BodySignRecord>>() {
		}.getType();
		List<BodySignRecord> itemList = fromJson(bodySignRecords, type);
		if (itemList == null) {
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			vo.setMsg("json解析失败!");
			return vo;
		}

		try {
			bodySignService.batchSaveFixTimeBodySignRecords(itemList,
					WebContextUtils.getSessionUserInfo());
		} catch (Exception e) {
			LOGGER.error("", e);
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.BODY_SIGN_ERROR);
			return vo;
		}
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("录入成功！");
		return vo;
	}
	
	/**
	 * 单个保存固定体温单时间点生命体征
	 * 
	 * @param bodySignRecords
	 * @return
	 */
	@RequestMapping("/saveFixTimeBodySignRecord")
	@ResponseBody
	public BaseDataVo saveFixTimeBodySignRecord(String bodySignRecord) {
		LOGGER.debug("saveFixTimeBodySignRecord-> bodySignRecord:" + bodySignRecord );
		BaseDataVo vo = new BaseDataVo();
		if(StringUtils.isBlank(bodySignRecord)){
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			vo.setMsg("传入参数为空!");
			return vo;
		}

		BodySignRecord record = fromJson(bodySignRecord, BodySignRecord.class);
		if (record == null) {
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			vo.setMsg("json解析失败!");
			return vo;
		}

		try {
			bodySignService.saveFixTimeBodySignRecord(record,
					WebContextUtils.getSessionUserInfo());
		} catch (Exception e) {
			LOGGER.error("", e);
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.BODY_SIGN_ERROR);
			return vo;
		}
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("录入成功！");
		return vo;
	}
	
	@RequestMapping("/getBodySignRecordVosToSheet")
	@ResponseBody
	public BaseMapVo getBodySignRecordVosToSheet(String patId,String date){
		//queryBodySignTemp("41200", patId, DateUtil.format(new Date(), DateFormat.FULL));
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(patId)) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("病人编号不能为空！");
			return vo;
		}
		
		vo.addData("lst", bodySignService.getBodySignRecordVosToSheet(patId, date));
		vo.setRslt(ResultCst.SUCCESS);
		LOGGER.debug(GsonUtils.toJson(vo));
		return vo;
	}
	
	/**
	 * 根据患者编号及日期，查询生命体征记录
	 * 
	 * @param id  患者编号
	 * @param date 查询日期
	 * @param isSpecialDay 指定时间
	 * @return
	 */
	@RequestMapping("/getFixTimeBodySignRecords")
	@ResponseBody
	public BaseMapVo getFixTimeBodySignRecords(String patIds,
			@RequestParam String date, boolean isNda, boolean isAllItems) {
		LOGGER.debug(date);
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(patIds)) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg("病人编号不能为空！");
			return vo;
		}
		
		/*Date day = null;
		if(StringUtils.isBlank(date)){
			day = new Date();
		}else{
			day = parse(date);
		}*/
		
		vo.addData("lst", bodySignService.getFixTimeBodySignRecords(patIds.split(MnisConstants.LINE),date,isNda,isAllItems));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	@RequestMapping("/delete")
	public @ResponseBody BaseVo delete(String record) {
		BaseVo vo = new BaseVo();

		if (StringUtils.isBlank(record)) {
			vo.setMsg("json解析失败!");
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			return vo;
		}
		BodySignRecord bodySignRecord = fromJson(record, BodySignRecord.class);

		bodySignService.delete(bodySignRecord);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	@RequestMapping("/deletePartByTime")
	public @ResponseBody BaseVo deletePartByTime(String patId,String recordDate) {
		LOGGER.debug("patId:" + patId+";recordDate"+recordDate);
		BaseVo vo = new BaseVo();
		try{
			bodySignService.deletePartByTime(patId, recordDate);
			vo.setRslt(ResultCst.SUCCESS);//删除成功
		}catch (Exception e) {
			LOGGER.error("deletePartByDate:",e);
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("操作失败");
		}
		//输出
		LOGGER.debug(GsonUtils.toJson(vo));
		return vo;
	}
	
	/**
	 * 
	 * 保存体征记录
	 * @param records 生命体征信息
	 * @return vo:处理结果输出
	 */
	@RequestMapping("/saveBodySignRecords")
	public @ResponseBody BaseDataVo saveBodySignRecords(String records){
		BaseDataVo vo = new BaseDataVo();
		LOGGER.debug(records);
		
		try{
			//校验参数传入是否为空
			if(StringUtils.isEmpty(records)){
				throw new MnisException("接口传入参数[records]为空");
			}
			//校验数据格式是否能解析
			Type type = new TypeToken<List<BodySignRecord>>(){}.getType();
			List<BodySignRecord> recordList = fromJson(records, type);
			if(null == recordList) {
				throw new MnisException("json解析失败!");
			}

			String hourType = identityService.getConfigure(MnisConstants.SYS_CONFIG_HOURTYPE);//是否24小时制
			String eventHours = identityService.getConfigure(BodySignConstants.BODY_SIGN_EVENT_TIME);
			//数据处理
			UserInformation user = WebContextUtils.getSessionUserInfo();
			List<BodySignRecord> dataList = new ArrayList<BodySignRecord>();
			for(BodySignRecord record : recordList){
				//校验患者流水号
				if(StringUtils.isEmpty(record.getPatientId())){
					throw new MnisException("床号["+record.getBedCode()+"]患者流水号不允许为空!");
				}
				
				//主记录中更新人的信息
				record.setModifyNurseCode(user.getCode());
				record.setModifyNurseName(user.getName());
				//主记录中创建人信息
				record.setRecordNurseCode(user.getCode());
				record.setRecordNurseName(user.getName());
				record.setFullDateTime(DateUtil.parse(record.getRecordDay() + " " + record.getRecordTime(),DateFormat.FULL));
				//把生命体征中的一天一次项目和一天多次项目拆分开
				dataList.addAll(BodySignUtil.splitRecord(record, hourType, eventHours));
			}
			
			//数据保存
			bodySignService.saveBodySignRecords(dataList);

			vo.setRslt(ResultCst.SUCCESS);
			vo.setMsg("操作成功！");
			//释放缓存
			dataList = null;
			recordList = null;
		}catch (MnisException e) {
			//异常处理
			LOGGER.error("saveBodySignRecords:",e);
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}catch(Exception e){
			LOGGER.error("saveBodySignRecords"+e.toString(), e);
			e.printStackTrace();
			vo.setMsg("操作失败！");
			vo.setRslt(ResultCst.ALERT_ERROR);
			return vo;
		}
		return vo;
	}
	
	/**
	 * 查询科室/患者的生命体征
	 * @param deptCode：科室编号
	 * @param patId：患者ID
	 * @param recordDate：记录日期
	 * @param recordTime：记录时间
	 * @return
	 */
	@RequestMapping("/queryBodySignTemp")
	public @ResponseBody BaseMapVo queryBodySignTemp(String deptCode,String patId,String recordDate){
		//数据输出
		BaseMapVo outVo = new BaseMapVo();
		try{
			/*
			 * 解析传入数据--数据校验
			 */
			//记录日期不允许为空
			if(StringUtils.isEmpty(recordDate)){
				throw new MnisException("记录日期不允许为空!");
			}
			if(recordDate.trim().length()==10){
				recordDate = recordDate.trim() +" 00:00:00";
			}else if(recordDate.trim().length()==13){
				recordDate = recordDate.trim() +":00:00";
			}else if(recordDate.trim().length()==16){
				recordDate = recordDate.trim() +":00";
			}else if(recordDate.trim().length()==19){
				recordDate = recordDate.trim();
			}else{
				throw new MnisException("记录时间["+recordDate+"]格式错误!");
			}
			//科室编号不允许为空
			if(StringUtils.isEmpty(deptCode)){
				throw new MnisException("科室编号不允许为空!");
			}
			//数据查询
			List<BodySignRecord> records = bodySignService.queryBodyTemperature(deptCode, patId, recordDate);
			outVo.addData("lst", records);
			outVo.setRslt(ResultCst.SUCCESS);
			outVo.setMsg("操作成功！");
		}catch (MnisException e) {
			LOGGER.error("queryBodySignList",e);
			outVo.setRslt(ResultCst.ALERT_ERROR);
			outVo.setMsg(e.getMessage());
		}catch (Exception e) {
			LOGGER.error("queryBodySignList",e);
			outVo.setRslt(ResultCst.ALERT_ERROR);
			outVo.setMsg("操作失败!");
		}
		LOGGER.debug("queryBodySignTemp:"+GsonUtils.toJson(outVo));
		return outVo;
	}
	
	@RequestMapping("/getBodySignConfigByDeptCode")
	@ResponseBody
	public BaseMapVo getBodySignConfigByDeptCode(String deptCode){
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(deptCode)){
			deptCode = WebContextUtils.getSessionUserInfo().getDeptCode();
		}
		List<BodySignItemConfig> bodySignSysConfigs = bodySignService.getBodySignConfigByDeptCode(deptCode);
		vo.addData("bodySignConfig", bodySignSysConfigs);
		vo.addData("isSyncDoc", identityService.isSyncDocReport());
		vo.setRslt(ResultCst.SUCCESS);
		
		return vo;
		
	}

	/**
	 * 转抄指定的生命体征数据到护理文书
	 * @param patIDs 指定患者的住院号集合
	 * @param deptCode 科室编号
	 * @param date 指定日期
	 * @param time 指定体征录入时间
	 * @throws Exception
	 */
	@RequestMapping(value = "/copyBodySignRecordsToDoc")
	@ResponseBody
	public BaseDataVo copyBodySignRecordsToDocRecords(String patIDs, String deptCode, String date, String time){
		BaseDataVo dataVo = new BaseDataVo();
		try {
			if(StringUtils.isEmpty(patIDs) || StringUtils.isEmpty(deptCode) || StringUtils.isEmpty(date) || StringUtils.isEmpty(time)){
				throw new MnisException("参数信息不全。");
			}
			Type type = new TypeToken<List<String>>(){}.getType();
			List<String> patIDList = fromJson(patIDs, type);
			if(patIDList.isEmpty()){
				new MnisException("Json解析失败。");
			}
			bodySignService.copyBodySignRecordsToDocRecords(patIDList, deptCode, date, time);
			dataVo.setRslt(ResultCst.SUCCESS);
			dataVo.setMsg("体征数据已转抄到文书记录。");
		}catch (Exception e){
			dataVo.setRslt(ResultCst.FAILURE);
			dataVo.setMsg(e.getMessage());
		}

		return dataVo;
	}
}