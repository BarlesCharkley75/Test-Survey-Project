package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
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

import static com.example.quizapp.MainActivity.testList;

public class Creator_CreateFRQuestionActivity extends AppCompatActivity {

    private EditText question;
    private TextInputLayout word_limit;
    private Button finish;

    private int count = 0;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__create_f_r_question);


        question = findViewById(R.id.question);
        word_limit = findViewById(R.id.WordLimit);
        finish = findViewById(R.id.finish);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("FRQuestions").document("questionList").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            if(doc.exists()){
                                count = Integer.valueOf(doc.getString("count"));
                            }
                        }
                    }
                });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                        .collection("FRQuestions").document("question"+String.valueOf(count + 1)).set(questionData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Map<String,Object> newList = new ArrayMap<>();

                                newList.put("count",String.valueOf(count + 1));

                                firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                                        .collection("FRQuestions").document("questionList").update(newList)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Creator_CreateFRQuestionActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });

                Intent intent = new Intent(Creator_CreateFRQuestionActivity.this, Creator_CreateQuestionsActivity.class);
                startActivity(intent);
            }
        });


    }
}