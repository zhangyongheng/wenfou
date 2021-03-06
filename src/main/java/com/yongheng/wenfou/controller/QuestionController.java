package com.yongheng.wenfou.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yongheng.wenfou.dto.Response;
import com.yongheng.wenfou.po.Question;
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
	
	@PostMapping("/ask")
	@ResponseBody
	public Response ask(Question question, String topicName, HttpServletRequest request) {
		Integer userId = userService.getUserIdFromRedis(request);
		Integer questionId = questionService.ask(question, topicName, userId);
		
		return new Response(0, "", questionId);
	}
	
	@PostMapping("/listQuestionByPage")
	@ResponseBody
	public Response listQuestionByPage(Integer page) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Question> questionList = questionService.listQuestionByPage(page);
		map.put("questionList", questionList);
		return new Response(0, "", map);
	}
}
