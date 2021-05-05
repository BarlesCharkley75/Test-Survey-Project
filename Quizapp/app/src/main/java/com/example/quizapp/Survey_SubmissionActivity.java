package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.example.quizapp.FRQuestionActivity.FR_Answers;
import static com.example.quizapp.MCQuestionActivity2.MC_selected;
import static com.example.quizapp.MatchingQurestionActivity.MATCHING_Answers;
import static com.example.quizapp.RankingQuestionActivity.RANKING_Answers;
import static com.example.quizapp.Survey_FRQuestionActivity.survey_FR_Answers;
import static com.example.quizapp.Survey_MCQuestionActivity.survey_MC_selected;
import static com.example.quizapp.Survey_MatchingQuestionActivity.survey_MATCHING_Answers;
import static com.example.quizapp.Survey_RankingQuestionActivity.survey_RANKING_Answers;

public class Survey_SubmissionActivity extends AppCompatActivity {

    private Button back_to_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey__submission);

        back_to_main = findViewById(R.id.back_to_main);

//        survey_MC_selected.clear();
//        survey_FR_Answers.clear();
//        survey_MATCHING_Answers.clear();
//        survey_RANKING_Answers.clear();


        back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Survey_SubmissionActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}