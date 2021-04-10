package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MCQuestionActivity2 extends AppCompatActivity implements View.OnClickListener{

    private TextView question;
    private TextView q_count;

    private Button option1, option2, option3, option4, GoNext, GoPrev;

    private ArrayList<MCQuestion> questionList;

    private int current_question;

    private int info = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m_c_question2);


        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        question = findViewById(R.id.question);
        q_count = findViewById(R.id.question_num);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);


        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);

        getQuestionList();
    }

    private void getQuestionList(){
        questionList = new ArrayList<>();
        questionList.add(new MCQuestion("Who are you?","A","B","C","D",2, 0));
        questionList.add(new MCQuestion("Best ice cream brand?","A2","B2","C2","D2",2, 0));



//        setQuestion(current_question);
        if(info == 1){
            current_question = 0;
            setQuestion(current_question);
        }
        else if (info == 2){
            current_question = questionList.size()-1;
            setQuestion(current_question);

        }

    }

    private void setQuestion(int i){
        question.setText(questionList.get(i).getQuestion());

        option1.setText(questionList.get(i).getOption1());
        option2.setText(questionList.get(i).getOption2());
        option3.setText(questionList.get(i).getOption3());
        option4.setText(questionList.get(i).getOption4());

        q_count.setText(String.valueOf(i + 1) + "/" + String.valueOf(questionList.size()));

        option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));

        if(questionList.get(current_question).getSelectedAnswer() ==1){
            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if(questionList.get(current_question).getSelectedAnswer() ==2){
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if(questionList.get(current_question).getSelectedAnswer() ==3){
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }
        else if (questionList.get(current_question).getSelectedAnswer() ==4){
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }


    }

    @Override
    public void onClick(View v) {



        if (v.getId() == R.id.option1){
            questionList.get(current_question).setSelectedAnswer(1);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if (v.getId() == R.id.option2){
            questionList.get(current_question).setSelectedAnswer(2);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if(v.getId() == R.id.option3){
            questionList.get(current_question).setSelectedAnswer(3);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        }
        else if(v.getId() == R.id.option4){
            questionList.get(current_question).setSelectedAnswer(4);

            option1.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
        }


        if(v.getId() == R.id.GoNext){
            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){
            changeQuestionBackward();
        }


    }



    private void changeQuestionForward(){
        if(current_question < questionList.size() - 1){

            current_question ++;
            setQuestion(current_question);

        }

        else{
            // go to free response activity
            Intent intent = new Intent(MCQuestionActivity2.this, FRQuestionActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }
    }

    private void changeQuestionBackward(){
        if(current_question > 0){

            current_question --;

            setQuestion(current_question);



        }
    }
}