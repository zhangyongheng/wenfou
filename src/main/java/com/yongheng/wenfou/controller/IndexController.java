package com.yongheng.wenfou.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yongheng.wenfou.dto.Response;
import com.yongheng.wenfou.service.UserService;

@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/", "/index" })
	public String index() {
		return "index";
	}

	@PostMapping("getIndexDetail")
	@ResponseBody
	public Response getIndexDetail(Integer page, Integer userId, HttpServletRequest request) {
		Map<String, Object> map = userService.getIndexDetail(userId, page);

		return new Response(0, "", map);
	}

}
