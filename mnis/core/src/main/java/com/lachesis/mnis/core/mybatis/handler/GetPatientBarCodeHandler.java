package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;



public class GetPatientBarCodeHandler implements TypeHandler<String>{

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		
	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String patientCode = rs.getString(columnName);
		return "P"+patientCode;
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String patientCode = rs.getString(columnIndex);
		return "P"+patientCode;
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
