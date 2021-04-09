package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ScrollView;

public class TestListActivity extends AppCompatActivity {

    private GridView test_list_grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of Tests");


        test_list_grid = findViewById(R.id.test_lists_gridview);


        TestListAdapter adapter = new TestListAdapter(15);
        test_list_grid.setAdapter(adapter);


    }
}