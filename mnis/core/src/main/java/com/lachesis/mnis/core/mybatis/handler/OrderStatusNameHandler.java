package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.util.OrderUtil;


public class OrderStatusNameHandler implements TypeHandler<String> {

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {

	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String orderStatus = rs.getString(columnName);
		return OrderUtil.getOrderStatusName(orderStatus);
//		return OrderUtil.processOrderStatusName(orderStatus);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String orderStatus = rs.getString(columnIndex);
		return OrderUtil.getOrderStatusName(orderStatus);
//		return OrderUtil.processOrderStatusName(orderStatus);
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String orderStatus = cs.getString(columnIndex);
		return OrderUtil.getOrderStatusName(orderStatus);
//		return OrderUtil.processOrderStatusName(orderStatus);
	}

}
