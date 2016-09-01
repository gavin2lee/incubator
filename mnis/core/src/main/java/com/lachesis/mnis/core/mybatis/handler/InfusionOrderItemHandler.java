package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.order.entity.OrderItem;

public class InfusionOrderItemHandler implements TypeHandler<List<OrderItem>> {

	@Override
	public List<OrderItem> getResult(ResultSet rs, String fld) throws SQLException {
		String value = rs.getString(fld);
		List<OrderItem> result = new ArrayList<OrderItem>();
		if(StringUtils.isEmpty(value)){
			return result;
		}else{
			String[] itemStrs = value.split(MnisConstants.COMMA);
			for (int i = 0; i < itemStrs.length; i++) {
				OrderItem item = new OrderItem();
				result.add(item);
				String[] itemSubs = itemStrs[i].split(MnisConstants.COLON);
				for (int j = 0; j < itemSubs.length; j++) {
					item.setOrderName(itemSubs[0]);
					item.setDosage(Float.parseFloat(itemSubs[1]));
					item.setDosageUnit(itemSubs[2]);
				}
			}
		}
		return result;
	}

	@Override
	public List<OrderItem> getResult(ResultSet rs, int fldIdx) throws SQLException {
		String value = rs.getString(fldIdx);
		List<OrderItem> result = new ArrayList<OrderItem>();
		if(StringUtils.isEmpty(value)){
			return result;
		}else{
			String[] itemStrs = value.split(MnisConstants.COMMA);
			for (int i = 0; i < itemStrs.length; i++) {
				OrderItem item = new OrderItem();
				result.add(item);
				String[] itemSubs = itemStrs[i].split(MnisConstants.COLON);
				for (int j = 0; j < itemSubs.length; j++) {
					item.setOrderName(itemSubs[0]);
					item.setDosage(Float.parseFloat(itemSubs[1]));
					item.setDosageUnit(itemSubs[2]);
				}
			}
		}
		return result;
	}

	@Override
	public List<OrderItem> getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		String value = callable.getString(fldIdx);
		List<OrderItem> result = new ArrayList<OrderItem>();
		if(StringUtils.isEmpty(value)){
			return result;
		}else{
			String[] itemStrs = value.split(MnisConstants.COMMA);
			for (int i = 0; i < itemStrs.length; i++) {
				OrderItem item = new OrderItem();
				result.add(item);
				String[] itemSubs = itemStrs[i].split(MnisConstants.COLON);
				for (int j = 0; j < itemSubs.length; j++) {
					item.setOrderName(itemSubs[0]);
					item.setDosage(Float.parseFloat(itemSubs[1]));
					item.setDosageUnit(itemSubs[2]);
				}
			}
		}
		return result;
	}



	@Override
	public void setParameter(PreparedStatement ps, int i, List<OrderItem> parameter,
			JdbcType jdbcType) throws SQLException {
		
	}

}
