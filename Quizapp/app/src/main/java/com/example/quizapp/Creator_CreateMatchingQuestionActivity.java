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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import static com.example.quizapp.MainActivity.testList;

public class Creator_CreateMatchingQuestionActivity extends AppCompatActivity {

    private EditText question;

    private TextInputLayout optionL1, optionL2, optionL3, optionL4;

    private TextInputLayout optionR1, optionR2, optionR3, optionR4;

    private Button finish;

    private int count = 0;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator__create_matching_question);

        question = findViewById(R.id.question);

        optionL1 = findViewById(R.id.optionL1);
        optionL2 = findViewById(R.id.optionL2);
        optionL3 = findViewById(R.id.optionL3);
        optionL4 = findViewById(R.id.optionL4);

        optionR1 = findViewById(R.id.optionR1);
        optionR2 = findViewById(R.id.optionR2);
        optionR3 = findViewById(R.id.optionR3);
        optionR4 = findViewById(R.id.optionR4);


        finish = findViewById(R.id.finish);

        firestore = FirebaseFirestore.getInstance();

        firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                .collection("MatchingQuestions").document("questionList").get()
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
                questionData.put("optionL1",optionL1.getEditText().getText().toString());
                questionData.put("optionL2",optionL2.getEditText().getText().toString());
                questionData.put("optionL3",optionL3.getEditText().getText().toString());
                questionData.put("optionL4",optionL4.getEditText().getText().toString());
                questionData.put("optionR1",optionR1.getEditText().getText().toString());
                questionData.put("optionR2",optionR2.getEditText().getText().toString());
                questionData.put("optionR3",optionR3.getEditText().getText().toString());
                questionData.put("optionR4",optionR4.getEditText().getText().toString());

                questionData.put("L1_Answer","0");
                questionData.put("L2_Answer","0");
                questionData.put("L3_Answer","0");
                questionData.put("L4_Answer","0");

                questionData.put("L1_selected","0");
                questionData.put("L2_selected","0");
                questionData.put("L3_selected","0");
                questionData.put("L4_selected","0");


                firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                        .collection("MatchingQuestions").document("question"+String.valueOf(count + 1)).set(questionData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Map<String,Object> newList = new ArrayMap<>();

                                newList.put("count",String.valueOf(count + 1));

                                firestore.collection("tests").document("test"+String.valueOf(testList.size() + 1))
                                        .collection("MatchingQuestions").document("questionList").update(newList)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(Creator_CreateMatchingQuestionActivity.this, "succeed", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });

                Intent intent = new Intent(Creator_CreateMatchingQuestionActivity.this, Creator_CreateQuestionsActivity.class);
                startActivity(intent);

            }
        });



    }
}