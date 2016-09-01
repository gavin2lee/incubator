/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.CriticalValueService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.critical.entity.CriticalOperRecord;
import com.lachesis.mnis.core.critical.entity.CriticalValue;
import com.lachesis.mnis.core.critical.entity.CriticalValueRecord;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;

/**
 * The class CrisisValueAction.
 * 
 * 危急值管理
 * 
 * @author: yanhui.wang
 * @since: 2014-6-24
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
@Controller
@RequestMapping("/nur/crisisValue")
public class CrisisValueAction {
	static final Logger LOGGER = LoggerFactory
			.getLogger(CrisisValueAction.class);

	@Autowired
	private CriticalValueService criticalValueService;

	/**
	 * 
	 * 进入患者危机值查询界面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/patCrisisValueMain")
	public String patCrisisValueMain(String patientId, Model model) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> enter patient cirsis value maintiance page.");
		}

		model.addAttribute("currentDate", DateUtil.format(DateFormat.YMD));
		model.addAttribute(MnisConstants.ID, patientId);
		return "/nur/patCrisisValueMain";
	}

	/**
	 * 根据患者编号、日期，获取患者的危急值信息。未指定患者时，获取用户当前科室的危急值
	 * 
	 * @param patientId
	 *            患者编号
	 * @param startDate
	 *            查询开始日期
	 * @param endDate
	 *            查询截止日期
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getCriticalValue")
	public @ResponseBody BaseMapVo getCriticalValue(String patientId,
			String startDate, String endDate) {
		BaseMapVo baseMapVo = new BaseMapVo();

		String deptCode = null;
		List<String> patIds = new ArrayList<String>();
		// 未指定患者时，获取用户当前科室的危急值
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		deptCode = sessionUserInfo.getDeptCode();
		if (StringUtils.isNotBlank(patientId)) {
			Collections.addAll(patIds, patientId.split(","));
		}
		Date sDate = null;
		Date eDate = null;
		if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
			sDate = DateUtil.setDateToDay(new Date());
			eDate = DateUtil.setNextDayToDate(new Date());
		} else {
			sDate = DateUtil.parse(startDate.substring(0, 10), DateFormat.YMD);
			eDate = DateUtil.parse(endDate.substring(0, 10), DateFormat.YMD);

			if (sDate.getTime() == eDate.getTime()) {
				eDate = DateUtils.addDays(eDate, 1);
			}
		}

		List<CriticalValue> result = criticalValueService.getCriticalValue(
				deptCode, patIds, sDate, eDate, true);

		baseMapVo.addData("list", result);
		baseMapVo.setMsg("");
		baseMapVo.setRslt(ResultCst.SUCCESS);
		return baseMapVo;
	}

	/**
	 * 根据患者编号、日期，获取患者的危急值信息。未指定患者时，获取用户当前科室的危急值
	 * 
	 * @param patientId
	 *            患者编号
	 * @param startDate
	 *            查询开始日期
	 * @param endDate
	 *            查询截止日期
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/confirmCritical")
	public @ResponseBody BaseVo confirmCriticalValue(String criticalId) {
		BaseVo baseVo = new BaseVo();

		if (StringUtils.isEmpty(criticalId)) {

			baseVo.setRslt(ResultCst.INVALID_PARAMETER);
			baseVo.setMsg("危机值ID不能为空");
			return baseVo;
			// 未指定患者时，获取用户当前科室的危急值
		}

		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();

		CriticalValue result = criticalValueService
				.getCriticalValue(criticalId);
		if (result == null) {
			baseVo.setRslt(ResultCst.NOT_EXIST_RECORD);
			baseVo.setMsg("危机值记录不存在");
			return baseVo;
		}

		/*
		 * if(!AnyiConstants.CRITICAL_PROCESSED.equals(result.getStatus())){
		 * baseVo.setRslt(ResultCst.NOT_EXIST_RECORD);
		 * baseVo.setMsg("危机值已被医生("+result.getDispose() +")在"+
		 * result.getDisposeTime()+"处理"); return baseVo; }
		 */

		if (!StringUtils.isEmpty(result.getNurseId())) {
			baseVo.setRslt(ResultCst.NOT_EXIST_RECORD);
			baseVo.setMsg(result.getNurseName() + "正在处理此危机值");
			return baseVo;
		}

		if (criticalValueService.confirmCritical(criticalId,
				sessionUserInfo.getCode())) {
			baseVo.setMsg("你确定了危机值消息，请尽快处理!");
			baseVo.setRslt(ResultCst.SUCCESS);
			return baseVo;
		} else {
			baseVo.setMsg("保存记录失败。");
			baseVo.setRslt(ResultCst.SYSTEM_ERROR);
			return baseVo;
		}

	}

	/**
	 * 根据时间端获取所有危急值
	 * @param deptCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/getCriticalValueRecord")
	@ResponseBody
	public BaseMapVo getCriticalValueRecord(String deptCode,String patId, String startDate,
			String endDate,boolean isAll) {
		BaseMapVo vo = new BaseMapVo();
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("criticalValue getCriticalValueRecord deptCode is null");
			vo.setMsg("科室参数不存在!");
			vo.setRslt(ResultCst.ALERT_ERROR);
			return vo;
		}
		
		
		try {
			List<CriticalValueRecord> criticalValues = criticalValueService
					.getCriticalValueRecord(deptCode, patId,startDate, endDate,isAll);

			vo.addData("records", criticalValues);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (AlertException e) {
			LOGGER.error("CrisisValueAction getCriticalValueRecord error:"
					+ e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("CrisisValueAction getCriticalValueRecord error:"
					+ e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("获取数据失败!");
		}
		return vo;
	}

	/**
	 * 处置危急值
	 * @param id
	 * @param nurseCode
	 * @param nurseName
	 * @param disposeDate
	 * @return
	 */
	@RequestMapping("/disposeCriticalRecords")
	@ResponseBody
	public BaseMapVo disposeCriticalRecords(@RequestParam String criticalOperRecords,
			String loginName,String password) {
		BaseMapVo vo = new BaseMapVo();

		try {
			Type type = new TypeToken<List<CriticalOperRecord>>() {}.getType();
			List<CriticalOperRecord> records = GsonUtils.fromJson(criticalOperRecords, type);
			//处理危急值
			criticalValueService.batchSaveCriticalOperRecord(records,loginName,password);
			UserInformation userInformation = WebContextUtils.getSessionUserInfo();
			//返回最新危急值
			List<CriticalValueRecord> criticalValues = criticalValueService
					.getCriticalValueRecord(userInformation.getDeptCode(), null, 
							null, null, false);
			vo.addData("records", criticalValues);
			vo.setRslt(ResultCst.SUCCESS);
		} catch (AlertException e) {
			LOGGER.error("CrisisValueAction disposeCriticalRecords error:"
					+ e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg(e.getMessage());
		} catch (Exception e) {
			LOGGER.error("CrisisValueAction disposeCriticalRecords error:"
					+ e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("执行失败!");
		}

		return vo;
	}
}
