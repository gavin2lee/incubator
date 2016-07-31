package com.gl.exchange.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.gl.exchange.model.CashAccount;

public interface CashAccountRepository {
	@Insert("insert into CashAccount (holder, balance) values (#{holder}, #{balance})")
	void insertOne(CashAccount ca);
	
	@Update("update CashAccount set balance = #{balance} where id = #{id}")
	void updateOne(CashAccount ca);
	
	@Select("select * from CashAccount where holder = #{holder}")
	CashAccount findByHolder(String holder);
}
