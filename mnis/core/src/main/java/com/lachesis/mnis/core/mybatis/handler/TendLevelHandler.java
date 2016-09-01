package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class TendLevelHandler implements TypeHandler<Integer> {

	@Override
	public Integer getResult(ResultSet rs, String fld) throws SQLException {
		String value = rs.getString(fld);
		if(StringUtils.isBlank(value)){
			return 3;
		}
		if(value.contains("一级护理")){
			return 1;
		}
		if(value.contains("二级护理")){
			return 2;
		}
		if(value.contains("三级护理")){
			return 3;
		}
		if(value.contains("特级护理")){
			return 0;
		}
		return 3;
	}

	@Override
	public Integer getResult(ResultSet rs, int fldIdx) throws SQLException {
		String value = rs.getString(fldIdx);
		if(StringUtils.isBlank(value)){
			return 3;
		}
		if(value.contains("一级护理")){
			return 1;
		}
		if(value.contains("二级护理")){
			return 2;
		}
		if(value.contains("三级护理")){
			return 3;
		}
		if(value.contains("特级护理")){
			return 0;
		}
		return 3;
	}

	@Override
	public Integer getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		String value = callable.getString(fldIdx);
		if(StringUtils.isBlank(value)){
			return 3;
		}
		if(value.contains("一级护理")){
			return 1;
		}
		if(value.contains("二级护理")){
			return 2;
		}
		if(value.contains("三级护理")){
			return 3;
		}
		if(value.contains("特级护理")){
			return 0;
		}
		return 3;
	}



	@Override
	public void setParameter(PreparedStatement ps, int i, Integer parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
