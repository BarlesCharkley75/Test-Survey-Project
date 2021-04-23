package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.quizapp.MainActivity.testList;

public class Creator_CreateQuestionsActivity extends AppCompatActivity {

    private Button CreateMC, CreateFR, CreateMatching, CreateRanking;

    public static int CurrentNumOfTest;

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

        Intent intent = getIntent();
        int temp = intent.getIntExtra("NAME",0);


        if(temp == -10){//this is a new test
            CurrentNumOfTest = testList.size() + 1;
        }
        else if(temp != 0){
            CurrentNumOfTest = temp;
        }



        CreateMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_MCQuestionsListActivity.class);
                intent.putExtra("NAME",CurrentNumOfTest);
                startActivity(intent);
            }
        });

        CreateFR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_FRQuestionsListActivity.class);
                startActivity(intent);
            }
        });

        CreateMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_MatchingQuestionsListActivity.class);
                startActivity(intent);
            }
        });

        CreateRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_RankingQuestionsListActivity.class);
                startActivity(intent);
            }
        });

    }


}