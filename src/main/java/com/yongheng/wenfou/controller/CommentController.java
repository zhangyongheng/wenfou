package com.yongheng.wenfou.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yongheng.wenfou.dto.Response;
import com.yongheng.wenfou.po.AnswerComment;
import com.yongheng.wenfou.service.CommentService;
import com.yongheng.wenfou.service.UserService;


@Controller
@RequestMapping("/")
public class CommentController {

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@RequestMapping("/commentAnswer")
	@ResponseBody
	public Response commentAnswer(Integer answerId, String commentContent, HttpServletRequest request) {
		Integer userId = userService.getUserIdFromRedis(request);
		AnswerComment comment = commentService.commentAnswer(answerId, commentContent, userId);
		return new Response(0, "", comment);
	}

	@RequestMapping("/replyAnswerComment")
	@ResponseBody
	public Response replyAnswerComment(AnswerComment answerComment, HttpServletRequest request) {
		System.out.println(answerComment);
		Integer userId = userService.getUserIdFromRedis(request);
		AnswerComment comment = commentService.replyAnswerComment(answerComment, userId);
		return new Response(0, "", comment);
	}
}
