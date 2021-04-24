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

import static com.example.quizapp.Creator_CreateQuestionsActivity.CurrentNumOfTest;
import static com.example.quizapp.Creator_TestListActivity.test_id_list;

public class Creator_MatchingQuestionsListActivity extends AppCompatActivity {

    private GridView MatchingQuestion_List;
    private Button add_new_question;
    private FirebaseFirestore firestore;
    private Dialog loading;

    private ArrayList<String> sub_MatchingQuestionsList = new ArrayList<>();

    public static ArrayList<String> Matching_id_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__matching_questions_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("test"+String.valueOf(CurrentNumOfTest + 1)+": " + "List of Matching questions");

        MatchingQuestion_List = findViewById(R.id.creator_MatchingQuestions_list_gridview);
        add_new_question = findViewById(R.id.AddQuestionButton);

        firestore = FirebaseFirestore.getInstance();

        loading = new Dialog(Creator_MatchingQuestionsListActivity.this);
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
                Intent intent = new Intent (Creator_MatchingQuestionsListActivity.this, Creator_CreateMatchingQuestionActivity.class);
                startActivity(intent);
            }
        });
    }


    private void loadData(){
        sub_MatchingQuestionsList.clear();
        Matching_id_list.clear();
        firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest)).collection("MatchingQuestions")
                .document("questionList").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()){
                                String count = doc.getString("count");

                                for(int i = 1; i <= Integer.valueOf(count); i ++){
//                            String testName = doc.getString("test" + String.valueOf(i)+"_name");
                                    String testName = "question" + String.valueOf(i);
                                    sub_MatchingQuestionsList.add(testName);


                                    Matching_id_list.add(doc.getString("question" + String.valueOf(i)+"_id"));

                                }

                                //Refer to TestListActivity to see how to set up the grid view adapter.
                                Creator_MatchingQuestionsListAdapter adapter = new Creator_MatchingQuestionsListAdapter(sub_MatchingQuestionsList);
                                MatchingQuestion_List.setAdapter(adapter);

                                loading.cancel();
                            }
                            else{
                                loading.cancel();
                                Toast.makeText(Creator_MatchingQuestionsListActivity.this, "No question yet",Toast.LENGTH_SHORT).show();
                                Creator_MatchingQuestionsListAdapter adapter = new Creator_MatchingQuestionsListAdapter(sub_MatchingQuestionsList);
                                MatchingQuestion_List.setAdapter(adapter);
                            }
                        }
                        else{
                            loading.cancel();
                            Toast.makeText(Creator_MatchingQuestionsListActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}