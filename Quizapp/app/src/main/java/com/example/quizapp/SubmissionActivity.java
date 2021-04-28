package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubmissionActivity extends AppCompatActivity implements View.OnClickListener{

    private Button submisison, GoPrev;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        submisison = findViewById(R.id.submission);
        GoPrev = findViewById(R.id.GoPrev);

        submisison.setOnClickListener(this);
        GoPrev.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submission){
            Intent intent = new Intent(SubmissionActivity.this, UserProfileActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //This will cancel all the opened activities.
            startActivity(intent);
        }

        if(v.getId() == R.id.GoPrev){
            Intent intent = new Intent(SubmissionActivity.this, RankingQuestionActivity.class);
            intent.putExtra("info",2);
            startActivity(intent);
        }
    }
}