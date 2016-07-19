package com.gl.avs.user.repository;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gl.avs.user.Application;
import com.gl.avs.vo.UserVO;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={Application.class})
public class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;

	@Test
	public void testFindOne() {
		assertThat(userRepository, notNullValue());
		
		
	}

	@Test
	public void testSave() {
		assertThat(userRepository, notNullValue());
		
		UserVO user = new UserVO();
		user.setUsername("test-a");
		
		user.setBirth(new Date());
		
		userRepository.save(user);
	}

}
