package com.gl.avs.user.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.gl.avs.user.repository.UserRepository;
import com.gl.avs.vo.UserVO;

@Repository
public class UserRepositoryMongoImpl implements UserRepository {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public UserVO findOne(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String save(UserVO user) {
		mongoTemplate.insert(user);
		return "ok";
	}

}
