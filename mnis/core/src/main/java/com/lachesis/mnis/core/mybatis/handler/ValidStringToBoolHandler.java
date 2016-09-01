package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class ValidStringToBoolHandler implements TypeHandler<Boolean> {

	@Override
	public Boolean getResult(ResultSet rs, String fld) throws SQLException {
		String value = rs.getString(fld);
		boolean isTrue = false;
		if(value.equals("1")){
			isTrue = true;
		}
		return isTrue;
	}

	@Override
	public Boolean getResult(ResultSet rs, int fldIdx) throws SQLException {
		String value = rs.getString(fldIdx);
		boolean isTrue = false;
		if(value.equals("1")){
			isTrue = true;
		}
		return isTrue;
	}

	@Override
	public Boolean getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		String value = callable.getString(fldIdx);
		boolean isTrue = false;
		if(value.equals("1")){
			isTrue = true;
		}
		return isTrue;
	}



	@Override
	public void setParameter(PreparedStatement ps, int i, Boolean parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
