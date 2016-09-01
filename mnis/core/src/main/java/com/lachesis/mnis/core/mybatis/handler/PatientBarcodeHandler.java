package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.util.BarcodeUtil;

public class PatientBarcodeHandler implements TypeHandler<String> {

	@Override
	public String getResult(ResultSet resultSet, String field) throws SQLException {
		
		String patId = resultSet.getString(field);
		return BarcodeUtil.makePatientBarcodeFromPatId(patId);
	}

	@Override
	public String getResult(ResultSet resultSet, int fieldIdx) throws SQLException {
		
		String patId = resultSet.getString(fieldIdx);
		return BarcodeUtil.makePatientBarcodeFromPatId(patId);
	}

	@Override
	public String getResult(CallableStatement callable, int fieldIdx)
			throws SQLException {
		String patId = callable.getString(fieldIdx);
		return BarcodeUtil.makePatientBarcodeFromPatId(patId);
	}

	@Override
	public void setParameter(PreparedStatement pstat, int fieldIdx, String barcode,
			JdbcType arg3) throws SQLException {
		
	}
}
