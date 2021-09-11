package com.javaproj.backend.model;

import javax.persistence.*;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String instance;

    private String subject;

    @ManyToOne
    private User user;

    public Integer getId() {
        return id;
    }

    public String getInstance() {
        return instance;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getSubject() { return subject; }

    public void setSubject(String subject) { this.subject = subject; }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
