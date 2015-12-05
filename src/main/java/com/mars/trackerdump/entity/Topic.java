package com.mars.trackerdump.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Temporal;
import static javax.persistence.TemporalType.DATE;

@Entity
@Table(name = "topic")
public class Topic {

    //"ID форума";"Название форума";"ID раздачи";"Info hash";"Название раздачи";"Размер в байтах","Дата регистрации торрента"
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    long topic_id;
    long forum_id;
    String forum_name;
    String topic_hash;
    String topic_name;
    long topic_size;
    @Temporal(DATE)
    Date topic_date;

    public Topic() {
    }

    public Topic(long forum_id, String forum_name, long topic_id, String topic_hash, String topic_name, long topic_size, Date topic_date) {
        this.forum_id = forum_id;
        this.forum_name = forum_name;
        this.topic_id = topic_id;
        this.topic_hash = topic_hash;
        this.topic_name = topic_name;
        this.topic_size = topic_size;
        this.topic_date = topic_date;
    }

    public long getForum_id() {
        return forum_id;
    }

    public void setForum_id(long forum_id) {
        this.forum_id = forum_id;
    }

    public String getForum_name() {
        return forum_name;
    }

    public void setForum_name(String forum_name) {
        this.forum_name = forum_name;
    }

    public long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(long topic_id) {
        this.topic_id = topic_id;
    }

    public String getTopic_hash() {
        return topic_hash;
    }

    public void setTopic_hash(String topic_hash) {
        this.topic_hash = topic_hash;
    }

    public String getTopic_name() {
        return topic_name;
    }

    public void setTopic_name(String topic_name) {
        this.topic_name = topic_name;
    }

    public long getTopic_size() {
        return topic_size;
    }

    public void setTopic_size(long topic_size) {
        this.topic_size = topic_size;
    }

    public Date getTopic_date() {
        return topic_date;
    }

    public void setTopic_date(Date topic_date) {
        this.topic_date = topic_date;
    }

}
