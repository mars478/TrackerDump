package com.mars.trackerdump.entity;

public class Description {

    long desc_id;
    long topic_id;
    String desc_url;
    String desc_text;

    public Description() {
    }

    public Description(long desc_id, long topic_id, String desc_url, String desc_text) {
        this.desc_id = desc_id;
        this.topic_id = topic_id;
        this.desc_url = desc_url;
        this.desc_text = desc_text;
    }

    public long getDesc_id() {
        return desc_id;
    }

    public void setDesc_id(long desc_id) {
        this.desc_id = desc_id;
    }

    public long getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(long topic_id) {
        this.topic_id = topic_id;
    }

    public String getDesc_url() {
        return desc_url;
    }

    public void setDesc_url(String desc_url) {
        this.desc_url = desc_url;
    }

    public String getDesc_text() {
        return desc_text;
    }

    public void setDesc_text(String desc_text) {
        this.desc_text = desc_text;
    }

}
