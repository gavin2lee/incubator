package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class LabTestNormalFlagHandler implements TypeHandler<String> {

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {

	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String flagString = rs.getString(columnName);
		return getNormalFlag(flagString);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String flagString = rs.getString(columnIndex);
		return getNormalFlag(flagString);
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String flagString = cs.getString(columnIndex);
		return getNormalFlag(flagString);
	}

	private String getNormalFlag(String flag) {
		String normalFlag = (flag==null)?"N":flag;
		/*if ("高".equals(flag) || "高于极限".equals(flag)) {
			normalFlag = "H";
		} else if ("低".equals(flag) || "低于极限".equals(flag)) {
			normalFlag = "L";
		} else {
			normalFlag = "N";
		}*/

		return normalFlag;
	}
}
