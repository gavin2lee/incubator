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

import com.gl.avs.model.User;
import com.gl.avs.model.UserGroup;
import com.gl.avs.vo.UserGroupVO;
import com.gl.avs.vo.UserVO;

@ContextConfiguration(locations = {"classpath:config/spring/repository.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@Rollback(false)
@ActiveProfiles("dev")
public class UserRepositoryTest {
	@Autowired
	UserRepository repo;
	
	@Autowired
	UserGroupRepository ugRepo;

	@Test
	@Ignore
	public void testInsertOne() {
		User u = new User();
		u.setBirth(new Date());
		u.setCreateAt(new Date());
		u.setFirstName("Gavin");
		u.setLastName("li");
		u.setGender("男");
		u.setUsername("abc");
		u.setPasswd("123");
		
		UserGroupVO ugVO = ugRepo.findByCode((byte)10);
		UserGroup ug = new UserGroup();
		ug.setOid(ugVO.getOid());
		ug.setCode((byte)ugVO.getCode());
		ug.setName(ugVO.getName());
		
		u.setUserGroup(ug);
		
		int res = repo.insertUser(u);
		
		System.out.println("result of insert:"+res);
	}
	
	@Test
	public void findByTemplate(){
		User template = new User();
		template.setFirstName("Gavin");
		template.setLastName("li");
		
		List<UserVO> users = repo.findByTemplate(template);
		
		assertThat(users, notNullValue());
		assertThat(users.size(),greaterThan(0) );
	}
	
	public void updateUser(){
		
	}
	
	public void deleteUser(){
		
	}

	@Test
	public void testFindById() {
		//fail("Not yet implemented");
//		System.out.println();
	}

	@Test
	public void testFindByUsername() {
		//fail("Not yet implemented");
	}

}
