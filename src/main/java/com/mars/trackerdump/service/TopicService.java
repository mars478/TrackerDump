package com.mars.trackerdump.service;

import com.mars.trackerdump.entity.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicService extends CrudRepository<Topic, Long> {

    public TopicService setDbName(String dbName);
}
