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


import static com.example.quizapp.UserProfileActivity.testList;

public class Creator_TestListActivity extends AppCompatActivity {

    private GridView creator_test_list_gridview;
    private Button AddNewTest;
    private FirebaseFirestore firestore;
    private Dialog loading;

    private int count, next;

    public static ArrayList<String> test_id_list;

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
        test_id_list = new ArrayList<>();
        firestore.collection("tests").document("testList").
                get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){

                        count = Integer.valueOf(doc.getString("count"));
                        next = Integer.valueOf(doc.getString("NEXT"));

                        for(int i = 1; i <= count; i ++){
//                            String testName = doc.getString("test" + String.valueOf(i)+"_name");
                            String testName = "test" + String.valueOf(i);
                            testList.add(testName);

                            test_id_list.add(doc.getString("test"+String.valueOf(i)+"_id"));


                        }

                        //Refer to TestListActivity to see how to set up the grid view adapter.
                        Creator_TestListAdapter adapter = new Creator_TestListAdapter(testList);
                        creator_test_list_gridview.setAdapter(adapter);

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

        testData.put("name", "test" + String.valueOf(count + 1));



//      update the database to add a new test.
        String doc_id = "test" + String.valueOf(next);

        firestore.collection("tests").document(doc_id)
                .set(testData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Map<String , Object> testDoc = new ArrayMap<>();

                testDoc.put("test" + String.valueOf(count + 1)+"_id", "test" + String.valueOf(next));
                testDoc.put("count", String.valueOf(testList.size() + 1));
                testDoc.put("NEXT", String.valueOf(next + 1));

                firestore.collection("tests").document("testList")
                        .update(testDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        test_id_list.add("test"+String.valueOf(next));

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
        questionSetData.put("NEXT","1");



//        Add MCQuestions

        firestore.collection("tests").document("test"+String.valueOf(next))
                .collection("MCQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                });

//        Add FRQuestions
        firestore.collection("tests").document("test"+String.valueOf(next))
                .collection("FRQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

//        Add MatchingQuestions

        firestore.collection("tests").document("test"+String.valueOf(next))
                .collection("MatchingQuestions").document("questionList").set(questionSetData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });

//        Add RankingQuestions
        firestore.collection("tests").document("test"+String.valueOf(next))
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


        createNewWorksheet();
    }

    private void createNewWorksheet(){

        Map<String, Object> name = new ArrayMap<>();

        name.put("name", "test" + String.valueOf(next));

        firestore.collection("TestWorksheet").document("test"+String.valueOf(next))
                .set(name);



        Map<String, Object> worksheetListData = new ArrayMap<>();

        worksheetListData.put("count","0");
        worksheetListData.put("NEXT","1");

        firestore.collection("TestWorksheet").document("test"+String.valueOf(next))
                .collection("userWorksheets").document("worksheetList").set(worksheetListData);



    }


}