package com.lachesis.mnis.core.order.repository.impl;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.order.entity.HisOrderGroup;
import com.lachesis.mnis.core.order.entity.OrderBedInfo;
import com.lachesis.mnis.core.order.entity.OrderCount;
import com.lachesis.mnis.core.order.entity.OrderExecDocumentInfo;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.order.entity.OrderPrintInfo;
import com.lachesis.mnis.core.order.repository.OrderRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.StringUtil;

public class OrderServiceImplTest extends SpringTest {
	@Autowired
	OrderRepository orderRepository;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderServiceImplTest.class);
	private String patientId = null;
	private List<String> patientIds = new ArrayList<>();
	private String orderTypeCode = null;
	private Date startDate = null;
	private Date endDate = null;
	private String orderGroupId = null;
	private Date[] dateRegion = new Date[2];
	HashMap<String, Object> params = new HashMap<String, Object>();
	@Before
	public void init() {
		patientId = "34363864_1";
		patientIds.add("ZA4673929_1");
		startDate = getCurDateWithMinTime();
		endDate = getCurDateWithMaxTime();
		orderGroupId = "ZA1001803*30*1";
		
		dateRegion = DateUtil.getQueryRegionDates(new Date());
		
		
		params.put("deptCode", "5042");
		params.put("queryDate", "2016-02-19");
	}

	@Test
	public void testInit() {
		assertNotNull(orderRepository);
	}

	@Test
	public void testSelectOrderBaseGroupByPatientId() {
		/*List<OrderExecGroup> orderGroupList = orderRepository
				.selectOrderBaseGroupByPatientIds(patientIds, orderTypeCode,
						null, null, startDate, endDate);
		Gson gson = new Gson();
		String orderGroupString = gson.toJson(orderGroupList);
		LOGGER.debug("testSelectOrderBaseGroupByPatientId : "
				+ orderGroupList.size());

		assertNotNull(orderGroupList);*/
	}

	@Test
	public void testSelectExecutedOrderGroups() {
		List<OrderExecGroup> orderExecGroups = orderRepository
				.selectExecutedOrderGroups(patientId, null, null, startDate,
						endDate);
		if (orderExecGroups != null && orderExecGroups.size() == 0)
			orderExecGroups = null;
		Assert.assertNull(orderExecGroups);
	}

	@Test
	public void testSelectOrderGroupDetailByGroupId() {
		OrderExecGroup orderExecGroup = orderRepository
				.getOrderListScanGroupId(orderGroupId,null,null);
		Assert.assertNull(orderExecGroup);
	}

	/*
	 * @Test public void testSelectPendingOrderGroupByPatientId() {
	 * List<OrderExecGroup> orderExecGroups = orderRepository
	 * .selectPendingOrderGroupByPatientId(patientId, startDate, endDate,
	 * orderTypeCode); assertNotNull(orderExecGroups); }
	 */

	@Test
	public void testInsertSingleOrderExecution() {
		// List<OrderExecGroup> orderExecGroups = orderRepository
		// .selectExecutedOrderGroups(patientId, startDate, endDate);
		// assertNotNull(orderExecGroups);
	}

	@Test
	public void testUpdateOrderExecution() {
		/*
		 * List<OrderExecGroup> orderExecGroups = orderRepository
		 * .selectExecutedOrderGroups(patientId, startDate, endDate);
		 * assertNotNull(orderExecGroups);
		 */
	}

	@Test
	public void testGetShiftOrderList() {
		// List<OrderExecGroup> orderExecGroups = orderRepository
		// .getShiftOrderList(patientIds, startDate, endDate);
		// assertNotNull(orderExecGroups);
	}

	@Test
	public void testGetOrderBedInfoList() {
		List<OrderBedInfo> orderBedInfos = orderRepository.getOrderLongBedInfoList(
				patientIds,null, "0",null, startDate, endDate);
		Assert.assertNull(orderBedInfos);
	}
	
	@Test
	public void testgetOriginalOrderList(){
		
		List<HisOrderGroup> hisOrderGroups = orderRepository.getOriginalOrderList(
				patientIds, null, null, dateRegion[0], dateRegion[1]);
		Gson gson = new Gson();
		String gsonString = gson.toJson(hisOrderGroups);
		assertNotNull(hisOrderGroups);
	}
	
	@Test
	public void testGetOrderPrintInfos(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("deptCode", "H250005");
		params.put("orderTypeCode", "1");
		params.put("startDate",startDate);
		params.put("endDate", endDate);
		List<OrderPrintInfo> hisOrderGroups = orderRepository.getOrderPrintInfos(params);
		
		Gson gson = new Gson();
		String gsonString = gson.toJson(hisOrderGroups);
		assertNotNull(hisOrderGroups);
	}
	
	@Test
	public void testGetOrderExecDocumentInfos(){
		List<OrderExecDocumentInfo> orderExecDocumentInfos = orderRepository.getOrdExecDocInfos(params);
		Gson gson = new Gson();
		String gsonString = gson.toJson(orderExecDocumentInfos);
		
		StringUtil.writeStrToFile(gsonString, "D:/jsonFile/orderExecDocumentInfos.txt");
		Assert.assertNotNull(gsonString);
	}
	@Test
	public void testGetOrdExecDocInfosToOral(){
		List<OrderExecDocumentInfo> orderExecDocumentInfos = orderRepository.getOrdExecDocInfosToOral(params);
		Gson gson = new Gson();
		String gsonString = gson.toJson(orderExecDocumentInfos);
		
		StringUtil.writeStrToFile(gsonString, "D:/jsonFile/getOrdExecDocInfosToOral.txt");
		Assert.assertNotNull(gsonString);
	}
	@Test
	public void testGetOrdExecDocInfosToLab(){
		HashMap<String, Object> labParams = new HashMap<String, Object>();
		labParams.put("deptCode", "5042");
		labParams.put("queryDate", "2015-12-25");
		List<OrderExecDocumentInfo> orderExecDocumentInfos = orderRepository.getOrdExecDocInfosToLab(labParams);
		Gson gson = new Gson();
		String gsonString = gson.toJson(orderExecDocumentInfos);
		
		StringUtil.writeStrToFile(gsonString, "D:/jsonFile/getOrdExecDocInfosToLab.txt");
		Assert.assertNotNull(gsonString);
	}
	@Test
	public void testGetOrdExecDocInfosOnInfuCard(){
		List<OrderExecDocumentInfo> orderExecDocumentInfos = orderRepository.getOrdExecDocInfosOnInfuCard(params);
		Gson gson = new Gson();
		String gsonString = gson.toJson(orderExecDocumentInfos);
		
		StringUtil.writeStrToFile(gsonString, "D:/jsonFile/getOrdExecDocInfosOnInfuCard.txt");
		Assert.assertNotNull(gsonString);
	}
	@Test
	public void testGetOrdExecDocInfosOnLabel(){
		List<OrderExecDocumentInfo> orderExecDocumentInfos = orderRepository.getOrdExecDocInfosOnLabel(params);
		Gson gson = new Gson();
		String gsonString = gson.toJson(orderExecDocumentInfos);
		
		StringUtil.writeStrToFile(gsonString, "D:/jsonFile/getOrdExecDocInfosOnLabel.txt");
		Assert.assertNotNull(gsonString);
	}
	
	@Test
	public void testselectOrderBaseGroupByPatId(){
		List<OrderExecGroup> orders = orderRepository
				.selectOrderBaseGroupByPatId("ZY020000137670", null,
						"ORAL", null, DateUtil.parse("2016-02-19"), DateUtil.parse("2016-02-20"));
		
		Assert.assertNotNull(orders);
	}
	
	@Test
	public void testGetOrderBarcodeType(){
		String ordType = orderRepository
				.getOrderBarcodeType("13", null);
		
		Assert.assertSame("NORMAL", ordType);
	}
	
	@Test
	public void testGetOrdExecDocInfosToNda(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("deptCode", "5042");
		params.put("startDate", "2016-05-01");
		params.put("endDate", "2016-05-03");
		
		List<OrderExecGroup> orderExecGroups = orderRepository.getOrdExecDocInfosToNda(params);
		Assert.assertNotNull(orderExecGroups);
	}
	
	@Test
	public void testGetOrdExecDocInfosToNdaOral(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("deptCode", "5042");
		params.put("startDate", "2016-05-01");
		params.put("endDate", "2016-05-03");
		
		List<OrderExecGroup> orderExecGroups = orderRepository.getOrdExecDocInfosToNdaOral(params);
		Assert.assertNotNull(orderExecGroups);
	}
	
	@Test
	public void testGetOrdExecDocInfosToNdaLab(){
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("deptCode", "5042");
		params.put("startDate", "2016-05-01");
		params.put("endDate", "2016-05-03");
		
		List<OrderExecGroup> orderExecGroups = orderRepository.getOrdExecDocInfosToNdaLab(params);
		Assert.assertNotNull(orderExecGroups);
	}
	
	
	@Test
	public void testGetBloodOrderGroupDetailByBarcode(){
		OrderExecGroup orderExecGroup = orderRepository.getBloodOrderGroupDetailByBarcode("111", "22", null);

		Gson gson = new Gson();
		String gsonString = gson.toJson(orderExecGroup);
		Assert.assertNotNull(gsonString);
	}
	
	@Test
	public void testGetBloodOrderCount(){

		OrderCount orderCount = orderRepository.getBloodOrderCount("111", new Date());
		Gson gson = new Gson();
		String gsonString = gson.toJson(orderCount);
		Assert.assertNotNull(gsonString);
	}
	
}