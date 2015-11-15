package com.mars.trackerdump.db;

import com.mars.trackerdump.entity.Topic;
import org.springframework.data.repository.CrudRepository;

public interface TopicService extends CrudRepository<Topic, Long> {
}
