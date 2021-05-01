package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.quizapp.CreatorSurvey_CreateQuestionsActivity.CurrentNumOfSurvey;
import static com.example.quizapp.Creator_SurveyListActivity.survey_id_list;

public class CreatorSurvey_FRQuestionsListActivity extends AppCompatActivity {

    private GridView FRQuestion_List;
    private Button add_new_question;
    private FirebaseFirestore firestore;
    private Dialog loading;

    private ArrayList<String> sub_FRQuestionsList = new ArrayList<>();

    public static ArrayList<String> FR_id_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatorsurvey__f_r_questions_list);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("survey"+String.valueOf(CurrentNumOfSurvey + 1)+": " + "List of FR questions");

        FRQuestion_List = findViewById(R.id.creatorsurvey_FRQuestions_list_gridview);
        add_new_question = findViewById(R.id.AddQuestionButton);

        firestore = FirebaseFirestore.getInstance();



        loading = new Dialog(CreatorSurvey_FRQuestionsListActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        loadData();

        add_new_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to createFRQuestion activity
                Intent intent = new Intent (CreatorSurvey_FRQuestionsListActivity.this, CreatorSurvey_CreateFRQuestionActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadData(){
        sub_FRQuestionsList.clear();
        FR_id_list.clear();
        firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey)).collection("FRQuestions")
                .document("questionList").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()){
                                String count = doc.getString("count");

                                for(int i = 1; i <= Integer.valueOf(count); i ++){
//                            String surveyName = doc.getString("survey" + String.valueOf(i)+"_name");
                                    String surveyName = "question" + String.valueOf(i);
                                    sub_FRQuestionsList.add(surveyName);


                                    FR_id_list.add(doc.getString("question" + String.valueOf(i)+"_id"));

                                }

                                //Refer to SurveyListActivity to see how to set up the grid view adapter.
                                CreatorSurvey_FRQuestionsListAdapter adapter = new CreatorSurvey_FRQuestionsListAdapter(sub_FRQuestionsList);
                                FRQuestion_List.setAdapter(adapter);

                                loading.cancel();
                            }
                            else{
                                loading.cancel();
                                Toast.makeText(CreatorSurvey_FRQuestionsListActivity.this, "No question yet",Toast.LENGTH_SHORT).show();
                                CreatorSurvey_FRQuestionsListAdapter adapter = new CreatorSurvey_FRQuestionsListAdapter(sub_FRQuestionsList);
                                FRQuestion_List.setAdapter(adapter);
                            }
                        }
                        else{
                            loading.cancel();
                            Toast.makeText(CreatorSurvey_FRQuestionsListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}