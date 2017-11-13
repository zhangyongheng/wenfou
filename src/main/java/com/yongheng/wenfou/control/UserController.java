package com.yongheng.wenfou.control;

import java.util.Map;

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

@Controller
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AnswerService answerService;

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
	public String profile(@PathVariable Integer userId, Integer page, HttpServletRequest request, Model model) {
		Map<String, Object> map = userService.profile(userId);
		// 获取回答列表
		PageBean<Answer> pageBean = answerService.listAnswerByUserId(userId, page);
		map.put("pageBean", pageBean);

		model.addAllAttributes(map);
		return "profileAnswer";
	}

}
