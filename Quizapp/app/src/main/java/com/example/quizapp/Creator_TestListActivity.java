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

import java.util.Map;

import static com.example.quizapp.MainActivity.testList;

public class Creator_TestListActivity extends AppCompatActivity {

    private GridView creator_test_list_gridview;
    private Button AddNewTest;
    private FirebaseFirestore firestore;
    private Dialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__test_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List of Tests");




        creator_test_list_gridview = findViewById(R.id.creator_test_lists_gridview);



        firestore = FirebaseFirestore.getInstance();
        AddNewTest = findViewById(R.id.AddTestButton);

        loading = new Dialog(Creator_TestListActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();

        loadData();






        AddNewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTest();
            }
        });


    }


    private void loadData(){
        testList.clear();
        firestore.collection("tests").document("testList").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        long count = (long)doc.get("count");

                        for(int i = 1; i <= count; i ++){
                            String testName = doc.getString("test" + String.valueOf(i)+"_name");
                            testList.add(testName);

                            //Refer to TestListActivity to see how to set up the grid view adapter.
                            Creator_TestListAdapter adapter = new Creator_TestListAdapter(testList);
                            creator_test_list_gridview.setAdapter(adapter);

                        }
                        loading.cancel();
                    }
                    else{
                        loading.cancel();
                        Toast.makeText(Creator_TestListActivity.this, "No tests yet",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
                else{
                    loading.cancel();
                    Toast.makeText(Creator_TestListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
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
                    }
                });

//        Add FRQuestions
        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("FRQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

//        Add MatchingQuestions

        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("MatchingQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

//        Add RankingQuestions
        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("RankingQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Creator_TestListActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                    }
                });

        createQuestions();
    }

    private void createQuestions(){
        Intent intent = new Intent(Creator_TestListActivity.this,Creator_CreateQuestionsActivity.class);
        intent.putExtra("NAME",-10);
        startActivity(intent);
    }



}