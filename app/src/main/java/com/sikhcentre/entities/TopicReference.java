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
public class TopicReference implements Parcelable{
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

    protected TopicReference(Parcel in) {
        id = in.readLong();
        topicId = in.readLong();
        referenceId = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeLong(topicId);
        parcel.writeLong(referenceId);
    }

    public static final Creator<TopicReference> CREATOR = new Creator<TopicReference>() {
        @Override
        public TopicReference createFromParcel(Parcel in) {
            return new TopicReference(in);
        }

        @Override
        public TopicReference[] newArray(int size) {
            return new TopicReference[size];
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
    public Long getReferenceId() {
        return this.referenceId;
    }
    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

}
