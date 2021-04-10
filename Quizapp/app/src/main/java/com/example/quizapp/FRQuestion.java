package com.example.quizapp;

public class FRQuestion {

    private String question;

    private String user_answer;

    public FRQuestion(String question, String user_answer) {
        this.question = question;
        this.user_answer = user_answer;
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
}
