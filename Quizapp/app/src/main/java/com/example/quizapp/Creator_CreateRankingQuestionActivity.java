package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Freezable;
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

public class Creator_CreateRankingQuestionActivity extends AppCompatActivity {

    private EditText question;
    private TextInputLayout option1, option2, option3, option4;
    private TextInputLayout Answer1, Answer2, Answer3, Answer4;
    private Button finish;

    private int count = 0;
    private FirebaseFirestore firestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__create_ranking_question);

        question = findViewById(R.id.question);

        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        Answer1 = findViewById(R.id.Answer1);
        Answer2 = findViewById(R.id.Answer2);
        Answer3 = findViewById(R.id.Answer3);
        Answer4 = findViewById(R.id.Answer4);

        finish = findViewById(R.id.finish);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("RankingQuestions").document("questionList").get()
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

                questionData.put("question",question.getText().toString());
                questionData.put("option1",option1.getEditText().getText().toString());
                questionData.put("option2",option2.getEditText().getText().toString());
                questionData.put("option3",option3.getEditText().getText().toString());
                questionData.put("option4",option4.getEditText().getText().toString());

                questionData.put("Answer1", Answer1.getEditText().getText().toString());
                questionData.put("Answer2", Answer2.getEditText().getText().toString());
                questionData.put("Answer3", Answer3.getEditText().getText().toString());
                questionData.put("Answer4", Answer4.getEditText().getText().toString());

                questionData.put("user_input1", "null");
                questionData.put("user_input2", "null");
                questionData.put("user_input3", "null");
                questionData.put("user_input4", "null");

                firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                        .collection("RankingQuestions").document("question"+String.valueOf(count + 1)).set(questionData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Map<String,Object> newList = new ArrayMap<>();

                                newList.put("count",String.valueOf(count + 1));

                                firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                                        .collection("RankingQuestions").document("questionList").update(newList)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Creator_CreateRankingQuestionActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });

                Intent intent = new Intent(Creator_CreateRankingQuestionActivity.this, Creator_CreateQuestionsActivity.class);
                startActivity(intent);
            }
        });

    }
}