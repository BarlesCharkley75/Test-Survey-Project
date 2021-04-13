package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.auth.User;

public class UserTypeActivity extends AppCompatActivity {

    private Button taker, creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_type);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("User type");

        taker = findViewById(R.id.button_taker);
        creator = findViewById(R.id.button_creator);

        taker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this,CategoryActivity.class);
                startActivity(intent);
            }
        });

        creator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserTypeActivity.this,Creator_CategoryActivity.class);
                startActivity(intent);
            }
        });




    }


}