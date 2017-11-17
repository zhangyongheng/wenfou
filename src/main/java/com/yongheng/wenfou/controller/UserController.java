package com.yongheng.wenfou.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yongheng.wenfou.dto.PageBean;
import com.yongheng.wenfou.dto.Response;
import com.yongheng.wenfou.po.Answer;
import com.yongheng.wenfou.service.AnswerService;
import com.yongheng.wenfou.service.UserService;

import redis.clients.jedis.JedisPool;

@Controller
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private JedisPool jedisPool;

	@RequestMapping("/toLogin")
	public String toLogin() {
		return "toLogin";
	}

	@RequestMapping("/login")
	@ResponseBody
	public Response login(String email, String password, HttpServletResponse response) {

		Map<String, Object> map = userService.login(email, password, response);
		if (map.get("error") == null) {
			return new Response(0, "", map);
		} else {
			return new Response(1, map.get("error").toString());
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		userService.logout(request, response);
		return "redirect:/toLogin";
	}

	@RequestMapping("/profile/{userId}")
	public String profile(@PathVariable Integer userId, Integer page, HttpServletRequest request, Model model) throws Exception {
		Map<String, Object> map = userService.profile(userId);
		if (map.get("user") == null) {
			throw new Exception("该用户不存在");
		}
		// 获取回答列表
		PageBean<Answer> pageBean = answerService.listAnswerByUserId(userId, page);
		//判断用户是否是自己
		boolean isSelf = false;
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("loginToken")){
				String loginToken = cookie.getValue();
				String localUserId = jedisPool.getResource().get(loginToken);
				if (localUserId != null) {
					isSelf = localUserId.equals(String.valueOf(userId));
					break;
				}
			}
		}
		
		map.put("pageBean", pageBean);
		map.put("isSelf", String.valueOf(isSelf));
		model.addAllAttributes(map);
		return "profileAnswer";
	}

	@RequestMapping("/judgePeopleFollowUser")
	@ResponseBody
	public Response judgePeopleFollowUserI(Integer userId, Integer peopleId) {
		Boolean followed = userService.judgePeopleFollowUser(userId, peopleId);
		return new Response(0, "", followed);
	}

	@RequestMapping("/followUser")
	@ResponseBody
	public Response followUser(Integer userId, Integer peopleId) {
		Integer result = userService.followUser(userId, peopleId);
		return new Response(result);
	}

}
