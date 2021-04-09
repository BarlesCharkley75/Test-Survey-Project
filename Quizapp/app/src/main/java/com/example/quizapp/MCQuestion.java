package com.example.quizapp;

public class MCQuestion {

    String question;

    String option1;
    String option2;
    String option3;
    String option4;

    int CorrectAnswer;
    int SelectedAnswer;

    public MCQuestion(String question, String option1, String option2, String option3, String option4, int correctAnswer, int SelectedAnswer) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        CorrectAnswer = correctAnswer;
        this.SelectedAnswer = SelectedAnswer;
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

    public int getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public int getSelectedAnswer(){
        return SelectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer){
        SelectedAnswer = selectedAnswer;
    }
}
