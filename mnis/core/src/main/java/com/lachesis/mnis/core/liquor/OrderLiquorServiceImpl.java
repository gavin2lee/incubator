package com.lachesis.mnis.core.liquor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lachesis.mnis.core.IdentityService;
import com.lachesis.mnis.core.OrderLiquorService;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.PatientService;
import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.exception.AlertException;
import com.lachesis.mnis.core.exception.MnisException;
import com.lachesis.mnis.core.liquor.entity.DrugItem;
import com.lachesis.mnis.core.liquor.entity.OrderLiquor;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorItem;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorStatistic;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecord;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecordItem;
import com.lachesis.mnis.core.liquor.repository.OrderLiquorRepository;
import com.lachesis.mnis.core.order.entity.HttpResponeMsg;
import com.lachesis.mnis.core.order.entity.OrderItem;
import com.lachesis.mnis.core.util.BarCodeVo;
import com.lachesis.mnis.core.util.BarcodeUtil;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.StringUtil;
import com.lachesis.mnis.core.util.WebServiceUtil;

@Transactional
@Service("orderLiquorService")
public class OrderLiquorServiceImpl implements OrderLiquorService {

	private static Logger LOGGER = LoggerFactory.getLogger(OrderLiquorServiceImpl.class);
	
	@Autowired
	private OrderLiquorRepository orderLiquorRepository;

	@Autowired
	private PatientService patientService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private IdentityService identityService;

	@Override
	public List<OrderLiquor> getOrderLiquorList(String deptId,
			String liquorState, boolean isAllStatus) {

		Date curDate = new Date();
		Date startDate = DateUtil.setDateToDay(curDate);
		Date endDate = DateUtil.setNextDayToDate(DateUtils
				.addDays(startDate, 1));

		if (StringUtils.isBlank(liquorState)
				|| "NULL".equals(liquorState.toUpperCase())) {
			liquorState = null;
		} else {
			liquorState = liquorState.toUpperCase();
		}
		//配置是否长嘱还是临嘱
		String orderTypeCode = identityService.getLiquorOrderType();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("patientIds", null);
		params.put("deptId", deptId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("liquorStatus", liquorState);
		params.put("isAllStatus", isAllStatus);
		params.put("orderTypeCode", orderTypeCode);

		List<OrderLiquor> orderLiquors = orderLiquorRepository.getOrderLiquorList(params);
		//配液类型
		Map<String, Object> liquorMap = orderService.getCopyOrderExecTypes(MnisConstants.COM_DIC_USAGE_LIQUOR);
		//包药机
		if(liquorMap.containsKey(MnisConstants.ORDER_EXEC_TYPE_ORAL)){
			orderLiquors.addAll(orderLiquorRepository.getDrugBagLiquorList(params));
		}
		return orderLiquors;
	}
	
	@Override
	public void execLiquor(String execOrderId, String liquorNurseCode,
			String liquorNurseName, int execType) {
		
		switch (execType) {
		case 0:
			//备药
			execOrderPrepare(execOrderId,liquorNurseCode,liquorNurseName);
			break;
		case 1:
			//审核
			execOrderVerify(execOrderId,liquorNurseCode,liquorNurseName);
			break;
		case 2:
			//完成
			execOrderLiquor(execOrderId,liquorNurseCode,liquorNurseName);
			break;
		default:
			break;
		}

	}

	@Override
	public void execOrderPrepare(String execOrderId, String prepareNurseCode,
			String prepareNurseName) {
		//判断医嘱状态
		String orderStatus = orderService.getOrderStatusByOrderGroupId(execOrderId, "1", null);
		
		if(StringUtils.isBlank(orderStatus)){
			HttpResponeMsg msg = WebServiceUtil.webServiceRequest(identityService.isSyncHis(),
					identityService.getSyncHisIp(),MnisConstants.WEB_SERVICE_HIS_ALL_ORDER_DATA,
					execOrderId);
			if(msg == null){
				LOGGER.debug("Order liquor execOrderPrepare execOrderId :" + execOrderId + ";orderstatus:" + orderStatus);
				
				throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
						MnisConstants.ORD_NOT_EXIST, "医嘱不存在,无需备药!"));
			}else{
				orderStatus = orderService.getOrderStatusByOrderGroupId(execOrderId, "1", null);
			}
		}
		
		if("3".equals(orderStatus)){
			LOGGER.debug("Order liquor execOrderPrepare execOrderId :" + execOrderId + ";orderstatus:" + orderStatus);
			throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
					MnisConstants.ORD_STOP, "医嘱已停止,无需备药!"));
		}
		
		OrderLiquorItem checkItem = selectOrderLiquorByExecOrderId(execOrderId);

		if (checkItem != null) {
			throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
					MnisConstants.ORD_LIQ_PREPAREED, "该药物已备药!"));
		} else {
			checkItem = new OrderLiquorItem();
			checkItem.setExecOrderId(execOrderId);
			checkItem.setOrderId(execOrderId);
			checkItem.setPrepareNurseId(prepareNurseCode);
			checkItem.setPrepareNurseName(prepareNurseName);
			checkItem.setState(MnisConstants.LIQUOR_PREPARE);
			checkItem.setPrepareTime(new Date());

			if (insertOrderLiquorForPrepare(checkItem) == 0) {
				throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
						MnisConstants.ORD_LIQ_PREPAREED, "备药不成功!"));
			}
		}
	}

	@Override
	public void execOrderVerify(String execOrderId, String verifyNurseCode,
			String verifyNurseName) {
		OrderLiquorItem checkItem = selectOrderLiquorByExecOrderId(execOrderId);

		if (checkItem == null) {
			throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
					MnisConstants.ORD_LIQ_UN_PREPARE, "该药物未备药!"));
		}
		OrderLiquorItem operItem = new OrderLiquorItem();
		operItem.setExecOrderId(execOrderId);
		operItem.setOrderId(execOrderId);
		operItem.setVerifyNurseId(verifyNurseCode);
		operItem.setVerifyNurseName(verifyNurseName);
		operItem.setVerifyTime(new Date());
		operItem.setState(MnisConstants.LIQUOR_VERIFY);

		// 判断是否已经审核
		if (checkItem.getVerifyNurseId() == null) {
			if (updateOrderLiquorForVerify(operItem) == 0) {
				throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
						MnisConstants.ORD_LIQ_VERIFIED, "审核不成功!"));
			}
		} else {
			throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
					MnisConstants.ORD_LIQ_VERIFIED, "该药物已审核!!"));
		}
	}

	@Override
	public void execOrderLiquor(String execOrderId, String liquorNurseCode,
			String liquorNurseName) {
		OrderLiquorItem checkItem = selectOrderLiquorByExecOrderId(execOrderId);
		if (checkItem == null) {
			throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
					MnisConstants.ORD_LIQ_UN_PREPARE, "该药物未备药!"));
		}

		OrderLiquorItem item = new OrderLiquorItem();
		item.setExecOrderId(execOrderId);
		item.setOrderId(execOrderId);
		item.setExecNurseId(liquorNurseCode);
		item.setExecNurseName(liquorNurseName);
		item.setState(MnisConstants.LIQUOR_FINISH);
		item.setExecTime(new Date());

		// 没审核不可以配液

		if (checkItem.getState() == null
				|| MnisConstants.LIQUOR_PREPARE.equals(checkItem.getState())) {
			throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
					MnisConstants.ORD_LIQ_EXECED, "配液前请先审核"));
		}

		if (checkItem.getExecNurseId() == null) {
			if (updateOrderLiquor(item) == 0) {
				throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
						MnisConstants.ORD_LIQ_EXECED, "配液不成功!"));
			}

		} else {
			throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
					MnisConstants.ORD_LIQ_EXECED, "该药物已配液!"));
		}
	}

	@Override
	public int insertOrderLiquorForPrepare(OrderLiquorItem orderLiquor) {
		return orderLiquorRepository.insertOrderLiquorForPrepare(orderLiquor);
	}

	@Override
	public int insertOrderLiquorForExec(OrderLiquorItem orderLiquor) {
		return orderLiquorRepository.insertOrderLiquorForExec(orderLiquor);
	}

	@Override
	public int updateOrderLiquor(OrderLiquorItem orderLiquor) {
		return orderLiquorRepository.updateOrderLiquor(orderLiquor);
	}

	@Override
	public int updateOrderLiquorForVerify(OrderLiquorItem orderLiquor) {
		return orderLiquorRepository.updateOrderLiquorForVerify(orderLiquor);
	}

	@Override
	public OrderLiquorItem selectOrderLiquorByExecOrderId(String orderId) {
		return orderLiquorRepository.selectOrderLiquorByExecOrderId(orderId);
	}

	@Override
	public List<OrderLiquor> getOrderLiquorListByNurseId(String deptId,
			String nurseId) {
		List<String> patientIds = patientService
				.getPatientByDeptCodeOrUserCode(nurseId, deptId);
		Date curDate = new Date();
		Date startDate = DateUtil.setDateToDay(curDate);
		Date endDate = DateUtil.setNextDayToDate(DateUtils
				.addDays(startDate, 1));

		String orderTypeCode = identityService.getLiquorOrderType();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("patientIds", patientIds);
		params.put("deptId", deptId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("liquorStatus", null);
		params.put("isAllStatus", false);
		params.put("orderTypeCode", orderTypeCode);
		List<OrderLiquor> orderLiquors = orderLiquorRepository.getOrderLiquorList(params);
		//配液类型
		Map<String, Object> liquorMap = orderService.getCopyOrderExecTypes(MnisConstants.COM_DIC_USAGE_LIQUOR);
		//包药机
		if(liquorMap.containsKey(MnisConstants.ORDER_EXEC_TYPE_ORAL)){
			orderLiquors.addAll(orderLiquorRepository.getDrugBagLiquorList(params));
		}
		return orderLiquors;
	}

	@Override
	public int insertOrderLiquorForVerify(OrderLiquorItem orderLiquor) {
		return orderLiquorRepository.insertOrderLiquorForVerify(orderLiquor);
	}

	@Override
	public List<OrderLiquor> getLiqourOrderByOrderGroupId(String orderGroupId,
			String deptId, String liquorStatus) {
		if (StringUtils.isBlank(orderGroupId)) {
			return null;
		}
		String barCodeParams = identityService
				.getConfigure(MnisConstants.BARCODE);
		if (!StringUtils.isEmpty(barCodeParams)) {
			// 读取条码配置参数 为空不影响流程
			// 根据条码长度 配置中索引字符 判断条码类型
			BarCodeVo vo = BarcodeUtil.getBarCodeSet(barCodeParams,orderGroupId);
			orderGroupId = (vo != null ? vo.getBarCode() : null);
			if (orderGroupId == null) {
				throw new AlertException(StringUtil.createErrorMsg("LIQUOR", 
						MnisConstants.BAR_EMPTY, "无效条码!"));
			}
		}

		String patId = orderService.getPatientIdByOrderGroupId(orderGroupId,
				MnisConstants.ORDER_EXEC_TYPE_ORAL);
		if (StringUtils.isBlank(patId)) {
			return null;
		}
		List<String> patientIds = new ArrayList<String>();
		patientIds.add(patId);
		Date startDate = DateUtil.setDateToDay(new Date());
		Date endDate = DateUtil.setNextDayToDate(DateUtils
				.addDays(startDate, 1));

		if (StringUtils.isBlank(liquorStatus)
				|| "NULL".equals(liquorStatus.toUpperCase())) {
			liquorStatus = null;
		} else {
			liquorStatus = liquorStatus.toUpperCase();
		}

		String orderTypeCode = identityService.getLiquorOrderType();

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("patientIds", patientIds);
		params.put("deptId", deptId);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("liquorStatus", liquorStatus);
		params.put("isAllStatus", true);
		params.put("orderTypeCode", orderTypeCode);
		List<OrderLiquor> orderLiquors = orderLiquorRepository.getOrderLiquorList(params);
		//配液类型
		Map<String, Object> liquorMap = orderService.getCopyOrderExecTypes(MnisConstants.COM_DIC_USAGE_LIQUOR);
		//包药机
		if(liquorMap.containsKey(MnisConstants.ORDER_EXEC_TYPE_ORAL)){
			orderLiquors.addAll(orderLiquorRepository.getDrugBagLiquorList(params));
		}
		return orderLiquors;
	}

	@Override
	public OrderLiquorStatistic getOrderLiquorStatistic(String deptCode,
			Date startDate, Date endDate) {
		if (null == startDate || null == endDate) {
			startDate = DateUtil.setDateToDay(new Date());
			endDate = DateUtil
					.setNextDayToDate(DateUtils.addDays(startDate, 1));
		}
		String orderTypeCode = identityService.getLiquorOrderType();
		//配液类型
		Map<String, Object> liquorMap = orderService.getCopyOrderExecTypes(MnisConstants.COM_DIC_USAGE_LIQUOR);
		String type = null;
		if(liquorMap.containsKey(MnisConstants.ORDER_EXEC_TYPE_ORAL)){
			type = MnisConstants.ORDER_EXEC_TYPE_ORAL;
		}
		return orderLiquorRepository.getOrderLiquorStatistic(deptCode,
				startDate, endDate, orderTypeCode, type);
	}

	@Override
	public List<OrderUnExecRecord> getOrderUnExecRecords(String deptCode,
			String nurseCode,String recordId,Date startDate,Date endDate) {
		//查询是否关注
		List<String> patIds = new ArrayList<String>();
		
		if(StringUtils.isBlank(deptCode)){
			LOGGER.error("getOrdderUnExecRecords deptCode is null!");
			throw new MnisException("无效科室!");
		}
		
		if(StringUtils.isNotBlank(nurseCode)){
			patIds = patientService.getPatientByDeptCodeOrUserCode(nurseCode, deptCode);
		}
		
		if(null == startDate || null == endDate){
			Date[] dates = DateUtil.getQueryRegionDates(new Date());
			startDate = dates[0];
			endDate = dates[1];
		}
		List<OrderUnExecRecord> orderUnExecRecords = orderLiquorRepository
				.getOrderUnExecRecords(deptCode, patIds,recordId,startDate,endDate);
		//分解药物信息
		List<DrugItem> drugItems = null;
		List<OrderUnExecRecordItem> orderUnExecRecordItems = new ArrayList<OrderUnExecRecordItem>();
		for (OrderUnExecRecord orderUnExecRecord : orderUnExecRecords) {
			orderUnExecRecordItems  = orderUnExecRecord.getOrderUnExecRecordItems();
			for (OrderUnExecRecordItem orderUnExecRecordItem : orderUnExecRecordItems) {
				drugItems = new ArrayList<DrugItem>();
				String drugInfosStr = orderUnExecRecordItem.getDrugInfos();
				if(StringUtils.isBlank(drugInfosStr)){
					continue;
				}
				//药物间以(-;)隔开
				String[] drugInfoArray = drugInfosStr.split(MnisConstants.LINE_SEMI_COLON);
				for (String drugInfo : drugInfoArray) {
					//药物内部以(-:)隔开
					String[] drugInfos = drugInfo.split(MnisConstants.LINE_COLON);
					DrugItem drugItem = new DrugItem();
					drugItem.setDrugName(drugInfos[0]);
					drugItem.setDrugDosage(StringUtils.isNotBlank(drugInfos[1])?Float.valueOf(drugInfos[1]):0);
					drugItem.setDrugUnit(drugInfos[2]);
					drugItem.setDrugUsage(drugInfos[3]);
					drugItems.add(drugItem);
				}
				//设置具体药物
				orderUnExecRecordItem.setDrugItems(drugItems);
			}
		}
		return orderUnExecRecords;
	}

	@Override
	public int insertOrderUnExecRecordItem(String barcode,
			String nurseCode,String nurseName,String deptCode) {
		//1.判断条码是否有效
		BarCodeVo vo = orderService.getOrderBarcodeType(barcode, null,true);
		barcode = vo.getBarCode();
		String barType = vo.getBarType();
		if(StringUtils.isBlank(barcode)){
			LOGGER.error("insertOrderUnExecRecordItem 扫描条码不存在!");
			throw new AlertException("扫描条码不存在!");
		}
		
		if(orderService.getOrderExecCountById(barcode,null, null)>0){
			LOGGER.error("insertOrderUnExecRecordItem 该条码医嘱已执行!");
			throw new AlertException("该条码医嘱已执行!");
		}
		String[] barTypes = barType.split(MnisConstants.LINE);
		String patId = barTypes[1];
		//检验条码
		if(MnisConstants.ORDER_EXEC_TYPE_LAB.equals(barTypes[0])){
			//获取的是住院号
			List<String> patIds = patientService.getPatIdsByInHospNo(deptCode, patId);
			if(null == patIds || patIds.isEmpty()){
				LOGGER.error("insertOrderUnExecRecordItem 住院号患者未分配患者编号!");
				throw new MnisException("患者不存在!");
			}
			//取最新患者
			patId = patIds.get(0);
		}
		//2.获取项目信息
		List<OrderItem> orderItems = orderService.getOrderItemsByBarcode(barcode, barTypes[0]);
		
		//3.构建保存属性
		OrderUnExecRecordItem orderUnExecRecordItem = new OrderUnExecRecordItem();
		orderUnExecRecordItem.setDeptCode(deptCode);
		orderUnExecRecordItem.setPatId(patId);
		orderUnExecRecordItem.setBarcode(barcode);
		orderUnExecRecordItem.setRecordNurseCode(nurseCode);
		orderUnExecRecordItem.setRecordNurseName(nurseName);
		orderUnExecRecordItem.setRecordDate(new Date());
		
		StringBuffer drugInfoSb = new StringBuffer();;
		//4.分解项目
		for (int i=0 ; i<orderItems.size(); i++) {
			OrderItem orderItem = orderItems.get(i);
			
			drugInfoSb.append(StringUtil.validEmpty(orderItem.getOrderName())).append(MnisConstants.LINE_COLON)
			.append(null == orderItem.getDosage() ? 0:orderItem.getDosage() ).append(MnisConstants.LINE_COLON)
			.append(StringUtil.validEmpty(orderItem.getDosageUnit())).append(MnisConstants.LINE_COLON)
			.append(StringUtil.validEmpty(orderItem.getUsageName()));
			
			if(i != (orderItems.size()-1)){
				drugInfoSb.append(MnisConstants.LINE_SEMI_COLON);
			}
		}
		orderUnExecRecordItem.setDrugInfos(drugInfoSb.toString());
		orderLiquorRepository.insertOrderUnExecRecordItem(orderUnExecRecordItem);
		return orderUnExecRecordItem.getId();
	}
}
