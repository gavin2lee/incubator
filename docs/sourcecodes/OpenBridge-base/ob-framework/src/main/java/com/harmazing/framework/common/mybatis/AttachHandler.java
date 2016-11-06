package com.harmazing.framework.common.mybatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import com.harmazing.framework.common.model.Attach;

@SuppressWarnings("rawtypes")
public class AttachHandler extends BaseTypeHandler<Attach> {

	@Override
	public Attach getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		return new Attach(rs.getObject(columnName));
	}

	@Override
	public Attach getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		return new Attach(rs.getObject(columnIndex));
	}

	@Override
	public Attach getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return new Attach(cs.getObject(columnIndex));
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i,
			Attach parameter, JdbcType jdbcType) throws SQLException {
		ps.setObject(i, parameter.getRoot());
	}

}
