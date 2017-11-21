package com.yongheng.wenfou.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yongheng.wenfou.dao.AnswerMapper;
import com.yongheng.wenfou.dao.CommentMapper;
import com.yongheng.wenfou.dao.UserFollowMapper;
import com.yongheng.wenfou.dao.UserMapper;
import com.yongheng.wenfou.po.Answer;
import com.yongheng.wenfou.po.User;
import com.yongheng.wenfou.po.UserFollow;
import com.yongheng.wenfou.util.MyUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@Transactional
public class UserService {

	public static final String EMAIL_REGEX = "^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$";

	public static final String PASSWORD_REGEX = "^\\w{6,20}$";

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private AnswerMapper answerMapper;

	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private UserFollowMapper userFollowMapper;

	@Autowired
	private JedisPool jedisPool;

	public Map<String, String> register(String username, String email, String password) {
		Map<String, String> map = new HashMap<>();
		// 校验邮箱格式
		Pattern p = Pattern.compile(EMAIL_REGEX);
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
		p = Pattern.compile(PASSWORD_REGEX);
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

		String activationCode = MyUtil.createRandomCode();
		user.setActivationCode(activationCode);
		user.setJoinTime(System.currentTimeMillis());
		user.setUsername(username);
		user.setAvatarUrl("");

		// 向数据库插入记录
		userMapper.insertUser(user);
		// 设置已激活
		userMapper.updateActivationStateByActivationCode(activationCode);

		map.put("ok", "注册完成");
		return map;
	}

	@Transactional(readOnly = true)
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

		Jedis jedis = jedisPool.getResource();
		jedis.set(loginToken, userId.toString(), "NX", "EX", 60 * 60 * 24 * 30);
		jedis.close();
		jedis = null;

		// 将用户信息返回，存入localStorage
		User user = userMapper.selectUserInfoByUserId(userId);
		user.setUserId(userId);
		map.put("userInfo", user);

		return map;
	}

	public String logout(HttpServletRequest request, HttpServletResponse response) {
		String loginToken = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("loginToken".equals(cookie.getName())) {
				loginToken = cookie.getValue();
				break;
			}
		}
		Cookie cookie = new Cookie("loginToken", "");
		cookie.setPath("/");
		cookie.setMaxAge(60 * 60 * 24 * 30);
		response.addCookie(cookie);

		Jedis jedis = jedisPool.getResource();
		jedis.del(loginToken);
		jedis.close();
		jedis = null;

		return loginToken;
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

	@Transactional(readOnly = true)
	public Map<String, Object> getIndexDetail(Integer userId, Integer curPage) {
		Map<String, Object> map = new HashMap<>();
		List<Integer> idList = new ArrayList<>();
		List<UserFollow> userFollowList = userFollowMapper.listUserFollowByUserId(userId);
		for (UserFollow userFollow : userFollowList) {
			idList.add(userFollow.getFollowUser());
		}
		List<Answer> answerList = new ArrayList<Answer>();
		if (idList.size() > 0) {
			answerList = getIndexDetailReturnAnswerList(idList, curPage);
		}

		map.put("answerList", answerList);
		return map;
	}

	@Transactional(readOnly = true)
	private List<Answer> getIndexDetailReturnAnswerList(List<Integer> idList, Integer curPage) {

		// 当请求页数为空时
		curPage = curPage == null ? 1 : curPage;
		// 每页记录数，从哪开始
		int limit = 8;
		int offset = (curPage - 1) * limit;
		// 构造查询map
		Map<String, Object> map = new HashMap<>();
		map.put("offset", offset);
		map.put("limit", limit);
		map.put("userIdList", idList);
		List<Answer> answerList = answerMapper.listAnswerByUserIdList(map);

		for (Answer answer : answerList) {
			int commentCount = commentMapper.selectAnswerCommentCountByAnswerId(answer.getAnswerId());
			answer.setCommentCount(commentCount);
		}

		return answerList;
	}

	@Transactional(readOnly = true)
	public Map<String, Object> profile(Integer userId) {
		Map<String, Object> map = new HashMap<>();
		User user = userMapper.selectProfileInfoByUserId(userId);

		map.put("user", user);
		return map;
	}

	/**
	 * 判断用户是否关注了某用户
	 * 
	 * @param userId
	 * @param peopleId
	 *            要判断是否被关注的用户ID
	 * @return
	 */
	public Boolean judgePeopleFollowUser(Integer userId, Integer peopleId) {
		List<UserFollow> userFollowList = userFollowMapper.listUserFollowByUserId(userId);
		for (UserFollow userfollow : userFollowList) {
			if (userfollow.getFollowUser() == peopleId) {
				return true;
			}
		}
		return false;
	}

	public Integer followUser(Integer userId, Integer peopleId) {

		return userFollowMapper.addUserFollow(userId, peopleId);
	}

	public Integer unfollowUser(Integer userId, Integer peopleId) {

		return userFollowMapper.removeUserFollow(userId, peopleId);
	}

	public Integer getUserIdFromRedis(HttpServletRequest request) {
		String loginToken = null;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("loginToken".equals(cookie.getName())) {
				loginToken = cookie.getValue();
				break;
			}
		}

		Jedis jedis = jedisPool.getResource();
		String userId = jedis.get(loginToken);
		jedis.close();
		jedis = null;

		return Integer.parseInt(userId);
	}

	public boolean judgeUserIsSelf(Integer userId, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if ("loginToken".equals(cookie.getName())) {
				String loginToken = cookie.getValue();
				String localUserId = jedisPool.getResource().get(loginToken);
				if (localUserId != null) {
					return localUserId.equals(String.valueOf(userId));
				}
			}
		}
		return false;
	}

}
