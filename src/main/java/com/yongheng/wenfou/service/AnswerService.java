package com.yongheng.wenfou.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yongheng.wenfou.dao.AnswerMapper;
import com.yongheng.wenfou.dao.CommentMapper;
import com.yongheng.wenfou.dto.PageBean;
import com.yongheng.wenfou.po.Answer;
import com.yongheng.wenfou.util.RedisKey;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@Transactional
public class AnswerService {

	@Autowired
	private AnswerMapper answerMapper;
	
	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private JedisPool jedisPool;

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

	/**
	 *  点赞答案
	 * @param userId
	 * @param answerId
	 */
	public void likeAnswer(Integer userId, Integer answerId) {
		Integer likedCount = answerMapper.selectLikedCountByAnswerId(answerId);
		// 更新答案被点赞数量
		Map<String, Object> map = new HashMap<>();
		map.put("likedCount", ++likedCount);
		map.put("answerId", answerId);
		answerMapper.updateLikedCount(map);
		// Redis中记录点赞用户
		Jedis jedis = jedisPool.getResource();
		jedis.zadd(answerId + RedisKey.LIKED_ANSWER, System.currentTimeMillis(), String.valueOf(userId));
		jedis.close();
		jedis = null;
	}

	/** 
	 * 取消点赞
	 * @param userId
	 * @param answerId
	 */
	public void unLikeAnswer(Integer userId, Integer answerId) {
		Integer likedCount = answerMapper.selectLikedCountByAnswerId(answerId);
		// 更新答案被点赞数量
		Map<String, Object> map = new HashMap<>();
		map.put("likedCount", --likedCount);
		map.put("answerId", answerId);
		answerMapper.updateLikedCount(map);
		// Redis中删除点赞用户
		Jedis jedis = jedisPool.getResource();
		jedis.zrem(answerId + RedisKey.LIKED_ANSWER, String.valueOf(userId));
		jedis.close();
		jedis = null;
	}

	public Integer answer(Answer answer, Integer userId) {
		answer.setUserId(userId);
		answer.setCreateTime(System.currentTimeMillis());
		answerMapper.insertAnswer(answer);

		return answer.getAnswerId();
	}
	
	@Transactional(readOnly = true)
	public Integer getUserIdByAnswerId(Integer answerId) {
		return answerMapper.selectUserIdByAnswerId(answerId);
	}

	public void deleteAnswer(Integer answerId) {
		answerMapper.deleteAnswerByAnswerId(answerId);
		commentMapper.deleteCommentsByAnswerId(answerId);
		Jedis jedis = jedisPool.getResource();
		jedis.del(answerId + RedisKey.LIKED_ANSWER);
		jedis.close();
		jedis = null;
		
	}

}
