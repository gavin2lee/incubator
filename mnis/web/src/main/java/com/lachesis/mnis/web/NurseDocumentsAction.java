package com.lachesis.mnis.web;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.DocumentsService;
import com.lachesis.mnis.core.InfusionMonitorService;
import com.lachesis.mnis.core.WardPatrolService;
import com.lachesis.mnis.core.documents.entity.DocumentInfo;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.infusionmonitor.entity.InfusionMonitorInfo;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

/**
 * 护理单据
 * @author lei.lei
 *
 */
@Controller
@RequestMapping("/nur/documents")
public class NurseDocumentsAction{

	@Autowired
	private DocumentsService mDocumentsService;
	
	@Autowired
	private WardPatrolService mWardPatrolService;
	
	@Autowired
	private InfusionMonitorService mInfusionMonitorService;

	@RequestMapping("/liquor")
	@ResponseBody
	public BaseMapVo queryLiquor(
			@RequestParam(value = "patientId", required = true) String patientId,
			@RequestParam(value = "queryTime", required = true) String queryTime) {

		BaseMapVo json = new BaseMapVo();
		
		List<DocumentInfo> listLiquors = null;
		try {
			listLiquors = mDocumentsService.selectLiquorDocument(patientId, queryTime);
			json.setRslt(ResultCst.SUCCESS);
			json.setMsg("");
			json.addData("list", listLiquors);
		}catch(AlertException e) {
			json.setRslt(ResultCst.SYSTEM_ERROR);
			json.setMsg(e.getMessage());
		}
	
		return json;
	}
	
	@RequestMapping("/persral")
	@ResponseBody
	public BaseMapVo queryPersral(
			@RequestParam(value = "patientId", required = true) String patientId,
			@RequestParam(value = "queryTime", required = true) String queryTime) {

		BaseMapVo json = new BaseMapVo();

		List<DocumentInfo> listLiquors = null;
		try {
			listLiquors = mDocumentsService.selectPersralDocument(patientId, queryTime);
			json.setRslt(ResultCst.SUCCESS);
			json.setMsg("");
			json.addData("list", listLiquors);
		}catch(AlertException e) {
			json.setRslt(ResultCst.SYSTEM_ERROR);
			json.setMsg(e.getMessage());
		}
	
		return json;
	}
	
	@RequestMapping("/ward")
	@ResponseBody
	public BaseMapVo queryWardPatrol(
			@RequestParam(value = "patientId", required = true) String patientId,
			@RequestParam(value = "queryTime", required = true) String queryTime) {

		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(patientId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("患者ID为空。");
			return json;
		}
		
		List<WardPatrolInfo> listLiquors = null;
		try {
			listLiquors = mWardPatrolService.getWardPatrolLogByPatId(patientId, queryTime);
			json.setRslt(ResultCst.SUCCESS);
			json.setMsg("");
			json.addData("list", listLiquors);
		}catch(Exception e) {
			json.setRslt(ResultCst.SYSTEM_ERROR);
			json.setMsg(e.getMessage());
			return json;
		}
	
		return json;
	}
	
	
	@RequestMapping("/infusion")
	@ResponseBody
	public BaseMapVo queryInfusion(
			@RequestParam(value = "patientId", required = true) String patientId,
			@RequestParam(value = "queryTime", required = true) String queryTime) {

		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(patientId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("患者ID为空。");
			return json;
		}
		
		List<InfusionMonitorInfo> listLiquors = null;
		
		Date date = null;
		//默认为当天
		if(StringUtils.isBlank(queryTime)){
			date = new Date();
		}else{
			date = DateUtil.parse(queryTime.substring(0,10),DateFormat.YMD);
		}
		
		Date startDate = DateUtil.setDateToDay(date);
		Date endDate = DateUtil.setNextDayToDate(date);
		
		try {
			listLiquors = mInfusionMonitorService.getInfusionMonitor(patientId, startDate,endDate,null,null);
			json.setRslt(ResultCst.SUCCESS);
			json.setMsg("");
			json.addData("list", listLiquors);
		}catch(Exception e) {
			json.setRslt(ResultCst.SYSTEM_ERROR);
			json.setMsg(e.getMessage());
			return json;
		}
	
		return json;
	}
}
