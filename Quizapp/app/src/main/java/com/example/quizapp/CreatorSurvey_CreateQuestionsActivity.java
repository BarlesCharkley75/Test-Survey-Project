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

import static com.example.quizapp.Creator_SurveyListActivity.survey_id_list;

import static com.example.quizapp.UserProfileActivity.surveyList;

public class CreatorSurvey_CreateQuestionsActivity extends AppCompatActivity {

    private Button CreateMC, CreateFR, CreateMatching, CreateRanking;

    public static int CurrentNumOfSurvey;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatorsurvey__create_questions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        CreateMC = findViewById(R.id.CreateMCQuestion);
        CreateFR = findViewById(R.id.CreateFRQuestion);
        CreateMatching = findViewById(R.id.CreateMatchingQuestion);
        CreateRanking = findViewById(R.id.CreateRankingQuestion);

        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        int temp = intent.getIntExtra("NAME",-5);


        if(temp == -10) {//this is a new survey
            CurrentNumOfSurvey = surveyList.size();

//            firestore.collection("surveys").document("surveyList").
//                    get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot doc = task.getResult();
//                        if (doc.exists()) {
//                            survey_id_list.add(doc.getString("survey"+String.valueOf(CurrentNumOfSurvey + 1)+"_id"));
//                        }
//                    }
//                }
//            });
        }
        else if(temp != -5){
            CurrentNumOfSurvey = temp;
        }

        getSupportActionBar().setTitle("survey"+String.valueOf(CurrentNumOfSurvey + 1)+": " + "Create questions");



        CreateMC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatorSurvey_CreateQuestionsActivity.this, CreatorSurvey_MCQuestionsListActivity.class);
                startActivity(intent);
            }
        });

        CreateFR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatorSurvey_CreateQuestionsActivity.this, CreatorSurvey_FRQuestionsListActivity.class);
                startActivity(intent);
            }
        });

        CreateMatching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatorSurvey_CreateQuestionsActivity.this, CreatorSurvey_MatchingQuestionsListActivity.class);
                startActivity(intent);
            }
        });

        CreateRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreatorSurvey_CreateQuestionsActivity.this, CreatorSurvey_RankingQuestionsListActivity.class);
                startActivity(intent);
            }
        });

    }


}