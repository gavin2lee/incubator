package com.lachesis.mnis.core.liquor.repository.impl;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorItem;
import com.lachesis.mnis.core.liquor.repository.OrderLiquorRepository;
import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

public class testLiquorReposityImpl extends SpringTest {

	private static final String testId = "I201411190920349572";
	private static final String selectTestId = "I201411030920063799";

	@Autowired
	OrderLiquorRepository orderLiquorRepository;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(testLiquorReposityImpl.class);

	private OrderLiquorItem orderLiquorItem;

	@Before
	public void init() {

		
	}

	@Test
	public void testInit() {
		Assert.assertNotNull(orderLiquorRepository);
	}
//
//	@Test
//	public void testQueryAll() {
//		List<OrderLiquor> items = this.orderLiquorRepository
//				.getOrderLiquorList("3061");
//
//		Assert.assertNotNull(items);
//	}

	@Test
	public void testInsertForPrepare() {
		
		orderLiquorItem = new OrderLiquorItem();
		orderLiquorItem.setExecOrderId("1234556");
		orderLiquorItem.setOrderId("12233333");
		orderLiquorItem.setPrepareNurseId("1111111111111111");
		orderLiquorItem.setPrepareNurseName("11111111111111111");
		orderLiquorItem.setPrepareTime(DateUtil.parse("2014-11-19 12:00:00", DateFormat.FULL));
		orderLiquorItem.setState("P");;
		
		orderLiquorRepository.insertOrderLiquorForPrepare(orderLiquorItem);
		
		Assert.assertNotNull(orderLiquorItem);
	}

	@Test
	public void testInsertForExec() {
		orderLiquorItem = new OrderLiquorItem();
		orderLiquorItem.setExecOrderId("1234556");
		orderLiquorItem.setOrderId("12233333");
		orderLiquorItem.setExecNurseId("1111111111111111");
		orderLiquorItem.setExecNurseName("11111111111111111");
		orderLiquorItem.setExecTime(DateUtil.parse("2014-11-19 12:00:00", DateFormat.FULL));
		orderLiquorItem.setState("F");;
		
		orderLiquorRepository.insertOrderLiquorForExec(orderLiquorItem);
		
		Assert.assertNotNull(orderLiquorItem);
	}

	@Test
	public void testUpdate() {
		
		orderLiquorItem = new OrderLiquorItem();
		orderLiquorItem.setExecOrderId("1234556");
		orderLiquorItem.setOrderId("12233333");
		orderLiquorItem.setPrepareNurseId("1111111111111111");
		orderLiquorItem.setPrepareNurseName("11111111111111111");
		orderLiquorItem.setPrepareTime(DateUtil.parse("2014-11-19 12:00:00", DateFormat.FULL));
		orderLiquorItem.setState("P");;
		
		orderLiquorRepository.insertOrderLiquorForPrepare(orderLiquorItem);
		
		orderLiquorItem = new OrderLiquorItem();
		orderLiquorItem.setExecOrderId("1234556");
		orderLiquorItem.setOrderId("12233333");
		orderLiquorItem.setExecNurseId("1111111111111111");
		orderLiquorItem.setExecNurseName("11111111111111111");
		orderLiquorItem.setExecTime(DateUtil.parse("2014-11-19 12:00:00", DateFormat.FULL));
		orderLiquorItem.setState("F");;
		
		orderLiquorRepository.updateOrderLiquor(orderLiquorItem);
		
		Assert.assertNotNull(orderLiquorItem);
	}
	
	@Test
	public void testUpdateForVerify(){
		orderLiquorItem = new OrderLiquorItem();
		orderLiquorItem.setExecOrderId("1234556");
		orderLiquorItem.setOrderId("12233333");
		orderLiquorItem.setPrepareNurseId("1111111111111111");
		orderLiquorItem.setPrepareNurseName("11111111111111111");
		orderLiquorItem.setPrepareTime(DateUtil.parse("2014-11-19 12:00:00", DateFormat.FULL));
		orderLiquorItem.setState("P");;
		
		orderLiquorRepository.insertOrderLiquorForPrepare(orderLiquorItem);
		
		orderLiquorItem = new OrderLiquorItem();
		orderLiquorItem.setExecOrderId("1234556");
		orderLiquorItem.setOrderId("12233333");
		orderLiquorItem.setVerifyNurseId("1111111111111111");
		orderLiquorItem.setVerifyNurseName("11111111111111111");
		orderLiquorItem.setVerifyTime(new Date());
		orderLiquorItem.setState("V");;
		
		orderLiquorRepository.updateOrderLiquorForVerify(orderLiquorItem);
		
		Assert.assertNotNull(orderLiquorItem);
	}

	@Test
	public void testSelectById() {
		orderLiquorItem = new OrderLiquorItem();
		orderLiquorItem.setExecOrderId("1234556");
		orderLiquorItem.setOrderId("12233333");
		orderLiquorItem.setPrepareNurseId("1111111111111111");
		orderLiquorItem.setPrepareNurseName("11111111111111111");
		orderLiquorItem.setPrepareTime(DateUtil.parse("2014-11-19 12:00:00", DateFormat.FULL));
		orderLiquorItem.setState("P");;
		
		orderLiquorRepository.insertOrderLiquorForPrepare(orderLiquorItem);
		
		OrderLiquorItem item = orderLiquorRepository.selectOrderLiquorByExecOrderId("1234556");
		
	}


}
