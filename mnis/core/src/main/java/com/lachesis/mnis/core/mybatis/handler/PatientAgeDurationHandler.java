package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.patient.PatientHelper;
import com.lachesis.mnis.core.util.DateUtil;

/**
 * 通过生日得到年龄阶段数据(婴儿,儿童,成人)
 * 
 *
 */
public class PatientAgeDurationHandler implements TypeHandler<String> {
	private static final String IN_DATE_COL = "in_date";
	
	@Override
	public String getResult(ResultSet rs, String fld) throws SQLException {
		Date currentDate = rs.getDate(IN_DATE_COL);
		if(currentDate == null){
			currentDate = new Date();
		}
		Date birthday = rs.getDate(fld);
		return getAgeDuration(currentDate, birthday);
	}

	@Override
	public String getResult(ResultSet rs, int fldIdx) throws SQLException {
		Date currentDate = rs.getDate(IN_DATE_COL);
		if(currentDate == null){
			currentDate = new Date();
		}
		Date birthday = rs.getDate(fldIdx);
		return getAgeDuration(currentDate, birthday);
	}

	@Override
	public String getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		Date currentDate =callable.getDate(IN_DATE_COL);
		if(currentDate == null){
			currentDate = new Date();
		}
		Date birthday = callable.getDate(fldIdx);
		return getAgeDuration(currentDate, birthday);
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, String arg2,
			JdbcType arg3) throws SQLException {
		
	}

	public String getAgeDuration(Date currentDate, Date birthday) {
		return PatientHelper.getAgeDuration( DateUtil.calDatePoor(birthday, currentDate) );
	}
}
