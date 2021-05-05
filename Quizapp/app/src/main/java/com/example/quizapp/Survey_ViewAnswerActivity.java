package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static com.example.quizapp.Creator_Survey_GradeFRQuestionsActivity.survey_grade_fr_current_question;
import static com.example.quizapp.Creator_Survey_GradeMCQuestionsActivity.grader_NumOfSurvey;
import static com.example.quizapp.UserProfileActivity.SURVEY_IDs;

public class Survey_ViewAnswerActivity extends AppCompatActivity {

    private TextView answer;

    private int NumOfAnswer;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey__view_answer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        answer = findViewById(R.id.answer);


        Intent intent = getIntent();

        NumOfAnswer = intent.getIntExtra("NAME",-1);


        firestore = FirebaseFirestore.getInstance();


        firestore.collection("SurveyWorksheet").document(SURVEY_IDs.get(grader_NumOfSurvey)).collection("FRQuestions")
                .document("question" + String.valueOf(survey_grade_fr_current_question + 1)).collection("userAnswers")
                .document("answer" + String.valueOf(NumOfAnswer + 1)).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {



                String input = documentSnapshot.getString("input");

                answer.setText(input);

            }
        });




    }
}