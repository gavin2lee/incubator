package com.lachesis.mnis.core.mybatis.handler;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 转换数量
 * @author xin.chen
 *
 */
public class FeeQuantityHandler implements TypeHandler<Double> {

	@Override
	public Double getResult(ResultSet rs, String fld) throws SQLException {
		double unitPrice = rs.getDouble("unit_price");
		double totCost = rs.getDouble("tot_cost");
		return number4((totCost/unitPrice*1.0));
	}
	@Override
	public Double getResult(ResultSet rs, int fldIdx) throws SQLException {
		double unitPrice = rs.getDouble("unit_price");
		double totCost = rs.getDouble("tot_cost");
		return number4((totCost/unitPrice*1.0));
	}
	@Override
	public Double getResult(CallableStatement callable, int fldIdx)
			throws SQLException {
		double unitPrice = callable.getDouble("unit_price");
		double totCost = callable.getDouble("tot_cost");
		return number4((totCost/unitPrice*1.0));
	}
	@Override
	public void setParameter(PreparedStatement ps, int i, Double parameter,
			JdbcType jdbcType) throws SQLException {
		
	}
	private Double number4(Double pDouble){
		BigDecimal bd = new BigDecimal(pDouble);
		bd = bd.setScale(4, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
}
