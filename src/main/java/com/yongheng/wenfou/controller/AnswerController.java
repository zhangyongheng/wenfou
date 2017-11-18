package com.yongheng.wenfou.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yongheng.wenfou.dto.Response;
import com.yongheng.wenfou.service.AnswerService;
import com.yongheng.wenfou.service.UserService;

@RestController
@RequestMapping("/")
public class AnswerController {

	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private UserService userService;

	@RequestMapping("/likeAnswer")
	public Response likeAnswer(Integer answerId, HttpServletRequest request) {
		Integer userId = userService.getUserIdFromRedis(request);
		answerService.likeAnswer(userId, answerId);
		return new Response(0, "");
	}

	@RequestMapping("/unLikeAnswer")
	public Response unLikeAnswer(Integer answerId, HttpServletRequest request) {
		Integer userId = userService.getUserIdFromRedis(request);
		answerService.unLikeAnswer(userId, answerId);
		return new Response(0, "");
	}
}
