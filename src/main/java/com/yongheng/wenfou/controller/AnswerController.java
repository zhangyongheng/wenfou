package com.yongheng.wenfou.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yongheng.wenfou.dto.Response;
import com.yongheng.wenfou.service.AnswerService;


@Controller
@RequestMapping("/")
public class AnswerController {

	@Autowired
	private AnswerService answerService;

	@RequestMapping("/likeAnswer")
	@ResponseBody
	public Response likeAnswer(Integer answerId, Integer userId, HttpServletRequest request) {
		answerService.likeAnswer(userId, answerId);
		return new Response(0, "");
	}
	
	@RequestMapping("/unLikeAnswer")
	@ResponseBody
	public Response unLikeAnswer(Integer answerId, Integer userId, HttpServletRequest request) {
		answerService.likeAnswer(userId, answerId);
		return new Response(0, "");
	}
}
