package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.bodysign.BodySignConstants;

public class ItemMeasureNoteNameHandler implements TypeHandler<String> {

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String measureNoteCode = rs.getString("measure_note_code");
		return BodySignConstants.MAP_BODYSIGN_ITEM.get(measureNoteCode);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String measureNoteCode = rs.getString("measure_note_code");
		return BodySignConstants.MAP_BODYSIGN_ITEM.get(measureNoteCode);
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
