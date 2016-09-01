package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.util.OrderUtil;

public class OrderExecStatusNameHandler implements TypeHandler<String> {

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {

	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String orderExecId = rs.getString(columnName);
		return OrderUtil.processOrderExecStatusName(orderExecId);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String orderExecId = rs.getString(columnIndex);
		return OrderUtil.processOrderExecStatusName(orderExecId);
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String orderExecId = cs.getString(columnIndex);
		return OrderUtil.processOrderExecStatusName(orderExecId);
	}
}
