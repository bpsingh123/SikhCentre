package com.sikhcentre.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

/**
 * Created by brinder.singh on 26/03/17.
 */
@Entity
public class RelatedTopic implements Parcelable{
    @Id(autoincrement = true)
    private Long id;
    private Long topicId;
    private Long relatedTopicId;
    private Double weight;

    @ToOne(joinProperty = "relatedTopicId")
    private Topic relatedTopic;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1912718555)
    private transient RelatedTopicDao myDao;
    @Generated(hash = 1660454596)
    private transient Long relatedTopic__resolvedKey;

    @Generated(hash = 146970062)
    public RelatedTopic(Long id, Long topicId, Long relatedTopicId, Double weight) {
        this.id = id;
        this.topicId = topicId;
        this.relatedTopicId = relatedTopicId;
        this.weight = weight;
    }
    @Generated(hash = 1341454251)
    public RelatedTopic() {
    }

    protected RelatedTopic(Parcel in) {
        id = in.readLong();
        topicId = in.readLong();
        relatedTopicId = in.readLong();
        weight = in.readDouble();
        relatedTopic = in.readParcelable(Topic.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(topicId);
        parcel.writeLong(relatedTopicId);
        parcel.writeDouble(weight);
        parcel.writeParcelable(relatedTopic, i);
    }

    public static final Creator<RelatedTopic> CREATOR = new Creator<RelatedTopic>() {
        @Override
        public RelatedTopic createFromParcel(Parcel in) {
            return new RelatedTopic(in);
        }

        @Override
        public RelatedTopic[] newArray(int size) {
            return new RelatedTopic[size];
        }
    };

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
    public Double getWeight() {
        return this.weight;
    }
    public void setWeight(Double weight) {
        this.weight = weight;
    }
    public Long getRelatedTopicId() {
        return this.relatedTopicId;
    }
    public void setRelatedTopicId(Long relatedTopicId) {
        this.relatedTopicId = relatedTopicId;
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
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2092850619)
    public Topic getRelatedTopic() {
        Long __key = this.relatedTopicId;
        if (relatedTopic__resolvedKey == null || !relatedTopic__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TopicDao targetDao = daoSession.getTopicDao();
            Topic relatedTopicNew = targetDao.load(__key);
            synchronized (this) {
                relatedTopic = relatedTopicNew;
                relatedTopic__resolvedKey = __key;
            }
        }
        return relatedTopic;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 208274377)
    public void setRelatedTopic(Topic relatedTopic) {
        synchronized (this) {
            this.relatedTopic = relatedTopic;
            relatedTopicId = relatedTopic == null ? null : relatedTopic.getId();
            relatedTopic__resolvedKey = relatedTopicId;
        }
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 377443740)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRelatedTopicDao() : null;
    }

}
