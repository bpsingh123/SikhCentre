package com.sikhcentre.entities;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.List;

import lombok.ToString;

/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
@ToString
public class Topic {
    @Id
    private Long id;
    private String title;
    private String url;

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
            entity = TopicTag.class,
            sourceProperty = "topicId",
            targetProperty = "tagId"
    )
    private List<Tag> tags;


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

    @Generated(hash = 788151660)
    public Topic(Long id, String title, String url, TopicType topicType) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.topicType = topicType;
    }

    @Generated(hash = 849012203)
    public Topic() {
    }


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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 90582728)
    public List<Tag> getTags() {
        if (tags == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TagDao targetDao = daoSession.getTagDao();
            List<Tag> tagsNew = targetDao._queryTopic_Tags(id);
            synchronized (this) {
                if (tags == null) {
                    tags = tagsNew;
                }
            }
        }
        return tags;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 404234)
    public synchronized void resetTags() {
        tags = null;
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
        VIDEO(4);
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
            return authorStr.substring(0, authorStr.length()-2);
        }
        return "";
    }

    public String getTagString() {
        List<Tag> Tags = getTags();
        if (Tags != null) {
            StringBuilder tagStr = new StringBuilder();
            for (Tag tag : Tags) {
                tagStr.append(tag.getName());
                tagStr.append(", ");
            }
            return tagStr.substring(0, tagStr.length()-2);
        }
        return "";
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1373867845)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTopicDao() : null;
    }
}
