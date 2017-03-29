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
public class Reference implements Parcelable{
    @Id
    private Long id;
    private String name;
    private String type;
    @Generated(hash = 1027087912)
    public Reference(Long id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    @Generated(hash = 53272157)
    public Reference() {
    }

    protected Reference(Parcel in) {
        id = in.readLong();
        name = in.readString();
        type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(type);
    }

    public static final Creator<Reference> CREATOR = new Creator<Reference>() {
        @Override
        public Reference createFromParcel(Parcel in) {
            return new Reference(in);
        }

        @Override
        public Reference[] newArray(int size) {
            return new Reference[size];
        }
    };

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return this.type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
