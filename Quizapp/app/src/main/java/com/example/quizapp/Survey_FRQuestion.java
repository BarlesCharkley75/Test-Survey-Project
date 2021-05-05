package com.example.quizapp;

public class Survey_FRQuestion {

    private String question;

    private int word_limit;

    public Survey_FRQuestion(String question, int word_limit) {
        this.question = question;
        this.word_limit = word_limit;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getWord_limit() {
        return word_limit;
    }

    public void setWord_limit(int word_limit) {
        this.word_limit = word_limit;
    }
}
