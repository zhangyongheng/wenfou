package com.yongheng.wenfou.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yongheng.wenfou.dao.CommentMapper;
import com.yongheng.wenfou.dao.UserMapper;
import com.yongheng.wenfou.po.AnswerComment;
import com.yongheng.wenfou.po.User;


@Service
public class CommentService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private CommentMapper commentMapper;

	// 评论回答
	public AnswerComment commentAnswer(Integer answerId, String commentContent, Integer userId) {
		AnswerComment comment = new AnswerComment();
		comment.setLikedCount(0);
		comment.setCreateTime(new Date().getTime());
		comment.setAnswerCommentContent(commentContent);
		comment.setAnswerId(answerId);
		comment.setUserId(userId);

		commentMapper.insertAnswerComment(comment);
		User user = userMapper.selectUserInfoByUserId(userId);
		comment.setUser(user);

		return comment;
	}

	public AnswerComment replyAnswerComment(AnswerComment comment, Integer userId) {
		comment.setLikedCount(0);
		comment.setCreateTime(new Date().getTime());
		comment.setUserId(userId);

		commentMapper.insertAnswerCommentReply(comment);
		User user = userMapper.selectUserInfoByUserId(userId);
		comment.setUser(user);

		return comment;
	}

}
