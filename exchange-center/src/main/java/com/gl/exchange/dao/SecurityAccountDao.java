package com.gl.exchange.dao;

import com.gl.exchange.model.SecurityAccount;

public interface SecurityAccountDao {
	void insertOne(SecurityAccount sa);
	SecurityAccount findByHolder(String holder);
	void updateOne(SecurityAccount sa);
}
