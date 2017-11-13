package com.yongheng.wenfou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yongheng.wenfou.dao.AnswerMapper;
import com.yongheng.wenfou.dto.PageBean;
import com.yongheng.wenfou.po.Answer;

@Service
@Transactional
public class AnswerService {

	@Autowired
	private AnswerMapper answerMapper;

	@Transactional(readOnly = true)
	public PageBean<Answer> listAnswerByUserId(Integer userId, Integer curPage) {

		// 当请求页数为空时
		curPage = curPage == null ? 1 : curPage;
		// 每页记录数，从哪开始
		int limit = 8;
		int offset = (curPage - 1) * limit;
		// 获得总记录数，总页数
		int allCount = answerMapper.selectAnswerCountByUserId(userId);
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
		List<Answer> answerList = answerMapper.listAnswerByUserId(map);

		// 构造PageBean
		PageBean<Answer> pageBean = new PageBean<>(allPage, curPage);
		pageBean.setList(answerList);

		return pageBean;
	}

	// 点赞答案
	public void likeAnswer(Integer userId, Integer answerId) {
		Integer likedCount = answerMapper.selectLikedCountByAnswerId(answerId);
		// 更新答案被点赞数量
		Map<String, Object> map = new HashMap<>();
		map.put("likedCount", ++likedCount);
		answerMapper.updateLikedCount(map);

	}

	// 取消点赞
	public void unLikeAnswer(Integer userId, Integer answerId) {
		Integer likedCount = answerMapper.selectLikedCountByAnswerId(answerId);
		// 更新答案被点赞数量
		Map<String, Object> map = new HashMap<>();
		map.put("likedCount", --likedCount);
		answerMapper.updateLikedCount(map);

	}

}
