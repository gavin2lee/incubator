package com.lachesis.mnis.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseListVo;
import com.lachesis.mnis.web.common.vo.BaseVo;
import com.lachesis.mnis.web.vo.OrderFreqPlanTimeVo;

/**
 * The class BodySignRecordAction.
 * 
 * 护士--医嘱管理相关
 * 
 * @author yuliang.xu
 * @since: 2014-9-2
 * @version: $Revision$ $Date$ $LastChangedBy$
 * 
 */
//@Controller
//@RequestMapping(ActionCst.FORWARD_NURSE_ORDER_MANAGER)
public class NurseOrderManagerAction {
	static final Logger LOGGER = LoggerFactory.getLogger(NurseOrderManagerAction.class);
	
	@Autowired
	private OrderService orderService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private TaskService taskService;

	@RequestMapping("/check")
	public String orderCHeck(Model model, String patientId){
		model.addAttribute("patientId", patientId);
		model.addAttribute("currentDate", DateUtil.format(DateFormat.YMD));
		return "/test/prescriptionCheck";
	}

	@RequestMapping("/exec")
	public String orderExec(Model model, String patientId){	
		model.addAttribute("patientId", patientId);
		model.addAttribute("currentDate", DateUtil.format(DateFormat.YMD));
		return "/test/prescriptionExe"; 	
	}
	
	/**
	 * 查询病人列表的待审核医嘱
	 * @param patientIds	可传入选择的病人id列表，已逗号分隔
	 * @param orderTypeCode 医嘱类型，全部/CZ/LZ，pc端查询字段 
	 * @param startTime	医嘱开立开始时间 
	 * @param endTime	医嘱开立结束时间 
	 * @param state  
	 * @return
	 */
	@RequestMapping("/selectPatientDoctorCreateOrder")
	public @ResponseBody
	BaseListVo getPatientListOrderDetail(
			@RequestParam(value = "patientIdList", required = false) String patientIds,			
			@RequestParam(value = "orderTypeCode", required = false) String orderTypeCode,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime
			) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> NurseOrderManagerAction execute getPatientListOrderDetail, patientIds:" + patientIds 
					+", orderTypeCode:"+orderTypeCode +", startTime:"+startTime+", endTime:"+endTime);
		}
		
		BaseListVo vo = new BaseListVo();
		List<String> patientIdList = null;
		// 查询出病人列表
		if (StringUtils.isEmpty(patientIds)) {
			UserInformation userInformation = WebContextUtils.getSessionUserInfo();
			patientIdList = patientService.getAttention(userInformation.getCode(), userInformation.getDeptCode());
		}else{
			patientIdList = Arrays.asList(patientIds.split(MnisConstants.COMMA));
		}
		
		if(patientIdList == null || patientIdList.size() == 0){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("未关注的任何病人");
			return vo;
		}

		try {
			vo.setList(orderService.getOrderBaseGroupList(patientIdList, orderTypeCode, state, startTime,  endTime));
			vo.setRslt(ResultCst.SUCCESS);
			return vo;
		
		} catch (Exception e) {
			LOGGER.info(ResultCst.INVALID_PARAMETER, e);
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("系统出现出错");
			return vo;
		}
	}
	
	/**
	 * NDA端根据护士id查询关注病人列表的待审核医嘱
	 * @param nurseId  	护士ID，根据护士ID获取护士所关注的病人列表
	 * @return
	 */
	@RequestMapping("/attentPatientDoctorCreateOrder")
	@ResponseBody
	public BaseListVo getPatientListOrderDetail(
				String nurseId, 
				String deptCode, 
				@RequestParam(value = "orderTypeCode", required = false) String orderTypeCode,
				@RequestParam(value = "state", required = false) String state) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> NurseOrderManagerAction execute getPatientListOrderDetail, nurseId:" + nurseId);
		}
		
		BaseListVo vo = new BaseListVo();
		//查询出关注的病人列表
		List<String> patientIdList = patientService.getAttention(nurseId, deptCode);
		if(patientIdList == null || patientIdList.size() == 0){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("未关注的任何病人");
			return vo;
		}
		
		try {
//			vo.setList(orderService.getOrderBaseGroupList(patientIdList, orderTypeCode, state, null,  null));
			vo.setRslt(ResultCst.SUCCESS);
			return vo;
		} catch (Exception e) {
			LOGGER.info(ResultCst.INVALID_PARAMETER, e);
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("系统出现出错");
			return vo;
		}
	}
	
	/**
	 * 分解医嘱
	 * @param nurseId  		护士ID，分解护士所有关注病人的医嘱
	 * @param orderGroupId	医嘱组id，分解医嘱组id对应的医嘱，多个已逗号分隔
	 * @return
	 */
	@RequestMapping("/decomposeOrder")
	@ResponseBody
	public BaseListVo decomposeOrder(
			@RequestParam(value = "nurseId", required = false) String nurseId,
			@RequestParam(value = "patientIdList", required = false) String patientIds	,
			@RequestParam(value = "orderTypeCode", required = false) String orderTypeCode,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> NurseOrderManagerAction execute decomposeOrder, nurseId:" + nurseId);
		}
		
		BaseListVo vo = new BaseListVo();
		/*List<String> patientIdList = null;
		// 查询出病人列表
		if (StringUtils.isEmpty(patientIds)) {
			patientIdList = taskService.getAttention(nurseId);
		}else{
			patientIdList = Arrays.asList(patientIds.split(CommonCst.COMMA));
		}
		
		if(patientIdList == null || patientIdList.size() == 0){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("未关注或未选择的任何病人");
			return vo;
		}
		
		vo.setLst(orderService.handDecompPendingOrderGroups(patientIdList, orderTypeCode, state, startTime,  endTime));
		vo.setRslt(ResultCst.SUCCESS);*/
		return vo;
	}
	
	@RequestMapping("/decomposeOrderFromGroupNo")
	@ResponseBody
	public BaseListVo decomposeOrderByGroupNo(
			@RequestParam(value = "orderGroupNo") String orderGroupNo) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> NurseOrderManagerAction execute decomposeOrderByGroupNo, orderGroupNo:" + orderGroupNo);
		}
		
		BaseListVo vo = new BaseListVo();
		/*List<String> groupNoList = Arrays.asList(orderGroupNo.split(CommonCst.COMMA));
		if(groupNoList == null || groupNoList.size() == 0){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("医嘱组号不能为空");
			return vo;
		}
		
		vo.setLst(orderService.handDecompPendingOrderGroups(groupNoList));
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("医嘱审核成功");*/
		return vo;
	}

	@RequestMapping("/getAllPendingOrder")
	@ResponseBody
	public BaseListVo getAllPenddingOrder(
			@RequestParam(value = "nurseId", required = false) String userCode,
			@RequestParam(value = "deptCode", required = false) String deptCode,
			@RequestParam(value = "patientIdList", required = false) String patientIds,
			@RequestParam(value = "orderTypeCode", required = false) String orderTypeCode,
			@RequestParam(value = "orderExecTypeCode", required = false) String orderExecTypeCode,
			@RequestParam(value = "startTime", required = false) String startPlanTime,
			@RequestParam(value = "endTime", required = false) String endPlanTime) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> NurseOrderManagerAction execute getAllPenddingOrder, nurseId:" + userCode);
		}
		
		BaseListVo vo = new BaseListVo();
		List<String> patientIdList = null;
		// 查询出病人列表
		if (StringUtils.isEmpty(patientIds)) {
			patientIdList = patientService.getAttention(userCode, deptCode);
		}else{
			patientIdList = Arrays.asList(patientIds.split(MnisConstants.COMMA));
		}
		
		if(patientIdList == null || patientIdList.size() == 0){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("未关注或未选择的任何病人");
			return vo;
		}
//删除
//		List<OrderExecGroup> orderGroup = orderService.getAllOrderExecOrder(patientIdList, orderTypeCode, orderExecTypeCode, startPlanTime, endPlanTime);
		List<OrderExecGroup> orderGroup = new ArrayList<>();
		for (OrderExecGroup orderExecGroup : orderGroup) {
			orderExecGroup.getOrderGroup().setPatientBedCode(orderExecGroup.getOrderGroup().getPatientBedCode());
			orderExecGroup.getOrderExecLog().setPlanDate(orderExecGroup.getOrderExecLog().getPlanDate());
			orderExecGroup.getOrderGroup().setCreateDate(orderExecGroup.getOrderGroup().getCreateDate());
		}
		vo.setList(orderGroup);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;		
	}
	
	/**
	 * 设置部门频次默认执行时间
	 * @param orderFreqList
	 * @return
	 */
	@RequestMapping("/setFreqDefaultPlanTime")
	@ResponseBody	
	public BaseListVo insertOrderFreqPlanTimeRecord(String lst){
		BaseListVo vo = new BaseListVo();
		
		OrderFreqPlanTimeVo result = GsonUtils.fromJson(lst, OrderFreqPlanTimeVo.class);
		if(result == null || result.getOrderFreqList() == null ||  result.getOrderFreqList().size() == 0){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("不合法的参数");
			return vo;
		}
		//删除...
//		try {
//			orderService.insertOrderFreqPlanTimeRecord(result.getOrderFreqList());
//		}catch(Exception e){
//			LOGGER.error("", e);
//			vo.setList(result.getOrderFreqList());
//			vo.setRslt(ResultCst.SYSTEM_ERROR);
//			vo.setMsg("设置部门频次默认执行时间失败");
//			return vo;
//		}
		
		vo.setList(result.getOrderFreqList());
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("成功设置部门频次默认执行时间");
		return vo;
	}
	
	/**
	 * 获取某个部门频次默认执行时间
	 * @param deptCode
	 * @return
	 */
	@RequestMapping("/getFreqDefaultPlanTime")
	@ResponseBody	
	public BaseListVo getOrderFreqPlanTimeByDeptCode(String deptCode){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> NurseOrderManagerAction execute getOrderFreqPlanTimeByDeptCode, deptCode:" + deptCode);
		}
		BaseListVo vo = new BaseListVo();
		if(StringUtils.isBlank(deptCode)){
			vo.setRslt(ResultCst.INVALID_PARAMETER);
			vo.setMsg("部门不能为空");
			return vo;
		}
		//删除...
//		vo.setList(orderService.getDeptOrderGroupPlanExecTime(deptCode));
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("成功设置开立医嘱执行时间");
		return vo;
	}
	
	/**
	 * 设置开立医嘱执行时间，首次执行时间
	 * @param orderFreqList
	 * @return
	 */
	@RequestMapping("/setOrderDefaultPlanTime")
	@ResponseBody
	public BaseVo  setOrderGroupPlanExecTime(String orderGroupNo, String planExecTime, String firstPlanExecTime){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> NurseOrderManagerAction execute setOrderGroupPlanExecTime, orderGroupNo:" + orderGroupNo 
					+ ", planExecTime:" + planExecTime + ", firstPlanExecTime:" + firstPlanExecTime);
		}
		BaseVo vo = new BaseVo();
		//删除...
//		
//		try {
//			orderService.setOrderGroupPlanExecTime(orderGroupNo, planExecTime, firstPlanExecTime);
//		}catch(Exception e){
//			LOGGER.info("", e); 
//			vo.setRslt(ResultCst.SYSTEM_ERROR);
//			vo.setMsg("设置开立医嘱执行时间失败");
//			return vo;
//		}
//		
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("成功设置开立医嘱执行时间");
		return vo;
	}

	/**
	 * 设置待执行医嘱计划执行时间
	 * @param orderFreqList
	 * @return
	 */
	@RequestMapping("/setPendingOrderPlanTime")
	@ResponseBody
	public BaseVo setOrderExecPlanTime(String orderExecId, String planExecTime){
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> NurseOrderManagerAction execute setOrderExecPlanTime, orderExecId:" + orderExecId 
					+ ", planExecTime:" + planExecTime);
		}
		BaseVo vo = new BaseVo();
		//删除...
//		try {
//			orderService.setOrderExecPlanTime(orderExecId, planExecTime);
//		}catch(Exception e){
//			LOGGER.error("", e);
//			vo.setRslt(ResultCst.SYSTEM_ERROR);
//			vo.setMsg("设置待执行医嘱计划执行时间失败");
//			return vo;
//		}
		
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("成功设置待执行医嘱计划执行时间");
		return vo;
	}
}
