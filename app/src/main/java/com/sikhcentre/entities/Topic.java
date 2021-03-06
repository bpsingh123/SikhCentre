package com.sikhcentre.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

import lombok.Getter;
import lombok.ToString;


/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
@ToString
public class Topic implements Parcelable{
    @Id
    private Long id;
    private String title;
    private String url;
    private String info;
    private String content;

    @Convert(converter = TopicTypeConverter.class, columnType = Integer.class)
    private TopicType topicType;

    @ToMany
    @JoinEntity(
            entity = TopicAuthor.class,
            sourceProperty = "topicId",
            targetProperty = "authorId"
    )
    private List<Author> authors;

    @ToMany
    @JoinEntity(
            entity = TopicReference.class,
            sourceProperty = "topicId",
            targetProperty = "referenceId"
    )
    private List<Reference> references;

    @ToMany(referencedJoinProperty = "topicId")
    @OrderBy("weight DESC")
    private List<TopicTag> topicTags;

    @ToMany(referencedJoinProperty = "topicId")
    @OrderBy("weight DESC")
    private List<RelatedTopic> relatedTopics;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 694021448)
    private transient TopicDao myDao;

    @Generated(hash = 461814294)
    public Topic(Long id, String title, String url, String info, String content, TopicType topicType) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.info = info;
        this.content = content;
        this.topicType = topicType;
    }

    @Generated(hash = 849012203)
    public Topic() {
    }

    protected Topic(Parcel in) {
        id = in.readLong();
        topicType = TopicType.valueOf(in.readString());
        title = in.readString();
        url = in.readString();
        info = in.readString();
        content = in.readString();
        authors = in.createTypedArrayList(Author.CREATOR);
        references = in.createTypedArrayList(Reference.CREATOR);
        topicTags = in.createTypedArrayList(TopicTag.CREATOR);
        relatedTopics = in.createTypedArrayList(RelatedTopic.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(topicType.name());
        parcel.writeString(title);
        parcel.writeString(url);
        parcel.writeString(info);
        parcel.writeString(content);
        parcel.writeTypedList(authors);
        parcel.writeTypedList(references);
        parcel.writeTypedList(topicTags);
        parcel.writeTypedList(relatedTopics);
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
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

    public TopicType getTopicType() {
        return this.topicType;
    }

    public void setTopicType(TopicType topicType) {
        this.topicType = topicType;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1186586908)
    public List<Author> getAuthors() {
        if (authors == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AuthorDao targetDao = daoSession.getAuthorDao();
            List<Author> authorsNew = targetDao._queryTopic_Authors(id);
            synchronized (this) {
                if (authors == null) {
                    authors = authorsNew;
                }
            }
        }
        return authors;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 405652703)
    public synchronized void resetAuthors() {
        authors = null;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Getter
    public enum TopicType {
        @SerializedName("0")
        UNKNOWN(0),
        @SerializedName("1")
        TEXT(1),
        @SerializedName("2")
        IMAGE(2),
        @SerializedName("3")
        AUDIO(3),
        @SerializedName("4")
        VIDEO(4),
        @SerializedName("5")
        PDF(5);
        final int id;

        TopicType(int id) {
            this.id = id;
        }
    }

    public static class TopicTypeConverter implements PropertyConverter<TopicType, Integer> {

        @Override
        public TopicType convertToEntityProperty(Integer databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            for (TopicType topicType : TopicType.values()) {
                if (topicType.id == databaseValue) {
                    return topicType;
                }
            }
            return TopicType.UNKNOWN;
        }

        @Override
        public Integer convertToDatabaseValue(TopicType entityProperty) {
            return entityProperty == null ? null : entityProperty.id;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Topic topic = (Topic) o;

        return id.equals(topic.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getAuthorString() {
        List<Author> authors = getAuthors();
        if (authors != null) {
            StringBuilder authorStr = new StringBuilder();
            for (Author author : authors) {
                authorStr.append(author.getName());
                authorStr.append(", ");
            }
            return authorStr.substring(0, authorStr.length() - 2);
        }
        return "";
    }

//    public String getTagString() {
//        List<Tag> Tags = getTags();
//        if (Tags != null) {
//            StringBuilder tagStr = new StringBuilder();
//            for (Tag tag : Tags) {
//                tagStr.append(tag.getName());
//                tagStr.append(", ");
//            }
//            return tagStr.substring(0, tagStr.length() - 2);
//        }
//        return "";
//    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1296712280)
    public List<TopicTag> getTopicTags() {
        if (topicTags == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TopicTagDao targetDao = daoSession.getTopicTagDao();
            List<TopicTag> topicTagsNew = targetDao._queryTopic_TopicTags(id);
            synchronized (this) {
                if (topicTags == null) {
                    topicTags = topicTagsNew;
                }
            }
        }
        return topicTags;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 7070170)
    public synchronized void resetTopicTags() {
        topicTags = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1152752378)
    public List<RelatedTopic> getRelatedTopics() {
        if (relatedTopics == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RelatedTopicDao targetDao = daoSession.getRelatedTopicDao();
            List<RelatedTopic> relatedTopicsNew = targetDao._queryTopic_RelatedTopics(id);
            synchronized (this) {
                if (relatedTopics == null) {
                    relatedTopics = relatedTopicsNew;
                }
            }
        }
        return relatedTopics;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 588647111)
    public synchronized void resetRelatedTopics() {
        relatedTopics = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1111704743)
    public List<Reference> getReferences() {
        if (references == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReferenceDao targetDao = daoSession.getReferenceDao();
            List<Reference> referencesNew = targetDao._queryTopic_References(id);
            synchronized (this) {
                if (references == null) {
                    references = referencesNew;
                }
            }
        }
        return references;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 932734098)
    public synchronized void resetReferences() {
        references = null;
    }

    public String getInfo() {
        return this.info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1373867845)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTopicDao() : null;
    }


}
