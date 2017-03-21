package com.sikhcentre.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
public class TopicTag {
    @Id(autoincrement = true)
    private Long id;
    private Long topicId;
    private Long tagId;
    @Generated(hash = 497429178)
    public TopicTag(Long id, Long topicId, Long tagId) {
        this.id = id;
        this.topicId = topicId;
        this.tagId = tagId;
    }
    @Generated(hash = 588083404)
    public TopicTag() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getTopicId() {
        return this.topicId;
    }
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
    public Long getTagId() {
        return this.tagId;
    }
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
