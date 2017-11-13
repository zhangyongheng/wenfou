package com.yongheng.wenfou.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yongheng.wenfou.dto.Response;
import com.yongheng.wenfou.service.AnswerService;

@RestController
@RequestMapping("/")
public class AnswerController {

	@Autowired
	private AnswerService answerService;

	@RequestMapping("/likeAnswer")
	public Response likeAnswer(Integer answerId, Integer userId, HttpServletRequest request) {
		answerService.likeAnswer(userId, answerId);
		return new Response(0, "");
	}

	@RequestMapping("/unLikeAnswer")
	public Response unLikeAnswer(Integer answerId, Integer userId, HttpServletRequest request) {
		answerService.likeAnswer(userId, answerId);
		return new Response(0, "");
	}
}
