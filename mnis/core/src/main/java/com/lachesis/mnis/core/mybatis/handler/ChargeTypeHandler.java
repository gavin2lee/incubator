package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class ChargeTypeHandler implements TypeHandler<String> {

	@Override
	public String getResult(ResultSet rs, String fld) throws SQLException {
		String value = rs.getString(fld);
		if(StringUtils.isBlank(value)){
			return "";
		}else if(value.equals("01")){
			return "自费";
		}else if(value.equals("02")){
			return "保险";
		}else if(value.equals("03")){
			return "公费在职";
		}else if(value.equals("04")){
			return "公费退休";
		}else if(value.equals("05")){
			return "公费高干";
		}
		return "";
	}

	@Override
	public String getResult(ResultSet rs, int fldIdx) throws SQLException {
		String value = rs.getString(fldIdx);
		if(StringUtils.isBlank(value)){
			return "";
		}else if(value.equals("01")){
			return "自费";
		}else if(value.equals("02")){
			return "保险";
		}else if(value.equals("03")){
			return "公费在职";
		}else if(value.equals("04")){
			return "公费退休";
		}else if(value.equals("05")){
			return "公费高干";
		}
		return "";
	}

	@Override
	public String getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		String value = callable.getString(fldIdx);
		if(StringUtils.isBlank(value)){
			return "";
		}else if(value.equals("01")){
			return "自费";
		}else if(value.equals("02")){
			return "保险";
		}else if(value.equals("03")){
			return "公费在职";
		}else if(value.equals("04")){
			return "公费退休";
		}else if(value.equals("05")){
			return "公费高干";
		}
		return "";
	}



	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
