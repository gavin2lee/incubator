/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.barcode.BarcodeService;
import com.lachesis.mnis.core.barcode.InpatientWithOrders;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.nursing.NurseShiftEntity;
import com.lachesis.mnis.core.nursing.NurseShiftRecordEntity;
import com.lachesis.mnis.core.nursing.NurseShiftService;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.patient.entity.WorkUnitStat;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

/**
 * The class ShiftBookAction.
 * 
 * 交班本
 * 
 * @author: yanhui.wang
 * @since: 2014-6-24
 * @version: $Revision$ $Date$ $LastChangedBy$
 *
 */
@Controller
@RequestMapping("/nur/shiftBook")
public class ShiftBookAction{

	@Autowired
	private NurseShiftService nurseShiftService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private BarcodeService barcodeService;
	@Autowired
	private IdentityService identityService;

	/**
	 * 进入交班本主界面
	 * 
	 * @param modelMap
	 * @param request
	 * @return
	 */
	@RequestMapping("/shiftBookMain")
	public String shiftBookMain(Model model){
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		String workUnitCode = sessionUserInfo.getDeptCode();
		WorkUnitStat stat = patientService.getPatientStatisticByDeptCode(workUnitCode);
		model.addAttribute("currentDate", DateUtil.format());
		model.addAttribute("shiftStatis", stat);
		model.addAttribute("today", DateUtil.format(DateFormat.YMD));
		model.addAttribute("user", sessionUserInfo);
		model.addAttribute("deptName", sessionUserInfo.getDeptList().get(0).getName());
		model.addAttribute("deptCode", sessionUserInfo.getDeptCode());
		model.addAttribute("hospitalName", identityService.getHospitalName());

		return "/nur/shiftBookMain";
	}

	/**
	 * 
	 * 根据当前登录用户，用户所在的科室编号，查询交班本统计信息
	 * 
	 * @return
	 */
	@RequestMapping("/getWorkUnitStatistics")
	public @ResponseBody WorkUnitStat getWorkUnitStatistics(){
		return patientService.getPatientStatisticByDeptCode(
				WebContextUtils.getSessionUserInfo().getDeptCode());
	}

	/**
	 * 根据当前登录用户，用户所在的科室，查询交班本详细信息
	 * 
	 * @param nurseId
	 * @param deptCode
	 * @param startDate
	 * @param endDate
	 * @param rangeType
	 *            0或null表示那时段查询，1表示按整天查询
	 * @return
	 */
	@RequestMapping("/getNurseShiftList")
	public @ResponseBody BaseMapVo getNurseShiftList(String nurseId,
			String deptId, String startDate, String endDate, String rangeType){
		BaseMapVo vo = new BaseMapVo();
		List<NurseShiftEntity> nurseShiftEntities = this.nurseShiftService
				.getNurseShiftListByTime(deptId, nurseId, null,startDate,	
						endDate, rangeType);
		if (nurseShiftEntities == null) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("未获取到列表信息");

		} else {
			vo.addData("list", nurseShiftEntities);
			vo.setRslt(ResultCst.SUCCESS);
		}
		return vo;
	}

	/**
	 * 
	 * 根据当前登录用户，用户所在的科室，查询交班本详细信息
	 * 
	 * @return
	 */
	@RequestMapping("/getNurseShiftListByPatId")
	public @ResponseBody BaseMapVo getNurseShiftListByPatId(String patId,
			String startDate, String endDate, String rangeType){
		BaseMapVo vo = new BaseMapVo();
		List<NurseShiftEntity> nurseShiftEntities = this.nurseShiftService
				.getNurseShiftListByTime(null, null, patId, startDate,endDate, rangeType);
		if (nurseShiftEntities == null) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("未获取到列表信息");

		} else {
			vo.addData("list", nurseShiftEntities);
			vo.setRslt(ResultCst.SUCCESS);
		}
		return vo;
	}

	/**
	 * 保存交班本信息
	 * 
	 * @param problem
	 * @param recorderId
	 * @param patientId
	 * @return
	 */
	@RequestMapping("/saveNurseShift")
	public @ResponseBody BaseMapVo saveNurseShift(String nurseShift) {
		BaseMapVo vo = new BaseMapVo();
		NurseShiftEntity nurseShiftEntity = GsonUtils.fromJson(nurseShift, NurseShiftEntity.class);
		if(nurseShiftEntity == null){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("不合法的参数");
			return vo;
		}

		int flag = nurseShiftService.saveNurseShift(nurseShiftEntity);
		if (flag == 0) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("修改失败!");
		} else
			vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 修改交班本信息(交接班处理)
	 * 
	 * @param problem
	 * @param recorderId
	 * @param patientId
	 * @return
	 */
	@RequestMapping("/updateNurseShift")
	public @ResponseBody BaseMapVo updateNurseShift(
			@RequestParam(value = "nurseShift", required = false) String nurseShift,
			@RequestParam(value = "barcode", required = false) String barcode,
			@RequestParam(value = "patType", required = false) String patType,
			@RequestParam(value = "shiftType", required = false) String shiftType){
		BaseMapVo vo = new BaseMapVo();
		NurseShiftEntity nurseShiftEntity = GsonUtils.fromJson(nurseShift, NurseShiftEntity.class);
		if(nurseShiftEntity == null){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("不合法的参数");
			return vo;
		}

		if (StringUtils.isNotBlank(patType) && "1".equals(patType)) {
			// 条码进入
			if (StringUtils.isBlank(barcode)) {
				vo.setRslt(ResultCst.INVALID_PARAMETER);
				vo.setMsg("病人条码不能为空-barcodeId");
				return vo;
			}
			// 获取条码对应的patId
			InpatientWithOrders inpatientWithOrders = barcodeService
					.getPatientInfoByBarcode(barcode);

			if (inpatientWithOrders == null
					|| inpatientWithOrders.getInpatientInfo() == null) {
				vo.setRslt(ResultCst.ALERT_ERROR);
				vo.setMsg("没有此患者");
				return vo;
			}
			nurseShiftEntity.setPatientId(inpatientWithOrders
					.getInpatientInfo().getPatId());
			nurseShiftEntity.setDeptCode(inpatientWithOrders.getInpatientInfo()
					.getDeptCode());
		}

		int flag = nurseShiftService.updateNurseShift(nurseShiftEntity,
				shiftType);
		if (flag == 0) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("修改失败!");
		} else
			vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 根据交班本主键删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteNurseShift")
	public @ResponseBody BaseMapVo deleteNurseShift(String nurseShiftId){
		BaseMapVo vo = new BaseMapVo();
		int flag = nurseShiftService.deleteNurseShiftById(nurseShiftId);
		if (flag == 0) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("修改失败!");
		} else
			vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 
	 * 根据patId或条形码或床位号获取交班本记录信息(当前时段和上一个时段) typeCode:0或null
	 * 病人id,1条形码barcode,2床位号bedNo
	 * 
	 * @return
	 */
	@RequestMapping("/getNurseShiftRecordList")
	public @ResponseBody BaseMapVo getNurseShiftRecordList(
			@RequestParam(value = "patId", required = false) String patId,
			@RequestParam(value = "barcode", required = false) String barcode,
			@RequestParam(value = "bedNo", required = false) String bedNo,
			@RequestParam(value = "typeCode", required = false) String typeCode,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate){
		BaseMapVo vo = new BaseMapVo();
		patId = getShiftRecordPatientId(patId, barcode, bedNo, typeCode);
		List<NurseShiftRecordEntity> nurseShiftRecordEntities = this.nurseShiftService
				.getNurseShiftRecordEntitiesByPatId(patId, startDate,endDate);

		if (nurseShiftRecordEntities == null
				|| nurseShiftRecordEntities.size() == 0) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("未获取到列表信息");

		} else {
			vo.addData("list", nurseShiftRecordEntities);
			vo.setRslt(ResultCst.SUCCESS);
		}
		return vo;
	}

	/**
	 * 获取病人id
	 * 
	 * @param patId
	 * @param barcode
	 * @param bedNo
	 * @param typeCode
	 * @return
	 */
	private String getShiftRecordPatientId(String patId, String barcode,
			String bedNo, String typeCode) {
		if (StringUtils.isNotBlank(typeCode)) {
			int type = Integer.valueOf(typeCode);
			switch (type) {
			case 2:// 床位号
					// bedNo
				if (StringUtils.isBlank(bedNo)) {
					throw new AlertException("床位号不能为空");
				}
				
				Patient patient = patientService.getPatientByDeptCodeAndBedCode(WebContextUtils.getSessionUserInfo().getDeptCode(), bedNo);
				if (patient == null) {
					throw new AlertException("没有此患者");
				}

				patId = patient.getPatId();
				break;
			case 1:// 条形码

				if (StringUtils.isBlank(barcode)) {
					throw new AlertException("病人条码不能为空");
				}
				// 获取条码对应的patId
				InpatientWithOrders inpatientWithOrders = barcodeService
						.getPatientInfoByBarcode(barcode);

				if (inpatientWithOrders == null
						|| inpatientWithOrders.getInpatientInfo() == null) {
					throw new AlertException("没有此患者");
				}
				patId = inpatientWithOrders.getInpatientInfo().getPatId();
				break;

			default:
				break;
			}
		}

		return patId;
	}

	/**
	 * 保存交班本内容信息
	 * 
	 * @param shiftBookId
	 * @param problem
	 * @return
	 */
	@RequestMapping("/saveNurseShiftRecord")
	public @ResponseBody BaseMapVo saveNurseShiftRecord(String shiftRecord){
		BaseMapVo vo = new BaseMapVo();

		NurseShiftRecordEntity nurseShiftRecordEntity = GsonUtils.fromJson(shiftRecord, NurseShiftRecordEntity.class);
		if(nurseShiftRecordEntity == null){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("不合法的参数");
			return vo;
		}

		int flag = nurseShiftService.saveShiftRecord(nurseShiftRecordEntity);
		if (flag == 0) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("修改失败!");
		} else
			vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 根据交班本id更新交班本内容
	 * 
	 * @param shiftBookId
	 * @param problem
	 * @return
	 */
	@RequestMapping("/updateNurseShiftRecord")
	public @ResponseBody BaseMapVo updateNurseShiftRecord(String shiftRecord){
		BaseMapVo vo = new BaseMapVo();
		NurseShiftRecordEntity nurseShiftRecordEntity = GsonUtils.fromJson(shiftRecord, NurseShiftRecordEntity.class);
		if(nurseShiftRecordEntity == null){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("不合法的参数");
			return vo;
		}
	
		int flag = nurseShiftService.updateShiftRecord(nurseShiftRecordEntity);
		if (flag == 0) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("修改失败!");
		} else
			vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 根据交班本id删除交班本记录
	 * 
	 * @param shiftBookId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deleteNurseShiftRecord")
	public @ResponseBody BaseMapVo deleteNurseShiftRecord(String shiftRecordId){
		BaseMapVo vo = new BaseMapVo();
		int flag = nurseShiftService.deleteShiftRecord(shiftRecordId);
		if (flag == 0) {
			vo.setRslt(ResultCst.SHIFT_BOOK_ERROR);
			vo.setMsg("删除失败!");
		} else
			vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 获得指定日期前一天的代码
	 * 
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		return DateUtil.format(
				DateUtils.addDays(
						DateUtil.parse(specifiedDay, DateFormat.YMD), 
						-1), 
				DateFormat.YMD);
	}
}
