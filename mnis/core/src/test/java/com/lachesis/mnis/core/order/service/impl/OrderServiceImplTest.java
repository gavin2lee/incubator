package com.lachesis.mnis.core.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.lachesis.mnis.core.OrderService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.order.entity.OrderExecGroup;
import com.lachesis.mnis.core.order.entity.OrderUnprintStatistic;
import com.lachesis.mnis.core.order.repository.OrderRepository;
import com.lachesis.mnis.core.util.DateUtil;

public class OrderServiceImplTest extends SpringTest {
	@Autowired
	OrderService orderService;
	@Autowired
	OrderRepository orderMapper;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderServiceImplTest.class);
	private static String patientId = null;
	private static List<String> patientIds = new ArrayList<>();
	private static String orderGroupId = null;
	private String orderTypeCode = null;
	private static Date startDate = null;
	private static Date endDate = null;
	private static String userId = "4055";

	@BeforeClass
	public static void init() {

		patientId = "ZA1001803_1";
		patientIds.add(patientId);
		orderGroupId = "ZA1001803*32*1";
		
		startDate = DateUtil.setDateToDay(new Date());
		endDate = DateUtil.setNextDayToDate(startDate);
	}

	@Test
	public void testGetOrderBaseGroupList() {
		List<OrderExecGroup> orderExecGroups = orderService
				.getOrderBaseGroupList(patientId, orderTypeCode,null,null, startDate,
						endDate);

		Assert.assertNotNull(orderExecGroups);

	}

	@Test
	public void testGetOrderBaseGroupListTwo() {
		List<OrderExecGroup> orderExecGroups = orderService
				.getOrderBaseGroupList(patientIds, orderTypeCode, null, null, null);
		Assert.assertNotNull(orderExecGroups);
	}

	@Test
	public void testDecompAndCreateLXOrderExecGroup() {
		List<OrderExecGroup> orderExecGroups = orderService
				.getOrderBaseGroupList(patientId, orderTypeCode, null,null,startDate,
						endDate);
		
		Assert.assertNotNull(orderExecGroups);
	}

	@Test
	public void testGetPendingOrderGroups() {
		List<OrderExecGroup> orderExecGroups = orderService
				.getPendingOrderGroups(patientId, orderTypeCode,null,null,null);
		Assert.assertNotNull(orderExecGroups);
	}

	@Test
	public void testGetOrderGroupDetail() {
//		OrderExecGroup orderExecGroup = orderService
//				.getOrderGroupDetail(orderGroupId, patientIds);
//
//		Assert.assertNotNull(orderExecGroup);
	}

	@Test
	public void testGetOrderGroupByExecId() {
		OrderExecGroup orderExecGroup = orderService
				.getOrderGroupByExecId(orderGroupId, patientId);

		Assert.assertNotNull(orderExecGroup);
	}

	@Test
	public void testExecOrderGroupWithPDA() {
		OrderExecGroup orderExecGroup  = orderService
				.execOrderGroupWithPDA(orderGroupId, patientId, null,null,null,null,false);
		Assert.assertNotNull(orderExecGroup);
	}

	@Test
	public void testBatchExecOrderGroupWithPDA() {
		List<OrderExecGroup> orderExecGroups = orderService
				.batchExecOrderGroupWithPDA(orderGroupId, patientId, null,null,null,null,false);

		Assert.assertNotNull(orderExecGroups);
	}

	@Test
	public void testBatchExecMultPatOrderGroupWithPDA() {
//		List<OrderExecGroup> orderExecGroups = orderService
//				.batchExecMultPatOrderGroupWithPDA(records, userId);
//		Assert.assertNotNull(orderExecGroups);
	}


	@Test
	public void testGetOrderBaseGroupForTask() {
		List<OrderExecGroup> orderExecGroups = orderService
				.getOrderBaseGroupForTask(patientIds, null, null);

		Assert.assertNotNull(orderExecGroups);
	}

	@Test
	public void testGetShiftOrderList() {
		List<OrderExecGroup> orderExecGroups = orderService
				.getOrderBaseGroupList(patientId, orderTypeCode, null,null,startDate,
						endDate);

		Assert.assertNotNull(orderExecGroups);
	}
	
	@Test
	public void testGetUnprintOrderStatisticList(){
		List<OrderUnprintStatistic> orderUnprintStatistics = orderService
				.getUnprintOrderStatisticList("H250005", "LZ", startDate, endDate);

		Gson gson = new Gson();
		String gsonString = gson.toJson(orderUnprintStatistics);
		Assert.assertNotNull(gsonString);
	}
	
	@Test
	public void testGetBloodOrderGroupDetail(){
		OrderExecGroup orderExecGroup = orderService.getBloodOrderGroupDetail("111", "22", null);

		Gson gson = new Gson();
		String gsonString = gson.toJson(orderExecGroup);
		Assert.assertNotNull(gsonString);
	}
}
