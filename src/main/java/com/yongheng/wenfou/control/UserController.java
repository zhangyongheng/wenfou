package com.yongheng.wenfou.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yongheng.wenfou.service.UserService;
import com.yongheng.wenfou.util.Response;


@Controller
@RequestMapping("/")
public class UserController {

	@Autowired
	private UserService userService;


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

	

}
