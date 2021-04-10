package com.example.quizapp;

public class RankingQuestion {

    private String question;

    private String option1, option2, option3, option4;

    private String user_input1, user_input2, user_input3, user_input4;

    private String ans_1, ans_2, ans_3, ans_4;


    public RankingQuestion(String question, String option1, String option2, String option3, String option4,
                           String user_input1, String user_input2, String user_input3, String user_input4,
                           String ans_1, String ans_2, String ans_3, String ans_4) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.user_input1 = user_input1;
        this.user_input2 = user_input2;
        this.user_input3 = user_input3;
        this.user_input4 = user_input4;
        this.ans_1 = ans_1;
        this.ans_2 = ans_2;
        this.ans_3 = ans_3;
        this.ans_4 = ans_4;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
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

    public String getAns_1() {
        return ans_1;
    }

    public void setAns_1(String ans_1) {
        this.ans_1 = ans_1;
    }

    public String getAns_2() {
        return ans_2;
    }

    public void setAns_2(String ans_2) {
        this.ans_2 = ans_2;
    }

    public String getAns_3() {
        return ans_3;
    }

    public void setAns_3(String ans_3) {
        this.ans_3 = ans_3;
    }

    public String getAns_4() {
        return ans_4;
    }

    public void setAns_4(String ans_4) {
        this.ans_4 = ans_4;
    }
}
