package com.yongheng.wenfou.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yongheng.wenfou.po.AnswerComment;


public interface CommentMapper {

	List<AnswerComment> listAnswerCommentByAnswerId(@Param("answerId") Integer answerId);

	void insertAnswerComment(AnswerComment comment);

	void insertAnswerCommentReply(AnswerComment comment);

	int selectAnswerCommentCountByAnswerId(@Param("answerId") Integer answerId);
	
	void deleteCommentsByAnswerId(@Param("answerId") Integer answerId);

}
