package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import static com.example.quizapp.UserProfileActivity.surveyList;
import static com.example.quizapp.UserProfileActivity.testList;

public class SurveyListActivity extends AppCompatActivity {

    private GridView survey_list_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of Surveys");

        //use grid view adapter to display sets of surveys.
        survey_list_grid = findViewById(R.id.survey_lists_gridview);

        SurveyListAdapter adapter = new SurveyListAdapter(surveyList);
        survey_list_grid.setAdapter(adapter);

    }
}