package com.lachesis.mnis.core.order.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lachesis.mnis.core.OrderService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.Beta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.mybatis.mapper.OrderMapper;
import com.lachesis.mnis.core.order.entity.HisOrderGroup;
import com.lachesis.mnis.core.order.entity.OrderApprove;
import com.lachesis.mnis.core.order.entity.OrderBedInfo;
import com.lachesis.mnis.core.order.entity.OrderCount;
import com.lachesis.mnis.core.order.entity.OrderExecDocumentInfo;
import com.lachesis.mnis.core.order.entity.OrderExecDocumentPrintInfo;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.order.entity.OrderExecLog;
import com.lachesis.mnis.core.order.entity.OrderItem;
import com.lachesis.mnis.core.order.entity.OrderPrintInfo;
import com.lachesis.mnis.core.order.entity.OrderUnprintStatistic;
import com.lachesis.mnis.core.order.repository.OrderRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;
import com.lachesis.mnis.core.util.SuperCacheUtil;

@Repository("orderRepository")
public class OrderRepositoryImpl implements OrderRepository {
	@Autowired
	private OrderMapper orderMapper;
	
	@Override
	public List<OrderExecGroup> selectExecutedOrderGroups(
			String patientId, String orderTypeCode,String orderExecTypeCode,Date startDate, Date endDate) {
		HashMap<String, Object> params = new HashMap<>();
		
		params.put("patientId", patientId);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("hospitalCode", SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));
		return orderMapper.selectExecutedOrderGroups(params);
	}
	
	@Override
	public List<OrderExecGroup> selectUnExecutedOrderGroups(
			String patientId, String orderTypeCode,String orderExecTypeCode,Date startDate, Date endDate) {
		HashMap<String, Object> params = new HashMap<>();

		params.put("patientId", patientId);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		return orderMapper.selectUnExecutedOrderGroups(params);
	}

	
	@Override
	public OrderExecGroup getOrderListScanGroupId(String orderGroupId,String patientId,String stopType) {
		if(StringUtils.isBlank(orderGroupId)){
			throw new RuntimeException("医嘱ID不能为空!");
		}
		if(StringUtils.isBlank(stopType)){
			stopType = null;
		}
		String hospitcalCode = SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode");
		orderGroupId = getLxBarcode(orderGroupId);
		return orderMapper.getOrderListScanGroupId(orderGroupId,patientId,stopType,hospitcalCode);
	}

	/**
	 * 通过医院条码查询联新条码，适合医院同一个条码要执行多次的情况
	 * 须在同步时根据情况拆分医嘱，医嘱的条码为联新生成的条码，同时在表pat_barcode_relation中建立条码关联
	 * @param his_barcode
	 * @return
	 */
	private String getLxBarcode(String his_barcode) {
		if(his_barcode !=null && his_barcode.startsWith("lx_")){
			return his_barcode;
		}
		List<OrderExecLog> barcodeList = orderMapper.getBracodeRelation(his_barcode);
		if(barcodeList != null && barcodeList.size() > 0){
			boolean haveNotExecuted = false;
			for (Iterator<OrderExecLog> iterator = barcodeList.iterator(); iterator.hasNext();) {
				OrderExecLog orderExecLog = (OrderExecLog) iterator.next();
				if(orderExecLog.getOrderExecBarcode() == null){
					his_barcode = orderExecLog.getOrderExecId();
					haveNotExecuted = true;
					break;
				}
			}
			if(!haveNotExecuted){
				his_barcode = barcodeList.get(0).getOrderExecId();
			}
		}
		return his_barcode;
	}

	@Override
	public int insertSingleOrderExecution(OrderExecLog execInfo) {
		return orderMapper.insertSingleOrderExecution(execInfo);
	}

	@Override
	public List<OrderExecGroup> selectOrderBaseGroupByPatId(
			String patientId, String orderTypeCode,String orderExecTypeCode, String status,
			Date startDate, Date endDate) {
		
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("patientId", patientId);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("hospitalCode", SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));
		//sql脚本排除包药机和检验
		List<OrderExecGroup> orderExecGroups = orderMapper.selectOrderBaseGroupByPatId(params);
		
		if(MnisConstants.ORDER_EXEC_TYPE_ORAL.equals(orderExecTypeCode) && "1".equals(orderTypeCode)){
			//包药机
			orderExecGroups.addAll(orderMapper.selectDrugBagByPatId(params));
		}else if(MnisConstants.ORDER_EXEC_TYPE_LAB.equals(orderExecTypeCode) && "0".equals(orderTypeCode)){
			//检验类
			orderExecGroups.addAll(orderMapper.selectLabByPatId(params));
		}else if(MnisConstants.ORDER_EXEC_TYPE_BLOOD.equals(orderExecTypeCode) && "0".equals(orderTypeCode)){
			//输血
			orderExecGroups.addAll(orderMapper.getBloodOrderGroupDetail(null, null, patientId, 
					DateUtil.format(startDate,DateFormat.FULL), DateUtil.format(endDate,DateFormat.FULL)));
		}
		return orderExecGroups;
	}

	@Override
	public int updateOrderExecution(List<OrderExecLog> orderExecInfos,
			String execNurseCode, String execDate) {
		return orderMapper.updateOrderExecution(orderExecInfos, execNurseCode, execDate);
	}

	@Override
	public List<OrderExecGroup> getShiftOrderList(List<String> patients,
			Date startDate, Date endDate) {
		List<OrderExecGroup> results = new ArrayList<OrderExecGroup>();
		results.addAll(orderMapper.getDischargeOrderList(patients,startDate,endDate));
		results.addAll(orderMapper.getNursingOrderList(patients,startDate,endDate));
		results.addAll(orderMapper.getBloodsugarOrderList(patients,startDate,endDate));
		return results;
	}

	@Override
	public List<OrderBedInfo> getOrderLongBedInfoList(List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode,Date startDate, Date endDate) {
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("patientIds", patientIds);
		params.put("deptCode", deptCode);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		
		return orderMapper.getOrderLongBedInfoList(params);
	}
	
	@Override
	public List<OrderBedInfo> getOrderTempBedInfoList(List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode,Date startDate, Date endDate) {
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("patientIds", patientIds);
		params.put("deptCode", deptCode);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		
		return orderMapper.getOrderTempBedInfoList(params);
	}

	@Override
	public int updateOrderNoByOrderGroupId(String orderGroupId, String newOrderNo) {
		return orderMapper.updateOrderNoByOrderGroupId(orderGroupId, String.valueOf(newOrderNo));
	}

	@Override
	public List<HisOrderGroup> getOriginalOrderList(List<String> patientIds,
			String orderTypeCode, String orderExecTypeCode,
			Date startDate, Date endDate) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("patientIds", patientIds);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("isAdd", "0");
		params.put("hospitalCode", SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));
		
		List<HisOrderGroup> rs = new ArrayList<HisOrderGroup>();
		if(StringUtils.isBlank(orderExecTypeCode)){
			//获取不包括口服药和检验类医嘱
			rs.addAll(orderMapper.getOriginalOrderList(params));
			//获取口服药和检验类医嘱
			rs.addAll(orderMapper.getOriginalOrderListToORAL(params));
			rs.addAll(orderMapper.getOriginalOrderListToLAB(params));
		}else{
			if(MnisConstants.ORDER_EXEC_TYPE_ORAL.equals(orderExecTypeCode)){
				//获取口服药和检验类医嘱
				rs = orderMapper.getOriginalOrderListToORAL(params);
			}else if(MnisConstants.ORDER_EXEC_TYPE_LAB.equals(orderExecTypeCode)){
				rs = orderMapper.getOriginalOrderListToLAB(params);
			}else{
				//获取不包括口服药和检验类医嘱
				rs = orderMapper.getOriginalOrderList(params);
			}
		}
		
		return rs;
	}
	
	
	@Override
	public List<HisOrderGroup> getOriginalOrderListOne(List<String> patientIds,
			String orderTypeCode, String orderExecTypeCode,
			Date startDate, Date endDate) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("patientIds", patientIds);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("isAdd", "1");
		params.put("hospitalCode", SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));
		
		return orderMapper.getOriginalOrderList(params);
	}
	
	@Override
	public String getPatientIdByOrderGroupId(String orderGroupId,String type) {
		orderGroupId = getLxBarcode(orderGroupId);
		return orderMapper.getPatientIdByOrderGroupId(orderGroupId,type);
	}

	@Override
	public List<OrderBedInfo> getOrderOriginalLongBedInfoList(
			List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode,Date startDate,Date endDate ) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("patientIds", patientIds);
		params.put("deptCode", deptCode);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("hospitalCode", SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));
		return orderMapper.getOrderOriginalLongBedInfoList(params);
	}
	
	@Override
	public List<OrderBedInfo> getOrderOriginalTempBedInfoList(
			List<String> patientIds,String deptCode,
			String orderTypeCode,String orderExecTypeCode,Date startDate,Date endDate ) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("patientIds", patientIds);
		params.put("deptCode", deptCode);
		params.put("orderTypeCode", orderTypeCode);
		params.put("orderExecTypeCode", orderExecTypeCode);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("hospitalCode", SuperCacheUtil.getSYSTEM_CONFIGS().get("hospitalCode"));
		return orderMapper.getOrderOriginalTempBedInfoList(params);
	}

	@Override
	public List<String> getOralOrderIdByBarcode(String barcode) {
		barcode = getLxBarcode(barcode);
		return orderMapper.getOralOrderIdByBarcode(barcode);
	}

	@Override
	public OrderExecGroup getORALOrderGroupDetail(String barcode, String pat_id) {
		barcode = getLxBarcode(barcode);
		return orderMapper.getORALOrderGroupDetail(barcode, pat_id);
	}

	@Override
	public OrderExecGroup getLABOrderGroupDetailByBarcode(String barcode,
			String pat_id) {
		barcode = getLxBarcode(barcode);
		return orderMapper.getLABOrderGroupDetailByBarcode(barcode, pat_id);
	}
	
	@Override
	public OrderExecGroup getBloodOrderGroupDetailByBarcode(String barcode,String secBarcode,
			String patId) {
		barcode = getLxBarcode(barcode);
		 List<OrderExecGroup> orderExecGroups = orderMapper.getBloodOrderGroupDetail(barcode, 
				 secBarcode,patId,null,null);
		 if(orderExecGroups.isEmpty()){
			 return null;
		 } 
		 return orderExecGroups.get(0);
	}

	@Override
	public OrderCount getOrderCount(String patId,String orderExecType,String orderType,Date queryDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("patId", patId);
		params.put("orderExecType", orderExecType);
		params.put("orderType", orderType);
		if(queryDate == null){
			queryDate = new Date();
		}
		Date[] queryDates = DateUtil.getQueryRegionDates(queryDate);
		params.put("startDate", queryDates[0]);
		params.put("endDate", queryDates[1]);
		
		OrderCount count = orderMapper.getOrderCount(params);
		
		return count;
	}
	
	@Override
	public OrderCount getBloodOrderCount(String patId,Date queryDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("patId", patId);
		if(queryDate == null){
			queryDate = new Date();
		}
		Date[] queryDates = DateUtil.getQueryRegionDates(queryDate);
		params.put("startDate", queryDates[0]);
		params.put("endDate", queryDates[1]);
		
		OrderCount count = orderMapper.getBloodOrderCount(params);
		
		return count;
	}

	@Override
	public List<OrderUnprintStatistic> getUnprintOrderStatisticList(
			Map<String, Object> params) {
		return orderMapper.getUnprintOrderStatisticList(params);
	}

	@Override
	public List<OrderPrintInfo> getOrderPrintInfos(Map<String, Object> params) {
		return orderMapper.getOrderPrintInfos(params);
	}

	@Override
	public String getOrderStatusByOrderGroupId(String orderGroupId,String type,String patId) {
		orderGroupId = getLxBarcode(orderGroupId);
		return orderMapper.getOrderStatusByOrderGroupId(orderGroupId,type,patId);
	}

	@Override
	public int getOrderExecCountById(String barcode,String secBarcode, String patId) {
		barcode = getLxBarcode(barcode);
		return orderMapper.getOrderExecCountById(barcode,secBarcode, patId);
	}

	@Override
	public int getInAndOutOrder(String patientId, Date beginDate, Date endDate) {
		
		return orderMapper.getInAndOutOrder(patientId, beginDate, endDate);
	}

	@Override
	public int getUrineOrder(String patientId, Date beginDate, Date endDate) {
		
		return orderMapper.getUrineOrder(patientId, beginDate, endDate);
	}

	@Override
	public String getCreateDateTimeOfPatInoutOrder(String patId, Date beginDate, Date endDate){
		return orderMapper.getCreateDateTimeOfPatInoutOrder(patId, beginDate, endDate);
	}

	@Override
	public List<Map<String, Object>> getOrderExecTypes() {
		return orderMapper.getOrderExecTypes();
	}

	@Override
	public List<Map<String, Object>> getOrderUsageTypes() {
		return orderMapper.getOrderUsageTypes();
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfos(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfos(params);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosToOral(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosToOral(params);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosToLab(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosToLab(params);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosOnInfuCard(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosOnInfuCard(params);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosOnLabel(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosOnLabel(params);
	}

	@Override
	public String saveOrdExecDocPrtInf(
			OrderExecDocumentPrintInfo orderExecDocumentPrintInfo) {
		orderMapper.saveOrdExecDocPrtInf(orderExecDocumentPrintInfo);
		 return orderExecDocumentPrintInfo.getPrintId();
	}

	@Override
	public int updateOrdExecDocPrtInf(
			OrderExecDocumentPrintInfo orderExecDocumentPrintInfo) {
		return orderMapper.updateOrdExecDocPrtInf(orderExecDocumentPrintInfo);
	}

	@Override
	public int getOrdExecDocPrtInfCount(String id) {
		return orderMapper.getOrdExecDocPrtInfCount(id);
	}

	@Override
	public List<String> getOrderSubNoFromDrugBag(HashMap<String, Object> params) {
		return orderMapper.getOrderSubNoFromDrugBag(params);
	}
	
	@Override
	public List<String> getOrderIdFromBlood(HashMap<String, Object> params) {
		return orderMapper.getOrderIdFromBlood(params);
	}

	@Override
	public List<Map<String, Object>> getCopyOrderExecTypes(HashMap<String, Object> params) {
		return orderMapper.getCopyOrderExecTypes(params);
	}

	@Override
	public boolean isStopOrderOriginal(String groupNo, String patId) {
		String countString = orderMapper.isStopOrderOriginal(groupNo, patId);
		if(StringUtils.isNotBlank(countString) && Integer.valueOf(countString) == 3){
			return true;
		}
		return false;
	}

	@Override
	public int insertPatOrderRemark(String value,String barcode,int type) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("value", value);
		params.put("barcode", barcode);
		params.put("type", type);
		return orderMapper.insertPatOrderRemark(params);
	}

	@Override
	public boolean isExistOrderRemark(String barcode,int type) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("barcode", barcode);
		params.put("type", type);
		return orderMapper.getCountByRemarkBarcode(params) >0 ? true:false;
	}

	@Override
	public int updatePatOrderRemarkByBarcode(String value, String barcode,int type) {
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("value", value);
		params.put("barcode", barcode);
		params.put("type", type);
		return orderMapper.updatePatOrderRemarkByBarcode(params);
	}

	@Override
	public String getOrderBarcodeType(String barcode, String patId) {
		return orderMapper.getOrderBarcodeType(barcode, patId);
	}

	@Override
	public List<OrderItem> getOrderItemsByBarcode(String barcode) {
		return orderMapper.getOrderItemsByBarcode(barcode);
	}

	@Override
	public List<OrderItem> getOralOrderItemsByBarcode(String barcode) {
		return orderMapper.getOralOrderItemsByBarcode(barcode);
	}

	@Override
	public List<OrderItem> getLabOrderItemsByBarcode(String barcode) {
		return orderMapper.getLabOrderItemsByBarcode(barcode);
	}

	@Override
	public List<OrderExecGroup> getOrdExecDocInfosToNda(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosToNda(params);
	}

	@Override
	public List<OrderExecGroup> getOrdExecDocInfosToNdaOral(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosToNdaOral(params);
	}

	@Override
	public List<OrderExecGroup> getOrdExecDocInfosToNdaLab(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosToNdaLab(params);
	}
	
	@Override
	public List<OrderExecGroup> getOrdExecDocInfosToNdaBlood(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosToNdaBlood(params);
	}

	@Override
	public String getOriginalOrderStatusByOrderGroupId(String orderGroupId,
			String patId) {
		return orderMapper.getOriginalOrderStatusByOrderGroupId(orderGroupId, patId);
	}

	@Override
	public List<HisOrderGroup> getPublishOriginalOrders(
			Map<String, Object> params) {
		return orderMapper.getPublishOriginalOrders(params);
	}

	@Override
	public void insertOrderApprove(OrderApprove orderApprove) {
		orderMapper.insertOrderApprove(orderApprove);
	}

	@Override
	public void updateOrderApprove(OrderApprove orderApprove) {
		orderMapper.updateOrderApprove(orderApprove);
	}

	@Override
	public Integer getOrderApproveCount(String patId,String orderNo) {
		return orderMapper.getOrderApproveCount(patId,orderNo);
	}

	@Override
	public List<OrderExecDocumentInfo> getOrdExecDocInfosToBlood(
			HashMap<String, Object> params) {
		return orderMapper.getOrdExecDocInfosToBlood(params);
	}
}
