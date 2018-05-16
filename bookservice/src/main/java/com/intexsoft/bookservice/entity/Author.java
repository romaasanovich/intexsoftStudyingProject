package com.intexsoft.bookservice.entity;


import com.intexsoft.bookservice.entity.aentity.AbstractEntity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "author")
public class Author extends AbstractEntity {
    @Column(name = "name", length = 45)
    private String name;
    @Column(name = "bio", length = 100)
    private String bio;
    @Column(name = "birthDay")
    @Temporal(value = TemporalType.DATE)
    private Date birthDay;

    public Author() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }
}
