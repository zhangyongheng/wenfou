package com.yongheng.wenfou.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.yongheng.wenfou.po.Answer;

public interface AnswerMapper {

	void insertAnswer(Answer answer);

	List<Answer> selectAnswerByQuestionId(@Param("questionId") Integer questionId);

	int selectAnswerCountByUserId(@Param("userId") Integer userId);

	List<Answer> listAnswerByUserId(Map<String, Object> map);

	List<Answer> listAnswerByAnswerId(List<Integer> idList);

	List<Answer> listGoodAnswerByQuestionId(Map<String, Object> map);

	int listAnswerCountByQuestionId(List<Integer> questionIdList);

	int selectAnswerCountByQuestionId(@Param("questionId") Integer questionId);

	void updateLikedCount(Map<String, Object> map);

	List<Answer> listAnswerByUserIdList(Map<String, Object> map);

	List<Answer> listAnswerByCreateTime(@Param("createTime") long createTime);

	Integer selectUserIdByAnswerId(@Param("answerId") Integer answerId);
	
	Integer selectLikedCountByAnswerId(@Param("answerId") Integer answerId);
	
	void deleteAnswerByAnswerId(@Param("answerId") Integer answerId);
	
}
