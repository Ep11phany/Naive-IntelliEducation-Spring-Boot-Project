package com.javaproj.backend.model;


import javax.persistence.*;

@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Long questionID;

    private String qBody;

    private String qAnswer;

    private Long addTime;

    private Integer redoTimes;

    private Integer redoCorrectTimes;

    @ManyToOne
    private User user;

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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() { return user; }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Integer getRedoTimes() {
        return redoTimes;
    }

    public void setRedoTimes(Integer redoTimes) {
        this.redoTimes = redoTimes;
    }

    public Integer getRedoCorrectTimes() {
        return redoCorrectTimes;
    }

    public void setRedoCorrectTimes(Integer redoCorrectTimes) {
        this.redoCorrectTimes = redoCorrectTimes;
    }
}
