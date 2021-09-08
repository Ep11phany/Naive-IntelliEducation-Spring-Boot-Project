package com.javaproj.backend.domain;

import com.javaproj.backend.model.History;
import com.javaproj.backend.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History, Integer> {
    History findByInstanceAndSubjectAndUser(String instance, String subject, User user);
    void deleteByInstanceAndSubjectAndUser(String instance, String subject, User user);
    List<History> findAllByUser(User user);
    void deleteAllByUser(User user);
}
