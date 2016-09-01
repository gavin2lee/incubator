package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class OrderStopDatetimeHandler implements TypeHandler<Date> {

	private static final String STOP_DATETIME = "stop_datetime";

	@Override
	public void setParameter(PreparedStatement ps, int i, Date parameter,
			JdbcType jdbcType) throws SQLException {

	}

	@Override
	public Date getResult(ResultSet rs, String columnName) throws SQLException {
		String dcUserDc = rs.getString(columnName);

		return getStopDate(dcUserDc, rs);
	}

	@Override
	public Date getResult(ResultSet rs, int columnIndex) throws SQLException {
		String dcUserDc = rs.getString(columnIndex);
		return getStopDate(dcUserDc, rs);
	}

	@Override
	public Date getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return null;
	}

	private Date getStopDate(String dcUserDc, ResultSet rs) throws SQLException {
		Date stopDate = null;
		if (dcUserDc != null) {
			stopDate = rs.getTimestamp(STOP_DATETIME);
		}
		return stopDate;
	}

}
