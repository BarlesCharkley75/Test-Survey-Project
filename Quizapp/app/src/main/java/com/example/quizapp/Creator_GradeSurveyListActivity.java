package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import static com.example.quizapp.UserProfileActivity.surveyList;
import static com.example.quizapp.UserProfileActivity.testList;

public class Creator_GradeSurveyListActivity extends AppCompatActivity {

    private GridView grade_surveyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__grade_survey_list);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Grading: List of Surveys");

        grade_surveyList = findViewById(R.id.grade_survey_lists_gridview);

        Creator_GradeSurveyListAdapter adapter = new Creator_GradeSurveyListAdapter(surveyList);
        grade_surveyList.setAdapter(adapter);
    }
}