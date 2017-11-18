package com.yongheng.wenfou.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yongheng.wenfou.service.QuestionService;
import com.yongheng.wenfou.service.UserService;

@Controller
@RequestMapping("/")
public class QuestionController {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private UserService userService;

	@RequestMapping("/question/{questionId}")
	public String questionDetail(@PathVariable Integer questionId, HttpServletRequest request, Model model) {
		Integer userId = userService.getUserIdFromRedis(request);
	
		Map<String, Object> questionDetail = questionService.getQuestionDetail(questionId, userId);
	
		model.addAllAttributes(questionDetail);
		return "questionDetail";
	}

	@RequestMapping("/questionList")
	public String questionList() {
		return "questionList";
	}

}
