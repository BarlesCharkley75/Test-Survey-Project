package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ScrollView;

import java.lang.reflect.Array;
import java.util.ArrayList;


import static com.example.quizapp.UserProfileActivity.surveyList;

public class SurveyListActivity extends AppCompatActivity {

    private GridView survey_list_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of Surveys");


        //use grid view adapter to display sets of surveys.
        survey_list_grid = findViewById(R.id.test_lists_gridview);

        SurveyListAdapter adapter = new SurveyListAdapter(surveyList);
        survey_list_grid.setAdapter(adapter);


    }
}