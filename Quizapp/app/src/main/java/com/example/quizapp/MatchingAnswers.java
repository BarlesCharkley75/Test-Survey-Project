package com.example.quizapp;

public class MatchingAnswers {


    int L1_selected,L2_selected,L3_selected,L4_selected;

    public MatchingAnswers(int l1_selected, int l2_selected, int l3_selected, int l4_selected) {
        L1_selected = l1_selected;
        L2_selected = l2_selected;
        L3_selected = l3_selected;
        L4_selected = l4_selected;
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
