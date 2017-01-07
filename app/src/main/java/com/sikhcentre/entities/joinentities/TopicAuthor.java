package com.sikhcentre.entities.joinentities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
public class TopicAuthor {
    @Id
    private Long id;
    private Long topicId;
    private Long authorId;
    @Generated(hash = 1257598652)
    public TopicAuthor(Long id, Long topicId, Long authorId) {
        this.id = id;
        this.topicId = topicId;
        this.authorId = authorId;
    }
    @Generated(hash = 1023691281)
    public TopicAuthor() {
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
    public Long getAuthorId() {
        return this.authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

}
