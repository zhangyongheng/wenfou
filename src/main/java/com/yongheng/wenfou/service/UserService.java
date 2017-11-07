package com.yongheng.wenfou.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yongheng.wenfou.dao.UserMapper;
import com.yongheng.wenfou.po.User;
import com.yongheng.wenfou.util.MyUtil;

@Service
public class UserService {

	@Autowired
	private UserMapper userMapper;

	/* 注册 */
	public Map<String, String> register(String username, String email, String password) {
		Map<String, String> map = new HashMap<>();
		// 校验邮箱格式
		Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$");
		Matcher m = p.matcher(email);
		if (!m.matches()) {
			map.put("regi-email-error", "请输入正确的邮箱");
			return map;
		}

		// 校验用户名长度
		if (StringUtils.isEmpty(username) || username.length() > 10) {
			map.put("regi-username-error", "用户名长度须在1-10个字符");
			return map;
		}

		// 校验密码长度
		p = Pattern.compile("^\\w{6,20}$");
		m = p.matcher(password);
		if (!m.matches()) {
			map.put("regi-password-error", "密码长度须在6-20个字符");
			return map;
		}

		int emailCount = userMapper.selectEmailCount(email);
		if (emailCount > 0) {
			map.put("regi-email-error", "该邮箱已注册");
			return map;
		}

		User user = new User();
		user.setEmail(email);
		user.setPassword(MyUtil.md5(password));
		// 构造user，设置未激活
		String activateCode = MyUtil.createRandomCode();
		user.setActivationCode(activateCode);
		user.setJoinTime(new Date().getTime());

		user.setUsername(username);

		// 向数据库插入记录
		userMapper.insertUser(user);

		map.put("ok", "注册完成");
		return map;
	}

	/* 登录 */
	public Map<String, Object> login(String email, String password, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();

		// 校验用户名和密码是否正确
		Integer userId = userMapper.selectUserIdByEmailAndPassword(email, MyUtil.md5(password));
		if (userId == null) {
			map.put("error", "用户名或密码错误");
			return map;
		}

		// 校验用户帐号是否激活
		Integer activationState = userMapper.selectActivationStateByUserId(userId);
		if (activationState != 1) {
			map.put("error", "您的帐号还没有激活");
			return map;
		}

		// 设置登录cookie
		String loginToken = MyUtil.createRandomCode();
		Cookie cookie = new Cookie("loginToken", loginToken);
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 30);
		response.addCookie(cookie);

		// 将用户信息返回，存入localStorage
		User user = userMapper.selectUserInfoByUserId(userId);
		user.setUserId(userId);
		map.put("userInfo", user);

		return map;
	}

	/* 激活帐号 */
	public void activate(String activationCode) {
		userMapper.updateActivationStateByActivationCode(activationCode);
	}

	public String logout(HttpServletRequest request, HttpServletResponse response) {
		String loginToken = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("loginToken")) {
				loginToken = cookie.getValue();
				break;
			}
		}

		Cookie cookie = new Cookie("loginToken", "");
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 30);
		response.addCookie(cookie);

		return loginToken;
	}

	public User getProfileInfo(Integer userId) {
		User user = userMapper.selectProfileInfoByUserId(userId);
		return user;
	}

	public void updateProfile(User user) {
		userMapper.updateProfile(user);
	}

	public Map<String, String> updatePassword(Integer userId, String password, String newpassword) {

		Map<String, String> map = new HashMap<String, String>();
		int userCount = userMapper.selectUserCountByUserIdAndPassword(userId, MyUtil.md5(password));
		if (userCount < 1) {
			map.put("error", "原密码不正确");
			return map;
		}
		userMapper.updatePassword(userId, MyUtil.md5(newpassword));
		return map;
	}

	public void updateAvatarUrl(Integer userId, String avatarUrl) {
		userMapper.updateAvatarUrl(userId, avatarUrl);

	}

}