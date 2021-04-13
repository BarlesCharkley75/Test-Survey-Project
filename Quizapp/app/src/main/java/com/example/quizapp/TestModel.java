package com.example.quizapp;

public class TestModel {
    private String id;
    private String name;
    private String num_of_questions;


    public TestModel(String id, String name, String num_of_questions) {
        this.id = id;
        this.name = name;
        this.num_of_questions = num_of_questions;
    }

    public String getNum_of_questions() {
        return num_of_questions;
    }

    public void setNum_of_questions(String num_of_questions) {
        this.num_of_questions = num_of_questions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
