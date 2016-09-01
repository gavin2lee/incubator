package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class DoubleToBoolHandler implements TypeHandler<Boolean> {

	@Override
	public Boolean getResult(ResultSet rs, String fld) throws SQLException {
		double value = rs.getDouble(fld);

		return value < 0;
	}

	@Override
	public Boolean getResult(ResultSet rs, int fldIdx) throws SQLException {
		double value = rs.getDouble(fldIdx);
		return value < 0;
	}

	@Override
	public Boolean getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		double value = callable.getDouble(fldIdx);
		return value < 0;
	}



	@Override
	public void setParameter(PreparedStatement ps, int i, Boolean parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
