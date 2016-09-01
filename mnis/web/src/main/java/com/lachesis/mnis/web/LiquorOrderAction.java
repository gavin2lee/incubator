package com.lachesis.mnis.web;

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

import com.lachesis.mnis.core.NurseScanService;
import com.lachesis.mnis.core.OrderLiquorService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.identity.entity.UserInformation;
import com.lachesis.mnis.core.liquor.entity.OrderLiquor;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorStatistic;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecord;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.GsonUtils;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.web.common.ResultCst;
import com.lachesis.mnis.web.common.util.WebContextUtils;
import com.lachesis.mnis.web.common.vo.BaseMapVo;

@Controller
@RequestMapping("/nur/liquorOrder")
public class LiquorOrderAction {
	static final Logger LOGGER = LoggerFactory
			.getLogger(LiquorOrderAction.class);

	@Autowired
	private OrderLiquorService orderLiquorService;

	@Autowired
	private NurseScanService nurseScanService;

	/**
	 * 进入配液单主页面
	 * 
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/{id}/liquorOrderMain")
	public String liquorOrderMain(@PathVariable String id, Model model) {
		model.addAttribute("currentDate", DateUtil.format(DateFormat.YMD));
		model.addAttribute(MnisConstants.ID, id);
		return "/nur/liquorOrder";
	}

	/**
	 * -----------------------------配液 API FOR NDA
	 * --------------------------------------
	 */
	@RequestMapping("/queryAll")
	@ResponseBody
	public BaseMapVo query(
			@RequestParam(value = "deptId", required = true) String deptId,
			String liquorStatus) {

		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(deptId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("部门ID 为空");
			return json;
		}

		List<OrderLiquor> listOrders = null;
		listOrders = orderLiquorService.getOrderLiquorList(deptId,
				liquorStatus, false);
		json.setRslt(ResultCst.SUCCESS);
		json.setMsg("");
		json.addData("list", listOrders);
		LOGGER.debug(GsonUtils.toJson(json));

		return json;
	}

	/**
	 * 根据护士Id获取配液信息
	 * 
	 * @param deptId
	 * @return
	 */
	@RequestMapping("/queryAllById")
	@ResponseBody
	public BaseMapVo queryByNurseId(
			@RequestParam(value = "deptId", required = true) String deptId,
			String liquorStatus) {

		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(deptId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("部门ID 为空");
			return json;
		}

		List<OrderLiquor> listOrders = null;
		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		listOrders = orderLiquorService.getOrderLiquorListByNurseId(deptId,
				sessionUserInfo.getCode());
		LOGGER.debug(listOrders + "");
		json.setRslt(ResultCst.SUCCESS);
		json.setMsg("");
		json.addData("list", listOrders);

		return json;
	}

	@RequestMapping("/getOrderLiquorStatistic")
	@ResponseBody
	public BaseMapVo getOrderLiquorStatistic(
			@RequestParam(value = "deptId", required = true) String deptId) {

		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(deptId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("部门ID 为空");
			return json;
		}

		OrderLiquorStatistic orderLiquorStatistic = orderLiquorService
				.getOrderLiquorStatistic(deptId, null, null);
		json.setRslt(ResultCst.SUCCESS);
		json.setMsg("");
		json.addData("statistic", orderLiquorStatistic);

		return json;
	}

	/**
	 * 配药执行(三个单独接口通过execType合并)
	 * 
	 * @param execOrderId
	 *            :条码
	 * @param deptId
	 *            ：科室
	 * @param execType
	 *            ：类型：0：备药,1:审核,2:执行
	 * @param isGetData
	 * @return
	 */
	@RequestMapping("/execLiquor")
	@ResponseBody
	public BaseMapVo execLiquor(
			@RequestParam(value = "execOrderId", required = true) String execOrderId,
			@RequestParam(value = "deptId", required = true) String deptId,
			int execType, boolean isGetData) {
		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(execOrderId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("执行医嘱ID为空");
			return json;
		}

		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();

		try {
			// 执行配药
			orderLiquorService.execLiquor(execOrderId,
					sessionUserInfo.getCode(), sessionUserInfo.getName(),
					execType);

			OrderLiquorStatistic orderLiquorStatistic = orderLiquorService
					.getOrderLiquorStatistic(deptId, null, null);
			List<OrderLiquor> orderLiquors = null;
			if (isGetData) {
				orderLiquors = orderLiquorService.getLiqourOrderByOrderGroupId(
						execOrderId, deptId, null);
			}
			json.setRslt(ResultCst.SUCCESS);
			json.setMsg("");
			json.addData("list", orderLiquors);
			json.addData("statistic", orderLiquorStatistic);

			nurseScanService.handleNurseScan(execOrderId, null, deptId, null,
					sessionUserInfo.getCode(), sessionUserInfo.getName(),
					MnisConstants.SCAN_LIQUOR, 1);
		} catch (AlertException e) {
			LOGGER.error("prepare:" + e.getMessage());
			json = handlerExceptionMsg(json, execOrderId, deptId,
					sessionUserInfo, e.getMessage(), ResultCst.ALERT_ERROR);
		} catch (Exception e) {
			LOGGER.error("prepare:" + e.getMessage());
			json = handlerExceptionMsg(json, execOrderId, deptId,
					sessionUserInfo, e.getMessage(), ResultCst.SYSTEM_ERROR);
		}
		return json;
	}

	/**
	 * 备药
	 * 
	 * @param execOrderId
	 * @return
	 */
	@RequestMapping("/prepare")
	@ResponseBody
	public BaseMapVo prepare(
			@RequestParam(value = "execOrderId", required = true) String execOrderId,
			@RequestParam(value = "deptId", required = true) String deptId,
			boolean isGetData) {
		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(execOrderId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("执行医嘱ID为空");
			return json;
		}

		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();

		try {

			orderLiquorService.execOrderPrepare(execOrderId,
					sessionUserInfo.getCode(), sessionUserInfo.getName());
			OrderLiquorStatistic orderLiquorStatistic = orderLiquorService
					.getOrderLiquorStatistic(deptId, null, null);
			List<OrderLiquor> orderLiquors = null;
			if (isGetData) {
				orderLiquors = orderLiquorService.getLiqourOrderByOrderGroupId(
						execOrderId, deptId, null);
			}
			json.setRslt(ResultCst.SUCCESS);
			json.setMsg("");
			json.addData("list", orderLiquors);
			json.addData("statistic", orderLiquorStatistic);
			nurseScanService.handleNurseScan(execOrderId, null, deptId, null,
					sessionUserInfo.getCode(), sessionUserInfo.getName(),
					MnisConstants.SCAN_LIQUOR, 1);
		} catch (AlertException e) {
			LOGGER.error("prepare:" + e.getMessage());
			json = handlerExceptionMsg(json, execOrderId, deptId,
					sessionUserInfo, e.getMessage(), ResultCst.ALERT_ERROR);
		}
		return json;
	}

	/**
	 * 审核
	 * 
	 * @param execOrderId
	 * @return
	 */
	@RequestMapping("/verify")
	@ResponseBody
	public BaseMapVo verify(
			@RequestParam(value = "execOrderId", required = true) String execOrderId,
			@RequestParam(value = "deptId", required = true) String deptId,
			boolean isGetData) {
		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(execOrderId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("执行医嘱ID为空");
			return json;
		}

		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();

		try {
			orderLiquorService.execOrderVerify(execOrderId,
					sessionUserInfo.getCode(), sessionUserInfo.getName());
			OrderLiquorStatistic orderLiquorStatistic = orderLiquorService
					.getOrderLiquorStatistic(deptId, null, null);
			List<OrderLiquor> orderLiquors = null;
			if (isGetData) {
				orderLiquors = orderLiquorService.getLiqourOrderByOrderGroupId(
						execOrderId, deptId, null);
			}
			json.setRslt(ResultCst.SUCCESS);
			json.setMsg("");
			json.addData("list", orderLiquors);
			json.addData("statistic", orderLiquorStatistic);
			nurseScanService.handleNurseScan(execOrderId, null, deptId, null,
					sessionUserInfo.getCode(), sessionUserInfo.getName(),
					MnisConstants.SCAN_LIQUOR, 1);
		} catch (AlertException e) {
			LOGGER.error("verify:" + e.getMessage());
			json = handlerExceptionMsg(json, execOrderId, deptId,
					sessionUserInfo, e.getMessage(), ResultCst.ALERT_ERROR);
		}
		return json;
	}

	/**
	 * 配液
	 * 
	 * @param execOrderId
	 * @return
	 */
	@RequestMapping("/exec")
	@ResponseBody
	public BaseMapVo exec(
			@RequestParam(value = "execOrderId", required = true) String execOrderId,
			@RequestParam(value = "deptId", required = true) String deptId,
			boolean isGetData) {
		BaseMapVo json = new BaseMapVo();

		if (StringUtils.isEmpty(execOrderId)) {
			json.setRslt(ResultCst.INVALID_PARAMETER);
			json.setMsg("执行医嘱ID为空");
			return json;
		}

		UserInformation sessionUserInfo = WebContextUtils.getSessionUserInfo();
		try {
			orderLiquorService.execOrderLiquor(execOrderId,
					sessionUserInfo.getCode(), sessionUserInfo.getName());
			OrderLiquorStatistic orderLiquorStatistic = orderLiquorService
					.getOrderLiquorStatistic(deptId, null, null);
			List<OrderLiquor> orderLiquors = null;
			if (isGetData) {
				orderLiquors = orderLiquorService.getLiqourOrderByOrderGroupId(
						execOrderId, deptId, null);
			}
			json.setRslt(ResultCst.SUCCESS);
			json.setMsg("");
			json.addData("list", orderLiquors);
			json.addData("statistic", orderLiquorStatistic);
			nurseScanService.handleNurseScan(execOrderId, null, deptId, null,
					sessionUserInfo.getCode(), sessionUserInfo.getName(),
					MnisConstants.SCAN_LIQUOR, 1);
		} catch (AlertException e) {
			LOGGER.error("liquor:" + e.getMessage());
			json = handlerExceptionMsg(json, execOrderId, deptId,
					sessionUserInfo, e.getMessage(), ResultCst.ALERT_ERROR);
		}
		return json;
	}

	@RequestMapping("/getOrderUnExecRecords")
	@ResponseBody
	public BaseMapVo getOrderUnExecRecords(String deptCode, String nurseCode,
			String startDate, String endDate) {
		BaseMapVo vo = new BaseMapVo();

		Date start = null;
		Date end = null;
		if (StringUtils.isNotBlank(startDate)
				&& StringUtils.isNotBlank(endDate)) {
			start = DateUtil.parse(startDate);
			end = DateUtil.parse(endDate);
		}

		List<OrderUnExecRecord> orderUnExecRecords = orderLiquorService
				.getOrderUnExecRecords(deptCode, nurseCode, null, start, end);
		vo.addData("records", orderUnExecRecords);
		return vo;
	}

	@RequestMapping("/saveOrderUnExecRecordItem")
	@ResponseBody
	public BaseMapVo saveOrderUnExecRecordItem(String barcode, String deptCode,
			String nurseCode, String nurseName) {
		BaseMapVo vo = new BaseMapVo();
		if (StringUtils.isBlank(deptCode) || StringUtils.isBlank(nurseCode)
				|| StringUtils.isBlank(nurseName)) {
			UserInformation userInformation = WebContextUtils
					.getSessionUserInfo();
			deptCode = userInformation.getDeptCode();
			nurseCode = userInformation.getCode();
			nurseName = userInformation.getName();
		}
		int recordId = orderLiquorService.insertOrderUnExecRecordItem(barcode,
				nurseCode, nurseName, deptCode);
		if (recordId == 0) {
			vo.setMsg("保存失败!");
			vo.setMsg(ResultCst.ALERT_ERROR);
		} else {
			vo.addData("records", orderLiquorService.getOrderUnExecRecords(
					deptCode, null, String.valueOf(recordId), null, null));
			vo.setMsg("保存成功!");
			vo.setMsg(ResultCst.SUCCESS);
		}

		return vo;
	}

	/**
	 * 处理异常消息
	 * 
	 * @param json
	 * @param barcode
	 * @param deptCode
	 * @param sessionUserInfo
	 * @param msg
	 * @return
	 */
	private BaseMapVo handlerExceptionMsg(BaseMapVo json, String barcode,
			String deptCode, UserInformation sessionUserInfo, String msg,
			String rslt) {
		json.setRslt(rslt);
		json.setMsg(msg);
		json.setMsg(StringUtil.getErrorMsg(msg));
		nurseScanService.handleNurseScan(barcode, null, deptCode, msg,
				sessionUserInfo.getCode(), sessionUserInfo.getName(), MnisConstants.SCAN_LIQUOR,
				0);
		return json;
	}

}
