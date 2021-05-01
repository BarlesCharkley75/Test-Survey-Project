package com.example.quizapp;

public class RankingAnswers {
    String user_input1,user_input2,user_input3,user_input4;


    public RankingAnswers(String user_input1, String user_input2, String user_input3, String user_input4) {
        this.user_input1 = user_input1;
        this.user_input2 = user_input2;
        this.user_input3 = user_input3;
        this.user_input4 = user_input4;
    }

    public String getUser_input1() {
        return user_input1;
    }

    public void setUser_input1(String user_input1) {
        this.user_input1 = user_input1;
    }

    public String getUser_input2() {
        return user_input2;
    }

    public void setUser_input2(String user_input2) {
        this.user_input2 = user_input2;
    }

    public String getUser_input3() {
        return user_input3;
    }

    public void setUser_input3(String user_input3) {
        this.user_input3 = user_input3;
    }

    public String getUser_input4() {
        return user_input4;
    }

    public void setUser_input4(String user_input4) {
        this.user_input4 = user_input4;
    }
}
