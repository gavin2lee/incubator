package com.lachesis.mnisqm.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnisqm.constants.MnisQmConstants;
import com.lachesis.mnisqm.core.utils.DateTimeUtils;
import com.lachesis.mnisqm.core.utils.DateUtils;
import com.lachesis.mnisqm.core.utils.GetDataByWeb;
import com.lachesis.mnisqm.module.patientManage.domain.BedInfo;
import com.lachesis.mnisqm.module.patientManage.domain.PatientInfo;
import com.lachesis.mnisqm.module.patientManage.service.IPatientManageService;
import com.lachesis.mnisqm.module.system.domain.SysConfig;
import com.lachesis.mnisqm.module.system.service.ICacheService;
import com.lachesis.mnisqm.module.system.service.ISysService;
import com.lachesis.mnisqm.module.user.dao.ComDeptMapper;
import com.lachesis.mnisqm.module.user.domain.ComDept;

public class AcquireBeiDaHisBedAndPatientInfo {
	
	@Autowired
	private ICacheService cacheService;
	
	@Autowired
	private ISysService sysService;
	
	@Autowired
	private IPatientManageService patientManageService;
	
	@Autowired
	private ComDeptMapper comDeptMapper;
	
	private static Element messageElement;
	
	private static Logger logger = LoggerFactory.getLogger(GetDataByWeb.class);
	
	private static boolean flag = true;
	
	public void job(){
		System.out.println("开始同步任务：。。。。。。"+(new Date()).toLocaleString());
		if(flag){
			flag = false;
			List<ComDept> deptList = comDeptMapper.selectAll();
			for (ComDept comDept : deptList) {
				acquireBedDate(comDept.getDeptCode());
			}
		}
	}
	
	/**
	 * 获取手术患者人数
	 * @param deptCode 科室code
	 * @param url webService地址
	 * @param body 请求体
	 */
	public static String getOperationSumByDeptCode(String deptCode, String url, String body){
		String temp = "";
		try {
			String info = GetDataByWeb.getData(url, body);
			if(null == info) return null;
			Document document = DocumentHelper.parseText(info);
			getNodeByName(document.getRootElement(), "message");
			@SuppressWarnings("unchecked")
			List<Element> listMessageChild = messageElement.elements();
			messageElement = null;
			for (Element element : listMessageChild) {
				@SuppressWarnings("unchecked")
				List<Element> listBody = element.elements();
				for (Element node : listBody) {
					// 如果当前节点内容不为空，则赋值
					if (!(node.getTextTrim().equals(""))) {
						temp = node.getTextTrim();
						switch (node.getName()) {
						case "NAME":
//							dept.setDeptName(temp);
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}
	
	/**
	 * 通过科室号同步床位信息
	 */
	public void acquireBedDateByDeptCode(){
		List<ComDept> deptList = comDeptMapper.selectAll();
		for (ComDept comDept : deptList) {
			acquireBedDate(comDept.getDeptCode());
		}
	}
	
	/**
	 * 同步科室信息
	 */
	public void acquireDeptDate(){
		String url = sysService.getSysConfigByConfigId(MnisQmConstants.hisWebServiceUrl).getConfigValue();
		String body = sysService.getSysConfigByConfigId(MnisQmConstants.deptInfoWebServiceBody).getConfigValue();
		logger.error("打印url地址："+url);
		logger.error("打印请求体："+body);
		List<ComDept> deptList = new ArrayList<ComDept>();
		ComDept dept;
		String temp;
		// 解析xml
		try {
			String info = GetDataByWeb.getData(url, body);
			if(null == info) return;
			Document document = DocumentHelper.parseText(info);
			getNodeByName(document.getRootElement(), "message");
			@SuppressWarnings("unchecked")
			List<Element> listMessageChild = messageElement.elements();
			messageElement = null;
			for (Element element : listMessageChild) {
				dept = new ComDept();
				@SuppressWarnings("unchecked")
				List<Element> listBody = element.elements();
				for (Element node : listBody) {
					// 如果当前节点内容不为空，则赋值
					if (!(node.getTextTrim().equals(""))) {
						temp = node.getTextTrim();
						switch (node.getName()) {
						case "NAME":
							dept.setDeptName(temp);
							break;
						case "ID":
							dept.setDeptCode(temp);
							break;
						}
					}
				}
				deptList.add(dept);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		ComDept d;
		Date date = new Date();
		for (ComDept comDept : deptList) {
			d = comDeptMapper.getByDeptCode(comDept.getDeptCode());
			if(null != d){
				comDeptMapper.updateByDeptCode(comDept);
			}else{
				comDept.setStatus("01");
				comDept.setCreateTime(date);
				comDept.setCreatePerson("ADMIN");
				comDeptMapper.insert(comDept);
			}
		}
	}
	
	
	/**
	 * 同步床位信息
	 */
	public void acquireBedDate(String deptCode){
		String url = sysService.getSysConfigByConfigId(MnisQmConstants.hisWebServiceUrl).getConfigValue();
		String body = sysService.getSysConfigByConfigId(MnisQmConstants.bedWebServiceBody).getConfigValue().replace("#{deptCode}", deptCode);
		logger.error("打印url地址："+url);
		logger.error("打印请求体："+body);
		List<BedInfo> bedList = new ArrayList<BedInfo>();
		BedInfo bed;
		String temp;
		// 解析xml
		try {
			String info = GetDataByWeb.getData(url, body);
			if(null == info) return;
			Document document = DocumentHelper.parseText(info);
			getNodeByName(document.getRootElement(), "message");
			@SuppressWarnings("unchecked")
			List<Element> listMessageChild = messageElement.elements();
			messageElement = null;
			for (Element element : listMessageChild) {
				bed = new BedInfo();
				@SuppressWarnings("unchecked")
				List<Element> listBody = element.elements();
				for (Element node : listBody) {
					// 如果当前节点内容不为空，则赋值
					if (!(node.getTextTrim().equals(""))) {
						temp = node.getTextTrim();
						switch (node.getName()) {
						case "code":
							bed.setCode(temp);
							break;
						case "ward_code":
							bed.setWardCode(temp);
							break;
						case "dept_code":
							bed.setWardCode(temp);
							break;
						case "bed_type":
							bed.setBedType(temp);
							break;
						case "bed_name":
							bed.setBedTypeName(temp);
							break;
						case "bed_price":
							bed.setBedPrice(new BigDecimal(temp));
							break;
						case "flag":
							bed.setFlag(temp);
							break;
						}
					}
				}
				bedList.add(bed);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Date date = new Date();
		for (BedInfo bedInfo : bedList) {
			bed = patientManageService.getBedInfoByCode(bedInfo.getCode());
			if(null != bed){
				patientManageService.updateBedInfo(bedInfo);
			}else{
				bedInfo.setSyncCreate(date);
				patientManageService.saveBedInfo(bedInfo);
			}
		}
	}
	
	/**
	 * 同步入院患者信息入口
	 */
	public void acqurieInPatientDate(){
		//获取最后同步时间
		SysConfig timeSysConfig = sysService.getSysConfigByConfigId(MnisQmConstants.lastInSynchronizationTime);
		Date tempDate = DateTimeUtils.getDateByMinute(DateTimeUtils.convertDateByString(timeSysConfig.getConfigValue(),DateTimeUtils.YY_MM_DD_HH_MM_SS), -5);
		String beginTime = DateTimeUtils.formatDate(tempDate, DateTimeUtils.YY_MM_DD_HH_MM_SS, null);
		String endTime = DateUtils.format(new Date(), DateTimeUtils.YY_MM_DD_HH_MM_SS);
		String url = sysService.getSysConfigByConfigId(MnisQmConstants.hisWebServiceUrl).getConfigValue();
		String body = sysService.getSysConfigByConfigId(MnisQmConstants.inPatientInfoWebServiceBody).getConfigValue()
				.replace("#{beginTime}", beginTime)
				.replace("#{endTime}", endTime);
		logger.error("打印url地址："+url);
		logger.error("打印入院患者请求体："+body);
		//同步入院患者信息
		acquirePatientDate(url, body, endTime, timeSysConfig);
	}
	
	/**
	 * 同步出院患者信息入口
	 */
	public void acqurieOutPatientDate(){
		//获取最后同步时间
		SysConfig timeSysConfig = sysService.getSysConfigByConfigId(MnisQmConstants.lastOutSynchronizationTime);
		Date tempDate = DateTimeUtils.getDateByMinute(DateTimeUtils.convertDateByString(timeSysConfig.getConfigValue(),DateTimeUtils.YY_MM_DD_HH_MM_SS), -10);
		String url = sysService.getSysConfigByConfigId(MnisQmConstants.hisWebServiceUrl).getConfigValue();
		String beginTime = DateTimeUtils.formatDate(tempDate, DateTimeUtils.YY_MM_DD_HH_MM_SS, null);
		String endTime = DateUtils.format(new Date(), DateTimeUtils.YY_MM_DD_HH_MM_SS);
		String body = sysService.getSysConfigByConfigId(MnisQmConstants.outPatientInfoWebServiceBody).getConfigValue()
				.replace("#{beginTime}", beginTime)
				.replace("#{endTime}", endTime);
		logger.error("打印出院患者请求体："+body);
		//同步出院患者信息
		acquirePatientDate(url, body, endTime, timeSysConfig);
	}
	
	/**
	 * 同步患者信息
	 * @param url 
	 * @param body
	 * @param endTime 最后同步时间
	 * @param timeSysConfig 同步时间系统配置信息
	 */
	@SuppressWarnings("deprecation")
	public void acquirePatientDate(String url, String body, String endTime, SysConfig timeSysConfig){
		List<PatientInfo> patientList = new ArrayList<PatientInfo>();
		PatientInfo patient;
		String temp;
		// 解析xml
		try {
			String info = GetDataByWeb.getData(url, body);
			if(null == info) return;
			Document document = DocumentHelper.parseText(info);
			getNodeByName(document.getRootElement(), "message");
			@SuppressWarnings("unchecked")
			List<Element> listMessageChild = messageElement.elements();
			messageElement = null;
			for (Element element : listMessageChild) {
				patient = new PatientInfo();
				@SuppressWarnings("unchecked")
				List<Element> listBody = element.elements();
				for (Element node : listBody) {
					// 如果当前节点内容不为空，则赋值
					if (!(node.getTextTrim().equals(""))) {
						temp = node.getTextTrim();
						switch (node.getName()) {
						case "PAT_ID":
							patient.setPatId(temp);
							break;
						case "IN_HOSP_NO":
							patient.setInHospNo(temp);
							break;
						case "NAME":
							patient.setName(temp);
							break;
						case "BED_CODE":
							patient.setBedCode(temp);
							break;
						case "WARD_CODE":
							patient.setWardCode(temp);
							break;
						case "Ward_Name":
							patient.setWardName(temp);
							break;
						case "Dept_Code":
							patient.setDeptCode(temp);
							break;
						case "Dept_Name":
							patient.setDeptName(temp);
							break;
						case "GENDER":
							patient.setGender(temp);
							break;
						case "PERSON_ID":
							patient.setPersonId(temp);
							break;
						case "BIRTH_DATE":
							patient.setBirthDate(new Date(temp));
							break;
						case "CONTACT_PERSON":
							patient.setContactPerson(temp);
							break;
						case "CONTACT_PHONE":
							patient.setContactPhone(temp);
							break;
						case "CONTACT_ADDRESS":
							patient.setContactAddress(temp);
							break;
						case "IS_BABY":
							patient.setIsBaby(temp);
							break;
						case "DANGER_LEVEL":
							patient.setDangerLevel(temp);
							break;
						case "NURSE_LEVEL":
							patient.setNurseLevel(temp);
							break;
						case "CHARGE_TYPE":
							patient.setChargeType(temp);
							break;
						case "CHARGE_TYPE_NAME":
							patient.setChargeTypeName(temp);
							break;
						case "DOCTOR_CODE":
							patient.setDoctorCode(temp);
							break;
						case "DOCTOR_NAME":
							patient.setDoctorName(temp);
							break;
						case "DUTY_NURSE_CODE":
							patient.setDutyNurseCode(temp);
							break;
						case "DUTY_NURSE_NAME":
							patient.setDutyNurseName(temp);
							break;
						case "IN_DATE":
							patient.setInDate(new Date(temp));
							break;
						case "IN_DIAG":
							patient.setInDiag(temp);
							break;
						case "OUT_DATE":
							patient.setOutDate(new Date(temp));
							break;
						case "OUT_DIAG":
							patient.setOutDiag(temp);
							break;
						case "STATUS":
							patient.setStatus(temp);
							break;
						case "DIET_NAME":
							patient.setDietName(temp);
							break;
						case "PREPAY_COST":
							patient.setPrepayCost(new BigDecimal(temp));
							break;
						case "OWN_COST":
							patient.setOwnCost(new BigDecimal(temp));
							break;
						case "BALANCE":
							patient.setBalance(new BigDecimal(temp));
							break;
						case "ALLERGEN":
							patient.setAllergen(temp);
							break;
						case "ADVERSE_REACTION_DRUGS":
							patient.setAdverseReactionDrugs(temp);
							break;
						case "SYNC_CREATE":
							patient.setSyncCreate(new Date(temp));
							break;
						/*case "SYNC_UPDATE":
							patient.setSyncUpdate(syncUpdate);
							break;*/
						case "ISSEPARATE":
							patient.setIsseparate(Integer.parseInt(temp));
							break;
						case "MARITAL_STATUS":
							patient.setMaritalStatus(temp);
							break;
						case "OCCUPATION":
							patient.setOccupation(temp);
							break;
						case "EDUCATION":
							patient.setEducation(temp);
							break;
						case "HOMETOWN":
							patient.setHometown(temp);
							break;
						case "RELIGION":
							patient.setReligion(temp);
							break;
						case "SOURCE":
							patient.setSource(temp);
							break;
						case "DAILYCAREGIVERS":
							patient.setDailycaregivers(temp);
							break;
						case "ADMISSIONMODE":
							patient.setAdmissionmode(temp);
							break;
						}
					}
				}
				patientList.add(patient);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		//将最后更新日期保存到数据库
		timeSysConfig.setConfigValue(endTime);
		sysService.updateSysConfig(timeSysConfig);
		//保存患者信息
		for (PatientInfo patientInfo : patientList) {
			PatientInfo p = patientManageService.getPatientInfoByInHospNo(patientInfo.getInHospNo());
			if(p != null){
				patientManageService.updatePatientInfo(patientInfo);
			}else{
				patientManageService.savePatientInfo(patientInfo);
			}
		}
	}
	
	/**
	 * 递归获取指定节点对象
	 * @param node xml节点
	 * @param name 指定节点的名称
	 */
	public static void getNodeByName(Element node, String name){
		if(node.getName().equals(name)){
			messageElement = node;
			 return;
		}
		@SuppressWarnings("unchecked")
		Iterator<Element> iterator = node.elementIterator();
		while(iterator.hasNext()){
			Element e = iterator.next();
			getNodeByName(e, name);
		}
	}

}
