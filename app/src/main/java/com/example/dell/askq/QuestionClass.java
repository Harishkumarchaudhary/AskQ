package com.example.dell.askq;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Dell on 14-06-2019.
 */
public class QuestionClass implements Serializable{
    @PropertyName("asked by")
    public String uid;
    @PropertyName("question text")
    public String question_text;
    private String qid;

    public QuestionClass() {
    }

    public QuestionClass(  String qid,String question_text,String uid) {
        this.question_text = question_text;
        this.qid = qid;
        this.uid=uid;
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

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

}
