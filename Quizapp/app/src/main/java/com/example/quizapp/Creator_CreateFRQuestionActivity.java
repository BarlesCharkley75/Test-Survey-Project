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
import static com.example.quizapp.Creator_FRQuestionsListActivity.FR_id_list;
import static com.example.quizapp.Creator_MCQuestionsListActivity.MC_id_list;
import static com.example.quizapp.Creator_TestListActivity.test_id_list;


public class Creator_CreateFRQuestionActivity extends AppCompatActivity {

    private EditText question;
    private TextInputLayout word_limit;
    private Button finish;

    private int count = 0;
    private int next = 0;

    private int NumOfQuestion;

    private String current_id;

    private boolean modifying = false;

    private FirebaseFirestore firestore;
    private Dialog loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__create_f_r_question);


        question = findViewById(R.id.question);
        word_limit = findViewById(R.id.WordLimit);
        finish = findViewById(R.id.finish);

        firestore = FirebaseFirestore.getInstance();


        Intent intent = getIntent();


        NumOfQuestion = intent.getIntExtra("QuestionNAME",0);

        loading = new Dialog(Creator_CreateFRQuestionActivity.this);
        loading.setContentView(R.layout.loading);
        loading.setCancelable(false);
        loading.getWindow().setBackgroundDrawableResource(R.drawable.loading_background);
        loading.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                .collection("FRQuestions").document("questionList").get()
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

                    questionData.put("question", question.getText().toString());
                    questionData.put("UserAnswer","null");

                    String limit = word_limit.getEditText().getText().toString();
                    if(limit.equals("")){
                        questionData.put("WordLimit","-1");
                    }
                    else{
                        questionData.put("WordLimit", limit);
                    }



                    firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                            .collection("FRQuestions").document("question"+String.valueOf(next)).set(questionData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Map<String,Object> newList = new ArrayMap<>();

                                    newList.put("count",String.valueOf(count + 1));
                                    newList.put("NEXT", String.valueOf(next + 1));
                                    newList.put("question"+String.valueOf(count + 1)+"_id","question"+String.valueOf(next));

                                    firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                                            .collection("FRQuestions").document("questionList").update(newList)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    loading.cancel();
                                                    Toast.makeText(Creator_CreateFRQuestionActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                    Intent intent = new Intent(Creator_CreateFRQuestionActivity.this, Creator_FRQuestionsListActivity.class);
                                    startActivity(intent);
                                }
                            });
                }

                else{
                    Map<String,Object> questionData = new ArrayMap<>();

                    questionData.put("question", question.getText().toString());

                    String limit = word_limit.getEditText().getText().toString();
                    if(limit.equals("")){
                        questionData.put("WordLimit","-1");
                    }
                    else{
                        questionData.put("WordLimit", limit);
                    }


                    firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                            .collection("FRQuestions").document(FR_id_list.get(NumOfQuestion - 1)).update(questionData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loading.cancel();
                                    Intent intent = new Intent(Creator_CreateFRQuestionActivity.this, Creator_FRQuestionsListActivity.class);
                                    startActivity(intent);
                                }
                            });
                }



            }
        });

    }

    private void setInterface(){

        if(CurrentNumOfTest >= 0 && NumOfQuestion > 0){//it means we are modifying existed questions
            modifying = true;


            //get current question id

            firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                    .collection("FRQuestions").document("questionList").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if(doc.exists()){
                                    current_id = doc.getString("question" + String.valueOf(NumOfQuestion)+"_id");

                                    firestore.collection("tests").document(test_id_list.get(CurrentNumOfTest))
                                            .collection("FRQuestions").document(current_id)
                                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()){
                                                DocumentSnapshot doc = task.getResult();
                                                if(doc.exists()){
                                                    String question_text = doc.getString("question");

                                                    question.setText(question_text);

                                                    String limit = doc.getString("WordLimit");

                                                    if(limit.equals("-1")){
                                                        word_limit.getEditText().getText().clear();
                                                    }
                                                    else{
                                                        word_limit.getEditText().setText(limit);
                                                    }



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