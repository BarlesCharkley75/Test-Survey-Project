package com.example.quizapp;

public class MatchingQuestion {

    String question;

    String L1,L2,L3,L4;
    String R1,R2,R3,R4;

    int L1_ANS, L2_ANS, L3_ANS, L4_ANS;
    int L1_selected, L2_selected, L3_selected, L4_selected; //it is the ids that are selected, not option




    public MatchingQuestion(String question, String l1, String l2, String l3, String l4, String r1, String r2, String r3, String r4,
                            int l1_ANS, int l2_ANS, int l3_ANS, int l4_ANS,
                            int l1_selected, int l2_selected, int l3_selected, int l4_selected
                            ) {
        this.question = question;
        L1 = l1;
        L2 = l2;
        L3 = l3;
        L4 = l4;
        R1 = r1;
        R2 = r2;
        R3 = r3;
        R4 = r4;
        L1_ANS = l1_ANS;
        L2_ANS = l2_ANS;
        L3_ANS = l3_ANS;
        L4_ANS = l4_ANS;
        L1_selected = l1_selected;
        L2_selected = l2_selected;
        L3_selected = l3_selected;
        L4_selected = l4_selected;

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getL1() {
        return L1;
    }

    public void setL1(String l1) {
        L1 = l1;
    }

    public String getL2() {
        return L2;
    }

    public void setL2(String l2) {
        L2 = l2;
    }

    public String getL3() {
        return L3;
    }

    public void setL3(String l3) {
        L3 = l3;
    }

    public String getL4() {
        return L4;
    }

    public void setL4(String l4) {
        L4 = l4;
    }

    public String getR1() {
        return R1;
    }

    public void setR1(String r1) {
        R1 = r1;
    }

    public String getR2() {
        return R2;
    }

    public void setR2(String r2) {
        R2 = r2;
    }

    public String getR3() {
        return R3;
    }

    public void setR3(String r3) {
        R3 = r3;
    }

    public String getR4() {
        return R4;
    }

    public void setR4(String r4) {
        R4 = r4;
    }

    public int getL1_ANS() {
        return L1_ANS;
    }

    public void setL1_ANS(int l1_ANS) {
        L1_ANS = l1_ANS;
    }

    public int getL2_ANS() {
        return L2_ANS;
    }

    public void setL2_ANS(int l2_ANS) {
        L2_ANS = l2_ANS;
    }

    public int getL3_ANS() {
        return L3_ANS;
    }

    public void setL3_ANS(int l3_ANS) {
        L3_ANS = l3_ANS;
    }

    public int getL4_ANS() {
        return L4_ANS;
    }

    public void setL4_ANS(int l4_ANS) {
        L4_ANS = l4_ANS;
    }

    public int getL1_selected() {
        return L1_selected;
    }

    public void setL1_selected(int l1_selected) {
        L1_selected = l1_selected;
    }

    public int getL2_selected() {
        return L2_selected;
    }

    public void setL2_selected(int l2_selected) {
        L2_selected = l2_selected;
    }

    public int getL3_selected() {
        return L3_selected;
    }

    public void setL3_selected(int l3_selected) {
        L3_selected = l3_selected;
    }

    public int getL4_selected() {
        return L4_selected;
    }

    public void setL4_selected(int l4_selected) {
        L4_selected = l4_selected;
    }

}
