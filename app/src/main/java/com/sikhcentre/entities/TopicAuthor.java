package com.sikhcentre.entities;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
public class TopicAuthor implements Parcelable{
    @Id(autoincrement = true)
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

    protected TopicAuthor(Parcel in) {
        id = in.readLong();
        topicId = in.readLong();
        authorId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(topicId);
        parcel.writeLong(authorId);
    }

    public static final Creator<TopicAuthor> CREATOR = new Creator<TopicAuthor>() {
        @Override
        public TopicAuthor createFromParcel(Parcel in) {
            return new TopicAuthor(in);
        }

        @Override
        public TopicAuthor[] newArray(int size) {
            return new TopicAuthor[size];
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
    public Long getAuthorId() {
        return this.authorId;
    }
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

}
