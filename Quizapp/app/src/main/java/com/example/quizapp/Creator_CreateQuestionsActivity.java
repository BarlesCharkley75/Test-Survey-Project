package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Creator_CreateQuestionsActivity extends AppCompatActivity {

    private Button CreateMC, CreateFR, CreateMatching, CreateRanking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__create_questions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create questions");


        CreateMC = findViewById(R.id.CreateMCQuestion);
        CreateFR = findViewById(R.id.CreateFRQuestion);
        CreateMatching = findViewById(R.id.CreateMatchingQuestion);
        CreateRanking = findViewById(R.id.CreateRankingQuestion);


        CreateMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_CreateMCQuestionActivity.class);
                startActivity(intent);
            }
        });

        CreateFR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_CreateFRQuestionActivity.class);
                startActivity(intent);
            }
        });

        CreateMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_CreateMatchingQuestionActivity.class);
                startActivity(intent);
            }
        });

        CreateRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_CreateRankingQuestionActivity.class);
                startActivity(intent);
            }
        });

    }


}