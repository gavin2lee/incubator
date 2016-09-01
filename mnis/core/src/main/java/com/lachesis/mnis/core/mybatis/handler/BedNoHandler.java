package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.patient.PatientHelper;

public class BedNoHandler implements TypeHandler<String> {

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {

	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String roomCode = rs.getString("bed_code");
		return PatientHelper.resolveBedCode(roomCode);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String roomCode = rs.getString("bed_code");
		return PatientHelper.resolveBedCode(roomCode);
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String roomCode = cs.getString("bed_code");
		return PatientHelper.resolveBedCode(roomCode);
	}

}
