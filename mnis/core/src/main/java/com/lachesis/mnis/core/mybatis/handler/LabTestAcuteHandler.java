package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class LabTestAcuteHandler implements TypeHandler<Integer> {

	@Override
	public void setParameter(PreparedStatement ps, int i, Integer parameter,
			JdbcType jdbcType) throws SQLException {
		
	}

	@Override
	public Integer getResult(ResultSet rs, String columnName)
			throws SQLException {
		String recordFlagString = rs.getString(columnName);
		
		return getRecordFlag(recordFlagString);
	}

	@Override
	public Integer getResult(ResultSet rs, int columnIndex) throws SQLException {
		String recordFlagString = rs.getString(columnIndex);
		return getRecordFlag(recordFlagString);
	}

	@Override
	public Integer getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String recordFlagString = cs.getString(columnIndex);
		return getRecordFlag(recordFlagString);
	}
	
	private int getRecordFlag(String recordFlagString){
		int recordFlag = 0;
		if(recordFlagString !=null && recordFlagString.equals("加急")){
			recordFlag = 1;
		}
		return recordFlag;
	}
}
