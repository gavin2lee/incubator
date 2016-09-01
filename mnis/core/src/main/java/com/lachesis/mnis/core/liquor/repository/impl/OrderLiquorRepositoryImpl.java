package com.lachesis.mnis.core.liquor.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lachesis.mnis.core.liquor.entity.OrderLiquor;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorItem;
import com.lachesis.mnis.core.liquor.entity.OrderLiquorStatistic;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecord;
import com.lachesis.mnis.core.liquor.entity.OrderUnExecRecordItem;
import com.lachesis.mnis.core.liquor.repository.OrderLiquorRepository;
import com.lachesis.mnis.core.mybatis.mapper.OrderLiquorMapper;

@Repository("orderLiquorRepository")
public class OrderLiquorRepositoryImpl implements OrderLiquorRepository {

	@Autowired
	private OrderLiquorMapper orderLiquorMapper;

	@Override
	public List<OrderLiquor> getOrderLiquorList(HashMap<String, Object> params) {
		
		return orderLiquorMapper.getOrderLiquorList(params);
	}

	@Override
	public int insertOrderLiquorForPrepare(OrderLiquorItem orderLiquor) {
		return orderLiquorMapper.insertOrderLiquorForPrepare(orderLiquor);
	}

	@Override
	public int insertOrderLiquorForExec(OrderLiquorItem orderLiquor) {
		return orderLiquorMapper.insertOrderLiquorForExec(orderLiquor);
	}

	@Override
	public int insertOrderLiquorForVerify(OrderLiquorItem orderLiquor) {
		return orderLiquorMapper.insertOrderLiquorForVerify(orderLiquor);
	}

	@Override
	public int updateOrderLiquor(OrderLiquorItem orderLiquor) {
		return orderLiquorMapper.updateOrderLiquor(orderLiquor);
	}

	@Override
	public int updateOrderLiquorForVerify(OrderLiquorItem orderLiquor) {
		return orderLiquorMapper.updateOrderLiquorForVerify(orderLiquor);
	}

	@Override
	public OrderLiquorItem selectOrderLiquorByExecOrderId(String execOrderId) {
		return orderLiquorMapper.selectOrderLiquorByExecOrderId(execOrderId);
	}

	@Override
	public OrderLiquorStatistic getOrderLiquorStatistic(String deptCode,
			Date startDate, Date endDate,String orderTypeCode,String type) {
		return orderLiquorMapper.getOrderLiquorStatistic(deptCode, startDate,
				endDate,orderTypeCode,type);
	}

	@Override
	public List<OrderUnExecRecord> getOrderUnExecRecords(String deptCode,
			List<String> patIds,String recordId,Date startDate,Date endDate) {
		return orderLiquorMapper.getOrderUnExecRecords(deptCode, patIds,recordId,startDate,endDate);
	}

	@Override
	public int insertOrderUnExecRecordItem(
			OrderUnExecRecordItem orderUnExecRecordItem) {
		return orderLiquorMapper.insertOrderUnExecRecordItem(orderUnExecRecordItem);
	}

	@Override
	public List<OrderLiquor> getDrugBagLiquorList(HashMap<String, Object> params) {
		return orderLiquorMapper.getDrugBagLiquorList(params);
	}

}
