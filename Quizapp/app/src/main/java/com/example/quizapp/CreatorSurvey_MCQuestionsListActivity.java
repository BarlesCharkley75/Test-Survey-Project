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
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import static com.example.quizapp.CreatorSurvey_CreateQuestionsActivity.CurrentNumOfSurvey;
import static com.example.quizapp.Creator_SurveyListActivity.survey_id_list;


public class CreatorSurvey_MCQuestionsListActivity extends AppCompatActivity {

    private GridView MCQuestion_List;
    private Button add_new_question;
    private FirebaseFirestore firestore;
    private Dialog loading;

//    public static int CurrentNumOfSurvey;

    private ArrayList<String> sub_MCQuestionsList = new ArrayList<>();

    public static ArrayList<String> MC_id_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creatorsurvey__m_c_questions_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("survey"+String.valueOf(CurrentNumOfSurvey + 1)+": " + "List of MC questions");

        MCQuestion_List = findViewById(R.id.creatorsurvey_MCQuestions_list_gridview);
        add_new_question = findViewById(R.id.AddQuestionButton);

        firestore = FirebaseFirestore.getInstance();



        loading = new Dialog(CreatorSurvey_MCQuestionsListActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        loadData();


        add_new_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to createMCQuestion activity
                Intent intent = new Intent (CreatorSurvey_MCQuestionsListActivity.this, CreatorSurvey_CreateMCQuestionsActivity.class);
                startActivity(intent);
            }
        });

    }

    private void loadData(){
        sub_MCQuestionsList.clear();
        MC_id_list.clear();
        firestore.collection("surveys").document(survey_id_list.get(CurrentNumOfSurvey)).collection("MCQuestions")
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
                                    sub_MCQuestionsList.add(surveyName);


                                    MC_id_list.add(doc.getString("question" + String.valueOf(i)+"_id"));

                                }

                                //Refer to SurveyListActivity to see how to set up the grid view adapter.
                                CreatorSurvey_MCQuestionsListAdapter adapter = new CreatorSurvey_MCQuestionsListAdapter(sub_MCQuestionsList);
                                MCQuestion_List.setAdapter(adapter);

                                loading.cancel();
                            }
                            else{
                                loading.cancel();
                                Toast.makeText(CreatorSurvey_MCQuestionsListActivity.this, "No question yet",Toast.LENGTH_SHORT).show();
                                CreatorSurvey_MCQuestionsListAdapter adapter = new CreatorSurvey_MCQuestionsListAdapter(sub_MCQuestionsList);
                                MCQuestion_List.setAdapter(adapter);
                            }
                        }
                        else{
                            loading.cancel();
                            Toast.makeText(CreatorSurvey_MCQuestionsListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}