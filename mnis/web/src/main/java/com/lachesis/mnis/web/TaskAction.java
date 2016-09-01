/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lachesis.mnis.core.BodySignService;
import com.lachesis.mnis.core.InspectionService;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.WardPatrolService;
import com.lachesis.mnis.core.bodysign.entity.BodySignRecord;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.inspection.entity.InspectionRecord;
import com.lachesis.mnis.core.nursing.NurseShiftService;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.patrol.entity.WardPatrolInfo;
import com.lachesis.mnis.core.task.TaskService;
import com.lachesis.mnis.core.task.WhiteBoardRecord;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseListVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;

/**
 * The class CrisisValueAction.
 * 
 * 任务清单
 * 
 * @author: yanhui.wang
 * @since: 2014-6-24
 * @version: $Revision$ $Date$ $LastChangedBy$
 * 
 */
@Controller
@RequestMapping("/nur/task")
public class TaskAction{
	static final Logger LOGGER = LoggerFactory.getLogger(TaskAction.class);

	@Autowired
	private OrderService orderService;

	@Autowired
	private PatientService patientService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private WardPatrolService wardPatrolService;

	@Autowired
	private BodySignService bodySignService;

	@Autowired
	private InspectionService inspectionSypacsService;
	
	@Autowired
	private NurseShiftService nurseShiftService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateUtil.FORMAT_YMD, true));
	}
	
	/**
	 * 
	 * 任务清单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/list")
	public @ResponseBody
	BaseMapVo taskList(
			@RequestParam(value = "nurseId", required = false) String nurseId,
			@RequestParam(value = "deptId", required = false) String deptId,
			@RequestParam(value = "execDate", required = false) Date execDate) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> Nurse task list," + nurseId + ":"
					+ ":" + execDate);
		}
		BaseMapVo json = new BaseMapVo();

		List<String> patientIdList = patientService.getPatientByDeptCodeOrUserCode(nurseId, deptId);
				
		// 确定执行时间
		if (execDate == null) {
			execDate = new Date();
		}

		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		//resultList.addAll(getOrderForTask(execDate, patientIdList));
		//resultList.addAll(getWardPatrolForTask(execDate, patientIdList));
		//resultList.addAll(getBodysignForTask(execDate, patientIdList));
		//resultList.addAll(getInspectionForTask(execDate, patientIdList));
		json.addData("list",resultList);
		json.setRslt(ResultCst.SUCCESS);
		json.setMsg("");

		return json;
	}

	private List<HashMap<String, String>> getInspectionForTask(String execDate,	List<String> patients) {
		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();

		List<InspectionRecord> result = inspectionSypacsService
				.getInspectionRecordByPatients(patients, execDate);
		
		HashMap<String, String> jsonData = new HashMap<String, String>();

		int notCompleted = 0;
		int total = 0;
		for (Iterator<InspectionRecord> iterator = result.iterator(); iterator
				.hasNext();) {
			InspectionRecord inspectionRecord = iterator.next();
			if (inspectionRecord.getDetailList().size() > 0) {
				notCompleted++;
			}
			total++;
		}
		jsonData.put("id", "inspection");
		jsonData.put("name", "检查");
		jsonData.put("uncompCnt", String.valueOf(notCompleted));
		jsonData.put("total", String.valueOf(total));
		resultList.add(jsonData);
		
		return resultList;
	}

	private String splitBedCode(String bedNo) {
		if (bedNo == null) {
			return null;
		}
		String[] arr = bedNo.split("_");
		if (arr.length > 1) {
			return arr[1];
		} else {
			return arr[0];
		}
	}

	private List<HashMap<String, String>> getBodysignForTask(Date date, String[] patients) {
		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
	
		int notCompleted = 0;
		int total = 0;
		
		List<BodySignRecord> result2 = bodySignService.getBodySignRecord(date, patients,false);
		for (Iterator iterator = result2.iterator(); iterator.hasNext();) {
			total++;
		}

		resultList.add(getOrderSummaryJson(
				"bodysign",
				"体征", notCompleted,
				total - notCompleted , 0));
		
		return resultList;
	}

	private List<HashMap<String, String>> getWardPatrolForTask(String execDate,	List<String> patients) {
		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		List<WardPatrolInfo> result = wardPatrolService
				.getWardPatrolLogByPatients(patients, execDate);

		int notCompleted = 0;
		int total = 0;
		for (Iterator<WardPatrolInfo> iterator = result.iterator(); iterator
				.hasNext();) {
			WardPatrolInfo info = iterator.next();
			if (info.getFlag() == -1) {
				notCompleted++;
			}
			total++;
		}
		resultList.add(getOrderSummaryJson(
				"wardpatrol",
				"巡视", notCompleted,
				total - notCompleted , 0));

		return resultList;
	}

	private List<HashMap<String, String>> getOrderForTask(String execDate,
			List<String> patients) {
		List<HashMap<String, String>> resultList = new ArrayList<HashMap<String, String>>();
		List<OrderExecGroup> result = orderService.getOrderBaseGroupForTask(patients, execDate,null);
		//口服药
		int orclCountN = 0;
		int orclCountY = 0;
		int orclCountPatient = 0;
		
		//检验
		int labCountN = 0;
		int labCountY = 0;
		int labCountPatient = 0;
		
		//输液
		int infusionCountN = 0;
		int infusionCountY = 0;
		int infusionCountPatient = 0;
		
		//检查
		int inspectionCountN = 0;
		int inspectionCountY = 0;
		int inspectionPatient = 0;
		
		//治疗
		int treatmentCountN = 0;
		int treatmentCountY = 0;
		int treatmentCountPatient = 0;
		
		//其他医嘱
		int otherCountN = 0;
		int otherCountY = 0;
		int otherCountPatient = 0;
		
		for (Iterator<OrderExecGroup> iterator = result.iterator(); iterator.hasNext();) {
			OrderExecGroup orderGroup = iterator.next();
			String orderExecType = orderGroup.getOrderGroup().getOrderExecTypeCode();
			if (orderExecType == null) {
				continue;
			}
			if (MnisConstants.ORDER_EXEC_TYPE_ORAL
					.equals(orderExecType)) {
				if (orderGroup.getOrderExecLog()== null || StringUtils.isEmpty(orderGroup.getOrderExecLog().getExecNurseId())) {
					orclCountN++;
				} else {
					orclCountY++;
				}
				orclCountPatient++;
			} else if (MnisConstants.ORDER_EXEC_TYPE_LAB
					.equals(orderExecType)) {
				if (orderGroup.getOrderExecLog()== null || StringUtils.isEmpty(orderGroup.getOrderExecLog().getExecNurseId())) {
					labCountN++;
				} else {
					labCountY++;
				}
				labCountPatient++;
			} else if (MnisConstants.ORDER_EXEC_TYPE_INFUSION
					.equals(orderExecType)) {
				if (orderGroup.getOrderExecLog()== null || StringUtils.isEmpty(orderGroup.getOrderExecLog().getExecNurseId())) {
					infusionCountN++;
				} else {
					infusionCountY++;
				}
				infusionCountPatient++;
			} else if (MnisConstants.ORDER_EXEC_TYPE_INSPECTION
					.equals(orderExecType)) {
				if (orderGroup.getOrderExecLog()== null || StringUtils.isEmpty(orderGroup.getOrderExecLog().getExecNurseId())) {
					inspectionCountN++;
				} else {
					inspectionCountY++;
				}
				inspectionPatient++;
			} else if (MnisConstants.ORDER_EXEC_TYPE_TREATMENT
					.equals(orderExecType)) {
				if (orderGroup.getOrderExecLog()== null || StringUtils.isEmpty(orderGroup.getOrderExecLog().getExecNurseId())) {
					treatmentCountN++;
				} else {
					treatmentCountY++;
				}
				treatmentCountPatient++;
			} else {
				if (orderGroup.getOrderExecLog()== null || StringUtils.isEmpty(orderGroup.getOrderExecLog().getExecNurseId())) {
					otherCountN++;
				} else {
					otherCountY++;
				}
				otherCountPatient++;
			}
		}

		resultList.add(getOrderSummaryJson(
				MnisConstants.ORDER_EXEC_TYPE_ORAL,
				MnisConstants.ORDER_EXEC_TYPE_ORAL_NAME, orclCountN,
				orclCountY, orclCountPatient));

		resultList.add(getOrderSummaryJson(
				MnisConstants.ORDER_EXEC_TYPE_LAB,
				MnisConstants.ORDER_EXEC_TYPE_LAB_NAME, labCountN,
				labCountY, labCountPatient));

		resultList.add(getOrderSummaryJson(
				MnisConstants.ORDER_EXEC_TYPE_INFUSION,
				MnisConstants.ORDER_EXEC_TYPE_INFUSION_NAME,
				infusionCountN, infusionCountY,infusionCountPatient));

		resultList.add(getOrderSummaryJson(
				MnisConstants.ORDER_EXEC_TYPE_INSPECTION,
				MnisConstants.ORDER_EXEC_TYPE_INSPECTION_NAME,
				inspectionCountN, inspectionCountY, infusionCountY));

		resultList.add(getOrderSummaryJson(
				MnisConstants.ORDER_EXEC_TYPE_TREATMENT,
				MnisConstants.ORDER_EXEC_TYPE_TREATMENT_NAME,
				treatmentCountN, treatmentCountY, treatmentCountPatient));

		resultList.add(getOrderSummaryJson(
				MnisConstants.ORDER_EXEC_TYPE_OTHER,
				MnisConstants.ORDER_EXEC_TYPE_OTHER_NAME, otherCountN,
				otherCountY, otherCountPatient));

		return resultList;
	}

	private HashMap<String, String> getOrderSummaryJson(String orderTypeId,
			String orderTypeName, int countN, int countY, int patientNum) {
		HashMap<String, String> orclData = new HashMap<String, String>();
//		orclData.put("id", orderTypeId);
//		orclData.put("name", orderTypeName);
//		orclData.put("uncompCnt", String.valueOf(countN));
//		orclData.put("total", String.valueOf(countN + countY));
		orclData.put("orderType", orderTypeId);
		orclData.put("orderName", orderTypeName);
		orclData.put("pendingNum", String.valueOf(countN));
		orclData.put("patientNum", String.valueOf(patientNum));
		orclData.put("execNum", String.valueOf(countY));
		
		return orclData;
	}

	/**
	 * 
	 * 添加关注
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/attention/add")
	public @ResponseBody
	BaseVo whiteBoardAdd(
			@RequestParam(value = "nurseId", required = true) String nurseId,
			@RequestParam(value = "deptCode", required = true) String deptCode,
			@RequestParam(value = "bedCode", required = true) String bedCode,
			ModelMap modelMap) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> attention add");
		}
		
		patientService.addAttention(nurseId, deptCode, bedCode);
		BaseVo result = new BaseVo(ResultCst.SUCCESS, "");
		return result;
	}

	/**
	 * 
	 * 删除关注
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/attention/del")
	public @ResponseBody
	BaseVo whiteBoardDel(
			@RequestParam(value = "nurseId", required = true) String nurseId,
			@RequestParam(value = "deptCode", required = true) String deptCode,
			@RequestParam(value = "bedCode", required = true) String bedCode,
			ModelMap modelMap) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> attention add");
		}

		patientService.delAttention(nurseId, deptCode, bedCode);
		BaseVo result = new BaseVo(ResultCst.SUCCESS, "");
		return result;
	}

	/**
	 * 
	 * 得到关注
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/attention/get")
	public @ResponseBody
	BaseMapVo getAttention(
			@RequestParam(value = "nurseId", required = true) String nurseId,
			@RequestParam(value = "deptCode", required = true) String deptCode) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> attention get");
		}
		BaseMapVo vo = new BaseMapVo();
		List<String> ids = patientService.getAttention(nurseId, deptCode);
		List<HashMap<String,String>> result = new ArrayList<HashMap<String,String>>();
		for (Iterator<String> iterator = ids.iterator(); iterator.hasNext();) {
			String id = (String) iterator.next();
			HashMap<String,String> entity = new HashMap<String,String>();
			entity.put("patientId", id);
			result.add(entity);
		}
		vo.addData(BaseVo.VO_KEY, result);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}

	/**
	 * 
	 * 护理小白板-添加数据
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/white/add")
	public @ResponseBody
	BaseDataVo whiteBoardAdd(
			@RequestParam(value = "whiteBoardRecord", required = true) String whiteBoardRecord,
			ModelMap modelMap) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> Nurse white board add");
			LOGGER.debug(whiteBoardRecord);
		}

		BaseDataVo vo = new BaseDataVo();
		if (StringUtils.isBlank(whiteBoardRecord)) {
			vo.setRslt(ResultCst.NOT_NULL_PARAMETER);
			vo.setMsg("传入参数不能为空");
			return vo;
		}
		
		try {
			whiteBoardRecord = URLDecoder.decode(whiteBoardRecord, MnisConstants.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("", e);
			vo.setRslt(ResultCst.SYSTEM_ERROR);
			vo.setMsg("传入参数错误");
			return vo;
		}
		
		WhiteBoardRecord record = GsonUtils.fromJson(whiteBoardRecord, WhiteBoardRecord.class);
		if(record.getItemValue() == null){
			record.setItemValue("");
		}
		taskService.addWhiteBoardRecord(record);
		vo.setRslt(ResultCst.SUCCESS);
		return vo;
	}
	
	/**
	 * 
	 * 白板清单
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/white/list")
	public @ResponseBody
	BaseListVo whiteBoardList(
			@RequestParam(value = "deptId", required = true) String deptId,
			@RequestParam(value = "showDate", required = false) String showDate) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> Nurse white board list");
		}

		try {
			List<WhiteBoardRecord> record = taskService.getWhiteList(deptId,showDate);
			BaseListVo resultBean = new BaseListVo();
			resultBean.setRslt(ResultCst.SUCCESS);
			resultBean.setList(record);
			return resultBean;
		} catch (Exception e) {
			e.printStackTrace();
			BaseListVo result = new BaseListVo();
			result.setRslt(ResultCst.SYSTEM_ERROR);
			result.setMsg(e.getMessage());
			return result;
		}
	}
}
