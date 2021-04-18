package com.example.quizapp;

public class FRQuestion {

    private String question;

    private String user_answer;

    private int word_limit;

    public FRQuestion(String question, String user_answer, int word_limit) {
        this.question = question;
        this.user_answer = user_answer;
        this.word_limit = word_limit;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUser_answer() {
        return user_answer;
    }

    public void setUser_answer(String user_answer) {
        this.user_answer = user_answer;
    }

    public int getWord_limit() {
        return word_limit;
    }

    public void setWord_limit(int word_limit) {
        this.word_limit = word_limit;
    }
}
