package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import static com.example.quizapp.Creator_GradeFRQuestionsActivity.FR_points_get;
import static com.example.quizapp.Creator_GradeFRQuestionsActivity.FR_points_total;
import static com.example.quizapp.Creator_GradeMCQuestionsActivity.MC_points_get;
import static com.example.quizapp.Creator_GradeMCQuestionsActivity.MC_points_total;
import static com.example.quizapp.Creator_GradeMatchingQuestionsActivity.MATCHING_points_get;
import static com.example.quizapp.Creator_GradeMatchingQuestionsActivity.MATCHING_points_total;
import static com.example.quizapp.Creator_GradeRankingQuestionsActivity.RANKING_points_get;
import static com.example.quizapp.Creator_GradeRankingQuestionsActivity.RANKING_points_total;

public class FinalScoreActivity extends AppCompatActivity {

    private TextView points_get, points_total;

    private int points_get_all, points_total_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);


        points_get = findViewById(R.id.points_get);
        points_total = findViewById(R.id.points_total);

        points_get_all = 0;

        for (int i = 0; i<MC_points_get.size();i++){
            points_get_all += MC_points_get.get(i);
        }

        for (int i = 0; i<FR_points_get.size();i++){
            points_get_all += FR_points_get.get(i);
        }
        for (int i = 0; i<MATCHING_points_get.size();i++){
            points_get_all += MATCHING_points_get.get(i);
        }
        for (int i = 0; i<RANKING_points_get.size();i++){
            points_get_all += RANKING_points_get.get(i);
        }


        points_total_all = MC_points_total+FR_points_total+MATCHING_points_total+RANKING_points_total;


        points_get.setText(String.valueOf(points_get_all));

        points_total.setText(String.valueOf(points_total_all));




    }
}