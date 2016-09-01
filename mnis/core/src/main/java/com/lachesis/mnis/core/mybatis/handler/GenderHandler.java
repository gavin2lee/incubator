package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class GenderHandler implements TypeHandler<String> {

	@Override
	public String getResult(ResultSet rs, String fld) throws SQLException {
		String gender = rs.getString(fld);
		if(gender == null) {
			return null;
		}
		return gender.equals("M")?"男":"女";
	}

	@Override
	public String getResult(ResultSet rs, int fldIdx) throws SQLException {
		String gender = rs.getString(fldIdx);
		if(gender == null) {
			return null;
		}
		return gender.equals("M")?"男":"女";
	}

	@Override
	public String getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		String gender =callable.getString(fldIdx);
		if(gender==null) {
			return null;
		}
		return gender.equals("M")?"男":"女";
	}
	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		
	}


}
