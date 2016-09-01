package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.constants.MnisConstants;
import com.lachesis.mnis.core.util.OrderUtil;
import com.lachesis.mnis.core.util.OrderUtil.OrderType;

public class OrderExecTypeCodeHandler implements TypeHandler<String> {
	private static final String DRUG_USAGE_COL = "usage_name";
	//private static final String USAGE_CODE = "usage_code";
	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
		// TODO Auto-generated method stub
	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String usage = rs.getString(DRUG_USAGE_COL);
		String classCode = rs.getString( columnName);
		return makeOrderExecType(usage, classCode);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String usage = rs.getString( DRUG_USAGE_COL);
		String classCode = rs.getString( columnIndex);
		return makeOrderExecType(usage, classCode);
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		return null;
	}
	
	private String makeOrderExecType(String usage, String classCode) {
		if (usage == null && classCode == null) {
			return null;
		} else {

			if (StringUtils.isNumeric(classCode)
					&& StringUtils.isNotBlank(classCode)) {
				if (classCode.equals("052")) {
					classCode = MnisConstants.ORDER_EXEC_TYPE_SKINTEST;
				} else if (classCode.equals("001") || classCode.equals("003")) {
					classCode = MnisConstants.ORDER_EXEC_TYPE_INFUSION;
				} else {
					classCode = MnisConstants.ORDER_EXEC_TYPE_OTHER_DRUG;
				}
			}

			OrderType orderType = OrderUtil.parseOrderType(usage, classCode);
			if (orderType == null) {
				return classCode;
			}
			switch (orderType) {
			case DRUG_INFUSION:
				return MnisConstants.ORDER_EXEC_TYPE_INFUSION;
			case DRUG_ORAL:
				return MnisConstants.ORDER_EXEC_TYPE_ORAL;
			case LAB_TEST:
				return MnisConstants.ORDER_EXEC_TYPE_LAB;
			default:
				return classCode;
			}
		}
	}
}
