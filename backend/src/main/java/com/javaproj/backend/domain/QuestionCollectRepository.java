package com.javaproj.backend.domain;

import com.javaproj.backend.model.QuestionCollect;
import org.springframework.data.repository.CrudRepository;

public interface QuestionCollectRepository extends CrudRepository<QuestionCollect, Integer> {
    QuestionCollect findByQuestionID(Long questionID);
}
