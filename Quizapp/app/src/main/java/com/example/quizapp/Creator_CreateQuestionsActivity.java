package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.quizapp.Creator_TestListActivity.test_id_list;

import static com.example.quizapp.UserProfileActivity.testList;

public class Creator_CreateQuestionsActivity extends AppCompatActivity {

    private Button CreateMC, CreateFR, CreateMatching, CreateRanking;

    public static int CurrentNumOfTest;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__create_questions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        CreateMC = findViewById(R.id.CreateMCQuestion);
        CreateFR = findViewById(R.id.CreateFRQuestion);
        CreateMatching = findViewById(R.id.CreateMatchingQuestion);
        CreateRanking = findViewById(R.id.CreateRankingQuestion);

        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        int temp = intent.getIntExtra("NAME",-5);


        if(temp == -10) {//this is a new test
            CurrentNumOfTest = testList.size();

//            firestore.collection("tests").document("testList").
//                    get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot doc = task.getResult();
//                        if (doc.exists()) {
//                            test_id_list.add(doc.getString("test"+String.valueOf(CurrentNumOfTest + 1)+"_id"));
//                        }
//                    }
//                }
//            });
        }
        else if(temp != -5){
            CurrentNumOfTest = temp;
        }

        getSupportActionBar().setTitle("test"+String.valueOf(CurrentNumOfTest + 1)+": " + "Create questions");



        CreateMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Creator_CreateQuestionsActivity.this, Creator_MCQuestionsListActivity.class);
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