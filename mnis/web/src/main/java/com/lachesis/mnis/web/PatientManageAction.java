package com.lachesis.mnis.web;

import static com.lachesis.mnis.core.util.GsonUtils.fromJson;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientManageService;
import com.lachesis.mnis.core.bodysign.BodySignConstants;
import com.lachesis.mnis.core.bodysign.BodySignConstants.BodySignStatus;
import com.lachesis.mnis.core.bodysign.BodySignUtil;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.SysUser;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.patient.entity.PatientEvent;
import com.lachesis.mnis.core.patientManage.entity.PatCureLocalInfo;
import com.lachesis.mnis.core.patientManage.entity.PatLeaveGoout;
import com.lachesis.mnis.core.patientManage.entity.PatOperationInfo;
import com.lachesis.mnis.core.patientManage.entity.PatOperationStatus;
import com.lachesis.mnis.core.patientManage.entity.PatOrderConfiguration;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.ExcelUtil;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseDataVo;

@Controller
@RequestMapping("/nur/patientManage")
public class PatientManageAction {

	@Autowired
	private PatientManageService patientManageService;
	
	@Autowired
	private IdentityService identityService;
	
	/**
	 * 保存手术信息
	 * @param patOperationInfo
	 * @return
	 */
	@RequestMapping("/savePatOperationInfo")
	public @ResponseBody
	BaseDataVo savePatOperationInfo(@RequestBody PatOperationInfo patOperationInfo) {
		BaseDataVo vo = new BaseDataVo();
		if(!StringUtil.hasValue(patOperationInfo.getPatId()) || !StringUtil.hasValue(patOperationInfo.getIsAnesthesiaConsultation()+"")
				|| !StringUtil.hasValue(patOperationInfo.getIsEmergencyTreatment()+"")){
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("请填写患者id、是否麻醉、是否急诊等信息！");
			return vo;
		}
		UserInformation user = WebContextUtils.getSessionUserInfo();
		patOperationInfo.setCreatePerson(user.getName());
		patOperationInfo.setCreateTime(new Date());
		vo.setData(patientManageService.savePatOperationInfo(patOperationInfo));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	
	/**
	 * 保存手术状态信息
	 * @param patOperationStatus
	 * @return
	 */
	@RequestMapping("/savePatOperationStatus")
	public @ResponseBody
	BaseDataVo savePatOperationStatus(String records) {
		BaseDataVo vo = new BaseDataVo();
		//校验参数传入是否为空
		if(StringUtils.isEmpty(records)){
			throw new MnisException("接口传入参数[patLeaveGoout]为空");
		}
		//校验数据格式是否能解析
		Type type = new TypeToken<PatOperationStatus>(){}.getType();
		PatOperationStatus patOperationStatus = fromJson(records, type);
		PatOperationInfo patOperationInfo = patientManageService.getPatOperationInfoByPatId(patOperationStatus.getPatId());
		if(null == patOperationInfo){
			vo.setMsg("没有该患者的手术信息！");
			return vo;
		}
		patOperationStatus.setOperationId(patOperationInfo.getOperationId());
		if(!StringUtil.hasValue(patOperationStatus.getPatId()) || !StringUtil.hasValue(patOperationStatus.getOperationId())){
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("请填写患者id、手术id！");
			return vo;
		}
		UserInformation user = WebContextUtils.getSessionUserInfo();
		patOperationStatus.setExecNurseId(user.getCode());
		patOperationStatus.setCreatePerson(user.getName());
		patOperationStatus.setCreateTime(new Date());
		String result = patientManageService.savePatOperationStatus(patOperationStatus);
		if(result.length() > 1){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg(result);
			return vo;
		}
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 保存顺序配置信息
	 * @param patOrderConfiguration
	 * @return
	 */
	@RequestMapping("/savePatOrderConfiguration")
	public @ResponseBody
	BaseDataVo savePatOrderConfiguration(@RequestBody PatOrderConfiguration patOrderConfiguration) {
		BaseDataVo vo = new BaseDataVo();
		if(!StringUtil.hasValue(patOrderConfiguration.getModulename()) || !StringUtil.hasValue(patOrderConfiguration.getStatus())
				|| !StringUtil.hasValue(patOrderConfiguration.getBackstatus())){
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("请填写模块名、状态、后置状态等信息！");
			return vo;
		}
		UserInformation user = WebContextUtils.getSessionUserInfo();
		patOrderConfiguration.setCreatePerson(user.getName());
		patOrderConfiguration.setCreateTime(new Date());
		vo.setData(patientManageService.savePatOrderConfiguration(patOrderConfiguration));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 保存本地患者信息
	 * @param patCureLocalInfo
	 * @return
	 */
	@RequestMapping("/savePatCureLocalInfo")
	public @ResponseBody
	BaseDataVo savePatCureLocalInfo(@RequestBody PatCureLocalInfo patCureLocalInfo) {
		BaseDataVo vo = new BaseDataVo();
		if(!StringUtil.hasValue(patCureLocalInfo.getPatId())){
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("请填写患者id！");
			return vo;
		}
		UserInformation user = WebContextUtils.getSessionUserInfo();
		patCureLocalInfo.setCreatePerson(user.getName());
		patCureLocalInfo.setCreateTime(new Date());
		vo.setData(patientManageService.savePatCureLocalInfo(patCureLocalInfo));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 保存请假外出信息
	 * @param patLeaveGoout
	 * @return
	 */
	@RequestMapping("/savePatLeaveGoout")
	public @ResponseBody
	BaseDataVo savePatLeaveGoout(String records) {
		BaseDataVo vo = new BaseDataVo();
		//校验参数传入是否为空
		if(StringUtils.isEmpty(records)){
			throw new MnisException("接口传入参数[patLeaveGoout]为空");
		}
		//校验数据格式是否能解析
		Type type = new TypeToken<PatLeaveGoout>(){}.getType();
		PatLeaveGoout pat = fromJson(records, type);
		if(!StringUtil.hasValue(pat.getPatId())){
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("请填写患者id！");
			return vo;
		}
		UserInformation user = WebContextUtils.getSessionUserInfo();
		pat.setExecNurseId(user.getCode());
		pat.setCreatePerson(user.getName());
		pat.setCreateTime(new Date());
		vo.setData(patientManageService.savePatLeaveGoout(pat));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 根据患者id获取手术信息
	 * @param patId
	 * @return
	 */
	@RequestMapping("/getPatOperationInfoByPatId")
	public @ResponseBody
	BaseDataVo getPatOperationInfoByPatId(String patId) {
		BaseDataVo vo = new BaseDataVo();
		if(!StringUtil.hasValue(patId)){
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("请填写患者id！");
			return vo;
		}
		vo.setData(patientManageService.getPatOperationInfoByPatId(patId));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 获取所有顺序配置信息
	 * @return
	 */
	@RequestMapping("/queryAllPatOrderConfiguration")
	public @ResponseBody
	BaseDataVo queryAllPatOrderConfiguration() {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(patientManageService.queryAllPatOrderConfiguration());
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 获取所有患者手术信息
	 * @return
	 */
	@RequestMapping("/queryAllPatOperationInfo")
	public @ResponseBody
	BaseDataVo queryAllPatOperationInfo() {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(patientManageService.queryAllPatOperationInfo());
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 根据状态或时间查询手术患者信息
	 * @param status 状态
	 * @param date 日期
	 * @return
	 */
	@RequestMapping("/queryPatOperationInfoByStatusOrDate")
	public @ResponseBody
	BaseDataVo queryPatOperationInfoByStatusOrDate(String status, String beginTime, String endTime) {
		BaseDataVo vo = new BaseDataVo();
		if(!StringUtil.hasValue(beginTime) || !StringUtil.hasValue(endTime) ){
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("请填写开始结束日期");
			return vo;
		}
		vo.setData(patientManageService.queryPatOperationInfoByStatusOrDate(status, beginTime, endTime));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 获取所有外出患者信息
	 * @param dateTime 日期;格式: 2001-01-01
	 * @param status 1 请假，2 外出，3 检查外出
	 * @return
	 */
	@RequestMapping("/queryAllOutPatientByDateAndStatus")
	public @ResponseBody
	BaseDataVo queryAllOutPatientByDateAndStatus(String beginTime, String endTime, String status) {
		BaseDataVo vo = new BaseDataVo();
		vo.setData(patientManageService.queryAllOutPatientByDateAndStatus(beginTime, endTime, status));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 保存事件(出入院)信息
	 * @param records
	 * @return
	 */
	@RequestMapping("/savePatientEvent")
	public @ResponseBody BaseDataVo savePatientEvent(String records){
		BaseDataVo vo = new BaseDataVo();
		
		try{
			//校验参数传入是否为空
			if(StringUtils.isEmpty(records)){
				throw new MnisException("接口传入参数[records]为空");
			}
			//校验数据格式是否能解析
			Type type = new TypeToken<PatientEvent>(){}.getType();
			PatientEvent event = fromJson(records, type);
			if(null == event) {
				throw new MnisException("json解析失败!");
			}
			String hourType = identityService.getConfigure(MnisConstants.SYS_CONFIG_HOURTYPE);//是否24小时制
			String eventHours = identityService.getConfigure(BodySignConstants.BODY_SIGN_EVENT_TIME);
			
			//校验事件编号是否为空
			if(StringUtils.isEmpty(event.getEventCode())){
				throw new MnisException("患者["+event.getPatientName()+"]事件中项目编号不允许为空!");
			}
			//校验事件记录日期
			if(null == event.getRecord_date()){
				throw new MnisException("患者["+event.getPatientName()+"]事件中记录时间不允许为空!");
			}
			//校验事件事件时间
			if(null == event.getRecordDate()){
				throw new MnisException("患者["+event.getPatientName()+"]事件中事件时间不允许为空!");
			}
			
			event.setEventName(BodySignConstants.MAP_BODYSIGN_ITEM.get(event.getEventCode()));
			
			//计算index
		    int[] index = BodySignUtil.getIndexByrecordDate(event.getRecordDate());
		    Date eventDate = DateUtil.parse(event.getRecordDate());
		    if(!StringUtils.isEmpty(eventHours)){
		    	//如果存在配置
		    	event.setIndex(BodySignUtil.getEventIndex(eventDate,eventHours));
		    }else{
		    	event.setIndex(index[0]);//体温单上显示在一天6次的格子上
		    }
		    //事件编号
		    if(StringUtils.isEmpty(event.getEventCode())){
		    	event.setStatus(BodySignStatus.DELETE.getValue());
		    }else{
		    	event.setStatus(BodySignStatus.NEW.getValue());
		    }
		    
		    //设置中文时间
			if (StringUtils.isNotBlank(event.getRecordDate())) {
				if (event.getRecordDate().length() > 10) {
					event.setRecordDate(event.getRecordDate().substring(11,16));
				}
				//为事件计算中文显示
				boolean bool = (!StringUtils.isEmpty(hourType) && "1".equals(hourType)) ? true : false;
				event.setChineseEventDate(DateUtil.getChineseHourMinute(event.getRecordDate(),bool));
			}
			patientManageService.savePatientEvent(event);
			vo.setRslt(ResultCst.SUCCESS);
			
		}catch (MnisException e) {
			vo.setMsg(e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
		}catch(Exception e){
			vo.setMsg("操作失败！");
			vo.setRslt(ResultCst.ALERT_ERROR);
			return vo;
		}
		return vo;
	}
	
	@RequestMapping(value="/importExcelTest")
	public @ResponseBody BaseDataVo  importExcelTest(){
		BaseDataVo vo = new BaseDataVo();
		try {
            ExcelUtil<SysUser> excelReader = new ExcelUtil<SysUser>();
            // 对读取Excel表格内容测试
            InputStream is = new FileInputStream("D:\\移动护理\\护理人员信息导入表.xls");
            SysUser sysUser = new SysUser();
            Map<String, Object> map  = excelReader.readExcelContent(is, sysUser, "com.lachesis.mnis.core.identity.entity.SysUser");
            Set<String> set = map.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                SysUser value = (SysUser) map.get(key);
                identityService.insertSysUser(value);
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return vo;  
	}
	
	/**
	 * 查询指定时间内的出入院事件
	 * @param eventCode 事件编号 ，null表示查询出院和住院
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value="/queryInOutEventByTime")
	public @ResponseBody BaseDataVo  queryInOutEventByTime(String eventCode,
			String beginTime, String endTime){
		BaseDataVo vo = new BaseDataVo();
		vo.setData(patientManageService.queryInOutEventByTime(eventCode, beginTime, endTime));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 获取各事件人数
	 * @param beginTime 时间范围起始 如 ：2001-01-01 01:01:59
	 * @param endTime 时间范围终止
	 * @return
	 */
	@RequestMapping(value="/queryNdaManageInfo")
	public @ResponseBody BaseDataVo  queryNdaManageInfo(
			String beginTime, String endTime){
		BaseDataVo vo = new BaseDataVo();
		vo.setData(patientManageService.queryNdaManageInfo(beginTime, endTime));
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 前端获取数据统一接口
	 * @param beginTime 时间范围起始 如 ：2001-01-01 01:01:59
	 * @param endTime
	 * @param method 查询的方法拼音首字母
	 * @return
	 */
	@RequestMapping(value="/queryManageInfo")
	public @ResponseBody BaseDataVo  queryManageInfo(String beginTime, String endTime ,String method ){
		BaseDataVo vo = new BaseDataVo();
		if(StringUtil.hasValue(method)){
			if("ss".equals(method)){
				vo.setData(patientManageService.queryPatOperationInfoByStatusOrDate(null, beginTime, endTime));
			}else if("cry".equals(method)){
				vo.setData(patientManageService.queryInOutEventByTime(null, beginTime, endTime));
			}else if("wc".equals(method) || "jc".equals(method)){
				vo.setData(patientManageService.queryAllOutPatientByDateAndStatus(beginTime, endTime, null));
			}
		}else{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("手术", patientManageService.queryPatOperationInfoByStatusOrDate(null, beginTime, endTime));
			map.put("出入院", patientManageService.queryInOutEventByTime(null, beginTime, endTime));
			map.put("外出", patientManageService.queryAllOutPatientByDateAndStatus(beginTime, endTime, null));
			vo.setData(map);
		}
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 导入护理人员信息
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/importSysUserExcel")
	public @ResponseBody BaseDataVo importSysUserExcel(@RequestParam MultipartFile file, HttpServletRequest request){
		BaseDataVo vo = new BaseDataVo();
		if (null == file){
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("请选择文件！");
			return vo;
		}
		String name = file.getOriginalFilename();// 获取上传文件名,包括路径
		long size = file.getSize();
		if ((name == null || name.equals("")) && size == 0)
			return null;
		try {
            ExcelUtil<SysUser> excelReader = new ExcelUtil<SysUser>();
            // 对读取Excel表格内容测试
            InputStream is = file.getInputStream();
            SysUser sysUser = new SysUser();
            Map<String, Object> map  = excelReader.readExcelContent(is, sysUser, "com.lachesis.mnis.core.identity.entity.SysUser");
            Set<String> set = map.keySet();
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                SysUser value = (SysUser) map.get(key);
                identityService.insertSysUser(value);
            }
            is.close();
            vo.setRslt(ResultCst.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
		return vo;  
	}
	
}
