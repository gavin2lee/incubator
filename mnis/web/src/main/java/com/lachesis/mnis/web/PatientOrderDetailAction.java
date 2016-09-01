/**
 * (C) Copyright. LACHESIS All rights reserved.
 *
 */
package com.lachesis.mnis.web;

import com.google.gson.reflect.TypeToken;
import com.lachesis.mnis.core.*;
import com.lachesis.mnis.core.barcode.BarcodeService;
import com.lachesis.mnis.core.barcode.InpatientWithOrders;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.event.entity.DocReportEventEntity;
import com.lachesis.mnis.core.event.entity.DocReportEventItemEntity;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.order.entity.*;
import com.lachesis.mnis.core.patient.entity.Patient;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseDataVo;
import com.lachesis.mnis.web.common.vo.BaseMapVo;
import com.lachesis.mnis.web.common.vo.BaseVo;
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

import java.lang.reflect.Type;
import java.util.*;

import static com.lachesis.mnis.core.util.GsonUtils.fromJson;

/**
 * The class PatientOrderDetailAction.
 * 
 * 以患者为视角-->医嘱管理
 * 
 * @author: yanhui.wang
 * @since: 2014-6-10
 * @version: $Revision$ $Date$ $LastChangedBy$
 * 
 */
@Controller
@RequestMapping("/nur/patOrderDetail")
public class PatientOrderDetailAction {
	static final Logger LOGGER = LoggerFactory
			.getLogger(PatientOrderDetailAction.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private PatientService patientService;
	@Autowired
	private BarcodeService barcodeService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private InfusionMonitorService infusionmonitorService;

	@Autowired
	private NurseScanService nurseScanService;

	/**
	 * 
	 * 进入患者医嘱管理主界面
	 * 
	 * @param id
	 *            患者ID
	 * @param modelMap
	 *            用于后台与前台参数传递
	 * @return
	 */
	@RequestMapping("/{id}/patientOrderDetail")
	public String patientGlanceMain(@PathVariable String id, Model model)
			throws Exception {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> redirect to patient order detail page.");
		}

		Patient patientInfo = patientService.getPatientByPatId(id);
		if (patientInfo == null) {
			throw new Exception("患者不存在");
		}

		model.addAttribute("current_patient_prop", patientInfo);
		return "/nur/patientOrderDetail";
	}

	/**
	 * 医嘱查询
	 * 
	 * @return
	 */
	@RequestMapping("/prescriptionAll")
	public String prescriptionAll() {
		return "/nur/prescriptionAll";
	}

	/**
	 * 未执行医嘱
	 * 
	 * @return
	 */
	@RequestMapping("/prescriptionUnExe")
	public String prescriptionUnExe() {
		return "/nur/prescriptionUnExe";
	}

	/**
	 * 医嘱执行单
	 * 
	 * @return
	 */
	@RequestMapping("/executionOrder")
	public String executionOrder() {
		return "/nur/executionOrder";
	}

	/**
	 * 根据患者ID,医嘱类型，获取该患者全部的医嘱
	 * 
	 * @param patientId
	 * @param orderExecTypeCode
	 *            医嘱执行类型(null全部，其他：输液，口服药等)
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/queryOrderGroupList")
	public @ResponseBody BaseMapVo queryOrderGroupList(String patientId,
			String orderExecTypeCode, String orderTypeCode, String startDate,
			String endDate) throws Exception {
		BaseMapVo json = new BaseMapVo();
		if (StringUtils.isBlank(patientId)) {
			throw new IllegalArgumentException("病人编号不能为空-patientId");
		}

		Date sTime = null;
		Date eTime = null;
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			// 获取当前时间和前24小时区间
			sTime = DateUtil.parse(startDate.substring(0, 10), DateFormat.YMD);
			eTime = DateUtil.parse(endDate.substring(0, 10), DateFormat.YMD);
		}

		json.addData("list", orderService.getOrderBaseGroupList(patientId,
				orderTypeCode, orderExecTypeCode, null, sTime, eTime));
		// json.addData("OrderBedInfoList",
		// orderService.getOrderBedInfoList(patientId,
		// orderTypeCode,orderExecTypeCode, sTime, eTime));
		json.setRslt(ResultCst.SUCCESS);

		return json;
	}

	/**
	 * 根据患者ID,医嘱类型，获取该患者待执行的医嘱
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 *            医嘱分类(长嘱，临嘱)
	 * @param orderExecTypeCode
	 *            医嘱执行类型(输液，口服药等)
	 * @return
	 */
	@RequestMapping("/queryPendingOrderGroupList")
	public @ResponseBody BaseMapVo queryPendingOrderGroupList(String patientId,
			String orderTypeCode, String orderExecTypeCode, String startDate,
			String endDate) {
		BaseMapVo json = new BaseMapVo();
		if (StringUtils.isBlank(patientId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("病人编号不能为空-patientId");
			return json;
		}
		Date sTime = null;
		Date eTime = null;

		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			// 获取当前时间和前24小时区间
			sTime = DateUtil.parse(startDate.substring(0, 10), DateFormat.YMD);
			eTime = DateUtil.parse(endDate.substring(0, 10), DateFormat.YMD);
		}

		json.addData("list", orderService.getPendingOrderGroups(patientId,
				orderTypeCode, orderExecTypeCode, sTime, eTime));
		// json.addData("OrderBedInfoList",
		// orderService.getOrderBedInfoList(patientId, orderTypeCode,
		// orderExecTypeCode,null, null));
		json.setRslt(ResultCst.SUCCESS);

		return json;
	}

	/**
	 * 根据患者扫描的条码，查询患者及患者所有的医嘱信息
	 * 
	 * @param barcodeId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getPatientInfoByBarcode")
	public @ResponseBody BaseVo getPatientInfoByBarcode(String barcodeId,
			String deptId) {
		BaseDataVo json = new BaseDataVo();

		if (StringUtils.isBlank(barcodeId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("病人条码不能为空-barcodeId");
			return json;
		}
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		try {
			final InpatientWithOrders inpatientWithOrders = barcodeService
					.getPatientInfoByBarcode(barcodeId);
			if (inpatientWithOrders == null
					|| inpatientWithOrders.getInpatientInfo() == null) {
				handlerExceptionMsg(json, barcodeId, barcodeId,
						sessionUserInfo, MnisConstants.SCAN_PATIENT,
						StringUtil.createErrorMsg(MnisConstants.SCAN_PATIENT,
								MnisConstants.PAT_NOT_EXIST, "没有此患者"),
						ResultCst.ALERT_ERROR, true);
			} else {

				String currDept = deptId;
				if (StringUtils.isEmpty(currDept)) {
					currDept = sessionUserInfo.getDeptCode();
				}

				if (!inpatientWithOrders.getInpatientInfo().getDeptCode()
						.equals(currDept)) {
					handlerExceptionMsg(json, barcodeId, barcodeId,
							sessionUserInfo, MnisConstants.SCAN_PATIENT,
							StringUtil.createErrorMsg(
									MnisConstants.SCAN_PATIENT,
									MnisConstants.PAT_NOT_EXIST, "此患者不属于此科室"),
							ResultCst.ALERT_ERROR, true);
				} else {
					nurseScanService.handleNurseScan(barcodeId, barcodeId,
							sessionUserInfo.getDeptCode(), null,
							sessionUserInfo.getCode(),
							sessionUserInfo.getName(), "PATIENT", 1);
					json.setRslt(ResultCst.SUCCESS);
					json.setData(inpatientWithOrders.getInpatientInfo());
				}
				/*
				 * Executors.newCachedThreadPool().execute(new Runnable() {
				 * 
				 * @Override public void run() { // 所有扫描患者腕带的记录自动同步到巡视记录里 if
				 * (StringUtils.isNotEmpty(currUser)) {
				 * wardPatrolService.saveWardPatrolInfoForPatient(
				 * inpatientWithOrders.getInpatientInfo(), currUser, currDept);
				 * } } });
				 */
			}
		} catch (MnisException e) {
			LOGGER.error(e.toString());
			handlerExceptionMsg(json, barcodeId, barcodeId, sessionUserInfo,
					MnisConstants.SCAN_PATIENT, StringUtil.createErrorMsg(
							MnisConstants.SCAN_PATIENT,
							MnisConstants.PAT_ERROR, e.getMessage()),
					ResultCst.ALERT_ERROR, true);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			handlerExceptionMsg(json, barcodeId, barcodeId, sessionUserInfo,
					MnisConstants.SCAN_PATIENT, StringUtil.createErrorMsg(
							MnisConstants.SCAN_PATIENT,
							MnisConstants.PAT_ERROR, "获取失败!"),
					ResultCst.ALERT_ERROR, true);
		}
		return json;
	}

	/**
	 * NDA端根据医嘱执行ID，执行当前医嘱组
	 * 
	 * @param id
	 *            患者ID或者条码
	 * @param orderExecId
	 *            医嘱执行ID
	 * @param orderType医嘱类型
	 *            ORAL：口服药;INFUSION：输液;SKINTEST:皮试;LAB：皮试; UC：检验;UZ：治疗
	 *            2015.09.08 end by Qingzhi.Liu
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/execOrderNda")
	public @ResponseBody BaseDataVo execOrderNda(String orderExecId,
			String patientId, String orderType, String execDateTime) {
		LOGGER.debug("orderExecId:" + orderExecId + ";" + "patientId:"
				+ patientId + ";" + "orderType:" + orderType + ";");
		BaseDataVo json = new BaseDataVo();
		if (StringUtils.isBlank(orderExecId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("医嘱执行ID不能为空-orderExecId");
			return json;
		}
		try {
			UserInformation sessionUserInfo = WebContextUtils
					.getSessionUserInfo();
			Date execOrderDate = null;
			if (StringUtils.isBlank(execDateTime)) {
				execOrderDate = new Date();
			} else {
				execOrderDate = DateUtil.parse(execDateTime, DateFormat.FULL);
			}
			OrderExecGroup orderGroup = orderService.execOrderGroupWithPDA(
					orderExecId, patientId, sessionUserInfo, null, orderType,
					execOrderDate, false);

			json.setData(orderGroup);
			json.setRslt(ResultCst.SUCCESS);
			LOGGER.debug(GsonUtils.toJson(json));
			return json;
		} catch (AlertException e) {
			json.setRslt(ResultCst.ALERT_ERROR);
			json.setMsg(e.getMessage());
			return json;
		} catch (Exception ex) {
			ex.printStackTrace();
			json.setRslt(ResultCst.SYSTEM_ERROR);
			json.setMsg(ex.getMessage());
			return json;
		}
	}

	/**
	 * 各种执行单中执行医嘱核对
	 * 
	 * @param orderExecId
	 *            :待执行医嘱条码--输血条码以'*'隔开
	 * @param endOrderExecId
	 *            :待结束条码(输液)
	 * @param patientId
	 *            :患者编号
	 * @param approveNurseCode
	 *            :确认护士code
	 * @param approveNurseName
	 *            :确认护士name
	 * @param orderExecType
	 *            :医嘱执行类型-输液,检验,口服,输血等
	 * @param execDate
	 *            :执行时间
	 * @param isScan
	 *            :true:扫描执行,false:手动执行
	 * @return
	 */
	@RequestMapping("/execInfusionOrderNda")
	public @ResponseBody BaseMapVo execInfusionOrderNda(String orderExecId,
			String endOrderExecId, String patientId, String approveNurseCode,
			String approveNurseName, String orderExecType, String execDate,
			boolean isScan) {
		BaseMapVo json = new BaseMapVo();
		LOGGER.debug("orderExecId:" + orderExecId + ";patientId:" + patientId
				+ ";endOrderExecId:" + endOrderExecId + ";orderExecType:"
				+ orderExecType);
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		if (StringUtils.isBlank(orderExecId)) {
			json = handlerExceptionMsg(json, orderExecId, patientId,
					sessionUserInfo, orderExecType, StringUtil.createErrorMsg(
							orderExecType, MnisConstants.BAR_EMPTY,
							"医嘱执行ID不能为空"), ResultCst.INVALID_PARAMETER, isScan);
			return json;
		}

		String[] barcodes = null;
		try {
			Date execOrderDate = null;
			if (StringUtils.isBlank(execDate)) {
				execOrderDate = new Date();
			} else {
				execOrderDate = DateUtil.parse(execDate, DateFormat.FULL);
			}
			OrderExecGroup orderGroup = null;
			barcodes = orderExecId.split(MnisConstants.LINE_COLON);
			if (barcodes.length > 1) {
				orderExecType = MnisConstants.ORDER_EXEC_TYPE_BLOOD;
				orderGroup = orderService.execBloodOrderGroupWithPDA(
						barcodes[0], barcodes[1], patientId, sessionUserInfo,
						approveNurseCode, approveNurseName, execOrderDate,
						isScan);
			} else {
				orderGroup = orderService.execOrderGroupWithPDA(orderExecId,
						patientId, sessionUserInfo, endOrderExecId,
						orderExecType, execOrderDate, isScan);
			}

			List<OrderExecGroup> orderExecGroups = new ArrayList<OrderExecGroup>();
			if (null != orderGroup) {
				orderExecGroups.add(orderGroup);
			}
			json.addData("list", orderExecGroups);
			if (MnisConstants.ORDER_EXEC_TYPE_INFUSION.equals(orderGroup
					.getOrderGroup().getOrderExecTypeCode())) {
				List<String> orderExecIds = new ArrayList<String>();
				// 正执行输液信息
				orderExecIds.add(orderExecId);
				/*
				 * //待结束输液信息 if(StringUtils.isNotBlank(endOrderExecId)){
				 * orderExecIds.add(endOrderExecId); }
				 */
				json.addData("infusionRecordList", infusionmonitorService
						.getInfusionMonitorRecordList(null, orderExecIds, null,
								null));
			}

			json.setRslt(ResultCst.SUCCESS);
			LOGGER.debug(GsonUtils.toJson(json));
			if (isScan) {
				nurseScanService.handleNurseScan(orderExecId, patientId,
						sessionUserInfo.getDeptCode(), null,
						sessionUserInfo.getCode(), sessionUserInfo.getName(),
						orderExecType, 1);
			}

			return json;
		} catch (AlertException e) {
			LOGGER.error("execInfusionOrderNda --" + e.getMessage());
			json = handlerExceptionMsg(json, orderExecId, patientId,
					sessionUserInfo, orderExecType, e.getMessage(),
					ResultCst.ALERT_ERROR, isScan);
			return json;
		} catch (MnisException e) {
			LOGGER.error("execInfusionOrderNda --" + e.getMessage());
			json = handlerExceptionMsg(json, orderExecId, patientId,
					sessionUserInfo, orderExecType, e.getMessage(),
					ResultCst.ALERT_ERROR, isScan);
			return json;
		} catch (Exception ex) {
			LOGGER.error("execInfusionOrderNda --" + ex.getMessage());
			json = handlerExceptionMsg(json, orderExecId, patientId,
					sessionUserInfo, orderExecType, ex.getMessage(),
					ResultCst.SYSTEM_ERROR, isScan);
			return json;
		}
	}

	/**
	 * 批量执行 各种执行单中执行医嘱核对
	 * 
	 * @param orderExecId
	 *            :待执行医嘱条码--输血条码以'*'隔开
	 * @param patientId
	 *            :患者编号
	 * @param approveNurseCode
	 *            :确认护士code
	 * @param approveNurseName
	 *            :确认护士name
	 * @param orderType
	 *            :医嘱执行类型-输液,检验,口服,输血等
	 * @param isScan
	 *            :true:扫描执行,false:手动执行
	 * @return
	 */
	@RequestMapping("/batchExecOrderNda")
	public @ResponseBody BaseMapVo batchExecOrderNda(String orderExecId,
			String patientId, String approveNurseCode, String approveNurseName,
			String orderType, boolean isScan) {
		BaseMapVo json = new BaseMapVo();
		LOGGER.debug("orderExecId:" + orderExecId + ";patientId:" + patientId
				+ ";orderType:" + orderType);
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		if (StringUtils.isBlank(orderExecId) || StringUtils.isBlank(patientId)) {
			json = handlerExceptionMsg(json, orderExecId, patientId,
					sessionUserInfo, orderType, StringUtil.createErrorMsg(
							orderType, MnisConstants.BAR_EMPTY, "医嘱执行ID不能为空"),
					ResultCst.INVALID_PARAMETER, isScan);
			return json;
		}
		try {
			List<OrderExecGroup> executedOrders = orderService
					.batchExecOrderGroupWithPDA(orderExecId, patientId,
							sessionUserInfo, approveNurseCode,
							approveNurseName, orderType, isScan);
			json.addData("list", executedOrders);

			if (!orderExecId.contains(MnisConstants.COMMA)) {
				if (MnisConstants.ORDER_EXEC_TYPE_INFUSION
						.equals(executedOrders.get(0).getOrderGroup()
								.getOrderExecTypeCode())) {
					List<String> orderExecIds = new ArrayList<String>();
					orderExecIds.add(orderExecId);
					json.addData("infusionRecordList", infusionmonitorService
							.getInfusionMonitorRecordList(null, orderExecIds,
									null, null));
				}
			}

			json.setRslt(ResultCst.SUCCESS);
			LOGGER.debug(GsonUtils.toJson(json));
			if (isScan) {
				nurseScanService.handleNurseScan(orderExecId, patientId,
						sessionUserInfo.getDeptCode(), null,
						sessionUserInfo.getCode(), sessionUserInfo.getName(),
						orderType, 1);
			}
			return json;
		} catch (AlertException e) {
			LOGGER.error("batchExecOrderNda"+e.toString(),e);
			json = handlerExceptionMsg(json, orderExecId, patientId,
					sessionUserInfo, orderType, e.toString(),
					ResultCst.SYSTEM_ERROR, isScan);
			return json;
		} catch (Exception ex) {
			LOGGER.error("batchExecOrderNda"+ex.toString(),ex);
			json = handlerExceptionMsg(json, orderExecId, patientId,
					sessionUserInfo, orderType, ex.toString(),
					ResultCst.ALERT_ERROR, isScan);
			return json;
		}
	}

	/**
	 * 根据医嘱组，医嘱执行完成后处理
	 * 
	 * @param id
	 * @param orderExecId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/setOrderFinished")
	public @ResponseBody BaseDataVo setOrderFinished(String orderExecId) {
		BaseDataVo json = new BaseDataVo();

		if (StringUtils.isBlank(orderExecId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("医嘱执行ID不能为空-orderExecId");
			return json;
		}
		// 删除...
		/*
		 * UserInformation sessionUserInfo =
		 * WebContextUtils.getSessionUserInfo(); OrderExecGroup result; try {
		 * result = drugOrderService.finishOrderExecution(orderExecId,
		 * sessionUserInfo.getCode(), sessionUserInfo.getName()); //result =
		 * this.handlerOrderFinished(orderExecId, sessionUserInfo.getEmplCode(),
		 * sessionUserInfo.getName()); if (result == null) {
		 * json.setRslt(ResultCst.FAILURE); json.setMsg("标记完成失败"); } else {
		 * json.setData(result); json.setRslt(ResultCst.SUCCESS); } } catch
		 * (Exception e) { e.printStackTrace();
		 * json.setRslt(ResultCst.INVALID_PARAMETER);
		 * json.setMsg(e.getMessage()); }
		 */

		return json;
	}

	/**
	 * 条码扫描获取医嘱信息
	 * 
	 * @param orderGroupNo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getOrderGroupDetail")
	public @ResponseBody BaseDataVo getOrderGroupDetail(String orderGroupNo,
			String patientId) {
		BaseDataVo json = new BaseDataVo();
		String execType = null;
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		try {
			if (StringUtils.isBlank(orderGroupNo)) {
				json.setRslt(ResultCst.FAILURE);
				json.setMsg("医嘱编号不能为空-orderGroupNo");
				json = handlerExceptionMsg(json, orderGroupNo, patientId,
						sessionUserInfo, execType, StringUtil.createErrorMsg(
								execType, MnisConstants.BAR_EMPTY, "医嘱编号不能为空"),
						ResultCst.INVALID_PARAMETER, true);
				return json;
			}
			String[] barcodes = orderGroupNo.split(MnisConstants.LINE_COLON);
			if (barcodes.length > 1) {
				execType = MnisConstants.ORDER_EXEC_TYPE_BLOOD;
				// 输血条码
				//三院情况：如果采血码错扫成了血袋序号，则截取前13位作为采血码
				String bloodBar = barcodes[0];
				if(13<bloodBar.length()){
					bloodBar = bloodBar.substring(0, 13);
				}
				json.setData(orderService.getBloodOrderGroupDetail(bloodBar,
						barcodes[1], patientId));
			} else {
				json.setData(orderService.getOrderGroupDetail(orderGroupNo,
						patientId));
			}

			json.setRslt(ResultCst.SUCCESS);
			nurseScanService.handleNurseScan(orderGroupNo, patientId,
					sessionUserInfo.getCode(), null, sessionUserInfo.getCode(),
					sessionUserInfo.getName(), execType, 1);
		} catch (MnisException e) {
			LOGGER.error(e.toString());
			json = handlerExceptionMsg(json, orderGroupNo, patientId,
					sessionUserInfo, execType, e.getMessage(),
					ResultCst.ALERT_ERROR, true);
		} catch (Exception e) {
			LOGGER.error(e.toString());
			json = handlerExceptionMsg(json, orderGroupNo, patientId,
					sessionUserInfo, execType, e.getMessage(),
					ResultCst.ALERT_ERROR, true);
		}
		LOGGER.debug(GsonUtils.toJson(json));
		return json;
	}

	/**
	 * 医嘱管理_获取医嘱查看列表
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 *            医嘱分类（CZ,LZ）
	 * @param orderExecTypeCode
	 *            医嘱执行分类(输液，口服药等)
	 * @param status
	 *            医嘱状态(待执行，已执行，停止)
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getOrderBaseGroupList")
	public @ResponseBody BaseMapVo getOrderBaseGroupList(String patientId,
			String orderTypeCode, String orderExecTypeCode, String status,
			String startDate, String endDate) throws Exception {
		if (StringUtils.isBlank(patientId)) {
			throw new IllegalArgumentException("病人ID不能为空-patientId");
		}

		Date sTime = null;
		Date eTime = null;
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			// 获取当前时间和前24小时区间
			sTime = DateUtil.parse(startDate.substring(0, 10), DateFormat.YMD);
			eTime = DateUtil.parse(endDate.substring(0, 10), DateFormat.YMD);
		}

		List<OrderExecGroup> allOrderList = orderService.getOrderBaseGroupList(
				patientId, orderTypeCode, orderExecTypeCode, status, sTime,
				eTime);

		BaseMapVo json = new BaseMapVo();
		json.addData("list", allOrderList);
		json.setRslt(ResultCst.SUCCESS);

		return json;
	}

	/**
	 * 多病人-分类-待执行医嘱查询
	 * 
	 * @param nurseId
	 *            护士ID
	 * @param deptId
	 *            科室ID
	 * @param orderExecType
	 *            执行类型
	 * @param execDate
	 *            执行时间
	 * @return
	 */
	@RequestMapping("/queryMultOrderGroupList")
	public @ResponseBody BaseMapVo taskOrderList(
			@RequestParam(value = "nurseId", required = false) String nurseId,
			@RequestParam(value = "deptId", required = true) String deptId,
			@RequestParam(value = "orderExecType", required = false) String orderExecType) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(" >>>>>>>>>>>>> Nurse multi order list," + nurseId
					+ ":" + ":" + deptId + ":" + orderExecType);
		}
		BaseMapVo json = new BaseMapVo();

		List<String> patientIdList = patientService
				.getPatientByDeptCodeOrUserCode(nurseId, deptId);

		List<OrderExecGroup> orders = orderService.getOrderBaseGroupForTask(
				patientIdList, null, orderExecType);
		json.addData("list", orders);
		json.setRslt(ResultCst.SUCCESS);
		json.setMsg("");

		return json;
	}

	/**
	 * NDA 多人、批量执行医嘱
	 * 
	 * @param orderRecords
	 *            :医嘱记录表
	 * @param approveNurseCode
	 *            :确认护士code
	 * @param approveNurseName
	 *            :确认护士name
	 * @return
	 */
	@RequestMapping("/batchExecMultPatOrderNda")
	public @ResponseBody BaseMapVo batchExecMultPatOrderNda(
			String orderRecords, String approveNurseCode,
			String approveNurseName) {
		BaseMapVo json = new BaseMapVo();
		if (StringUtils.isBlank(orderRecords)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("医嘱执行信息不能为空-orderRecords");
			return json;
		}

		Type type = new TypeToken<List<OrderGroup>>() {
		}.getType();
		List<OrderGroup> lst = GsonUtils.fromJson(orderRecords, type);
		if (lst == null) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("医嘱执行信息不能为空-orderRecords");
			return json;
		}

		Map<String, String> records = new HashMap<String, String>();
		for (Iterator<OrderGroup> iterator = lst.iterator(); iterator.hasNext();) {
			OrderGroup orderGroup = iterator.next();
			String value = records.get(orderGroup.getPatientId());
			if (value == null) {
				records.put(orderGroup.getPatientId(),
						orderGroup.getOrderGroupNo());
			} else {
				value = value + "," + orderGroup.getOrderGroupNo();
				records.put(orderGroup.getPatientId(), value);
			}
		}
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		try {

			orderService.batchExecMultPatOrderGroupWithPDA(records,
					sessionUserInfo, approveNurseCode, approveNurseName);
			json.setRslt(ResultCst.SUCCESS);
			return json;
		} catch (AlertException e) {
			json = handlerExceptionMsg(json, orderRecords, null,
					sessionUserInfo, null, e.getMessage(),
					ResultCst.ALERT_ERROR, true);
			return json;
		} catch (Exception ex) {
			ex.printStackTrace();
			json = handlerExceptionMsg(json, orderRecords, null,
					sessionUserInfo, null, ex.getMessage(),
					ResultCst.SYSTEM_ERROR, true);
			return json;
		}
	}

	/**
	 * 医嘱管理_获取医嘱查看列表
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 *            医嘱分类（CZ,LZ）
	 * @param orderExecTypeCode
	 *            医嘱执行分类(输液，口服药等)
	 * @param status
	 *            医嘱状态(待执行，已执行，停止)
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getAllOrderList")
	public @ResponseBody BaseMapVo getAllOrderList(String patientId,
			String orderTypeCode, String orderExecTypeCode, String startDate,
			String endDate) throws Exception {
		if (StringUtils.isBlank(patientId)) {
			throw new IllegalArgumentException("病人ID不能为空-patientId");
		}
		LOGGER.debug("patientId:" + patientId + ";orderTypeCode:"
				+ orderTypeCode + ";orderExecTypeCode:" + orderExecTypeCode
				+ ";startDate:" + startDate + ";endDate:" + endDate);
		Date sTime = null;
		Date eTime = null;
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			// 获取当前时间和前24小时区间
			sTime = DateUtil.parse(startDate.substring(0, 10), DateFormat.YMD);
			// 检验类医嘱，起始日期往前推一定天数
			if (MnisConstants.ORDER_EXEC_TYPE_LAB.equals(orderExecTypeCode)) {
				String beforeDay = identityService
						.getConfigure(MnisConstants.LAB_BEFORE_DAY);
				if (!StringUtils.isEmpty(beforeDay)) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(sTime);
					cal.add(Calendar.DATE, -Integer.parseInt(beforeDay));
					sTime = cal.getTime();
				}
			}
			eTime = DateUtil.parse(endDate.substring(0, 10), DateFormat.YMD);
		}

		List<OrderExecGroup> allOrderList = orderService
				.getOrderBaseGroupList(patientId, orderTypeCode,
						orderExecTypeCode, null, sTime, eTime);

		BaseMapVo json = new BaseMapVo();
		json.addData("list", allOrderList);
		// 只针对输液医嘱
		if (orderExecTypeCode != null
				&& MnisConstants.ORDER_EXEC_TYPE_INFUSION
						.equals(orderExecTypeCode)) {
			// 根据患者和医嘱获取巡视记录
			List<String> orderExecList = new ArrayList<String>();
			for (OrderExecGroup orderExecGroup : allOrderList) {
				orderExecList.add(orderExecGroup.getOrderExecBarcode());
			}
			json.addData("infusionRecordList", infusionmonitorService
					.getInfusionMonitorRecordList(patientId, orderExecList,
							sTime, eTime));
		}
		json.setRslt(ResultCst.SUCCESS);
		LOGGER.debug("getAllOrderList" + GsonUtils.toJson(json));
		return json;
	}

	/**
	 * 医嘱管理_获取医嘱查看列表
	 * 
	 * @param patientId
	 * @param orderTypeCode
	 *            医嘱分类（CZ,LZ）
	 * @param orderExecTypeCode
	 *            医嘱执行分类(输液，口服药等)
	 * @param status
	 *            医嘱状态(待执行，已执行，停止)
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getOriginalOrderList")
	public @ResponseBody BaseMapVo getOriginalOrderList(String patientId,
			String orderTypeCode, String orderExecTypeCode, String startDate,
			String endDate) throws Exception {
		if (StringUtils.isBlank(patientId)) {
			throw new IllegalArgumentException("病人ID不能为空-patientId");
		}
		LOGGER.debug("patientId:" + patientId + ";orderTypeCode:"
				+ orderTypeCode + ";orderExecTypeCode:" + orderExecTypeCode
				+ ";startDate:" + startDate + ";endDate:" + endDate);

		Date sTime = null;
		Date eTime = null;
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			// 获取当前时间和前24小时区间
			sTime = DateUtil.parse(startDate.substring(0, 10), DateFormat.YMD);
			eTime = DateUtil.parse(endDate.substring(0, 10), DateFormat.YMD);
		}

		List<HisOrderGroup> hisOrderGroups = orderService.getOriginalOrderList(
				patientId, orderTypeCode, orderExecTypeCode, sTime, eTime);
		BaseMapVo json = new BaseMapVo();
		json.addData("list", hisOrderGroups);
		json.setRslt(ResultCst.SUCCESS);
		LOGGER.debug(GsonUtils.toJson(json));
		return json;
	}

	/**
	 * 获取医嘱床位信息
	 * 
	 * @param nurseId
	 *            护士Id
	 * @param deptId
	 *            部门Id(关注：护士id不为null,全部：护士id为null)
	 * @param orderTypeCode
	 *            医嘱分类(长嘱，临嘱)
	 * @param orderExecTypeCode
	 *            医嘱执行类型(输液，口服药等)
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/getOrderBedInfoList")
	public @ResponseBody BaseMapVo getOrderBedInfoList(String nurseId,
			String deptId, String orderTypeCode, String orderExecTypeCode,
			String startDate, String endDate, boolean isOriginal) {
		LOGGER.debug("nurseId:" + nurseId + ";orderTypeCode:" + orderTypeCode
				+ ";orderExecTypeCode:" + orderExecTypeCode + ";startDate:"
				+ startDate + ";endDate:" + endDate + ";isOriginal:"
				+ isOriginal);
		List<String> patientIdList = null;
		if (!StringUtils.isBlank(nurseId)) {
			patientIdList = patientService.getPatientByDeptCodeOrUserCode(
					nurseId, deptId);
		}

		Date sTime = null;
		Date eTime = null;
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {

			// 获取当前时间和前24小时区间
			sTime = DateUtil.parse(startDate.substring(0, 10), DateFormat.YMD);
			// 检验类医嘱，起始日期往前推一定天数
			if (MnisConstants.ORDER_EXEC_TYPE_LAB.equals(orderExecTypeCode)) {
				String beforeDay = identityService
						.getConfigure(MnisConstants.LAB_BEFORE_DAY);
				if (!StringUtils.isEmpty(beforeDay)) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(sTime);
					cal.add(Calendar.DATE, -Integer.parseInt(beforeDay));
					sTime = cal.getTime();
				}
			}
			eTime = DateUtil.parse(endDate.substring(0, 10), DateFormat.YMD);
		}
		BaseMapVo json = new BaseMapVo();
		json.addData("list", orderService.getOrderBedInfoList(patientIdList,
				deptId, orderTypeCode, orderExecTypeCode, sTime, eTime,
				isOriginal));
		json.setRslt(ResultCst.SUCCESS);
		LOGGER.debug("getOrderBedInfoList" + GsonUtils.toJson(json));
		return json;
	}

	/**
	 * 医嘱排序
	 * 
	 * @param newOrderGroupId
	 *            待排序医嘱id(新排序号为oldOrderGroupId医嘱号)
	 * @param oldOrderGroupId
	 *            被排序医嘱id(新排序号为原newOrderGroupId医嘱号)
	 * @return
	 */
	@RequestMapping("/updateOrderNoByOrderGroupIds")
	public @ResponseBody BaseMapVo updateOrderNoOrderGroupIds(
			String newOrderGroupId, String oldOrderGroupId) {
		BaseMapVo json = new BaseMapVo();
		try {
			if (orderService.updateOrderNoByOrderGroupIds(newOrderGroupId,
					oldOrderGroupId) > 0) {
				json.setRslt(ResultCst.SUCCESS);
			} else {
				json.setRslt(ResultCst.FAILURE);
				json.setMsg("更新排序失败!");
			}
		} catch (MnisException e) {
			json.setRslt(ResultCst.FAILURE);
			json.setMsg(e.getMessage());
		}

		return json;
	}

	/**
	 * 获取医嘱未打印信息
	 * 
	 * @param deptCode
	 * @param orderTypeCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@RequestMapping("/getUnprintOrderStatisticList")
	public @ResponseBody BaseDataVo getUnprintOrderStatisticList(
			@RequestParam(value = "deptCode", required = true) String deptCode,
			String orderTypeCode, String startDate, String endDate) {
		BaseDataVo json = new BaseDataVo();
		Date sTime = null;
		Date eTime = null;
		if (!StringUtils.isBlank(startDate) && !StringUtils.isBlank(endDate)) {
			// 获取当前时间和前24小时区间
			sTime = DateUtil.parse(startDate.substring(0, 10), DateFormat.YMD);
			eTime = DateUtil.parse(endDate.substring(0, 10), DateFormat.YMD);
		}

		List<OrderUnprintStatistic> orderUnprintStatistics = orderService
				.getUnprintOrderStatisticList(deptCode, orderTypeCode, sTime,
						eTime);

		json.setRslt(ResultCst.SUCCESS);
		json.setData(orderUnprintStatistics);
		return json;
	}

	@RequestMapping("/getOrderPrintInfos")
	@ResponseBody
	public BaseMapVo getOrderPrintInfos(String deptCode, String orderTypeCode,
			String orderExecTypeCode, String startDate, String orderPrint,
			String patIds) {
		List<String> patientIds = null;
		if (!StringUtils.isEmpty(patIds)) {
			patientIds = new ArrayList<String>();
			String[] patIdArr = patIds.split(",");
			for (int i = 0; i < patIdArr.length; i++) {
				if (!StringUtils.isEmpty(patIdArr[i])) {
					patientIds.add(patIdArr[i]);
				}
			}
		}

		Date sTime = null;
		Date eTime = null;
		if (StringUtils.isEmpty(startDate)) {
			// 获取当前时间和前24小时区间
			Date[] dates = DateUtil.getQueryRegionDates(new Date());
			sTime = dates[0];
			eTime = dates[1];
		} else {
			sTime = DateUtil.parse(startDate, DateFormat.YMD);
			eTime = DateUtil.setNextDayToDate(sTime);
		}
		BaseMapVo json = new BaseMapVo();
		json.addData("list", orderService.getOrderPrintInfos(deptCode,
				orderTypeCode, orderExecTypeCode, sTime, eTime, orderPrint,
				patientIds));
		json.setRslt(ResultCst.SUCCESS);
		return json;
	}

	@RequestMapping("/saveDocReport")
	@ResponseBody
	public BaseMapVo saveDocReport(String patientId, String recodValue) {
		LOGGER.debug("patientId" + patientId + "recodValue" + recodValue);
		BaseMapVo json = new BaseMapVo();// 数据返回对象
		try {
			// 获取录入护士
			UserInformation sessionUserInfo = WebContextUtils
					.getSessionUserInfo();
			DocReportEventEntity docReportEventEntity = new DocReportEventEntity();
			docReportEventEntity.setCreate_person(sessionUserInfo.getName());// 录入护士的姓名
			docReportEventEntity.setInpatient_no(patientId);// 患者ID
			docReportEventEntity.setDept_code(sessionUserInfo.getDeptCode());// 科室编号
			// 反抄明细信息
			List<DocReportEventItemEntity> docReportEventItemEntities = new ArrayList<DocReportEventItemEntity>();
			DocReportEventItemEntity valueDocReportEventItemEntity = new DocReportEventItemEntity();
			valueDocReportEventItemEntity.setRecord_value(String
					.valueOf(recodValue));
			valueDocReportEventItemEntity.setTemplate_item_id("remark1");
			docReportEventItemEntities.add(valueDocReportEventItemEntity);
			docReportEventEntity.setData_item(docReportEventItemEntities);
			orderService.publishDocReportByManual(docReportEventEntity);
			json.setRslt(ResultCst.SUCCESS);
		} catch (Exception e) {
			LOGGER.debug(e.toString());
			json.setRslt(ResultCst.FAILURE);
			json.setMsg(e.getMessage());
		}
		LOGGER.debug(GsonUtils.toJson(json));
		return json;
	}

	/**
	 * 医嘱执行单NDA--执行单
	 * 
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @param execType
	 *            0全部，1已执行，2未执行
	 * @param docType
	 *            0普通,1执行单
	 * @return
	 */
	@RequestMapping("/getOrdExecDocInfosToNda")
	@ResponseBody
	public BaseMapVo getOrdExecDocInfosToNda(String deptCode, String patIds,
			String startDate, String endDate, int execType) {
		BaseMapVo vo = new BaseMapVo();
		List<String> patIdList = null;
		if (StringUtils.isNotBlank(patIds)) {
			patIdList = new ArrayList<String>();
			Collections.addAll(patIdList, patIds.split(","));
		}
		try {
			// 执行医嘱
			List<OrderExecGroup> ordExecs = orderService
					.getOrdExecDocInfosToNda(deptCode, patIdList, startDate,
							endDate, execType);
			vo.addData("ordExecs", ordExecs);
			vo.setRslt(ResultCst.SUCCESS);
			return vo;
		} catch (Exception e) {
			LOGGER.error("PatientOrderDetailAction getOrdExecDocInfosToNdao error:"
					+ e.getMessage());
			vo.setRslt(ResultCst.ALERT_ERROR);
			vo.setMsg("获取数据失败!");
			return vo;
		}

	}

	/**
	 * 医嘱执行单--执行单
	 * 
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @param execType
	 *            0全部，1已执行，2未执行
	 * @param docType
	 *            0普通,1执行单
	 * @return
	 */
	@RequestMapping("/getOrdExecDocInfos")
	@ResponseBody
	public BaseMapVo getOrdExecDocInfos(String deptCode, String patIds,
			String startDate, String endDate, int execType, int docType) {
		BaseMapVo vo = new BaseMapVo();
		List<String> patIdList = null;
		if (StringUtils.isNotBlank(patIds)) {
			patIdList = new ArrayList<String>();
			Collections.addAll(patIdList, patIds.split(","));
		}
		try {
			// 执行医嘱
			List<OrderExecDocumentInfo> ordExecs = orderService
					.getOrdExecDocInfos(deptCode, patIdList, startDate,
							endDate, execType, docType);

			vo.addData("ordExecs", ordExecs);
			return vo;
		} catch (Exception e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
			return vo;
		}

	}

	/**
	 * 医嘱执行单--输液卡
	 * 
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @return
	 */
	@RequestMapping("/getOrdExecDocInfosOnInfuCard")
	@ResponseBody
	public BaseMapVo getOrdExecDocInfosOnInfuCard(String deptCode,
			String patIds, String startDate, String endDate) {
		BaseMapVo vo = new BaseMapVo();
		List<String> patIdList = null;
		if (StringUtils.isNotBlank(patIds)) {
			patIdList = new ArrayList<String>();
			Collections.addAll(patIdList, patIds.split(","));
		}
		try {
			// 执行医嘱
			List<OrderExecDocumentInfo> ordExecs = orderService
					.getOrdExecDocInfosOnInfuCard(deptCode, patIdList,
							startDate, endDate);

			vo.addData("ordExecs", ordExecs);
			return vo;
		} catch (Exception e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
			return vo;
		}
	}

	/**
	 * 医嘱执行单--瓶签
	 * 
	 * @param deptCode
	 * @param patIds
	 * @param queryDate
	 * @return
	 */
	@RequestMapping("/getOrdExecDocInfosOnLabel")
	@ResponseBody
	public BaseMapVo getOrdExecDocInfosOnLabel(String deptCode, String patIds,
			String startDate, String endDate) {
		BaseMapVo vo = new BaseMapVo();
		List<String> patIdList = null;

		if (StringUtils.isNotBlank(patIds)) {
			patIdList = new ArrayList<String>();
			Collections.addAll(patIdList, patIds.split(","));
		}
		try {
			// 执行医嘱
			List<OrderExecDocumentInfo> ordExecs = orderService
					.getOrdExecDocInfosOnLabel(deptCode, patIdList, startDate,
							endDate);

			vo.addData("ordExecs", ordExecs);
			return vo;
		} catch (Exception e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
			return vo;
		}
	}

	/**
	 * 获取医嘱执行类型和用法类型
	 * 
	 * @return
	 */
	@RequestMapping("/getOrderConfigData")
	@ResponseBody
	public BaseMapVo getOrderConfigData() {
		BaseMapVo vo = new BaseMapVo();
		// 医嘱执行类型
		Map<String, Object> ordExecTypes = orderService.getOrderExecTypes();
		// 医嘱用法类型
		Map<String, Object> ordUsageTypes = orderService.getOrderUsageTypes();

		vo.addData("ordExecTypes", ordExecTypes);
		vo.addData("ordUsageTypes", ordUsageTypes);
		return vo;
	}

	@RequestMapping("/saveOrdExecDocPrtInfo")
	@ResponseBody
	public BaseMapVo saveOrdExecDocPrtInfo(String ordExecDocPrtInfos) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(ordExecDocPrtInfos)) {
			throw new MnisException("传入的数据为空");
		}
		try {
			Type type = new TypeToken<List<OrderExecDocumentPrintInfo>>() {
			}.getType();
			List<OrderExecDocumentPrintInfo> printInfos = fromJson(
					ordExecDocPrtInfos, type);

			List<String> printIds = orderService
					.saveOrdExecDocPrtInfs(printInfos);

			vo.addData("printIds", printIds);
		} catch (Exception e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
			return vo;
		}
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("打印成功!");
		return vo;
	}

	@RequestMapping("/savePatOrderRemark")
	@ResponseBody
	public BaseMapVo savePatOrderRemark(String barcode, String value, int type) {
		BaseMapVo vo = new BaseMapVo();

		orderService.savePatOrderRemark(barcode, value, type);

		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("备注已添加!");
		return vo;
	}

	@RequestMapping("/saveOrdApprove")
	@ResponseBody
	public BaseMapVo saveOrdApprove(String ordApproves) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(ordApproves)) {
			throw new MnisException("传入的数据为空");
		}
		try {
			Type type = new TypeToken<List<OrderApprove>>() {
			}.getType();
			List<OrderApprove> orderApproves = fromJson(ordApproves, type);

			orderService.batchSaveOrderApprove(orderApproves);

		} catch (Exception e) {
			vo.setRslt(ResultCst.FAILURE);
			vo.setMsg(e.getMessage());
			return vo;
		}
		vo.setRslt(ResultCst.SUCCESS);
		vo.setMsg("核对成功!");
		return vo;
	}

	/**
	 * 处理BaseMapVo异常消息
	 * 
	 * @param json
	 * @param barcode
	 * @param deptCode
	 * @param sessionUserInfo
	 * @param msg
	 * @return
	 */
	private BaseMapVo handlerExceptionMsg(BaseMapVo json, String barcode,
			String patId, UserInformation sessionUserInfo,
			String execOrderType, String msg, String rslt, boolean isScan) {
		json.setRslt(rslt);
		json.setMsg(StringUtil.getErrorMsg(msg));
		if (isScan) {
			nurseScanService.handleNurseScan(barcode, patId,
					sessionUserInfo.getDeptCode(), msg,
					sessionUserInfo.getCode(), sessionUserInfo.getName(),
					execOrderType, 0);
		}
		return json;
	}

	/**
	 * 处理BaseDataVo异常消息
	 * 
	 * @param json
	 * @param barcode
	 * @param deptCode
	 * @param sessionUserInfo
	 * @param msg
	 * @return
	 */
	private BaseDataVo handlerExceptionMsg(BaseDataVo json, String barcode,
			String patId, UserInformation sessionUserInfo,
			String execOrderType, String msg, String rslt, boolean isScan) {
		json.setRslt(rslt);
		json.setMsg(msg);
		json.setMsg(StringUtil.getErrorMsg(msg));
		if (isScan) {
			nurseScanService.handleNurseScan(barcode, patId,
					sessionUserInfo.getDeptCode(), msg,
					sessionUserInfo.getCode(), sessionUserInfo.getName(),
					execOrderType, 0);
		}
		return json;
	}

}
