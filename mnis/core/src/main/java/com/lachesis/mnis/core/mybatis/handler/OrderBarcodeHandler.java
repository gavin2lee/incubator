package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.util.BarcodeUtil;

public class OrderBarcodeHandler implements TypeHandler<String> {
	private static final String DRUG_USAGE_COL = "usage_name";
	private static final String ORDER_CLASS_CODE_COL = "order_exec_type_code";
	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String usage = rs.getString(DRUG_USAGE_COL );
		String classCode = rs.getString(ORDER_CLASS_CODE_COL );
		String orderExecId = rs.getString(columnName);
		return BarcodeUtil.makeOrderBarcodeFrom(orderExecId, usage, classCode);		
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String usage = rs.getString(DRUG_USAGE_COL );
		String orderExecId = rs.getString(columnIndex);
		String classCode = rs.getString(ORDER_CLASS_CODE_COL );
		return BarcodeUtil.makeOrderBarcodeFrom(orderExecId, usage, classCode);
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
