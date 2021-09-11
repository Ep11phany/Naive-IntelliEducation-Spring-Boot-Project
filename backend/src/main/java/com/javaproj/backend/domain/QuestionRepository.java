package com.javaproj.backend.domain;

import com.javaproj.backend.model.Question;
import com.javaproj.backend.model.User;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Integer> {
    Question findByQuestionIDAndUser(Long questionID, User user);
    void deleteByQuestionIDAndUser(Long questionID, User user);
}
