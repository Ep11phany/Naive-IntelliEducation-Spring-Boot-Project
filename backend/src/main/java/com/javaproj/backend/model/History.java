package com.javaproj.backend.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String url;

    private Long time;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Long getTime() {
        return time;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
