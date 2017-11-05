package com.yongheng.wenfou.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.yongheng.wenfou.po.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class UserMapperTest {
	
	@Autowired
	private UserMapper userMapper;
	
	@Test
	public void selectUserInfoByUserIdTest() {
		
		User user = userMapper.selectUserInfoByUserId(5);
		System.out.println(user.getUsername());
		
	}
	

}
