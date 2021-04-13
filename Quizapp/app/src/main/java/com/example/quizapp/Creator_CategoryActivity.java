package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Creator_CategoryActivity extends AppCompatActivity {

    private Button test,survey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__category);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Creator: Select Type");

        test = findViewById(R.id.button_test);
        survey = findViewById(R.id.button_survey);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CategoryActivity.this,Creator_TestListActivity.class);
                startActivity(intent);
            }
        });


    }
}