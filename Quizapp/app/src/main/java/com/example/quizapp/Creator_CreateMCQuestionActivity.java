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

public class Creator_CreateMCQuestionActivity extends AppCompatActivity {

    private EditText question;
    private TextInputLayout option1, option2, option3, option4, Answer;

    private Button finish;

    private int count = 0;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__create_m_c_question);


        question = findViewById(R.id.question);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        Answer = findViewById(R.id.Answer);

        finish = findViewById(R.id.finish);

        firestore = FirebaseFirestore.getInstance();


        //get number of questions we already have

        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("MCQuestions").document("questionList").get()
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



        // on click, set a new question in a file, and then upload.
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> questionData = new ArrayMap<>();

                questionData.put("question",question.getText().toString());
                questionData.put("option1",option1.getEditText().getText().toString());
                questionData.put("option2",option2.getEditText().getText().toString());
                questionData.put("option3",option3.getEditText().getText().toString());
                questionData.put("option4",option4.getEditText().getText().toString());
                questionData.put("CorrectAnswer",Answer.getEditText().getText().toString());
                questionData.put("SelectedAnswer","0");


                firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                        .collection("MCQuestions").document("question"+String.valueOf(count + 1)).set(questionData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {


                                Map<String,Object> newList = new ArrayMap<>();

                                newList.put("count",String.valueOf(count + 1));

                                firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                                        .collection("MCQuestions").document("questionList").update(newList)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Creator_CreateMCQuestionActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });

                Intent intent = new Intent(Creator_CreateMCQuestionActivity.this, Creator_CreateQuestionsActivity.class);
                startActivity(intent);

            }
        });
    }
}