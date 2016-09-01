package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.constants.MnisConstants;

public class OrderExecTypeNameHandler implements TypeHandler<String>{

	@Override
	public void setParameter(PreparedStatement ps, int i, String parameter,
			JdbcType jdbcType) throws SQLException {
	}

	@Override
	public String getResult(ResultSet rs, String columnName)
			throws SQLException {
		String usageCode = rs.getString(columnName);
		return processOrderExecTypeName(usageCode);
	}

	@Override
	public String getResult(ResultSet rs, int columnIndex) throws SQLException {
		String usageCode = rs.getString(columnIndex);
		return processOrderExecTypeName(usageCode);
	}

	@Override
	public String getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		String usageCode = cs.getString(columnIndex);
		return processOrderExecTypeName(usageCode);
	}
	
	/**
	 * 医嘱执行药物类型(皮试，输液，其他药物类)
	 * @param usageCode
	 * @return
	 */
	private String processOrderExecTypeName(String usageCode) {
		String typeName = "";
		if (StringUtils.isNotBlank(usageCode)
				&& StringUtils.isNumeric(usageCode)) {
			if (usageCode.equals("052")) {
				typeName = MnisConstants.ORDER_EXEC_TYPE_SKINTEST_NAME;
			} else if (usageCode.equals("001") || usageCode.equals("003")) {
				typeName = MnisConstants.ORDER_EXEC_TYPE_INFUSION_NAME;
			} else {
				typeName = MnisConstants.ORDER_EXEC_TYPE_OTHER_DRUG_NAME;
			}
		} else {
			typeName = usageCode;
		}
		return typeName;
	}
}
