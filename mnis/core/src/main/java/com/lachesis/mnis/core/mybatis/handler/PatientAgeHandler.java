package com.lachesis.mnis.core.mybatis.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.lachesis.mnis.core.identity.entity.AgeEntity;

/**
 * 通过生日日期转换成年龄
 * 
 * @author zhongzhong.zhou
 * 
 */
public class PatientAgeHandler implements TypeHandler<String> {	
	private static final String IN_DATE_COL = "in_date";
	@Override
	public String getResult(ResultSet rs, String fld) throws SQLException {
		return calculateAge(rs.getDate(IN_DATE_COL), rs.getDate(fld));
	}

	@Override
	public String getResult(ResultSet rs, int fldIdx) throws SQLException {
		return calculateAge(rs.getDate(IN_DATE_COL), rs.getDate(fldIdx));
	}

	@Override
	public String getResult(CallableStatement callable, int fldIdx) throws SQLException {
		return calculateAge(callable.getDate(IN_DATE_COL),  callable.getDate(fldIdx));
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, String arg2, JdbcType arg3)
			throws SQLException {
		//
	}
	
	private String calculateAge(Date inDate, Date birthday) {
		//出生日期为空，默认为0  created by qingzhi.liu 2016-04-21
		if(birthday==null){  
			return null;
		}
		//出生日期为空，默认为0  end by qingzhi.liu 2016-04-21 
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(birthday);
		boolean bool = true;
		
		int m =  calendar.get(Calendar.MONTH)-calendar2.get(Calendar.MONTH);
		if(m<0){
			bool =false;
		}
		int d = calendar.get(Calendar.DAY_OF_MONTH)-calendar2.get(Calendar.DAY_OF_MONTH);
		if(bool){
			if(m==0&& d<0){
				bool = false;
			}
		}
		
		int y = calendar.get(Calendar.YEAR)-calendar2.get(Calendar.YEAR)-(bool?0:1);
		AgeEntity ageEntity = null;
		if(y==0){
			if(m>0){
				ageEntity =new AgeEntity(m, AgeEntity.AGE_UNIT_MONTH);
			}else{
				ageEntity =new AgeEntity(d, AgeEntity.AGE_UNIT_DAY);
			}
		}else{
			ageEntity = new AgeEntity(y, AgeEntity.AGE_UNIT_YEAR);
		}
		
		//return AgeEntity.calculateAge(inDate, birthday ).getAgeString();   
		//原来计算岁数按照365天计算，计算年龄会差几天，暂时修改按照当前天减去出生日期来计算年龄
		return ageEntity.getAgeString();
	}

}
