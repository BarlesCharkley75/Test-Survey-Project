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

public class MatchingQurestionActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView question, q_count;

    private Button option1_L, option2_L, option3_L, option4_L;
    private Button option1_R, option2_R, option3_R, option4_R;
    private Button GoNext, GoPrev, clear;

    private ArrayList<MatchingQuestion> questionList;

    private int current_question;
    private int info = 0;

    private int pair_order = 0;
    private int correct_match;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_qurestion);

        Intent intent = getIntent();
        info = intent.getIntExtra("info",0);

        question = findViewById(R.id.question);
        q_count = findViewById(R.id.question_num);

        option1_L = findViewById(R.id.option_L1);
        option2_L = findViewById(R.id.option_L2);
        option3_L = findViewById(R.id.option_L3);
        option4_L = findViewById(R.id.option_L4);

        option1_R = findViewById(R.id.option_R1);
        option2_R = findViewById(R.id.option_R2);
        option3_R = findViewById(R.id.option_R3);
        option4_R = findViewById(R.id.option_R4);

        GoNext = findViewById(R.id.GoNext);
        GoPrev = findViewById(R.id.GoPrev);
        clear = findViewById(R.id.Clear);


        option1_L.setOnClickListener(this);
        option2_L.setOnClickListener(this);
        option3_L.setOnClickListener(this);
        option4_L.setOnClickListener(this);

        option1_R.setOnClickListener(this);
        option2_R.setOnClickListener(this);
        option3_R.setOnClickListener(this);
        option4_R.setOnClickListener(this);

        GoNext.setOnClickListener(this);
        GoPrev.setOnClickListener(this);
        clear.setOnClickListener(this);



        getQuestionList();

    }

    private void getQuestionList(){
        questionList = new ArrayList<MatchingQuestion>();

        questionList.add(new MatchingQuestion("match","A","B","C","D","C","D","A","B",3,4,
                1,2,0,0,0,0));

        questionList.add(new MatchingQuestion("match","meat","vegetable","fruit","fish","apple","onions","salmon","beef",4,2,
                1,3,0,0,0,0)); //random order example


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
        q_count.setText(String.valueOf(i + 1) + "/" + String.valueOf(questionList.size()));

        option1_L.setText(questionList.get(i).getL1());
        option2_L.setText(questionList.get(i).getL2());
        option3_L.setText(questionList.get(i).getL3());
        option4_L.setText(questionList.get(i).getL4());

        option1_R.setText(questionList.get(i).getR1());
        option2_R.setText(questionList.get(i).getR2());
        option3_R.setText(questionList.get(i).getR3());
        option4_R.setText(questionList.get(i).getR4());

        option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));

        option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
        option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));

        if(questionList.get(current_question).getL1_selected() != 0){
            option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            if(questionList.get(current_question).getL1_selected() == 1){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if(questionList.get(current_question).getL1_selected() == 2){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if(questionList.get(current_question).getL1_selected() == 3){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
            else if(questionList.get(current_question).getL1_selected() == 4){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            }
        }

        if(questionList.get(current_question).getL2_selected() != 0){
            option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            if(questionList.get(current_question).getL2_selected() == 1){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            }
            else if(questionList.get(current_question).getL2_selected() == 2){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            }
            else if(questionList.get(current_question).getL2_selected() == 3){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            }
            else if(questionList.get(current_question).getL2_selected() == 4){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            }
        }

        if(questionList.get(current_question).getL3_selected() != 0){
            option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            if(questionList.get(current_question).getL3_selected() == 1){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            }
            else if(questionList.get(current_question).getL3_selected() == 2){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            }
            else if(questionList.get(current_question).getL3_selected() == 3){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            }
            else if(questionList.get(current_question).getL3_selected() == 4){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            }
        }

        if(questionList.get(current_question).getL4_selected() != 0){
            option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            if(questionList.get(current_question).getL4_selected() == 1){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            }
            else if(questionList.get(current_question).getL4_selected() == 2){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            }
            else if(questionList.get(current_question).getL4_selected() == 3){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            }
            else if(questionList.get(current_question).getL4_selected() == 4){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            }
        }

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.option_L1){
            option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
            pair_order = 1;
        }

        if(v.getId() == R.id.option_L2){
            option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
            pair_order = 2;
        }

        if(v.getId() == R.id.option_L3){
            option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
            pair_order = 3;
        }

        if (v.getId() == R.id.option_L4){
            option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
            pair_order = 4;
        }


        if(pair_order == 1){
            if(v.getId() == R.id.option_R1 && questionList.get(current_question).getL1_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                questionList.get(current_question).setL1_selected(1);
            }
            if(v.getId() == R.id.option_R2 && questionList.get(current_question).getL1_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                questionList.get(current_question).setL1_selected(2);
            }
            if(v.getId() == R.id.option_R3 && questionList.get(current_question).getL1_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                questionList.get(current_question).setL1_selected(3);
            }

            if (v.getId() == R.id.option_R4 && questionList.get(current_question).getL1_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#40ff40")));
                questionList.get(current_question).setL1_selected(4);
            }

        }

        if(pair_order == 2){
            if(v.getId() == R.id.option_R1 && questionList.get(current_question).getL2_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                questionList.get(current_question).setL2_selected(1);
            }
            if(v.getId() == R.id.option_R2 && questionList.get(current_question).getL2_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                questionList.get(current_question).setL2_selected(2);
            }
            if(v.getId() == R.id.option_R3 && questionList.get(current_question).getL2_selected() == 0 ){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                questionList.get(current_question).setL2_selected(3);
            }

            if (v.getId() == R.id.option_R4 && questionList.get(current_question).getL2_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffdbe4")));
                questionList.get(current_question).setL2_selected(4);

            }

        }

        if(pair_order == 3){
            if(v.getId() == R.id.option_R1 && questionList.get(current_question).getL3_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                questionList.get(current_question).setL3_selected(1);
            }
            if(v.getId() == R.id.option_R2 && questionList.get(current_question).getL3_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                questionList.get(current_question).setL3_selected(2);
            }
            if(v.getId() == R.id.option_R3 && questionList.get(current_question).getL3_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                questionList.get(current_question).setL3_selected(3);
            }

            if (v.getId() == R.id.option_R4 && questionList.get(current_question).getL3_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91e5ff")));
                questionList.get(current_question).setL3_selected(4);
            }

        }

        if(pair_order == 4){
            if(v.getId() == R.id.option_R1 && questionList.get(current_question).getL4_selected() == 0){
                option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                questionList.get(current_question).setL4_selected(1);
            }
            if(v.getId() == R.id.option_R2 && questionList.get(current_question).getL4_selected() == 0){
                option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                questionList.get(current_question).setL4_selected(2);
            }
            if(v.getId() == R.id.option_R3 && questionList.get(current_question).getL4_selected() == 0){
                option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                questionList.get(current_question).setL4_selected(3);
            }

            if (v.getId() == R.id.option_R4 && questionList.get(current_question).getL4_selected() == 0){
                option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffff97")));
                questionList.get(current_question).setL4_selected(4);
            }

        }

        if(v.getId() == R.id.Clear){
            questionList.get(current_question).setL1_selected(0);
            questionList.get(current_question).setL2_selected(0);
            questionList.get(current_question).setL3_selected(0);
            questionList.get(current_question).setL4_selected(0);

            option1_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4_L.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));

            option1_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option2_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option3_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));
            option4_R.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#e9e9e9")));


        }

        if(v.getId() == R.id.GoNext){
            changeQuestionForward();
        }
        else if (v.getId() == R.id.GoPrev){
            changeQuestionBackward();
        }
    }

    private void changeQuestionForward(){
        if(current_question >= questionList.size()-1){
            Intent intent = new Intent (MatchingQurestionActivity.this, RankingQuestionActivity.class);
            intent.putExtra("info",1);
            startActivity(intent);
        }

        else{
            current_question ++;

            setQuestion(current_question);


        }
    }

    private void changeQuestionBackward(){

        if(current_question==0){
            Intent intent = new Intent(MatchingQurestionActivity.this, FRQuestionActivity.class);
            intent.putExtra("info",2);
            startActivity(intent);
        }
        else{
            current_question --;

            setQuestion(current_question);
        }
    }
}