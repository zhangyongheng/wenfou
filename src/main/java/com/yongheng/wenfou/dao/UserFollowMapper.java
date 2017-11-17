package com.yongheng.wenfou.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yongheng.wenfou.po.UserFollow;

public interface UserFollowMapper {
	
	List<UserFollow> listUserFollowByUserId(@Param("userId") Integer userId);
	
	Integer addUserFollow(@Param("userId") Integer userId, @Param("followUserId") Integer followUserId);

}
