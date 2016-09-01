package com.lachesis.mnis.core.liquor.service.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.lachesis.mnis.core.OrderLiquorService;
import com.lachesis.mnis.core.SpringTest;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecord;

public class testLiquorServiceImpl extends SpringTest{
	@Autowired
	private OrderLiquorService orderLiquorService;

	@Test
	public void testGetOrderUnExecRecord(){
		
		List<OrderUnExecRecord> orderUnExecRecords = orderLiquorService.getOrderUnExecRecords("5042", null,null,null,null);
		Gson gson = new Gson();
		String gsonString = gson.toJson(orderUnExecRecords);
		System.err.println(gsonString);
		
		Assert.assertNotNull(orderUnExecRecords);
	}
	
	@Test
	public void testInsertOrderUnExecRecordItem(){
		String recordUserCode = "000283";
		String recordUserName="段钢";
		String deptCode = "5042";
		String barcode = "I20160504203014105011";
		int count = orderLiquorService.insertOrderUnExecRecordItem(barcode, recordUserCode, recordUserName, deptCode);
		Assert.assertSame(1, count);
	}
}
