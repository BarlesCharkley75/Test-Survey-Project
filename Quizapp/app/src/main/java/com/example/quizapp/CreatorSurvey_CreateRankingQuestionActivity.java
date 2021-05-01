package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.icu.util.Freezable;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static com.example.quizapp.CreatorSurvey_CreateQuestionsActivity.CurrentNumOfSurvey;
import static com.example.quizapp.CreatorSurvey_MCQuestionsListActivity.MC_id_list;
import static com.example.quizapp.CreatorSurvey_RankingQuestionsListActivity.Ranking_id_list;
import static com.example.quizapp.Creator_SurveyListActivity.survey_id_list;

public class CreatorSurvey_CreateRankingQuestionActivity extends AppCompatActivity {

    private EditText question;
    private TextInputLayout option1, option2, option3, option4;
    private TextInputLayout Answer1, Answer2, Answer3, Answer4;
    private Button finish;

    private int count = 0;
    private FirebaseFirestore firestore;

    private int next = 0;

    private int NumOfQuestion;

    private String current_id;

    private boolean modifying = false;

    private Dialog loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatorsurvey__create_ranking_question);

        question = findViewById(R.id.question);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        Answer1 = findViewById(R.id.Answer1);
        Answer2 = findViewById(R.id.Answer2);
        Answer3 = findViewById(R.id.Answer3);
        Answer4 = findViewById(R.id.Answer4);

        finish = findViewById(R.id.finish);

        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        NumOfQuestion = intent.getIntExtra("QuestionNAME",0);


        loading = new Dialog(CreatorSurvey_CreateRankingQuestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                .collection("RankingQuestions").document("questionList").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()){
                                count = Integer.valueOf(doc.getString("count"));
                                next = Integer.valueOf(doc.getString("NEXT"));
                            }
                        }
                    }
                });

        setInterface();


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loading.show();

                if (modifying == false){//means this is a new question
                    Map<String,Object> questionData = new ArrayMap<>();

                    questionData.put("question",question.getText().toString());
                    questionData.put("option1",option1.getEditText().getText().toString());
                    questionData.put("option2",option2.getEditText().getText().toString());
                    questionData.put("option3",option3.getEditText().getText().toString());
                    questionData.put("option4",option4.getEditText().getText().toString());

                    questionData.put("Answer1", Answer1.getEditText().getText().toString());
                    questionData.put("Answer2", Answer2.getEditText().getText().toString());
                    questionData.put("Answer3", Answer3.getEditText().getText().toString());
                    questionData.put("Answer4", Answer4.getEditText().getText().toString());

                    questionData.put("user_input1", "null");
                    questionData.put("user_input2", "null");
                    questionData.put("user_input3", "null");
                    questionData.put("user_input4", "null");

                    firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                            .collection("RankingQuestions").document("question"+String.valueOf(next)).set(questionData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Map<String,Object> newList = new ArrayMap<>();

                                    newList.put("count",String.valueOf(count + 1));
                                    newList.put("NEXT", String.valueOf(next + 1));
                                    newList.put("question"+String.valueOf(count + 1)+"_id","question"+String.valueOf(next));

                                    firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                                            .collection("RankingQuestions").document("questionList").update(newList)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    loading.cancel();
                                                    Toast.makeText(CreatorSurvey_CreateRankingQuestionActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent = new Intent(CreatorSurvey_CreateRankingQuestionActivity.this, CreatorSurvey_RankingQuestionsListActivity.class);
                                    startActivity(intent);
                                }
                            });


                }
                else {
                    Map<String,Object> questionData = new ArrayMap<>();

                    questionData.put("question", question.getText().toString());
                    questionData.put("option1", option1.getEditText().getText().toString());
                    questionData.put("option2", option2.getEditText().getText().toString());
                    questionData.put("option3", option3.getEditText().getText().toString());
                    questionData.put("option4", option4.getEditText().getText().toString());

                    questionData.put("Answer1", Answer1.getEditText().getText().toString());
                    questionData.put("Answer2", Answer2.getEditText().getText().toString());
                    questionData.put("Answer3", Answer3.getEditText().getText().toString());
                    questionData.put("Answer4", Answer4.getEditText().getText().toString());

                    questionData.put("user_input1", "null");
                    questionData.put("user_input2", "null");
                    questionData.put("user_input3", "null");
                    questionData.put("user_input4", "null");

                    firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                            .collection("RankingQuestions").document(Ranking_id_list.get(NumOfQuestion - 1)).update(questionData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loading.cancel();
                                    Intent intent = new Intent(CreatorSurvey_CreateRankingQuestionActivity.this, CreatorSurvey_RankingQuestionsListActivity.class);
                                    startActivity(intent);
                                }
                            });
                }

            }
        });

    }

    private void setInterface(){

        if(CurrentNumOfSurvey >= 0 && NumOfQuestion > 0){//it means we are modifying existed questions
            modifying = true;


            //get current question id

            firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                    .collection("RankingQuestions").document("questionList").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if(doc.exists()){
                                    current_id = doc.getString("question" + String.valueOf(NumOfQuestion)+"_id");

                                    firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                                            .collection("RankingQuestions").document(current_id)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot doc = task.getResult();
                                                if(doc.exists()){
                                                    String question_text = doc.getString("question");
                                                    String option1_text = doc.getString("option1");
                                                    String option2_text = doc.getString("option2");
                                                    String option3_text = doc.getString("option3");
                                                    String option4_text = doc.getString("option4");

                                                    String Answer1_text = doc.getString("Answer1");
                                                    String Answer2_text = doc.getString("Answer2");
                                                    String Answer3_text = doc.getString("Answer3");
                                                    String Answer4_text = doc.getString("Answer4");



                                                    question.setText(question_text);
                                                    option1.getEditText().setText(option1_text);
                                                    option2.getEditText().setText(option2_text);
                                                    option3.getEditText().setText(option3_text);
                                                    option4.getEditText().setText(option4_text);

                                                    Answer1.getEditText().setText(Answer1_text);
                                                    Answer2.getEditText().setText(Answer2_text);
                                                    Answer3.getEditText().setText(Answer3_text);
                                                    Answer4.getEditText().setText(Answer4_text);




                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });



        }
        else{
            modifying = false;
        }
    }
}