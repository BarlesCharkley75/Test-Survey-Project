package com.example.quizapp;

public class FRQuestion {

    private String question;



    private int word_limit, points;


    public FRQuestion(String question, int word_limit, int points) {
        this.question = question;
        this.word_limit = word_limit;
        this.points = points;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getWord_limit() {
        return word_limit;
    }

    public void setWord_limit(int word_limit) {
        this.word_limit = word_limit;
    }
}
