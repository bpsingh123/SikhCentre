package com.sikhcentre.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
public class TopicTag implements Parcelable{
    @Id(autoincrement = true)
    private Long id;
    private Long topicId;
    private Long tagId;
    private Double weight;

    @ToOne(joinProperty = "topicId")
    private Topic topic;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 662370100)
    private transient TopicTagDao myDao;
    @Generated(hash = 2135967171)
    private transient Long topic__resolvedKey;

    @Generated(hash = 694071312)
    public TopicTag(Long id, Long topicId, Long tagId, Double weight) {
        this.id = id;
        this.topicId = topicId;
        this.tagId = tagId;
        this.weight = weight;
    }

    @Generated(hash = 588083404)
    public TopicTag() {
    }

    protected TopicTag(Parcel in) {
        id = in.readLong();
        topicId = in.readLong();
        tagId = in.readLong();
        weight = in.readDouble();
    }

    public static final Creator<TopicTag> CREATOR = new Creator<TopicTag>() {
        @Override
        public TopicTag createFromParcel(Parcel in) {
            return new TopicTag(in);
        }

        @Override
        public TopicTag[] newArray(int size) {
            return new TopicTag[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(topicId);
        parcel.writeLong(tagId);
        parcel.writeDouble(weight);
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

    public Double getWeight() {
        return this.weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 150653431)
    public Topic getTopic() {
        Long __key = this.topicId;
        if (topic__resolvedKey == null || !topic__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TopicDao targetDao = daoSession.getTopicDao();
            Topic topicNew = targetDao.load(__key);
            synchronized (this) {
                topic = topicNew;
                topic__resolvedKey = __key;
            }
        }
        return topic;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2139073858)
    public void setTopic(Topic topic) {
        synchronized (this) {
            this.topic = topic;
            topicId = topic == null ? null : topic.getId();
            topic__resolvedKey = topicId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2075354250)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTopicTagDao() : null;
    }

}
