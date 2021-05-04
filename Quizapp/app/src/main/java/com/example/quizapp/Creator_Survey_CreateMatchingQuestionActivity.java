package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
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

import static com.example.quizapp.Creator_CreateQuestionsActivity.CurrentNumOfTest;
import static com.example.quizapp.Creator_MatchingQuestionsListActivity.Matching_id_list;
import static com.example.quizapp.Creator_SurveyListActivity.survey_id_list;
import static com.example.quizapp.Creator_Survey_CreateQuestionsActivity.CurrentNumOfSurvey;
import static com.example.quizapp.Creator_Survey_MatchingQuestionsListActivity.survey_Matching_id_list;
import static com.example.quizapp.Creator_TestListActivity.test_id_list;

public class Creator_Survey_CreateMatchingQuestionActivity extends AppCompatActivity {

    private EditText question;

    private TextInputLayout optionL1, optionL2, optionL3, optionL4;

    private TextInputLayout optionR1, optionR2, optionR3, optionR4;

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
        setContentView(R.layout.activity_creator__survey__create_matching_question);


        question = findViewById(R.id.question);

        optionL1 = findViewById(R.id.optionL1);
        optionL2 = findViewById(R.id.optionL2);
        optionL3 = findViewById(R.id.optionL3);
        optionL4 = findViewById(R.id.optionL4);

        optionR1 = findViewById(R.id.optionR1);
        optionR2 = findViewById(R.id.optionR2);
        optionR3 = findViewById(R.id.optionR3);
        optionR4 = findViewById(R.id.optionR4);

        finish = findViewById(R.id.finish);

        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();

        NumOfQuestion = intent.getIntExtra("QuestionNAME",0);

        loading = new Dialog(Creator_Survey_CreateMatchingQuestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                .collection("MatchingQuestions").document("questionList").get()
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

                if(modifying == false){//means this is a new question
                    Map<String,Object> questionData = new ArrayMap<>();

                    questionData.put("question",question.getText().toString());
                    questionData.put("optionL1",optionL1.getEditText().getText().toString());
                    questionData.put("optionL2",optionL2.getEditText().getText().toString());
                    questionData.put("optionL3",optionL3.getEditText().getText().toString());
                    questionData.put("optionL4",optionL4.getEditText().getText().toString());
                    questionData.put("optionR1",optionR1.getEditText().getText().toString());
                    questionData.put("optionR2",optionR2.getEditText().getText().toString());
                    questionData.put("optionR3",optionR3.getEditText().getText().toString());
                    questionData.put("optionR4",optionR4.getEditText().getText().toString());





                    firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                            .collection("MatchingQuestions").document("question"+String.valueOf(next)).set(questionData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Map<String,Object> newList = new ArrayMap<>();

                                    newList.put("count",String.valueOf(count + 1));
                                    newList.put("NEXT", String.valueOf(next + 1));
                                    newList.put("question"+String.valueOf(count + 1)+"_id","question"+String.valueOf(next));

                                    firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                                            .collection("MatchingQuestions").document("questionList").update(newList)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    loading.cancel();
                                                    Toast.makeText(Creator_Survey_CreateMatchingQuestionActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent = new Intent(Creator_Survey_CreateMatchingQuestionActivity.this, Creator_Survey_MatchingQuestionsListActivity.class);
                                    startActivity(intent);
                                }
                            });

                }
                else{
                    Map<String,Object> questionData = new ArrayMap<>();
                    questionData.put("question", question.getText().toString());
                    questionData.put("optionL1",optionL1.getEditText().getText().toString());
                    questionData.put("optionL2",optionL2.getEditText().getText().toString());
                    questionData.put("optionL3",optionL3.getEditText().getText().toString());
                    questionData.put("optionL4",optionL4.getEditText().getText().toString());
                    questionData.put("optionR1",optionR1.getEditText().getText().toString());
                    questionData.put("optionR2",optionR2.getEditText().getText().toString());
                    questionData.put("optionR3",optionR3.getEditText().getText().toString());
                    questionData.put("optionR4",optionR4.getEditText().getText().toString());


                    firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                            .collection("MatchingQuestions").document(survey_Matching_id_list.get(NumOfQuestion - 1)).update(questionData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loading.cancel();
                                    Intent intent = new Intent(Creator_Survey_CreateMatchingQuestionActivity.this, Creator_Survey_MatchingQuestionsListActivity.class);
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
                    .collection("MatchingQuestions").document("questionList").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if(doc.exists()){
                                    current_id = doc.getString("question" + String.valueOf(NumOfQuestion)+"_id");

                                    firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey))
                                            .collection("MatchingQuestions").document(current_id)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot doc = task.getResult();
                                                if(doc.exists()){
                                                    String question_text = doc.getString("question");
                                                    String optionL1_text = doc.getString("optionL1");
                                                    String optionL2_text = doc.getString("optionL2");
                                                    String optionL3_text = doc.getString("optionL3");
                                                    String optionL4_text = doc.getString("optionL4");
                                                    String optionR1_text = doc.getString("optionR1");
                                                    String optionR2_text = doc.getString("optionR2");
                                                    String optionR3_text = doc.getString("optionR3");
                                                    String optionR4_text = doc.getString("optionR4");


                                                    question.setText(question_text);
                                                    optionL1.getEditText().setText(optionL1_text);
                                                    optionL2.getEditText().setText(optionL2_text);
                                                    optionL3.getEditText().setText(optionL3_text);
                                                    optionL4.getEditText().setText(optionL4_text);
                                                    optionR1.getEditText().setText(optionR1_text);
                                                    optionR2.getEditText().setText(optionR2_text);
                                                    optionR3.getEditText().setText(optionR3_text);
                                                    optionR4.getEditText().setText(optionR4_text);


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