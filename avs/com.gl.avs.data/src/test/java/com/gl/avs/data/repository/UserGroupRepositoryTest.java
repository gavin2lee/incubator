package com.gl.avs.data.repository;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gl.avs.model.UserGroup;
import com.gl.avs.vo.UserGroupVO;

@ContextConfiguration(locations = {"classpath:config/spring/repository.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(false)
@ActiveProfiles("dev")
public class UserGroupRepositoryTest {
	@Autowired
	UserGroupRepository repo;

	@Test
	@Ignore
	public void insertOne() {
		UserGroup ug = new UserGroup();
		ug.setCreateAt(new Date());
		ug.setCode((byte)1);
		ug.setName("user-group-test");
		
		repo.insertOne(ug);
	}
	
	@Test
	@Ignore
	public void findById(){
		UserGroupVO ug = repo.findById(2L);
		assertThat(ug, notNullValue());
	}
	
	@Test
	public void findAll(){
		List<UserGroupVO> userGroups = repo.findAll();
		assertThat(userGroups, notNullValue());
		assertThat(userGroups.size(), greaterThan(0));
	}

}
