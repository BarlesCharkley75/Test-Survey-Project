package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import static com.example.quizapp.UserProfileActivity.TEST_IDs;
import static com.example.quizapp.UserProfileActivity.testList;

public class Test_WorksheetListActivity extends AppCompatActivity {

    private GridView worksheet_list;

    public static int grader_NumOfTest;

    public static ArrayList<String> grader_worksheet_names = new ArrayList<>();

    private FirebaseFirestore firestore;

    private Dialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__worksheet_list);

        Intent intent = getIntent();

        grader_NumOfTest = intent.getIntExtra("NAME",-1);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Grading " + "test " + String.valueOf(grader_NumOfTest + 1)+": worksheets");


        firestore = FirebaseFirestore.getInstance();
        loading = new Dialog(Test_WorksheetListActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loading.show();


        firestore.collection("TestWorksheet").document(TEST_IDs.get(grader_NumOfTest)).collection("userWorksheets")
                .document("worksheetList").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()){
                        int count = Integer.valueOf(doc.getString("count"));

                        grader_worksheet_names.clear();

                        for(int i = 1; i <= count; i++){
                            grader_worksheet_names.add(doc.getString("worksheet"+String.valueOf(i)+"_name"));
                        }

                        loading.cancel();


                        worksheet_list = findViewById(R.id.worksheet_list_gridview);

                        Test_WorksheetListAdapter adapter = new Test_WorksheetListAdapter(grader_worksheet_names);

                        worksheet_list.setAdapter(adapter);
                    }
                }
            }
        });










    }
}