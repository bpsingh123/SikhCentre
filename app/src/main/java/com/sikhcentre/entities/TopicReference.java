package com.sikhcentre.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
public class TopicReference {
    @Id(autoincrement = true)
    private Long id;
    private Long topicId;
    private Long referenceId;
    @Generated(hash = 1210923516)
    public TopicReference(Long id, Long topicId, Long referenceId) {
        this.id = id;
        this.topicId = topicId;
        this.referenceId = referenceId;
    }
    @Generated(hash = 1068388684)
    public TopicReference() {
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
    public Long getReferenceId() {
        return this.referenceId;
    }
    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }
}
