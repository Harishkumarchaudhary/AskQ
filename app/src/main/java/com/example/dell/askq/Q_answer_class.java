package com.example.dell.askq;

import android.util.Log;

import com.google.firebase.database.PropertyName;

/**
 * Created by Dell on 21-07-2019.
 */

public class Q_answer_class {
    private String answer;
    private String the_answerer;

    public Q_answer_class(){

    }

    public Q_answer_class(String answer, String the_answerer) {
        this.answer = answer;
        this.the_answerer = the_answerer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getThe_answerer() {
        return the_answerer;
    }

    public void setThe_answerer(String the_answerer) {
        this.the_answerer = the_answerer;
    }
}
