package com.yongheng.wenfou.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yongheng.wenfou.po.Topic;

public interface TopicMapper {

	Integer selectTopicIdByTopicName(@Param("topicName") String topicName);

	Integer insertTopic(Topic topic);

	Topic selectTopicByTopicId(@Param("topicId") Integer topicId);

	List<Topic> listTopicByTopicId(List<Integer> idList);

}
