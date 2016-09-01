package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.util.DateUtil;
import com.lachesis.mnis.core.util.DateUtil.DateFormat;

public class String2DateHMSHandler implements TypeHandler<String> {
	
	@Override
	public String getResult(ResultSet rs, String fld) throws SQLException {
		return DateUtil.format(rs.getTimestamp(fld), DateFormat.HMS);
	}

	@Override
	public String getResult(ResultSet rs, int fldIdx) throws SQLException {
		return DateUtil.format(rs.getTimestamp(fldIdx), DateFormat.HMS);
	}

	@Override
	public String getResult(CallableStatement callable, int fldIdx) throws SQLException {
		return DateUtil.format(callable.getTimestamp(fldIdx), DateFormat.HMS);
	}

	@Override
	public void setParameter(PreparedStatement pstat, int fldIdx, String value,
			JdbcType arg3) throws SQLException {
		if (value != null && !"".equals(value)) {
			pstat.setTimestamp(fldIdx, new Timestamp(DateUtil.parse(value).getTime()));
		}
	}

}
