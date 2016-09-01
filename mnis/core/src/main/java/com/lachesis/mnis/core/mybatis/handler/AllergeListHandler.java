package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class AllergeListHandler implements TypeHandler<List<String> > {

	@Override
	public void setParameter(PreparedStatement ps, int i,
			List<String> parameter, JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getResult(ResultSet rs, String columnName)
			throws SQLException {
		List<String> stringList = new ArrayList<String>();
		while(rs.next() ){
			stringList.add(  rs.getString(columnName) );
		}
		return stringList;
	}

	@Override
	public List<String> getResult(ResultSet rs, int columnIndex)
			throws SQLException {
		List<String> stringList = new ArrayList<String>();
		while(rs.next() ){
			stringList.add(  rs.getString(columnIndex) );
		}
		return stringList;
	}

	@Override
	public List<String> getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		//
		return null;
	}

}
