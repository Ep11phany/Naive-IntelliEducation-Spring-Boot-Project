package com.javaproj.backend.model;

import javax.persistence.*;

@Entity
public class QuestionCollect {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Long questionID;

    private String qBody;

    private String qAnswer;

    public Integer getId() { return id; }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getQuestionID() {
        return questionID;
    }

    public void setQuestionID(Long questionID) {
        this.questionID = questionID;
    }

    public String getqAnswer() {
        return qAnswer;
    }

    public void setqAnswer(String qAnswer) {
        this.qAnswer = qAnswer;
    }

    public String getqBody() {
        return qBody;
    }

    public void setqBody(String qBody) {
        this.qBody = qBody;
    }

}
