package com.sikhcentre.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
public class Reference {
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
