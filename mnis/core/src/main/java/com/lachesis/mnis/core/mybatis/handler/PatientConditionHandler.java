package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.util.NurseConstants;

public class PatientConditionHandler implements TypeHandler<String> {

	@Override
	public String getResult(ResultSet rs, String fld) throws SQLException {
		String value = rs.getString(fld);
		if("0".equals(value)){
			return NurseConstants.PATIENT_CONDITION[0];
		}
		if("1".equals(value)){
			return NurseConstants.PATIENT_CONDITION[1];
		}
		if("2".equals(value)){
			return NurseConstants.PATIENT_CONDITION[2];
		}
		return NurseConstants.PATIENT_CONDITION[0];
	}

	@Override
	public String getResult(ResultSet rs, int fldIdx) throws SQLException {
		String value = rs.getString(fldIdx);
		if("0".equals(value)){
			return NurseConstants.PATIENT_CONDITION[0];
		}
		if("1".equals(value)){
			return NurseConstants.PATIENT_CONDITION[1];
		}
		if("2".equals(value)){
			return NurseConstants.PATIENT_CONDITION[2];
		}
		return NurseConstants.PATIENT_CONDITION[0];
	}

	@Override
	public String getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		String value = callable.getString(fldIdx);
		if("0".equals(value)){
			return NurseConstants.PATIENT_CONDITION[0];
		}
		if("1".equals(value)){
			return NurseConstants.PATIENT_CONDITION[1];
		}
		if("2".equals(value)){
			return NurseConstants.PATIENT_CONDITION[2];
		}
		return NurseConstants.PATIENT_CONDITION[0];
	}



	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
