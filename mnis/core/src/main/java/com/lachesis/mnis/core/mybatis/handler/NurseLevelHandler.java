package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.util.CommonHelper;

/**
 * 护理等级转换类
 *
 */
public class NurseLevelHandler implements TypeHandler<Integer> {

	@Override
	public Integer getResult(ResultSet resultSet, String field) throws SQLException {
		
		String tend = resultSet.getString(field);
		return CommonHelper.getNurseLevel(tend);
	}

	@Override
	public Integer getResult(ResultSet resultSet, int fieldIdx) throws SQLException {
		
		String tend = resultSet.getString(fieldIdx);
		return CommonHelper.getNurseLevel(tend);
	}

	@Override
	public Integer getResult(CallableStatement callable, int fieldIdx)
			throws SQLException {
		String tend = callable.getString(fieldIdx);
		return CommonHelper.getNurseLevel(tend);
	}

	@Override
	public void setParameter(PreparedStatement pstat, int fieldIdx, Integer nurseLevel,
			JdbcType arg3) throws SQLException {
		
	}
}
