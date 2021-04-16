package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static com.example.quizapp.MainActivity.testList;

public class Creator_TestListActivity extends AppCompatActivity {

    private GridView creator_test_list_gridview;
    private Button AddNewTest;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__test_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of Tests");


        creator_test_list_gridview = findViewById(R.id.creator_test_lists_gridview);

        //Refer to TestListActivity to see how to set up the grid view adapter.
        Creator_TestListAdapter adapter = new Creator_TestListAdapter(testList);
        creator_test_list_gridview.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        AddNewTest = findViewById(R.id.AddTestButton);

        AddNewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNewTest();
            }
        });


    }

    private void addNewTest(){
        Map<String,Object> testData = new ArrayMap<>();

        testData.put("name", "test" + String.valueOf(testList.size() + 1));
        testData.put("NumOfQuestions", 0);


//      update the database to add a new test.
        String doc_id = "test" + String.valueOf(testList.size()+1);

        firestore.collection("tests").document(doc_id)
                .set(testData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Map<String , Object> testDoc = new ArrayMap<>();

                testDoc.put("test" + String.valueOf(testList.size() + 1)+"_name", "test" + String.valueOf(testList.size() + 1));
                testDoc.put("count", testList.size() + 1);

                firestore.collection("tests").document("testList")
                        .update(testDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Creator_TestListActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Creator_TestListActivity.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Creator_TestListActivity.this, e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }
                });

        addQuestionSets();
    }

    private void addQuestionSets(){
        Map<String, Object> questionSetData = new ArrayMap<>();
        questionSetData.put("count","0");



//        Add MCQuestions

        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("MCQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Creator_TestListActivity.this, "succeed1", Toast.LENGTH_SHORT).show();
                    }
                });

//        Add FRQuestions
        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("FRQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Creator_TestListActivity.this, "succeed2", Toast.LENGTH_SHORT).show();
                    }
                });

//        Add MatchingQuestions

        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("MatchingQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Creator_TestListActivity.this, "succeed3", Toast.LENGTH_SHORT).show();
                    }
                });

//        Add RankingQuestions
        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("RankingQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Creator_TestListActivity.this, "succeed4", Toast.LENGTH_SHORT).show();
                    }
                });

        createQuestions();
    }

    private void createQuestions(){
        Intent intent = new Intent(Creator_TestListActivity.this,Creator_CreateQuestionsActivity.class);
        startActivity(intent);
    }



}