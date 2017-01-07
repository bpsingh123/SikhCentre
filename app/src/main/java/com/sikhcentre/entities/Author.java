package com.sikhcentre.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by brinder.singh on 31/12/16.
 */
@Entity
public class Author {
    @Id
    private Long id;
    private String name;

    @Generated(hash = 1310344436)
    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 64241762)
    public Author() {
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
}
