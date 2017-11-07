package com.yongheng.wenfou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yongheng.wenfou.dao.AnswerMapper;
import com.yongheng.wenfou.po.Answer;
import com.yongheng.wenfou.po.PageBean;

@Service
public class AnswerService {

	@Autowired
	private AnswerMapper answerMapper;


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

}
