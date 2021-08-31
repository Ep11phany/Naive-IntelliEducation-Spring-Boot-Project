package com.javaproj.backend.model;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String url;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
