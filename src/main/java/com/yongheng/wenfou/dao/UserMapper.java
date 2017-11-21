package com.yongheng.wenfou.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.yongheng.wenfou.po.User;

@Repository
public interface UserMapper {

	void insertUser(User user);

	int selectEmailCount(String email);

	Integer selectUserIdByEmailAndPassword(@Param("email") String email, @Param("password") String password);

	User selectUserInfoByUserId(@Param("userId") Integer userId);

	Integer selectActivationStateByUserId(@Param("userId") Integer userId);

	void updateActivationStateByActivationCode(@Param("activationCode") String activationCode);

	User selectProfileInfoByUserId(@Param("userId") Integer userId);

	List<User> listUserInfoByUserId(List<Integer> userIdList);

	void updateLikedCountByAnswerId(@Param("answerId") Integer answerId);

	void updateCollectedCountByAnswerId(@Param("answerId") Integer answerId);

	void updateProfile(User user);

	int selectUserCountByUserIdAndPassword(@Param("userId") Integer userId, @Param("password") String password);

	void updatePassword(@Param("userId") Integer userId, @Param("newpassword") String newpassword);

	void updateAvatarUrl(@Param("userId") Integer userId, @Param("avatarUrl") String avatarUrl);

	String selectUsernameByUserId(@Param("userId") Integer userId);

}
