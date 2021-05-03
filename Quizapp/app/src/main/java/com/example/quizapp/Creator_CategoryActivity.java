package com.example.quizapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

        test.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(Creator_CategoryActivity.this)
                        .setTitle("Grade")
                        .setMessage("Do you want to grade the tests instead of edit them?")
                        .setPositiveButton("Grade", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Creator_CategoryActivity.this, Creator_GradeTestListActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();


                alertDialog.show();
                return true;
            }
        });


        survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CategoryActivity.this,Creator_SurveyListActivity.class);
                startActivity(intent);
            }
        });

        survey.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Creator_CategoryActivity.this)
                        .setTitle("Summary")
                        .setMessage("Do you want to see the survey result instead of edit them?")
                        .setPositiveButton("Grade", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Creator_CategoryActivity.this, Creator_GradeSurveyListActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();


                alertDialog.show();
                return true;
            }
        });


    }


}