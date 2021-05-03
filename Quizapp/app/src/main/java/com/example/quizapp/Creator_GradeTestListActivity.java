package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import static com.example.quizapp.UserProfileActivity.testList;

public class Creator_GradeTestListActivity extends AppCompatActivity {

    private GridView grade_testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__grade_test_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Grading: List of Tests");

        grade_testList = findViewById(R.id.grade_test_lists_gridview);

        Creator_GradeTestListAdpater adapter = new Creator_GradeTestListAdpater(testList);
        grade_testList.setAdapter(adapter);
    }
}