package com.example.dell.askq;

import java.io.Serializable;

/**
 * Created by Dell on 13-06-2019.
 */

public class AnswerClass implements Serializable {
    private String question;
    private String user;
    private String qid;
    private String uid;

    public AnswerClass(String question, String user,String qid,String uid) {
        this.question = question;
        this.user = user;
        this.uid=uid;
        this.qid=qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
