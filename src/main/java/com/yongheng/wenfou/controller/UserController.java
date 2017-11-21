package com.yongheng.wenfou.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yongheng.wenfou.dto.PageBean;
import com.yongheng.wenfou.dto.Response;
import com.yongheng.wenfou.po.Answer;
import com.yongheng.wenfou.po.Question;
import com.yongheng.wenfou.service.AnswerService;
import com.yongheng.wenfou.service.QuestionService;
import com.yongheng.wenfou.service.UserService;

@Controller
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AnswerService answerService;

	@Autowired
	private QuestionService questionService;

	@RequestMapping("/toLogin")
	public String toLogin() {
		return "toLogin";
	}

	@PostMapping("/login")
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
	
	@PostMapping("/register")
	@ResponseBody
	public Response register(String username, String email, String password, HttpServletResponse response) {
		Map<String, String> map = userService.register(username, email, password);
		if (map.get("ok") != null ) {
			return new Response(0, "注册成功,请登录！", map);
		} else {
			return new Response(-1, "", map);
		}
	}

	@RequestMapping("/profile/{userId}")
	public String profile(@PathVariable Integer userId, Integer page, HttpServletRequest request, Model model)
			throws Exception {
		Map<String, Object> map = userService.profile(userId);
		if (map.get("user") == null) {
			throw new Exception("该用户不存在");
		}
		// 判断用户是否是自己
		boolean isSelf = userService.judgeUserIsSelf(userId, request);
		// 获取回答列表
		PageBean<Answer> pageBean = answerService.listAnswerByUserId(userId, page);

		map.put("isSelf", String.valueOf(isSelf));
		map.put("pageBean", pageBean);
		model.addAllAttributes(map);
		return "profileAnswer";
	}

	@PostMapping("/judgePeopleFollowUser")
	@ResponseBody
	public Response judgePeopleFollowUserI(Integer userId, Integer peopleId) {
		Boolean followed = userService.judgePeopleFollowUser(userId, peopleId);
		return new Response(0, "", followed);
	}

	@PostMapping("/followUser")
	@ResponseBody
	public Response followUser(Integer userId, Integer peopleId) {
		Integer result = userService.followUser(userId, peopleId);
		return new Response(result);
	}

	@PostMapping("/unfollowUser")
	@ResponseBody
	public Response unfollowUser(Integer userId, Integer peopleId) {
		Integer result = userService.unfollowUser(userId, peopleId);
		return new Response(result);
	}

	@RequestMapping("/profileQuestion/{userId}")
	public String profileQuestion(@PathVariable Integer userId, Integer page, HttpServletRequest request, Model model)
			throws Exception {
		Map<String, Object> map = userService.profile(userId);
		if (map.get("user") == null) {
			throw new Exception("该用户不存在");
		}
		// 判断用户是否是自己
		boolean isSelf = userService.judgeUserIsSelf(userId, request);
		PageBean<Question> pageBean = questionService.listQuestionByUserId(userId, page);
		
		map.put("isSelf", String.valueOf(isSelf));
		map.put("pageBean", pageBean);

		model.addAllAttributes(map);
		return "profileQuestion";
	}

}
