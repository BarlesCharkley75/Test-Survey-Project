package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;


import static com.example.quizapp.UserProfileActivity.surveyList;

public class Creator_SurveyListActivity extends AppCompatActivity {

    private GridView creator_survey_list_gridview;
    private Button AddNewSurvey;
    private FirebaseFirestore firestore;
    private Dialog loading;

    private int count, next;

    public static ArrayList<String> survey_id_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__test_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of Surveys");




        creator_survey_list_gridview = findViewById(R.id.creator_test_lists_gridview);



        firestore = FirebaseFirestore.getInstance();
        AddNewSurvey = findViewById(R.id.AddTestButton);

        loading = new Dialog(Creator_SurveyListActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        loadData();






        AddNewSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSurvey();
            }
        });


    }


    private void loadData(){
        surveyList.clear();
        survey_id_list = new ArrayList<>();
        firestore.collection("survey").document("surveyList").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        count = Integer.valueOf(doc.getString("count"));
                        next = Integer.valueOf(doc.getString("NEXT"));

                        for(int i = 1; i <= count; i ++){
//                            String SurveyName = doc.getString("survey" + String.valueOf(i)+"_name");
                            String surveyName = "survey" + String.valueOf(i);
                            surveyList.add(surveyName);

                            survey_id_list.add(doc.getString("survey"+String.valueOf(i)+"_id"));


                        }

                        //Refer to SurveyListActivity to see how to set up the grid view adapter.
                        Creator_SurveyListAdapter adapter = new Creator_SurveyListAdapter(surveyList);
                        creator_survey_list_gridview.setAdapter(adapter);

                        loading.cancel();
                    }
                    else{
                        loading.cancel();
                        Toast.makeText(Creator_SurveyListActivity.this, "No surveys yet",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    loading.cancel();
                    Toast.makeText(Creator_SurveyListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addNewSurvey(){
        Map<String,Object> surveyData = new ArrayMap<>();

        surveyData.put("name", "survey" + String.valueOf(count + 1));



//      update the database to add a new survey.
        String doc_id = "survey" + String.valueOf(next);

        firestore.collection("survey").document(doc_id)
                .set(surveyData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Map<String , Object> testDoc = new ArrayMap<>();

                testDoc.put("survey" + String.valueOf(count + 1)+"_id", "survey" + String.valueOf(next));
                testDoc.put("count", String.valueOf(surveyList.size() + 1));
                testDoc.put("NEXT", String.valueOf(next + 1));

                firestore.collection("surveys").document("surveyList")
                        .update(testDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        survey_id_list.add("survey"+String.valueOf(next));

                        Toast.makeText(Creator_SurveyListActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Creator_SurveyListActivity.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Creator_SurveyListActivity.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });

        addQuestionSets();
    }

    private void addQuestionSets(){
        Map<String, Object> questionSetData = new ArrayMap<>();
        questionSetData.put("count","0");
        questionSetData.put("NEXT","1");



//        Add MCQuestions

        firestore.collection("surveys").document("survey"+String.valueOf(next))
                .collection("MCQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });

//        Add FRQuestions
        firestore.collection("surveys").document("survey"+String.valueOf(next))
                .collection("FRQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

//        Add MatchingQuestions

        firestore.collection("surveys").document("survey"+String.valueOf(next))
                .collection("MatchingQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

//        Add RankingQuestions
        firestore.collection("surveys").document("survey"+String.valueOf(next))
                .collection("RankingQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Creator_SurveyListActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                    }
                });



        createQuestions();
    }

    private void createQuestions(){
        Intent intent = new Intent(Creator_SurveyListActivity.this,Creator_CreateQuestionsActivity.class);
        intent.putExtra("NAME",-10);
        startActivity(intent);
    }



}