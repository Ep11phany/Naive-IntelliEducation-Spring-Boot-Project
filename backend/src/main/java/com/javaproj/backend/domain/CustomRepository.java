package com.javaproj.backend.domain;

import com.javaproj.backend.model.User;

import java.util.List;

public interface CustomRepository {
    List<User> findByName(String name);
}
