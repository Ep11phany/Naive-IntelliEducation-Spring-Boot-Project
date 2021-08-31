package com.javaproj.backend.domain;

import com.javaproj.backend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByName(String name);
    List<User> findAllByName(String name);
    User findByNameAndPassword(String name, String password);
    void deleteByName(String name);
}