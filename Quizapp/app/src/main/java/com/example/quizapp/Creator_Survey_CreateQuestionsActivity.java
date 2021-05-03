package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.quizapp.UserProfileActivity.surveyList;
import static com.example.quizapp.UserProfileActivity.testList;

public class Creator_Survey_CreateQuestionsActivity extends AppCompatActivity {

    private Button CreateMC, CreateFR, CreateMatching, CreateRanking;

    public static int CurrentNumOfSurvey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__survey__create_questions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CreateMC = findViewById(R.id.CreateMCQuestion);
        CreateFR = findViewById(R.id.CreateFRQuestion);
        CreateMatching = findViewById(R.id.CreateMatchingQuestion);
        CreateRanking = findViewById(R.id.CreateRankingQuestion);


        Intent intent = getIntent();
        int temp = intent.getIntExtra("NAME",-5);

        if(temp == -10) {//this is a new test
            CurrentNumOfSurvey = surveyList.size();

//
        }
        else if(temp != -5){
            CurrentNumOfSurvey = temp;
        }

        getSupportActionBar().setTitle("survey"+String.valueOf(CurrentNumOfSurvey + 1)+": " + "Create questions");

        CreateMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_Survey_CreateQuestionsActivity.this, Creator_Survey_MCQuestionsListActivity.class);
                startActivity(intent);
            }
        });

        CreateFR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_Survey_CreateQuestionsActivity.this, Creator_Survey_FRQuestionsListActivity.class);
                startActivity(intent);
            }
        });

        CreateMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_Survey_CreateQuestionsActivity.this, Creator_Survey_MatchingQuestionsListActivity.class);
                startActivity(intent);
            }
        });

        CreateRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_Survey_CreateQuestionsActivity.this, Creator_Survey_RankingQuestionsListActivity.class);
                startActivity(intent);
            }
        });
    }
}