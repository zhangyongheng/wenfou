package com.yongheng.wenfou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yongheng.wenfou.dao.AnswerMapper;
import com.yongheng.wenfou.dao.CommentMapper;
import com.yongheng.wenfou.dao.QuestionMapper;
import com.yongheng.wenfou.dao.UserMapper;
import com.yongheng.wenfou.po.Answer;
import com.yongheng.wenfou.po.AnswerComment;
import com.yongheng.wenfou.po.PageBean;
import com.yongheng.wenfou.po.Question;
import com.yongheng.wenfou.po.User;

@Service
public class QuestionService {

	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private AnswerMapper answerMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private CommentMapper commentMapper;


	// 获得问题页详情
	@SuppressWarnings("unchecked")
	public Map<String, Object> getQuestionDetail(Integer questionId) {
		Map<String, Object> map = new HashMap<>();
		// 获取问题信息
		Question question = questionMapper.selectQuestionByQuestionId(questionId);
		if (question == null) {
			throw new RuntimeException("该问题id不存在~");
		}

		// 获取提问用户信息
		User askUser = userMapper.selectUserInfoByUserId(question.getUserId());
		question.setUser(askUser);

		// 获取答案列表信息
		List<Answer> answerList = answerMapper.selectAnswerByQuestionId(questionId);
		for (Answer answer : answerList) {
			User answerUser = userMapper.selectUserInfoByUserId(answer.getUserId());
			answer.setUser(answerUser);
			answer.setLikeState("false");
			// 获取答案评论列表
			List<AnswerComment> answerCommentList = commentMapper.listAnswerCommentByAnswerId(answer.getAnswerId());
			for (AnswerComment comment : answerCommentList) {
				// 为评论绑定用户信息
				User commentUser = userMapper.selectUserInfoByUserId(comment.getUserId());
				comment.setUser(commentUser);
				// 判断用户是否赞过该评论
				// 获取该评论被点赞次数
				comment.setLikeState("false");

			}
			answer.setAnswerCommentList(answerCommentList);

			// 获取用户点赞状态
			// 获取该回答被点赞次数
			
		}

		// 获取话题信息
		Map<Integer, String> topicMap = (Map<Integer, String>) JSON.parse(question.getTopicKvList());

		map.put("topicMap", topicMap);
		map.put("question", question);
		map.put("answerList", answerList);
		return map;
	}

	public PageBean<Question> listQuestionByUserId(Integer userId, Integer curPage) {
		// 当请求页数为空时
		curPage = curPage == null ? 1 : curPage;
		// 每页记录数，从哪开始
		int limit = 8;
		int offset = (curPage - 1) * limit;
		// 获得总记录数，总页数
		int allCount = questionMapper.selectQuestionCountByUserId(userId);
		int allPage = 0;
		if (allCount <= limit) {
			allPage = 1;
		} else if (allCount / limit == 0) {
			allPage = allCount / limit;
		} else {
			allPage = allCount / limit + 1;
		}

		// 构造查询map
		Map<String, Object> map = new HashMap<>();
		map.put("offset", offset);
		map.put("limit", limit);
		map.put("userId", userId);
		// 得到某页数据列表
		List<Question> questionList = questionMapper.listQuestionByUserId(map);

		// 构造PageBean
		PageBean<Question> pageBean = new PageBean<>(allPage, curPage);
		pageBean.setList(questionList);

		return pageBean;
	}



}