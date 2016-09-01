package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.util.OrderUtil;
import com.lachesis.mnis.core.util.StringUtil;

public class PerformScheduleHandler implements TypeHandler<String> {
	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {

	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String performSchedule = rs.getString(columnName);
		if(StringUtils.isNotBlank(performSchedule)){
			performSchedule = performSchedule.substring(0,performSchedule.indexOf(":")+3);
		}
		return performSchedule;
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String performSchedule = rs.getString(columnIndex);
		if(StringUtils.isNotBlank(performSchedule)){
			performSchedule = performSchedule.substring(0,performSchedule.indexOf(":")+3);
		}
		return performSchedule;
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String performSchedule = cs.getString(columnIndex);
		if(StringUtils.isNotBlank(performSchedule)){
			performSchedule = performSchedule.substring(0,performSchedule.indexOf(":")+3);
		}
		return performSchedule;
	}
}
